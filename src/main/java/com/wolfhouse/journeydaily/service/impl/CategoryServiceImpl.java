package com.wolfhouse.journeydaily.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wolfhouse.journeydaily.pojo.entity.Category;
import com.wolfhouse.journeydaily.service.CategoryService;
import com.wolfhouse.journeydaily.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
 * @author linexsong
 * @description 针对表【category】的数据库操作Service实现
 * @createDate 2025-01-23 16:54:09
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {

}
