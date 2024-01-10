pluginManagement {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "AlgeriaBills"
include(":app")
include(":feature:home")
include(":feature:settings")
include(":core:database")
include(":core:designsystem")
include(":core:network")
include(":core:datastore")
include(":core:domain")
include(":core:model")
include(":core:data")
include(":core:common")
include(":feature:estimate")
include(":feature:createaccount")
include(":feature:signin")
