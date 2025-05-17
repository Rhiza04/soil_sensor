package com.example.project1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.project1.databinding.ActivityMainBinding
import com.google.firebase.database.*

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var humidityRef: DatabaseReference
    private lateinit var percentSoilSensorDataRef: DatabaseReference
    private lateinit var RawSoilSensorDataRef: DatabaseReference
    private lateinit var TemperatureRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseDatabase.getInstance().setPersistenceEnabled(false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Referensi untuk mengirim data
        firebaseRef = FirebaseDatabase.getInstance().getReference("test")

        binding.tvSendData.setOnClickListener {
            firebaseRef.setValue("tes kirim data")
                .addOnCompleteListener {
                    Toast.makeText(this, "data stored successfully", Toast.LENGTH_SHORT).show()
                }
        }

        // Referensi untuk menerima data
        humidityRef = FirebaseDatabase.getInstance().getReference("Sensor/humidity")
        receiveHumidityData()

        // Referensi untuk menerima data
        percentSoilSensorDataRef = FirebaseDatabase.getInstance().getReference("Sensor/percent_soil_sensor_data")
        receivepercentSoilSensorData()

        // Referensi untuk menerima data
        RawSoilSensorDataRef = FirebaseDatabase.getInstance().getReference("Sensor/raw_soil_sensor_data")
        receiveRawSoilSensorData()

        // Referensi untuk menerima data
        TemperatureRef = FirebaseDatabase.getInstance().getReference("Sensor/temperature")
        receiveTemperature()
    }

    private fun receiveHumidityData() {
        humidityRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val humidity = snapshot.getValue(Double::class.java)
                if (humidity != null) {
                    binding.tvHumidity.text = "humidity: $humidity%"
                } else {
                    binding.tvHumidity.text = "humidity: -"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load humidity", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun receivepercentSoilSensorData() {
        percentSoilSensorDataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val percent_soil_sensor_data = snapshot.getValue(Double::class.java)
                if (percent_soil_sensor_data != null) {
                    binding.tvpercentSoilSensorData.text = "percent_soil_sensor_data: $percent_soil_sensor_data%"
                } else {
                    binding.tvpercentSoilSensorData.text = "percent_soil_sensor_data: -"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load humidity", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun receiveRawSoilSensorData() {
        RawSoilSensorDataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val raw_soil_sensor_data = snapshot.getValue(Double::class.java)
                if (raw_soil_sensor_data != null) {
                    binding.tvRawSoilSensorData.text = "raw_soil_sensor_data: $raw_soil_sensor_data"
                } else {
                    binding.tvRawSoilSensorData.text = "raw_soil_sensor_data: -"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load humidity", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun receiveTemperature() {
        TemperatureRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temperature = snapshot.getValue(Double::class.java)
                if (temperature != null) {
                    binding.tvTemperature.text = "temperature: $temperature"
                } else {
                    binding.tvTemperature.text = "temperature: -"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load humidity", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
