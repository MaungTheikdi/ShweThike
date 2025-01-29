plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.theikdi.shwethike"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.theikdi.shwethike"
        minSdk = 26
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation("androidx.recyclerview:recyclerview:1.2.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Print 0.9.1
    implementation("com.github.anggastudio:Printama:0.9.7")

    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    implementation("com.nambimobile.widgets:expandable-fab:1.2.1")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    
}