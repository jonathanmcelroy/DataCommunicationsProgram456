package nudat.protocol;

public enum ErrorCode {
    NOERROR,
    BADVERSION,
    UNEXPECTEDERRORCODE,
    UNEXPECTEDPACKETTYPE,
    PACKETTOOLONG,
    PACKETTOOSHORT,
    NETWORKERROR;

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
        default:
            return UNEXPECTEDERRORCODE;
        }
    }

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

    public String getErrorMessage() {
        switch(this) {
        case NOERROR:
            return "";
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
            return "";
        }
    }

}
