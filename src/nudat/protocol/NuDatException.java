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

    }

    ////////////////////
    // Methods
    ////////////////////

}
