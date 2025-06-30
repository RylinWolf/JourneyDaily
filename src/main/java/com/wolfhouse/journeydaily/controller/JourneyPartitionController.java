package com.wolfhouse.journeydaily.controller;

import com.wolfhouse.journeydaily.common.constant.JourneyConstant;
import com.wolfhouse.journeydaily.common.util.BeanUtil;
import com.wolfhouse.journeydaily.common.util.Result;
import com.wolfhouse.journeydaily.mq.service.JourneyMqService;
import com.wolfhouse.journeydaily.pojo.dto.JourneyPartitionDto;
import com.wolfhouse.journeydaily.pojo.vo.JourneyPartitionBriefVo;
import com.wolfhouse.journeydaily.pojo.vo.JourneyPartitionVo;
import com.wolfhouse.journeydaily.service.JourneyPartitionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author linexsong
 */
@RestController
@RequestMapping("/jpt")
@RequiredArgsConstructor
@Api(tags = "日记分区管理")
public class JourneyPartitionController {
    private final JourneyPartitionService service;
    private final JourneyMqService journeyMqService;

    @GetMapping
    @ApiOperation("获取分区")
    public Result<List<JourneyPartitionBriefVo>> getPartitions() {
        return Result.success(BeanUtil.copyList(service.getPartitions(), JourneyPartitionBriefVo.class));
    }

    @PostMapping("/add")
    @ApiOperation("添加分区")
    public Result<JourneyPartitionVo> addPartition(@RequestBody JourneyPartitionDto dto) {
        return Result.failedIfBlank(service.addPartition(dto), JourneyConstant.PARTITION_ADD_FAILED);
    }

    @PutMapping("/edit")
    @ApiOperation("修改分区")
    public Result<JourneyPartitionVo> editPartition(@RequestBody JourneyPartitionDto dto) {
        return Result.failedIfBlank(service.updatePartition(dto), JourneyConstant.PARTITION_UPDATE_FAILED);
    }

    @DeleteMapping("/del")
    @ApiOperation("删除分区")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> deletePartition(@RequestParam Long partitionId) {
        // 获取分区，若不存在则删除失败
        JourneyPartitionVo p = service.getPartition(partitionId);
        if (BeanUtil.isBlank(p)) {
            return Result.failed(JourneyConstant.PARTITION_DELETE_FAILED);
        }
        service.deletePartition(partitionId);
        // 通知日记服务修改分区为父分区
        journeyMqService.changePartition(partitionId, p.getParent());
        return Result.success();
    }
}
