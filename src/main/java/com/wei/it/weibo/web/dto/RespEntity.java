package com.wei.it.weibo.web.dto;

public class RespEntity {
    private int code;
    private String msg;
    private Object data;

    // 构造函数改为 private，禁止外部直接 new
    private RespEntity(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // ========== 静态工厂方法 ==========

    /**
     * 成功响应 (200)
     */
    public static RespEntity success(Object data) {
        return new RespEntity(200, "success", data);
    }

    /**
     * 成功响应 (自定义消息)
     */
    public static RespEntity success(String msg, Object data) {
        return new RespEntity(200, msg, data);
    }

    /**
     * 成功响应 (自定义状态码和消息)
     */
    public static RespEntity success(int code, String msg, Object data) {
        return new RespEntity(code, msg, data);
    }

    /**
     * 失败响应 (500)
     */
    public static RespEntity error(Object data) {
        return new RespEntity(500, "error", data);
    }

    /**
     * 失败响应 (自定义消息)
     */
    public static RespEntity error(String msg, Object data) {
        return new RespEntity(500, msg, data);
    }

    /**
     * 失败响应 (自定义状态码和消息)
     */
    public static RespEntity error(int code, String msg, Object data) {
        return new RespEntity(code, msg, data);
    }

    // ========== Getter/Setter ==========

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}