package com.wolfhouse.journeydaily.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wolfhouse.journeydaily.pojo.entity.Label;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * 映射接口，用于操作标签表（label）的数据库操作。
 * 提供一些自定义查询。
 *
 * @author linexsong
 */
@Mapper
public interface LabelMapper extends BaseMapper<Label> {

    /**
     * 根据标签ID查询标签信息
     *
     * @param id 标签ID
     * @return 标签
     */
    @Select("select * from `label` where label_id = #{id}")
    Label getLabelById(Long id);

    /**
     * 根据标签名称查询标签信息
     *
     * @param name 标签名称
     * @return 标签
     */
    @Select("select * from `label` where name = #{name}")
    Label getLabelByName(String name);

    /**
     * 根据标签名称判断标签是否存在。
     *
     * @param name 标签名称
     * @return 标签是否存在
     */
    @Select("select count(label_id) from label where name = #{name}")
    Boolean isNameExists(String name);

    /**
     * 根据标签名称与用户 ID 查询标签是否存在
     *
     * @param userId    用户 ID
     * @param labelName 标签名称
     * @return 指定标签是否存在
     */
    @Select("select count(l.label_id) from user_label u left join label l on u.label_id = l.label_id where user_id = #{userId} and l.name = #{labelName}")
    Boolean isUserLabelExists(Long userId, String labelName);

    /**
     * 添加一个新的标签到数据库
     *
     * @param label 标签实体对象，其中必须包含标签名称（name）字段；其主键（id）在插入完成后由数据库生成并自动填充
     */
    @Insert("insert into label(name) value (#{label.name})")
    @Options(useGeneratedKeys = true, keyProperty = "label.id")
    void addLabel(Label label);

    /**
     * 将指定标签与用户进行绑定
     *
     * @param userId  用户 ID
     * @param labelId 标签 ID
     */
    @Insert("insert into user_label(user_id, label_id) value (#{userId}, #{labelId})")
    void addUserLabel(Long userId, Long labelId);

    /**
     * 将指定标签与日记进行绑定
     *
     * @param journeyId 日记 ID
     * @param labelId   标签 ID
     */
    @Insert("insert into journey_label(journey_id, label_id) value (#{journeyId}, #{labelId})")
    void addJourneyLabel(Long journeyId, Long labelId);
}
