package com.wolfhouse.journeydaily.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wolfhouse.journeydaily.common.annotation.NotBlankArgs;
import com.wolfhouse.journeydaily.context.BaseContext;
import com.wolfhouse.journeydaily.mapper.DraftJourneysMapper;
import com.wolfhouse.journeydaily.pojo.entity.DraftJourneys;
import com.wolfhouse.journeydaily.service.DraftJourneysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author linexsong
 * @description 针对表【draft_journeys(暂存文章)】的数据库操作Service实现
 * @createDate 2025-04-12 16:38:43
 */
@Service
public class DraftJourneysServiceImpl extends ServiceImpl<DraftJourneysMapper, DraftJourneys>
        implements DraftJourneysService {
    private final DraftJourneysMapper draftJourneysMapper;

    @Autowired
    public DraftJourneysServiceImpl(DraftJourneysMapper draftJourneysMapper) {
        this.draftJourneysMapper = draftJourneysMapper;
    }

    @Override
    public List<Long> getByNullableId(Long userId) {
        if (userId == null) {
            return draftJourneysMapper.getAll();
        }
        Long draftId = draftJourneysMapper.getById(userId);
        if (draftId == null) {
            return Collections.emptyList();
        }
        return List.of(draftId);
    }

    @Override
    @NotBlankArgs({"journeyId"})
    public Boolean doDraft(Long journeyId) {
        return draftJourneysMapper.insertOrUpdate(new DraftJourneys(BaseContext.getUserId(), journeyId));
    }

    @Override
    @NotBlankArgs({"journeyId"})
    public Boolean removeDraft(Long journeyId) {
        return draftJourneysMapper.delete(new QueryWrapper<DraftJourneys>().lambda()
                                                                           .eq(DraftJourneys::getUserId,
                                                                               BaseContext.getUserId())
                                                                           .eq(DraftJourneys::getJourneyId,
                                                                               journeyId)) > 0;
    }

    @Override
    public Boolean isDraftExists(Long journeyId) {
        return draftJourneysMapper.exists(new QueryWrapper<DraftJourneys>().lambda()
                                                                           .eq(DraftJourneys::getUserId,
                                                                               BaseContext.getUserId())
                                                                           .eq(DraftJourneys::getJourneyId, journeyId));
    }

    @Override
    public Long getDraft() {
        return draftJourneysMapper.getById(BaseContext.getUserId());
    }
}




