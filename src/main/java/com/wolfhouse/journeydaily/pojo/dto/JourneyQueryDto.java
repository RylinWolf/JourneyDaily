package com.wolfhouse.journeydaily.pojo.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wolfhouse.journeydaily.common.constant.JourneyConstant;
import com.wolfhouse.journeydaily.pojo.entity.Journey;
import com.wolfhouse.pagehelper.query.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 日记分页查询 Dto
 * <p>
 * 分页参数: pageNo, pageSize
 *
 * @author linexsong
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class JourneyQueryDto extends PageQuery implements Serializable {
    protected Long authorId;
    protected Long journeyId;
    protected Long partitionId;
    protected String title;
    protected Integer visibility;
    protected String orderBy;
    protected Boolean isAsc;

    public Page<Journey> toPageOrderByDefault() {
        return this.toPage(this.orderBy, this.isAsc != null && this.isAsc);
    }

    public Page<Journey> toPageOrderByPostTime(boolean asc) {
        return this.toPage(JourneyConstant.POST_TIME_DB, asc);
    }

    @Override
    public String toString() {
        return "JourneyQueryDto{" + "authorId=" + authorId + ", journeyId=" + journeyId + ", partitionId=" +
               partitionId + ", title='" + title + '\'' + ", visibility=" + visibility + ", orderBy='" + orderBy +
               '\'' + ", isAsc=" + isAsc + '}' + "with super: " + super.toString();
    }
}
