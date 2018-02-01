package com.g00fy2.versioncompare;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class) public class VersionTestHigherLower {

  @Parameters public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
        { "1.2.3", "1.2.2" },
        { "12.4.567.3", "12.4.566.3" },
        { "12.4-beta", "12.4-alpha" },
        { "12.4-beta", "12.4.alpha" },
        { "12.4.beta", "12.4-alpha" },
        { "12.4.5-rc", "12.4.5-beta" },
        { "12.4.5-rc1", "12.4.5-rc" },
        { "12.4.5-rc12", "12.4.5-rc11" },
        { "12.4.5-rc12", "12.4.5-rc11asd" },
        { "12.4.5-alpha3", "12.4.5-alpha1" },
        { "12.4.5-alpha3a2", "12.4.5-alpha1a3" },
        { "12.4.5-alpha3", "12.4.5-alpha002" },
        { "12.4.5-alpha3xyz", "12.4.5-alpha1yxz" },
        { "12.4.5-xyz1", "12.4.5-alpha1" },
        { "12.4.6-xyz1", "12.4.5" },
        { "1.2beta33", "1.2-4beta3" },
        { "1-beta3", "1-alpha4" },
        { "10", "9" },
        { "2", "jgfa9" },
        { "2.1-alpha", "jgfa9-beta" },
        { "1", null }
    });
  }

  private String higherVersion;
  private String lowerVersion;

  public VersionTestHigherLower(String inputA, String inputB) {
    higherVersion = inputA;
    lowerVersion = inputB;
  }

  @Test public void isHigherThan_isCorrect() throws Exception {
    assertEquals(higherVersion + " should be higher than " + lowerVersion, true,
        new Version(higherVersion).isHigherThan(lowerVersion));
  }

  @Test public void isHigherThan_isIncorrect() throws Exception {
    assertEquals(lowerVersion + " should NOT be higher than " + higherVersion, false,
        new Version(lowerVersion).isHigherThan(higherVersion));
  }

  @Test public void isLowerThan_isCorrect() throws Exception {
    assertEquals(lowerVersion + " should be lower than " + higherVersion, true,
        new Version(lowerVersion).isLowerThan(higherVersion));
  }

  @Test public void isLowerThan_isIncorrect() throws Exception {
    assertEquals(higherVersion + " should NOT be lower than " + lowerVersion, false,
        new Version(higherVersion).isLowerThan(lowerVersion));
  }
}