package com.wolfhouse.journeydaily.controller;

import com.wolfhouse.journeydaily.common.constant.AdminConstant;
import com.wolfhouse.journeydaily.common.util.Result;
import com.wolfhouse.journeydaily.context.BaseContext;
import com.wolfhouse.journeydaily.pojo.dto.AdminAddDto;
import com.wolfhouse.journeydaily.pojo.dto.AdminDto;
import com.wolfhouse.journeydaily.pojo.vo.AdminVo;
import com.wolfhouse.journeydaily.pojo.vo.UserVo;
import com.wolfhouse.journeydaily.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author linexsong
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "管理员管理")
public class AdminController {
    private final AdminService service;

    @Autowired
    public AdminController(AdminService service) {
        this.service = service;
    }

    @GetMapping
    @ApiOperation("获取管理员")
    public Result<AdminVo> getAdmin() {
        return Result.failedIfBlank(service.getVoByUserId(BaseContext.getUserId()), AdminConstant.ADMIN_NOT_EXIST);
    }

    @PostMapping("/add")
    @ApiOperation("添加管理员")
    public Result<AdminVo> add(@RequestBody AdminAddDto dto) {
        return Result.failedIfBlank(service.add(dto), AdminConstant.ADMIN_ADD_FAILED_UNKNOWN);
    }

    @PutMapping("/update")
    @ApiOperation("更新管理员")
    public Result<AdminVo> update(@RequestBody AdminDto dto) {
        return Result.failedIfBlank(service.update(dto), AdminConstant.ADMIN_NOT_EXIST);
    }

    @GetMapping("/recovery/user")
    @ApiOperation("恢复用户")
    public Result<UserVo> recoveryUser(@RequestParam String userEmail, @RequestParam String password) {
        return Result.failedIfBlank(service.recoveryUser(userEmail, password), AdminConstant.USER_RECOVERY_FAILED);
    }

    @DeleteMapping("/deregister/user")
    @ApiOperation("注销用户")
    public Result<UserVo> deregisterUser(@RequestParam Long userId, @RequestParam String password) {
        return Result.failedIfBlank(service.deregisterUser(userId, password), AdminConstant.USER_DEREGISTER_FAILED);
    }

    @DeleteMapping("/deregister/admin")
    @ApiOperation("注销管理员")
    public Result<?> deregisterAdmin(@RequestParam Long userId, @RequestParam String password) {
        return service.delete(userId, password) ? Result.success() : Result.failed(
                AdminConstant.ADMIN_DEREGISTER_FAILED);
    }
}
