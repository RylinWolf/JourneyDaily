package com.wolfhouse.journeydaily.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wolfhouse.journeydaily.pojo.dto.LabelEditDto;
import com.wolfhouse.journeydaily.pojo.dto.LabelQueryDto;
import com.wolfhouse.journeydaily.pojo.entity.Label;
import com.wolfhouse.journeydaily.pojo.vo.LabelVo;

import java.util.List;

/**
 * @author linexsong
 */
public interface JourneyLabelService extends IService<Label> {
    /**
     * 添加标签业务方法。若标签已存在则将用户与标签进行绑定，否则添加标签并绑定
     *
     * @param dto 标签数据传输对象，包括与日记的关联信息。
     * @return 是否添加成功
     */
    LabelVo addLabel(LabelEditDto dto);

    /**
     * 删除标签信息。
     *
     * @param dto 标签数据传输对象，包括与日记的关联信息。
     * @return 是否删除成功
     */
    Boolean deleteLabel(LabelEditDto dto);

    /**
     * 更新标签信息。
     *
     * @param dto 标签数据传输对象，包含需要更新的标签信息与关联信息。
     * @return 是否更新成功
     */
    Boolean updateLabel(LabelEditDto dto);

    /**
     * 获取标签信息。
     *
     * @param dto 标签数据传输对象，包含筛选条件或关联信息。
     * @return 标签视图对象，包含详细的标签信息
     */
    List<LabelVo> getLabels(LabelQueryDto dto);


}
