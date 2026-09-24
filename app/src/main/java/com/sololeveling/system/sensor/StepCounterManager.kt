package com.sololeveling.system.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class StepCounterManager(private val context: Context) : SensorEventListener {

    private val sensorManager: SensorManager? =
        context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager

    private var stepCounterSensor: Sensor? = null
    private var stepDetectorSensor: Sensor? = null

    private var initialStepCount = -1f
    private var accumulatedSteps = 0

    private var onStepUpdateListener: ((Int) -> Unit)? = null

    init {
        stepCounterSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepDetectorSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
    }

    val isSensorAvailable: Boolean
        get() = stepCounterSensor != null || stepDetectorSensor != null

    fun startTracking(onStepUpdate: (Int) -> Unit) {
        this.onStepUpdateListener = onStepUpdate
        initialStepCount = -1f
        accumulatedSteps = 0

        stepCounterSensor?.let {
            sensorManager?.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_UI
            )
        } ?: stepDetectorSensor?.let {
            sensorManager?.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    fun stopTracking() {
        sensorManager?.unregisterListener(this)
        onStepUpdateListener = null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            val totalStepsSinceReboot = event.values[0]
            if (initialStepCount < 0) {
                initialStepCount = totalStepsSinceReboot
            }
            val currentSessionSteps = (totalStepsSinceReboot - initialStepCount).toInt()
            if (currentSessionSteps > 0) {
                onStepUpdateListener?.invoke(currentSessionSteps)
            }
        } else if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0f) {
                accumulatedSteps += 1
                onStepUpdateListener?.invoke(accumulatedSteps)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No-op
    }
}
