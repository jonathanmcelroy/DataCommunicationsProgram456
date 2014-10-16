package nudat.protocol;

/**
 * NuDatException
 *
 * October 16, 2014
 *
 * @author Jonathan McElroy
 * @version 0.2
 */
public class NuDatException extends Exception {
    ////////////////////
    // Members
    ////////////////////

    /**
     * ID to serialization
     */
    private static final long serialVersionUID = 1L;

    /**
     * The error code associated with the exception
     */
    private ErrorCode errorCode;

    ////////////////////
    // Contructors
    ////////////////////

    /**
     * Create the exception from an ErrorCode
     *
     * @param errorCode
     */
    public NuDatException(ErrorCode errorCode){
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

    /**
     * Create an exception from an ErrorCode and a cause
     *
     * @param errorCode
     * @param cause
     */
    public NuDatException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    ////////////////////
    // Methods
    ////////////////////

    /**
     * Get the error code associated with the exception
     *
     * @return
     */
    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
