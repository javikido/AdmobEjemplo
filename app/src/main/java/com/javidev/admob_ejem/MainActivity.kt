package com.javidev.admob_ejem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.javidev.admob_ejem.ui.theme.Admob_ejemTheme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // INICIALIZAR EL SDK DE ADMOB
        //MobileAds.initialize(this) { "ca-app-pub-4645661747813939~7522552247"}

        setContent {

            Admob_ejemTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        AdmobEjem()
                        Spacer(modifier = Modifier.height(20.dp))
                        AdmobEjemploLista()
                        AdvertView()
                    }

                }

            }
        }
    }
}

@Composable
fun AdmobEjem() {

    Column(
        modifier = Modifier
            .background(Color.Yellow)
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Regular Banner")

        // shows a banner test ad
        AndroidView(
            factory = { context ->
                AdView(context).apply {
                    adSize = AdSize.BANNER
                    adUnitId = "ca-app-pub-3940256099942544/6300978111"
                    loadAd(AdRequest.Builder().build())
                }
            }
        )

    }
}


@Composable
fun AdvertView(modifier: Modifier = Modifier) {
    val isInEditMode = LocalInspectionMode.current
    if (isInEditMode)
        Text(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(horizontal = 2.dp, vertical = 6.dp),
            textAlign = TextAlign.Center,
            color = Color.White,
            text = "Advert Here",
        )
    else
        AndroidView(
            modifier = modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    adSize = AdSize.BANNER
                    adUnitId = "ca-app-pub-3940256099942544/6300978111"
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
}


@Composable
fun AdmobEjemploLista() {

    val deviceCurrentWidth = LocalConfiguration.current.screenWidthDp
    val padding = 16
    var i by remember { mutableStateOf(0) }
    var containerWidth by remember { mutableStateOf<Int?>(null) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = Modifier
            .padding(padding.dp)
            .fillMaxWidth()
            .onSizeChanged {
                containerWidth = it.width
            }
    ) {
        val items =
            listOf(
                "deviceCurrentWidth - 40" to deviceCurrentWidth - 40,
                "deviceCurrentWidth - padding * 2" to deviceCurrentWidth - padding * 2,
                "AdSize.FULL_WIDTH" to AdSize.FULL_WIDTH,
                "onSizeChanged" to with(LocalDensity.current) {
                    containerWidth?.let { containerWidth ->
                        (containerWidth / density).roundToInt()
                    }
                }
            )
        items.forEach {
            val (title, width) = it
            if (width == null) {
                return@forEach
            }

            Text(title)
            AndroidView(
                factory = { context ->
                    AdView(context).apply {
                        adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                            context,
                            width
                        )
                        adUnitId = "ca-app-pub-3940256099942544/6300978111"
                        loadAd(AdRequest.Builder().build())
                    }
                },
                update = { adView ->
                    adView.loadAd(AdRequest.Builder().build())
                    i // needed to update view on i change
                },
            )
        }
        LaunchedEffect(Unit) {
            while (true) {
                delay(1000)
                i++
            }
        }
    }

}


