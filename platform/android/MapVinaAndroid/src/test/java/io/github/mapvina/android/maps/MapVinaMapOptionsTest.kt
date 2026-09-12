package io.github.mapvina.android.maps

import android.graphics.Color
import android.view.Gravity
import io.github.mapvina.android.camera.CameraPosition
import io.github.mapvina.android.constants.MapVinaConstants
import io.github.mapvina.android.geometry.LatLng
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import io.github.mapvina.android.BaseTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.*

@RunWith(RobolectricTestRunner::class)
class MapVinaMapOptionsTest : BaseTest() {
    @Test
    fun testSanity() {
        Assert.assertNotNull("should not be null",
            MapVinaMapOptions()
        )
    }

    @Test
    fun testDebugEnabled() {
        Assert.assertFalse(MapVinaMapOptions().debugActive)
        Assert.assertTrue(MapVinaMapOptions().debugActive(true).debugActive)
        Assert.assertFalse(MapVinaMapOptions().debugActive(false).debugActive)
    }

    @Test
    fun testCompassEnabled() {
        Assert.assertTrue(MapVinaMapOptions().compassEnabled(true).compassEnabled)
        Assert.assertFalse(MapVinaMapOptions().compassEnabled(false).compassEnabled)
    }

    @Test
    fun testCompassGravity() {
        Assert.assertEquals(
            Gravity.TOP or Gravity.END,
            MapVinaMapOptions().compassGravity
        )
        Assert.assertEquals(
            Gravity.BOTTOM,
            MapVinaMapOptions().compassGravity(Gravity.BOTTOM).compassGravity
        )
        Assert.assertNotEquals(
            Gravity.START.toLong(),
            MapVinaMapOptions().compassGravity(Gravity.BOTTOM).compassGravity.toLong()
        )
    }

    @Test
    fun testCompassMargins() {
        Assert.assertTrue(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                MapVinaMapOptions()
                    .compassMargins(intArrayOf(0, 1, 2, 3)).compassMargins
            )
        )
        Assert.assertFalse(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                MapVinaMapOptions()
                    .compassMargins(intArrayOf(0, 0, 0, 0)).compassMargins
            )
        )
    }

    @Test
    fun testLogoEnabled() {
        Assert.assertTrue(MapVinaMapOptions().logoEnabled(true).logoEnabled)
        Assert.assertFalse(MapVinaMapOptions().logoEnabled(false).logoEnabled)
    }

    @Test
    fun testLogoGravity() {
        Assert.assertEquals(
            Gravity.BOTTOM or Gravity.START,
            MapVinaMapOptions().logoGravity
        )
        Assert.assertEquals(
            Gravity.BOTTOM,
            MapVinaMapOptions().logoGravity(Gravity.BOTTOM).logoGravity
        )
        Assert.assertNotEquals(
            Gravity.START.toLong(),
            MapVinaMapOptions().logoGravity(Gravity.BOTTOM).logoGravity.toLong()
        )
    }

    @Test
    fun testLogoMargins() {
        Assert.assertTrue(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                MapVinaMapOptions()
                    .logoMargins(intArrayOf(0, 1, 2, 3)).logoMargins
            )
        )
        Assert.assertFalse(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                MapVinaMapOptions()
                    .logoMargins(intArrayOf(0, 0, 0, 0)).logoMargins
            )
        )
    }

    @Test
    fun testAttributionTintColor() {
        Assert.assertEquals(-1, MapVinaMapOptions().attributionTintColor)
        Assert.assertEquals(
            Color.RED,
            MapVinaMapOptions().attributionTintColor(Color.RED).attributionTintColor
        )
    }

    @Test
    fun testAttributionEnabled() {
        Assert.assertTrue(MapVinaMapOptions().attributionEnabled(true).attributionEnabled)
        Assert.assertFalse(MapVinaMapOptions().attributionEnabled(false).attributionEnabled)
    }

    @Test
    fun testAttributionGravity() {
        Assert.assertEquals(
            Gravity.BOTTOM or Gravity.END,
            MapVinaMapOptions().attributionGravity
        )
        Assert.assertEquals(
            Gravity.BOTTOM,
            MapVinaMapOptions().attributionGravity(Gravity.BOTTOM).attributionGravity
        )
        Assert.assertNotEquals(
            Gravity.START.toLong(),
            MapVinaMapOptions().attributionGravity(Gravity.BOTTOM).attributionGravity.toLong()
        )
    }

    @Test
    fun testAttributionMargins() {
        Assert.assertTrue(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                MapVinaMapOptions()
                    .attributionMargins(intArrayOf(0, 1, 2, 3)).attributionMargins
            )
        )
        Assert.assertFalse(
            Arrays.equals(
                intArrayOf(0, 1, 2, 3),
                MapVinaMapOptions()
                    .attributionMargins(intArrayOf(0, 0, 0, 0)).attributionMargins
            )
        )
    }

    @Test
    fun testMinZoom() {
        Assert.assertEquals(
            MapVinaConstants.MINIMUM_ZOOM.toDouble(),
            MapVinaMapOptions().minZoomPreference,
            DELTA
        )
        Assert.assertEquals(
            5.0,
            MapVinaMapOptions().minZoomPreference(5.0).minZoomPreference,
            DELTA
        )
        Assert.assertNotEquals(
            2.0,
            MapVinaMapOptions().minZoomPreference(5.0).minZoomPreference,
            DELTA
        )
    }

    @Test
    fun testMaxZoom() {
        Assert.assertEquals(
            MapVinaConstants.MAXIMUM_ZOOM.toDouble(),
            MapVinaMapOptions().maxZoomPreference,
            DELTA
        )
        Assert.assertEquals(
            5.0,
            MapVinaMapOptions().maxZoomPreference(5.0).maxZoomPreference,
            DELTA
        )
        Assert.assertNotEquals(
            2.0,
            MapVinaMapOptions().maxZoomPreference(5.0).maxZoomPreference,
            DELTA
        )
    }

    @Test
    fun testMinPitch() {
        Assert.assertEquals(
            MapVinaConstants.MINIMUM_PITCH.toDouble(),
            MapVinaMapOptions().minPitchPreference,
            DELTA
        )
        Assert.assertEquals(
            5.0,
            MapVinaMapOptions().minPitchPreference(5.0).minPitchPreference,
            DELTA
        )
        Assert.assertNotEquals(
            2.0,
            MapVinaMapOptions().minPitchPreference(5.0).minPitchPreference,
            DELTA
        )
    }

    @Test
    fun testMaxPitch() {
        Assert.assertEquals(
            MapVinaConstants.MAXIMUM_PITCH.toDouble(),
            MapVinaMapOptions().maxPitchPreference,
            DELTA
        )
        Assert.assertEquals(
            5.0,
            MapVinaMapOptions().maxPitchPreference(5.0).maxPitchPreference,
            DELTA
        )
        Assert.assertNotEquals(
            2.0,
            MapVinaMapOptions().maxPitchPreference(5.0).maxPitchPreference,
            DELTA
        )
    }

    @Test
    fun testTiltGesturesEnabled() {
        Assert.assertTrue(MapVinaMapOptions().tiltGesturesEnabled)
        Assert.assertTrue(MapVinaMapOptions().tiltGesturesEnabled(true).tiltGesturesEnabled)
        Assert.assertFalse(MapVinaMapOptions().tiltGesturesEnabled(false).tiltGesturesEnabled)
    }

    @Test
    fun testScrollGesturesEnabled() {
        Assert.assertTrue(MapVinaMapOptions().scrollGesturesEnabled)
        Assert.assertTrue(MapVinaMapOptions().scrollGesturesEnabled(true).scrollGesturesEnabled)
        Assert.assertFalse(MapVinaMapOptions().scrollGesturesEnabled(false).scrollGesturesEnabled)
    }

    @Test
    fun testHorizontalScrollGesturesEnabled() {
        Assert.assertTrue(MapVinaMapOptions().horizontalScrollGesturesEnabled)
        Assert.assertTrue(MapVinaMapOptions().horizontalScrollGesturesEnabled(true).horizontalScrollGesturesEnabled)
        Assert.assertFalse(MapVinaMapOptions().horizontalScrollGesturesEnabled(false).horizontalScrollGesturesEnabled)
    }

    @Test
    fun testZoomGesturesEnabled() {
        Assert.assertTrue(MapVinaMapOptions().zoomGesturesEnabled)
        Assert.assertTrue(MapVinaMapOptions().zoomGesturesEnabled(true).zoomGesturesEnabled)
        Assert.assertFalse(MapVinaMapOptions().zoomGesturesEnabled(false).zoomGesturesEnabled)
    }

    @Test
    fun testRotateGesturesEnabled() {
        Assert.assertTrue(MapVinaMapOptions().rotateGesturesEnabled)
        Assert.assertTrue(MapVinaMapOptions().rotateGesturesEnabled(true).rotateGesturesEnabled)
        Assert.assertFalse(MapVinaMapOptions().rotateGesturesEnabled(false).rotateGesturesEnabled)
    }

    @Test
    fun testCamera() {
        val position = CameraPosition.Builder().build()
        Assert.assertEquals(
            CameraPosition.Builder(position).build(),
            MapVinaMapOptions().camera(position).camera
        )
        Assert.assertNotEquals(
            CameraPosition.Builder().target(LatLng(1.0, 1.0)),
            MapVinaMapOptions().camera(position)
        )
        Assert.assertNull(MapVinaMapOptions().camera)
    }

    @Test
    fun testPrefetchesTiles() {
        // Default value
        Assert.assertTrue(MapVinaMapOptions().prefetchesTiles)

        // Check mutations
        Assert.assertTrue(MapVinaMapOptions().setPrefetchesTiles(true).prefetchesTiles)
        Assert.assertFalse(MapVinaMapOptions().setPrefetchesTiles(false).prefetchesTiles)
    }

    @Test
    fun testPrefetchZoomDelta() {
        // Default value
        Assert.assertEquals(4, MapVinaMapOptions().prefetchZoomDelta)

        // Check mutations
        Assert.assertEquals(
            5,
            MapVinaMapOptions().setPrefetchZoomDelta(5).prefetchZoomDelta
        )
    }

    @Test
    fun testCrossSourceCollisions() {
        // Default value
        Assert.assertTrue(MapVinaMapOptions().crossSourceCollisions)

        // check mutations
        Assert.assertTrue(MapVinaMapOptions().crossSourceCollisions(true).crossSourceCollisions)
        Assert.assertFalse(MapVinaMapOptions().crossSourceCollisions(false).crossSourceCollisions)
    }

    @Test
    fun testLocalIdeographFontFamily_enabledByDefault() {
        val options = MapVinaMapOptions.createFromAttributes(RuntimeEnvironment.application, null)
        Assert.assertEquals(
            MapVinaConstants.DEFAULT_FONT,
            options.localIdeographFontFamily
        )
    }

    companion object {
        private const val DELTA = 1e-15
    }
}
