package com.wolfhouse.journeydaily.controller;

import com.wolfhouse.journeydaily.common.constant.LabelConstant;
import com.wolfhouse.journeydaily.common.util.Result;
import com.wolfhouse.journeydaily.pojo.dto.LabelEditDto;
import com.wolfhouse.journeydaily.pojo.vo.LabelVo;
import com.wolfhouse.journeydaily.service.JourneyLabelService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author linexsong
 */
@RestController
@RequestMapping("/label")
public class LabelController {
    private final JourneyLabelService service;

    public LabelController(JourneyLabelService service) {
        this.service = service;
    }

    @PostMapping
    public Result<LabelVo> addLabel(@RequestBody LabelEditDto dto) {
        return Result.failedIfBlank(service.addLabel(dto), LabelConstant.LABEL_ADD_FAILED);
    }

}
