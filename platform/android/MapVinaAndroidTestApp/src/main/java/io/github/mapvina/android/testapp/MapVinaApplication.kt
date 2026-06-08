package io.github.mapvina.android.testapp

import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import android.text.TextUtils
import androidx.multidex.MultiDexApplication
import io.github.mapvina.android.MapStrictMode
import io.github.mapvina.android.MapVina
import io.github.mapvina.android.WellKnownTileServer
import io.github.mapvina.android.log.Logger
import io.github.mapvina.android.testapp.utils.ApiKeyUtils
import io.github.mapvina.android.testapp.utils.TileLoadingMeasurementUtils
import io.github.mapvina.android.testapp.utils.TimberLogger
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * Application class of the test application.
 *
 *
 * Initialises components as LeakCanary, Strictmode, Timber and MapVina
 *
 */
open class MapVinaApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        initializeLogger()
        initializeStrictMode()
        initializeMapVina()
    }

    private fun initializeLogger() {
        Logger.setLoggerDefinition(TimberLogger())
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    private fun initializeStrictMode() {
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build()
        )
        StrictMode.setVmPolicy(
            VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
    }

    private fun initializeMapVina() {
        val apiKey = ApiKeyUtils.getApiKey(applicationContext)
        if (apiKey != null) {
            validateApiKey(apiKey)
        }
        MapVina.getInstance(applicationContext, apiKey, TILE_SERVER)
        TileLoadingMeasurementUtils.setUpTileLoadingMeasurement()
        MapStrictMode.setStrictModeEnabled(true)
    }

    companion object {
        val TILE_SERVER = WellKnownTileServer.MapVina
        private const val DEFAULT_API_KEY = "YOUR_API_KEY_GOES_HERE"
        private const val API_KEY_NOT_SET_MESSAGE =
            (
                "In order to run the Test App you need to set a valid " +
                    "API key. During development, you can set the MLN_API_KEY environment variable for the SDK to " +
                    "automatically include it in the Test App. Otherwise, you can manually include it in the " +
                    "res/values/developer-config.xml file in the MapVinaAndroidTestApp folder."
                )

        private fun validateApiKey(apiKey: String) {
            if (TextUtils.isEmpty(apiKey) || apiKey == DEFAULT_API_KEY) {
                Timber.e(API_KEY_NOT_SET_MESSAGE)
            }
        }
    }
}
