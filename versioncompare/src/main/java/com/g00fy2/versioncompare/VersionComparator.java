package com.g00fy2.versioncompare;

import java.util.List;
import java.util.regex.Pattern;

/**
 * author   Thomas Wirth
 * date     12.01.18
 * website  github.com/G00fY2
 */

final class VersionComparator {

  // position of semver version part
  static final int MAJOR = 0;
  static final int MINOR = 1;
  static final int PATCH = 2;

  // weighting of the prerelease suffixes
  private static final int PRE_ALPHA = 0;
  private static final int ALPHA = 1;
  private static final int BETA = 2;
  private static final int RELEASE_CANDIDATE = 3;
  private static final int UNKNOWN = 4;

  // only compile the regex pattern once
  static final Pattern NUMERIC = Pattern.compile("[0-9]+");

  static int compareSubversionNumbers(final List<Integer> subversionsA, final List<Integer> subversionsB) {
    if (subversionsA != null && subversionsB != null) {
      final int versionASubversionsCount = subversionsA.size();
      final int versionBSubversionsCount = subversionsB.size();

      for (int i = 0; i < Math.max(versionASubversionsCount, versionBSubversionsCount); ++i) {
        if ((i < versionASubversionsCount ? subversionsA.get(i) : 0) > (i < versionBSubversionsCount ? subversionsB.get(
            i) : 0)) {
          return 1;
        }
        else if ((i < versionASubversionsCount ? subversionsA.get(i) : 0) < (i < versionBSubversionsCount ? subversionsB
            .get(i) : 0)) {
          return -1;
        }
      }
    }
    return 0;
  }

  static int compareSuffix(final String suffixA, final String suffixB) {
    if ((suffixA != null && suffixA.length() > 0) || (suffixB != null && suffixB.length() > 0)) {
      int preReleaseQualifierA = preReleaseQualifier(suffixA);
      int preReleaseQualifierB = preReleaseQualifier(suffixB);
      // compare pre release priority
      if (preReleaseQualifierA > preReleaseQualifierB) {
        return 1;
      } else if (preReleaseQualifierA < preReleaseQualifierB) {
        return -1;
      } else if (preReleaseQualifierA != UNKNOWN && preReleaseQualifierA == preReleaseQualifierB) {
        int suffixVersionA = getPrereleaseVersionInfo(suffixA.split("\\p{P}"));
        int suffixVersionB = getPrereleaseVersionInfo(suffixB.split("\\p{P}"));
        if (suffixVersionA > suffixVersionB) {
          return 1;
        } else if (suffixVersionA < suffixVersionB) {
          return -1;
        } else {
          return 0;
        }
      }
    }
    return 0;
  }

  private static int preReleaseQualifier(String suffix) {
    if (suffix != null && suffix.length() > 0) {
      suffix = suffix.toLowerCase();
      if (suffix.contains("pre") && suffix.contains("alpha")) return PRE_ALPHA;
      if (suffix.contains("alpha")) return ALPHA;
      if (suffix.contains("beta")) return BETA;
      if (suffix.contains("rc")) return RELEASE_CANDIDATE;
    }
    return UNKNOWN;
  }

  private static int getPrereleaseVersionInfo(String[] preReleaseSuffixes) {
    // TODO: handle numbers before preReleaseQualifier
    int lastIntegerIndex = 0;
    StringBuilder versionNumber;
    for (String suffix : preReleaseSuffixes) {
      if (NUMERIC.matcher(suffix).find()) {
        versionNumber = new StringBuilder();
        for (int i = 0; i < suffix.length(); i++) {
          if (NUMERIC.matcher(String.valueOf(suffix.charAt(i))).matches() && (lastIntegerIndex == 0
              || lastIntegerIndex + 1 == i)) {
            lastIntegerIndex = i;
            versionNumber.append(suffix.charAt(i));
          } else if (lastIntegerIndex > 0) {
            break;
          }
        }
        return Integer.valueOf(versionNumber.toString());
      }
    }
    return 0;
  }
}
