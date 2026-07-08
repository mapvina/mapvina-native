plugins {
    alias(libs.plugins.kotlinter)
    id("com.android.application")
    alias(libs.plugins.kotlinPluginSerialization)
    id("mapvina.gradle-make")
    id("mapvina.gradle-config")
    id("mapvina.gradle-checkstyle")
    id("mapvina.gradle-lint")
}

fun obtainTestBuildType(): String {
    return if (project.hasProperty("testBuildType")) {
        project.properties["testBuildType"] as String
    } else {
        "debug"
    }
}

android {
    ndkVersion = Versions.ndkVersion

    compileSdk = 35

    defaultConfig {
        applicationId = "io.github.mapvina.android.testapp"
        minSdk = 23
        targetSdk = 33
        versionCode = 14
        testInstrumentationRunner = "io.github.mapvina.android.InstrumentationRunner"
        multiDexEnabled = true
        versionName = file("../VERSION").readText().trim()

        manifestPlaceholders["SENTRY_DSN"] = ""
        manifestPlaceholders["SENTRY_ENV"] = ""
    }

    nativeBuild(listOf("example-custom-layer"))

    packaging {
        resources.excludes += listOf("META-INF/LICENSE.txt", "META-INF/NOTICE.txt", "LICENSE.txt")
        jniLibs {
            useLegacyPackaging = false
        }
    }

    buildTypes {
        getByName("debug") {
            manifestPlaceholders += mapOf()
            isJniDebuggable = true
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            packaging {
                jniLibs {
                    keepDebugSymbols += "**/*.so"
                }
            }

            buildConfigField("String", "SENTRY_DSN", "\"" + (System.getenv("SENTRY_DSN") ?: "") + "\"")
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
            manifestPlaceholders["SENTRY_DSN"] = System.getenv("SENTRY_DSN") ?: ""
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            testProguardFiles("test-proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")

            buildConfigField("String", "SENTRY_DSN", "\"" + (System.getenv("SENTRY_DSN") ?: "") + "\"")
            manifestPlaceholders["SENTRY_DSN"] = System.getenv("SENTRY_DSN") ?: ""
        }
    }

    testBuildType = obtainTestBuildType()

    flavorDimensions += "renderer"

    productFlavors {
        create("opengl") {
            dimension = "renderer"
            ndk {
                abiFilters += "arm64-v8a"
            }
            externalNativeBuild {
                cmake {
                    arguments("-DMLN_WITH_OPENGL=ON")
                }
            }
        }
        create("vulkan") {
            dimension = "renderer"
            externalNativeBuild {
                cmake {
                    arguments("-DMLN_WITH_VULKAN=ON")
                }
            }
        }
        create("webgpuDawn") {
            dimension = "renderer"
            externalNativeBuild {
                cmake {
                    arguments("-DMLN_WITH_WEBGPU=ON", "-DMLN_WEBGPU_IMPL_DAWN=ON")
                }
            }
        }
        create("webgpuWgpu") {
            dimension = "renderer"
            ndk {
                abiFilters += "arm64-v8a"
            }
            externalNativeBuild {
                cmake {
                    arguments("-DMLN_WITH_WEBGPU=ON", "-DMLN_WEBGPU_IMPL_WGPU=ON")
                }
            }
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    namespace = "io.github.mapvina.android.testapp"

    lint {
        abortOnError = false
        baseline = file("lint-baseline-local.xml")
        checkAllWarnings = true
        disable += listOf("MissingTranslation", "GoogleAppIndexingWarning", "UnpackedNativeCode", "IconDipSize", "TypographyQuotes")
        warningsAsErrors = true
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":MapVinaAndroid"))

    implementation(libs.mapvinaNavigation) {
        exclude(group = "io.github.mapvina", module = "android-sdk")
        exclude(group = "io.github.mapvina", module = "android-sdk-opengl")
    }

    implementation(libs.mapvinaJavaTurf)

    implementation(libs.supportRecyclerView)
    implementation(libs.supportPrint)
    implementation(libs.supportDesign)
    implementation(libs.supportConstraintLayout)
    implementation(libs.kotlinxSerializationJson)

    implementation(libs.multidex)
    implementation(libs.timber)
    implementation(libs.okhttp3)
    implementation(libs.kotlinxCoroutinesCore)
    implementation(libs.kotlinxCoroutinesAndroid)

    debugImplementation(libs.leakCanary)

    androidTestImplementation(libs.supportAnnotations)
    androidTestImplementation(libs.testRunner)
    androidTestImplementation(libs.testRules)
    androidTestImplementation(libs.testEspressoCore)
    androidTestImplementation(libs.testEspressoIntents)
    androidTestImplementation(libs.testEspressoContrib)
    androidTestImplementation(libs.testUiAutomator)
    androidTestImplementation(libs.appCenter)
    androidTestImplementation(libs.androidxTestExtJUnit)
    androidTestImplementation(libs.androidxTestCoreKtx)
    androidTestImplementation(libs.kotlinxCoroutinesTest)
}

apply<SentryConditionalPlugin>()
