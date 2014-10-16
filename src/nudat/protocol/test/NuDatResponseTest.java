package nudat.protocol.test;

import nudat.protocol.ErrorCode;
import nudat.protocol.NuDatException;
import nudat.protocol.NuDatQuery;
import nudat.protocol.NuDatResponse;

import org.junit.Assert;
import org.junit.Test;

public class NuDatResponseTest {
    
    @Test
    public void TestNuDatResponseConstructorNullBuffer() {
        byte[] bad = null;
        try {
            new NuDatResponse(bad);
        } catch (NuDatException e) {
            Assert.assertEquals(ErrorCode.PACKETTOOSHORT, e.getErrorCode());
        }
    }

    @Test
    public void TestNuDatResponseConstructorTooShort() {
        byte[] bad = new byte[1];
        try {
            new NuDatResponse(bad);
        } catch (NuDatException e) {
            Assert.assertEquals(ErrorCode.PACKETTOOSHORT, e.getErrorCode());
        }
    }

    @Test
    public void TestNuDatResponseConstructorTooLong() {
        byte[] bad = new byte[10];
        try {
            new NuDatResponse(bad);
        } catch (NuDatException e) {
            Assert.assertEquals(ErrorCode.PACKETTOOLONG, e.getErrorCode());
        }
    }

    @Test
    public void TestNuDatResponseGetPosts() throws NuDatException {
        NuDatResponse response = new NuDatResponse(getGoodResponse());
        Assert.assertEquals(response.getRequestedPosts(), 10);
    }

    @Test
    public void TestNuDatResponseSetPosts() throws NuDatException {
        NuDatResponse response = new NuDatResponse(getGoodResponse());
        response.setRequestedPosts(20);
        Assert.assertEquals(response.getRequestedPosts(), 20);
    }

    @Test(expected=IllegalArgumentException.class)
    public void TestNuDatResponseSetBadPosts() throws NuDatException {
        NuDatResponse response = new NuDatResponse(getGoodResponse());
        response.setRequestedPosts(-1);
    }

    @Test
    public void TestNuDatResponseEncode() throws NuDatException {
        byte[] good = getGoodResponse();
        NuDatResponse response = new NuDatResponse(good);
        byte[] testing = response.encode();

        Assert.assertArrayEquals(good, testing);
    }
    
    @Test
    public void TestNuDatResponseToString() throws NuDatException {
        NuDatResponse response = new NuDatResponse(getGoodResponse());
        Assert.assertEquals("NuDatResponse: ResponseId:20, errorCode:NOERROR, requestedPosts:10", response.toString());
    }
    
    private static byte[] getGoodResponse() {
        byte[] good = new byte[8];       
        // Version = 0010, RQ = 0, Reserved = 0
        good[0] = 0b00100000;
        
        // ErrorCode = 0;
        good[1] = 0;

        // QueryId = 0
        good[2] = 0;
        good[3] = 0;
        good[4] = 0;
        good[5] = 20;

        // Requested post number good[6] = 0;
        good[7] = 10;

        return good;
    }
}
