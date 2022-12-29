
plugins {
    //trick: for the same plugin versions in all sub-modules
    val android_v = "7.3.1"
    id("com.android.application").version(android_v).apply(false)
    id("com.android.library").version(android_v).apply(false)
    val kt_v = "1.7.20"
    kotlin("android").version(kt_v).apply(false)
    kotlin("multiplatform").version(kt_v).apply(false)
    id("org.jetbrains.compose").version("1.2.2").apply(false)
    id("com.squareup.sqldelight").version("1.5.3").apply(false)
    kotlin("plugin.serialization").version("1.7.21").apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xcontext-receivers")
    }
}
