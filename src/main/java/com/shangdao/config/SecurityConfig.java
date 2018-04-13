package com.shangdao.config;

import com.shangdao.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http
                .csrf().disable().anonymous().authorities("ANONYMOUS")
                .and()
                .authorizeRequests().antMatchers("/","/login","/config/**","/mobile/**","/register","/upload","/uploads/**","/frontLogin","/test","/admin","/js/**","/browser/**","/favicon.ico")
                .permitAll()
                .and()
                .authorizeRequests().antMatchers("/front/**")
                .authenticated()
//                .and()
//                .authorizeRequests().antMatchers(HttpMethod.POST,"/rest/*")
//                .authenticated()

                .and()
                .authorizeRequests().antMatchers("/admin/**","front/**")
                .hasAuthority("ADMIN")
                .and().authorizeRequests().anyRequest().authenticated()   //拦截
                .and()
//                .exceptionHandling().accessDeniedHandler(new GlobalAccessDeniedHandler())
//                .authorizeRequests().antMatchers("/login").permitAll()
//                .and()
                .formLogin().defaultSuccessUrl("/").loginPage("/login").permitAll()
                .and()
                .rememberMe().tokenValiditySeconds(60 * 60 * 24 * 30).userDetailsService(userService())
                .and()
                .logout().permitAll();
    }

    @Bean
    public UserService userService() {
        return new UserService();
    }

//    @Override
//    public UserDetailsService userDetailsServiceBean() throws Exception {
//        return userService();
//    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return  new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService()).passwordEncoder(passwordEncoder());
    }
}