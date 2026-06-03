pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "Mall"

include(":app")

// Core modules
include(":core:common")
include(":core:ui")
include(":core:network")
include(":core:database")
include(":core:datastore")
include(":core:navigation")
include(":core:designsystem")
include(":core:model")
include(":core:domain")
include(":core:player")

// Feature modules
include(":feature:login")
include(":feature:home")
include(":feature:chat")
include(":feature:conversation")
include(":feature:voice_room")
include(":feature:live")
include(":feature:gift")
include(":feature:wallet")
include(":feature:profile")
include(":feature:setting")
