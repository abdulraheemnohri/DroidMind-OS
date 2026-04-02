pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "DroidMindOS"

include(":app")
include(":core:ai_engine")
include(":core:context_engine")
include(":core:vector_memory")
include(":modules:powerforge")
include(":modules:privacyshield")
include(":modules:networkninja")
include(":modules:adblockerx")
include(":modules:neuralcache")
include(":modules:storageSage")
include(":modules:focusflow")
include(":modules:socialsync")
include(":modules:devdock")
include(":services:vpn_service")
include(":services:accessibility_service")
include(":services:automation_engine")
include(":models")
include(":database")
include(":ui")
