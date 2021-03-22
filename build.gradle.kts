import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application") version "4.1.3" apply false
    kotlin("android") version "1.4.31" apply false
    id("org.sonarqube") version "3.1.1"
}

subprojects {
    apply(plugin = "org.sonarqube")

    sonarqube {
        if (name == "sample") {
            isSkipProject = true
        } else {
            properties {
                property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/jacoco/test/jacocoTestReport.xml")
            }
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
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