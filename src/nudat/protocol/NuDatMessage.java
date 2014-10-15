package nudat.protocol;

public class NuDatMessage {

    ////////////////////
    // Members
    ////////////////////

    public static final String CHARENCODING;

    ////////////////////
    // Contructors
    ////////////////////
    
    ////////////////////
    // Methods
    ////////////////////
    
    public static NuDatMessage decode(byte[] buffer) throws NuDatException {

    }

    public byte[] encode() {

    }

    public ErrorCode getErrorCode();

    public long getQueryId();

    public void setQueryId(long queryId);

    @overrides
    public String toString() {

    }
}
