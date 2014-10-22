package nudat.protocol;

import java.util.Map;
import java.util.HashMap;

/**
 * Possible error codes for the NuDat protocol
 *
 * @version 0.2
 * @author Jonathan McElroy
 */
public enum ErrorCode {
    /**
     * Indicates no error
     */
    NOERROR(0, "No error"),

    /**
     * Indicates a message with a bad version was received
     */
    BADVERSION(1, "Bad version"),

    /**
     * Indicates a message with an unexpected error code was received
     */
    UNEXPECTEDERRORCODE(2, "Unexpected error code"),

    /**
     * Indicates a message with an unexpected packet type was received
     */
    UNEXPECTEDPACKETTYPE(3, "Unexpected packet type"),

    /**
     * Indicates a message with extraneous trailing bytes was received
     */
    PACKETTOOLONG(4, "Packet too long"),

    /**
     * Indicates a message with insufficient bytes was received
     */
    PACKETTOOSHORT(5, "Packet too short"),

    /**
     * Indicates some network error occurred
     */
    NETWORKERROR(7, "Network error");

    /**
     * The value of the error code
     */
    private final int errorCodeValue;

    /**
     * The error code message
     */
    private final String errorMessage;

    /**
     * A mapping from the error code value to the error code
     */
    private static final Map<Integer, ErrorCode> valueToErrorCode = new HashMap<Integer, ErrorCode>();
    static {
        for(ErrorCode code : values()) {
            valueToErrorCode.put(code.getErrorCodeValue(), code);
        }
    }

    /**
     * Create an error code given its value and message
     * 
     * @param errorCodeValue
     *      the value of the error code to create
     * @param errorMessage
     *      the message within the error code to create
     */
    private ErrorCode(int errorCodeValue, String errorMessage) {
        this.errorCodeValue = errorCodeValue;
        this.errorMessage = errorMessage;
    }

    /**
     * Create an error code by giving its error value
     *
     * @param errorCodeValue
     *      the value of the error code to find
     * @return the error code
     *
     * @throws IllegalArgumentException
     */
    public static ErrorCode getErrorCode(int errorCodeValue) throws IllegalArgumentException {
        ErrorCode code = valueToErrorCode.get(errorCodeValue);
        if(code == null) {
            throw new IllegalArgumentException("Unknwon error code");
        }
        return code;
    }

    /**
     * Get the value cresponding to the ErrorCode
     *
     * @return the value of the error code
     */
    public int getErrorCodeValue() {
        return this.errorCodeValue;
    }

    /**
     * Get the error message corresponding tot he ErrorCode
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
