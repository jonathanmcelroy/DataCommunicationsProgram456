package nudat.protocol;

import java.util.ArrayList;
import java.util.List;

public class NuDatResponse extends NuDatMessage {
    
    ////////////////////
    // Members
    ////////////////////
    
    private List<String> posts;

    ////////////////////
    // Contructors
    ////////////////////
    
    public NuDatResponse(byte[] buffer) throws NuDatException {
        setQueryIdFromBuffer(buffer);
        setErrorCodeFromBuffer(buffer);

        if(buffer.length < 8) {
            throw new NuDatException(ErrorCode.PACKETTOOSHORT);
        }

        int numPosts = getUnsignedShort(buffer, 5);
        this.posts = new ArrayList<String>(numPosts);
        for(int i=0; i < numPosts; i++) {

        }

        if(buffer.length > 8) {
            throw new NuDatException(ErrorCode.PACKETTOOLONG);
        }
    }

    public NuDatResponse(ErrorCode errorCode, long queryId, List<String> posts) throws IllegalArgumentException {
        // TODO: do something with queryId
        this.posts = posts;
    }

    ////////////////////
    // Methods
    ////////////////////

    @Override
    public byte[] encode() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public List<String> getPosts() {
        return this.posts;
    }

    public void setPosts(List<String> posts) throws IllegalArgumentException {
        // TODO: throw IllegalArgumentException
        this.posts = posts;
    }

    public void setErrorCode(ErrorCode errorCode) {

    }

    public void setErrorCode(int errorCodeValue) {

    }

    @Override
    public String toString() {
        // TODO: complete this function
        return "NuDatResponse:";
    }

}
