plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.dalawingo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.dalawingo"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
    // Extra resource dirs for tidy layout grouping
    sourceSets {
        getByName("main") {
            res.srcDirs(
                "src/main/res",
                "src/main/res_lessons",
                "src/main/res_study",
                "src/main/res_practice"
            )
            assets.srcDirs("src/main/assets")
        }
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("nl.dionsegijn:konfetti-xml:2.0.2")
    implementation("com.airbnb.android:lottie:6.3.0")
    implementation(libs.ui.text.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
