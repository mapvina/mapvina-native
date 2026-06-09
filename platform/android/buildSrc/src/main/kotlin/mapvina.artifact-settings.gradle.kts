extra["mapVinaArtifactGroupId"] = "io.github.mapvina"
extra["mapVinaArtifactId"] = "android-sdk"
extra["mapVinaArtifactTitle"] = "MapVina Android"
extra["mapVinaArtifactDescription"] = "MapVina Android"
extra["mapVinaDeveloperName"] = "MapVina"
extra["mapVinaDeveloperId"] = "mapvina"
extra["mapVinaArtifactUrl"] = "https://github.com/mapvina/mapvina-native"
extra["mapVinaArtifactScmUrl"] = "scm:git@github.com:mapvina/mapvina-native.git"
extra["mapVinaArtifactLicenseName"] = "BSD"
extra["mapVinaArtifactLicenseUrl"] = "https://opensource.org/licenses/BSD-2-Clause"

val versionFilePath = rootDir.resolve("VERSION")
val versionName = if (versionFilePath.exists()) {
    versionFilePath.readText().trim()
} else {
    throw GradleException("VERSION file not found at ${versionFilePath.absolutePath}")
}

extra["versionName"] = versionName
