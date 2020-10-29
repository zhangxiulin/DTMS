package zh.shawn.project.framework.comp.database.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zh.shawn.project.framework.commons.exception.CommonException;

public class DataOperationException extends CommonException {
    private static final Logger log = LoggerFactory.getLogger(DataOperationException.class);
    private static final long serialVersionUID = 1L;

    public DataOperationException() {
    }

    public DataOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataOperationException(String message) {
        super(message);
    }

    public DataOperationException(Throwable cause) {
        super(cause);
    }
}
