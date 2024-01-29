plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.hilt)
  alias(libs.plugins.kapt)
}

apply(from = "../../common-android-build.gradle")

android {
  namespace = "earth.core.network"
  buildFeatures.buildConfig = true
  
}

dependencies {
  implementation(project(":core:model"))
  
  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)
  
  implementation(libs.kotlinx.serialization.json)
  
  implementation(libs.bundles.ktor)
  
}