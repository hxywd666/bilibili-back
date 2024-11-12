package com.bilibili.interceptor;

import com.bilibili.constant.AccountConstant;
import com.bilibili.constant.MessageConstant;
import com.bilibili.constant.UriConstant;
import com.bilibili.exception.LoginErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AppInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler == null) {
            return false;
        }
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        if (request.getRequestURI().contains(UriConstant.URL_ACCOUNT)) {
            return true;
        }
        String token = request.getHeader(AccountConstant.ADMIN_COOKIE_KEY);
        //如果是file类型的请求 token在Cookie里而不是请求头中 这里需要额外判断
        if (request.getRequestURI().contains(UriConstant.URL_FILE)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(AccountConstant.CLIENT_COOKIE_KEY) && StringUtils.hasText(cookie.getValue())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }
        if (!StringUtils.hasText(token)) {
            throw new LoginErrorException(MessageConstant.LOGIN_TIMED_OUT);
        }
        if (redisTemplate.opsForValue().get(token) == null) {
            throw new LoginErrorException(MessageConstant.LOGIN_TIMED_OUT);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
