@file:Suppress("UnstableApiUsage")

//import com.google.protobuf.gradle.* // ProtoDataStore 쓰려다가 필요 없을 것 같아서 중단.


plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
//    id("com.google.protobuf") version "0.9.1"
}

android {
    namespace = "com.hippoddung.ribbit"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.hippoddung.ribbit"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlin {
        // Or shorter:
        jvmToolchain(8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Import the Compose BOM
    implementation(platform("androidx.compose:compose-bom:2023.06.01"))
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.appcompat:appcompat:1.6.1")
    //Compose
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    // Lifecycle
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:${rootProject.extra["lifecycle_version"]}")
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.6.0")
    // Preferences DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    // Proto DataStore
//    implementation("androidx.datastore:datastore:1.0.0")
//    implementation("com.google.protobuf:protobuf-javalite:3.18.0")

    // Work
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    // Retrofit with Kotlin serialization Converter
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    // DI to Compose
    implementation("com.google.dagger:hilt-android:${rootProject.extra["dagger_version"]}")
    implementation("com.google.dagger:hilt-android:${rootProject.extra["dagger_version"]}")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    kapt("com.google.dagger:hilt-android-compiler:${rootProject.extra["dagger_version"]}")
    implementation("com.google.dagger:dagger:${rootProject.extra["dagger_version"]}")
    kapt("com.google.dagger:dagger-compiler:${rootProject.extra["dagger_version"]}")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    // load Image, Video from URL
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("androidx.webkit:webkit:1.6.0")
    // google play services ads
    implementation("com.google.android.gms:play-services-ads:22.2.0")


    //Test and Debug
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
kapt {
    correctErrorTypes = true
}

//protobuf {
//    protoc {
//        artifact = "com.google.potobuf:protoc:3.21.7"
//    }
//    generateProtoTasks {
//        all().forEach { task ->
//            task.builtins{
//                java{
//                    "lite"
//                }
//            }
//        }
//    }
//}