package yosnap.protocol;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Program 1
 * 
 * @author Jonathan McElroy
 * 
 */
public class MessageOutput {

    OutputStream out;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * @param out
     */
    public MessageOutput(OutputStream out) {
        this.out = out;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * @param b
     * @throws IOException
     */
    public void write(byte[] b) throws IOException {
        this.out.write(b);
    }
}
