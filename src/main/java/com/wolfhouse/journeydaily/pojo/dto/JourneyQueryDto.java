package com.wolfhouse.journeydaily.pojo.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wolfhouse.journeydaily.common.constant.JourneyConstant;
import com.wolfhouse.journeydaily.pojo.entity.Journey;
import com.wolfhouse.pagehelper.query.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 日记分页查询 Dto
 * <p>
 * 分页参数: pageNo, pageSize
 *
 * @author linexsong
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JourneyQueryDto extends PageQuery implements Serializable {
    private Long authorId;
    private Long journeyId;
    private Long partitionId;
    private String title;
    private Integer visibility;
    private String orderBy;
    private Boolean isAsc;

    public Page<Journey> toPageOrderByDefault() {
        return this.toPage(this.orderBy, this.isAsc != null && this.isAsc);
    }

    public Page<Journey> toPageOrderByPostTime(boolean asc) {
        return this.toPage(JourneyConstant.POST_TIME_FIELD, asc);
    }
}
