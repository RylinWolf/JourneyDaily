package com.wolfhouse.journeydaily.controller;

import com.wolfhouse.journeydaily.common.constant.CommonConstant;
import com.wolfhouse.journeydaily.common.constant.JourneyConstant;
import com.wolfhouse.journeydaily.common.util.BeanUtil;
import com.wolfhouse.journeydaily.common.util.Result;
import com.wolfhouse.journeydaily.mq.service.JourneyMqService;
import com.wolfhouse.journeydaily.pojo.doc.JourneyDoc;
import com.wolfhouse.journeydaily.pojo.dto.JourneyDto;
import com.wolfhouse.journeydaily.pojo.dto.JourneyEsQueryDto;
import com.wolfhouse.journeydaily.pojo.dto.JourneyQueryDto;
import com.wolfhouse.journeydaily.pojo.dto.JourneyUpdateDto;
import com.wolfhouse.journeydaily.pojo.vo.JourneyBriefVo;
import com.wolfhouse.journeydaily.pojo.vo.JourneyVo;
import com.wolfhouse.journeydaily.service.DraftJourneysService;
import com.wolfhouse.journeydaily.service.JourneyEsService;
import com.wolfhouse.journeydaily.service.JourneyService;
import com.wolfhouse.pagehelper.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author linexsong
 */
@RestController
@RequestMapping("/journeys")
@Api(tags = "日记管理")
@RequiredArgsConstructor
public class JourneyController {
    private final JourneyService service;
    private final JourneyEsService esService;
    private final JourneyMqService mqService;
    private final DraftJourneysService draftService;

    @PostMapping("/list")
    @ApiOperation("获取日记列表")
    public Result<PageResult<JourneyBriefVo>> getJourneys(@RequestBody JourneyQueryDto queryDto) {
        return Result.success(service.getJourneysBrief(queryDto));
    }

    @PostMapping("/docs")
    @ApiOperation("获取日记文档列表")
    public Result<PageResult<JourneyDoc>> getJourneyDocs(@RequestBody JourneyEsQueryDto queryDto)
            throws IOException, NoSuchFieldException, IllegalAccessException {
        return Result.success(esService.searchJourneyDocs(queryDto));
    }

    @GetMapping("/{id:\\d+}")
    @ApiOperation("根据 ID 获取日记")
    public Result<JourneyVo> getJourneyById(@PathVariable(value = "id") Long journeyId) {
        return Result.failedIfBlank(service.getJourneyVoById(journeyId), JourneyConstant.JOURNEY_NOT_EXIST);
    }

    @GetMapping("/docs/{id}")
    @ApiOperation("根据 ID 获取日记文档")
    public Result<JourneyVo> getJourneyDocById(@PathVariable(value = "id") Long journeyId) throws IOException {
        return Result.failedIfBlank(BeanUtil.copyProperties(esService.getJourneyDocById(String.valueOf(journeyId)),
                                                            JourneyVo.class), JourneyConstant.JOURNEY_NOT_EXIST);
    }

    @PostMapping
    @ApiOperation("发布日记")
    public Result<JourneyVo> postJourney(@RequestBody JourneyDto dto) {
        JourneyVo post = service.post(dto);
        if (BeanUtil.isBlank(post)) {
            return Result.failed(null, JourneyConstant.POST_FAILED);
        }
        // 发布消息
        mqService.postJourney(post);
        return Result.success(post);
    }

    @PatchMapping(consumes = {"application/" + CommonConstant.MEDIA_TYPE_PATCH})
    @ApiOperation("更新日记")
    @Transactional(rollbackFor = Exception.class)
    public Result<JourneyVo> update(@RequestBody JourneyUpdateDto dto) {
        JourneyVo vo = service.updatePatch(dto);
        mqService.updateJourney(dto.getJourneyId(), dto);
        return Result.success(vo);
    }

    @DeleteMapping
    @ApiOperation("删除日记")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> delete(@RequestParam Long journeyId) throws IOException {
        if (!service.delete(journeyId)) {
            return Result.failed(JourneyConstant.JOURNEY_DELETED_FAILED);
        }
        mqService.deleteJourney(journeyId);
        return Result.success();
    }

    @PutMapping("/recovery")
    @ApiOperation("恢复日记")
    @Transactional(rollbackFor = Exception.class)
    public Result<JourneyVo> recovery(@RequestParam Long journeyId) {
        JourneyVo vo = service.recovery(journeyId);
        if (!BeanUtil.isBlank(vo)) {
            mqService.postJourney(vo);
            return Result.success(vo);
        }
        return Result.failed(null, JourneyConstant.JOURNEY_RECOVERY_FAILED);
    }


    @PostMapping("/draft")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation("暂存日记")
    public Result<?> doDraft(@RequestBody JourneyDto dto) {
        // 若已有暂存日记，则只能修改当前暂存的日记
        Long draftJourney = draftService.getDraft();
        if (!BeanUtil.isBlank(draftJourney) && !draftJourney.equals(dto.getJourneyId())) {
            return Result.failed(JourneyConstant.JOURNEY_DRAFT_EXISTS);
        }

        // 若无暂存日记，则不可修改已有的日记
        if (BeanUtil.isBlank(draftJourney)) {
            dto.setJourneyId(null);
        }
        JourneyVo post = service.updateOrPost(dto);
        if (BeanUtil.isBlank(post)) {
            return Result.failed(null, JourneyConstant.JOURNEY_DRAFT_FAILED);
        }
        return draftService.doDraft(post.getJourneyId()) ? Result.success() :
               Result.failed(JourneyConstant.JOURNEY_DRAFT_FAILED);
    }

    @GetMapping("/draft")
    @ApiOperation("获取暂存日记")
    public Result<JourneyVo> getDraft() {
        Long journeyId = draftService.getDraft();
        return Result.failedIfBlank(service.getJourneyVoById(journeyId), JourneyConstant.JOURNEY_DRAFT_NOT_EXISTS);
    }
}
