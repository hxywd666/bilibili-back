package com.bilibili.interceptor;

import com.bilibili.constant.AccountConstant;
import com.bilibili.constant.RedisConstant;
import com.bilibili.context.UserContext;
import com.bilibili.pojo.entity.User;
import com.bilibili.pojo.vo.LoginVO;
import com.bilibili.utils.ConvertUtils;
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
public class WebInterceptor implements HandlerInterceptor {

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
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AccountConstant.CLIENT_COOKIE_KEY) && StringUtils.hasText(cookie.getValue())) {
                    String token = cookie.getValue();
                    Object object = redisTemplate.opsForValue().get(
                            RedisConstant.CLIENT_KEY_PREFIX
                            + RedisConstant.LOGIN_REDIS_KEY
                            + token
                    );
                    LoginVO loginVO = ConvertUtils.convertObject(object, LoginVO.class);
                    if (loginVO != null) {
                        UserContext.setUserId(Long.parseLong(loginVO.getUserId()));
                    }
                    break;
                }
            }
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
