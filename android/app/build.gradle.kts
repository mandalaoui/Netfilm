plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.androidapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.androidapp"
//        minSdk = 21
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
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
    configurations.all {
        resolutionStrategy {
//            force ("androidx.room:room-compiler:2.5.0")
//            force ("com.google.android.material:material:1.9.0")
//            force ("androidx.appcompat:appcompat:1.6.0")
        }
    }
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.google.code.gson:gson:2.8.9")
//    implementation (libs.exoplayer.v2181)
//    implementation ("com.google.android.exoplayer2:exoplayer:2.18.0")
//    implementation("com.google.android.exoplayer2:exoplayer-ui:2.18.0")
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
    implementation ("com.google.android.material:material:1.8.0")
    implementation ("androidx.room:room-runtime:2.6.0")
    annotationProcessor ("androidx.room:room-compiler:2.6.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")
    implementation("com.google.android.exoplayer:exoplayer:2.15.1")
    implementation ("androidx.media3:media3-exoplayer:1.0.0")
    implementation ("androidx.media3:media3-ui:1.0.0")
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
//    implementation(libs.appcompat)
//    implementation(libs.material)
    implementation(libs.activity)
//    implementation(libs.constraintlayout)
//    implementation(libs.swiperefreshlayout)

    implementation(libs.room.common)
    implementation(libs.media3.ui)
//    implementation(libs.room.runtime)
    implementation(libs.adapters)
    implementation(libs.media3.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
//    implementation("com.github.bumptech.glide:glide:4.15.0")
//    annotationProcessor("com.github.bumptech.glide:compiler:4.15.0")
//    implementation ("com.github.bumptech.glide:glide:4.13.2")
//    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.2")
//    annotationProcessor(libs.room.compiler)

//    implementation ("androidx.room:room-runtime:2.5.0")
//    annotationProcessor ("androidx.room:room-compiler:2.5.0")

//    implementation ("androidx.appcompat:appcompat:1.3.1")
    implementation ("androidx.appcompat:appcompat:1.6.0")
//    implementation ("com.google.android.material:material:1.6.0")
//    implementation ("com.google.android.material:material:1.9.0")
//    implementation ("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.coordinatorlayout:coordinatorlayout:1.2.0")



    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
    implementation ("com.google.android.material:material:1.8.0")
    implementation ("androidx.room:room-runtime:2.6.0")
    annotationProcessor ("androidx.room:room-compiler:2.6.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")
    implementation("com.google.android.exoplayer:exoplayer:2.15.1")
    implementation ("androidx.media3:media3-exoplayer:1.0.0")
    implementation ("androidx.media3:media3-ui:1.0.0")
}