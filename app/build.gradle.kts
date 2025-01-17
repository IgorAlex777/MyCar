plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
   // id("com.google.devtools.ksp")
}

android {
    namespace = "com.cmex.mycar"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cmex.mycar"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {

    implementation ("com.davemorrissey.labs:subsampling-scale-image-view-androidx:3.10.0")

  //  implementation ("com.github.barteksc:android-pdf-viewer:2.8.2")
    implementation ("com.github.barteksc:AndroidPdfViewerV1:1.6.0")

    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.vanniktech:android-image-cropper:4.3.3")


    implementation("androidx.room:room-ktx:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation ("androidx.fragment:fragment-ktx:1.6.2")
    kapt ("androidx.room:room-compiler:2.6.1")
   // ksp("androidx.room:room-compiler:2.5.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}


