package com.wolfhouse.journeydaily;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author linexsong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTest implements Serializable {
    private String name;
    private String email;
    private String password;


}
