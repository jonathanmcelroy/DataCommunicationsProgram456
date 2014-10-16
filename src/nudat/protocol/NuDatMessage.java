package nudat.protocol;

public abstract class NuDatMessage {

    ////////////////////
    // Members
    ////////////////////

    public static final String CHARENCODING = "ACSII";

    protected long queryId;
    protected ErrorCode errorCode = ErrorCode.NOERROR;

    ////////////////////
    // Contructors
    ////////////////////

    ////////////////////
    // Methods
    ////////////////////

    public static NuDatMessage decode(byte[] buffer) throws NuDatException {
        // get the first byte, which contains the version number and the query/response bit
        byte versionQR = buffer[0];

        // extract the version number and query/response bit from the first byte
        int version = versionQR & 0b11110000;
        int QR = versionQR & 0b00001000;
        int reserved = version & 0b00000111;

        if(version != 1) {
            throw new NuDatException(ErrorCode.BADVERSION);
        }
        if(reserved != 0) {
            throw new NuDatException(ErrorCode.NETWORKERROR);
        }

        // if query
        if(QR == 0) {
            return new NuDatQuery(buffer);
        }
        // if response
        else if(QR == 1) {
            return new NuDatResponse(buffer);
        }
        // other type
        else {
            // TODO: remove this case; it should never happen
            throw new NuDatException(ErrorCode.UNEXPECTEDPACKETTYPE);
        }
    }

    protected final static long getUnsignedInt(byte[] buffer, int offset) {
        return ((buffer[offset]    & 0xFF) << 24) |
               ((buffer[offset + 1]  & 0xFF) << 16) |
               ((buffer[offset + 2]  & 0xFF) << 8) |
               ((buffer[offset + 3]  & 0xFF));
    }

    protected final static int getUnsignedShort(byte[] buffer, int offset) {
        return ((buffer[offset]    & 0xFF) << 24) |
               ((buffer[offset + 1]  & 0xFF) << 16);
    }

    protected void setErrorCodeFromBuffer(byte[] buffer) throws NuDatException {
        if(buffer.length < 2) {
            throw new NuDatException(ErrorCode.PACKETTOOSHORT);
        }

        // get the error code from the second byte
        this.errorCode = ErrorCode.getErrorCode(buffer[1]);
    }

    protected void setQueryIdFromBuffer(byte[] buffer) throws NuDatException {
        if(buffer.length < 6) {
            throw new NuDatException(ErrorCode.PACKETTOOSHORT);
        }

        // get the query ID, which is a big-endian unsigned integer
        this.queryId = getUnsignedInt(buffer, 2);
    }

    public abstract byte[] encode();

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public long getQueryId() {
        return queryId;
    }

    public void setQueryId(long queryId) {
        this.queryId = queryId;
    }

    @Override
    public String toString() {
        // TODO: complete this function
        return "NuDatMessage";
    }
}


