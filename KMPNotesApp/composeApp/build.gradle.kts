import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kspCompose)
    alias(libs.plugins.room)
    id("com.google.gms.google-services")
    kotlin("plugin.serialization") version "2.0.20"
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
//            export(project("io.github.mirzemehdi:kmpnotifier:1.2.1"))
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    sourceSets.commonMain {
        kotlin.srcDir("build/generated/ksp/metadata")
    }


    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.room.runtime.android)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.animated.navigation.bar)


        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.room.runtime)
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.2")
            implementation("androidx.sqlite:sqlite-bundled:2.5.0-SNAPSHOT") //for sqlite drivers related
//            implementation(libs.androidx.sqlite.bundled.android)
            implementation(libs.kotlinx.datetime)
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.composeVM)
            val voyagerVersion = "1.1.0-beta02"

            //Voyager for navigation
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.bottom.sheet.navigator)
            implementation(libs.voyager.tab.navigator)
            implementation(libs.voyager.transitions)

            implementation(libs.androidx.graphics.shapes)
            //For Logging
            implementation("io.github.aakira:napier:2.7.1")

            //ARuntime Permission Handling

            api("dev.icerock.moko:permissions:0.18.0")
            //For Charting
            implementation("io.github.thechance101:chart:Beta-0.0.5")

            // compose multiplatform
            api("dev.icerock.moko:permissions-compose:0.18.0") // permissions api + compose extensions

            //File Picker
            // Enables FileKit without Compose dependencies
            implementation("io.github.vinceglb:filekit-core:0.8.2")

            // Enables FileKit with Composable utilities
            implementation("io.github.vinceglb:filekit-compose:0.8.2")

            //Firebase
            implementation(project.dependencies.platform("com.google.firebase:firebase-bom:33.2.0"))

            //For Push Notification
            api("io.github.mirzemehdi:kmpnotifier:1.2.1")
            //Gemini App
//            implementation ("com.google.cloud:gapic-google-ai:1.0.0")
            implementation("com.google.ai.client.generativeai:generativeai:0.7.0")



        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
//            implementation(libs.sqlite.driver)

        }
        iosMain.dependencies {
//            implementation(libs.ktor.client.darwin)
//            implementation(libs.native.driver)
        }
    }
}

buildscript {
    repositories {
        mavenCentral()
    }
}
android {
    namespace = "com.jetbrains.notes"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.jetbrains.notes"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

compose.desktop {
    application {
        mainClass = "com.jetbrains.notes.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.jetbrains.notes"
            packageVersion = "1.0.0"
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.core.i18n)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.generativeai)
    //    implementation(libs.androidx.sqlite.bundled.android)
    add("kspCommonMainMetadata", libs.room.compiler)
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

compose.resources {
    publicResClass = false
    packageOfResClass = "me.sample.library.resources"
    generateResClass = auto
}
