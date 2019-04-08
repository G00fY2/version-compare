Version Compare [![Download](https://api.bintray.com/packages/g00fy2/maven/version-compare/images/download.svg)](https://bintray.com/g00fy2/maven/version-compare/_latestVersion)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Version%20Compare-blue.svg?style=flat)](https://android-arsenal.com/details/1/6750)
[![codecov](https://codecov.io/gh/G00fY2/version-compare/branch/master/graph/badge.svg) ](https://codecov.io/gh/G00fY2/version-compare)
=====
Lightweight Android & Java library to compare version strings.

This library allows you to easily compare version strings. Versions can but do not necessarily have to follow the SemVer convention. Any number of version parts as well as common pre-release suffixes will be taken into account.

Pure Java (java.util), no dependencies, very small method count.

## Usage
**Gradle:**
```
dependencies {
  implementation 'com.g00fy2:versioncompare:1.3.2'
}
```
**Maven:**
```
<dependency>
  <groupId>com.g00fy2</groupId>
  <artifactId>versioncompare</artifactId>
  <version>1.3.2</version>
</dependency>
```

To compare two version strings just create a new Version object. Invalid inputs (*null* or non-numeric first char) will by default be handled as `0.0.0`.
```java
boolean result;

result = new Version("1.2.1").isHigherThan("1.2"); // result = true

result = new Version("1.0.2-rc2").isLowerThan("1.0.2-rc3"); // result = true

result = new Version("1.3").isEqual("1.3.0"); // result = true
```

### For more detailed usage, check out the [documentation](https://g00fy2.github.io/version-compare/com/g00fy2/versioncompare/Version.html).

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
* the optional suffix does **not need** a special separator

### Supported pre-release suffixes
| order | suffix     |
| ----- | --------- |
| 4     | *empty* or *unknown* |
| 3     | rc        |
| 2     | beta      |
| 1     | alpha     |
| 0     | pre + alpha |

**Notes:**
* higher order results in higher version `1.0 > 1.0-beta`
* pre-release builds are supported `1.0-rc3 > 1.0-rc2`

## Sample App
![Image](https://raw.githubusercontent.com/G00fY2/version-compare/gh-pages/images/version_compare_sampleapp_framed.png)

**Try out the sample app to compare your version inputs: [Download](https://github.com/G00fY2/version-compare/releases/download/1.3.2/version-compare-1.3.2-sample.apk)**

## License
	Copyright (C) 2018 Thomas Wirth

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
