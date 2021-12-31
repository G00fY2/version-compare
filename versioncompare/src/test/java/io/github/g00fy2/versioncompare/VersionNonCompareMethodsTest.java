package io.github.g00fy2.versioncompare;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VersionNonCompareMethodsTest {

  @Test(expected = NullPointerException.class)
  public void constructorIsArgumentNullptr() {
    new Version(null, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorIsArgumentIllegal() {
    new Version("x1.2.4", true);
    new Version(" ", true);
    new Version("", true);
  }

  @Test
  public void getMajorIsCorrect() {
    assertEquals(1L, new Version("1.2.3").getMajor());
    assertEquals(0L, new Version("0.662.34").getMajor());
  }

  @Test
  public void getMinorIsCorrect() {
    assertEquals(2L, new Version("1.2.3").getMinor());
    assertEquals(124L, new Version("0.124.3").getMinor());
  }

  @Test
  public void getPatchIsCorrect() {
    assertEquals(3L, new Version("1.2.3").getPatch());
    assertEquals(0L, new Version("ü").getPatch());
  }

  @Test
  public void getSubversionNumberIsCorrect() {
    assertEquals(new ArrayList<>(Arrays.asList(1L, 2L, 3L)), new Version("1.2.3").getSubversionNumbers());
    assertEquals(new ArrayList<>(Arrays.asList(144L, 22L, 3L, 44L)),
      new Version("144.22.3.44-beta").getSubversionNumbers());
    assertEquals(new ArrayList<>(Arrays.asList(144L, 22L, 3L, 44L)),
      new Version("144.22.3.44.alpha").getSubversionNumbers());
  }

  @Test
  public void getSuffixIsCorrect() {
    assertEquals("", new Version("1.65.5").getSuffix());
    assertEquals("-beta.23-4", new Version("1.65.5-beta.23-4").getSuffix());
  }

  @Test
  public void getOriginalStringIsCorrect() {
    assertEquals("1.65.5", new Version("1.65.5").getOriginalString());
    assertEquals("1.65.5-beta.23-4", new Version("1.65.5-beta.23-4").getOriginalString());
    assertNull(new Version(null).getOriginalString());
  }
}