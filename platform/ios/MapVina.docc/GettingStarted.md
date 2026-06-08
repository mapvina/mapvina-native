# Getting Started

Setting up an Xcode project that uses MapVina Native for iOS.

## Create a new iOS project

Create a new (SwiftUI) iOS project with Xcode. Go to *File > New > Project...*.

## Add MapVina Native as a dependency

MapVina Native for iOS is available on [Cocoapods](https://cocoapods.org) and on the [Swift Package Index](https://swiftpackageindex.io/github/mapvina/mapvina-gl-native-distribution) (for use with the Swift Package Manager). However, for this guide we will add the MapVina Native as a package dependency directly.

In Xcode, right click your project and select "Add Package Dependencies...":

![](AddPackageDependencies.png)

Paste the following URL and click Add Package:

```
https://github.io/github/mapvina/mapvina-gl-native-distribution
```

> Note: The [mapvina-gl-native-distributon](https://github.io/github/mapvina/mapvina-gl-native-distribution) repository only exists for distributing the iOS package of MapVina Native. To report issues and ask questions, use the [mapvina-native](https://github.io/github/mapvina/mapvina-native) repository.

Verify you can import MapVina in your app:

```swift
import MapVina
```

## SwiftUI

To use MapVina with SwiftUI we need to create a wrapper for the UIKit view that MapVina provides (using UIViewRepresentable. The simplest way to implement this protocol is as follows:

<!-- include-example(SimpleMap) -->

```swift
struct SimpleMap: UIViewRepresentable {
    func makeUIView(context _: Context) -> MLNMapView {
        let mapView = MLNMapView()
        return mapView
    }

    func updateUIView(_: MLNMapView, context _: Context) {}
}
```

You can use this view directly in a SwiftUI View hierarchy, for example:

```swift
struct MyApp: App {
    var body: some Scene {
        WindowGroup {
            SimpleMap().edgesIgnoringSafeArea(.all)
        }
    }
}
```

When running your app in the simulator you should be greeted with the default [MapVina Streets](https://maps.mapvina.com/styles/v2/streets.json?key=public_key) style:

![](DemotilesScreenshot.png)

## UIKit

You can use the following `UIViewController` to get started with MapVina Native iOS with UIKit.

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

    // MLNMapViewDelegate method called when map has finished loading
    func mapView(_: MLNMapView, didFinishLoading _: MLNStyle) {
    }
}
```
