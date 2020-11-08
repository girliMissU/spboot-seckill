package com.amaan.exception;

/**
 * 秒杀相关的所有业务异常
 *
 * @author amaan
 * @date 20/10/20
 */
public class SeckillException extends RuntimeException {
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
