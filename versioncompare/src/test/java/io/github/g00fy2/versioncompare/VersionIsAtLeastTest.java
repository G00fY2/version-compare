package io.github.g00fy2.versioncompare;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class VersionIsAtLeastTest {

  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
      {"1.2.3", "1.2"},
      {"2.0.0", "2"},
      {"1.0.3", "1.0.3-rc1"},
      {"2.4", "2.4-beta3"},
      {"2.4", "2.4-snapshot"},
      {"2.4-rc", "2.4-beta3"},
      {"2.4.0.2", "2.4.0.1"},
      {"hasdh10uadf", "hasdh10uadf"},
      {"?ü+", "?ü+"},
      {"kasfd5", "posfd4"},
      {"1.0.3838484884444", "1.0.3838484884444"},
      {"1.0.38384848844443838484884444", "1.0.38384848844443838484884444"},
      {null, null}
    });
  }

  private final String atLeastVersionA;
  private final String atLeastVersionB;

  private final Version atLeastVersionAObject;
  private final Version atLeastVersionBObject;

  public VersionIsAtLeastTest(String versionA, String versionB) {
    atLeastVersionA = versionA;
    atLeastVersionAObject = new Version(atLeastVersionA);

    atLeastVersionB = versionB;
    atLeastVersionBObject = new Version(atLeastVersionB);
  }

  @Test
  public void isAtLeastIsCorrect() {
    assertTrue("Version " + atLeastVersionA + " should be equal or higher than " + atLeastVersionB,
      new Version(atLeastVersionA).isAtLeast(atLeastVersionB));
  }

  @Test
  public void isAtLeastComparatorIsCorrect() {
    assertTrue("Version " + atLeastVersionA + " should be equal or higher than " + atLeastVersionB,
      atLeastVersionAObject.compareTo(atLeastVersionBObject) >= 0);
  }
}