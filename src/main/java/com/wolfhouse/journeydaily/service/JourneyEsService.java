package com.wolfhouse.journeydaily.service;

import com.wolfhouse.journeydaily.pojo.doc.JourneyDoc;
import com.wolfhouse.journeydaily.pojo.dto.JourneyEsQueryDto;
import com.wolfhouse.journeydaily.pojo.dto.JourneyUpdateDto;
import com.wolfhouse.pagehelper.PageResult;

import java.io.IOException;
import java.util.List;

/**
 * 日记 elasticsearch 服务
 *
 * @author rylinsong
 */
public interface JourneyEsService {
    /**
     * 获取指定范围的日记文档
     *
     * @param from 开始索引
     * @param size 要获取的数量
     * @return 指定范围的日记文档列表
     * @throws IOException -
     */
    List<JourneyDoc> getJourneyDocs(Integer from, Integer size) throws IOException;

    /**
     * 获取全部日记文档，默认上限为 10000 条
     *
     * @return 日记文档列表
     * @throws IOException -
     */
    List<JourneyDoc> getJourneyDocs() throws IOException;

    /**
     * 根据日记文档 ID 获取
     *
     * @param journeyId 日记 ID - 等同于日记文档 ID
     * @return 日记文档对象
     * @throws IOException -
     */
    JourneyDoc getJourneyDocById(String journeyId) throws IOException;

    /**
     * 根据日记查询 DTO 进行复合查询
     *
     * @param dto 日记 ES 查询 DTO
     * @return 日记文档对象列表
     * @throws IOException            -
     * @throws NoSuchFieldException   -
     * @throws IllegalAccessException -
     */
    PageResult<JourneyDoc> searchJourneyDocs(JourneyEsQueryDto dto)
            throws IOException, NoSuchFieldException, IllegalAccessException;

    /**
     * 添加日记文档
     *
     * @param journeyDoc 日记文档对象
     * @return 是否添加成功
     * @throws IOException -
     */
    Boolean postJourneyDoc(JourneyDoc journeyDoc) throws IOException;

    /**
     * 批量添加日记文档
     *
     * @param journeyDocs 日记文档对象列表
     * @return 是否添加成功
     * @throws IOException -
     */
    Boolean postJourneyDocs(List<JourneyDoc> journeyDocs) throws IOException;

    /**
     * 根据日记 ID 删除文档
     *
     * @param journeyId 日记 ID
     * @return 是否删除成功
     * @throws IOException -
     */
    Boolean deleteJourneyById(String journeyId) throws IOException;

    /**
     * 根据日记 ID 更新文档
     *
     * @param id  日记 ID - 等同于文档 ID
     * @param dto 更新的内容 Map
     * @return 是否更新完成
     * @throws IOException -
     */
    Boolean updateJourney(Long id, JourneyUpdateDto dto) throws IOException;

    /**
     * 同步数据库数据至 ES，该方法为全量同步，既将数据库完全读取并覆盖 ES，适用于初始化 ES
     *
     * @return 是否同步完成
     * @throws IOException -
     */
    Boolean fullySyncDb() throws IOException;
}
