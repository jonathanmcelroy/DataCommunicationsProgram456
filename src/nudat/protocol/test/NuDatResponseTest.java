package nudat.protocol.test;

import java.util.ArrayList;
import java.util.List;

import nudat.protocol.ErrorCode;
import nudat.protocol.NuDatException;
import nudat.protocol.NuDatResponse;

import org.junit.Assert;
import org.junit.Test;

public class NuDatResponseTest {

    @Test
    public void TestNuDatResponseConstructorNullBuffer() {
        // try to construct a response with a null array
        byte[] bad = null;
        try {
            new NuDatResponse(bad);
        }
        catch(NuDatException e) {
            Assert.assertEquals(ErrorCode.PACKETTOOSHORT, e.getErrorCode());
        }
    }

    @Test
    public void TestNuDatResponseConstructorTooShort() {
        // try to construct a response with a short array
        byte[] bad = new byte[1];
        try {
            new NuDatResponse(bad);
        }
        catch(NuDatException e) {
            Assert.assertEquals(ErrorCode.PACKETTOOSHORT, e.getErrorCode());
        }
    }

    @Test
    public void TestNuDatResponseConstructorTooLong() {
        // try to construct a response with a long array
        byte[] bad = getGoodResponse();

        // set the number of posts to be one
        bad[7] = 1;
        try {
            new NuDatResponse(bad);
        }
        catch(NuDatException e) {
            Assert.assertEquals(ErrorCode.PACKETTOOLONG, e.getErrorCode());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNuDatResponseConstuctorBadErrorCode() throws NuDatException {
        // try to construct a response with a null error code
        List<String> list = new ArrayList<String>();
        list.add("Hello");
        new NuDatResponse(null, -1, list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNuDatResponseConstuctorBadQueryId() throws NuDatException {
        // try to construct a response with a negative query id
        List<String> list = new ArrayList<String>();
        list.add("Hello");
        new NuDatResponse(ErrorCode.NOERROR, -1, list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNuDatResponseConstuctorBadQueryId2() throws NuDatException {
        // try to construct a response with a huge query id
        List<String> list = new ArrayList<String>();
        list.add("Hello");
        new NuDatResponse(ErrorCode.NOERROR, (long)(Math.pow(2, 32) + 10), list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNuDatResponseConstuctorBadPosts() throws NuDatException {
        // try to construct a response with a null list
        List<String> list = null;
        new NuDatResponse(ErrorCode.NOERROR, 10, list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNuDatResponseConstuctorBadPosts2() throws NuDatException {
        // try to a response with an empty list
        List<String> list = new ArrayList<String>();
        new NuDatResponse(ErrorCode.NOERROR, 10, list);
    }

    @Test
    public void TestNuDatResponseGetPosts() throws NuDatException {
        // try to get the posts
        NuDatResponse response = new NuDatResponse(getGoodResponse());
        List<String> list = new ArrayList<String>();
        list.add("abc");
        list.add("Hello");
        Assert.assertEquals(response.getPosts(), list);
    }

    @Test
    public void TestNuDatResponseSetPosts() throws NuDatException {
        // try to set the posts
        NuDatResponse response = new NuDatResponse(getGoodResponse());
        List<String> list = new ArrayList<String>();
        list.add("world");
        response.setPosts(list);
        Assert.assertEquals(response.getPosts(), list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNuDatResponseSetBadPosts() throws NuDatException {
        // try to set the posts with a null list
        NuDatResponse response = new NuDatResponse(getGoodResponse());
        List<String> list = null;
        response.setPosts(list);
        Assert.assertEquals(response.getPosts(), list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNuDatResponseSetBadPosts2() throws NuDatException {
        // try to set the posts with an empty list
        NuDatResponse response = new NuDatResponse(getGoodResponse());
        response.setPosts(new ArrayList<String>());
    }

    @Test
    public void TestNuDatResponseEncode() throws NuDatException {
        // try to encode the input
        byte[] good = getGoodResponse();
        NuDatResponse response = new NuDatResponse(good);
        byte[] testing = response.encode();

        Assert.assertArrayEquals(good, testing);
    }

    @Test
    public void TestNuDatResponseToString() throws NuDatException {
        // test the string return
        NuDatResponse response = new NuDatResponse(getGoodResponse());
        Assert.assertEquals("NuDatResponse: queryId:20, errorCode:NOERROR, posts:" + response.getPosts(), response.toString());
    }

    private static byte[] getGoodResponse() {
        // get an average response
        
        byte[] good = new byte[20];
        // Version = 0010, RQ = 0, Reserved = 0
        good[0] = 0b00100000;

        // ErrorCode = 0;
        good[1] = 0;

        // QueryId = 0
        good[2] = 0;
        good[3] = 0;
        good[4] = 0;
        good[5] = 20;

        // Number of posts
        good[6] = 0;
        good[7] = 2;

        // Length of first post
        good[8] = 0;
        good[9] = 3;

        // First post
        good[10] = 'a';
        good[11] = 'b';
        good[12] = 'c';

        // Length of second post
        good[13] = 0;
        good[14] = 5;

        good[15] = 'H';
        good[16] = 'e';
        good[17] = 'l';
        good[18] = 'l';
        good[19] = 'o';

        return good;
    }
}

