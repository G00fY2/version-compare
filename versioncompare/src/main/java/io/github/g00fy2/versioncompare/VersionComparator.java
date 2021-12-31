package io.github.g00fy2.versioncompare;

import org.jetbrains.annotations.NotNull;
import java.util.List;

final class VersionComparator {

  private VersionComparator() {
    // utility class should not be instantiated
  }

  // supported PreRelease suffixes
  private static final String SNAPSHOT_STRING = "snapshot";
  private static final String PRE_STRING = "pre";
  private static final String ALPHA_STRING = "alpha";
  private static final String BETA_STRING = "beta";
  private static final String RC_STRING = "rc";

  // weighting of the PreRelease suffixes
  enum ReleaseType {
    SNAPSHOT,
    PRE_ALPHA,
    ALPHA,
    BETA,
    RC,
    STABLE
  }

  static int compareSubversionNumbers(@NotNull final List<Long> versionNumbersA,
                                      @NotNull final List<Long> versionNumbersB) {
    final int numbersSizeA = versionNumbersA.size();
    final int numbersSizeB = versionNumbersB.size();
    final int maxSize = Math.max(numbersSizeA, numbersSizeB);

    for (int i = 0; i < maxSize; i++) {
      if ((i < numbersSizeA ? versionNumbersA.get(i) : 0) > (i < numbersSizeB ? versionNumbersB.get(i) : 0)) {
        return 1;
      } else if ((i < numbersSizeA ? versionNumbersA.get(i) : 0) < (i < numbersSizeB ? versionNumbersB.get(i) : 0)) {
        return -1;
      }
    }
    return 0;
  }

  static ReleaseType qualifierToReleaseType(@NotNull String suffix) {
    if (suffix.length() > 0) {
      suffix = suffix.toLowerCase();
      if (suffix.contains(RC_STRING)) return ReleaseType.RC;
      if (suffix.contains(BETA_STRING)) return ReleaseType.BETA;
      if (suffix.contains(ALPHA_STRING)) {
        if (suffix.substring(0, suffix.indexOf(ALPHA_STRING)).contains(PRE_STRING)) {
          return ReleaseType.PRE_ALPHA;
        } else {
          return ReleaseType.ALPHA;
        }
      }
      if (suffix.contains(SNAPSHOT_STRING)) return ReleaseType.SNAPSHOT;
    }
    return ReleaseType.STABLE;
  }

  static long preReleaseVersion(@NotNull final String suffix, final ReleaseType releaseType) {
    if (releaseType == ReleaseType.STABLE || releaseType == ReleaseType.SNAPSHOT) return 0;

    final int startIndex = indexOfQualifier(suffix, releaseType);
    if (startIndex < suffix.length()) {
      final int maxStartIndex = Math.min(startIndex + 2, suffix.length());
      if (containsNumeric(suffix.substring(startIndex, maxStartIndex))) {
        final StringBuilder versionNumber = new StringBuilder();
        for (int i = startIndex; i < suffix.length(); i++) {
          final char c = suffix.charAt(i);
          if (Character.isDigit(c)) {
            versionNumber.append(c);
          } else if (i != startIndex) {
            break;
          }
        }
        return safeParseLong(versionNumber.toString());
      }
    }
    return 0;
  }

  private static int indexOfQualifier(@NotNull final String suffix, final ReleaseType releaseType) {
    switch (releaseType) {
      case RC:
        return suffix.indexOf(RC_STRING) + RC_STRING.length();
      case BETA:
        return suffix.indexOf(BETA_STRING) + BETA_STRING.length();
      case ALPHA:
      case PRE_ALPHA:
        return suffix.indexOf(ALPHA_STRING) + ALPHA_STRING.length();
      default:
        return 0;
    }
  }

  // helper methods
  static boolean startsNumeric(@NotNull String str) {
    str = str.trim();
    return str.length() > 0 && Character.isDigit(str.charAt(0));
  }

  static long safeParseLong(@NotNull String numbers) {
    if (numbers.length() > 19) {
      numbers = numbers.substring(0, 19);
    }
    return Long.parseLong(numbers);
  }

  static boolean isNumeric(@NotNull final CharSequence cs) {
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

  private static boolean containsNumeric(@NotNull final CharSequence cs) {
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
