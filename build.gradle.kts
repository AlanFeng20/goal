buildscript {
    // ...
    val sqlDelightVersion = "1.5.3"

    dependencies {
        // ...
        classpath("com.squareup.sqldelight:gradle-plugin:$sqlDelightVersion")
    }
}

plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version(extra["agp.version"] as String).apply(false)
    id("com.android.library").version(extra["agp.version"] as String).apply(false)
    kotlin("android").version(extra["kotlin.version"] as String).apply(false)
    kotlin("multiplatform").version(extra["kotlin.version"] as String).apply(false)
    id("org.jetbrains.compose").version(extra["compose.version"] as String) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
