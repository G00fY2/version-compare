package com.g00fy2.versioncompare;

import java.util.regex.Pattern;

/**
 * author   Thomas Wirth
 * date     12.01.18
 * website  github.com/G00fY2
 */

final class StaticData {

  // position of semver version part
  static final int MAJOR = 0;
  static final int MINOR = 1;
  static final int PATCH = 2;

  // weighting of the prerelease suffixes
  static final int PRE_ALPHA = 0;
  static final int ALPHA = 1;
  static final int BETA = 2;
  static final int RELEASE_CANDIDATE = 3;
  static final int UNKNOWN = 4;

  // only compile the regex pattern once
  static final Pattern NUMERIC = Pattern.compile("[0-9]+");

  private StaticData() {

  }
}
