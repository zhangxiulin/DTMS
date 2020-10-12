package zh.shawn.project.pure.commons.exception;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/9/28 15:19
 */
public class ServiceBusinessException extends CommonException {
    public ServiceBusinessException() {
    }

    public ServiceBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceBusinessException(String message) {
        super(message);
    }

    public ServiceBusinessException(Throwable cause) {
        super(cause);
    }
}
