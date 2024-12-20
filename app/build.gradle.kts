plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services") // Required for Firebase services
}

android {
    namespace = "com.example.vortex_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.vortex_app"
        minSdk = 24
        targetSdk = 34
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

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

}

dependencies {
    // Firebase BOM for managing library versions
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-auth:21.3.0") // Firebase Authentication

    // Firebase core services
    implementation("com.google.firebase:firebase-database")       // Realtime Database
    implementation("com.google.firebase:firebase-firestore")      // Firestore
    implementation("com.google.firebase:firebase-storage")        // Storage
    implementation("com.google.firebase:firebase-messaging")      // Cloud Messaging
    implementation("com.google.firebase:firebase-auth")           // Authentication

    // AndroidX libraries
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.activity:activity:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.1")

    // ZXing for QR code scanning and generation
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0") // QR code Android Embedded
    implementation ("com.google.zxing:core:3.3.0")

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    // Geolocation and Maps
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.gms:play-services-maps:18.1.0")

    // Testing libraries
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Mockito for mocking Firebase interactions
    testImplementation("org.mockito:mockito-core:4.8.0")

    // Robolectric for unit testing
    testImplementation("org.robolectric:robolectric:4.10.3")

    // Add AndroidX Test Core for Robolectric compatibility
    testImplementation("androidx.test:core:1.5.0")

    // JUnit
    testImplementation("junit:junit:4.13.2")

    implementation ("com.google.firebase:firebase-storage:20.0.1")
    implementation ("com.google.firebase:firebase-firestore:24.0.0")

}
