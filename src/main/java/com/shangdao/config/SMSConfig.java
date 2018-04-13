package com.shangdao.config;

import com.shangdao.SMS.DemoCaptchaSMS;
import com.shangdao.SMS.SMS;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class SMSConfig {
    @Profile("dev")
    @Bean
    public SMS sms()
    {
        return new DemoCaptchaSMS();
    }
}
