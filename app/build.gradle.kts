import java.util.Properties

plugins {
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.kotlin.android)
//    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"
}

android {
    namespace = "com.example.fittrack"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.fittrack"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        // Load the API key from local.properties
        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())
            val apiKey = localProperties.getProperty("MAPS_API_KEY")
            if (apiKey != null) {
                manifestPlaceholders["MAPS_API_KEY"] = apiKey
            } else {
                throw GradleException("MAPS_API_KEY not found in local.properties")
            }
        } else {
            throw GradleException("local.properties file not found")
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
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
        kotlinOptions {
            jvmTarget = "11"
        }
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.11"
        }

        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }

    dependencies {

//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.lifecycle.runtime.ktx)
//    implementation(libs.androidx.activity.compose)
//    implementation(platform(libs.androidx.compose.bom))
//    implementation(libs.androidx.ui)
//    implementation(libs.androidx.ui.graphics)
//    implementation(libs.androidx.ui.tooling.preview)
//    implementation(libs.androidx.material3)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(platform(libs.androidx.compose.bom))
//    androidTestImplementation(libs.androidx.ui.test.junit4)
//    debugImplementation(libs.androidx.ui.tooling)
//    debugImplementation(libs.androidx.ui.test.manifest)

        // Core Android
        implementation(libs.kotlin.core)
        implementation(libs.lifecycle.runtime)
        implementation(libs.activity.compose)

        // Compose
        implementation(platform(libs.compose.bom))
        implementation(libs.compose.ui)
        implementation(libs.compose.ui.graphics)
        implementation(libs.compose.ui.tooling.preview)
        implementation(libs.compose.material3)


        // Navigation
        implementation(libs.navigation.compose)

        // Hilt
        implementation(libs.hilt.android)
        implementation(libs.play.services.location)
        implementation(libs.play.services.maps)
        implementation(libs.firebase.crashlytics.buildtools)
        ksp(libs.hilt.compiler)
        implementation(libs.hilt.navigation.compose)

        // Google Maps SDK
        implementation(libs.play.services.maps)

        implementation(libs.google.gson)

        implementation("com.google.android.gms:play-services-location:21.3.0")
        // Maps Compose library
        implementation(libs.maps.compose)

        // Room
        implementation(libs.room.runtime)
        implementation(libs.room.ktx)
        ksp(libs.room.compiler)

        // DataStore
        implementation(libs.datastore.preferences)

        // Kotlin Serialization
        implementation(libs.kotlinx.serialization)

        // Coroutines
        implementation(libs.kotlinx.coroutines)

        // Health Connect
        implementation(libs.health.connect)

        // Coil for image loading
        implementation(libs.coil.compose)

        // Charts
        implementation(libs.mp.android.chart)

        // Testing
        testImplementation(libs.junit)
        testImplementation(libs.kotlinx.coroutines.test)
        testImplementation(libs.turbine)
        testImplementation(libs.truth)
        testImplementation(libs.mockk)

        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.espresso.core)
        androidTestImplementation(platform(libs.compose.bom))
        androidTestImplementation(libs.compose.ui.test.junit4)

        debugImplementation(libs.compose.ui.tooling)
        debugImplementation(libs.compose.ui.test.manifest)
    }
