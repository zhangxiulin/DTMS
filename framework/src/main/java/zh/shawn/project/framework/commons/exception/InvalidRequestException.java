package zh.shawn.project.framework.commons.exception;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/9/28 17:08
 */
public class InvalidRequestException extends Exception {
    public InvalidRequestException() {
        super("ERROR:[非法请求.]");
    }

    public InvalidRequestException(String message, Throwable cause) {
        super("ERROR:[非法请求." + message + "]", cause);
    }

    public InvalidRequestException(String message) {
        super("ERROR:[非法请求." + message + "]");
    }
}
