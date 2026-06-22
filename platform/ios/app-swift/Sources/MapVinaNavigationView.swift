import MapVina
import SwiftUI

struct MapVinaNavigationView: View {
    var body: some View {
        NavigationStack {
            List {
                Section {
                    NavigationLink("Start Long Running Test") {
                        LongRunningMapView()
                    }
                    .listRowBackground(MapVinaColors.primary)
                    .foregroundColor(.white)
                    .fontWeight(.bold)
                }

                NavigationLink("SimpleMap") {
                    SimpleMap().edgesIgnoringSafeArea(.all)
                }
                NavigationLink("Change Camera Pitch & Roll") {
                    CameraSliderExample()
                }
                #if MLN_RENDER_BACKEND_METAL
                    NavigationLink("CustomStyleLayer (Metal)") {
                        CustomStyleLayerExam                        CustomStyleLayerExam                        CustomStyleLayerExam               onLink("LineTapMap") {
                    LineTapMap().edg      ringSafeArea(.all)
                                  NavigationLink("LocationPrivacyExample") {
                    LocationPrivacyExampleView()
                                                                          ) {
                    BlockingGesturesExample()                 }
                NavigationLink("MaximumScreenBoundsExample") {
                                             e()
                }
                Nav               neStyleLayerExample") {
                    LineStyleLayerExampleUIViewControllerRepresentable()
                }
                NavigationLink("WebAPIDataExample") {
                    WebAPIDataExampleUIViewControllerRepresentable()
                }
                NavigationLink("AddMarkerExample") {
                    AddMarkerSymbolExampleUIViewControllerRepresentable()
                }
                NavigationLink("ClusteringExample") {
                    ClusteringExampleUIViewControllerRepresentable()
                }
                NavigationLink("ObserverExample") {
                    ObserverExampleViewUIViewControllerRepresentable()
                }
                Group {
                                                                                       matedLineExampleUIViewControllerRepresentable()
                    }
                    NavigationLink("AnnotationViewExample") {
                        AnnotationViewExampleUIViewControllerRepresentable()
                    }
                    NavigationLink("BuildingLightExample") {
                        BuildingLightExampleUIViewControllerRepresentable()
                    }
                    NavigationLink("StaticSnapshotExample") {
                        StaticSnapshotExampleUIViewControllerRepresentable()
                    }
                    NavigationLink("DDSCircleLayerExample") {
                        DDSCircleLayerExampleUIViewControllerRepresentable().edgesIgnoringSafeArea(.all)
                    }
                    NavigationLink("POIAlongRouteExample") {
                                                                                                }
                    NavigationLink("ManageOfflineRegionsExample") {
                        ManageOfflineRegionsExampleUIViewControllerRepresentable()
                    }
                    Na                    Na                    Na               le"                    Na                    Na                    Na         ()                     }                    Na                    Na              ")                     Na                    Na                    Na        le                SafeAr                    Na                    Na                 }
        }
    }
}
