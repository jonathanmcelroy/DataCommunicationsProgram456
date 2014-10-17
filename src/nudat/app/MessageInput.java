package yosnap.protocol;

import java.io.InputStream;
import java.io.IOException;

/**
 * Program 1
 * 
 * @author Jonathan McElroy
 * 
 */
public class MessageInput {

    InputStream io;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * @param io
     */
    public MessageInput(InputStream io) {
        this.io = io;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * @return
     * @throws IOException
     */
    public String readAlphaNumberics() throws IOException {
        StringBuilder sb = new StringBuilder();

        this.io.mark(2);
        int ch = this.io.read();
        while ('0' <= ch && ch <= '9' || 'a' <= ch && ch <= 'z' || 'A' <= ch
                && ch <= 'Z') {
            this.io.mark(2);
            sb.append((char) ch);
            ch = this.io.read();
        }
        this.io.reset();

        return sb.toString();
    }

    /**
     * @return
     * @throws IOException
     */
    public byte[] readBase64() throws IOException {
        StringBuilder sb = new StringBuilder();

        this.io.mark(2);
        int ch = this.io.read();
        while ('0' <= ch && ch <= '9' || 'a' <= ch && ch <= 'z' || 'A' <= ch
                && ch <= 'Z' || ch == '+' || ch == '/') {
            this.io.mark(2);
            sb.append((char) ch);
            ch = this.io.read();
        }
        this.io.reset();

        return sb.toString().getBytes();
    }

    public float readFloat() throws IOException, YoSnapException {
        try {
            return Float.parseFloat(this.readWord());
        } catch (NumberFormatException exception) {
            throw new YoSnapException("Not a floating point number", exception);
        }
    }

    /**
     * @return
     * @throws IOException
     * @throws YoSnapException
     */
    public String readHash() throws IOException, YoSnapException {
        StringBuilder sb = new StringBuilder();

        this.io.mark(3);
        int ch1 = this.io.read();
        int ch2 = this.io.read();
        while ('0' <= ch1 && ch1 <= '9' || 'A' <= ch1 && ch1 <= 'Z') {
            if (!('0' <= ch1 && ch1 <= '9' || 'A' <= ch1 && ch1 <= 'Z')) {
                throw new YoSnapException("Not an MD5 Hash");
            }
            this.io.mark(3);
            sb.append((char) ch1);
            sb.append((char) ch2);
            ch1 = this.io.read();
            ch2 = this.io.read();
        }
        this.io.reset();

        return sb.toString();
    }

    /**
     * @return
     * @throws IOException
     */
    public String readLine() throws IOException {
        StringBuilder sb = new StringBuilder();

        this.io.mark(2);
        int ch = this.io.read();
        while (ch != '\r') {
            this.io.mark(2);
            sb.append((char) ch);
            ch = this.io.read();
        }
        this.io.reset();

        return sb.toString();
    }

    /**
     * @return
     * @throws IOException
     * @throws YoSnapException
     */
    public String readNewLine() throws IOException, YoSnapException {
        int chr = this.io.read();
        int chn = this.io.read();
        if (chr != '\r' || chn != '\n') {
            throw new YoSnapException("Not the end of the message");
        }
        return "\r\n";
    }

    /**
     * @return
     * @throws IOException
     */
    public String readNumeric() throws IOException {
        StringBuilder sb = new StringBuilder();

        this.io.mark(2);
        int ch = this.io.read();
        while ('0' <= ch && ch <= '9') {
            this.io.mark(2);
            sb.append((char) ch);
            ch = this.io.read();
        }
        this.io.reset();

        return sb.toString();
    }

    /**
     * @return
     * @throws IOException
     * @throws YoSnapException
     */
    public char readSpace() throws IOException, YoSnapException {
        this.io.mark(2);
        int ch = this.io.read();
        if (ch != ' ') {
            this.io.reset();
            throw new YoSnapException("Not a space");
        }
        return ' ';
    }

    /**
     * @return
     * @throws IOException
     */
    public String readWord() throws IOException {
        StringBuilder sb = new StringBuilder();

        this.io.mark(2);
        int ch = this.io.read();
        while (!Character.isWhitespace(ch)) {
            this.io.mark(2);
            sb.append((char) ch);
            ch = this.io.read();
        }
        this.io.reset();

        return sb.toString();
    }

    /**
     * @return
     * @throws IOException
     */
    public char peek() throws IOException {
        this.io.mark(2);
        int ch = this.io.read();
        this.io.reset();
        return (char) ch;
    }

}
