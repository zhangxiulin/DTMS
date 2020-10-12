package zh.shawn.project.pure.comp.database.exception;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/10/1 20:19
 */
public class NoDataException extends RuntimeException {
    public NoDataException(String message) {
        super(message);
    }

    public NoDataException() {
    }
}
