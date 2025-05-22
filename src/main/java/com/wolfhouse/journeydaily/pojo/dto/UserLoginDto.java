package com.wolfhouse.journeydaily.pojo.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import java.io.Serializable;

/**
 * @author linexsong
 */
@Data
public class UserLoginDto implements Serializable {
    @Email
    private String email;
    private String password;
}
