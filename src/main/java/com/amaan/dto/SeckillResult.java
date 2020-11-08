package com.amaan.dto;

/**
 * 将所有的ajax请求返回类型，全部封装成json数据
 * @author amaan
 * @date 20/10/20
 */
public class SeckillResult<T> {
    /**
     * 状态码
     * 0~9 代表成功
     * -1~-9 代表失败
     * 10~31 内部错误
     */
    private int code;
    /**
     * 明文消息
     */
    private String msg;
    /**
     * 消息实体
     */
    private T data;

    /**
     * 错误信息
     */
    private String error;

    public SeckillResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public SeckillResult(int code, String msg, String error) {
        this.code = code;
        this.msg = msg;
        this.error = error;
    }

    @Override
    public String toString() {
        return "SeckillResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", error='" + error + '\'' +
                '}';
    }
}
