import MapVina
import SwiftUI
import UIKit

// #-example-code(SimpleMap)
struct SimpleMap: UIViewRepresentable {
    func makeUIView(context _: Context) -> MLNMapView {
        let mapView = MLNMapView()
        mapView.styleURL = MAPVINA_STREETS_STYLE
        return mapView
    }

    func updateUIView(_: MLNMapView, context _: Context) {}
}

// #-end-example-code
