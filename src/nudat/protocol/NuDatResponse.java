package nudat.protocol

public class NuDatResponse {
    
    ////////////////////
    // Members
    ////////////////////
    
    private List<String> posts;

    ////////////////////
    // Contructors
    ////////////////////
    
    public NuDatResponce(byte[] buffer) throws NuDatException {
        // TODO: parse buffer to create object
    }

    public NuDatQuery(ErrorCode errorCode, long queryId, List<String> posts) throws IllegalArgumentException {
        // TODO: do something with queryId
        this.posts = posts;
    }

    ////////////////////
    // Methods
    ////////////////////
    
    public List<String> getPosts() {
        return this.posts;
    }

    public void setPosts(List<String> posts) throws IllegalArgumentException {
        // TODO: throw IllegalArgumentException
        this.requestedPosts = requestedPosts;
    }

    public void setErrorCode(ErrorCode errorCode) {

    }

    public void setErrorCode(int errorCodeValue) {

    }

    @overrides
    public String toString() {
        // TODO: complete this function
    }
}
