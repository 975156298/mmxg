package com.shangdao.config;

import com.shangdao.domain.user.User;
import com.shangdao.util.CommonUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Date;
import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class SpringSecurityAuditorAware implements AuditorAware<User> {
    @Override
    public Optional<User> getCurrentAuditor() {
        User user= CommonUtil.getPrincipal();
        if(user==null){
            return Optional.empty();
        }
        return Optional.of(user);
    }
}
