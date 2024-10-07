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
//    plugins {
//    }
//    resolutionStrategy {
//        eachPlugin {
//            if( requested.id.id == "dagger.hilt.android.plugin") {
//                useModule("com.google.dagger:hilt-android-gradle-plugin:${requested.version}")
//            }
//        }
//    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "WorkoutX"
include(":app")
