plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")


    kotlin("plugin.serialization") version "1.7.21"
    id("com.squareup.sqldelight")
}

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }
    
    sourceSets {

        val coroutinesVersion = "1.6.4"
        val ktorVersion = "2.1.2"
        val sqlDelightVersion = "1.5.3"
        val dateTimeVersion = "0.4.0"
        val okioVersion = "3.2.0"
        val commonMain by getting{

            dependencies{
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$dateTimeVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-client-encoding:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")

                implementation("com.squareup.okio:okio:$okioVersion")
                implementation("com.squareup.sqldelight:runtime:$sqlDelightVersion")
                implementation("com.ctrip.flight.mmkv:mmkv-kotlin:1.2.4")
//                implementation(libs.napier)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("com.squareup.okio:okio-fakefilesystem:$okioVersion")
            }
        }
        val androidMain by getting{
            dependencies{
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                implementation("com.squareup.sqldelight:android-driver:$sqlDelightVersion")
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies{
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
                implementation("com.squareup.sqldelight:native-driver:$sqlDelightVersion")
            }
        }
//        val iosX64Test by getting
//        val iosArm64Test by getting
//        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
//            iosX64Test.dependsOn(this)
//            iosArm64Test.dependsOn(this)
//            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "com.alanfeng.goal"
    compileSdk = 32
    defaultConfig {
        minSdk = 24
        targetSdk = 32
    }
}



sqldelight {
    database("AppDatabase") {
        packageName = "com.alanfeng.goal"
    }
}