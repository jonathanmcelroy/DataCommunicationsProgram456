package nudat.protocol;

/**
 * A message for the NuDat protocol
 *
 * @version 0.2
 * @author Jonathan McElroy
 */
public abstract class NuDatMessage {

    ////////////////////
    // Members
    ////////////////////

    /**
     * Character encoding for NuDat messages
     */
    public static final String CHARENCODING = "ASCII";

    /**
     * The query each message is referring to
     */
    protected long queryId;

    /**
     * The error code associated with the message
     */
    protected ErrorCode errorCode = ErrorCode.NOERROR;

    /**
     * The current NuDat version numbers
     */
    protected static final int VERSION = 2;

    /**
     * The index of the VersionQR byte
     */
    protected static final int VERSIONQR_INDEX = 0;

    /**
     * The length of a buffer the message requires in order to read the VersionQR byte
     */
    protected static final int VERSIONQR_REQUIRED_LENGTH = VERSIONQR_INDEX + 1;

    /**
     *  The index of the ErrorCode byte
     */
    protected static final int ERRORCODE_INDEX = 1;

    /**
     * The length of a buffer the message requires in order to read the ErrorCode byte
     */
    protected static final int ERRORCODE_REQUIRED_LENGTH = ERRORCODE_INDEX + 1;

    /**
     *  The index of the Query Id bytes
     */
    protected static final int QUERY_ID_INDEX = 2;

    /**
     * The length of a buffer the message requires in order to read the Query ID bytes
     */
    protected static final int QUERY_ID_REQUIRED_LENGTH = QUERY_ID_INDEX + 4;

    /**
     * The largest value the query id can take
     */
    protected final long LARGEST_UNSIGNED_INT = (long)(Math.pow(2, 32) - 1);

    ////////////////////
    // Constructors
    ////////////////////

    ////////////////////
    // Getters/Setters
    ////////////////////

    /**
     * Get the ErrorCode associated with this message
     *
     * @return the error code
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * Get the Query ID associated with this message
     *
     * @return the queryId
     */
    public long getQueryId() {
        return queryId;
    }

    /**
     * Set the Query Id to the input
     *
     * @param queryId
     */
    public void setQueryId(long queryId) {
        // if the query fits in an unsigned, two-byte integer, save it
        if(queryId < 0 || queryId > LARGEST_UNSIGNED_INT){
            throw new IllegalArgumentException("queryId must fit into an unsigned integer");
        }
        this.queryId = queryId;
    }

    /**
     * Set the error code by parsing the buffer
     *
     * @param buffer
     */
    protected void setErrorCodeFromBuffer(byte[] buffer) throws NuDatException {
        if(buffer.length < ERRORCODE_REQUIRED_LENGTH) {
            throw new NuDatException(ErrorCode.PACKETTOOSHORT);
        }

        // get the error code from the second byte
        this.errorCode = ErrorCode.getErrorCode(buffer[ERRORCODE_INDEX]);
    }

    /**
     * Write the current error code to the buffer
     *
     * @param buffer
     */
    protected void writeErrorCodeToBuffer(byte[] buffer) throws NuDatException {
        if(buffer.length < ERRORCODE_REQUIRED_LENGTH) {
            throw new NuDatException(ErrorCode.PACKETTOOSHORT);
        }

        buffer[ERRORCODE_INDEX] = (byte)(this.errorCode.getErrorCodeValue());
    }

    /**
     * Set the Query ID by parsing the buffer
     *
     * @param buffer
     */
    protected void setQueryIdFromBuffer(byte[] buffer) throws NuDatException {
        if(buffer.length < QUERY_ID_REQUIRED_LENGTH) {
            throw new NuDatException(ErrorCode.PACKETTOOSHORT);
        }

        // get the query ID, which is a big-endian unsigned integer
        this.queryId = readUnsignedInt(buffer, QUERY_ID_INDEX);
    }

    /**
     * Write the current query ID to the buffer
     *
     * @param buffer
     */
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

    /**
     * Decode the input and produce a NuDatMessage. This will either be a Query
     * or Response
     *
     * @param buffer
     * @return the parsed NuDatMessage
     */
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

    /**
     * Encode the current attributes to a byte array
     *
     * @return the encode instance
     */
    public abstract byte[] encode() throws NuDatException;

    /**
     * Read an unsigned four-byte integer from the buffer at the given offset
     *
     * @param buffer
     * @param offset
     * @return the result of the parse
     */
    protected final static long readUnsignedInt(byte[] buffer, int offset) {
        return ((buffer[offset]      & 0xFF) << 24) |
               ((buffer[offset + 1]  & 0xFF) << 16) |
               ((buffer[offset + 2]  & 0xFF) << 8) |
               ((buffer[offset + 3]  & 0xFF));
    }

    /**
     * Write the value into the buffer as an unsigned four-byte integer.
     *
     * @param value
     * @param buffer
     * @param offset
     */
    protected final static void writeUnsignedInt(long value, byte[] buffer, int offset) {
        buffer[offset]     = (byte)((value >> 24) & 0xFF);
        buffer[offset + 1] = (byte)((value >> 16) & 0xFF);
        buffer[offset + 2] = (byte)((value >> 8)  & 0xFF);
        buffer[offset + 3] = (byte)(value         & 0xFF);
    }

    /**
     * Read an unsigned two-byte integer from the buffer at the given offset
     *
     * @param buffer
     * @param offset
     * @return the result of the parse
     */
    protected final static int readUnsignedShort(byte[] buffer, int offset) {
        return ((buffer[offset]      & 0xFF) << 8) |
               ((buffer[offset + 1]  & 0xFF));
    }

    /**
     * Write the value into the buffer as an unsigned two-byte integer.
     *
     * @param value
     * @param buffer
     * @param offset
     */
    protected final static void writeUnsignedShort(int value, byte[] buffer, int offset) {
        buffer[offset]     = (byte)((value >> 8) & 0xFF);
        buffer[offset + 1] = (byte)(value        & 0xFF);
    }

    /**
     * @return the string representation of the message
     */
    @Override
    public String toString() {
        return "NuDatMessage: queryId:" + queryId + ", ErrorCode:" + errorCode;
    }
}
