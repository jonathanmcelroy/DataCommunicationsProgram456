package nudat.protocol;

/**
 * A NuDat query to send to a NuDat server
 *
 * @version 0.2
 * @author Jonathan McElroy
 */
public class NuDatQuery extends NuDatMessage {

    ////////////////////
    // Members
    ////////////////////

    /**
     * The number of posts the query is requesting to be sent
     */
    private int requestedPosts;


    /**
     * The index the requested posts is placed in the encoded buffer
     */
    private final int REQUESTED_POSTS_INDEX = 6;

    /**
     * The length of the encoded query
     */
    private final int QUERY_LENGTH = 8;

    /**
     * The length of a buffer the query requires in order to read the Requested posts
     */
    private final int REQUESTED_POSTS_REQUIRED_LENGTH = QUERY_LENGTH;

    /**
     * The value of the first byte in the query 
     */
    private final byte QUERY_VERSIONQR = 0b00100000;


    ////////////////////
    // Contructors
    ////////////////////

    /**
     * Construct a NuDatQuery by giving it the buffer to parse.
     *
     * @param buffer
     *      the buffer to build our NuDatQuery from
     */
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
        this.requestedPosts = readUnsignedShort(buffer, REQUESTED_POSTS_INDEX);
    }

    /**
     * Construct a Query by giving it the values to use
     *
     * @param queryId
     *      the query id to remember
     * @param requestedPosts
     *      the number of posts we are requesting
     *
     * @throws IllegalArgumentException
     */
    public NuDatQuery(long queryId, int requestedPosts) throws IllegalArgumentException {
        setQueryId(queryId);
        setRequestedPosts(requestedPosts);
    }

    ////////////////////
    // Getters/Setters
    ////////////////////

    /**
     * Get the number of requested posts.
     *
     * @return the number of requested posts
     */
    public int getRequestedPosts() {
        return this.requestedPosts;
    }

    /**
     * Set the number of requested posts
     *
     * @param requestedPosts
     *      the number to set the requested posts to
     */
    public void setRequestedPosts(int requestedPosts) throws IllegalArgumentException {
        if(requestedPosts <= 0) {
            throw new IllegalArgumentException("You must request at least 1 post");
        }

        this.requestedPosts = requestedPosts;
    }

    ////////////////////
    // Methods
    ////////////////////

    /**
     * Encode the current attributes to a byte array
     *
     * @return the encoded query
     */
    @Override
    public byte[] encode() throws NuDatException {
        // the encoded NuDatQuery to send to the server
        byte[] result = new byte[QUERY_LENGTH];

        // set the version number and QR flag
        result[0] = QUERY_VERSIONQR;

        // write the error code and query id to the buffer
        writeErrorCodeToBuffer(result);
        writeQueryIdToBuffer(result);

        // write the requested number of posts to the buffer
        writeUnsignedShort(this.requestedPosts, result, REQUESTED_POSTS_INDEX);

        return result;
    }

    /**
     * @return the string representation of the query
     */
    @Override
    public String toString() {
        return "NuDatQuery: queryId:" + queryId + ", errorCode:" + errorCode + ", requestedPosts:" + requestedPosts;
    }
}
