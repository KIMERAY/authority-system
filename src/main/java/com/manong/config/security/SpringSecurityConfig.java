package com.manong.config.security;

import com.manong.config.security.handler.AnonymousAuthenticationHandle;
import com.manong.config.security.handler.CustomerAccessDeniedHandle;
import com.manong.config.security.handler.LoginFailureHandle;
import com.manong.config.security.handler.LoginSuccessHandle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.servlet.AuthorizeRequestsDsl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private LoginSuccessHandle loginSuccessHandle;
    @Resource
    private LoginFailureHandle loginFailureHandle;
    @Resource
    private AnonymousAuthenticationHandle anonymousAuthenticationHandle;
    @Resource
    private CustomerAccessDeniedHandle customerAccessDeniedHandle;
    @Resource
    private CustomerUserDetailsServic customerUserDetailsServic;

    /**
     * 注入加密类
     *
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 处理登录认证
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        登录过程处理
        http.formLogin()            //表单登录
                .successHandler(loginSuccessHandle)   //认证成功处理器
                .failureHandler(loginFailureHandle)   //认证失败处理器
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //不创建Session;
                .and()
                .authorizeRequests()    //设置需要拦截的请求
                .antMatchers("/api/user/login").permitAll() //登录请求放行（不拦截）
                .anyRequest().authenticated() //其他一律请求都需要进行身份认证
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(anonymousAuthenticationHandle) //匿名无权限访问
                .accessDeniedHandler(customerAccessDeniedHandle) //认证用户无权限访问
                .and()
                .cors();    //支持跨域请求
    }

    /**
     * 配置认证处理器
     *
     * @param auth
     * @throws Exception
     */
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerUserDetailsServic).passwordEncoder(passwordEncoder());
    }
}
