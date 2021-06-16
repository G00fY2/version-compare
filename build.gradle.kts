plugins {
  id("com.android.application") version "4.2.1" apply false
  kotlin("android") version "1.5.10" apply false
  id("org.sonarqube") version "3.3"
}

subprojects {
  apply(plugin = "org.sonarqube")

  if (name == "sample") {
    sonarqube.isSkipProject = true
  } else {
    sonarqube.properties {
      property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/jacoco/test/jacocoTestReport.xml")
    }
  }
}

repositories {
  mavenCentral()
}

sonarqube {
  properties {
    property("sonar.projectKey", "G00fY2_version-compare")
    property("sonar.organization", "g00fy2")
    property("sonar.host.url", "https://sonarcloud.io")
  }
}

tasks.register<Delete>("clean") {
  delete(buildDir)
}