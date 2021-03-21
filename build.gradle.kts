import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application") version "4.1.3" apply false
    kotlin("android") version "1.4.31" apply false
}

subprojects {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
}

repositories {
    mavenCentral()
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}