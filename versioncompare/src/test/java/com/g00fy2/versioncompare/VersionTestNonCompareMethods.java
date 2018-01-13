package com.g00fy2.versioncompare;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VersionTestNonCompareMethods {

  @Test(expected = NullPointerException.class) public void constructor_isArgumentNullpointer() {
    new Version(null, true);
  }

  @Test(expected = IllegalArgumentException.class) public void constructor_isArgumentIlleagal() {
    new Version("x1.2.4", true);
  }

  @Test public void getMajor_isCorrect() throws Exception {
    assertEquals(1, new Version("1.2.3").getMajor());
    assertEquals(0, new Version("0.662.34").getMajor());
  }

  @Test public void getMinor_isCorrect() throws Exception {
    assertEquals(2, new Version("1.2.3").getMinor());
    assertEquals(124, new Version("0.124.3").getMinor());
  }

  @Test public void getPatch_isCorrect() throws Exception {
    assertEquals(3, new Version("1.2.3").getPatch());
    assertEquals(0, new Version("ü").getMinor());
  }

  @Test public void getSubversionNumber_isCorrect() throws Exception {
    assertEquals(new ArrayList<>(Arrays.asList(1, 2, 3)), new Version("1.2.3").getSubversionNumbers());
    assertEquals(new ArrayList<>(Arrays.asList(144, 22, 3, 44)),
        new Version("144.22.3.44-beta").getSubversionNumbers());
    assertEquals(new ArrayList<>(Arrays.asList(144, 22, 3, 44)),
        new Version("144.22.3.44.alpha").getSubversionNumbers());
  }

  @Test public void getSuffix_isCorrect() throws Exception {
    assertEquals("", new Version("1.65.5").getSuffix());
    assertEquals("-beta.23-4", new Version("1.65.5-beta.23-4").getSuffix());
  }

  @Test public void getOriginalString_isCorrect() throws Exception {
    assertEquals("1.65.5", new Version("1.65.5").getOriginalString());
    assertEquals("1.65.5-beta.23-4", new Version("1.65.5-beta.23-4").getOriginalString());
  }
}