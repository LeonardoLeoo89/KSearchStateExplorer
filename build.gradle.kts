plugins {
    kotlin("jvm") version "2.1.10" apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    if (project.path.startsWith(":examples:")) {
        pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            dependencies {
                "implementation"("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
            }
        }
    }
}