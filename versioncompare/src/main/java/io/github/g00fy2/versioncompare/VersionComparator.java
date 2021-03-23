package io.github.g00fy2.versioncompare;

import javax.annotation.Nonnull;
import java.util.List;

final class VersionComparator {

  private VersionComparator() {
    // utility class should not be instantiated
  }

  // position of SemVer version part
  static final int MAJOR = 0;
  static final int MINOR = 1;
  static final int PATCH = 2;

  // supported PreRelease suffixes
  private static final String SNAPSHOT_STRING = "snapshot";
  private static final String PRE_STRING = "pre";
  private static final String ALPHA_STRING = "alpha";
  private static final String BETA_STRING = "beta";
  private static final String RC_STRING = "rc";

  // weighting of the PreRelease suffixes
  private static final int SNAPSHOT = 0;
  private static final int PRE_ALPHA = 1;
  private static final int ALPHA = 2;
  private static final int BETA = 3;
  private static final int RC = 4;
  private static final int UNKNOWN = 5;

  static int compareSubversionNumbers(@Nonnull final List<Integer> subversionsA,
                                      @Nonnull final List<Integer> subversionsB) {
    final int versASize = subversionsA.size();
    final int versBSize = subversionsB.size();
    final int maxSize = Math.max(versASize, versBSize);

    for (int i = 0; i < maxSize; i++) {
      if ((i < versASize ? subversionsA.get(i) : 0) > (i < versBSize ? subversionsB.get(i) : 0)) {
        return 1;
      } else if ((i < versASize ? subversionsA.get(i) : 0) < (i < versBSize ? subversionsB.get(i) : 0)) {
        return -1;
      }
    }
    return 0;
  }

  static int compareSuffix(@Nonnull final String suffixA, @Nonnull final String suffixB) {
    if (suffixA.length() > 0 || suffixB.length() > 0) {
      final int qualifierA = qualifierToNumber(suffixA);
      final int qualifierB = qualifierToNumber(suffixB);

      if (qualifierA > qualifierB) {
        return 1;
      } else if (qualifierA < qualifierB) {
        return -1;
      } else if (qualifierA != UNKNOWN && qualifierA != SNAPSHOT) {
        final int suffixVersionA = preReleaseVersion(suffixA, qualifierA);
        final int suffixVersionB = preReleaseVersion(suffixB, qualifierB);

        if (suffixVersionA > suffixVersionB) {
          return 1;
        } else if (suffixVersionA < suffixVersionB) {
          return -1;
        }
      }
    }
    return 0;
  }

  static int qualifierToNumber(@Nonnull String suffix) {
    if (suffix.length() > 0) {
      suffix = suffix.toLowerCase();
      if (suffix.contains(RC_STRING)) return RC;
      if (suffix.contains(BETA_STRING)) return BETA;
      if (suffix.contains(ALPHA_STRING)) {
        if (suffix.substring(0, suffix.indexOf(ALPHA_STRING)).contains(PRE_STRING)) {
          return PRE_ALPHA;
        } else {
          return ALPHA;
        }
      }
      if (suffix.contains(SNAPSHOT_STRING)) return SNAPSHOT;
    }
    return UNKNOWN;
  }

  static int preReleaseVersion(@Nonnull final String suffix, final int qualifier) {
    final int startIndex = indexOfQualifier(suffix, qualifier);
    if (startIndex < suffix.length()) {
      final int maxStartIndex = Math.min(startIndex + 2, suffix.length());
      if (containsNumeric(suffix.substring(startIndex, maxStartIndex))) {
        StringBuilder versionNumber = new StringBuilder();
        for (int i = startIndex; i < suffix.length(); i++) {
          char c = suffix.charAt(i);
          if (Character.isDigit(c)) {
            versionNumber.append(c);
          } else if (i != startIndex) {
            break;
          }
        }
        return safeParseInt(versionNumber.toString());
      }
    }
    return 0;
  }

  private static int indexOfQualifier(@Nonnull final String suffix, final int qualifier) {
    if (qualifier == RC) return suffix.indexOf(RC_STRING) + RC_STRING.length();
    if (qualifier == BETA) return suffix.indexOf(BETA_STRING) + BETA_STRING.length();
    if (qualifier == ALPHA || qualifier == PRE_ALPHA) return suffix.indexOf(ALPHA_STRING) + ALPHA_STRING.length();
    return 0;
  }

  // helper methods
  static boolean startsNumeric(@Nonnull String str) {
    str = str.trim();
    return str.length() > 0 && Character.isDigit(str.charAt(0));
  }

  static int safeParseInt(@Nonnull String numbers) {
    if (numbers.length() > 9) {
      numbers = numbers.substring(0, 9);
    }
    return Integer.parseInt(numbers);
  }

  static boolean isNumeric(@Nonnull final CharSequence cs) {
    final int sz = cs.length();
    if (sz > 0) {
      for (int i = 0; i < sz; i++) {
        if (!Character.isDigit(cs.charAt(i))) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  private static boolean containsNumeric(@Nonnull final CharSequence cs) {
    final int sz = cs.length();
    if (sz > 0) {
      for (int i = 0; i < sz; i++) {
        if (Character.isDigit(cs.charAt(i))) {
          return true;
        }
      }
    }
    return false;
  }
}
