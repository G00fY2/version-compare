package com.g00fy2.versioncompare;

import java.util.ArrayList;
import java.util.List;

/**
 * author   Thomas Wirth
 * date     12.01.18
 * website  github.com/G00fY2
 */

@SuppressWarnings({ "WeakerAccess", "SameParameterValue" }) public class Version {

  private final String originalString;
  private List<Integer> subversionNumbers;
  private String suffix;

  public Version(String versionString) {
    this(versionString, false);
  }

  public Version(String versionString, boolean throwExceptions) {
    if (throwExceptions) {
      if (versionString == null) {
        throw new NullPointerException("Argument versionString is null");
      }
      if (!VersionComparator.NUMERIC.matcher(String.valueOf(versionString.trim().charAt(0))).matches()) {
        throw new IllegalArgumentException("Argument versionString is no valid version");
      }
    }

    originalString = versionString;
    initVersion();
  }

  public int getMajor() {
    return subversionNumbers.size() > VersionComparator.MAJOR ? subversionNumbers.get(VersionComparator.MAJOR) : 0;
  }

  public int getMinor() {
    return subversionNumbers.size() > VersionComparator.MINOR ? subversionNumbers.get(VersionComparator.MINOR) : 0;
  }

  public int getPatch() {
    return subversionNumbers.size() > VersionComparator.PATCH ? subversionNumbers.get(VersionComparator.PATCH) : 0;
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
    int subversionsResult =
        VersionComparator.compareSubversionNumbers(subversionNumbers, otherVersion.subversionNumbers);
    if (subversionsResult != 0) {
      return subversionsResult > 0;
    }

    int suffixResult = VersionComparator.compareSuffix(suffix, otherVersion.suffix);
    return suffixResult != 0 && suffixResult > 0;
  }

  public boolean isLowerThan(String otherVersion) {
    return isLowerThan(new Version(otherVersion));
  }

  public boolean isLowerThan(Version otherVersion) {
    int subversionsResult =
        VersionComparator.compareSubversionNumbers(subversionNumbers, otherVersion.subversionNumbers);
    if (subversionsResult != 0) {
      return subversionsResult < 0;
    }

    int suffixResult = VersionComparator.compareSuffix(suffix, otherVersion.suffix);
    return suffixResult != 0 && suffixResult < 0;
  }

  public boolean isEqual(String otherVersion) {
    return isEqual(new Version(otherVersion));
  }

  public boolean isEqual(Version otherVersion) {
    int subversionsResult =
        VersionComparator.compareSubversionNumbers(subversionNumbers, otherVersion.subversionNumbers);
    int suffixResult = VersionComparator.compareSuffix(suffix, otherVersion.suffix);
    return subversionsResult == 0 && suffixResult == 0;
  }

  private void initVersion() {
    subversionNumbers = new ArrayList<>();
    suffix = "";

    if (originalString != null) {
      String[] versionTokens = originalString.replaceAll("\\s", "").split("\\.");

      // array must not be empty, first string must not be empty, very first char must be numeric
      if (versionTokens.length > 0 && !versionTokens[0].isEmpty() && VersionComparator.NUMERIC.matcher(
          String.valueOf(versionTokens[0].charAt(0))).matches()) {
        boolean suffixFound = false;
        StringBuilder suffixSb = new StringBuilder();

        for (String versionToken : versionTokens) {
          if (VersionComparator.NUMERIC.matcher(versionToken).matches() && !suffixFound) {
            subversionNumbers.add(Integer.parseInt(versionToken));
          } else {
            for (int j = 0; j < versionToken.length(); j++) {
              if (!VersionComparator.NUMERIC.matcher(String.valueOf(versionToken.charAt(j))).find()) {
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
  }
}
