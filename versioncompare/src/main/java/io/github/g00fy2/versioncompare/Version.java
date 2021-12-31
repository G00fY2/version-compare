package io.github.g00fy2.versioncompare;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Version implements Comparable<Version> {

  @Nullable
  private final String originalString;
  @NotNull
  private final List<@NotNull Long> subversionNumbers = new ArrayList<>();
  @NotNull
  private final List<@NotNull Long> subversionNumbersWithoutTrailingZeros = new ArrayList<>();
  @NotNull
  private final String suffix;
  @NotNull
  private final VersionComparator.ReleaseType releaseType;
  private final long preReleaseVersion;

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
   * @param versionString   the string representing the version.
   * @param throwExceptions controls whether invalid {@code versionString} should cause exceptions.
   * @throws NullPointerException     if {@code versionString} is null.
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
    if (originalString != null && VersionComparator.startsNumeric(originalString)) {
      final String[] versionTokens = originalString.replaceAll("\\s", "").split("\\.");
      boolean suffixFound = false;
      StringBuilder suffixSb = null;

      for (String versionToken : versionTokens) {
        if (suffixFound) {
          suffixSb.append(".");
          suffixSb.append(versionToken);
        } else if (VersionComparator.isNumeric(versionToken)) {
          subversionNumbers.add(VersionComparator.safeParseLong(versionToken));
        } else {
          for (int i = 0; i < versionToken.length(); i++) {
            if (!Character.isDigit(versionToken.charAt(i))) {
              suffixSb = new StringBuilder();
              if (i > 0) {
                subversionNumbers.add(VersionComparator.safeParseLong(versionToken.substring(0, i)));
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
      suffix = (suffixSb != null) ? suffixSb.toString() : "";
      subversionNumbersWithoutTrailingZeros.addAll(subversionNumbers);
      while (!subversionNumbersWithoutTrailingZeros.isEmpty() &&
        subversionNumbersWithoutTrailingZeros.lastIndexOf(0L) == subversionNumbersWithoutTrailingZeros.size() - 1) {
        subversionNumbersWithoutTrailingZeros.remove(subversionNumbersWithoutTrailingZeros.lastIndexOf(0L));
      }
    } else {
      suffix = "";
    }
    releaseType = VersionComparator.qualifierToReleaseType(suffix);
    preReleaseVersion = VersionComparator.preReleaseVersion(suffix, releaseType);
  }

  /**
   * Returns the major version.
   *
   * @return the major version, default 0.
   */
  public long getMajor() {
    return subversionNumbers.size() > 0 ? subversionNumbers.get(0) : 0L;
  }

  /**
   * Returns the minor version.
   *
   * @return the minor version, default 0.
   */
  public long getMinor() {
    return subversionNumbers.size() > 1 ? subversionNumbers.get(1) : 0L;
  }

  /**
   * Returns the patch version.
   *
   * @return the patch version, default 0.
   */
  public long getPatch() {
    return subversionNumbers.size() > 2 ? subversionNumbers.get(2) : 0L;
  }

  /**
   * Returns a list with all numeric version parts.
   *
   * @return a list with all numeric version parts found, default empty.
   */
  @NotNull
  public List<@NotNull Long> getSubversionNumbers() {
    return subversionNumbers;
  }

  /**
   * Returns the suffix.
   *
   * @return the suffix (first non-numeric part), default empty.
   */
  @NotNull
  public String getSuffix() {
    return suffix;
  }

  /**
   * Returns the initial string
   *
   * @return the unmodified initial string.
   */
  @Nullable
  public String getOriginalString() {
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
    return compareTo(otherVersion) > 0;
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
    return compareTo(otherVersion) < 0;
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
    return compareTo(otherVersion) == 0;
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
    return compareTo(otherVersion) >= 0;
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
    return compareTo(otherVersion, ignoreSuffix) >= 0;
  }

  @Override
  public final int compareTo(@NotNull Version version) {
    return compareTo(version, false);
  }

  private int compareTo(@NotNull Version version, final boolean ignoreSuffix) {
    final int versionNumberResult = VersionComparator.compareSubversionNumbers(
      subversionNumbersWithoutTrailingZeros,
      version.subversionNumbersWithoutTrailingZeros
    );
    if (versionNumberResult != 0 || ignoreSuffix) {
      return versionNumberResult;
    }
    final int releaseTypeResult = releaseType.compareTo(version.releaseType);
    if (releaseTypeResult != 0) {
      return releaseTypeResult;
    } else {
      return Long.compare(preReleaseVersion, version.preReleaseVersion);
    }
  }

  @Override
  public final boolean equals(Object o) {
    if (o instanceof Version && isEqual((Version) o)) return true;
    else return super.equals(o);
  }

  @Override
  public final int hashCode() {
    int result = subversionNumbersWithoutTrailingZeros.hashCode();
    result = 31 * result + releaseType.hashCode();
    result = 31 * result + (int) (preReleaseVersion ^ (preReleaseVersion >>> 32));
    return result;
  }
}
