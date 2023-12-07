package com.manong.config.security.service;

import com.manong.entity.User;
import com.manong.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用户认证处理器类
 */
@Component
public class CustomerUserDetailsService implements UserDetailsService {
    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     * GrantedAuthority
     */

    @Resource
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        调用根据用户名查询用户信息方法
        User user = userService.findUserByUsername(username);
//        判断对象是否为空，如果对象为空，则表示登录失败
        if (user == null) {
            throw new UsernameNotFoundException("用户名错误或密码错误");
        }
//        查询成功
        return null;
    }
}
