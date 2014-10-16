package nudat.protocol;

public class NuDatQuery extends NuDatMessage {

    ////////////////////
    // Members
    ////////////////////

    private final int REQUESTED_POSTS_INDEX = 6;
    private final int QUERY_LENGTH = 8;
    private final int REQUESTED_POSTS_REQUIRED_LENGTH = QUERY_LENGTH;

    private int requestedPosts;

    ////////////////////
    // Contructors
    ////////////////////

    public NuDatQuery(byte[] buffer) throws NuDatException {
        if(buffer == null) {
            throw new NuDatException(ErrorCode.PACKETTOOSHORT);
        }

        setErrorCodeFromBuffer(buffer);
        setQueryIdFromBuffer(buffer);

        if(buffer.length < REQUESTED_POSTS_REQUIRED_LENGTH) {
            throw new NuDatException(ErrorCode.PACKETTOOSHORT);
        }
        else if(buffer.length > REQUESTED_POSTS_REQUIRED_LENGTH) {
            throw new NuDatException(ErrorCode.PACKETTOOLONG);
        }
        this.requestedPosts = getUnsignedShort(buffer, REQUESTED_POSTS_INDEX);
    }

    public NuDatQuery(long queryId, int requestedPosts) throws IllegalArgumentException {
        if(queryId < 0 || queryId > Math.pow(2, 32) - 1){
            throw new IllegalArgumentException("queryId must fit into an unsigned integer");
        }
        this.queryId = queryId;

        if(requestedPosts <= 0) {
            throw new IllegalArgumentException("You must request at least 1 post");
        }
        this.requestedPosts = requestedPosts;
    }

    ////////////////////
    // Getters/Setters
    ////////////////////

    public int getRequestedPosts() {
        return this.requestedPosts;
    }

    public void setRequestedPosts(int requestedPosts) throws IllegalArgumentException {
        if(requestedPosts <= 0) {
            throw new IllegalArgumentException("You must request at least 1 post");
        }

        this.requestedPosts = requestedPosts;
    }

    ////////////////////
    // Methods
    ////////////////////

    @Override
    public byte[] encode() throws NuDatException {
        // the encoded NuDatQuery to send to the server
        byte[] result = new byte[QUERY_LENGTH];

        // set the version number and QR flag
        result[0] = 0b00100000;

        // write the error code and query id to the buffer
        writeErrorCodeToBuffer(result);
        writeQueryIdToBuffer(result);

        // write the requested number of posts to the buffer
        writeUnsignedShort(this.requestedPosts, result, REQUESTED_POSTS_INDEX);

        return result;
    }

    @Override
    public String toString() {
        return "NuDatQuery: queryId:" + queryId + ", errorCode:" + errorCode + ", requestedPosts:" + requestedPosts;
    }
}

