package io.github.mapvina.android.testapp.activity.style

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.mapvina.android.maps.MapVinaMap
import io.github.mapvina.android.maps.MapView
import io.github.mapvina.android.maps.Style
import io.github.mapvina.android.testapp.R
import io.github.mapvina.android.testapp.styles.TestStyles

/**
 * Activity to demonstrate loading a style dynamically from a URL.
 */
class StyleUrlActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private var mapVinaMap: MapVinaMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_style_url)

        val urlInput = findViewById<AutoCompleteTextView>(R.id.urlAutoCompleteTextView)
        val loadButton = findViewById<Button>(R.id.loadButton)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        val styles = arrayOf(
            TestStyles.MAPVINA_STREETS,
            TestStyles.AMERICANA,
            TestStyles.OPENFREEMAP_LIBERTY,
            TestStyles.OPENFREEMAP_BRIGHT,
            TestStyles.AWS_OPEN_DATA_STANDARD_LIGHT,
            TestStyles.PROTOMAPS_LIGHT,
            TestStyles.PROTOMAPS_DARK,
            TestStyles.PROTOMAPS_GRAYSCALE,
            TestStyles.PROTOMAPS_WHITE,
            TestStyles.PROTOMAPS_BLACK
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, styles)
        urlInput.setAdapter(adapter)

        urlInput.setOnClickListener {
            urlInput.showDropDown()
        }

        mapView.getMapAsync { map ->
            mapVinaMap = map
            map.setStyle(Style.Builder().fromUri(urlInput.text.toString())) {
                // Set attribution gravity after style is loaded
                map.uiSettings.setAttributionGravity(android.view.Gravity.BOTTOM or android.view.Gravity.END)
                val margin = (16 * resources.displayMetrics.density).toInt()
                map.uiSettings.setAttributionMargins(margin, margin, margin, margin)
            }
        }

        loadButton.setOnClickListener {
            val url = urlInput.text.toString()
            if (url.isNotEmpty()) {
                mapVinaMap?.setStyle(Style.Builder().fromUri(url)) {
                    Toast.makeText(this, "Style loaded", Toast.LENGTH_SHORT).show()
                }
            }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}
