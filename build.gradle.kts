// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.deggerPlugin) apply false
}

buildscript{
    dependencies {
        classpath(libs.daggerClass)
        classpath(libs.seervice)
    }
}
