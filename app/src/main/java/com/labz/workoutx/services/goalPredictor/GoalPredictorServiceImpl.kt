package com.labz.workoutx.services.goalPredictor

import android.app.Application
import android.content.Context
import android.util.Log
import com.labz.workoutx.models.Gender
import com.labz.workoutx.models.Goal
import com.labz.workoutx.models.User
import com.labz.workoutx.utils.Consts
import org.json.JSONArray
import org.json.JSONObject
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.util.Calendar

class GoalPredictorServiceImpl : GoalPredictorService {
    private lateinit var interpreter: Interpreter
    private lateinit var inputData: FloatArray

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

    private fun standardizeInput(data: FloatArray, meanArray: FloatArray, scaleArray: FloatArray): FloatArray {
        return FloatArray(data.size) { (data[it] - meanArray[it]) / scaleArray[it] }
    }

    private fun loadScalerParams(context: Context): Pair<FloatArray, FloatArray> {
        val json = context.assets.open("scaler_params.json").bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(json)
        val mean = jsonObject.getJSONArray("mean")
        val scale = jsonObject.getJSONArray("scale")
        val meanArray = FloatArray(mean.length()) { mean.getDouble(it).toFloat() }
        val scaleArray = FloatArray(scale.length()) { scale.getDouble(it).toFloat() }
        return Pair(meanArray, scaleArray)
    }

    override fun preProcessData(
        application: Application,
        avgCalories: Double,
        avgWorkoutMinutes: Double
    ) {
        val (meanArray, scaleArray) = loadScalerParams(application)
        // Load the TFLite model from assets
        if (!this::interpreter.isInitialized) {
            interpreter = Interpreter(
                FileUtil.loadMappedFile(application, Consts.getModelPath())
            )
        }
        //    'Age', 'Weight', 'Height', 'BMI', 'Calories Burned', 'Workout Time (min)', 'Gender_Male', 'Gender_Other'

        try {
            val floatArray = floatArrayOf(
                calculateAge(User.dateOfBirth!!),
                User.weightInKgs.toFloat(),
                User.heightInCms.toFloat(),
                calculateBMI(User.weightInKgs.toFloat(), User.heightInCms.toFloat()),
                avgCalories.toFloat()/100f,
                avgWorkoutMinutes.toFloat(),
                calculateGender(User.gender!!),
                calculateGenderOther(User.gender!!)
            )
            inputData = standardizeInput(floatArray, meanArray, scaleArray)
        } catch (e: KotlinNullPointerException) {
            Log.e("GoalPredictorService", "User data not loaded", e)
        }
    }

    private fun loadLabels(context: Context): List<String> {
        val json = context.assets.open("labels.json").bufferedReader().use { it.readText() }
        return JSONArray(json).let { 0.until(it.length()).map { i -> it.getString(i) } }
    }

    private fun getPredictedLabel(output: FloatArray, labels: List<String>): String {
        val maxIndex = output.indices.maxByOrNull { output[it] } ?: -1
        return labels[maxIndex]
    }

    private fun predictionHelper(model: Interpreter, inputData: FloatArray): FloatArray {
        val output = Array(1) { FloatArray(4) }
        model.run(arrayOf(inputData), output)
        return output[0]
    }

    override fun predictGoal(application: Application): Goal {
        var goal: Goal
        try {
            val output = predictionHelper(interpreter, inputData)
            val labels = loadLabels(application)
            val predictedLabel = getPredictedLabel(output, labels)
            goal = Goal.valueOf(goal = predictedLabel)!!
        } catch (e: Exception) {
            Log.e("GoalPredictorService", "Error predicting goal", e)
            return Goal.MAINTAIN_WEIGHT
        } finally {
//            interpreter.close()
        }
        return goal
    }
}
