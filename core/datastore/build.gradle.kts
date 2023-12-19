plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin)
  alias(libs.plugins.hilt)
  alias(libs.plugins.kapt)
}

apply(from="../../common-android-build.gradle")

android {
  namespace = "earth.core.datastore"
}

dependencies {
  implementation(project(":core:model"))
  implementation(libs.androidx.datastore.preferences)
  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)
}