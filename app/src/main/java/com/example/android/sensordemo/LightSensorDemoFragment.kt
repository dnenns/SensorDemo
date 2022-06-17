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
import com.example.android.sensordemo.databinding.FragmentAccelerometerDemoBinding
import com.example.android.sensordemo.databinding.FragmentLightSensorDemoBinding

private const val LOG_TAG = "LightSensorDemoFragment"


class LightSensorDemoFragment : Fragment() {

    private lateinit var sensorManager: SensorManager
    private lateinit var sensorEventListener: SensorEventListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentLightSensorDemoBinding.inflate(layoutInflater)

        sensorManager =
            requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(sensorEvent: SensorEvent?) {
                Log.i(LOG_TAG, "onSensorChanged called")

                var sensorValues = sensorEvent?.values

                binding.sensorValue = sensorValues?.get(0)
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

            }
        }

        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        if (sensor != null) {
            sensorManager.registerListener(
                sensorEventListener,
                sensor,
                // Limited to 200 Hz. If a higher sampling rate is needed, user must be asked for permission
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        Log.i(LOG_TAG, "onPause called")
        sensorManager.unregisterListener(sensorEventListener)
    }
}