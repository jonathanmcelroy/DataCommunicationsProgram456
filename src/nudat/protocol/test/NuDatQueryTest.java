package nudat.protocol.test;

//import static org.junit.Assert.assertEquals;

import nudat.protocol.ErrorCode;
import nudat.protocol.NuDatException;
import nudat.protocol.NuDatMessage;
import nudat.protocol.NuDatQuery;

import static org.hamcrest.CoreMatchers.instanceOf;

import org.junit.Test;
import org.junit.Assert;
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;

public class NuDatQueryTest {

    @Test
    public void TestNuDatQueryConstructor() {
        byte[] good = getGoodQuery();

        try {
            NuDatMessage.decode(good);
        } catch (NuDatException e) {
            Assert.assertTrue("Could not decode the good array", false);
        }
    }

    @Test
    public void TestNuDatQueryBadVersion() {
        byte[] bad = getGoodQuery();

        bad[0] = 0b01000000;

        try {
            NuDatMessage.decode(bad);
        } catch (NuDatException e) {
            Assert.assertEquals(ErrorCode.BADVERSION, e.getErrorCode());
        }
    }

    @Test
    public void TestNuDatQueryWrongQR() {
        byte[] bad = getGoodQuery();

        bad[0] = 0b00001000;

        NuDatMessage message;
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
