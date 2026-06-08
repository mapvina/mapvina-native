package io.github.mapvina.android.maps

import androidx.test.annotation.UiThreadTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import io.github.mapvina.android.AppCenter
import io.github.mapvina.android.MapVina
import io.github.mapvina.android.exceptions.MapVinaConfigurationException

@RunWith(AndroidJUnit4ClassRunner::class)
class MapVinaTest : AppCenter() {
    private var realToken: String? = null
    @Before
    fun setup() {
        realToken = MapVina.getApiKey()
    }

    @Test
    @UiThreadTest
    fun testConnected() {
        Assert.assertTrue(MapVina.isConnected())

        // test manual connectivity
        MapVina.setConnected(true)
        Assert.assertTrue(MapVina.isConnected())
        MapVina.setConnected(false)
        Assert.assertFalse(MapVina.isConnected())

        // reset to Android connectivity
        MapVina.setConnected(null)
        Assert.assertTrue(MapVina.isConnected())
    }

    @Test
    @UiThreadTest
    fun setApiKey() {
        MapVina.setApiKey(API_KEY)
        Assert.assertSame(API_KEY, MapVina.getApiKey())
        MapVina.setApiKey(API_KEY_2)
        Assert.assertSame(API_KEY_2, MapVina.getApiKey())
    }

    @Test
    @UiThreadTest
    fun setNullApiKey() {
        Assert.assertThrows(
            MapVinaConfigurationException::class.java
        ) { MapVina.setApiKey(null) }
    }

    @After
    fun tearDown() {
        if (realToken?.isNotEmpty() == true) {
            MapVina.setApiKey(realToken)
        }

    }

    companion object {
        private const val API_KEY = "pk.0000000001"
        private const val API_KEY_2 = "pk.0000000002"
    }
}
