package nudat.protocol;

/**
 * ErrorCode
 *
 * October 16, 2014
 *
 * @author Jonathan McElroy
 * @version 0.2
 */
public enum ErrorCode {
    /**
     * Indicates no error
     */
    NOERROR,

    /**
     * Indicates a message with a bad version was received
     */
    BADVERSION,

    /**
     * Indicates a message with an unexpected error code was received
     */
    UNEXPECTEDERRORCODE,

    /**
     * Indicates a message with an unexpected packet type was received
     */
    UNEXPECTEDPACKETTYPE,

    /**
     * Indicates a message with extraneous trailing bytes was received
     */
    PACKETTOOLONG,

    /**
     * Indicates a message with insufficient bytes was received
     */
    PACKETTOOSHORT,

    /**
     * Indicates some network error occurred
     */
    NETWORKERROR;

    /**
     * Create an error code by giving its error value
     *
     * @param errorCodeValue
     * @return the error code
     *
     * @throws IllegalArgumentException
     */
    public static ErrorCode getErrorCode(int errorCodeValue) throws IllegalArgumentException {
        switch(errorCodeValue) {
        case 0:
            return NOERROR;
        case 1:
            return BADVERSION;
        case 2:
            return UNEXPECTEDERRORCODE;
        case 3:
            return UNEXPECTEDPACKETTYPE;
        case 4:
            return PACKETTOOSHORT;
        case 5:
            return PACKETTOOLONG;
        case 7:
            return NETWORKERROR;
        default:
            throw new IllegalArgumentException("Unknwon error code");
        }
    }

    /**
     * Get the value cresponding to the ErrorCode
     *
     * @return the value of the error code
     */
    public int getErrorCodeValue() {
        switch(this) {
        case NOERROR:
            return 0;
        case BADVERSION:
            return 1;
        case UNEXPECTEDERRORCODE:
            return 2;
        case UNEXPECTEDPACKETTYPE:
            return 3;
        case PACKETTOOLONG:
            return 4;
        case PACKETTOOSHORT:
            return 5;
        case NETWORKERROR:
            return 7;
        default:
            return 0;
        }
    }

    /**
     * Get the error message corresponding tot he ErrorCode
     *
     * @return the error message
     */
    public String getErrorMessage() {
        switch(this) {
        case NOERROR:
            return "No error";
        case BADVERSION:
            return "Bad version";
        case UNEXPECTEDERRORCODE:
            return "Unexpected error code";
        case UNEXPECTEDPACKETTYPE:
            return "Unexpected packet type";
        case PACKETTOOLONG:
            return "Packet too long";
        case PACKETTOOSHORT:
            return "Packet too short";
        case NETWORKERROR:
            return "Network error";
        default:
            return "Unknown error";
        }
    }

}
