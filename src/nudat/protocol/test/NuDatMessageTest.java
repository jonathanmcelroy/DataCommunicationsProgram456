package nudat.protocol.test;

import nudat.protocol.ErrorCode;
import nudat.protocol.NuDatException;
import nudat.protocol.NuDatMessage;
import nudat.protocol.NuDatQuery;

import static org.hamcrest.CoreMatchers.instanceOf;

import org.junit.Test;
import org.junit.Assert;

public class NuDatMessageTest {

    @Test
    public void TestNuDatMessageDecode() {
        byte[] good = getGoodQuery();

        try {
            NuDatMessage.decode(good);
        } catch (NuDatException e) {
            Assert.assertTrue("Could not decode the good array", false);
        }
    }

    @Test
    public void TestNuDatMessageDecodeBadVersion() {
        byte[] bad = getGoodQuery();

        bad[0] = 0b01000000;

        try {
            NuDatMessage.decode(bad);
        } catch (NuDatException e) {
            Assert.assertEquals(ErrorCode.BADVERSION, e.getErrorCode());
        }
    }

    @Test
    public void TestNuDatMessageDecodeWrongQR() {
        byte[] bad = getGoodQuery();

        bad[0] = 0b00001000;

        NuDatMessage message = null;
        try {
            message = NuDatMessage.decode(bad);
        } catch (NuDatException e) {
        }

        Assert.assertThat(message, instanceOf(NuDatQuery.class));

    }

    private static byte[] getGoodQuery() {
        byte[] good = new byte[8];       
        // Version = 0010, RQ = 0, Reserved = 0
        good[0] = 0b00100000;
        
        // ErrorCode = 0;
        good[1] = 0;

        // QueryId = 0
        good[2] = 0;
        good[3] = 0;
        good[4] = 0;
        good[5] = 0;

        // Requested post number
        good[6] = 0;
        good[7] = 10;

        return good;
    }
}
