package com.example.testapplication;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 *  local unit test, splitDecode()
 */
public class SplitTest {



    protected List<Split> splitTest = new ArrayList<>();

    @Before
    public void setup(){
        Split split = new Split(23.32, 20, "2021-06-06T11:57:42.401Z", "2021-06-06T12:27:42.401Z",20);
        Split split2 = new Split(27.32, 35, "2021-06-06T12:27:42.401Z", "2021-06-06T12:47:42.401Z",20);
        Split split3 = new Split(28.32, 39, "2021-06-06T12:47:42.401Z", "2021-06-06T13:07:42.401Z",20);
        Split split4 = new Split(23.32, 25, "2021-06-06T13:07:42.401Z", "2021-06-06T13:27:42.401Z",20);

        splitTest.add(split);
        splitTest.add(split2);
        splitTest.add(split3);
        splitTest.add(split4);

    }

    @Test
    public void testParse() {

        File TEST_FILE = new File(Objects.requireNonNull(getClass().getResource("/test.gpx")).getPath());
        Split splitT = new Split();
        List<Split> parsedSplits  = splitT.decodeSplit(TEST_FILE);
        assertEquals(parsedSplits.size(), splitTest.size());

        assertEquals(parsedSplits, splitTest);

    }

}