package com.wolfhouse.journeydaily;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author linexsong
 */
@SpringBootApplication
@EnableTransactionManagement
public class JourneyDailyApplication {

    public static void main(String[] args) {
        SpringApplication.run(JourneyDailyApplication.class, args);
    }

}
