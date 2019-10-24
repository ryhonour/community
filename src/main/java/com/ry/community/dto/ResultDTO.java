package com.ry.community.dto;

import com.ry.community.exception.CustomizeCode;
import com.ry.community.exception.CustomizeException;
import lombok.Data;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 16:56 2019/8/26
 * @Version 1.0
 */
@Data
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T result;

    public static ResultDTO errorOf(CustomizeCode customizeCode) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(customizeCode.getCode());
        resultDTO.setMessage(customizeCode.getMessage());
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeException ce) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(ce.getCode());
        resultDTO.setMessage(ce.getMessage());
        return resultDTO;
    }

    public static ResultDTO okOf() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("评论成功！");
        return resultDTO;
    }

    /**
     * 使用泛型构造函数
     */
    public static <T> ResultDTO okOf(T t) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("评论成功！");
        resultDTO.setResult(t);
        return resultDTO;
    }

    public static ResultDTO okOf(CustomizeCode customizeCode) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(customizeCode.getCode());
        resultDTO.setMessage(customizeCode.getMessage());
        return resultDTO;
    }
}
