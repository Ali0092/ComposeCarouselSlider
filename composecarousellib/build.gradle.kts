plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish")
}

android {
    namespace = "com.example.composecarousellib"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        targetSdk = 35

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

}

dependencies {

    implementation(libs.androidx.core.ktx)

    // Use BOM for consistent Compose versions
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Foundation for Pager
    implementation(libs.androidx.foundation.android)

    // Add Material library for theme attributes
    implementation(libs.material)
    implementation(libs.androidx.appcompat)

    // Runtime
    implementation(libs.androidx.runtime.android)

    // Coil
    implementation(libs.coil.compose)

}


afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.Ali0092"
                artifactId = "composecarousellib"
                version = "1.0.0"
            }
        }

        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/Ali0092/ComposeCarouselSlider")

                credentials {
                    username = findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                    password = findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
                }
            }
        }
    }
}
