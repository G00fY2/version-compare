package com.g00fy2.versioncompare;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("WeakerAccess") public class Version {

  @Nullable private final String originalString;
  @Nonnull private final List<Integer> subversionNumbers = new ArrayList<>();
  @Nonnull private String suffix = "";

  /**
   * Initializes a newly created Version object that represents the parsed version information.
   * Will have default values if {@code versionString} could not get parsed.
   *
   * @param versionString String representing the version.
   */
  public Version(@Nullable String versionString) {
    this(versionString, false);
  }

  /**
   * Initializes a newly created Version object that represents the parsed version information.
   * Throws exceptions if {@code versionString} could not get parsed.
   *
   * @param versionString  String representing the version.
   * @param throwExceptions NullPointerException if {@code versionString} is {@code null} and
   *                        IllegalArgumentException if {@code versionString} does not start with a
   *                        numeric character.
   */
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

  /**
   * Returns the major version.
   *
   * @return  Major version, default 0.
   */
  public int getMajor() {
    return subversionNumbers.size() > VersionComparator.MAJOR ? subversionNumbers.get(VersionComparator.MAJOR) : 0;
  }

  /**
   * Returns the minor version.
   *
   * @return Minor version, default 0.
   */
  public int getMinor() {
    return subversionNumbers.size() > VersionComparator.MINOR ? subversionNumbers.get(VersionComparator.MINOR) : 0;
  }

  /**
   * Returns the patch version.
   *
   * @return Patch version, default 0.
   */
  public int getPatch() {
    return subversionNumbers.size() > VersionComparator.PATCH ? subversionNumbers.get(VersionComparator.PATCH) : 0;
  }

  /**
   * Returns a list with all numeric version parts.
   *
   * @return List with all numeric version parts found, default empty.
   */
  @Nonnull public List<Integer> getSubversionNumbers() {
    return subversionNumbers;
  }

  /**
   * Returns the suffix.
   *
   * @return Suffix (first non-numeric part), default empty.
   */
  @Nonnull public String getSuffix() {
    return suffix;
  }

  /**
   * Returns the initial string
   *
   * @return Unmodified initial string.
   */
  @Nullable public String getOriginalString() {
    return originalString;
  }

  /**
   * Checks if the Version object is higher than {@code otherVersion}.
   *
   * @param otherVersion A string representing an other version.
   * @return True if Version object is higher than {@code otherVersion} or {@code otherVersion} could not get parsed.
   *         False if the Version is lower or equal.
   * @see #isHigherThan(Version otherVersion)
   */
  public boolean isHigherThan(String otherVersion) {
    return isHigherThan(new Version(otherVersion));
  }

  /**
   * Checks if the Version object is higher than {@code otherVersion}.
   *
   * @param otherVersion A Version object representing an other version.
   * @return True if Version object is higher than {@code otherVersion} or {@code otherVersion} could not get parsed.
   *         False if the Version is lower or equal.
   * @see #isHigherThan(String otherVersion)
   */
  public boolean isHigherThan(Version otherVersion) {
    int subversionsResult =
        VersionComparator.compareSubversionNumbers(subversionNumbers, otherVersion.subversionNumbers);
    if (subversionsResult != 0) {
      return subversionsResult > 0;
    }

    int suffixResult = VersionComparator.compareSuffix(suffix, otherVersion.suffix);
    return suffixResult != 0 && suffixResult > 0;
  }

  /**
   * Checks if the Version object is lower than {@code otherVersion}.
   *
   * @param otherVersion A string representing an other version.
   * @return True if Version object is lower than {@code otherVersion}. False if the Version is higher, equal or
   *         {@code otherVersion} could not get parsed.
   * @see #isLowerThan(Version otherVersion)
   */
  public boolean isLowerThan(String otherVersion) {
    return isLowerThan(new Version(otherVersion));
  }

  /**
   * Checks if the Version object is equal to {@code otherVersion}.
   *
   * @param otherVersion A Version object representing an other version.
   * @return True if Version object is equal to {@code otherVersion}. False if the Version is higher, lower or
   *         {@code otherVersion} could not get parsed.
   * @see #isLowerThan(String otherVersion)
   */
  public boolean isLowerThan(Version otherVersion) {
    int subversionsResult =
        VersionComparator.compareSubversionNumbers(subversionNumbers, otherVersion.subversionNumbers);
    if (subversionsResult != 0) {
      return subversionsResult < 0;
    }

    int suffixResult = VersionComparator.compareSuffix(suffix, otherVersion.suffix);
    return suffixResult != 0 && suffixResult < 0;
  }

  /**
   * Checks if the Version object is equal to {@code otherVersion}.
   *
   * @param otherVersion A string representing an other version.
   * @return True if Version object and {@code otherVersion} are logically equal. False if the Version is higher,
   *         lower or {@code otherVersion} could not get parsed.
   * @see #isEqual(Version otherVersion)
   */
  public boolean isEqual(String otherVersion) {
    return isEqual(new Version(otherVersion));
  }

  /**
   * Checks if the Version object is equal to {@code otherVersion}.
   *
   * @param otherVersion A Version object representing an other version.
   * @return True if Version object and {@code otherVersion} are logically equal. False if the Version is higher,
   *         lower or {@code otherVersion} could not get parsed.
   * @see #isEqual(String otherVersion)
   */
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
