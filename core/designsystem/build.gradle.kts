plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin)
}

apply(from="../../common-android-build.gradle")

android {
  namespace = "earth.core.designsystem"
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtensionVersion.get()
  }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.bundles.compose)
  
  implementation(libs.lottie)
}