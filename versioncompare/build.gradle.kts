plugins {
    `java-library`
    jacoco
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

jacoco {
    toolVersion = "0.8.6"
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        csv.isEnabled = false
    }
}

repositories {
    google()
    mavenCentral()
    jcenter()
}

dependencies {
    compileOnly("com.google.code.findbugs:jsr305:3.0.2") // only required at compile time

    testImplementation("junit:junit:4.13.1")
    testImplementation("nl.jqno.equalsverifier:equalsverifier:3.5")
}

apply(from = "$projectDir/deploy.gradle")