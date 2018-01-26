package com.g00fy2.versioncompare;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * author   Thomas Wirth
 * date     12.01.18
 * website  github.com/G00fY2
 */

@SuppressWarnings("WeakerAccess") public class Version {

  @Nullable private final String originalString;
  @Nonnull private final List<Integer> subversionNumbers = new ArrayList<>();
  @Nonnull private String suffix = "";

  public Version(@Nullable String versionString) {
    this(versionString, false);
  }

  public Version(@Nullable String versionString, boolean throwExceptions) {
    if (throwExceptions) {
      if (versionString == null) {
        throw new NullPointerException("Argument versionString is null");
      }
      if (!startsNumeric(versionString)) {
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

  @Nonnull public List<Integer> getSubversionNumbers() {
    return subversionNumbers;
  }

  @Nonnull public String getSuffix() {
    return suffix;
  }

  @Nullable public String getOriginalString() {
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
    if (originalString != null && startsNumeric(originalString)) {
      String[] versionTokens = originalString.replaceAll("\\s", "").split("\\.");
      boolean suffixFound = false;
      StringBuilder suffixSb = new StringBuilder();

      for (String versionToken : versionTokens) {
        if (suffixFound) {
          suffixSb.append(".");
          suffixSb.append(versionToken);
        } else if (VersionComparator.NUMERIC_PATTERN.matcher(versionToken).matches()) {
          subversionNumbers.add(Integer.parseInt(versionToken));
        } else {
          for (int i = 0; i < versionToken.length(); i++) {
            if (!VersionComparator.NUMERIC_PATTERN.matcher(String.valueOf(versionToken.charAt(i))).matches()) {
              if (i > 0) {
                subversionNumbers.add(Integer.parseInt(versionToken.substring(0, i)));
                suffixSb.append(versionToken.substring(i));
              } else {
                suffixSb.append(versionToken);
              }
              suffixFound = true;
              break;
            }
          }
        }
      }
      suffix = suffixSb.toString();
    }
  }

  private boolean startsNumeric(@Nonnull String s) {
    s = s.trim();
    return s.length() > 0 && VersionComparator.NUMERIC_PATTERN.matcher(String.valueOf(s.charAt(0))).matches();
  }
}
