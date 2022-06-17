package com.example.android.sensordemo

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.android.sensordemo.databinding.FragmentSensorOverviewBinding

class SensorOverviewFragment : Fragment() {

    private lateinit var binding: FragmentSensorOverviewBinding
    private lateinit var deviceSensors: List<Sensor>
    private lateinit var deviceSensorData: List<SensorData>
    private lateinit var sensorManager: SensorManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentSensorOverviewBinding.inflate(layoutInflater)
        // Get SensorManager to access sensors of device
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Safe all available Sensors in a list
        deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL)

        // Safe data/infos of device sensors in a list
        deviceSensorData = getSensorData(deviceSensors)

        initOverview()

        return binding.root
    }

    // Initializes the sensor overview list
    private fun initOverview() {
        val linearLayout = binding.linearLayout

        var colorSwitch = true

        for (sensorData in deviceSensorData) {
            val textViewName = TextView(requireContext())
            textViewName.setTextAppearance(R.style.Default_TextStyle)
            textViewName.text = sensorData.sensorName
            textViewName.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Sensor Information")
                builder.setItems(sensorData.getDataAsStringArray()) { _, _ -> }

                builder.show()
            }

            if (colorSwitch) {
                textViewName.setBackgroundColor(Color.LTGRAY)
                colorSwitch = false
            } else {
                colorSwitch = true
            }
            linearLayout.addView(textViewName)
        }

        val continueButton = Button(requireContext())
        continueButton.setText(R.string.button_continue)
        continueButton.setOnClickListener {
            findNavController().navigate(R.id.accelerometerDemoFragment)
        }

        linearLayout.addView(continueButton)
    }

    private fun getSensorData(list: List<Sensor>): List<SensorData> {
        val sensorData: MutableList<SensorData> = mutableListOf()

        for (sensor in list) {
            sensorData.add(SensorData(sensor.name,
                sensor.maximumRange,
                sensor.power,
                sensor.resolution,
                sensor.stringType,
                sensor.vendor,
                sensor.isWakeUpSensor))
        }

        return sensorData
    }

    data class SensorData(val sensorName: String,
                          val maximumRange: Float,
                          val power: Float,
                          val resolution: Float,
                          val type: String,
                          val vendor: String,
                          val isWakeUpSensor: Boolean) {
        fun getDataAsStringArray(): Array<String> {
            return arrayOf("Name: $sensorName",
                "Maximum Range: $maximumRange",
                "Powerusage: ${power}mA",
                "Resolution: $resolution",
                "Type: $type",
                "Vendor: $vendor",
                "IsWakeUpSensor: $isWakeUpSensor")
        }

    }
}