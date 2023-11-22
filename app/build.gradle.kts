plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.gosu.jungwon"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.gosu.jungwon"
        minSdk = 24
        targetSdk = 33
        versionCode = 23
        versionName = "3.2"

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
    buildFeatures{
        viewBinding=true
    }
    buildToolsVersion = "34.0.0"
}

    dependencies {
        // 기본 Android 관련 라이브러리들
        implementation("androidx.core:core-ktx:1.9.0")
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("com.google.android.material:material:1.8.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")

        // Google Mobile Ads SDK (AdMob) - 광고를 앱에 표시하기 위한 라이브러리
        implementation("com.google.android.gms:play-services-ads:22.4.0")

        implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
        implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
        annotationProcessor ("androidx.lifecycle:lifecycle-compiler:2.4.1")

        // 단위 테스트와 안드로이드 UI 테스트를 위한 라이브러리들
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.")
        implementation ("com.ramotion.circlemenu:circle-menu:0.3.2")


        // Google Play In-App Update - 앱 내 업데이트 기능을 사용하기 위한 라이브러리
        // 이 두 개의 의존성은 앱의 업데이트 상태를 확인하고, 사용자에게 업데이트를 요청하는 등의 기능을 제공합니다.
        implementation("com.google.android.play:core:1.10.3")//https://developer.android.com/reference/com/google/android/play/core/release-notes
        implementation("com.google.android.play:core-ktx:1.8.1")//https://developer.android.com/kotlin/ktx?hl=ko

    }


