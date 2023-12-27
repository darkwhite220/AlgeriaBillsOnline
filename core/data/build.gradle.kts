plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin)
  alias(libs.plugins.hilt)
  alias(libs.plugins.kapt)
}

apply(from="../../common-android-build.gradle")

android {
  namespace = "earth.core.data"
}

dependencies {
  implementation(project(":core:database"))
  implementation(project(":core:datastore"))
  implementation(project(":core:network"))
  implementation(project(":core:model"))
  
  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)
}