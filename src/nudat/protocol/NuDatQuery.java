package nudat.protocol;

public class NuDatQuery extends NuDatMessage {
    
    ////////////////////
    // Members
    ////////////////////
    
    private int requestedPosts;

    ////////////////////
    // Contructors
    ////////////////////
    
    public NuDatQuery(byte[] buffer) throws NuDatException {
        setQueryIdFromBuffer(buffer);
        setErrorCodeFromBuffer(buffer);

        if(buffer.length < 8) {
            throw new NuDatException(ErrorCode.PACKETTOOSHORT);
        }
        else if(buffer.length > 8) {
            throw new NuDatException(ErrorCode.PACKETTOOLONG);
        }
        this.requestedPosts = getUnsignedShort(buffer, 5);
    }

    public NuDatQuery(long queryId, int requestedPosts) throws IllegalArgumentException {
        this.queryId = queryId;
        this.requestedPosts = requestedPosts;
    }

    ////////////////////
    // Methods
    ////////////////////

    @Override
    public byte[] encode() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public int getRequestedPosts() {
        return this.requestedPosts;
    }

    public void setRequestedPosts(int requestedPosts) throws IllegalArgumentException {
        if(requestedPosts <= 0) {
            throw new IllegalArgumentException("You must request at least 1 post");
        }
        this.requestedPosts = requestedPosts;
    }

    @Override
    public String toString() {
        // TODO: complete this function
        return "NuDatQeury";
    }
}
