package nudat.protocol.test;

import nudat.protocol.ErrorCode;
import nudat.protocol.NuDatException;
import nudat.protocol.NuDatQuery;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test the NuDat query class
 *
 * @author Jonathan McElroy
 * @version 0.2
 */
public class NuDatQueryTest {

    /**
     * Test the Query's Constructor with a null buffer
     */
    @Test
    public void TestNuDatQueryConstructorNullBuffer() {
        try {
            byte[] bad = null;
            new NuDatQuery(bad);
        }
        catch(NuDatException e) {
            Assert.assertEquals(ErrorCode.PACKETTOOSHORT, e.getErrorCode());
        }
    }

    /**
     * Test the Query's Constructor with a buffer that is too short
     */
    @Test
    public void TestNuDatQueryConstructorTooShort() {
        try {
            byte[] bad = new byte[1];
            new NuDatQuery(bad);
        }
        catch(NuDatException e) {
            Assert.assertEquals(ErrorCode.PACKETTOOSHORT, e.getErrorCode());
        }
    }

    /**
     * Test the Query's Constructor with a buffer that is too long
     */
    @Test
    public void TestNuDatQueryConstructorTooLong() {
        try {
            byte[] bad = new byte[10];
            new NuDatQuery(bad);
        }
        catch(NuDatException e) {
            Assert.assertEquals(ErrorCode.PACKETTOOLONG, e.getErrorCode());
        }
    }

    /**
     * Test the Query's Constructor with a negative query id
     */
    @Test(expected = IllegalArgumentException.class)
    public void TestNuDatQueryConstuctorBadQueryId() throws NuDatException {
        new NuDatQuery(-1, 10);
    }

    /**
     * Test the Query's Constructor with a huge query id
     */
    @Test(expected = IllegalArgumentException.class)
    public void TestNuDatQueryConstuctorBadQueryId2() throws NuDatException {
        new NuDatQuery((long)(Math.pow(2, 32) + 10), 10);
    }

    /**
     * Test the Query's Constructor with a negative number of posts requested
     */
    @Test(expected = IllegalArgumentException.class)
    public void TestNuDatQueryConstuctorBadReqestedPosts() throws NuDatException {
        new NuDatQuery(1, -1);
    }

    /**
     * Test the Query's get number of posts
     */
    @Test
    public void TestNuDatQueryGetPosts() throws NuDatException {
        NuDatQuery query = new NuDatQuery(getGoodQuery());
        Assert.assertEquals(query.getRequestedPosts(), 10);
    }

    /**
     * Test the Query's set number of posts
     */
    @Test
    public void TestNuDatQuerySetPosts() throws NuDatException {
        NuDatQuery query = new NuDatQuery(getGoodQuery());
        query.setRequestedPosts(20);
        Assert.assertEquals(query.getRequestedPosts(), 20);
    }

    /**
     * Test the Query's set number of posts with a negative number
     */
    @Test(expected = IllegalArgumentException.class)
    public void TestNuDatQuerySetBadPosts() throws NuDatException {
        NuDatQuery query = new NuDatQuery(getGoodQuery());
        query.setRequestedPosts(-1);
    }

    /**
     * Test the Query's encode
     */
    @Test
    public void TestNuDatQueryEncode() throws NuDatException {
        byte[] good = getGoodQuery();
        NuDatQuery query = new NuDatQuery(good);
        byte[] testing = query.encode();

        Assert.assertArrayEquals(good, testing);
    }

    /**
     * Test the conversion to string
     */
    @Test
    public void TestNuDatQueryToString() throws NuDatException {
        NuDatQuery query = new NuDatQuery(getGoodQuery());
        Assert.assertEquals("NuDatQuery: queryId:20, errorCode:NOERROR, requestedPosts:10", query.toString());
    }

    /**
     * Get a valid query for testing purposes
     *
     * @return a valid query
     */
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
        good[5] = 20;

        // Requested post number
        good[6] = 0;
        good[7] = 10;

        return good;
    }
}
