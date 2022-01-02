package io.github.g00fy2.versioncompare;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class VersionEqualsVerifierTest {

  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(Version.class)
      .withIgnoredFields("originalString", "originalSubversionNumbers", "suffix")
      .withNonnullFields("subversionNumbers", "releaseType", "preReleaseVersion")
      .verify();
  }
}