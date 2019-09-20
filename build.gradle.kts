import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins { kotlin("jvm") version "1.3.50" }

group = "dev.lunarcoffee"
version = "0.1.0"

repositories { mavenCentral() }

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(group = "io.github.rybalkinsd", name = "kohttp", version = "0.11.0")
}

tasks.withType<KotlinCompile> { kotlinOptions.jvmTarget = "1.8" }
