package nudat.protocol;

import java.util.ArrayList;
import java.util.List;

public class NuDatResponse extends NuDatMessage {

    ////////////////////
    // Members
    ////////////////////

    private final int POST_NUM_INDEX = 6;
    private final int POST_NUM_REQUIRED_LENGTH = 8;
    private final int DATA_START_INDEX = 8;
    private final int RESPONSE_REQUIRED_LENGTH = 10;

    private List<String> posts;

    ////////////////////
    // Contructors
    ////////////////////

    public NuDatResponse(byte[] buffer) throws NuDatException {
        if(buffer == null) {
            throw new NuDatException(ErrorCode.PACKETTOOSHORT);
        }

        setErrorCodeFromBuffer(buffer);
        setQueryIdFromBuffer(buffer);

        if(buffer.length < RESPONSE_REQUIRED_LENGTH) {
            throw new NuDatException(ErrorCode.PACKETTOOSHORT);
        }

        int numPosts = getUnsignedShort(buffer, POST_NUM_INDEX);

        this.posts = new ArrayList<String>(numPosts);
        int index = DATA_START_INDEX;
        for(int i = 0; i < numPosts; i++) {
            if(index + 2 > buffer.length) {
                throw new NuDatException(ErrorCode.PACKETTOOSHORT);
            }
            int postLength = getUnsignedShort(buffer, index);

            if(index + 2 + postLength > buffer.length) {
                throw new NuDatException(ErrorCode.PACKETTOOSHORT);
            }
            String post = new String(buffer, index + 2, postLength);
            this.posts.add(post);
            index += 2 + postLength;
        }

        if(buffer.length > index) {
            throw new NuDatException(ErrorCode.PACKETTOOLONG);
        }
    }

    public NuDatResponse(ErrorCode errorCode, long queryId, List<String> posts) throws IllegalArgumentException {
        this.errorCode = errorCode;

        if(queryId < 0 || queryId > Math.pow(2, 32) - 1) {
            throw new IllegalArgumentException("queryId must fit into an unsigned integer");
        }
        this.queryId = queryId;

        if(posts.size() <= 0) {
            throw new IllegalArgumentException("You must have at least 1 post");
        }
        this.posts = posts;
    }

    ////////////////////
    // Methods
    ////////////////////

    @Override
    public byte[] encode() throws NuDatException {

        StringBuilder sb = new StringBuilder();
        for(String post : this.posts) {
            int length = post.length();
            // TODO: this might not work
            sb.append((short)(length & 0xFFFF));
            sb.append(post);
        }

        // the encoded NuDatQuery to send to the server
        byte[] result = new byte[POST_NUM_REQUIRED_LENGTH + sb.length()];

        // set the version number and QR flag
        result[0] = 0b00100000;

        // write the error code and query id to the buffer
        writeErrorCodeToBuffer(result);
        writeQueryIdToBuffer(result);

        // write the requested number of posts to the buffer
        writeUnsignedShort(this.posts.size(), result, POST_NUM_INDEX);

        System.arraycopy(sb.toString().getBytes(), 0, result, DATA_START_INDEX, sb.length());

        return result;
    }

    public List<String> getPosts() {
        return this.posts;
    }

    public void setPosts(List<String> posts) throws IllegalArgumentException {
        if(posts.size() <= 0) {
            throw new IllegalArgumentException("You must have at least 1 post");
        }
        this.posts = posts;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorCode(int errorCodeValue) {
        this.errorCode = ErrorCode.getErrorCode(errorCodeValue);
    }

    @Override
    public String toString() {
        return "NuDatResponse: queryId:" + queryId + ", errorCode:" + errorCode + ", posts:" + posts;
    }

}




