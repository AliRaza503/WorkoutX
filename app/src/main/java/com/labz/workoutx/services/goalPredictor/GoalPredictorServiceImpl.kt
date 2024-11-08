package com.labz.workoutx.services.goalPredictor

import android.app.Application
import com.labz.workoutx.models.Gender
import com.labz.workoutx.models.Goal
import com.labz.workoutx.models.User
import com.labz.workoutx.utils.Consts
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Calendar

class GoalPredictorServiceImpl : GoalPredictorService {
    private lateinit var interpreter: Interpreter
    private lateinit var byteBuffer: ByteBuffer

    // Calculate BMI
    private fun calculateBMI(weight: Float, height: Float): Float {
        return weight / ((height / 100) * (height / 100))
    }

    private fun calculateAge(dob: Calendar): Float {
        val today = Calendar.getInstance()
        return (today.timeInMillis - dob.timeInMillis) / (1000 * 60 * 60 * 24 * 365.25f)
    }

    private fun calculateGender(gender: Gender): Float = if (gender == Gender.MALE) 1f else 0f

    private fun calculateGenderOther(gender: Gender): Float =
        if (gender == Gender.PREFER_NOT_TO_SAY) 1f else 0f


    // Preprocess input data
    private fun initByteBuffer(
        dob: Calendar,
        gender: Gender,
        weight: Float,
        height: Float,
        caloriesBurned: Float,
        workoutTime: Float
    ) {
        val byteBuffer =
            ByteBuffer.allocateDirect(8 * 4)  // 8 input features, 4 bytes each (float32)
        byteBuffer.order(ByteOrder.nativeOrder())
        byteBuffer.putFloat(calculateAge(dob))
        byteBuffer.putFloat(weight)
        byteBuffer.putFloat(height)
        byteBuffer.putFloat(calculateBMI(weight, height))
        byteBuffer.putFloat(caloriesBurned)
        byteBuffer.putFloat(workoutTime)
        byteBuffer.putFloat(calculateGender(gender))
        byteBuffer.putFloat(calculateGenderOther(gender))
        this.byteBuffer = byteBuffer
    }

    // Interpret the output (customize according to your classes)
    private fun interpretOutput(output: FloatArray): Goal {
//        val classNames = listOf("Lose Weight", "Gain Weight", "Maintain Weight", "Build Muscles")
        val maxIndex = output.indices.maxByOrNull { output[it] } ?: -1
        return when (maxIndex) {
            2 -> Goal.LOSE_WEIGHT
            1 -> Goal.GAIN_WEIGHT
            3 -> Goal.MAINTAIN_WEIGHT
            0 -> Goal.BUILD_MUSCLE
            else -> throw IllegalArgumentException("Unknown Goal value: $maxIndex")
        }
    }

    override fun preProcessData(
        application: Application,
        avgCalories: Double,
        avgWorkoutMinutes: Double
    ) {
        // Load the TFLite model from assets
        interpreter = Interpreter(
            FileUtil.loadMappedFile(
                application.applicationContext,
                Consts.getModelPath(),
            )
        )
        if (User.infoIsWellSet()) {
            initByteBuffer(
                dob = User.dateOfBirth!!,
                gender = User.gender!!,
                weight = User.weightInKgs.toFloat(),
                height = User.heightInCms.toFloat(),
                caloriesBurned = avgCalories.toFloat(),
                workoutTime = avgWorkoutMinutes.toFloat(),
            )
        }
    }

    override fun predictGoal(): Goal {
        var goal: Goal
        try {
            val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 4), DataType.FLOAT32)
            interpreter.run(byteBuffer, outputBuffer.buffer.rewind())
            // Get the output goal prediction
            val output = outputBuffer.floatArray
            goal = interpretOutput(output)
        } catch (_: Exception) {
            return Goal.MAINTAIN_WEIGHT
        } finally {
//            interpreter.close()
        }
        return goal
    }

}