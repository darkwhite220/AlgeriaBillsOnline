plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

apply(from = "../../common-android-build.gradle")

android {
    namespace = "earth.code.database"
    
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(project(":core:model"))
  
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)
    
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}