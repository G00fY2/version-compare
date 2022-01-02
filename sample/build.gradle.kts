plugins {
  id("com.android.application")
  id("kotlin-android")
}

android {
  compileSdk = 31
  defaultConfig {
    applicationId = "io.github.g00fy2.versioncomparesample"
    minSdk = 14
    targetSdk = 31
    versionCode = 1
    versionName = "1.0"
  }
  buildTypes {
    getByName("release") {
      isShrinkResources = true
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  buildFeatures {
    viewBinding = true
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }
}

tasks.lint {
  enabled = false
}

repositories {
  google()
  mavenCentral()
}

dependencies {
  implementation(project(":versioncompare"))
}