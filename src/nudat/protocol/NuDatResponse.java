package nudat.protocol;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * NuDatResponse
 *
 * October 16, 2014
 *
 * @author Jonathan McElroy
 * @version 0.2
 */
public class NuDatResponse extends NuDatMessage {

    ////////////////////
    // Members
    ////////////////////

    private final int POST_NUM_INDEX = 6;
    private final int POST_NUM_REQUIRED_LENGTH = 8;
    private final int DATA_START_INDEX = 8;
    private final int RESPONSE_REQUIRED_LENGTH = 10;

    private final long LARGEST_UNSIGNED_INT = (long)(Math.pow(2, 32) - 1);

    private List<String> posts;

    ////////////////////
    // Constructors
    ////////////////////

    public NuDatResponse(byte[] buffer) throws NuDatException {
        // safety check
        if(buffer == null) {
            throw new NuDatException(ErrorCode.PACKETTOOSHORT);
        }

        // set the error code and queryID
        setErrorCodeFromBuffer(buffer);
        setQueryIdFromBuffer(buffer);

        // if the buffer is too short, stop and raise an exception
        if(buffer.length < RESPONSE_REQUIRED_LENGTH) {
            throw new NuDatException(ErrorCode.PACKETTOOSHORT);
        }

        // get the number of posts on the buffer
        int numPosts = readUnsignedShort(buffer, POST_NUM_INDEX);

        // initialize the lists of posts
        this.posts = new ArrayList<String>(numPosts);

        // initialize the index for each post
        int index = DATA_START_INDEX;

        // for each post
        for(int i = 0; i < numPosts; i++) {
            // if the buffer is not long enough to read 2 bytes, raise an exception
            if(index + 2 > buffer.length) {
                throw new NuDatException(ErrorCode.PACKETTOOSHORT);
            }

            // get the length of the post
            int postLength = readUnsignedShort(buffer, index);

            // if the buffer is not long enough to read the post, raise an exception
            if(index + 2 + postLength > buffer.length) {
                throw new NuDatException(ErrorCode.PACKETTOOSHORT);
            }

            // get post string
            String post = null;
            try {
                post = new String(buffer, index + 2, postLength, CHARENCODING);
            } catch (UnsupportedEncodingException e) {}

            // add the post to the list of posts
            this.posts.add(post);

            // move the index to the next post
            index += 2 + postLength;
        }

        // if there is still more data after the last post, then the buffer was too long
        if(buffer.length > index) {
            throw new NuDatException(ErrorCode.PACKETTOOLONG);
        }
    }

    public NuDatResponse(ErrorCode errorCode, long queryId, List<String> posts) throws IllegalArgumentException {
        // if the errorCode is not null, save it
        if(errorCode == null) {
            throw new IllegalArgumentException("ErrorCode must not be null");
        }
        this.errorCode = errorCode;

        // if the query fits in an unsigned, two-byte integer, save it
        if(queryId < 0 || queryId > LARGEST_UNSIGNED_INT) {
            throw new IllegalArgumentException("queryId must fit into an unsigned integer");
        }
        this.queryId = queryId;

        // if there is at least one post, save it
        if(posts == null || posts.size() <= 0) {
            throw new IllegalArgumentException("You must have at least 1 post");
        }
        this.posts = posts;
    }

    ////////////////////
    // Getters/Setters
    ////////////////////

    public List<String> getPosts() {
        return this.posts;
    }

    public void setPosts(List<String> posts) throws IllegalArgumentException {
        // if there is not at least one post, throw an exception
        if(posts == null || posts.size() <= 0) {
            throw new IllegalArgumentException("You must have at least 1 post");
        }
        this.posts = posts;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorCode(int errorCodeValue) throws IllegalArgumentException {
        this.errorCode = ErrorCode.getErrorCode(errorCodeValue);
    }

    ////////////////////
    // Methods
    ////////////////////

    @Override
    public byte[] encode() throws NuDatException {
        // this will build the string from the posts so that we can get the byte array
        StringBuilder sb = new StringBuilder();

        // for each post
        for(String post : this.posts) {
            // get the length of the post
            int length = post.length();
            
            // write the length as an unsigned, short
            sb.append((char)((length >> 8) & 0xFF));
            sb.append((char)((length) & 0xFF));
            
            // add the actual post after this
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

    @Override
    public String toString() {
        return "NuDatResponse: queryId:" + queryId + ", errorCode:" + errorCode + ", posts:" + posts;
    }

}
