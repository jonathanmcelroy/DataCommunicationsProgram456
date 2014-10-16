package nudat.protocol;

public class NuDatQuery extends NuDatMessage {
    
    ////////////////////
    // Members
    ////////////////////
    
    private final int REQUESTEDPOSTSINDEX = 6;
    
    private int requestedPosts;

    ////////////////////
    // Contructors
    ////////////////////
    
    public NuDatQuery(byte[] buffer) throws NuDatException {
        setQueryIdFromBuffer(buffer);
        setErrorCodeFromBuffer(buffer);
        if(this.errorCode != ErrorCode.NOERROR) {
            throw new NuDatException(this.errorCode);
        }

        if(buffer.length < 8) {
            throw new NuDatException(ErrorCode.PACKETTOOSHORT);
        }
        else if(buffer.length > 8) {
            throw new NuDatException(ErrorCode.PACKETTOOLONG);
        }
        this.requestedPosts = getUnsignedShort(buffer, REQUESTEDPOSTSINDEX);
    }

    public NuDatQuery(long queryId, int requestedPosts) throws IllegalArgumentException {
        this.queryId = queryId;
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
        byte[] result = new byte[8];

        // write the error code and query id to the buffer
        writeErrorCodeToBuffer(result);
        writeQueryIdToBuffer(result);

        // write the requested number of posts to the buffer
        writeUnsignedShort(this.requestedPosts, result, REQUESTEDPOSTSINDEX);

        return result;
    }
    
    @Override
    public String toString() {
        // TODO: complete this function
        return "NuDatQeury";
    }
}
