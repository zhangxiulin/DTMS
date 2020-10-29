package zh.shawn.project.framework.commons.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/9/28 15:15
 */
public class CommonException extends Exception {

    private static final long serialVersionUID = 1L;
    Logger log = LoggerFactory.getLogger(CommonException.class);

    public CommonException() {
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(Throwable cause) {
        super(cause);
    }

}
