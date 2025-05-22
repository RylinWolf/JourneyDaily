package com.wolfhouse.journeydaily.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wolfhouse.journeydaily.pojo.entity.DraftJourneys;

import java.util.List;

/**
 * @author linexsong
 * @description 针对表【draft_journeys(暂存文章)】的数据库操作Service
 * @createDate 2025-04-12 16:38:43
 */
public interface DraftJourneysService extends IService<DraftJourneys> {
    /**
     * 根据给定的用户 ID 查询相关的记录 ID 列表。
     *
     * @param userId 用户 ID，可为空。如果为空，则返回所有暂存文章 ID。
     * @return 一个包含记录 ID 的列表，如果未找到相关记录或 userId 为空，则返回空列表。
     */
    List<Long> getByNullableId(Long userId);


    /**
     * 将指定的日记保存为暂存状态。
     *
     * @param journeyId 日记 ID，用于标识需要暂存的旅程。
     * @return 如果操作成功返回 true，否则返回 false。
     */
    Boolean doDraft(Long journeyId);

    /**
     * 将指定文章取消暂存状态
     *
     * @param journeyId 暂存文章的 ID，用于指定位于暂存状态的特定文章。
     * @return 如果取消成功返回 true，否则返回 false。
     */
    Boolean removeDraft(Long journeyId);

    /**
     * 检查指定的日记是否存在暂存状态。
     *
     * @param journeyId 日记 ID，唯一标识需要检查的日记记录。
     * @return 如果指定的日记存在暂存状态，则返回 true；否则返回 false。
     */
    Boolean isDraftExists(Long journeyId);

    /**
     * 获取当前的暂存日记 Id。
     *
     * @return 一个包含暂存日记信息的 Journey 对象，如果没有暂存日记则返回 null。
     */
    Long getDraft();
}
