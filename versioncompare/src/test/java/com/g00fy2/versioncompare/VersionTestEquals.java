package com.g00fy2.versioncompare;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class) public class VersionTestEquals {

  @Parameters public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
        { "1.2.3", "1.2.3" },
        { "1", "1.0" },
        { "2.0.0", "2" },
        { "2.4.0-beta3", "2.4.beta3" },
        { "2.4.0.0.0-beta3", "2.4.beta3" },
        { "2.4.0.0.0-beta3", "2.4.beta-03" },
        { "2.4.0.beta3", "2.4-beta3" },
        { "2.4.0.beta3a1", "2.4-beta3a2" },
        { "hasdh10uadf", "hasdh10uadf" },
        { "?ü+", "?ü+" },
        { "kasfd5", "posfd4" },
        { "1.0.3838484884444", "1.0.3838484884444" },
        { null, null }
    });
  }

  private final String equalVersionA;
  private final String equalVersionB;

  public VersionTestEquals(String versionA, String versionB) {
    equalVersionA = versionA;
    equalVersionB = versionB;
  }

  @Test public void isEqualIsCorrect() {
    assertTrue(equalVersionA + " and " + equalVersionB + " should be equal",
        new Version(equalVersionA).isEqual(equalVersionB));
  }
}