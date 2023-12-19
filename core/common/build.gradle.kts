plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin)
}

apply(from="../../common-android-build.gradle")

android {
  namespace = "earth.core.common"
}

dependencies {
  implementation(libs.kotlinx.coroutines.android)
}