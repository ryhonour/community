package com.ry.community.advice;

import com.ry.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: rongyao
 * @Description: 处理上下文中的业务异常
 * @Date: Create in 10:40 2019/8/24
 * @Version 1.0
 */
@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable ex, Model model) {
        if (ex instanceof CustomizeException) {
            model.addAttribute("message", ex.getMessage());
        } else {
            model.addAttribute("message", "服务器太热了，请稍后再访问...");
        }

        return new ModelAndView("error");
    }
}
