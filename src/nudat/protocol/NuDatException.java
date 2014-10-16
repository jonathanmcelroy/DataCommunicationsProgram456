package nudat.protocol;

public class NuDatException extends Exception {
    ////////////////////
    // Members
    ////////////////////

    private static final long serialVersionUID = 1L;

    ErrorCode errorCode;

    ////////////////////
    // Contructors
    ////////////////////

    public NuDatException(ErrorCode errorCode){
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

    public NuDatException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    ////////////////////
    // Methods
    ////////////////////

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
