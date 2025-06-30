package com.wolfhouse.journeydaily.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wolfhouse.journeydaily.common.constant.JourneyConstant;
import com.wolfhouse.journeydaily.common.exceptions.ServiceException;
import com.wolfhouse.journeydaily.common.util.BeanUtil;
import com.wolfhouse.journeydaily.context.BaseContext;
import com.wolfhouse.journeydaily.mapper.JourneyPartitionMapper;
import com.wolfhouse.journeydaily.pojo.dto.JourneyPartitionDto;
import com.wolfhouse.journeydaily.pojo.entity.JourneyPartition;
import com.wolfhouse.journeydaily.pojo.vo.JourneyPartitionVo;
import com.wolfhouse.journeydaily.service.JourneyPartitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author linexsong
 * @description 针对表【journey_partition(日记分区表)】的数据库操作Service实现
 * @createDate 2025-02-21 17:28:06
 */
@Service
@RequiredArgsConstructor
public class JourneyPartitionServiceImpl extends ServiceImpl<JourneyPartitionMapper, JourneyPartition>
        implements JourneyPartitionService {
    private final JourneyPartitionMapper mapper;

    @Override
    public List<JourneyPartitionVo> getPartitions() {
        return BeanUtil.copyList(mapper.selectList(new QueryWrapper<JourneyPartition>().lambda()
                                                                                       .eq(JourneyPartition::getUserId,
                                                                                           BaseContext.getUserId())),
                                 JourneyPartitionVo.class);
    }

    /**
     * 获取存在的分区
     *
     * @param partitions 分区 Id 数组
     * @return 分区列表
     */
    @Override
    public List<JourneyPartition> hasPartitions(Long... partitions) {
        Long userId = BaseContext.getUserId();
        return userId == null ? ListUtil.empty() : mapper.hasPartitions(userId, partitions);
    }

    /**
     * 检查标题是否存在
     *
     * @param titles 标题数组
     * @return 存在的分区 Id
     */
    private List<Long> isTitleExists(String... titles) {
        return mapper.hasTitles(BaseContext.getUserId(), titles);
    }

    /**
     * 判断引入新的分区后是否存在分区循环
     *
     * @param dto 分区 Dto
     * @return bool
     */
    private Boolean isPartitionCircled(JourneyPartitionDto dto) {
        Long parent;
        // 添加新分区或未设置父级
        if (dto.getPartitionId() == null || (parent = dto.getParent()) == null) {
            return false;
        }
        List<JourneyPartitionVo> ps = getPartitions();
        // 无分区
        if (ps.isEmpty()) {
            return false;
        }
        Set<Long> idSets = new HashSet<>();
        Map<Long, Long> parentMap = new HashMap<>(ps.size());
        // 更新 id -> parent 映射
        ps.forEach(v -> parentMap.put(v.getPartitionId(), v.getParent()));
        // 更新 dto 的 parent 映射
        parentMap.put(dto.getPartitionId(), dto.getParent());

        while (parent != null) {
            // 添加父级
            if (!idSets.add(parent)) {
                // 添加失败，父级已存在，存在循环
                return true;
            }
            // 获取父级的父级
            parent = parentMap.get(parent);
        }
        // 到达顶级
        return false;
    }

    /**
     * 更新或新增检查
     *
     * @param dto 分区 dto
     */
    private void updateCheck(JourneyPartitionDto dto) {
        if (!BeanUtil.isBlank(dto.getParent()) && dto.getParent().equals(dto.getPartitionId())) {
            // 父级为自己
            throw new ServiceException(JourneyConstant.PARTITION_PARENT_CANNOT_BE_SELF);
        }
        String title = dto.getTitle();
        if (title.isBlank()) {
            // 标题为空
            throw ServiceException.fieldRequired(JourneyConstant.PARTITION_TITLE_FIELD);
        }
        if (isPartitionCircled(dto)) {
            // 分区循环
            throw new ServiceException(JourneyConstant.PARTITION_CIRCLED);
        }
        List<Long> titleIds = isTitleExists(title);
        Long pid = dto.getPartitionId();
        // pid != null, isEmpty + equals; pid == null, isEmpty

        // 作为插入方法时，标题已存在
        // 作为更新方法时，标题已存在且为当前分区
        if (!titleIds.isEmpty() && !titleIds.get(0).equals(pid)) {
            throw new ServiceException(JourneyConstant.PARTITION_TITLE_ALREADY_EXIST);
        }
        if (dto.getParent() != null && hasPartitions(dto.getParent()).isEmpty()) {
            // 父级不存在
            throw new ServiceException(JourneyConstant.PARTITION_PARENT_NOT_EXIST);
        }
    }


    private JourneyPartition insert(JourneyPartitionDto dto) {
        JourneyPartition jp = BeanUtil.copyProperties(dto, JourneyPartition.class);
        jp.setUserId(BaseContext.getUserId());
        mapper.insertWithGk(jp);
        return jp;
    }

    @Override
    public JourneyPartitionVo addPartition(JourneyPartitionDto dto) {
        // 将 ID 至空以避免带 ID 检查
        dto.setPartitionId(null);
        // 检查标题和父级
        updateCheck(dto);
        return BeanUtil.copyProperties(insert(dto), JourneyPartitionVo.class);
    }


    @Override
    public JourneyPartitionVo updatePartition(JourneyPartitionDto dto) {
        if (hasPartitions(dto.getPartitionId()).isEmpty()) {
            // 分区不存在
            throw new ServiceException(JourneyConstant.PARTITION_NOT_EXIST);
        }
        // 检查标题和父级
        updateCheck(dto);

        if (mapper.updateByDto(dto) != 1) {
            // 更新失败
            return null;
        }

        return BeanUtil.copyProperties(getById(dto.getPartitionId()), JourneyPartitionVo.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deletePartition(Long partitionId) {
        // 分区不存在
        if (hasPartitions(partitionId).isEmpty()) {
            throw new ServiceException(JourneyConstant.PARTITION_NOT_EXIST);
        }
        // 冗余防护
        Long userId = BaseContext.getUserId();
        // 修改父级
        mapper.changeParent(userId, partitionId);
        // 删除分区
        if (mapper.delete(userId, partitionId) != 1) {
            // 删除失败
            throw new ServiceException(JourneyConstant.PARTITION_DELETE_FAILED);
        }
        return true;
    }

    @Override
    public JourneyPartitionVo getPartition(Long partitionId) {
        if (hasPartitions(partitionId).isEmpty()) {
            return null;
        }
        return BeanUtil.copyProperties(getById(partitionId), JourneyPartitionVo.class);
    }
}




