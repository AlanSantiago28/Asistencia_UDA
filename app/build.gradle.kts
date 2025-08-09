plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.asistenciauda"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.asistenciauda"
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


    }

repositories {
    maven {
        url = uri("https://jitpack.io")
    }
}


dependencies {
    implementation("com.itextpdf:itext7-core:7.1.15")
    //p
    implementation("commons-io:commons-io:2.8.0")  //

    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.google.zxing:core:3.3.0")

    implementation("com.journeyapps:zxing-android-embedded:4.1.0")

    implementation("com.google.android.material:material:1.2.0-alpha3")
    implementation("com.android.volley:volley:1.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}