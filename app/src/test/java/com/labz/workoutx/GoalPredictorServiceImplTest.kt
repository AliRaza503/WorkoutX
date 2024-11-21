package com.labz.workoutx

import android.app.Application
import com.labz.workoutx.models.Gender
import com.labz.workoutx.models.Goal
import com.labz.workoutx.models.User
import com.labz.workoutx.services.goalPredictor.GoalPredictorServiceImpl
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.util.Calendar
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

//class GoalPredictorServiceImplTest {
//
//    private lateinit var goalPredictorService: GoalPredictorServiceImpl
//    private lateinit var mockInterpreter: Interpreter
//    private lateinit var mockApplication: Application
//
//    @Before
//    fun setUp() {
//        goalPredictorService = GoalPredictorServiceImpl()
//        mockInterpreter = mock(Interpreter::class.java)
//        mockApplication = mock(Application::class.java)
//
//        // Use reflection to set the private interpreter variable
//        val interpreterProperty =
//            goalPredictorService::class.declaredMemberProperties.find { it.name == "interpreter" }
//        interpreterProperty?.javaField?.isAccessible = true
//        interpreterProperty?.javaField?.set(goalPredictorService, mockInterpreter)
//    }
//
//    @Test
//    fun testCalculateBMI() {
//        val method =
//            goalPredictorService::class.declaredFunctions.find { it.name == "calculateBMI" }
//        method?.isAccessible = true
//        val bmi = method?.call(goalPredictorService, 70f, 175f) as Float
//        assertEquals(22.86f, bmi, 0.01f)
//    }
//
//    @Test
//    fun testCalculateAge() {
//        val method =
//            goalPredictorService::class.declaredFunctions.find { it.name == "calculateAge" }
//        method?.isAccessible = true
//        val dob = Calendar.getInstance().apply { set(1990, 0, 1) }
//        val age = method?.call(goalPredictorService, dob) as Float
//        assertEquals(34f, age, 0.9f) // Adjust the expected age based on the current year
//    }
//
//    @Test
//    fun testCalculateGender() {
//        val method =
//            goalPredictorService::class.declaredFunctions.find { it.name == "calculateGender" }
//        method?.isAccessible = true
//        val genderValue = method?.call(goalPredictorService, Gender.MALE) as Float
//        assertEquals(1f, genderValue, 0.0f)
//    }
//
//    @Test
//    fun testCalculateGenderOther() {
//        val method =
//            goalPredictorService::class.declaredFunctions.find { it.name == "calculateGenderOther" }
//        method?.isAccessible = true
//        val genderValue = method?.call(goalPredictorService, Gender.PREFER_NOT_TO_SAY) as Float
//        assertEquals(1f, genderValue, 0.0f)
//    }
//
//    @Test
//    fun testPredictGoalMaintainWeight() {
//        val byteBuffer = ByteBuffer.allocateDirect(32)
//        val byteBufferProperty =
//            goalPredictorService::class.declaredMemberProperties.find { it.name == "byteBuffer" }
//        byteBufferProperty?.isAccessible = true
//        byteBufferProperty?.javaField?.isAccessible = true
//        byteBufferProperty?.javaField?.set(goalPredictorService, byteBuffer)
//
//        val output = floatArrayOf(0.1f, 0.2f, 0.3f, 0.4f)
//        doAnswer { invocation ->
//            val outputBuffer = invocation.arguments[1] as ByteBuffer
//            outputBuffer.putFloat(output[0])
//            outputBuffer.putFloat(output[1])
//            outputBuffer.putFloat(output[2])
//            outputBuffer.putFloat(output[3])
//            null
//        }.`when`(mockInterpreter).run(any(ByteBuffer::class.java), any(ByteBuffer::class.java))
//
//        val goal = goalPredictorService.predictGoal()
//        assertEquals(Goal.MAINTAIN_WEIGHT, goal)
//    }
//
//    @Test
//    fun testPredictGoalBuildMuscle() {
//        val byteBuffer = ByteBuffer.allocateDirect(32)
//        val byteBufferProperty =
//            goalPredictorService::class.declaredMemberProperties.find { it.name == "byteBuffer" }
//        byteBufferProperty?.isAccessible = true
//        byteBufferProperty?.javaField?.isAccessible = true
//        byteBufferProperty?.javaField?.set(goalPredictorService, byteBuffer)
//
//        val output = floatArrayOf(0.9f, 0.9f, 0.9f, 0.9f)
//        doAnswer { invocation ->
//            val outputBuffer = invocation.arguments[1] as ByteBuffer
//            outputBuffer.putFloat(output[0])
//            outputBuffer.putFloat(output[1])
//            outputBuffer.putFloat(output[2])
//            outputBuffer.putFloat(output[3])
//            null
//        }.`when`(mockInterpreter).run(any(ByteBuffer::class.java), any(ByteBuffer::class.java))
//
//        val goal = goalPredictorService.predictGoal()
//        print(goal)
//        assertEquals(Goal.BUILD_MUSCLE, goal)
//    }
//
//    @Test
//    fun testPredictGoalGainWeight() {
//        val byteBuffer = ByteBuffer.allocateDirect(32)
//        val byteBufferProperty =
//            goalPredictorService::class.declaredMemberProperties.find { it.name == "byteBuffer" }
//        byteBufferProperty?.isAccessible = true
//        byteBufferProperty?.javaField?.isAccessible = true
//        byteBufferProperty?.javaField?.set(goalPredictorService, byteBuffer)
//
//        val output = floatArrayOf(0.1f, 0.9f, 0.1f, 0.1f)
//        doAnswer { invocation ->
//            val outputBuffer = invocation.arguments[1] as ByteBuffer
//            outputBuffer.putFloat(output[0])
//            outputBuffer.putFloat(output[1])
//            outputBuffer.putFloat(output[2])
//            outputBuffer.putFloat(output[3])
//            null
//        }.`when`(mockInterpreter).run(any(ByteBuffer::class.java), any(ByteBuffer::class.java))
//
//        val goal = goalPredictorService.predictGoal()
//        println(goal)
//        assertEquals(Goal.GAIN_WEIGHT, goal)
//    }
//
//    @Test
//    fun testPredictGoalLoseWeight() {
//        val byteBuffer = ByteBuffer.allocateDirect(32)
//        val byteBufferProperty =
//            goalPredictorService::class.declaredMemberProperties.find { it.name == "byteBuffer" }
//        byteBufferProperty?.isAccessible = true
//        byteBufferProperty?.javaField?.isAccessible = true
//        byteBufferProperty?.javaField?.set(goalPredictorService, byteBuffer)
//
//        val output = floatArrayOf(0.3f, 0.2f, 0.4f, 0.1f)
//        doAnswer { invocation ->
//            val outputBuffer = invocation.arguments[1] as ByteBuffer
//            outputBuffer.putFloat(output[0])
//            outputBuffer.putFloat(output[1])
//            outputBuffer.putFloat(output[2])
//            outputBuffer.putFloat(output[3])
//            null
//        }.`when`(mockInterpreter).run(any(ByteBuffer::class.java), any(ByteBuffer::class.java))
//
//        val goal = goalPredictorService.predictGoal()
//        println(goal)
//        assertEquals(Goal.LOSE_WEIGHT, goal)
//    }
//
//    @Test
//    fun testPreProcessDataAnd_BUILD_MUSCLE() {
//        `when`(mockApplication.applicationContext).thenReturn(mockApplication)
//        // Use reflection to set private properties of User
//        val dateOfBirthProperty =
//            User::class.declaredMemberProperties.find { it.name == "dateOfBirth" }
//        dateOfBirthProperty?.isAccessible = true
//        dateOfBirthProperty?.javaField?.set(User, Calendar.getInstance().apply { set(1990, 0, 1) })
//
//        val genderProperty = User::class.declaredMemberProperties.find { it.name == "gender" }
//        genderProperty?.isAccessible = true
//        genderProperty?.javaField?.set(User, Gender.MALE)
//
//        val weightInKgsProperty =
//            User::class.declaredMemberProperties.find { it.name == "weightInKgs" }
//        weightInKgsProperty?.isAccessible = true
//        weightInKgsProperty?.javaField?.set(User, 70.0)
//
//        val heightInCmsProperty =
//            User::class.declaredMemberProperties.find { it.name == "heightInCms" }
//        heightInCmsProperty?.isAccessible = true
//        heightInCmsProperty?.javaField?.set(User, 175.0)
//
//        goalPredictorService.preProcessData(mockApplication, 500.0, 60.0)
//
//        val byteBufferProperty =
//            goalPredictorService::class.declaredMemberProperties.find { it.name == "byteBuffer" }
//        byteBufferProperty?.isAccessible = true
//        val byteBuffer = byteBufferProperty?.getter?.call(goalPredictorService) as ByteBuffer
//        assertEquals(32, byteBuffer.capacity())
//
//        val goal = goalPredictorService.predictGoal()
//        println(goal)
//        assertEquals(Goal.BUILD_MUSCLE, goal)
//    }
//
//    @Test
//    fun testPreProcessDataAnd_LOSE_WEIGHT() {
//        `when`(mockApplication.applicationContext).thenReturn(mockApplication)
//        // Use reflection to set private properties of User
//        val dateOfBirthProperty =
//            User::class.declaredMemberProperties.find { it.name == "dateOfBirth" }
//        dateOfBirthProperty?.isAccessible = true
//        dateOfBirthProperty?.javaField?.set(User, Calendar.getInstance().apply { set(2010, 0, 1) })
//
//        val genderProperty = User::class.declaredMemberProperties.find { it.name == "gender" }
//        genderProperty?.isAccessible = true
//        genderProperty?.javaField?.set(User, Gender.MALE)
//
//        val weightInKgsProperty =
//            User::class.declaredMemberProperties.find { it.name == "weightInKgs" }
//        weightInKgsProperty?.isAccessible = true
//        weightInKgsProperty?.javaField?.set(User, 70.0)
//
//        val heightInCmsProperty =
//            User::class.declaredMemberProperties.find { it.name == "heightInCms" }
//        heightInCmsProperty?.isAccessible = true
//        heightInCmsProperty?.javaField?.set(User, 175.0)
//
//        goalPredictorService.preProcessData(mockApplication, 500.0, 60.0)
//
//        val byteBufferProperty =
//            goalPredictorService::class.declaredMemberProperties.find { it.name == "byteBuffer" }
//        byteBufferProperty?.isAccessible = true
//        val byteBuffer = byteBufferProperty?.getter?.call(goalPredictorService) as ByteBuffer
//        assertEquals(32, byteBuffer.capacity())
//
//        val goal = goalPredictorService.predictGoal()
//        println(goal)
//        assertEquals(Goal.LOSE_WEIGHT, goal)
//    }
//
//    @Test
//    fun testPreProcessDataAnd_GAIN_WEIGHT() {
//        `when`(mockApplication.applicationContext).thenReturn(mockApplication)
//        // Use reflection to set private properties of User
//        val dateOfBirthProperty =
//            User::class.declaredMemberProperties.find { it.name == "dateOfBirth" }
//        dateOfBirthProperty?.isAccessible = true
//        dateOfBirthProperty?.javaField?.set(User, Calendar.getInstance().apply { set(2000, 0, 1) })
//
//        val genderProperty = User::class.declaredMemberProperties.find { it.name == "gender" }
//        genderProperty?.isAccessible = true
//        genderProperty?.javaField?.set(User, Gender.MALE)
//
//        val weightInKgsProperty =
//            User::class.declaredMemberProperties.find { it.name == "weightInKgs" }
//        weightInKgsProperty?.isAccessible = true
//        weightInKgsProperty?.javaField?.set(User, 70.0)
//
//        val heightInCmsProperty =
//            User::class.declaredMemberProperties.find { it.name == "heightInCms" }
//        heightInCmsProperty?.isAccessible = true
//        heightInCmsProperty?.javaField?.set(User, 175.0)
//
//        goalPredictorService.preProcessData(mockApplication, 500.0, 60.0)
//
//        val byteBufferProperty =
//            goalPredictorService::class.declaredMemberProperties.find { it.name == "byteBuffer" }
//        byteBufferProperty?.isAccessible = true
//        val byteBuffer = byteBufferProperty?.getter?.call(goalPredictorService) as ByteBuffer
//        assertEquals(32, byteBuffer.capacity())
//
//
//        val goal = goalPredictorService.predictGoal()
//        println(goal)
//        assertEquals(Goal.BUILD_MUSCLE, goal)
//    }
//
//}