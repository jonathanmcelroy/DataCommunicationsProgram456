package nudat.protocol;

public abstract class NuDatMessage {

    ////////////////////
    // Members
    ////////////////////

    protected static final int VERSION = 2;
    protected static final int VERSIONQR_INDEX = 0;
    protected static final int VERSIONQR_REQUIRED_LENGTH = VERSIONQR_INDEX + 1;
    protected static final int ERRORCODE_INDEX = 1;
    protected static final int ERRORCODE_REQUIRED_LENGTH = ERRORCODE_INDEX + 1;
    protected static final int QUERY_ID_INDEX = 2;
    protected static final int QUERY_ID_REQUIRED_LENGTH = QUERY_ID_INDEX + 4;

    public static final String CHARENCODING = "ACSII";

    protected long queryId;
    protected ErrorCode errorCode = ErrorCode.NOERROR;

    ////////////////////
    // Contructors
    ////////////////////

    ////////////////////
    // Getters/Setters
    ////////////////////

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public long getQueryId() {
        return queryId;
    }

    public void setQueryId(long queryId) {
        this.queryId = queryId;
    }

    protected void setErrorCodeFromBuffer(byte[] buffer) throws NuDatException {
        if(buffer.length < ERRORCODE_REQUIRED_LENGTH) {
            throw new NuDatException(ErrorCode.PACKETTOOSHORT);
        }

        // get the error code from the second byte
        this.errorCode = ErrorCode.getErrorCode(buffer[ERRORCODE_INDEX]);
    }

    protected void writeErrorCodeToBuffer(byte[] buffer) throws NuDatException {
        if(buffer.length < ERRORCODE_REQUIRED_LENGTH) {
            throw new NuDatException(ErrorCode.PACKETTOOSHORT);
        }

        buffer[ERRORCODE_INDEX] = (byte)(this.errorCode.getErrorCodeValue());
    }

    protected void setQueryIdFromBuffer(byte[] buffer) throws NuDatException {
        if(buffer.length < QUERY_ID_REQUIRED_LENGTH) {
            throw new NuDatException(ErrorCode.PACKETTOOSHORT);
        }

        // get the query ID, which is a big-endian unsigned integer
        this.queryId = readUnsignedInt(buffer, QUERY_ID_INDEX);
    }

    protected void writeQueryIdToBuffer(byte[] buffer) throws NuDatException {
        if(buffer.length < QUERY_ID_REQUIRED_LENGTH) {
            throw new NuDatException(ErrorCode.PACKETTOOSHORT);
        }

        // write the query ID, which is a big-endian unsigned integer
        writeUnsignedInt(this.queryId, buffer, QUERY_ID_INDEX);
    }

    ////////////////////
    // Other Methods
    ////////////////////

    public static NuDatMessage decode(byte[] buffer) throws NuDatException {
        // get the first byte, which contains the version number and the query/response bit
        byte versionQR = buffer[VERSIONQR_INDEX];

        // extract the version number and query/response bit from the first byte
        int version = (versionQR & 0b11110000) >> 4;
        int QR = (versionQR & 0b00001000) >> 3;
        int reserved = versionQR & 0b00000111;

        if(version != VERSION) {
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

    public abstract byte[] encode() throws NuDatException;

    protected final static long readUnsignedInt(byte[] buffer, int offset) {
        return ((buffer[offset]      & 0xFF) << 24) |
               ((buffer[offset + 1]  & 0xFF) << 16) |
               ((buffer[offset + 2]  & 0xFF) << 8) |
               ((buffer[offset + 3]  & 0xFF));
    }

    protected final static void writeUnsignedInt(long value, byte[] buffer, int offset) {
        buffer[offset]     = (byte)((value >> 24) & 0xFF);
        buffer[offset + 1] = (byte)((value >> 16) & 0xFF);
        buffer[offset + 2] = (byte)((value >> 8)  & 0xFF);
        buffer[offset + 3] = (byte)(value         & 0xFF);
    }

    protected final static int getUnsignedShort(byte[] buffer, int offset) {
        return ((buffer[offset]      & 0xFF) << 8) |
               ((buffer[offset + 1]  & 0xFF));
    }

    protected final static void writeUnsignedShort(int value, byte[] buffer, int offset) {
        buffer[offset]     = (byte)((value >> 8) & 0xFF);
        buffer[offset + 1] = (byte)(value        & 0xFF);
    }

    @Override
    public String toString() {
        return "NuDatMessage: queryId:" + queryId + ", ErrorCode:" + errorCode;
    }
}







