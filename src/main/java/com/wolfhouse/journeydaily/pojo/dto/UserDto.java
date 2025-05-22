package com.wolfhouse.journeydaily.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author linexsong
 */

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private String userName;
    private String email;
    private String tagline;
    @JsonProperty("password")
    private String pwdHash;
}
