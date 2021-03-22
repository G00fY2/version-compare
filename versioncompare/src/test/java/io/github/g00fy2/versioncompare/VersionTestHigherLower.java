package io.github.g00fy2.versioncompare;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class) public class VersionTestHigherLower {

  @Parameters public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
        { "1.1.1", "0.0.0" },
        { "1.1.1", "1.0.2" },
        { "1.2.3", "1.2.2" },
        { "12.4.567.3", "12.4.566.3" },
        { "12.4-beta", "12.4-alpha" },
        { "12.4-beta", "12.4.alpha" },
        { "12.4.beta", "12.4-alpha" },
        { "12.4.5-rc", "12.4.5-beta" },
        { "12.4.5-alpha", "12.4.5-pre-alpha" },
        { "12.4.5-rc1", "12.4.5-rc" },
        { "12.4.5-rc", "12.4.5-beta.3" },
        { "12.4.5-rc12", "12.4.5-rc11" },
        { "12.4.5-rc12", "12.4.5-rc11asd" },
        { "12.4.5-alpha3", "12.4.5-alpha1" },
        { "12.4.5-alpha", "12.4.5-snapshot" },
        { "12.4.5-alpha3a2", "12.4.5-alpha1a3" },
        { "12.4.5-alpha3", "12.4.5-alpha002" },
        { "12.4.5-alpha3xyz", "12.4.5-alpha1yxz" },
        { "12.4.5-xyz1", "12.4.5-alpha1" },
        { "12.4.5-rc1", "12.4.5-rcxyz3" },
        { "12.4.5-rc13", "12.4.5-rc-12" },
        { "12.4.6-xyz1", "12.4.5" },
        { "1.2beta33", "1.2-4beta3" },
        { "1.2beta33", "1.2-44beta32" },
        { "1-beta3", "1-alpha4" },
        { "10", "9" },
        { "2", "jgfa9" },
        { "2.1-alpha", "jgfa9-beta" },
        { "1", null }
    });
  }

  private final String higherVersion;
  private final String lowerVersion;

  private final Version higherVersionObject;
  private final Version lowerVersionObject;

  public VersionTestHigherLower(String versionA, String versionB) {
    higherVersion = versionA;
    higherVersionObject = new Version(higherVersion);

    lowerVersion = versionB;
    lowerVersionObject = new Version(lowerVersion);
  }

  @Test public void isHigherThanIsCorrect() {
    assertTrue(higherVersion + " should be higher than " + lowerVersion,
        new Version(higherVersion).isHigherThan(lowerVersion));
  }

  @Test public void isHigherThanComparatorIsCorrect() {
    assertTrue(higherVersion + " should be higher than " + lowerVersion,
            higherVersionObject.compareTo(lowerVersionObject) > 0);
  }

  @Test public void isHigherThanIsNotCorrect() {
    assertFalse(lowerVersion + " should NOT be higher than " + higherVersion,
        new Version(lowerVersion).isHigherThan(higherVersion));
  }

  @Test public void isHigherThanComparatorIsNotCorrect() {
    assertFalse(lowerVersion + " should NOT be higher than " + higherVersion,
            lowerVersionObject.compareTo(higherVersionObject) > 0);
  }

  @Test public void isLowerThanIsCorrect() {
    assertTrue(lowerVersion + " should be lower than " + higherVersion,
        new Version(lowerVersion).isLowerThan(higherVersion));
  }

  @Test public void isLowerThanComparatorIsCorrect() {
    assertTrue(lowerVersion + " should be lower than " + higherVersion,
            lowerVersionObject.compareTo(higherVersionObject) < 0);
  }

  @Test public void isLowerThanIsNotCorrect() {
    assertFalse(higherVersion + " should NOT be lower than " + lowerVersion,
        new Version(higherVersion).isLowerThan(lowerVersion));
  }

  @Test public void isLowerThanComparatorIsNotCorrect() {
    assertFalse(higherVersion + " should NOT be lower than " + lowerVersion,
            higherVersionObject.compareTo(lowerVersionObject) < 0);
  }
}