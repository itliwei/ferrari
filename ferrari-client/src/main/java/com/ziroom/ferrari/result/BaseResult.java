package com.ziroom.ferrari.result;

import com.ziroom.ferrari.enums.ResultEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by homelink on 2017/6/5 0005.
 */
@Setter
@Getter
public class BaseResult {
    //返回code
    private int code;
    //状态
    private String status;
    //返回message
    private String message;

    public BaseResult() {
    }

    public BaseResult(int code, String status,String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public BaseResult(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.status = resultEnum.getStatus();
        this.message = resultEnum.getMessage();
    }

}
