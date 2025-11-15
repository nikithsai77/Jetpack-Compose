plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.android.compose"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.android.compose"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.android.compose.HiltTestRunner"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    //Compose dependencies
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
    implementation(libs.navigation.compose)
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    //Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.57.2")
    implementation(libs.androidx.runtime)
    kapt("com.google.dagger:hilt-android-compiler:2.57.2")
    kapt("androidx.hilt:hilt-compiler:1.3.0")

    //Support @HiltViewModel & hiltViewModel() for Compose UI
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")

    //Room
    implementation("androidx.room:room-runtime:2.8.3")
    kapt("androidx.room:room-compiler:2.8.3")

    //Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.8.3")

    //Type-Safe Navigation
    implementation(libs.kotlinx.serialization.json)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Local unit tests
    testImplementation("androidx.test:core:1.7.0")
    // Junit is used to perform all test cases with initial setup
    testImplementation(libs.junit)
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    // Use The Coroutines in test cases.
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    // truth lib
    testImplementation("com.google.truth:truth:1.4.5")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.9.4")

    // Instrumentation tests
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.57.2")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.57.2")
    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    androidTestImplementation("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation("com.google.truth:truth:1.4.5")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test:core-ktx:1.7.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
}
