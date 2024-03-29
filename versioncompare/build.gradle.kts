plugins {
  `java-library`
  jacoco
  `maven-publish`
  signing
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

jacoco {
  toolVersion = "0.8.8"
}

tasks.test {
  testLogging.events("failed", "passed", "skipped")
  finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
  reports {
    xml.required.set(true)
    csv.required.set(false)
  }
}

repositories {
  mavenCentral()
}

dependencies {
  compileOnly("org.jetbrains:annotations:23.0.0")

  testImplementation("junit:junit:4.13.2")
  testImplementation("nl.jqno.equalsverifier:equalsverifier:3.10")
}

group = "io.github.g00fy2"
version = "1.5.0"
rootProject.version = "1.5.0" // set version for sonarcloud

tasks.register<Jar>("javadocJar") {
  archiveClassifier.set("javadoc")
  from("$buildDir/docs/javadoc")
  dependsOn("javadoc")
}

tasks.register<Jar>("sourcesJar") {
  archiveClassifier.set("sources")
  from(sourceSets.getByName("main").java.srcDirs)
}

artifacts {
  archives(tasks.named("sourcesJar"))
  archives(tasks.named("javadocJar"))
}

publishing {
  publications {
    create<MavenPublication>("release") {
      from(components["java"])
      artifactId = project.name
      artifact(tasks.named("javadocJar"))
      artifact(tasks.named("sourcesJar"))
      pom {
        name.set(project.name)
        description.set("Lightweight library to compare version strings.")
        url.set("https://github.com/G00fY2/version-compare")
        licenses {
          license {
            name.set("The Apache Software License, Version 2.0")
            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
          }
        }
        developers {
          developer {
            id.set("g00fy2")
            name.set("Thomas Wirth")
            email.set("twirth.development@gmail.com")
          }
        }
        scm {
          connection.set("https://github.com/G00fY2/version-compare.git")
          developerConnection.set("https://github.com/G00fY2/version-compare.git")
          url.set("https://github.com/G00fY2/version-compare")
        }
      }
    }
  }
  repositories {
    maven {
      name = "sonatype"
      url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
      credentials {
        username = project.findStringProperty("sonatypeUsername")
        password = project.findStringProperty("sonatypePassword")
      }
    }
  }
}

signing {
  project.findStringProperty("signing.keyId")
  project.findStringProperty("signing.password")
  project.findStringProperty("signing.secretKeyRingFile")
  sign(publishing.publications)
}

fun Project.findStringProperty(propertyName: String): String? {
  return findProperty(propertyName) as String? ?: run {
    println("$propertyName missing in gradle.properties")
    null
  }
}