package com.manong.config.security.handler;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.manong.utils.Result;
import com.manong.utils.ResultCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 匿名用户访问无权限资源时处理器
 */
@Component
public class AnonymousAuthenticationHandler implements AuthenticationEntryPoint {
    /**
     * Commences an authentication scheme.
     * <p>
     * <code>ExceptionTranslationFilter</code> will populate the <code>HttpSession</code>
     * attribute named
     * <code>AbstractAuthenticationProcessingFilter.SPRING_SECURITY_SAVED_REQUEST_KEY</code>
     * with the requested target URL before calling this method.
     * <p>
     * Implementations should modify the headers on the <code>ServletResponse</code> as
     * necessary to commence the authentication process.
     *
     * @param request       that resulted in an <code>AuthenticationException</code>
     * @param response      so that the user agent can begin authentication
     * @param authException that caused the invocation
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        设置响应的编码格式
        response.setContentType("application/json;charset=utf-8");
//        获取输出流
        ServletOutputStream outputStream = response.getOutputStream();


//        将结果转换成JSON格式
        String result = JSON.toJSONString(Result.error().code(ResultCode.NO_LOGIN).message("匿名用户无权限访问"), SerializerFeature.DisableCircularReferenceDetect);
//        将结果保存到输出中写出
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
