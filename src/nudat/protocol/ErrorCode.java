package nudat.protocol

public enum ErrorCode {
    NOERROR,
    BADVERSION,
    UNEXPECTEDERRORCODE,
    UNEXPECTEDPACKETTYPE;
    PACKETTOOLONG,
    PACKETTOOSHORT,
    NETWORKERROR,

    public static ErrorCode getErrorCode(int errorCodeValue) {
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
        }
    }

    public static ErrorCode valueOf(String name) {

    }

    //public static ErrorCode[] values() {
    //}

    public int getErrorCodeValue() {
           
    }

    public String getErrorMessage() {

    }

}
