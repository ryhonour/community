package com.ry.community.advice;

import com.ry.community.dto.ResultDTO;
import com.ry.community.exception.CustomizeErrorCodeImpl;
import com.ry.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: rongyao
 * @Description: 处理上下文中的业务异常
 * @Date: Create in 10:40 2019/8/24
 * @Version 1.0
 */
@ControllerAdvice
public class CustomizeExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    Object handle(Throwable ex, Model model, HttpServletRequest request) {
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {
            //返回JSON
            if (ex instanceof CustomizeException) {
                return ResultDTO.errorOf((CustomizeException) ex);
            } else {
                return ResultDTO.errorOf(CustomizeErrorCodeImpl.SYS_ERROR);
            }
        } else {
            //错误页面跳转
            if (ex instanceof CustomizeException) {
                model.addAttribute("message", ex.getMessage());
            } else {
                model.addAttribute("message", CustomizeErrorCodeImpl.SYS_ERROR);
            }
            return new ModelAndView("error");
        }
    }
}
