package nudat.protocol;

/**
 * Exception class for the NuDat protocol
 *
 * @version 0.2
 * @author Jonathan McElroy
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
     *      the error code to associate with the exception
     */
    public NuDatException(ErrorCode errorCode){
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

    /**
     * Create an exception from an ErrorCode and a cause
     * 
     * @param errorCode
     *      the error code to associate with the ecxeption
     * @param cause
     *      the cause of the exception
     */
    public NuDatException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getErrorMessage(), cause);
        this.errorCode = errorCode;
    }

    ////////////////////
    // Methods
    ////////////////////

    /**
     * Get the error code associated with the exception
     *
     * @return the error code
     */
    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
