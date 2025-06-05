package com.wolfhouse.journeydaily.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wolfhouse.journeydaily.pojo.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author linexsong
 * @description 针对表【category】的数据库操作Mapper
 * @createDate 2025-01-23 16:54:09
 * @Entity com.wolfhouse.journeydaily.pojo.entity.Category
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {


}
