plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
}

android {
	namespace = "kr.xnu.keyboard.semen"
	compileSdk = 35

	defaultConfig {
		applicationId = "kr.xnu.keyboard.semen"
		minSdk = 28
		targetSdk = 35
		versionCode = 1
		versionName = "1.0"
	}

	buildTypes {
		release {
			isMinifyEnabled = true
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
	}
}

dependencies {
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.preference.ktx)
	implementation(libs.simple.bencode)

	testImplementation(libs.kotlin.test)
}
