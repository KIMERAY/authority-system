package com.manong.controller;

import com.manong.config.redis.RedisService;
import com.manong.utils.JwtUtils;
import com.manong.utils.Result;
import com.manong.vo.TokenVo;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/sysUser")
public class SysUserController {
    @Resource
    private RedisService redisService;
    @Resource
    private JwtUtils jwtUtils;

    /**
     * 刷新token
     *
     * @param request
     * @return
     */
    @PostMapping("/refreshToken")
    public Result refreshToken(HttpServletRequest request) {
//从header中获取前端提交的token
        String token = request.getHeader("token");
//如果header中没有token，则从参数中获取
        if (ObjectUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }
//从Spring Security上下文获取用户信息
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
//获取身份信息
        UserDetails details = (UserDetails) authentication.getPrincipal();
//重新生成token
        String reToken = "";
//验证原来的token是否合法
        if (jwtUtils.validateToken(token, details)) {
//生成新的token
            reToken = jwtUtils.refreshToken(token);
        }
//获取本次token的到期时间，交给前端做判断
        long expireTime = Jwts.parser().setSigningKey(jwtUtils.getSecret())
                .parseClaimsJws(reToken.replace("jwt_", ""))
                .getBody().getExpiration().getTime();
//清除原来的token信息
        String oldTokenKey = "token_" + token;
        redisService.del(oldTokenKey);
//存储新的token
        String newTokenKey = "token_" + reToken;
        redisService.set(newTokenKey, reToken, jwtUtils.getExpiration() / 1000);
//创建TokenVo对象
        TokenVo tokenVo = new TokenVo(expireTime, reToken);
//返回数据
        return Result.ok(tokenVo).message("token生成成功");
    }
}