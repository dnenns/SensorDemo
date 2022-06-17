package com.example.android.sensordemo

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.android.sensordemo.databinding.FragmentAccelerometerDemoBinding


private const val LOG_TAG = "AccelerometerDemoFragment"

class AccelerometerDemoFragment : Fragment() {

    private lateinit var sensorManager: SensorManager
    private lateinit var sensorEventListener: SensorEventListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAccelerometerDemoBinding.inflate(layoutInflater)

        // Get SensorManager
        sensorManager =
            requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Safe all Sensors of type ACCELEROMETER in a list
        val sensorList: MutableList<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER)

        // Create eventListener
        sensorEventListener = object : SensorEventListener {

            // Define what happens, when the sensor registers an event. Do not block this method
            override fun onSensorChanged(sensorEvent: SensorEvent?) {
                Log.i(LOG_TAG, "onSensorChange called")
                var sensorValues = sensorEvent?.values

                var x = sensorValues?.get(0)
                var y = sensorValues?.get(1)
                var z = sensorValues?.get(2)

                binding.x = x
                binding.y = y
                binding.z = z
            }

            // Called when sensor accuracy has changed. Not really interesting in this example
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            }
        }

        // Check if device actually has at least one sensor of type accelerometer
        if (sensorList.isNotEmpty()) {
            sensorManager.registerListener(
                sensorEventListener,
                sensorList[0],
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        // Method 2 for retrieving a sensor
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        if (sensor != null) {
            sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        binding.continueButton.setOnClickListener {
            findNavController().navigate(R.id.lightSensorDemoFragment)
        }

        return binding.root
    }



    // Always unregister eventListeners to avoid power consumption
    override fun onPause() {
        super.onPause()
        Log.i(LOG_TAG, "onPause called")
        sensorManager.unregisterListener(sensorEventListener)
    }
}