Version Compare [![GitHub Actions](
https://github.com/g00fy2/version-compare/actions/workflows/build.yml/badge.svg)](https://github.com/G00fY2/version-compare/actions) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=G00fY2_version-compare&metric=coverage)](https://sonarcloud.io/dashboard?id=G00fY2_version-compare)
=====
Lightweight library for Android, Java and Kotlin to compare version strings.

This library allows you to easily compare version strings. Versions can but do not necessarily have to follow the SemVer convention. Any number of version parts as well as common pre-release suffixes will be taken into account.

Pure Java (java.util), no dependencies, very small method count.

## Download [![Maven Central](https://img.shields.io/maven-central/v/io.github.g00fy2/versioncompare)](https://search.maven.org/artifact/io.github.g00fy2/versioncompare)

<details open>
  <summary>Gradle</summary>

```kotlin
dependencies {
  implementation("io.github.g00fy2:versioncompare:1.5.0")
}
```
</details>
<details>
  <summary>Maven</summary>

```xml
<dependency>
  <groupId>io.github.g00fy2</groupId>
  <artifactId>versioncompare</artifactId>
  <version>1.5.0</version>
</dependency>
```
</details>

> ⚠️ Starting with version 1.4.0 this library moved to MavenCentral. As a result the *groupId* had to be changed. If you use the old `com.g00fy2:versioncompare` artifact check out the migration guide in the [release notes](https://github.com/G00fY2/version-compare/releases/tag/1.4.0).

## Usage
To compare two version strings just create a new Version object. Invalid inputs (*null* or non-numeric first char) will by default be handled as `0.0.0`.

<details open>
  <summary>Kotlin</summary>

You can use the default [comparison operators](https://kotlinlang.org/docs/operator-overloading.html#comparison-operators) to compare version objects:

```kotlin
var result: Boolean

result = Version("1.2.1") > Version("1.2") // result = true

result = Version("1.0.2-rc2") < Version("1.0.2-rc3") // result = true

result = Version("1.3") == Version("1.3.0") // result = true

result = Version("2.1.0") >= Version("2.0") // result = true

result = Version("2.0.0-beta") >= Version("2.0") // result = false
result = Version("2.0.0-beta").isAtLeast("2.0", /* ignoreSuffix: */ true) // result = true
```
</details>

<details>
  <summary>Java</summary>

```java
boolean result;

result = new Version("1.2.1").isHigherThan("1.2"); // result = true

result = new Version("1.0.2-rc2").isLowerThan("1.0.2-rc3"); // result = true

result = new Version("1.3").isEqual("1.3.0"); // result = true

result = new Version("2.0.0-beta").isAtLeast("2.0"); // result = false
result = new Version("2.0.0-beta").isAtLeast("2.0", /* ignoreSuffix: */ true); // result = true
```
</details>

### For more detailed usage, check out the [documentation](https://g00fy2.github.io/version-compare/io/github/g00fy2/versioncompare/Version.html).

## Version structure example
```
Version 1.7.3-rc2.xyz
            +-------+   +-------+   +-------+   +-------+
    String  |   1   | . |   7   | . | 3-rc2 | . |  xyz  |
            +-------+   +-------+   +-------+   +-------+
                |           |         |  |          |
  major  [1] <--            |         |   ----      |
  minor  [7] <--------------          |       | ----
  patch  [3] <------------------------        ||
         ...                            +------------+
                                suffix  |  -rc2.xyz  |
                                        +------------+
-------------------------------------------------------------------------
suffix compare logic                          ||
                                         -----  -----
                                        |            |
                                    +-------+    +-------+
              detected pre-release  |  rc2  |    | .xyz  |  ignored part
                                    +-------+    +-------+
                                       ||
                                    ---  ---
                                   |        |
                                +----+    +---+
                                | rc |    | 2 |  pre-release build
                                +----+    +---+
```

**Notes:**
* expected separator between version numbers is `.`
* suffix and pre-release number do **not need** a special separator `1.1rc == 1.1.rc == 1.1-rc`

### Supported pre-release suffixes
| order | suffix     |
| ----- | --------- |
| 5     | *empty* or *unknown* |
| 4     | rc        |
| 3     | beta      |
| 2     | alpha     |
| 1     | pre + alpha |
| 0     | snapshot |

**Notes:**
* higher order results in higher version `1.0 > 1.0-beta`
* pre-release builds (except for snapshots) are supported `1.0-rc3 > 1.0-rc2`

## Sample App
![Image](https://raw.githubusercontent.com/G00fY2/version-compare/gh-pages/images/version_compare_sampleapp_framed.png)

**Try out the sample app to compare your version inputs: [Download APK](https://github.com/G00fY2/version-compare/releases/download/1.5.0/version-compare-1.5.0-sample.apk)**

## License
    Copyright (C) 2021 Thomas Wirth

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
