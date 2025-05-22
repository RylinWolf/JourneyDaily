package com.wolfhouse.journeydaily.controller;

import com.wolfhouse.journeydaily.common.constant.UserConstant;
import com.wolfhouse.journeydaily.common.enums.ResultStatusEnum;
import com.wolfhouse.journeydaily.common.util.Result;
import com.wolfhouse.journeydaily.context.BaseContext;
import com.wolfhouse.journeydaily.pojo.dto.UserDto;
import com.wolfhouse.journeydaily.pojo.dto.UserLoginDto;
import com.wolfhouse.journeydaily.pojo.vo.UserLoginVo;
import com.wolfhouse.journeydaily.pojo.vo.UserVo;
import com.wolfhouse.journeydaily.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author linexsong
 */
@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
@Api(tags = "用户管理")
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    Result<UserLoginVo> login(@RequestBody UserLoginDto loginDto) {
        log.info("用户登录, {}, {}", loginDto.getEmail(), loginDto.getPassword());
        UserLoginVo userLoginVo = userService.login(loginDto.getEmail(), loginDto.getPassword());
        if (userLoginVo != null) {
            BaseContext.setUserId(userLoginVo.getUserId());
        }
        log.info("登录结果: {}", userLoginVo == null ? ResultStatusEnum.FAILED : userLoginVo.getToken());
        return Result.failedIfBlank(userLoginVo, UserConstant.VERIFIED_FAIL);
    }

    @PostMapping("/register")
    @ApiOperation("用户注册")
    Result<UserLoginVo> register(@RequestBody UserDto userDto) {
        log.info("用户注册, {}", userDto.getEmail());
        UserLoginVo userV = userService.register(userDto);
        return Result.failedIfBlank(userV, UserConstant.EMAIL_ALREADY_EXISTS);
    }

    @PutMapping("/modify")
    @ApiOperation("修改用户")
    Result<UserVo> modify(@RequestBody UserDto userDto) {
        log.info("用户修改, {}", userDto.getEmail());
        return Result.failedIfBlank(userService.updateById(userDto), UserConstant.ACCOUNT_NOT_EXIST);
    }

    @DeleteMapping("/deregister")
    @ApiOperation("注销用户")
    Result<?> deregister(@RequestParam String password) {
        Long userId = BaseContext.getUserId();
        log.info("用户注销, {}", userId);
        return userService.deregister(userId, password) ? Result.success() : Result.failed();
    }

    @GetMapping
    @ApiOperation("获取用户信息")
    Result<UserVo> getUserInfo() {
        return Result.failedIfBlank(userService.getCurrentUserInfo(), UserConstant.GET_USER_INFO_FAILED);
    }
}
