plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"

}

android {
    namespace = "com.persol.mytodoapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.persol.mytodoapp"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(libs.play.services.fitness)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.support.annotations)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp)
    implementation(libs.converter.scalars)
    implementation (libs.androidx.navigation.compose)
    implementation (libs.androidx.material3.v111)

    implementation(libs.coil.compose.v260)
   

    implementation (libs.converter.gson)

    implementation (libs.gson)



    val room_version = "2.6.1"

    implementation(libs.androidx.room.runtime)

    ksp("androidx.room:room-compiler:$room_version")

    annotationProcessor(libs.androidx.room.room.compiler)

    implementation(libs.androidx.room.ktx)

    implementation(libs.androidx.room.rxjava2)

    implementation(libs.androidx.room.rxjava3)

    implementation(libs.androidx.room.guava)

    testImplementation(libs.androidx.room.testing)

    implementation(libs.androidx.room.paging)
}

