package com.example.project1

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.firebase.database.*

class MainActivity : ComponentActivity() {
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var humidityRef: DatabaseReference
    private lateinit var percentSoilSensorDataRef: DatabaseReference
    private lateinit var rawSoilSensorDataRef: DatabaseReference
    private lateinit var temperatureRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi Firebase
        FirebaseDatabase.getInstance().setPersistenceEnabled(false)
        firebaseRef = FirebaseDatabase.getInstance().getReference("test")

        // Buat layout utama
        val layout = ConstraintLayout(this).apply {
            id = View.generateViewId()
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        // Buat semua TextView
        val tvSendData = TextView(this).apply {
            id = View.generateViewId()
            text = "Tekan ini untuk mengirim data ke Firebase"
            textSize = 16f
        }

        val tvHumidity = TextView(this).apply {
            id = View.generateViewId()
            text = "humidity: -"
        }

        val tvpercentSoilSensorData = TextView(this).apply {
            id = View.generateViewId()
            text = "percent_soil_sensor_data: -"
        }

        val tvRawSoilSensorData = TextView(this).apply {
            id = View.generateViewId()
            text = "raw_soil_sensor_data: -"
        }

        val tvTemperature = TextView(this).apply {
            id = View.generateViewId()
            text = "temperature: -"
        }

        // Tambahkan semua view ke layout
        layout.addView(tvSendData)
        layout.addView(tvHumidity)
        layout.addView(tvpercentSoilSensorData)
        layout.addView(tvRawSoilSensorData)
        layout.addView(tvTemperature)

        // Atur posisi semua TextView
        ConstraintSet().apply {
            clone(layout)

            connect(tvSendData.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 320)
            connect(tvSendData.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 64)

            connect(tvHumidity.id, ConstraintSet.TOP, tvSendData.id, ConstraintSet.BOTTOM, 28)
            connect(tvHumidity.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)

            connect(tvpercentSoilSensorData.id, ConstraintSet.TOP, tvSendData.id, ConstraintSet.BOTTOM, 60)
            connect(tvpercentSoilSensorData.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)

            connect(tvRawSoilSensorData.id, ConstraintSet.TOP, tvSendData.id, ConstraintSet.BOTTOM, 92)
            connect(tvRawSoilSensorData.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)

            connect(tvTemperature.id, ConstraintSet.TOP, tvSendData.id, ConstraintSet.BOTTOM, 124)
            connect(tvTemperature.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)

            applyTo(layout)
        }

        // Listener tombol kirim data
        tvSendData.setOnClickListener {
            firebaseRef.setValue("tes kirim data")
                .addOnCompleteListener {
                    Toast.makeText(this, "data stored successfully", Toast.LENGTH_SHORT).show()
                }
        }

        // Inisialisasi referensi data sensor
        humidityRef = FirebaseDatabase.getInstance().getReference("Sensor/humidity")
        percentSoilSensorDataRef = FirebaseDatabase.getInstance().getReference("Sensor/percent_soil_sensor_data")
        rawSoilSensorDataRef = FirebaseDatabase.getInstance().getReference("Sensor/raw_soil_sensor_data")
        temperatureRef = FirebaseDatabase.getInstance().getReference("Sensor/temperature")

        // Jalankan fungsi untuk menerima data
        receiveHumidityData(tvHumidity)
        receivepercentSoilSensorData(tvpercentSoilSensorData)
        receiveRawSoilSensorData(tvRawSoilSensorData)
        receiveTemperature(tvTemperature)

        // Set tampilan ke layout yang kita buat
        setContentView(layout)
    }

    private fun receiveHumidityData(tvHumidity: TextView) {
        humidityRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val humidity = snapshot.getValue(Double::class.java)
                tvHumidity.text = if (humidity != null) "humidity: $humidity%" else "humidity: -"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load humidity", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun receivepercentSoilSensorData(tv: TextView) {
        percentSoilSensorDataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(Double::class.java)
                tv.text = if (value != null) "percent_soil_sensor_data: $value%" else "percent_soil_sensor_data: -"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load percent_soil_sensor_data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun receiveRawSoilSensorData(tv: TextView) {
        rawSoilSensorDataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(Double::class.java)
                tv.text = if (value != null) "raw_soil_sensor_data: $value" else "raw_soil_sensor_data: -"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load raw_soil_sensor_data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun receiveTemperature(tv: TextView) {
        temperatureRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(Double::class.java)
                tv.text = if (value != null) "temperature: $value" else "temperature: -"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load temperature", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
