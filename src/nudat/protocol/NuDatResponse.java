package nudat.protocol;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * A NuDat responce to send to a NuDat client
 *
 * @version 0.2
 * @author Jonathan McElroy
 */
public class NuDatResponse extends NuDatMessage {

    ////////////////////
    // Members
    ////////////////////

    /**
     * The list of posts to respond with
     */
    private List<String> posts;

    /**
     * The index of the number of posts in the buffer
     */
    private final int POST_NUM_INDEX = 6;

    /**
     * The length of a buffer the requires requires in order to read the number of posts integer
     */
    private final int POST_NUM_REQUIRED_LENGTH = 8;

    /**
     * The index of the start of the data in the buffer
     */
    private final int DATA_START_INDEX = 8;

    /**
     * The length of a buffer the response requires in order to read the number of posts integer
     */
    private final int RESPONSE_REQUIRED_LENGTH = 10;

    /**
     * The value of the first byte in the response
     */
    private final byte RESPONSE_VERSIONQR = 0b00101000;

    /**
     * Value for a shift to shift by one byte
     */
    private final int BYTE_SHIFT = 8;

    /**
     * Mask to get the last byte
     */
    private final int BYTE_MASK = 0xFF;

    ////////////////////
    // Constructors
    ////////////////////

    /**
     * Construct a NuDatResponse by giving it the buffer to parse.
     *
     * @param buffer
     *      the buffer to build our NuDatResponse from
     *
     * @throws NuDatException
     *      when the buffer is not properly formatted
     */
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
            }
            catch(UnsupportedEncodingException e) {}

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

    /**
     * Construct a Response by giving it the values to use.
     *
     * @param errorCode
     *      the error code to remember
     * @param queryId
     *      the query id to remember
     * @param posts
     *      the list of posts to remember
     *
     * @throws IllegalArumentException
     *      when we input an invalid thing to remember
     */
    public NuDatResponse(ErrorCode errorCode, long queryId, List<String> posts) throws IllegalArgumentException {
        setErrorCode(errorCode);
        setQueryId(queryId);
        setPosts(posts);
    }

    ////////////////////
    // Getters/Setters
    ////////////////////

    /**
     * Get the list of posts
     *
     * @return the list of posts
     */
    public List<String> getPosts() {
        return this.posts;
    }

    /**
     * Set the list of posts
     *
     * @param posts
     *      is the list of posts to remember
     *
     * @throws IllegalArgumentException
     *      when given a null list of posts
     */
    public void setPosts(List<String> posts) throws IllegalArgumentException {
        // if the posts is null, throw an exception
        if(posts == null) {
            throw new IllegalArgumentException("Null list of posts given");
        }
        this.posts = posts;
    }

    /**
     * Set the error code
     *
     * @param errorCode
     *      is the error code to remember
     */
    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Set the error code by its number
     *
     * @param errorCodeValue
     *      is the value of the error code that we want to remember
     *
     * @throws IllegalArgumentException
     *      if the error code value does not correspond to and error code
     */
    public void setErrorCode(int errorCodeValue) throws IllegalArgumentException {
        this.errorCode = ErrorCode.getErrorCode(errorCodeValue);
    }

    ////////////////////
    // Methods
    ////////////////////

    /**
     * Encode the current attributes to a byte array
     *
     * @return the encode instance
     *
     * @throws NuDatException
     *      when we have an invalidly set option
     */
    @Override
    public byte[] encode() throws NuDatException {
        // this will build the string from the posts so that we can get the byte array
        StringBuilder sb = new StringBuilder();

        // for each post
        for(String post : this.posts) {
            // get the length of the post
            int length = post.length();

            // write the length as an unsigned, short
            sb.append((char)((length >> BYTE_SHIFT) & BYTE_MASK));
            sb.append((char)((length) & BYTE_MASK));

            // add the actual post after this
            sb.append(post);
        }

        // the encoded NuDatQuery to send to the server
        byte[] result = new byte[POST_NUM_REQUIRED_LENGTH + sb.length()];

        // set the version number and QR flag
        result[0] = RESPONSE_VERSIONQR;

        // write the error code and query id to the buffer
        writeErrorCodeToBuffer(result);
        writeQueryIdToBuffer(result);

        // write the requested number of posts to the buffer
        writeUnsignedShort(this.posts.size(), result, POST_NUM_INDEX);

        System.arraycopy(sb.toString().getBytes(), 0, result, DATA_START_INDEX, sb.length());

        return result;
    }

    /**
     * @return the string representation of the response
     */
    @Override
    public String toString() {
        return "NuDatResponse: queryId:" + queryId + ", errorCode:" + errorCode + ", posts:" + posts;
    }

}

