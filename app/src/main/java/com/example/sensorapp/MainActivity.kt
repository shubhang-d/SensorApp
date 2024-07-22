package com.example.sensorapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.getSystemService
import com.example.sensorapp.ui.theme.SensorAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SensorAppTheme {
                MainUI()

            }
        }
    }
}


@Composable
fun MainUI(){
    val context = LocalContext.current
    val sensorManager = getSystemService(context, SensorManager::class.java) as SensorManager
    val GravitySensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
    var sensorAvailable by remember {
        mutableStateOf("No Sensor Detected")
    }
    var sensorIntensityX by remember{
        mutableStateOf(0f)
    }
    var sensorIntensityY by remember{
        mutableStateOf(0f)
    }
    var sensorIntensityZ by remember{
        mutableStateOf(0f)
    }

    val gravitySensorListener: SensorEventListener = object: SensorEventListener{
        override fun onSensorChanged(event: SensorEvent?) {
            if (event != null) {
                if (event.sensor.type == Sensor.TYPE_GRAVITY){
                    sensorIntensityX = event.values[0]
                    sensorIntensityY = event.values[1]
                    sensorIntensityZ = event.values[2]
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            
        }
    }

    if (GravitySensor != null){
        sensorAvailable = "Sensor Detected"
        sensorManager.registerListener(
            gravitySensorListener,
            GravitySensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }else{
        sensorAvailable = "Sensor not Available"
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(sensorAvailable)
            Text(text = "Gravity in X-direction: $sensorIntensityX m/s2")
            Text(text = "Gravity in Y-direction: $sensorIntensityY m/s2")
            Text(text = "Gravity in Z-direction: $sensorIntensityZ m/s2")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SensorAppTheme {

    }
}