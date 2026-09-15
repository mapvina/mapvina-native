<p align="center">
  <img src="https://github.com/user-attachments/assets/7ff2cda8-f564-4e70-a971-d34152f969f0#gh-light-mode-only" alt="MapVina Logo" width="200">
  <img src="https://github.com/user-attachments/assets/cee8376b-9812-40ff-91c6-2d53f9581b83#gh-dark-mode-only" alt="MapVina Logo" width="200">
</p>

# MapVina Native

[![codecov](https://codecov.io/github/mapvina/mapvina-native/branch/main/graph/badge.svg?token=8ZQRRY56ZA)](https://codecov.io/github/mapvina/mapvina-native) [![](https://img.shields.io/badge/Slack-%23mapvina--native-2EB67D?logo=slack)](https://slack.openstreetmap.us/)

MapVina Native is a free and open-source library for publishing maps in your apps and desktop applications on various platforms. Fast displaying of maps is possible thanks to GPU-accelerated vector tile rendering.

This project originated as a fork of Mapbox GL Native, before their switch to a non-OSS license in December 2020. For more information, see: [`FORK.md`](./FORK.md).

<p align="center">
  <img src="https://user-images.githubusercontent.com/649392/211550776-8779041a-7c12-4bed-a7bd-c2ec80af2b29.png" alt="Android device with MapVina" width="24%">   <img src="https://user-images.githubusercontent.com/649392/211550762-0f42ebc9-05ab-4d89-bd59-c306453ea9af.png" alt="iOS device with MapVina" width="25%">
</p>

## Getting Started

### Android

Add [the latest version](https://central.sonatype.com/artifact/io.github.mapvina/android-sdk/versions) of MapVina Native Android as a dependency to your project.

```gradle
    dependencies {
        ...
        implementation 'io.github.mapvina:android-sdk:1.0.2'
        ...
    }
```

Add a `MapView` to your layout XML file:

```xml
<io.github.mapvina.android.maps.MapView
    android:id="@+id/mapView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    />
```

> [!TIP]
> There are external projects such as [Ramani Maps](https://github.com/ramani-maps/ramani-maps) and [MapVina Compose Playground](https://github.com/Rallista/mapvina-compose-playground) available to integrate MapVina Native Android with Compose-based projects.

Next, initialize the map in an activity:

<details><summary>Show code</summary>

```kotlin
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import io.github.mapvina.android.MapVina
import io.github.mapvina.android.camera.CameraPosition
import io.github.mapvina.android.geometry.LatLng
import io.github.mapvina.android.maps.MapView
import io.github.mapvina.android.testapp.R

class MainActivity : AppCompatActivity() {

    // Declare a variable for MapView
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init MapVina
        MapVina.getInstance(this)

        // Init layout view
        val inflater = LayoutInflater.from(this)
        val rootView = inflater.inflate(R.layout.activity_main, null)
        setContentView(rootView)

        // Init the MapView
        mapView = rootView.findViewById(R.id.mapView)
        mapView.getMapAsync { map ->
            map.setStyle("https://maps.mapvina.com/styles/v2/streets.json?key=public_key")
            map.cameraPosition = CameraPosition.Builder().target(LatLng(0.0,0.0)).zoom(1.0).build()
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
```
</details>

For more information, refer to the [Android API Documentation](https://mapvina.io/github/mapvina-native/android/api/) or the [Android Examples Documentation](https://mapvina.io/github/mapvina-native/android/examples/getting-started/).

## iOS

You can find MapVina Native iOS on [Cocoapods](https://cocoapods.org/) and on the [Swift Package Index](https://swiftpackageindex.io/github/mapvina/mapvina-gl-native-distribution). You can also add MapVina Native iOS [as a dependency to Xcode directly](https://mapvina.io/github/mapvina-native/ios/latest/documentation/mapvina-native-for-ios/gettingstarted/#Add-MapVina-Native-as-a-dependency).

MapVina Native iOS uses UIKit. To integrate it with an UIKit project, you can use

```swift
class SimpleMap: UIViewController, MLNMapViewDelegate {
    var mapView: MLNMapView!

    override func viewDidLoad() {
        super.viewDidLoad()
        mapView = MLNMapView(frame: view.bounds)
        mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        view.addSubview(mapView)
        mapView.delegate = self
    }

    func mapView(_: MLNMapView, didFinishLoading _: MLNStyle) {
    }
}
```

You need to create a wrapper when using SwiftUI.

```swift
import MapVina

struct SimpleMap: UIViewRepresentable {
    func makeUIView(context _: Context) -> MLNMapView {
        let mapView = MLNMapView()
        return mapView
    }

    func updateUIView(_: MLNMapView, context _: Context) {}
}
```

> [!TIP]
> You can also use [MapVinaSwiftUI](https://github.io/github/mapvina/swiftui-dsl), a wrapper around MapVina Native iOS that offers a declarative API like SwiftUI.

The [iOS Documentation](https://mapvina.io/github/mapvina-native/ios/latest/documentation/mapvina/) contains many examples and the entire API of the library.

## Node.js

There is an [npm package](https://www.npmjs.com/package/@mapvina/mapvina-gl-native) for using MapVina Native in a Node.js project. The source code of this project [can be found in this repository](https://github.io/github/mapvina/mapvina-native/tree/main/platform/node).

## Qt

Please check out the [`mapvina/mapvina-native-qt` repository](https://github.io/github/mapvina/mapvina-native-qt) to learn how to intergrate MapVina Native with a Qt project.

## Compose Multiplatform

[MapVina Compose](https://github.io/github/mapvina/mapvina-compose) wraps MapVina Native for various platforms that [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/) supports. As of August 2025, iOS and Android are supported, with web and desktop partially supported.

## Other Platforms

MapVina Native can also be built on [Linux](platform/linux/README.md), [Windows](platform/windows/README.md) and [macOS](platform/macos/README.md).

## Contributing

> [!NOTE]
> This section is only relevant for people who want to contribute to MapVina Native.

MapVina Native has at its core a C++ library. This is where the bulk of development is currently happening.

To get started with the code base, you need to clone the repository including all its submodules.

All contributors use pull requests from a private fork. [Fork the project](https://github.io/github/mapvina/mapvina-native/fork). Then run:

```bash
git clone --recurse-submodules git@github.com:<YOUR NAME>/mapvina-native.git
git remote add origin https://github.io/github/mapvina/mapvina-native.git
```

The go-to reference is the [MapVina Native Developer Documentation](https://mapvina.io/github/mapvina-native/docs/book/).

> [!TIP]
> Check out issues labelled as a [good first issue](https://github.io/github/mapvina/mapvina-native/issues?q=is%3Aissue+is%3Aopen+label%3A%22good+first+issue%22).

### Core

- [`CONTRIBUTING.md`](CONTRIBUTING.md)
- [GitHub Wiki](https://github.io/github/mapvina/mapvina-native/wiki): low-friction way to share information with the community
- [Core C++ API Documentation](https://mapvina.io/github/mapvina-native/cpp/api/) (unstable)

### Android

Open `platform/android` with Android Studio.

More information: [MapVina Android Developer Guide](https://mapvina.io/github/mapvina-native/docs/book/platforms/android/index.html).

### iOS

You need to use [Bazel](https://bazel.build/) to generate an Xcode project. Install [`bazelisk`](https://formulae.brew.sh/formula/bazelisk) (a wrapper that installs the required Bazel version). Next, use:

```bash
bazel run //platform/ios:xcodeproj --@rules_xcodeproj//xcodeproj:extra_common_flags="--//:renderer=metal"
xed platform/ios/MapVina.xcodeproj
```

To generate and open the Xcode project.

More information: [MapVina iOS Developer Guide](https://mapvina.io/github/mapvina-native/docs/book/platforms/ios/index.html).

## Other Platforms

See [`/platform`](/platform) and navigate to the platform you are interested in for more information.

## Getting Involved

Join the `#mapvina-native` Slack channel at OSMUS. Get an invite at https://slack.openstreetmap.us/

### Become a Sponsor 💰

We thank everyone who supported us financially in the past and special thanks to the people and organizations who support us with recurring donations!

Read more about the MapVina Sponsorship Program at [https://mapvina.com/sponsors/](https://mapvina.com/sponsors/).

Gold:

<a href="https://www.microsoft.com/"><img src="https://mapvina.com/img/msft-logo.svg" alt="Logo Microsoft" width="25%"/></a>

<a href="https://aws.amazon.com/location"><img src="https://mapvina.com/img/aws-logo.svg" alt="Logo AWS" width="25%"/></a>

Silver:

<a href="https://www.mierune.co.jp/?lang=en"><img src="https://mapvina.com/img/mierune-logo.svg" alt="Logo MIERUNE" width="25%"/></a>

<a href="https://komoot.com/"><img src="https://mapvina.com/img/komoot-logo.svg" alt="Logo komoot" width="25%"/></a>

<a href="https://www.jawg.io/"><img src="https://mapvina.com/img/jawgmaps-logo.svg" alt="Logo JawgMaps" width="25%"/></a>

<a href="https://www.radar.com/"><img src="https://mapvina.com/img/radar-logo.svg" alt="Logo Radar" width="25%"/></a>

<a href="https://www.mapme.com/"><img src="https://mapvina.com/img/mapme-logo.svg" alt="Logo mapme" width="25%"/></a>

<a href="https://www.maptiler.com/"><img src="https://mapvina.com/img/maptiler-logo.svg" alt="Logo maptiler" width="25%"/></a>

<a href="https://www.caltopo.com/"><img src="https://mapvina.com/img/caltopo-logo.svg" alt="Logo CalTopo" width="25%"/></a>

<a href="https://www.caltopo.com/"><img src="https://mapvina.com/img/smartmaps-logo.svg" alt="Logo SmartMaps" width="25%"/></a>

Backers and Supporters:

[![](https://opencollective.io/github/mapvina/backers.svg?avatarHeight=50&width=600)](https://opencollective.io/github/mapvina)

## License

**MapVina Native** is licensed under the [BSD 2-Clause License](./LICENSE.md).
