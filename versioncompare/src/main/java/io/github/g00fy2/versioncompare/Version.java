package io.github.g00fy2.versioncompare;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("WeakerAccess")
public class Version implements Comparable<Version> {

  @Nullable private final String originalString;
  @Nonnull private final List<Integer> subversionNumbers = new ArrayList<>();
  @Nonnull private final List<Integer> subversionNumbersWithoutTrailingZeros = new ArrayList<>();
  @Nonnull private String suffix = "";

  /**
   * Initializes a newly created Version object that represents the parsed version information.
   * Will have default values if {@code versionString} could not get parsed.
   *
   * @param versionString the string representing the version.
   * @see #Version(String versionString, boolean throwExceptions)
   */
  public Version(@Nullable String versionString) {
    this(versionString, false);
  }

  /**
   * Initializes a newly created Version object that represents the parsed version information.
   * Throws exceptions if {@code throwExceptions} is {@code true} and {@code versionString} could not get parsed.
   *
   * @param versionString the string representing the version.
   * @param throwExceptions controls whether invalid {@code versionString} should cause exceptions.
   * @throws NullPointerException if {@code versionString} is null.
   * @throws IllegalArgumentException if {@code versionString} does not start with a numeric character.
   * @see #Version(String versionString)
   */
  public Version(@Nullable String versionString, boolean throwExceptions) {
    if (throwExceptions) {
      if (versionString == null) {
        throw new NullPointerException("Argument versionString is null");
      }
      if (!VersionComparator.startsNumeric(versionString)) {
        throw new IllegalArgumentException("Argument versionString is no valid version");
      }
    }

    originalString = versionString;
    initVersion();
  }

  /**
   * Returns the major version.
   *
   * @return the major version, default 0.
   */
  public int getMajor() {
    return subversionNumbers.size() > VersionComparator.MAJOR ? subversionNumbers.get(VersionComparator.MAJOR) : 0;
  }

  /**
   * Returns the minor version.
   *
   * @return the minor version, default 0.
   */
  public int getMinor() {
    return subversionNumbers.size() > VersionComparator.MINOR ? subversionNumbers.get(VersionComparator.MINOR) : 0;
  }

  /**
   * Returns the patch version.
   *
   * @return the patch version, default 0.
   */
  public int getPatch() {
    return subversionNumbers.size() > VersionComparator.PATCH ? subversionNumbers.get(VersionComparator.PATCH) : 0;
  }

  /**
   * Returns a list with all numeric version parts.
   *
   * @return a list with all numeric version parts found, default empty.
   */
  @Nonnull public List<Integer> getSubversionNumbers() {
    return subversionNumbers;
  }

  /**
   * Returns the suffix.
   *
   * @return the suffix (first non-numeric part), default empty.
   */
  @Nonnull public String getSuffix() {
    return suffix;
  }

  /**
   * Returns the initial string
   *
   * @return the unmodified initial string.
   */
  @Nullable public String getOriginalString() {
    return originalString;
  }

  /**
   * Checks if the Version object is higher than {@code otherVersion}.
   *
   * @param otherVersion a string representing another version.
   * @return {@code true} if Version object is higher than {@code otherVersion} or {@code otherVersion} could not get
   * parsed. {@code False} if the Version is lower or equal.
   * @see #isHigherThan(Version otherVersion)
   */
  public boolean isHigherThan(String otherVersion) {
    return isHigherThan(new Version(otherVersion));
  }

  /**
   * Checks if the Version object is higher than {@code otherVersion}.
   *
   * @param otherVersion a Version object representing another version.
   * @return {@code true} if Version object is higher than {@code otherVersion} or {@code otherVersion} could not get
   * parsed. {@code False} if the Version is lower or equal.
   * @see #isHigherThan(String otherVersion)
   */
  public boolean isHigherThan(Version otherVersion) {
    int subversionsResult = VersionComparator.compareSubversionNumbers(
        subversionNumbersWithoutTrailingZeros,
        otherVersion.subversionNumbersWithoutTrailingZeros);
    if (subversionsResult != 0) {
      return subversionsResult > 0;
    }
    return VersionComparator.compareSuffix(suffix, otherVersion.suffix) > 0;
  }

  /**
   * Checks if the Version object is lower than {@code otherVersion}.
   *
   * @param otherVersion a string representing another version.
   * @return {@code true} if Version object is lower than {@code otherVersion}. {@code False} if the Version is higher,
   * equal or {@code otherVersion} could not get parsed.
   * @see #isLowerThan(Version otherVersion)
   */
  public boolean isLowerThan(String otherVersion) {
    return isLowerThan(new Version(otherVersion));
  }

  /**
   * Checks if the Version object is lower than {@code otherVersion}.
   *
   * @param otherVersion a Version object representing another version.
   * @return {@code true} if Version object is lower than {@code otherVersion}. {@code False} if the Version is higher,
   * equal or {@code otherVersion} could not get parsed.
   * @see #isLowerThan(String otherVersion)
   */
  public boolean isLowerThan(Version otherVersion) {
    int subversionsResult = VersionComparator.compareSubversionNumbers(
        subversionNumbersWithoutTrailingZeros,
        otherVersion.subversionNumbersWithoutTrailingZeros
    );
    if (subversionsResult != 0) {
      return subversionsResult < 0;
    }
    return VersionComparator.compareSuffix(suffix, otherVersion.suffix) < 0;
  }

  /**
   * Checks if the Version object is equal to {@code otherVersion}.
   *
   * @param otherVersion a string representing another version.
   * @return {@code true} if Version object and {@code otherVersion} are logically equal. {@code False} if the Version
   * is higher, lower or {@code otherVersion} could not get parsed.
   * @see #isEqual(Version otherVersion)
   */
  public boolean isEqual(String otherVersion) {
    return isEqual(new Version(otherVersion));
  }

  /**
   * Checks if the Version object is equal to {@code otherVersion}.
   *
   * @param otherVersion a Version object representing another version.
   * @return {@code true} if Version object and {@code otherVersion} are logically equal. {@code False} if the Version
   * is higher, lower or {@code otherVersion} could not get parsed.
   * @see #isEqual(String otherVersion)
   */
  public boolean isEqual(Version otherVersion) {
    return VersionComparator.compareSubversionNumbers(
        subversionNumbersWithoutTrailingZeros,
        otherVersion.subversionNumbersWithoutTrailingZeros
    ) == 0 && VersionComparator.compareSuffix(suffix, otherVersion.suffix) == 0;
  }

  /**
   * Checks if the Version object is equal or higher than {@code otherVersion}.
   *
   * @param otherVersion a string representing another version.
   * @return {@code true} if the version is equal or higher than {@code otherVersion}. {@code False} if the version
   * is lower than {@code otherVersion}.
   * @see #isAtLeast(Version otherVersion)
   */
  public boolean isAtLeast(String otherVersion) {
    return isAtLeast(new Version(otherVersion));
  }

  /**
   * Checks if the Version object is equal or higher than {@code otherVersion}.
   *
   * @param otherVersion a Version object representing another version.
   * @return {@code true} if the version is equal or higher than {@code otherVersion}. {@code False} if the version
   * is lower than {@code otherVersion}.
   * @see #isAtLeast(String otherVersion)
   */
  public boolean isAtLeast(Version otherVersion) {
    return isAtLeast(otherVersion, false);
  }

  /**
   * Checks if the Version object is equal or higher than {@code otherVersion}.
   *
   * @param otherVersion a string representing another version.
   * @param ignoreSuffix controls whether suffixes should be ignored.
   * @return {@code true} if the version is equal or higher than {@code otherVersion}. {@code False} if the version
   * is lower than {@code otherVersion}.
   * @see #isAtLeast(Version otherVersion, boolean ignoreSuffix)
   */
  public boolean isAtLeast(String otherVersion, boolean ignoreSuffix) {
    return isAtLeast(new Version(otherVersion), ignoreSuffix);
  }

  /**
   * Checks if the Version object is equal or higher than {@code otherVersion}.
   *
   * @param otherVersion a Version object representing another version.
   * @param ignoreSuffix controls whether suffixes should be ignored.
   * @return {@code true} if the version is equal or higher than {@code otherVersion}. {@code False} if the version
   * is lower than {@code otherVersion}.
   * @see #isAtLeast(String otherVersion, boolean ignoreSuffix)
   */
  public boolean isAtLeast(Version otherVersion, boolean ignoreSuffix) {
    int subversionsResult = VersionComparator.compareSubversionNumbers(
        subversionNumbersWithoutTrailingZeros,
        otherVersion.subversionNumbersWithoutTrailingZeros
    );
    if (subversionsResult == 0 && !ignoreSuffix) {
      return VersionComparator.compareSuffix(suffix, otherVersion.suffix) >= 0;
    }
    return subversionsResult >= 0;
  }

  private void initVersion() {
    if (originalString != null && VersionComparator.startsNumeric(originalString)) {
      String[] versionTokens = originalString.replaceAll("\\s", "").split("\\.");
      boolean suffixFound = false;
      StringBuilder suffixSb = null;

      for (String versionToken : versionTokens) {
        if (suffixFound) {
          suffixSb.append(".");
          suffixSb.append(versionToken);
        } else if (VersionComparator.isNumeric(versionToken)) {
          subversionNumbers.add(VersionComparator.safeParseInt(versionToken));
        } else {
          for (int i = 0; i < versionToken.length(); i++) {
            if (!Character.isDigit(versionToken.charAt(i))) {
              suffixSb = new StringBuilder();
              if (i > 0) {
                subversionNumbers.add(VersionComparator.safeParseInt(versionToken.substring(0, i)));
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
      subversionNumbersWithoutTrailingZeros.addAll(subversionNumbers);
      while (!subversionNumbersWithoutTrailingZeros.isEmpty() &&
          subversionNumbersWithoutTrailingZeros.lastIndexOf(0) == subversionNumbersWithoutTrailingZeros.size() -1) {
        subversionNumbersWithoutTrailingZeros.remove(subversionNumbersWithoutTrailingZeros.lastIndexOf(0));
      }
      if (suffixSb != null) suffix = suffixSb.toString();
    }
  }

  @Override
  final public int compareTo(@Nonnull Version version) {
    if (this.isEqual(version)) return 0;
    else if (this.isLowerThan(version)) return -1;
    else return 1;
  }

  @Override
  final public boolean equals(Object o) {
    if (o instanceof Version && this.isEqual((Version) o)) return true;
    else return super.equals(o);
  }

  @Override
  final public int hashCode() {
    final int prime = 31;
    int hash = 1;
    hash = prime * hash + subversionNumbersWithoutTrailingZeros.hashCode();
    if (suffix.isEmpty()) return hash;

    int releaseQualifier = VersionComparator.qualifierToNumber(suffix);
    int releaseQualifierVersion = VersionComparator.preReleaseVersion(suffix, releaseQualifier);
    hash = prime * hash + releaseQualifier;
    hash = prime * hash + releaseQualifierVersion;
    return hash;
  }
}
