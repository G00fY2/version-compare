package com.g00fy2.versioncompare;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class VersionEqualsVerifier {

  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(Version.class).withIgnoredFields("originalString", "subversionNumbers", "suffix").verify();
  }
}
