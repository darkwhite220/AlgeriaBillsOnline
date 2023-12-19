plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin)
  alias(libs.plugins.hilt)
  alias(libs.plugins.kapt)
  alias(libs.plugins.ksp)
}

apply(from="../../common-android-build.gradle")

android {
  namespace = "earth.code.database"
}

dependencies {
  implementation(libs.bundles.room)
  ksp(libs.androidx.room.compiler)
  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)
}