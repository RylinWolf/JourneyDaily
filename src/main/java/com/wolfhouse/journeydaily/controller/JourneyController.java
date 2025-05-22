package com.wolfhouse.journeydaily.controller;

import com.wolfhouse.journeydaily.common.constant.JourneyConstant;
import com.wolfhouse.journeydaily.common.util.BeanUtil;
import com.wolfhouse.journeydaily.common.util.Result;
import com.wolfhouse.journeydaily.pojo.dto.JourneyDto;
import com.wolfhouse.journeydaily.pojo.dto.JourneyQueryDto;
import com.wolfhouse.journeydaily.pojo.vo.JourneyBriefVo;
import com.wolfhouse.journeydaily.pojo.vo.JourneyVo;
import com.wolfhouse.journeydaily.service.DraftJourneysService;
import com.wolfhouse.journeydaily.service.JourneyService;
import com.wolfhouse.pagehelper.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author linexsong
 */
@RestController
@RequestMapping("/journeys")
@Api(tags = "日记管理")
public class JourneyController {
    private final JourneyService service;
    private final DraftJourneysService draftService;

    @Autowired
    public JourneyController(JourneyService service, DraftJourneysService draftService) {
        this.service = service;
        this.draftService = draftService;
    }

    @PostMapping("/list")
    @ApiOperation("获取日记列表")
    public Result<PageResult<JourneyBriefVo>> getJourneys(@RequestBody JourneyQueryDto queryDto) {
        return Result.success(service.getJourneys(queryDto));
    }

    @GetMapping("/{id}")
    @ApiOperation("根据 ID 获取日记")
    public Result<JourneyVo> getJourneyById(@PathVariable(value = "id") Long journeyId) {
        return Result.failedIfBlank(service.getJourneyVoById(journeyId), JourneyConstant.JOURNEY_NOT_EXIST);
    }

    @PostMapping
    @ApiOperation("发布日记")
    public Result<JourneyVo> postJourney(@RequestBody JourneyDto dto) {
        return Result.failedIfBlank(service.post(dto), JourneyConstant.POST_FAILED);
    }

    @PutMapping
    @ApiOperation("更新日记")
    public Result<JourneyVo> update(@RequestBody JourneyDto dto) {
        return Result.success(service.update(dto));
    }

    @DeleteMapping
    @ApiOperation("删除日记")
    public Result<?> delete(@RequestParam Long journeyId) {
        return service.delete(journeyId) ? Result.success() : Result.failed(JourneyConstant.JOURNEY_DELETED_FAILED);
    }

    @PutMapping("/recovery")
    @ApiOperation("恢复日记")
    public Result<JourneyVo> recovery(@RequestParam Long journeyId) {
        return Result.failedIfBlank(service.recovery(journeyId), JourneyConstant.JOURNEY_RECOVERY_FAILED);
    }


    @PostMapping("/draft")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation("暂存日记")
    public Result<?> doDraft(@RequestBody JourneyDto dto) {
        JourneyVo post = service.post(dto);
        if (BeanUtil.isBlank(post)) {
            return Result.failed(null, JourneyConstant.JOURNEY_DRAFT_FAILED);
        }
        return draftService.doDraft(post.getJourneyId()) ? Result.success() : Result.failed(
                JourneyConstant.JOURNEY_DRAFT_FAILED);
    }

    @GetMapping("/draft")
    @ApiOperation("获取暂存日记")
    public Result<JourneyVo> getDraft() {
        Long journeyId = draftService.getDraft();
        return Result.failedIfBlank(service.getJourneyVoById(journeyId), JourneyConstant.JOURNEY_DRAFT_NOT_EXISTS);
    }

}
