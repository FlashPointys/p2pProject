package com.flashpoint.p2p.interceptor;


import com.flashpoint.p2p.common.Constant;
import com.flashpoint.p2p.model.user.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        User sessionUser= (User) request.getSession().getAttribute(Constant.SESSION_USER);

        if(sessionUser==null){
            response.sendRedirect(request.getContextPath()+"/login.jsp");
            return  false;
        }


        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
