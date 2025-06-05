package com.wolfhouse.journeydaily.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wolfhouse.journeydaily.common.constant.CommonConstant;
import com.wolfhouse.journeydaily.common.constant.JourneyConstant;
import com.wolfhouse.journeydaily.common.exceptions.ServiceException;
import com.wolfhouse.journeydaily.common.util.BeanUtil;
import com.wolfhouse.journeydaily.common.util.ServiceUtil;
import com.wolfhouse.journeydaily.context.BaseContext;
import com.wolfhouse.journeydaily.mapper.JourneyMapper;
import com.wolfhouse.journeydaily.mapper.UserMapper;
import com.wolfhouse.journeydaily.pojo.dto.JourneyDto;
import com.wolfhouse.journeydaily.pojo.dto.JourneyQueryDto;
import com.wolfhouse.journeydaily.pojo.dto.JourneyUpdateDto;
import com.wolfhouse.journeydaily.pojo.entity.Journey;
import com.wolfhouse.journeydaily.pojo.entity.JourneyPartition;
import com.wolfhouse.journeydaily.pojo.vo.JourneyBriefVo;
import com.wolfhouse.journeydaily.pojo.vo.JourneyPartitionVo;
import com.wolfhouse.journeydaily.pojo.vo.JourneyVo;
import com.wolfhouse.journeydaily.service.AdminService;
import com.wolfhouse.journeydaily.service.DraftJourneysService;
import com.wolfhouse.journeydaily.service.JourneyPartitionService;
import com.wolfhouse.journeydaily.service.JourneyService;
import com.wolfhouse.pagehelper.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author linexsong
 * @description 针对表【journey】的数据库操作Service实现
 * @createDate 2025-01-27 11:37:38
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JourneyServiceImpl extends ServiceImpl<JourneyMapper, Journey> implements JourneyService {
    private final JourneyMapper mapper;
    private final UserMapper userMapper;
    private final AdminService adminService;
    private final JourneyPartitionService partitionService;
    private final DraftJourneysService draftJourneysService;
    @Resource(name = "nonNullObjectMapper")
    private final ObjectMapper nonNullMapper;

    /**
     * 检查日记内容是否有更新
     * <p>
     * 返回固定长度的布尔值数组，每一项分别为: 内容 content、标题 title、摘要 summary、分区 partition、可见性 visibility
     *
     * @param dto     用于比较的JourneyDto对象，包含新的数据。
     * @param journey 用于比较的Journey对象，包含原始数据。
     * @return 布尔值数组，每个布尔值表示相应字段（如内容、标题、摘要等）是否不同或满足特定条件。
     */
    private static Boolean @NotNull [] getBooleans(JourneyDto dto, Journey journey) {

        return new Boolean[]{
                !Objects.equals(journey.getContent(), dto.getContent()) && !BeanUtil.isBlank(dto.getContent()),
                !Objects.equals(journey.getTitle(), dto.getTitle()),
                !Objects.equals(journey.getSummary(), dto.getSummary()),
                !Objects.equals(journey.getPartitionId(), dto.getPartitionId()),
                !Objects.equals(journey.getVisibility(), dto.getVisibility())};
    }

    /**
     * 检查日记是否可被指定用户修改。
     * 满足以下两条件之一时返回 true：
     * 1. 指定用户为作者
     * 2. 日记权限为公开且指定用户为日记管理员
     *
     * @param journey 日记对象
     * @param userId  用户 Id
     * @return bool
     */
    private Boolean isJourneyEditable(Journey journey, Long userId) {
        return journey != null && (userId.equals(journey.getAuthorId()) ||
                                   journey.getVisibility().equals(JourneyConstant.VISIBILITY_PUBLIC) &&
                                   adminService.isJourneyAdmin(userId));
    }

    /**
     * 检查日记是否可被指定用户修改。
     * 满足以下两条件之一时返回 true：
     * 1. 指定用户为作者
     * 2. 日记权限为公开且指定用户为日记管理员
     *
     * @param jid    日记 Id
     * @param userId 用户 Id
     * @return bool
     */
    private Boolean isJourneyEditable(Long jid, Long userId) {
        return isJourneyEditable(mapper.getById(jid), userId);
    }

    /**
     * 日记是否可被指定用户访问。
     * （指定用户为作者或日记为公开权限）
     *
     * @param journey 日记对象
     * @param userId  用户 Id
     * @return bool
     */
    private Boolean isJourneyReachable(Journey journey, Long userId) {
        return BaseContext.isCurrent(userId) || journey.getVisibility().equals(JourneyConstant.VISIBILITY_PUBLIC);
    }

    @Override
    public PageResult<JourneyBriefVo> getJourneysBrief(JourneyQueryDto queryDto) {
        // 构建查询条件 顺序不得改变. 排序条件放于最后一位
        Boolean[] isQryBlk = BeanUtil.checkBlank(queryDto.getAuthorId(),
                                                 queryDto.getTitle(),
                                                 queryDto.getVisibility(),
                                                 queryDto.getPartitionId(),
                                                 queryDto.getOrderBy());

        // 获取草稿日记
        List<Long> draftJourneys = draftJourneysService.getByNullableId(BaseContext.getUserId());

        // 构建排序条件
        // 根据发布时间降序排序
        Page<Journey> toPage = isQryBlk[isQryBlk.length -
                                        1] ? queryDto.toPageOrderByPostTime(false) : queryDto.toPageOrderByDefault();

        // 日记不为草稿
        Page<Journey> page = lambdaQuery().notIn(!draftJourneys.isEmpty(), Journey::getJourneyId, draftJourneys)
                                          .eq(!isQryBlk[0], Journey::getAuthorId, queryDto.getAuthorId())
                                          // 若查询的作者非当前登录用户，则仅能查看公开权限的日记
                                          .eq(!BaseContext.isCurrent(queryDto.getAuthorId()),
                                              Journey::getVisibility,
                                              JourneyConstant.VISIBILITY_PUBLIC)
                                          .like(!isQryBlk[1], Journey::getTitle, queryDto.getTitle())
                                          .eq(!isQryBlk[2], Journey::getVisibility, queryDto.getVisibility())
                                          .eq(!isQryBlk[3], Journey::getPartitionId, queryDto.getPartitionId())
                                          .page(toPage);

        // 注入作者信息
        PageResult<JourneyBriefVo> res = PageResult.of(page, JourneyBriefVo.class);
        // 作者姓名缓存
        var author = new HashMap<Long, String>(res.getList().size());
        for (JourneyBriefVo vo : res.getList()) {
            // 若 ID 未查询过，则存入缓存
            Long id = vo.getAuthorId();
            if (!author.containsKey(id)) {
                author.put(id, userMapper.getUserName(id));
            }
            vo.setAuthor(author.get(id));
        }
        return res;
    }

    public Journey getJourneyById(Long journeyId) {
        Journey journey = mapper.selectById(journeyId);
        if (journey == null) {
            throw new ServiceException(JourneyConstant.JOURNEY_NOT_EXIST);
        }
        if (!isJourneyReachable(journey, BaseContext.getUserId())) {
            throw ServiceException.requestNotAllowed();
        }
        return journey;
    }

    @Override
    public JourneyVo getJourneyVoById(Long journeyId) {
        JourneyVo vo = BeanUtil.copyProperties(getJourneyById(journeyId), JourneyVo.class);
        vo.setAuthor(userMapper.getUserName(vo.getAuthorId()));
        JourneyPartitionVo jp;
        // 分区不存在，当前未登录或登录用户非作者
        if ((jp = partitionService.getPartition(vo.getPartitionId())) == null) {
            vo.setPartition(null);
            vo.setPartitionId(null);
            return vo;
        }
        vo.setPartition(jp.getTitle());
        return vo;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean delete(Long jid) {
        Long userId = ServiceUtil.userIdOrException();
        Journey journey = getJourneyById(jid);
        // 发布作者非当前登录用户 同时满足: 公开日记且非管理员 或 私有日记
        // 简化逻辑，可归为： 不是管理员 或 私有日记
        // 最终逻辑：发布作者非登录用户 + （不是管理员或私有日记）
        return isJourneyEditable(journey, userId) && mapper.deleteById(jid) == 1;
    }


    @Override
    public JourneyVo recovery(Long jid) {
        Long userId = BaseContext.getUserId();
        if (!isJourneyEditable(jid, userId)) {
            // 无该日记访问权限
            throw ServiceException.requestNotAllowed();
        }
        // 日记未删除 或 恢复失败
        if (lambdaQuery().eq(Journey::getJourneyId, jid).exists() ||
            mapper.recovery(jid, CommonConstant.NOT_DELETED) != 1) {
            return null;
        }
        return getJourneyVoById(jid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JourneyVo post(JourneyDto dto) {
        // 获取当前用户的 ID
        // 如果用户 ID 为空，则抛出需要登录异常
        Long userId = ServiceUtil.userIdOrException(ServiceException.loginRequired());

        // 将 JourneyDto 对象复制为 Journey 对象
        Journey journey = BeanUtil.copyProperties(dto, Journey.class);

        String content = journey.getContent();

        // 检查标题和内容字段是否为空
        if (BeanUtil.isAnyBlank(journey, journey.getTitle(), content)) {
            throw ServiceException.fieldRequired();
        }

        // 分区操作
        Long pid = journey.getPartitionId();

        // 分区不存在
        if (!BeanUtil.isBlank(pid) && getPartition(pid) == null) {
            throw new ServiceException(JourneyConstant.PARTITION_NOT_EXIST);
        }

        // 设置作者 ID 为当前用户的 ID
        journey.setAuthorId(userId);

        // 设置字数统计
        journey.setLength(StrUtil.removeAll(content, JourneyConstant.REMOVE_CONTENT).length());

        // 插入 Journey 对象并获取生成的主键
        mapper.insertWithGeneratedKeys(journey);

        Long jid = journey.getJourneyId();
        // 如果日记为暂存日记，则移出暂存
        if (draftJourneysService.isDraftExists(jid) && !draftJourneysService.removeDraft(jid)) {
            throw new ServiceException(JourneyConstant.JOURNEY_DRAFT_CANNOT_REMOVE);
        }

        // 将 Journey 对象转换为 JourneyVo 对象并返回
        return getJourneyVoById(jid);
    }

    /**
     * 获取指定日记分区
     *
     * @param pid 日记分区 ID
     * @return 日记分区对象
     */
    private JourneyPartition getPartition(Long pid) {
        List<JourneyPartition> parts = partitionService.hasPartitions(pid);
        // 分区不为空且不存在
        if (pid == null || parts.isEmpty()) {
            return null;
        }
        return parts.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JourneyVo update(JourneyDto dto) {
        // 验证是否登录
        Long userId = ServiceUtil.userIdOrException(ServiceException.loginRequired());

        Long jid = dto.getJourneyId();
        Journey journey = getJourneyById(jid);

        // 是否具备编辑权限
        if (!isJourneyEditable(jid, userId)) {
            throw ServiceException.requestNotAllowed();
        }

        // 非空判断
        if (BeanUtil.isAnyBlank(dto.getContent(), dto.getTitle())) {
            throw ServiceException.fieldRequired();
        }

        // 是否有更新, 顺序不可改变
        Boolean[] notBlanks = getBooleans(dto, journey);
        Boolean updated = BeanUtil.isAnyNotBlank((Object[]) notBlanks);
        // 无更新
        if (!updated) {
            return BeanUtil.copyProperties(journey, JourneyVo.class);
        }
        // 有更新
        if (!lambdaUpdate().eq(Journey::getJourneyId, jid)
                           // 内容
                           .set(notBlanks[0], Journey::getContent, dto.getContent())
                           // 标题
                           .set(notBlanks[1], Journey::getTitle, dto.getTitle())
                           // 摘要
                           .set(notBlanks[2], Journey::getSummary, dto.getSummary())
                           // 分区
                           .set(notBlanks[3] && !partitionService.hasPartitions(dto.getPartitionId()).isEmpty(),
                                Journey::getPartitionId,
                                dto.getPartitionId())
                           // 公开/私有权限
                           .set(notBlanks[4], Journey::getVisibility, dto.getVisibility())
                           .set(Journey::getEditTime, LocalDateTime.now())
                           .update()) {
            // 更新失败
            throw new ServiceException(JourneyConstant.UPDATE_FAILED);
        }
        return getJourneyVoById(jid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JourneyVo updatePatch(JourneyUpdateDto dto) {
        // 检查是否有修改权限
        Long userId = ServiceUtil.userIdOrException(ServiceException.loginRequired());
        Long jid = dto.getJourneyId();
        if (!isJourneyEditable(jid, userId)) {
            // 无权限
            throw ServiceException.requestNotAllowed();
        }

        // 日记内容和题目不得为空
        // 执行更新
        LambdaUpdateWrapper<Journey> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Journey::getJourneyId, jid);

        dto.getVisibility().ifPresent(v -> wrapper.set(Journey::getVisibility, v));
        dto.getTitle().ifPresent(t -> {
            // 标题为空则忽略
            if (BeanUtil.isBlank(t)) {
                return;
            }
            wrapper.set(Journey::getTitle, t);
        });
        dto.getContent().ifPresent(s -> {
            // 内容为空则忽略
            if (BeanUtil.isBlank(s)) {
                return;
            }
            wrapper.set(Journey::getContent, s);
            // 更新内容长度
            int contentLength = StrUtil.removeAll(s, JourneyConstant.REMOVE_CONTENT).length();
            wrapper.set(Journey::getLength, contentLength);
            dto.setLength(contentLength);
        });
        dto.getSummary().ifPresent(s -> wrapper.set(Journey::getSummary, s));
        dto.getPartitionId().ifPresent(p -> {
            // 分区不存在则忽略
            JourneyPartition partition = getPartition(p);
            if (partition == null) {
                return;
            }
            wrapper.set(Journey::getPartitionId, p);
            dto.setPartition(partition.getTitle());
        });

        return mapper.update(wrapper) > 0 ? getJourneyVoById(dto.getJourneyId()) : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JourneyVo updateOrPost(JourneyUpdateDto dto) {
        if (mapper.exists(new QueryWrapper<Journey>().lambda().eq(Journey::getJourneyId, dto.getJourneyId()))) {
            return updatePatch(dto);
        }

        return post(nonNullMapper.convertValue(dto, JourneyDto.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JourneyVo updateOrPost(JourneyDto dto) {
        if (mapper.exists(new QueryWrapper<Journey>().lambda().eq(Journey::getJourneyId, dto.getJourneyId()))) {
            return update(dto);
        }
        return post(dto);
    }
}
