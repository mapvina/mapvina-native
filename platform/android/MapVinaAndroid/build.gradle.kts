plugins {
    id("com.android.library")
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.dokka)
    id("mapvina.download-vulkan-validation")
    id("mapvina.gradle-checkstyle")
    id("mapvina.gradle-dependencies-graph")
    id("mapvina.android-nitpick")
    id("mapvina.gradle-publish")
    id("mapvina.artifact-settings")
    id("io.github.mapvina.ccache-plugin")
}

dependencies {
    lintChecks(project(":MapVinaAndroidLint"))
    api(libs.mapvinaJavaGeoJSON)
    api(libs.mapvinaGestures)

    implementation(libs.mapvinaJavaTurf)
    implementation(libs.supportAnnotations)
    implementation(libs.supportFragmentV4)
    implementation(libs.okhttp3)
    implementation(libs.timber)
    implementation(libs.interpolator)

    testImplementation(libs.junit)
    testImplementation(libs.mockito)
    testImplementation(libs.mockk)
    testImplementation(libs.robolectric)
    testImplementation(libs.commonsIO)
    testImplementation(libs.assertjcore)

    androidTestImplementation(libs.testRunner)
    androidTestImplementation(libs.testRules)
}

dokka {
    moduleName.set("MapVina Native Android")
    val dokkaVariantName = "openglRelease"

    dokkaSourceSets {
        configureEach {
            suppress.set(name != dokkaVariantName)
            includes.from("Module.md")

            sourceLink {
                remoteUrl.set(uri("https://github.com/mapvina/mapvina-native/tree/main/platform/android/"))
                localDirectory.set(rootDir)
            }

            // TODO add externalDocumentationLinks when these get dokka or javadocs:
            // - https://github.io/github/mapvina/mapvina-java
            // - https://github.io/github/mapvina/mapvina-gestures-android
        }
    }
}

android {
    ndkVersion = Versions.ndkVersion

    defaultConfig {
        compileSdk = 34
        minSdk = 23
        buildConfigField("String", "GIT_REVISION_SHORT", "\"${getGitRevision()}\"")
        buildConfigField("String", "GIT_REVISION", "\"${getGitRevision(false)}\"")
        buildConfigField(
            "String",
            "MAPVINA_VERSION_STRING",
            "\"MapVina Android/${project.extra["versionName"]}\""
        )
        consumerProguardFiles("proguard-rules.pro")
    }

    flavorDimensions += "renderer"
    productFlavors {
        create("opengl") {
            dimension = "renderer"
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

    sourceSets {
        getByName("opengl") {
            java.srcDirs("src/opengl/java/")
        }
        listOf("webgpuDawn", "webgpuWgpu").forEach {
            getByName(it) {
                java.srcDirs("src/vulkan/java")
                manifest.srcFile("src/vulkan/AndroidManifest.xml")
            }
        }
    }

    // Build native libraries
    val nativeTargets = mutableListOf("mapvina")
    if (project.hasProperty("mapbox.with_test")) {
        nativeTargets.add("mbgl-test")
    }
    if (project.hasProperty("mapbox.with_benchmark")) {
        nativeTargets.add("mbgl-benchmark")
    }
    nativeBuild(nativeTargets)

    // Avoid naming conflicts, force usage of prefix
    resourcePrefix = "mapvina_"

    sourceSets {
        getByName("main") {
            res.srcDirs("src/main/res-public")
        }
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true

            // Robolectric 4.0 required config
            // http://robolectric.org/migrating/#migrating-to-40
            isIncludeAndroidResources = true
        }
        targetSdk = 33
    }

    buildTypes {
        debug {
            isJniDebuggable = true
            enableUnitTestCoverage = false
            enableAndroidTestCoverage = false
        }
    }

    namespace = "io.github.mapvina.android"

    lint {
        checkAllWarnings = true
        disable += listOf(
            "MissingTranslation",
            "TypographyQuotes",
            "ObsoleteLintCustomCheck",
            "MissingPermission",
            "WrongThreadInterprocedural"
        )
        warningsAsErrors = false
        targetSdk = 33
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

fun getGitRevision(shortRev: Boolean = true): String {
    val cmd = if (shortRev) "git rev-parse --short HEAD" else "git rev-parse HEAD"
    val proc = Runtime.getRuntime().exec(cmd)
    return proc.inputStream.bufferedReader().readText().trim()
}

configurations {
    getByName("implementation") {
        exclude(group = "commons-logging", module = "commons-logging")
        exclude(group = "commons-collections", module = "commons-collections")
    }
}

// apply<DownloadVulkanValidationPlugin>()

// intentionally disabled
// apply(plugin = "mapvina.jacoco-report")
