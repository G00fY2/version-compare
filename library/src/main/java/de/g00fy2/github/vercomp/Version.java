package de.g00fy2.github.vercomp;

import java.util.ArrayList;
import java.util.List;

/**
 * author   Thomas Wirth
 * date     12.01.18
 * website  github.com/G00fY2
 */

public class Version {

  private final String originalString;
  private List<Integer> subversionNumbers;
  private String suffix;

  public Version(String versionString) {
    originalString = versionString;
    initVersion();
  }

  public int getMajor() {
    return subversionNumbers.size() >= StaticData.MAJOR ? subversionNumbers.get(StaticData.MAJOR) : 0;
  }

  public int getMinor() {
    return subversionNumbers.size() >= StaticData.MINOR ? subversionNumbers.get(StaticData.MINOR) : 0;
  }

  public int getPatch() {
    return subversionNumbers.size() >= StaticData.PATCH ? subversionNumbers.get(StaticData.PATCH) : 0;
  }

  public List<Integer> getSubversionNumbers() {
    return subversionNumbers;
  }

  public String getSuffix() {
    return suffix;
  }

  public String getOriginalString() {
    return originalString;
  }

  public boolean isHigherThan(String otherVersion) {
    return isHigherThan(new Version(otherVersion));
  }

  public boolean isHigherThan(Version otherVersion) {
    int subversionsResult = compareSubversionNumbers(subversionNumbers, otherVersion.subversionNumbers);
    if (subversionsResult != 0) {
      // invalid version strings will count as lower
      return subversionsResult > 0;
    }

    int suffixResult = compareSuffix(suffix, otherVersion.suffix);
    return suffixResult != 0 && suffixResult > 0;
  }

  public boolean isLowerThan(String otherVersion) {
    return isLowerThan(new Version(otherVersion));
  }

  public boolean isLowerThan(Version otherVersion) {
    int subversionsResult = compareSubversionNumbers(subversionNumbers, otherVersion.subversionNumbers);
    if (subversionsResult != 0) {
      // invalid version strings will count as lower
      return subversionsResult < 0;
    }

    int suffixResult = compareSuffix(suffix, otherVersion.suffix);
    return suffixResult != 0 && suffixResult < 0;
  }

  public boolean isEqual(String otherVersion) {
    return isEqual(new Version(otherVersion));
  }

  public boolean isEqual(Version otherVersion) {
    int subversionsResult = compareSubversionNumbers(subversionNumbers, otherVersion.subversionNumbers);
    int suffixResult = compareSuffix(suffix, otherVersion.suffix);

    // two invalid version strings will result in true
    return subversionsResult == 0 && suffixResult == 0;
  }

  private void initVersion() {
    String[] versionTokens = originalString.replaceAll("\\s", "").split("\\.");

    if (versionTokens.length > 0) {
      subversionNumbers = new ArrayList<>();
      boolean suffixFound = false;
      StringBuilder suffixSb = new StringBuilder();

      for (String versionToken : versionTokens) {
        if (StaticData.NUMERIC.matcher(versionToken).matches() && !suffixFound) {
          subversionNumbers.add(Integer.parseInt(versionToken));
        } else {
          for (int j = 0; j < versionToken.length(); j++) {
            if (!StaticData.NUMERIC.matcher(String.valueOf(versionToken.charAt(j))).find()) {
              if (j > 0) {
                subversionNumbers.add(Integer.parseInt(versionToken.substring(0, j)));
                suffixSb.append(versionToken.substring(j));
                suffixFound = true;
              } else {
                suffixSb.append(versionToken);
                suffixFound = true;
              }
              break;
            } else if (suffixFound) {
              suffixSb.append(".");
              suffixSb.append(versionToken);
              break;
            }
          }
        }
      }
      suffix = suffixSb.toString();
    }
  }

  private int compareSubversionNumbers(final List<Integer> subversionsA, final List<Integer> subversionsB) {
    if (subversionsA != null && subversionsB != null) {
      final int versionASubversionsCount = subversionsA.size();
      final int versionBSubversionsCount = subversionsB.size();

      for (int i = 0; i < Math.max(versionASubversionsCount, versionBSubversionsCount); ++i) {
        // higher
        if ((i < versionASubversionsCount ? subversionNumbers.get(i) : 0) > (i < versionBSubversionsCount ? subversionsB
            .get(i) : 0)) {
          return 1;
        }
        //lower
        else if ((i < versionASubversionsCount ? subversionNumbers.get(i) : 0) < (i < versionBSubversionsCount
            ? subversionsB.get(i) : 0)) {
          return -1;
        }
      }
    }
    return 0;
  }

  private int compareSuffix(final String suffixA, final String suffixB) {
    if (suffixA != null || suffixB != null) {
      // higher
      if ((suffixA == null || suffixA.length() == 0) && (suffixB != null && suffixB.length() > 0)) {
        return 1;
      }
      // lower
      else if ((suffixA != null && suffixA.length() > 0) && (suffixB == null || suffixB.length() == 0)) {
        return -1;
      }
      // compare existent suffixes
      else {
        int preReleaseQualifierA = preReleaseQualifier(suffixA);
        int preReleaseQualifierB = preReleaseQualifier(suffixB);
        // compare pre release priority
        if (preReleaseQualifierA > preReleaseQualifierB) {
          return 1;
        } else if (preReleaseQualifierA < preReleaseQualifierB) {
          return -1;
        } else if (preReleaseQualifierA != StaticData.UNKNOWN && preReleaseQualifierA == preReleaseQualifierB) {
          if (suffixA != null && suffixB != null) {
            int suffixVersionA = getVersionInfoFromSuffix(suffixA.split("\\p{P}"));
            int suffixVersionB = getVersionInfoFromSuffix(suffixB.split("\\p{P}"));

            if (suffixVersionA > suffixVersionB) {
              return 1;
            } else if (suffixVersionA < suffixVersionB) {
              return -1;
            } else {
              return 0;
            }
          }
        }
      }
    }
    return 0;
  }

  private int preReleaseQualifier(final String suffixPart) {
    if (suffix.length() > 0) {
      String suffix = suffixPart.toLowerCase();
      if (suffix.contains("pre") && suffix.contains("alpha")) return StaticData.PRE_ALPHA;
      if (suffix.contains("alpha")) return StaticData.ALPHA;
      if (suffix.contains("beta")) return StaticData.BETA;
      if (suffix.contains("rc")) return StaticData.RELEASE_CANDIDATE;
    }
    return StaticData.UNKNOWN;
  }

  private int getVersionInfoFromSuffix(String[] preReleaseSuffixes) {
    int lastIntegerIndex = 0;
    StringBuilder versionNumber;
    for (String suffix : preReleaseSuffixes) {
      if (StaticData.NUMERIC.matcher(suffix).find()) {
        versionNumber = new StringBuilder();
        for (int i = 0; i < suffix.length(); i++) {
          if (StaticData.NUMERIC.matcher(String.valueOf(suffix.charAt(i))).matches() && (lastIntegerIndex == 0
              || lastIntegerIndex + 1 == i)) {
            lastIntegerIndex = i;
            versionNumber.append(suffix.charAt(i));
          }
        }
        return Integer.valueOf(versionNumber.toString());
      }
    }
    return 0;
  }
}
