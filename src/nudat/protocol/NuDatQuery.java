package nudat.protocol

public class NuDatQuery {
    
    ////////////////////
    // Members
    ////////////////////
    
    private int requestedPosts;

    ////////////////////
    // Contructors
    ////////////////////
    
    public NuDatQuery(byte[] buffer) throws NuDatException {
        // TODO: parse buffer to create object
    }

    public NuDatQuery(long queryId, int requestedPosts) throws IllegalArgumentException {
        // TODO: do something with queryId
        this.requestedPosts = requestedPosts;
    }

    ////////////////////
    // Methods
    ////////////////////
    
    public int getRequestedPosts() {
        return this.requestedPosts;
    }

    public void setRequestedPosts(int requestedPosts) throws IllegalArgumentException {
        // TODO: throw IllegalArgumentException
        this.requestedPosts = requestedPosts;
    }

    @overrides
    public String toString() {
        // TODO: complete this function
    }
}
