package com.wolfhouse.journeydaily.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wolfhouse.journeydaily.pojo.dto.JourneyDto;
import com.wolfhouse.journeydaily.pojo.dto.JourneyQueryDto;
import com.wolfhouse.journeydaily.pojo.entity.Journey;
import com.wolfhouse.journeydaily.pojo.vo.JourneyBriefVo;
import com.wolfhouse.journeydaily.pojo.vo.JourneyVo;
import com.wolfhouse.pagehelper.PageResult;

/**
 * @author linexsong
 * @description 针对表【journey】的数据库操作Service
 * @createDate 2025-01-27 11:37:38
 */
public interface JourneyService extends IService<Journey> {

    /**
     * 发布日记
     *
     * @param dto 日记 dto
     * @return 日记对象
     */
    JourneyVo post(JourneyDto dto);

    /**
     * 获取所有日记
     *
     * @param dto 查询 DTO
     * @return 日记列表
     */
    PageResult<JourneyBriefVo> getJourneys(JourneyQueryDto dto);

    /**
     * 根据 ID 获取日记
     *
     * @param journeyId 日记 ID
     * @return 日记VO
     */
    JourneyVo getJourneyVoById(Long journeyId);

    /**
     * 根据 Dto 更新日记
     *
     * @param dto 日记Dto
     * @return 日记 Vo
     */
    JourneyVo update(JourneyDto dto);

    /**
     * 根据 id 删除日记
     *
     * @param jid 日记 Id
     * @return bool
     */
    Boolean delete(Long jid);

    /**
     * 根据 id 恢复日记
     *
     * @param jid 日记 Id
     * @return 日记 Vo
     */
    JourneyVo recovery(Long jid);
}
