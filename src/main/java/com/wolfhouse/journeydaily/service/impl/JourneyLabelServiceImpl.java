package com.wolfhouse.journeydaily.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wolfhouse.journeydaily.common.constant.LabelConstant;
import com.wolfhouse.journeydaily.common.exceptions.ServiceException;
import com.wolfhouse.journeydaily.common.util.BeanUtil;
import com.wolfhouse.journeydaily.common.util.ServiceUtil;
import com.wolfhouse.journeydaily.mapper.LabelMapper;
import com.wolfhouse.journeydaily.pojo.dto.LabelEditDto;
import com.wolfhouse.journeydaily.pojo.dto.LabelQueryDto;
import com.wolfhouse.journeydaily.pojo.entity.Label;
import com.wolfhouse.journeydaily.pojo.vo.LabelVo;
import com.wolfhouse.journeydaily.service.JourneyLabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author linexsong
 */
@Service
@RequiredArgsConstructor
public class JourneyLabelServiceImpl extends ServiceImpl<LabelMapper, Label> implements JourneyLabelService {
    private final LabelMapper labelMapper;

    /**
     * 添加标签，同时将指定用户与该标签绑定
     *
     * @param labelDto 标签 Dto
     * @param userId   用户 ID
     */
    private Label addLabelWithJourneyUser(LabelEditDto labelDto, Long userId) {
        Label label = BeanUtil.copyProperties(labelDto, Label.class);
        labelMapper.addLabel(label);
        this.boundLabelJourneyUser(labelDto, userId);
        return label;
    }

    /**
     * 将用户与日记与指定标签绑定
     *
     * @param labelDto 标签 Dto
     * @param userId   用户 ID
     */
    private void boundLabelJourneyUser(LabelEditDto labelDto, Long userId) {
        Label label = BeanUtil.copyProperties(labelDto, Label.class);
        labelMapper.addUserLabel(userId, label.getLabelId());
        labelMapper.addJourneyLabel(labelDto.getJourneyId(), label.getLabelId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LabelVo addLabel(LabelEditDto dto) {
        // TODO 验证权限
        String labelName = dto.getName();
        Long userId = ServiceUtil.userIdOrException(ServiceException.loginRequired());

        // 标签名不能为空
        if (BeanUtil.isBlank(labelName)) {
            throw new ServiceException(LabelConstant.LABEL_NAME_REQUIRED);
        }

        // 标签已存在
        if (labelMapper.isNameExists(labelName)) {
            // 用户创建的标签已存在
            if (labelMapper.isUserLabelExists(userId, labelName)) {
                throw new ServiceException(LabelConstant.LABEL_ALREADY_EXIST);
            }
            // 用户未创建该标签，将用户与标签绑定，并将日记与标签绑定
            Label label = labelMapper.getLabelByName(labelName);
            this.boundLabelJourneyUser(dto, label.getLabelId());
            return BeanUtil.copyProperties(label, LabelVo.class);
        }

        // 标签不存在，创建标签并绑定
        Label label = this.addLabelWithJourneyUser(dto, userId);
        return BeanUtil.copyProperties(label, LabelVo.class);
    }

    @Override
    public Boolean deleteLabel(LabelEditDto dto) {
        // TODO 删除标签
        return null;
    }

    @Override
    public Boolean updateLabel(LabelEditDto dto) {
        // TODO 修改标签
        return null;
    }

    @Override
    public List<LabelVo> getLabels(LabelQueryDto dto) {
        // TODO 获取标签列表
        return null;
    }
}
