package cn.com.datu.springboot.fast.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * @author tianyao
 */
@Data
@ToString
@EqualsAndHashCode
public class ApiResponse<BT> {
    @ApiModelProperty(
            required = true,
            position = 0,
            value = "响应代码"
    )
    private String code;
    @ApiModelProperty(
            position = 2,
            value = "错误信息"
    )
    private String message;
    @ApiModelProperty(
            position = 3,
            value = "响应内容"
    )
    private BT body;

    public static <T> ApiResponse<T> ok(T body) {
        ApiResponse<T> resp = new ApiResponse();
        resp.setMessage("success");
        resp.setCode(HttpStatus.OK.value()+"");
        resp.setBody(body);
        return resp;
    }

    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> resp = new ApiResponse();
        resp.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()+"");
        resp.setMessage(message);
        return resp;
    }

    public static <T> ApiResponse<T> error(String message, T body) {
        ApiResponse<T> resp = new ApiResponse();
        resp.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()+"");
        resp.setMessage(message);
        resp.setBody(body);
        return resp;
    }
}
