plugins {
    id("com.android.application")
    id("kotlin-android")
}
android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "io.github.g00fy2.versioncomparesample"
        minSdkVersion(14)
        targetSdkVersion(30)
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
    sourceSets.getByName("main").java.srcDirs("src/main/kotlin")
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    lintOptions {
        disable(
                "UnusedResources",
                "LockedOrientationActivity",
                "HardcodedText",
                "GoogleAppIndexingWarning",
                "Autofill",
                "AllowBackup"
        )
        isAbortOnError = false
        isIgnoreTestSources = true
    }
}

repositories {
    google()
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(project(":versioncompare"))
}