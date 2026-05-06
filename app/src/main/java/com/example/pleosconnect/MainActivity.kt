package com.example.pleosconnect

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import ai.pleos.playground.vehicle.Vehicle
import ai.pleos.playground.vehicle.listener.DistanceDrivenListener
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sign
import kotlin.math.sin
import kotlin.random.Random
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

private class G2RoundedCornerShape(
    private val topStart: Dp,
    private val topEnd: Dp = topStart,
    private val bottomEnd: Dp = topStart,
    private val bottomStart: Dp = topStart
) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val radiusScale = 1.18f
        val maxRadius = size.minDimension / 2f
        val startTop = (topStart.toPx(density) * radiusScale).coerceAtMost(maxRadius)
        val endTop = (topEnd.toPx(density) * radiusScale).coerceAtMost(maxRadius)
        val endBottom = (bottomEnd.toPx(density) * radiusScale).coerceAtMost(maxRadius)
        val startBottom = (bottomStart.toPx(density) * radiusScale).coerceAtMost(maxRadius)

        val path = Path().apply {
            moveTo(startTop, 0f)
            lineTo(size.width - endTop, 0f)
            continuousCorner(size.width - endTop, endTop, endTop, -90.0, 0.0)
            lineTo(size.width, size.height - endBottom)
            continuousCorner(size.width - endBottom, size.height - endBottom, endBottom, 0.0, 90.0)
            lineTo(startBottom, size.height)
            continuousCorner(startBottom, size.height - startBottom, startBottom, 90.0, 180.0)
            lineTo(0f, startTop)
            continuousCorner(startTop, startTop, startTop, 180.0, 270.0)
            close()
        }

        return Outline.Generic(path)
    }

    private fun Path.continuousCorner(cx: Float, cy: Float, radius: Float, startDegrees: Double, endDegrees: Double) {
        if (radius <= 0f) return

        val steps = 14
        val exponent = 3.35
        for (i in 1..steps) {
            val progress = i.toDouble() / steps.toDouble()
            val degrees = startDegrees + (endDegrees - startDegrees) * progress
            val radians = degrees * PI / 180.0
            val c = cos(radians)
            val s = sin(radians)
            val x = cx + radius * sign(c).toFloat() * abs(c).powForSuperellipse(exponent).toFloat()
            val y = cy + radius * sign(s).toFloat() * abs(s).powForSuperellipse(exponent).toFloat()
            lineTo(x, y)
        }
    }

    private fun Double.powForSuperellipse(exponent: Double): Double =
        this.pow(2.0 / exponent)

    private fun Dp.toPx(density: Density): Float = with(density) { toPx() }
}

class MainActivity : ComponentActivity() {
    private lateinit var vehicle: Vehicle

    private var screen by mutableStateOf(AppScreen.Home)
    private var historyReturnScreen by mutableStateOf(AppScreen.Home)
    private var receiptReturnScreen by mutableStateOf(AppScreen.Home)
    private var passengerName by mutableStateOf("김카풀")
    private var selectedRecord by mutableStateOf<RideRecord?>(null)
    private var selectedPassengerName by mutableStateOf<String?>(null)
    private var selectedHistoryMonth by mutableStateOf(YearMonth.now())
    private var pendingRide by mutableStateOf<PendingRide?>(null)
    private var tollText by mutableStateOf("")
    private var extraText by mutableStateOf("")
    private val rideRecords = mutableStateListOf<RideRecord>()
    private val passengerWidgets = mutableStateListOf<String>()
    private var historyWidgetSelectionMode by mutableStateOf(false)
    private var selectedWidgetPassengerName by mutableStateOf<String?>(null)
    private var globalNoticeText by mutableStateOf<String?>(null)
    private var selectedRegion by mutableStateOf(FareRegion.SEOUL)
    private var selectedSurcharge by mutableStateOf(SurchargeMode.Normal)
    private var selectedTheme by mutableStateOf(MeshTheme.Pearl)
    private var useCustomFare by mutableStateOf(false)
    private var customBaseFare by mutableIntStateOf(4800)
    private var customStepFare by mutableIntStateOf(100)
    private var homeInitialPage by mutableIntStateOf(0)

    private var currentFare by mutableIntStateOf(0)
    private var isRunning by mutableStateOf(false)
    private var startDistanceKm by mutableStateOf<Float?>(null)
    private var currentDistanceKm by mutableStateOf<Float?>(null)
    private var testDistanceKm by mutableFloatStateOf(500f)
    private var farePolicy by mutableStateOf(FarePolicy.daytime())
    private var startedAtMillis by mutableLongStateOf(0L)
    private var elapsedSeconds by mutableLongStateOf(0L)

    private val distanceDrivenListener = object : DistanceDrivenListener {
        override fun onDistanceDrivenUpdated(value: Float?) {
            runOnUiThread {
                applyDistanceUpdate(value)
            }
        }

        override fun onFailed(e: Exception) {
            Log.e("TaxiMeter", "Odometer error", e)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rideRecords.addAll(loadRideRecords())
        passengerWidgets.addAll(loadPassengerWidgets())
        selectedRegion = loadSelectedRegion()
        selectedTheme = loadSelectedTheme()
        useCustomFare = loadUseCustomFare()
        customBaseFare = loadCustomBaseFare()
        customStepFare = loadCustomStepFare()
        farePolicy = activeFarePolicy()

        vehicle = Vehicle(this)
        vehicle.initialize()
        vehicle.getOdometer().registerDistanceDriven(distanceDrivenListener)

        setContent {
            LaunchedEffect(isRunning, startedAtMillis) {
                while (isRunning) {
                    elapsedSeconds = ((System.currentTimeMillis() - startedAtMillis) / 1000L).coerceAtLeast(0L)
                    kotlinx.coroutines.delay(1000L)
                }
            }

            CarpoolMeterApp(
                screen = screen,
                passengerName = passengerName,
                fare = currentFare,
                isRunning = isRunning,
                currentDistanceKm = currentDistanceKm,
                tripDistanceKm = tripDistanceKm(),
                elapsedSeconds = elapsedSeconds,
                farePolicy = farePolicy,
                pendingRide = pendingRide,
                tollText = tollText,
                extraText = extraText,
                records = rideRecords,
                passengerWidgets = passengerWidgets,
                selectedRecord = selectedRecord,
                selectedRegion = selectedRegion,
                selectedSurcharge = selectedSurcharge,
                selectedTheme = selectedTheme,
                useCustomFare = useCustomFare,
                customBaseFare = customBaseFare,
                customStepFare = customStepFare,
                historyReturnScreen = historyReturnScreen,
                historyWidgetSelectionMode = historyWidgetSelectionMode,
                selectedWidgetPassengerName = selectedWidgetPassengerName,
                globalNoticeText = globalNoticeText,
                selectedPassengerName = selectedPassengerName,
                selectedHistoryMonth = selectedHistoryMonth,
                homeInitialPage = homeInitialPage,
                onPassengerNameChange = { passengerName = it },
                onStart = { startRide() },
                onStop = { stopRide() },
                onTestDrive = { testDrive131m() },
                onGoHome = {
                    homeInitialPage = 0
                    screen = AppScreen.Home
                },
                onGoHistory = {
                    historyWidgetSelectionMode = false
                    selectedWidgetPassengerName = null
                    historyReturnScreen = screen
                    screen = AppScreen.History
                },
                onGoHistoryBack = {
                    historyWidgetSelectionMode = false
                    selectedWidgetPassengerName = null
                    screen = if (historyReturnScreen == AppScreen.Receipt && selectedRecord != null) {
                        AppScreen.Receipt
                    } else {
                        AppScreen.Home
                    }
                },
                onGoFareSettings = { screen = AppScreen.FareModeSelect },
                onGoRegionFareSettings = { screen = AppScreen.FareSettings },
                onGoCustomFareSettings = { screen = AppScreen.CustomFareSettings },
                onGoThemeSettings = { screen = AppScreen.ThemeSettings },
                onSelectRegion = {
                    useCustomFare = false
                    selectedRegion = it
                    farePolicy = activeFarePolicy()
                    saveSelectedRegion(it)
                    saveUseCustomFare(false)
                    screen = AppScreen.Home
                },
                onSelectCustomFare = { baseFare, stepFare ->
                    useCustomFare = true
                    customBaseFare = baseFare
                    customStepFare = stepFare
                    farePolicy = activeFarePolicy()
                    saveUseCustomFare(true)
                    saveCustomFare(baseFare, stepFare)
                },
                onSelectSurcharge = {
                    if (!isRunning) {
                        selectedSurcharge = it
                        farePolicy = activeFarePolicy()
                    }
                },
                onSelectTheme = {
                    selectedTheme = it
                    saveSelectedTheme(it)
                    screen = AppScreen.Home
                },
                onTollChange = { tollText = it.filter(Char::isDigit) },
                onExtraChange = { extraText = it.filter(Char::isDigit) },
                onSaveRide = { savePendingRide() },
                onSelectRecord = {
                    receiptReturnScreen = screen
                    selectedRecord = it
                    screen = AppScreen.Receipt
                },
                onShredRecord = { shredRideRecord(it) },
                onSettleRecord = { settleRideRecord(it) },
                onGoReceiptBack = { goReceiptBack() },
                onGoWidgetPicker = {
                    historyWidgetSelectionMode = true
                    selectedWidgetPassengerName = null
                    historyReturnScreen = AppScreen.Home
                    screen = AppScreen.History
                },
                onSelectWidgetPassenger = { selectedWidgetPassengerName = it },
                onAddWidgetPassenger = { addSelectedPassengerWidget() },
                onRemovePassengerWidget = { removePassengerWidget(it) },
                onDismissGlobalNotice = { globalNoticeText = null },
                onSelectPassenger = { name ->
                    selectedPassengerName = name
                    selectedHistoryMonth = rideRecords
                        .filter { it.passengerName.trim() == name }
                        .mapNotNull { it.recordDateOrNull() }
                        .maxOrNull()
                        ?.let { YearMonth.from(it) }
                        ?: YearMonth.now()
                    screen = AppScreen.PassengerHistory
                },
                onChangeHistoryMonth = { selectedHistoryMonth = it },
                onGoPassengerHistoryBack = { screen = AppScreen.History },
                onGoPassengerFullHistory = { screen = AppScreen.PassengerFullHistory },
                onGoPassengerMonthHistory = { screen = AppScreen.PassengerHistory },
                onOpenPassengerReceiptBoard = { name ->
                    selectedPassengerName = name
                    screen = AppScreen.PassengerReceiptBoard
                },
                onGoChallenges = {
                    screen = AppScreen.Challenges
                },
                onGoChallengeBadgeBoard = {
                    screen = AppScreen.ChallengeBadgeBoard
                },
                onGoHomePassengerPage = {
                    homeInitialPage = 1
                    screen = AppScreen.Home
                },
                onGoInfo = {
                    screen = AppScreen.AppInfo
                },
                onGoDonation = {
                    screen = AppScreen.Donation
                },
                onResetHistory = { resetRideHistory() },
                onGoSettingsPage = {
                    homeInitialPage = 2
                    screen = AppScreen.Home
                }
            )
        }
    }

    private fun startRide() {
        farePolicy = activeFarePolicy()
        currentFare = farePolicy.baseFare
        startDistanceKm = currentDistanceKm
        testDistanceKm = currentDistanceKm ?: 500f
        startedAtMillis = System.currentTimeMillis()
        elapsedSeconds = 0L
        pendingRide = null
        tollText = ""
        extraText = ""
        selectedRecord = null
        isRunning = true
        screen = AppScreen.Home
    }

    private fun stopRide() {
        if (!isRunning && currentFare == 0) return
        isRunning = false
        elapsedSeconds = ((System.currentTimeMillis() - startedAtMillis) / 1000L).coerceAtLeast(elapsedSeconds)

        pendingRide = PendingRide(
            passengerName = passengerName.ifBlank { "김카풀" },
            endedAt = LocalDateTime.now(),
            durationSeconds = elapsedSeconds,
            distanceKm = tripDistanceKm(),
            rideFare = currentFare
        )
        tollText = ""
        extraText = ""
        screen = AppScreen.EndRide
    }

    private fun testDrive131m() {
        if (!isRunning) return

        if (startDistanceKm == null) {
            applyDistanceUpdate(testDistanceKm)
        }

        testDistanceKm += 0.131f
        applyDistanceUpdate(testDistanceKm)
    }

    private fun applyDistanceUpdate(distance: Float?) {
        if (distance == null) return

        currentDistanceKm = distance

        if (!isRunning) return

        if (startDistanceKm == null) {
            startDistanceKm = distance
            return
        }

        val drivenMeters = tripDistanceKm() * 1000f
        val billableMeters = max(0f, drivenMeters - (farePolicy.baseDistanceKm * 1000f))
        val chargeCount = floor((billableMeters + 0.001f) / farePolicy.stepDistanceMeters).toInt()
        currentFare = farePolicy.baseFare + (chargeCount * farePolicy.stepFare)
    }

    private fun tripDistanceKm(): Float {
        val start = startDistanceKm ?: return 0f
        val current = currentDistanceKm ?: return 0f
        return max(0f, current - start)
    }

    private fun activeFarePolicy(): FarePolicy =
        if (useCustomFare) {
            FarePolicy.custom(
                baseFare = customBaseFare,
                stepFare = customStepFare,
                surchargeMode = selectedSurcharge
            )
        } else {
            FarePolicy.from(selectedRegion, selectedSurcharge)
        }

    private fun savePendingRide() {
        val pending = pendingRide ?: return
        val tollFare = tollText.toIntOrNull() ?: 0
        val extraFare = extraText.toIntOrNull() ?: 0
        val record = RideRecord(
            id = System.currentTimeMillis().toString(),
            passengerName = pending.passengerName,
            date = pending.endedAt.format(DateTimeFormatter.ofPattern("yy/MM/dd")),
            endTime = pending.endedAt.format(DateTimeFormatter.ofPattern("HH:mm")),
            durationSeconds = pending.durationSeconds,
            distanceKm = pending.distanceKm,
            rideFare = pending.rideFare,
            tollFare = tollFare,
            extraFare = extraFare,
            totalFare = pending.rideFare + tollFare + extraFare,
            themeCode = selectedTheme.code,
            isSettled = false
        )

        rideRecords.add(0, record)
        saveRideRecords(rideRecords)
        receiptReturnScreen = AppScreen.Home
        selectedRecord = record
        currentFare = 0
        isRunning = false
        startDistanceKm = null
        testDistanceKm = currentDistanceKm ?: 500f
        startedAtMillis = 0L
        elapsedSeconds = 0L
        pendingRide = null
        tollText = ""
        extraText = ""
        screen = AppScreen.Receipt
    }

    private fun goReceiptBack() {
        screen = when (receiptReturnScreen) {
            AppScreen.Receipt -> AppScreen.Home
            else -> receiptReturnScreen
        }
    }

    private fun shredRideRecord(record: RideRecord) {
        rideRecords.removeAll { it.id == record.id }
        saveRideRecords(rideRecords)
    }

    private fun settleRideRecord(record: RideRecord) {
        val index = rideRecords.indexOfFirst { it.id == record.id }
        if (index == -1) return

        val settledRecord = rideRecords[index].copy(isSettled = true)
        rideRecords[index] = settledRecord
        if (selectedRecord?.id == settledRecord.id) {
            selectedRecord = settledRecord
        }
        saveRideRecords(rideRecords)
    }

    private fun resetRideHistory() {
        rideRecords.clear()
        saveRideRecords(rideRecords)
        selectedRecord = null
        selectedPassengerName = null
        selectedWidgetPassengerName = null
        historyWidgetSelectionMode = false
    }

    private fun addSelectedPassengerWidget() {
        val name = selectedWidgetPassengerName?.trim().orEmpty()
        if (name.isEmpty()) return

        if (passengerWidgets.contains(name)) {
            historyWidgetSelectionMode = false
            selectedWidgetPassengerName = null
            screen = AppScreen.Home
            return
        }

        if (passengerWidgets.size >= 4) {
            globalNoticeText = "공간이 부족합니다, 기존 위젯을 지우고 다시 시도해 주세요"
            return
        }

        passengerWidgets.add(name)
        savePassengerWidgets(passengerWidgets)
        historyWidgetSelectionMode = false
        selectedWidgetPassengerName = null
        screen = AppScreen.Home
    }

    private fun removePassengerWidget(name: String) {
        passengerWidgets.remove(name)
        savePassengerWidgets(passengerWidgets)
    }

    private fun loadRideRecords(): List<RideRecord> {
        val json = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(KEY_RECORDS, "[]") ?: "[]"
        return runCatching {
            val array = JSONArray(json)
            List(array.length()) { index ->
                val item = array.getJSONObject(index)
                RideRecord(
                    id = item.getString("id"),
                    passengerName = item.getString("passengerName"),
                    date = item.getString("date"),
                    endTime = item.getString("endTime"),
                    durationSeconds = item.getLong("durationSeconds"),
                    distanceKm = item.getDouble("distanceKm").toFloat(),
                    rideFare = item.getInt("rideFare"),
                    tollFare = item.getInt("tollFare"),
                    extraFare = item.getInt("extraFare"),
                    totalFare = item.getInt("totalFare"),
                    themeCode = item.optString("themeCode", MeshTheme.Pearl.code),
                    isSettled = item.optBoolean("isSettled", false)
                )
            }
        }.getOrDefault(emptyList())
    }

    private fun saveRideRecords(records: List<RideRecord>) {
        val array = JSONArray()
        records.forEach { record ->
            array.put(
                JSONObject()
                    .put("id", record.id)
                    .put("passengerName", record.passengerName)
                    .put("date", record.date)
                    .put("endTime", record.endTime)
                    .put("durationSeconds", record.durationSeconds)
                    .put("distanceKm", record.distanceKm)
                    .put("rideFare", record.rideFare)
                    .put("tollFare", record.tollFare)
                    .put("extraFare", record.extraFare)
                    .put("totalFare", record.totalFare)
                    .put("themeCode", record.themeCode)
                    .put("isSettled", record.isSettled)
            )
        }
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_RECORDS, array.toString())
            .apply()
    }

    private fun loadPassengerWidgets(): List<String> {
        val json = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(KEY_PASSENGER_WIDGETS, "[]") ?: "[]"
        return runCatching {
            val array = JSONArray(json)
            List(array.length()) { index -> array.getString(index) }
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .distinct()
                .take(4)
        }.getOrDefault(emptyList())
    }

    private fun savePassengerWidgets(names: List<String>) {
        val array = JSONArray()
        names.map { it.trim() }
            .filter { it.isNotEmpty() }
            .distinct()
            .take(4)
            .forEach { array.put(it) }
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_PASSENGER_WIDGETS, array.toString())
            .apply()
    }

    private fun loadSelectedRegion(): FareRegion {
        val code = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(KEY_REGION, FareRegion.SEOUL.code)
        return FareRegion.entries.firstOrNull { it.code == code } ?: FareRegion.SEOUL
    }

    private fun saveSelectedRegion(region: FareRegion) {
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_REGION, region.code)
            .apply()
    }

    private fun loadUseCustomFare(): Boolean =
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getBoolean(KEY_USE_CUSTOM_FARE, false)

    private fun saveUseCustomFare(value: Boolean) {
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_USE_CUSTOM_FARE, value)
            .apply()
    }

    private fun loadCustomBaseFare(): Int =
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getInt(KEY_CUSTOM_BASE_FARE, 4800)

    private fun loadCustomStepFare(): Int =
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getInt(KEY_CUSTOM_STEP_FARE, 100)

    private fun saveCustomFare(baseFare: Int, stepFare: Int) {
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_CUSTOM_BASE_FARE, baseFare)
            .putInt(KEY_CUSTOM_STEP_FARE, stepFare)
            .apply()
    }

    private fun loadSelectedTheme(): MeshTheme {
        val code = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(KEY_THEME, MeshTheme.Pearl.code)
        return MeshTheme.entries.firstOrNull { it.code == code } ?: MeshTheme.Pearl
    }

    private fun saveSelectedTheme(theme: MeshTheme) {
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_THEME, theme.code)
            .apply()
    }

    override fun onDestroy() {
        vehicle.getOdometer().unregisterDistanceDriven(distanceDrivenListener)
        vehicle.release()
        super.onDestroy()
    }

    companion object {
        private const val PREFS_NAME = "carpool_meter"
        private const val KEY_RECORDS = "ride_records_json"
        private const val KEY_REGION = "selected_fare_region"
        private const val KEY_THEME = "selected_mesh_theme"
        private const val KEY_PASSENGER_WIDGETS = "passenger_widgets_json"
        private const val KEY_USE_CUSTOM_FARE = "use_custom_fare"
        private const val KEY_CUSTOM_BASE_FARE = "custom_base_fare"
        private const val KEY_CUSTOM_STEP_FARE = "custom_step_fare"
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CarpoolMeterApp(
    screen: AppScreen,
    passengerName: String,
    fare: Int,
    isRunning: Boolean,
    currentDistanceKm: Float?,
    tripDistanceKm: Float,
    elapsedSeconds: Long,
    farePolicy: FarePolicy,
    pendingRide: PendingRide?,
    tollText: String,
    extraText: String,
    records: List<RideRecord>,
    passengerWidgets: List<String>,
    selectedRecord: RideRecord?,
    selectedRegion: FareRegion,
    selectedSurcharge: SurchargeMode,
    selectedTheme: MeshTheme,
    useCustomFare: Boolean,
    customBaseFare: Int,
    customStepFare: Int,
    historyReturnScreen: AppScreen,
    historyWidgetSelectionMode: Boolean,
    selectedWidgetPassengerName: String?,
    globalNoticeText: String?,
    selectedPassengerName: String?,
    selectedHistoryMonth: YearMonth,
    homeInitialPage: Int,
    onPassengerNameChange: (String) -> Unit,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onTestDrive: () -> Unit,
    onGoHome: () -> Unit,
    onGoHistory: () -> Unit,
    onGoHistoryBack: () -> Unit,
    onGoFareSettings: () -> Unit,
    onGoRegionFareSettings: () -> Unit,
    onGoCustomFareSettings: () -> Unit,
    onGoThemeSettings: () -> Unit,
    onSelectRegion: (FareRegion) -> Unit,
    onSelectCustomFare: (Int, Int) -> Unit,
    onSelectSurcharge: (SurchargeMode) -> Unit,
    onSelectTheme: (MeshTheme) -> Unit,
    onTollChange: (String) -> Unit,
    onExtraChange: (String) -> Unit,
    onSaveRide: () -> Unit,
    onSelectRecord: (RideRecord) -> Unit,
    onShredRecord: (RideRecord) -> Unit,
    onSettleRecord: (RideRecord) -> Unit,
    onGoReceiptBack: () -> Unit,
    onGoWidgetPicker: () -> Unit,
    onSelectWidgetPassenger: (String) -> Unit,
    onAddWidgetPassenger: () -> Unit,
    onRemovePassengerWidget: (String) -> Unit,
    onDismissGlobalNotice: () -> Unit,
    onSelectPassenger: (String) -> Unit,
    onChangeHistoryMonth: (YearMonth) -> Unit,
    onGoPassengerHistoryBack: () -> Unit,
    onGoPassengerFullHistory: () -> Unit,
    onGoPassengerMonthHistory: () -> Unit,
    onOpenPassengerReceiptBoard: (String) -> Unit,
    onGoChallenges: () -> Unit,
    onGoChallengeBadgeBoard: () -> Unit,
    onGoHomePassengerPage: () -> Unit,
    onGoInfo: () -> Unit,
    onGoDonation: () -> Unit,
    onResetHistory: () -> Unit,
    onGoSettingsPage: () -> Unit
) {
    LaunchedEffect(globalNoticeText) {
        if (globalNoticeText != null) {
            kotlinx.coroutines.delay(1900L)
            onDismissGlobalNotice()
        }
    }

    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        MaterialTheme(
            colorScheme = lightColorScheme(
                primary = PrimaryBlue,
                onPrimary = Color.White,
                primaryContainer = SoftBlue,
                onPrimaryContainer = PrimaryBlue,
                surface = Color.White,
                surfaceVariant = Panel,
                background = AppBg,
                onBackground = DarkText
            ),
            typography = AstaTypography
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = AppBg
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppBg)
                ) {
                    val appBackgroundTheme = if (screen == AppScreen.Receipt) {
                        selectedRecord?.receiptThemeOr(selectedTheme) ?: selectedTheme
                    } else {
                        selectedTheme
                    }
                    AppMeshBackground(
                        selectedTheme = appBackgroundTheme,
                        overlayAlpha = if (screen == AppScreen.Receipt) appBackgroundTheme.receiptOuterOverlayAlpha() else appBackgroundTheme.backgroundOverlayAlpha()
                    )
                    AnimatedContent(
                        targetState = screen,
                        transitionSpec = {
                            val forward = targetState.ordinal > initialState.ordinal
                            val animation = tween<IntOffset>(durationMillis = 430, easing = FastOutSlowInEasing)
                            val fadeAnimation = tween<Float>(durationMillis = 280, easing = FastOutSlowInEasing)

                            if (forward) {
                                slideInHorizontally(animationSpec = animation, initialOffsetX = { it }) +
                                    fadeIn(animationSpec = fadeAnimation) togetherWith
                                    slideOutHorizontally(animationSpec = animation, targetOffsetX = { -it / 3 }) +
                                    fadeOut(animationSpec = fadeAnimation)
                            } else {
                                slideInHorizontally(animationSpec = animation, initialOffsetX = { -it / 3 }) +
                                    fadeIn(animationSpec = fadeAnimation) togetherWith
                                    slideOutHorizontally(animationSpec = animation, targetOffsetX = { it }) +
                                    fadeOut(animationSpec = fadeAnimation)
                            }
                        },
                        label = "screenTransition"
                    ) { targetScreen ->
                        if (targetScreen == AppScreen.Home) {
                            HomeScreen(
                                passengerName = passengerName,
                                fare = fare,
                                isRunning = isRunning,
                                currentDistanceKm = currentDistanceKm,
                                tripDistanceKm = tripDistanceKm,
                                elapsedSeconds = elapsedSeconds,
                                farePolicy = farePolicy,
                                selectedRegion = selectedRegion,
                                selectedSurcharge = selectedSurcharge,
                                selectedTheme = selectedTheme,
                                initialPage = homeInitialPage,
                                records = records,
                                passengerWidgets = passengerWidgets,
                                onPassengerNameChange = onPassengerNameChange,
                                onStart = onStart,
                                onStop = onStop,
                                onTestDrive = onTestDrive,
                                onGoHistory = onGoHistory,
                                onGoWidgetPicker = onGoWidgetPicker,
                                onRemovePassengerWidget = onRemovePassengerWidget,
                                onGoFareSettings = onGoFareSettings,
                                onGoThemeSettings = onGoThemeSettings,
                                onSelectSurcharge = onSelectSurcharge,
                                onOpenPassengerReceiptBoard = onOpenPassengerReceiptBoard,
                                onGoChallenges = onGoChallenges,
                                onGoInfo = onGoInfo,
                                onGoDonation = onGoDonation
                            )
                        } else if (targetScreen == AppScreen.PassengerReceiptBoard) {
                            PassengerReceiptBoardScreen(
                                passengerName = selectedPassengerName,
                                records = records,
                                selectedTheme = selectedTheme,
                                onGoBack = onGoHomePassengerPage,
                                onShredRecord = onShredRecord,
                                onSettleRecord = onSettleRecord
                            )
                        } else if (targetScreen == AppScreen.FareModeSelect) {
                            FareModeSelectScreen(
                                onGoRegionalFare = onGoRegionFareSettings,
                                onGoCustomFare = onGoCustomFareSettings,
                                onGoBack = onGoHome
                            )
                        } else if (targetScreen == AppScreen.CustomFareSettings) {
                            CustomFareSettingsScreen(
                                baseFare = customBaseFare,
                                stepFare = customStepFare,
                                useCustomFare = useCustomFare,
                                onSelectCustomFare = onSelectCustomFare,
                                onGoBack = onGoFareSettings,
                                onGoHome = onGoHome
                            )
                        } else if (targetScreen == AppScreen.Donation) {
                            DonationScreen(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 18.dp, end = 18.dp, top = 44.dp, bottom = 18.dp),
                                onGoBack = onGoSettingsPage
                            )
                        } else {
                            BouncyScrollableColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 18.dp, end = 18.dp, top = 44.dp, bottom = 18.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                when (targetScreen) {
                                    AppScreen.EndRide -> EndRideScreen(
                                        passengerName = passengerName,
                                        pendingRide = pendingRide,
                                        tollText = tollText,
                                        extraText = extraText,
                                        selectedTheme = selectedTheme,
                                        onTollChange = onTollChange,
                                        onExtraChange = onExtraChange,
                                        onSaveRide = onSaveRide,
                                        onGoHome = onGoHome
                                    )

                                    AppScreen.History -> HistoryScreen(
                                        records = records,
                                        onSelectPassenger = onSelectPassenger,
                                        widgetSelectionMode = historyWidgetSelectionMode,
                                        selectedWidgetPassengerName = selectedWidgetPassengerName,
                                        onSelectWidgetPassenger = onSelectWidgetPassenger,
                                        onAddWidgetPassenger = onAddWidgetPassenger,
                                        returnButtonText = if (historyReturnScreen == AppScreen.Receipt) "뒤로가기" else "홈으로",
                                        onReturnClick = onGoHistoryBack
                                    )

                                    AppScreen.PassengerHistory -> PassengerHistoryScreen(
                                        passengerName = selectedPassengerName,
                                        records = records,
                                        selectedMonth = selectedHistoryMonth,
                                        onChangeMonth = onChangeHistoryMonth,
                                        onSelectRecord = onSelectRecord,
                                        onGoBack = onGoPassengerHistoryBack,
                                        onGoFullHistory = onGoPassengerFullHistory
                                    )

                                    AppScreen.PassengerFullHistory -> PassengerFullHistoryScreen(
                                        passengerName = selectedPassengerName,
                                        records = records,
                                        onSelectRecord = onSelectRecord,
                                        onGoBack = onGoPassengerMonthHistory
                                    )

                                    AppScreen.Receipt -> ReceiptScreen(
                                        record = selectedRecord,
                                        selectedTheme = selectedTheme,
                                        onGoHistory = onGoHistory,
                                        onGoBack = onGoReceiptBack,
                                        onGoHome = onGoHome,
                                        onShredRecord = onShredRecord,
                                        onSettleRecord = onSettleRecord
                                    )

                                    AppScreen.FareSettings -> FareSettingsScreen(
                                        selectedRegion = selectedRegion,
                                        onSelectRegion = onSelectRegion,
                                        onGoHome = onGoHome
                                    )

                                    AppScreen.ThemeSettings -> ThemeSettingsScreen(
                                        selectedTheme = selectedTheme,
                                        onSelectTheme = onSelectTheme,
                                        onGoHome = onGoHome
                                    )

                                    AppScreen.Challenges -> ChallengesScreen(
                                        records = records,
                                        onGoHome = onGoHomePassengerPage,
                                        onOpenBadgeBoard = onGoChallengeBadgeBoard
                                    )

                                    AppScreen.ChallengeBadgeBoard -> ChallengeBadgeBoardScreen(
                                        records = records,
                                        onGoBack = onGoChallenges
                                    )

                                    AppScreen.AppInfo -> AppInfoScreen(
                                        selectedTheme = selectedTheme,
                                        onGoBack = onGoSettingsPage,
                                        onResetHistory = onResetHistory
                                    )

                                    AppScreen.Donation -> DonationScreen(
                                        onGoBack = onGoSettingsPage
                                    )

                                    AppScreen.PassengerReceiptBoard -> Unit
                                    AppScreen.FareModeSelect -> Unit
                                    AppScreen.CustomFareSettings -> Unit
                                    AppScreen.Home -> Unit
                                }
                            }
                        }
                    }
                    TopNotice(
                        visible = globalNoticeText != null,
                        text = globalNoticeText.orEmpty()
                    )
                }
            }
        }
    }
}

@Composable
private fun BouncyScrollableColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val bounceOffset = remember { Animatable(0f) }

    val bounceConnection = remember(scrollState) {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val pullingDownAtTop = !scrollState.canScrollBackward && available.y > 0f
                val pullingUpAtBottom = !scrollState.canScrollForward && available.y < 0f

                if (pullingDownAtTop || pullingUpAtBottom) {
                    scope.launch {
                        val next = bounceOffset.value + available.y * 0.22f
                        bounceOffset.snapTo(next)
                    }
                }

                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                bounceOffset.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = 0.82f,
                        stiffness = Spring.StiffnessLow
                    )
                )
                return Velocity.Zero
            }
        }
    }

    Box(
        modifier = modifier
            .nestedScroll(bounceConnection)
            .clipToBounds()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { translationY = bounceOffset.value }
                .verticalScroll(scrollState),
            verticalArrangement = verticalArrangement,
            content = content
        )
    }
}

@Composable
private fun AppMeshBackground(selectedTheme: MeshTheme, overlayAlpha: Float = selectedTheme.backgroundOverlayAlpha()) {
    Image(
        painter = painterResource(id = selectedTheme.drawableRes),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = overlayAlpha))
    )
}

@Composable
private fun TopNotice(visible: Boolean, text: String) {
    AnimatedVisibility(
        visible = visible,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 34.dp, vertical = 24.dp),
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 420),
            initialOffsetY = { -it - 40 }
        ) + fadeIn(animationSpec = tween(durationMillis = 260)),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = 360),
            targetOffsetY = { -it - 40 }
        ) + fadeOut(animationSpec = tween(durationMillis = 240))
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = G2RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.96f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    GlassIconContainer(
                        symbol = "!",
                        size = 58.dp,
                        fontSize = 30.sp
                    )
                }
                Text(text = text, color = DarkText, fontSize = 30.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    passengerName: String,
    fare: Int,
    isRunning: Boolean,
    currentDistanceKm: Float?,
    tripDistanceKm: Float,
    elapsedSeconds: Long,
    farePolicy: FarePolicy,
    selectedRegion: FareRegion,
    selectedSurcharge: SurchargeMode,
    selectedTheme: MeshTheme,
    initialPage: Int,
    records: List<RideRecord>,
    passengerWidgets: List<String>,
    onPassengerNameChange: (String) -> Unit,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onTestDrive: () -> Unit,
    onGoHistory: () -> Unit,
    onGoWidgetPicker: () -> Unit,
    onRemovePassengerWidget: (String) -> Unit,
    onGoFareSettings: () -> Unit,
    onGoThemeSettings: () -> Unit,
    onSelectSurcharge: (SurchargeMode) -> Unit,
    onOpenPassengerReceiptBoard: (String) -> Unit,
    onGoChallenges: () -> Unit,
    onGoInfo: () -> Unit,
    onGoDonation: () -> Unit
) {
    var topNoticeVisible by remember { mutableStateOf(false) }
    var passengerDialogVisible by remember { mutableStateOf(false) }
    var editingPassengerName by remember(passengerName) { mutableStateOf(passengerName) }
    val passengerDialogBlur by animateDpAsState(
        targetValue = if (passengerDialogVisible) 18.dp else 0.dp,
        animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing),
        label = "passengerDialogBlur"
    )
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { 3 })

    LaunchedEffect(initialPage) {
        if (pagerState.currentPage != initialPage) {
            pagerState.scrollToPage(initialPage)
        }
    }

    LaunchedEffect(topNoticeVisible) {
        if (topNoticeVisible) {
            kotlinx.coroutines.delay(1800L)
            topNoticeVisible = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .blur(passengerDialogBlur)
                .padding(start = 18.dp, end = 18.dp, top = 16.dp, bottom = 18.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                pageSpacing = 14.dp
            ) { page ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = if (page == 0) 0.dp else 28.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    when (page) {
                        0 -> HomeDashboardPage(
                            passengerName = passengerName,
                            fare = fare,
                            isRunning = isRunning,
                            tripDistanceKm = tripDistanceKm,
                            elapsedSeconds = elapsedSeconds,
                            farePolicy = farePolicy,
                            selectedSurcharge = selectedSurcharge,
                            selectedTheme = selectedTheme,
                            currentPage = pagerState.currentPage,
                            onEditPassenger = {
                                editingPassengerName = passengerName
                                passengerDialogVisible = true
                            },
                            onStart = onStart,
                            onStop = onStop,
                            onGoFareSettings = {
                                if (isRunning) {
                                    topNoticeVisible = true
                                } else {
                                    onGoFareSettings()
                                }
                            },
                            onGoThemeSettings = onGoThemeSettings,
                            onGoHistory = onGoHistory,
                            onSelectSurcharge = onSelectSurcharge
                        )

                        1 -> PassengerWidgetPage(
                            records = records,
                            passengerWidgets = passengerWidgets,
                            currentPage = pagerState.currentPage,
                            onAddClick = onGoWidgetPicker,
                            onRemovePassengerWidget = onRemovePassengerWidget,
                            onPassengerClick = onOpenPassengerReceiptBoard,
                            onGoChallenges = onGoChallenges
                        )

                        else -> HomeQuickSettingsPage(
                            isRunning = isRunning,
                            farePolicy = farePolicy,
                            selectedRegion = selectedRegion,
                            selectedSurcharge = selectedSurcharge,
                            currentPage = pagerState.currentPage,
                            onGoFareSettings = onGoFareSettings,
                            onGoThemeSettings = onGoThemeSettings,
                            onGoHistory = onGoHistory,
                            onGoInfo = onGoInfo,
                            onGoDonation = onGoDonation
                        )
                    }
                }
            }
        }

        TopNotice(
            visible = topNoticeVisible,
            text = "주행 중에는 요금제를 변경할 수 없어요"
        )

        PassengerEditDialogOverlay(
            visible = passengerDialogVisible,
            editingPassengerName = editingPassengerName,
            onNameChange = { editingPassengerName = it },
            onDismiss = { passengerDialogVisible = false },
            onSave = {
                val cleanedName = editingPassengerName.trim()
                if (cleanedName.isNotEmpty()) {
                    onPassengerNameChange(cleanedName)
                }
                passengerDialogVisible = false
            }
        )
    }
}

@Composable
private fun HomeDashboardPage(
    passengerName: String,
    fare: Int,
    isRunning: Boolean,
    tripDistanceKm: Float,
    elapsedSeconds: Long,
    farePolicy: FarePolicy,
    selectedSurcharge: SurchargeMode,
    selectedTheme: MeshTheme,
    currentPage: Int,
    onEditPassenger: () -> Unit,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onGoFareSettings: () -> Unit,
    onGoThemeSettings: () -> Unit,
    onGoHistory: () -> Unit,
    onSelectSurcharge: (SurchargeMode) -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val expandedLayout = maxWidth >= 700.dp
        val topOffset = HomePagerTopPadding
        val bottomOffset = HomePagerBottomPadding
        val sectionSpacing = HomePagerSectionSpacing
        val actionPanelHeight = if (expandedLayout) 216.dp else 204.dp
        val surchargeHeight = if (expandedLayout) 156.dp else 148.dp
        val indicatorSlotHeight = HomePagerIndicatorSlotHeight
        val meterHeight = (
            maxHeight -
                topOffset -
                bottomOffset -
                actionPanelHeight -
                surchargeHeight -
                indicatorSlotHeight -
                sectionSpacing * 3f
            ).coerceAtLeast(470.dp)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topOffset, bottom = bottomOffset),
            verticalArrangement = Arrangement.spacedBy(sectionSpacing)
        ) {
            MeterCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(meterHeight),
                passengerName = passengerName,
                fare = fare,
                isRunning = isRunning,
                tripDistanceKm = tripDistanceKm,
                elapsedSeconds = elapsedSeconds,
                farePolicy = farePolicy,
                selectedTheme = selectedTheme,
                onEditPassenger = onEditPassenger
            )

            HomeActionPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(actionPanelHeight),
                isRunning = isRunning,
                onStart = onStart,
                onStop = onStop,
                onGoFareSettings = onGoFareSettings,
                onGoThemeSettings = onGoThemeSettings,
                onGoHistory = onGoHistory
            )

            SurchargeSelector(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(surchargeHeight),
                selectedSurcharge = selectedSurcharge,
                enabled = !isRunning,
                onSelectSurcharge = onSelectSurcharge
            )

            HomePagerIndicatorSlot(currentPage = currentPage)
        }
    }
}

@Composable
private fun HomeActionPanel(
    modifier: Modifier = Modifier,
    isRunning: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onGoFareSettings: () -> Unit,
    onGoThemeSettings: () -> Unit,
    onGoHistory: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = G2RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = GlassWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.36f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val expandedPanel = maxWidth >= 700.dp
            val primaryButtonHeight = if (expandedPanel) 88.dp else 78.dp
            val secondaryButtonHeight = if (expandedPanel) 66.dp else 58.dp
            val primaryButtonFont = if (expandedPanel) 21.sp else 19.sp
            val secondaryButtonFont = if (expandedPanel) 19.sp else 17.sp
            val panelPadding = if (expandedPanel) 16.dp else 14.dp
            val buttonGap = if (expandedPanel) 14.dp else 12.dp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = panelPadding, vertical = panelPadding),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(buttonGap)
                ) {
                    AnimatedActionButton(
                        text = "주행시작",
                        modifier = Modifier.weight(1f),
                        active = !isRunning,
                        height = primaryButtonHeight,
                        fontSize = primaryButtonFont,
                        onClick = onStart
                    )
                    AnimatedActionButton(
                        text = "주행종료",
                        modifier = Modifier.weight(1f),
                        active = isRunning,
                        height = primaryButtonHeight,
                        fontSize = primaryButtonFont,
                        onClick = onStop
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(buttonGap),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CompactGhostButton(text = "요금제 설정", modifier = Modifier.weight(1f), height = secondaryButtonHeight, fontSize = secondaryButtonFont, onClick = onGoFareSettings)
                    CompactGhostButton(text = "테마", modifier = Modifier.weight(1f), height = secondaryButtonHeight, fontSize = secondaryButtonFont, onClick = onGoThemeSettings)
                    CompactGhostButton(text = "정산하기", modifier = Modifier.weight(1f), height = secondaryButtonHeight, fontSize = secondaryButtonFont, onClick = onGoHistory)
                }
            }
        }
    }
}

@Composable
private fun CompactGhostButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 62.dp,
    fontSize: androidx.compose.ui.unit.TextUnit = 17.sp,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.height(height),
        onClick = onClick,
        enabled = enabled,
        shape = G2RoundedCornerShape(22.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDDE6F2).copy(alpha = 0.78f)),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonGray,
            contentColor = DarkText,
            disabledContainerColor = ButtonGray.copy(alpha = 0.54f),
            disabledContentColor = Color(0xFFB8B8B8)
        )
    ) {
        Text(text = text, fontSize = fontSize, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
    }
}

@Composable
private fun PassengerWidgetPage(
    records: List<RideRecord>,
    passengerWidgets: List<String>,
    currentPage: Int,
    onAddClick: () -> Unit,
    onRemovePassengerWidget: (String) -> Unit,
    onPassengerClick: (String) -> Unit,
    onGoChallenges: () -> Unit
) {
    var deleteTargetName by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val bounceOffset = remember { Animatable(0f) }
    val recentCutoffDate = LocalDate.now().minusDays(30)
    val currentMonth = YearMonth.now()
    val monthlyRecords = records.filter { record ->
        record.recordDateOrNull()?.let { YearMonth.from(it) == currentMonth } == true
    }
    val monthlyRevenue = monthlyRecords.sumOf { it.totalFare }
    val monthlyDistance = monthlyRecords.sumOf { it.distanceKm.toDouble() }.toFloat()
    val recentPassengerGroups = records
        .filter { record ->
            record.recordDateOrNull()?.let { !it.isBefore(recentCutoffDate) } == true
        }
        .groupBy { it.passengerName.trim().ifBlank { "이름 없는" } }
        .mapValues { entry ->
            entry.value.sortedWith(
                compareByDescending<RideRecord> { it.recordDateOrNull() }.thenByDescending { it.endTime }
            )
        }
        .toList()
        .sortedWith(
            compareByDescending<Pair<String, List<RideRecord>>> { it.second.firstOrNull()?.recordDateOrNull() }
                .thenByDescending { it.second.firstOrNull()?.endTime.orEmpty() }
        )
    val bounceConnection = remember(listState) {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val pullingDownAtTop = !listState.canScrollBackward && available.y > 0f
                val pullingUpAtBottom = !listState.canScrollForward && available.y < 0f

                if (pullingDownAtTop || pullingUpAtBottom) {
                    scope.launch {
                        bounceOffset.snapTo(bounceOffset.value + available.y * 0.20f)
                    }
                }

                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                bounceOffset.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = 0.82f,
                        stiffness = Spring.StiffnessLow
                    )
                )
                return Velocity.Zero
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(HomePagerSectionSpacing)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = G2RoundedCornerShape(34.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.62f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.ppap2),
                    contentDescription = null,
                    modifier = Modifier
                        .matchParentSize()
                        .graphicsLayer { alpha = 0.72f },
                    contentScale = ContentScale.Crop,
                    alignment = BiasAlignment(horizontalBias = -0.34f, verticalBias = 0f)
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .height(220.dp)
                        .clipToBounds()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ppap2),
                        contentDescription = null,
                        modifier = Modifier
                            .matchParentSize()
                            .blur(20.dp)
                            .graphicsLayer { alpha = 0.46f },
                        contentScale = ContentScale.Crop,
                        alignment = BiasAlignment(horizontalBias = -0.34f, verticalBias = 0f)
                    )
                }

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colorStops = arrayOf(
                                    0.00f to Color.White.copy(alpha = 0.86f),
                                    0.18f to Color.White.copy(alpha = 0.58f),
                                    0.46f to Color.White.copy(alpha = 0.24f),
                                    0.72f to Color.White.copy(alpha = 0.26f),
                                    1.00f to Color.White.copy(alpha = 0.68f)
                                )
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.88f),
                                    Color.White.copy(alpha = 0.30f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(210.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.30f),
                                    Color.White.copy(alpha = 0.82f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(G2RoundedCornerShape(26.dp))
                            .background(Color.White.copy(alpha = 0.74f))
                            .border(1.dp, Color.White.copy(alpha = 0.62f), G2RoundedCornerShape(26.dp))
                            .padding(horizontal = 22.dp, vertical = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "최근 손님", color = DarkText, fontSize = 32.sp, fontWeight = FontWeight.Black)
                        Text(
                            text = "최근 30일간의 이용 내역과 영수증을 빠르게 열어볼 수 있어요",
                            color = GrayText,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 24.sp
                        )
                    }

                    MonthlyRevenueSummaryCard(
                        revenue = monthlyRevenue,
                        rideCount = monthlyRecords.size,
                        distanceKm = monthlyDistance
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .nestedScroll(bounceConnection)
                            .clipToBounds()
                    ) {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { translationY = bounceOffset.value },
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (recentPassengerGroups.isEmpty()) {
                                item {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = G2RoundedCornerShape(26.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.94f)),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.72f)),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                                    ) {
                                        Text(
                                            text = "최근 카풀했던 사람의 이름이 표시됩니다",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 28.dp, vertical = 80.dp),
                                            color = GrayText,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            } else {
                                items(
                                    items = recentPassengerGroups,
                                    key = { it.first }
                                ) { (name, passengerRecords) ->
                                    val latest = passengerRecords.first()
                                    val totalFare = passengerRecords.sumOf { it.totalFare }
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onPassengerClick(name) },
                                        shape = G2RoundedCornerShape(26.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.94f)),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.72f)),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 22.dp, vertical = 20.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(
                                                modifier = Modifier.weight(1f),
                                                verticalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text(text = "$name 손님", color = DarkText, fontSize = 24.sp, fontWeight = FontWeight.Black)
                                                Text(
                                                    text = "총 ${passengerRecords.size}회 이용 · 최근 ${latest.date} ${latest.endTime}",
                                                    color = GrayText,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                            Text(
                                                text = "${totalFare.formatWon()}원",
                                                color = PrimaryBlue,
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            onClick = onAddClick,
            shape = G2RoundedCornerShape(999.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue, contentColor = Color.White)
        ) {
            Text(text = "+", fontSize = 38.sp, fontWeight = FontWeight.Black)
        }

        ChallengeEntryButton(onClick = onGoChallenges)

        HomePagerIndicatorSlot(currentPage = currentPage)
    }

    deleteTargetName?.let { name ->
        ConfirmDeleteWidgetDialog(
            onConfirm = {
                onRemovePassengerWidget(name)
                deleteTargetName = null
            },
            onDismiss = { deleteTargetName = null }
        )
    }
}

@Composable
private fun MonthlyRevenueSummaryCard(
    revenue: Int,
    rideCount: Int,
    distanceKm: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = G2RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.82f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.58f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = "이번 달 누적 수익", color = DarkText, fontSize = 21.sp, fontWeight = FontWeight.Black)
                Text(
                    text = "총 ${rideCount}회 · ${"%.1f".format(distanceKm)}km",
                    color = GrayText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = "${revenue.formatWon()}원",
                modifier = Modifier.padding(start = 18.dp),
                color = PrimaryBlue,
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun ChallengeEntryButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        onClick = onClick,
        shape = G2RoundedCornerShape(999.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = 0.94f),
            contentColor = DarkText
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_challenge_badge),
                contentDescription = null,
                modifier = Modifier.size(46.dp)
            )
            Text(text = "도전 과제", fontSize = 22.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun ChallengesScreen(
    records: List<RideRecord>,
    onGoHome: () -> Unit,
    onOpenBadgeBoard: () -> Unit
) {
    val challenges = buildChallengeItems(records)
    val completedCount = challenges.count { it.progress >= 1f }

    PageHeader(
        title = "도전 과제",
        subtitle = "카풀 기록에 따라 자동으로 완수율이 올라가요."
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenBadgeBoard() },
        shape = G2RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8ECF4)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_challenge_badge),
                contentDescription = null,
                modifier = Modifier.size(86.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "카풀 배지 보드", color = DarkText, fontSize = 30.sp, fontWeight = FontWeight.Black)
                Text(
                    text = "완료 $completedCount/${challenges.size}개 · 완료한 주행 ${records.size}회",
                    color = GrayText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "눌러서 완료한 배지를 볼 수 있어요",
                    color = PrimaryBlue,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }

    OrangeButton(text = "돌아가기", modifier = Modifier.fillMaxWidth(), onClick = onGoHome)

    challenges.forEach { challenge ->
        ChallengeCard(challenge = challenge)
    }
}

@Composable
private fun ChallengeCard(challenge: ChallengeItem) {
    val animatedProgress by animateFloatAsState(
        targetValue = challenge.progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 620, easing = FastOutSlowInEasing),
        label = "challengeProgress"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = G2RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8ECF4)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = challenge.iconRes()),
                contentDescription = null,
                modifier = Modifier.size(84.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(text = challenge.title, color = DarkText, fontSize = 24.sp, fontWeight = FontWeight.Black)
                        Text(text = challenge.description, color = GrayText, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                    Text(
                        text = "${(challenge.progress.coerceIn(0f, 1f) * 100).roundToInt()}%",
                        color = PrimaryBlue,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                ChallengeProgressBar(progress = animatedProgress)

                Text(
                    text = challenge.progressLabel,
                    color = if (challenge.progress >= 1f) PrimaryBlue else GrayText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
private fun ChallengeBadgeBoardScreen(
    records: List<RideRecord>,
    onGoBack: () -> Unit
) {
    val challenges = buildChallengeItems(records)
    val completedChallenges = challenges.filter { it.progress >= 1f }

    PageHeader(
        title = "카풀 배지 보드",
        subtitle = "완료한 도전 과제만 모아봤어요."
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = G2RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8ECF4)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_challenge_badge),
                contentDescription = null,
                modifier = Modifier.size(88.dp)
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "완료 ${completedChallenges.size}/${challenges.size}개",
                    color = DarkText,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "완료한 배지만 이 보드에 정리됩니다.",
                    color = GrayText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    if (completedChallenges.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = G2RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8ECF4)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Text(
                text = "아직 완료한 도전 과제가 없어요.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 72.dp),
                color = GrayText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    } else {
        completedChallenges.chunked(3).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { challenge ->
                    CompletedBadgeItem(
                        challenge = challenge,
                        modifier = Modifier.weight(1f)
                    )
                }
                repeat(3 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }

    OrangeButton(text = "도전 과제로 돌아가기", modifier = Modifier.fillMaxWidth(), onClick = onGoBack)
}

@Composable
private fun ChallengeBadgeBoardDialog(
    completedChallenges: List<ChallengeItem>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = G2RoundedCornerShape(34.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8ECF4)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_challenge_badge),
                        contentDescription = null,
                        modifier = Modifier.size(72.dp)
                    )
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "카풀 배지 보드", color = DarkText, fontSize = 30.sp, fontWeight = FontWeight.Black)
                        Text(
                            text = "완료한 도전 과제를 모아봤어요",
                            color = GrayText,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                if (completedChallenges.isEmpty()) {
                    Text(
                        text = "아직 완료한 도전 과제가 없어요.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 42.dp),
                        color = GrayText,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.height(520.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(completedChallenges.chunked(3)) { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                rowItems.forEach { challenge ->
                                    CompletedBadgeItem(
                                        challenge = challenge,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                repeat(3 - rowItems.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                OrangeButton(text = "닫기", modifier = Modifier.fillMaxWidth(), onClick = onDismiss)
            }
        }
    }
}

@Composable
private fun CompletedBadgeItem(challenge: ChallengeItem, modifier: Modifier = Modifier) {
    var visible by remember(challenge.title) { mutableStateOf(false) }
    LaunchedEffect(challenge.title) {
        kotlinx.coroutines.delay(80L)
        visible = true
    }
    val springScale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.72f,
        animationSpec = spring(
            dampingRatio = 0.58f,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "completedBadgeScale"
    )
    val springAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = 0.75f,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "completedBadgeAlpha"
    )

    Card(
        modifier = modifier
            .height(174.dp)
            .graphicsLayer {
                scaleX = springScale
                scaleY = springScale
                alpha = springAlpha
            },
        shape = G2RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Panel),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE1E7F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = challenge.iconRes()),
                contentDescription = null,
                modifier = Modifier
                    .size(96.dp)
                    .graphicsLayer {
                        scaleX = 1.28f
                        scaleY = 1.28f
                    }
            )
            Text(
                text = challenge.title,
                color = DarkText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
            Text(
                text = "완료",
                color = PrimaryBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

private fun ChallengeItem.iconRes(): Int =
    when (title) {
        "첫 영수증 발급" -> R.drawable.challenge_icon_09
        "단골 손님 메모리" -> R.drawable.challenge_icon_02
        "최근 30일 카풀러" -> R.drawable.challenge_icon_19
        "100km 동행" -> R.drawable.challenge_icon_11
        "10만원 정산왕" -> R.drawable.challenge_icon_16
        "1시간 안전 운행" -> R.drawable.challenge_icon_20
        "나이트 영수증 컬렉터" -> R.drawable.challenge_icon_21
        "장거리 동행" -> R.drawable.challenge_icon_10
        "동네 한 바퀴" -> R.drawable.challenge_icon_14
        "통행료 정산러" -> R.drawable.challenge_icon_07
        "추가요금 협상가" -> R.drawable.challenge_icon_05
        "만원 넘는 카풀" -> R.drawable.challenge_icon_08
        "테마 수집가" -> R.drawable.challenge_icon_03
        "찐 단골 인증" -> R.drawable.challenge_icon_13
        "하루 카풀 챌린지" -> R.drawable.challenge_icon_06
        else -> R.drawable.ic_challenge_badge
    }

@Composable
private fun ChallengeProgressBar(progress: Float) {
    val shape = G2RoundedCornerShape(999.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(18.dp)
            .background(Panel, shape)
            .border(1.dp, Color(0xFFE1E7F0), shape)
            .clip(shape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .height(18.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(PrimaryBlue, Color(0xFF49A8FF))
                    ),
                    shape
                )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PassengerWidgetSlot(
    name: String?,
    modifier: Modifier = Modifier,
    onRemoveRequest: (String) -> Unit
) {
    var editMode by remember(name) { mutableStateOf(false) }
    val shape = G2RoundedCornerShape(28.dp)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = 0.50f), shape)
            .border(1.dp, Color.White.copy(alpha = 0.76f), shape)
            .combinedClickable(
                onClick = {},
                onLongClick = { if (name != null) editMode = true }
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(18.dp)
                .background(Color.White.copy(alpha = 0.20f), shape)
        )

        if (name == null) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                val lineColor = Color.White.copy(alpha = 0.78f)
                val strokeWidth = 1.5.dp.toPx()
                drawLine(
                    color = lineColor,
                    start = Offset(size.width / 2f, 0f),
                    end = Offset(size.width / 2f, size.height),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = lineColor,
                    start = Offset(0f, size.height / 2f),
                    end = Offset(size.width, size.height / 2f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }
        } else {
            Text(
                text = name,
                modifier = Modifier.padding(16.dp),
                color = DarkText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
        }

        if (editMode && name != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(42.dp)
                    .background(Color.White.copy(alpha = 0.88f), CircleShape)
                    .border(1.dp, Color.White.copy(alpha = 0.92f), CircleShape)
                    .clickable { onRemoveRequest(name) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "-", color = PrimaryBlue, fontSize = 28.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun ConfirmDeleteWidgetDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = G2RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                Text(
                    text = "내역 위젯을 지우시겠습니까?",
                    color = DarkText,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OrangeButton(text = "예", modifier = Modifier.weight(1f), onClick = onConfirm)
                    GhostButton(text = "아니오", modifier = Modifier.weight(1f), onClick = onDismiss)
                }
            }
        }
    }
}

@Composable
private fun HomeOverviewPage(
    fare: Int,
    isRunning: Boolean,
    currentDistanceKm: Float?,
    tripDistanceKm: Float,
    elapsedSeconds: Long,
    farePolicy: FarePolicy,
    selectedRegion: FareRegion,
    selectedSurcharge: SurchargeMode,
    currentPage: Int,
    onGoFareSettings: () -> Unit,
    onGoThemeSettings: () -> Unit,
    onGoHistory: () -> Unit,
    onGoInfo: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(310.dp),
        shape = G2RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = GlassWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(26.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "주행 요약", color = DarkText, fontSize = 34.sp, fontWeight = FontWeight.Black)
                Text(
                    text = if (isRunning) "현재 주행 데이터를 실시간으로 보고 있어요" else "스와이프해서 요약과 설정을 빠르게 확인해요",
                    color = GrayText,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = "${fare.formatWon()}원",
                modifier = Modifier.fillMaxWidth(),
                color = PrimaryBlue,
                fontSize = 58.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.End
            )
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DashboardTile(
            title = "이번 주행",
            value = "%.3fkm".format(tripDistanceKm),
            modifier = Modifier.weight(1f)
        )
        DashboardTile(
            title = "주행 시간",
            value = elapsedSeconds.formatDuration(),
            modifier = Modifier.weight(1f)
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DashboardTile(
            title = "현재 거리",
            value = currentDistanceKm?.let { "%.1fkm".format(it) } ?: "--",
            modifier = Modifier.weight(1f)
        )
        DashboardTile(
            title = "요금제",
            value = selectedSurcharge.buttonText,
            modifier = Modifier.weight(1f)
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = G2RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "빠른 이동", color = DarkText, fontSize = 24.sp, fontWeight = FontWeight.Black)
            Text(
                text = farePolicy.summaryText(),
                color = GrayText,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GhostButton(text = "요금제 설정", modifier = Modifier.weight(1f), onClick = onGoFareSettings)
                GhostButton(text = "테마", modifier = Modifier.weight(1f), onClick = onGoThemeSettings)
                GhostButton(text = "내역", modifier = Modifier.weight(1f), onClick = onGoHistory)
            }
        }
    }

    HomePagerIndicatorSlot(currentPage = currentPage)
}

@Composable
private fun HomeQuickSettingsPage(
    isRunning: Boolean,
    farePolicy: FarePolicy,
    selectedRegion: FareRegion,
    selectedSurcharge: SurchargeMode,
    currentPage: Int,
    onGoFareSettings: () -> Unit,
    onGoThemeSettings: () -> Unit,
    onGoHistory: () -> Unit,
    onGoInfo: () -> Unit,
    onGoDonation: () -> Unit
) {
    var autoSurchargeEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(HomePagerSectionSpacing)
    ) {
        BouncyScrollableColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                shape = G2RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = GlassWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(26.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "설정 요약", color = DarkText, fontSize = 34.sp, fontWeight = FontWeight.Black)
                        Text(
                            text = if (isRunning) "주행 중에는 요금제를 바꾸지 않아요" else "카풀미터기 설정을 빠르게 확인해요",
                            color = GrayText,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DashboardTile(
                            title = "지역",
                            value = selectedRegion.displayName,
                            modifier = Modifier.weight(1f)
                        )
                        DashboardTile(
                            title = "할증",
                            value = selectedSurcharge.buttonText,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = G2RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(text = "현재 요금 규칙", color = DarkText, fontSize = 24.sp, fontWeight = FontWeight.Black)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DashboardTile(
                            title = "기본 요금",
                            value = "${farePolicy.baseFare.formatWon()}원",
                            modifier = Modifier.weight(1f)
                        )
                        DashboardTile(
                            title = "거리 요금",
                            value = "${farePolicy.stepDistanceMeters.toInt()}m",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Text(
                        text = farePolicy.distanceRuleText(),
                        color = GrayText,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            SmartSettingsCard(
                modifier = Modifier.height(QuickSettingsSecondaryHeight),
                autoSurchargeEnabled = autoSurchargeEnabled,
                onAutoSurchargeChange = { autoSurchargeEnabled = it }
            )

            DeveloperSupportBanner(
                modifier = Modifier.height(QuickSettingsSupportHeight),
                onClick = onGoDonation
            )

            SettingsInfoButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onGoInfo
            )
        }

        HomePagerIndicatorSlot(currentPage = currentPage)
    }
}

@Composable
private fun ResetHistoryDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing)),
        exit = fadeOut(animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.46f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(durationMillis = 240, easing = FastOutSlowInEasing)) +
                    scaleIn(
                        initialScale = 0.965f,
                        animationSpec = tween(durationMillis = 340, easing = FastOutSlowInEasing)
                    ),
                exit = fadeOut(animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing)) +
                    scaleOut(
                        targetScale = 0.98f,
                        animationSpec = tween(durationMillis = 190, easing = FastOutSlowInEasing)
                    )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 34.dp)
                        .clickable(onClick = {}),
                    shape = G2RoundedCornerShape(34.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.72f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 14.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 30.dp, vertical = 28.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "!",
                                color = Color(0xFFFF3B30),
                                fontSize = 38.sp,
                                fontWeight = FontWeight.Black,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "전체 내역 초기화",
                                color = DarkText,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Black,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "모든 주행 내역과 정산 기록을 삭제하시겠습니까?",
                                color = GrayText,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                lineHeight = 24.sp
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            GhostButton(
                                text = "취소",
                                modifier = Modifier
                                    .weight(1f)
                                    .height(58.dp),
                                onClick = onDismiss
                            )
                            Button(
                                onClick = onDelete,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(58.dp),
                                shape = G2RoundedCornerShape(22.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF3B30),
                                    contentColor = Color.White
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
                            ) {
                                Text(text = "삭제", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StandaloneDetailHeader(
    title: String,
    subtitle: String? = null,
    onGoBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Box(
            modifier = Modifier
                .size(62.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.92f))
                .border(1.dp, Color.White.copy(alpha = 0.76f), CircleShape)
                .clickable(onClick = onGoBack),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "←",
                color = DarkText,
                fontSize = 34.sp,
                fontWeight = FontWeight.Black,
                lineHeight = 34.sp
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                color = DarkText,
                fontSize = 40.sp,
                fontWeight = FontWeight.Black
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = GrayText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun AppInfoScreen(
    selectedTheme: MeshTheme,
    onGoBack: () -> Unit,
    onResetHistory: () -> Unit
) {
    var showResetDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        StandaloneDetailHeader(title = "정보", onGoBack = onGoBack)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(460.dp),
            shape = G2RoundedCornerShape(42.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(G2RoundedCornerShape(42.dp))
            ) {
                Image(
                    painter = painterResource(id = selectedTheme.drawableRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0.26f))
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF2D7CFF).copy(alpha = 0.72f),
                                    Color(0xFF53E0C6).copy(alpha = 0.62f),
                                    Color(0xFFFFE178).copy(alpha = 0.52f),
                                    Color(0xFFFF7FA5).copy(alpha = 0.56f)
                                )
                            )
                        )
                )
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.33f),
                        radius = size.minDimension * 0.48f,
                        center = Offset(size.width * 0.82f, size.height * 0.12f)
                    )
                    drawCircle(
                        color = Color(0xFF072E68).copy(alpha = 0.30f),
                        radius = size.minDimension * 0.62f,
                        center = Offset(size.width * 0.04f, size.height * 0.92f)
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.20f),
                        radius = size.minDimension * 0.42f,
                        center = Offset(size.width * 0.52f, size.height * 0.56f)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(42.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "1.0",
                        color = Color.White,
                        fontSize = 96.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 96.sp
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "카풀 미터기",
                            color = Color.White,
                            fontSize = 50.sp,
                            fontWeight = FontWeight.Black,
                            lineHeight = 54.sp
                        )
                        Text(
                            text = "Pleos Carpool Meter",
                            color = Color.White.copy(alpha = 0.88f),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = G2RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.94f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.72f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 30.dp, vertical = 12.dp)
            ) {
                AppInfoRow(title = "소프트웨어 버전", value = "1.0.0")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFE2E5EA))
                )
                AppInfoRow(title = "개발자 건의사항 및 문의", value = "appleslie0@gmail.com")
            }
        }

        DangerButton(
            text = "전체 내역 초기화",
            modifier = Modifier.fillMaxWidth(),
            onClick = { showResetDialog = true }
        )
    }

    ResetHistoryDialog(
        visible = showResetDialog,
        onDismiss = { showResetDialog = false },
        onDelete = {
            showResetDialog = false
            onResetHistory()
            Toast.makeText(context, "전체 내역을 초기화했어요", Toast.LENGTH_SHORT).show()
        }
    )
}

@Composable
private fun AppInfoRow(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = DarkText,
            fontSize = 25.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = value,
            color = GrayText,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        )
    }
}

private data class DonationItem(
    val name: String,
    val price: String,
    val imageRes: Int
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DonationScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit
) {
    val donationItems = remember {
        listOf(
            DonationItem("🍬 졸음 번쩍 껌 한 통", "1,000원", R.drawable.ggum),
            DonationItem("☕ 시원한 아아 한잔", "1,500원", R.drawable.ice),
            DonationItem("⚡ 시원한 에너지 드링크", "3,500원", R.drawable.monster),
            DonationItem("🥪 가난한 편의점 샌드위치", "4,500원", R.drawable.sandwich),
            DonationItem("🍕 야근엔 역시 피자", "15,000원", R.drawable.pizza),
            DonationItem("🍗 야식 치킨 수혈", "20,000원", R.drawable.chicken),
            DonationItem("⛽ 가득이요! VIP 주유권", "50,000원", R.drawable.gas)
        )
    }
    val donationPages = remember(donationItems) { donationItems.chunked(4) }
    val pagerState = rememberPagerState(pageCount = { donationPages.size })

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        StandaloneDetailHeader(
            title = "개발자 후원",
            subtitle = "커피 한 잔만큼 앱이 더 부드러워져요",
            onGoBack = onGoBack
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(end = 54.dp),
            pageSpacing = 16.dp
        ) { page ->
            DonationPageCard(items = donationPages[page])
        }

        HomePagerIndicatorSlot(
            currentPage = pagerState.currentPage,
            pageCount = donationPages.size
        )
    }
}

@Composable
private fun DonationPageCard(items: List<DonationItem>) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = G2RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.94f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.78f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DonationGridRow(items = items.take(2), modifier = Modifier.weight(1f))
            DonationGridRow(items = items.drop(2).take(2), modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun DonationGridRow(
    items: List<DonationItem>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        items.forEach { item ->
            DonationItemCard(
                item = item,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            )
        }
        repeat(2 - items.size) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun DonationItemCard(
    item: DonationItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = G2RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Line.copy(alpha = 0.72f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.22f)
                    .clip(G2RoundedCornerShape(26.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFFF4EC),
                                Color(0xFFFFDDEB),
                                Color(0xFFE9F7FF),
                                Color(0xFFEFFFFB)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.48f),
                        radius = size.minDimension * 0.44f,
                        center = Offset(size.width * 0.25f, size.height * 0.12f)
                    )
                    drawCircle(
                        color = Color(0xFFFF9F65).copy(alpha = 0.22f),
                        radius = size.minDimension * 0.40f,
                        center = Offset(size.width * 0.86f, size.height * 0.18f)
                    )
                    drawCircle(
                        color = Color(0xFF64D8FF).copy(alpha = 0.25f),
                        radius = size.minDimension * 0.42f,
                        center = Offset(size.width * 0.48f, size.height * 0.92f)
                    )
                }
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp)
                        .graphicsLayer {
                            scaleX = 1.08f
                            scaleY = 1.08f
                        },
                    contentScale = ContentScale.Fit
                )
            }

            Text(
                text = item.name,
                color = DarkText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = item.price,
                color = PrimaryBlue,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            OrangeButton(
                text = "후원하기",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = {}
            )
        }
    }
}

private data class SupportSnack(
    val label: String,
    val imageRes: Int,
    val onClick: () -> Unit = {}
)

@Composable
private fun DeveloperSupportBanner(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = G2RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFFD7B8),
                            Color(0xFFFF8E4D),
                            Color(0xFFFF5F7A),
                            Color(0xFFFFC0A6)
                        )
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.52f),
                                Color.Transparent
                            ),
                            radius = 860f
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 26.dp, vertical = 22.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🥺 불쌍한 밤샘 개발자를\n커피로 후원해 주세요 ☕",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 34.sp
                )
            }
        }
    }
}

@Composable
private fun SettingsInfoButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.height(QuickSettingsSecondaryHeight),
        onClick = onClick,
        shape = G2RoundedCornerShape(30.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Panel,
            contentColor = DarkText
        )
    ) {
        Text(text = "정보", fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SupportSnackButton(snack: SupportSnack) {
    Card(
        modifier = Modifier
            .width(176.dp)
            .height(158.dp)
            .clickable(onClick = snack.onClick),
        shape = G2RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.82f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.50f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = snack.imageRes),
                contentDescription = null,
                modifier = Modifier.size(76.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = snack.label,
                color = DarkText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SmartSettingsCard(
    modifier: Modifier = Modifier,
    autoSurchargeEnabled: Boolean,
    onAutoSurchargeChange: (Boolean) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = G2RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Text(text = "스마트 설정", color = DarkText, fontSize = 25.sp, fontWeight = FontWeight.Black)
                Text(
                    text = "⏰ 지역 및 시간 기반 자동 할증",
                    color = DarkText,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "선택한 지역과 현재 시간을 인식해 할증 요금을 자동으로 적용합니다",
                    color = GrayText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp
                )
            }
            Box(
                modifier = Modifier
                    .width(112.dp)
                    .height(88.dp),
                contentAlignment = Alignment.Center
            ) {
                Switch(
                    checked = autoSurchargeEnabled,
                    onCheckedChange = onAutoSurchargeChange,
                    modifier = Modifier.graphicsLayer {
                        scaleX = 1.22f
                        scaleY = 1.22f
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = PrimaryBlue,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Panel
                    )
                )
            }
        }
    }
}

@Composable
private fun HomePagerIndicatorSlot(
    currentPage: Int,
    pageCount: Int = 3,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(HomePagerIndicatorSlotHeight),
        contentAlignment = Alignment.Center
    ) {
        HomePagerIndicator(
            currentPage = currentPage,
            pageCount = pageCount,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = HomePagerIndicatorYOffset)
        )
    }
}

@Composable
private fun HomePagerIndicator(
    currentPage: Int,
    pageCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val active = index == currentPage
            val dotWidth by animateDpAsState(
                targetValue = if (active) 28.dp else 10.dp,
                animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing),
                label = "pagerDotWidth"
            )
            val dotColor by animateColorAsState(
                targetValue = if (active) PrimaryBlue else Color.White.copy(alpha = 0.62f),
                animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing),
                label = "pagerDotColor"
            )
            val borderColor by animateColorAsState(
                targetValue = Color.White.copy(alpha = if (active) 0.45f else 0.7f),
                animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing),
                label = "pagerDotBorder"
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(width = dotWidth, height = 10.dp)
                    .background(
                        color = dotColor,
                        shape = G2RoundedCornerShape(999.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = G2RoundedCornerShape(999.dp)
                    )
            )
        }
    }
}

@Composable
private fun PassengerEditDialogOverlay(
    visible: Boolean,
    editingPassengerName: String,
    onNameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing)),
        exit = fadeOut(animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.46f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(durationMillis = 240, easing = FastOutSlowInEasing)) +
                    scaleIn(
                        initialScale = 0.965f,
                        animationSpec = tween(durationMillis = 340, easing = FastOutSlowInEasing)
                    ),
                exit = fadeOut(animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing)) +
                    scaleOut(
                        targetScale = 0.98f,
                        animationSpec = tween(durationMillis = 190, easing = FastOutSlowInEasing)
                    )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 34.dp)
                        .clickable(onClick = {}),
                    shape = G2RoundedCornerShape(34.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.72f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 14.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 30.dp, vertical = 28.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "손님 정보 수정",
                                color = DarkText,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "영수증과 주행 종료 화면에 표시될 이름입니다.",
                                color = GrayText,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        OutlinedTextField(
                            value = editingPassengerName,
                            onValueChange = onNameChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(76.dp),
                            singleLine = true,
                            label = { Text("손님 이름", fontSize = 16.sp) },
                            shape = G2RoundedCornerShape(22.dp),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            GhostButton(
                                text = "취소",
                                modifier = Modifier
                                    .weight(1f)
                                    .height(58.dp),
                                onClick = onDismiss
                            )
                            OrangeButton(
                                text = "저장",
                                modifier = Modifier
                                    .weight(1f)
                                    .height(58.dp),
                                onClick = onSave
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MeterCard(
    modifier: Modifier = Modifier,
    passengerName: String,
    fare: Int,
    isRunning: Boolean,
    tripDistanceKm: Float,
    elapsedSeconds: Long,
    farePolicy: FarePolicy,
    selectedTheme: MeshTheme,
    onEditPassenger: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = G2RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val expandedCard = maxWidth >= 700.dp
            val wideCard = maxWidth >= 560.dp
            val cardPadding = when {
                expandedCard -> 29.dp
                wideCard -> 26.dp
                else -> 24.dp
            }
            val nameFont = when {
                expandedCard -> 37.sp
                wideCard -> 34.sp
                else -> 31.sp
            }
            val dateFont = when {
                expandedCard -> 22.sp
                wideCard -> 20.sp
                else -> 18.sp
            }
            val centerLabelFont = when {
                expandedCard -> 25.sp
                wideCard -> 22.sp
                else -> 20.sp
            }
            val fareFont = when {
                expandedCard -> 112.sp
                wideCard -> 102.sp
                else -> 88.sp
            }
            val fareLineHeight = when {
                expandedCard -> 114.sp
                wideCard -> 104.sp
                else -> 90.sp
            }
            val policyFont = when {
                expandedCard -> 20.sp
                wideCard -> 18.sp
                else -> 16.sp
            }
            val chipFont = when {
                expandedCard -> 22.sp
                wideCard -> 15.sp
                else -> 14.sp
            }
            val chipHorizontalPadding = when {
                expandedCard -> 24.dp
                wideCard -> 15.dp
                else -> 13.dp
            }
            val chipVerticalPadding = when {
                expandedCard -> 17.dp
                wideCard -> 10.dp
                else -> 9.dp
            }
            val chipGap = if (expandedCard) 14.dp else 9.dp
            val bottomPanelPaddingH = when {
                expandedCard -> 24.dp
                wideCard -> 22.dp
                else -> 19.dp
            }
            val bottomPanelPaddingV = when {
                expandedCard -> 19.dp
                wideCard -> 17.dp
                else -> 15.dp
            }
            val metricTitleFont = when {
                expandedCard -> 17.sp
                wideCard -> 16.sp
                else -> 15.sp
            }
            val metricValueFont = when {
                expandedCard -> 27.sp
                wideCard -> 25.sp
                else -> 22.sp
            }
            val metricDividerHeight = when {
                expandedCard -> 54.dp
                wideCard -> 50.dp
                else -> 44.dp
            }

            Image(
                painter = painterResource(id = selectedTheme.drawableRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.16f),
                                Color.Black.copy(alpha = 0.12f),
                                Color.Black.copy(alpha = 0.24f)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(cardPadding),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(text = "$passengerName 손님", color = Color.White, fontSize = nameFont, fontWeight = FontWeight.Black)
                        Text(text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm")), color = Color.White.copy(alpha = 0.76f), fontSize = dateFont, fontWeight = FontWeight.Medium)
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(chipGap),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MeterStatusChip(
                            text = if (isRunning) "실제 주행" else "대기 중",
                            active = isRunning,
                            fontSize = chipFont,
                            horizontalPadding = chipHorizontalPadding,
                            verticalPadding = chipVerticalPadding
                        )
                        MeterStatusChip(
                            text = farePolicy.shortLabel,
                            active = true,
                            fontSize = chipFont,
                            horizontalPadding = chipHorizontalPadding,
                            verticalPadding = chipVerticalPadding
                        )
                        GlassTextChip(
                            text = "이름 변경",
                            fontSize = chipFont,
                            horizontalPadding = chipHorizontalPadding,
                            verticalPadding = chipVerticalPadding,
                            onClick = onEditPassenger
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "현재 요금",
                        color = Color.White.copy(alpha = 0.78f),
                        fontSize = centerLabelFont,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${fare.formatWon()}원",
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        fontSize = fareFont,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        lineHeight = fareLineHeight
                    )
                    Text(
                        text = farePolicy.summaryText(),
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White.copy(alpha = 0.78f),
                        fontSize = policyFont,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.92f), G2RoundedCornerShape(22.dp))
                        .padding(horizontal = bottomPanelPaddingH, vertical = bottomPanelPaddingV),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MeterMetricItem(
                        title = "주행시간",
                        value = elapsedSeconds.formatDuration(),
                        titleFontSize = metricTitleFont,
                        valueFontSize = metricValueFont,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(metricDividerHeight)
                            .background(Line)
                    )
                    MeterMetricItem(
                        title = "주행거리",
                        value = "%.3fkm".format(tripDistanceKm),
                        titleFontSize = metricTitleFont,
                        valueFontSize = metricValueFont,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun MeterStatusChip(
    text: String,
    active: Boolean,
    fontSize: androidx.compose.ui.unit.TextUnit = 15.sp,
    horizontalPadding: Dp = 15.dp,
    verticalPadding: Dp = 10.dp
) {
    val shape = G2RoundedCornerShape(22.dp)
    Text(
        text = text,
        modifier = Modifier
            .heightIn(min = verticalPadding * 2f + 24.dp)
            .background(
                if (active) PrimaryBlue.copy(alpha = 0.92f) else Color.White.copy(alpha = 0.22f),
                shape
            )
            .border(1.dp, Color.White.copy(alpha = 0.24f), shape)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        color = Color.White,
        fontSize = fontSize,
        fontWeight = FontWeight.Black,
        maxLines = 1
    )
}

@Composable
private fun MeterMetricItem(
    title: String,
    value: String,
    titleFontSize: androidx.compose.ui.unit.TextUnit = 15.sp,
    valueFontSize: androidx.compose.ui.unit.TextUnit = 22.sp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(text = title, color = GrayText, fontSize = titleFontSize, fontWeight = FontWeight.Bold)
        Text(text = value, color = DarkText, fontSize = valueFontSize, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun DriveStatusPanel(
    modifier: Modifier = Modifier,
    isRunning: Boolean,
    tripDistanceKm: Float,
    elapsedSeconds: Long
) {
    Card(
        modifier = modifier,
        shape = G2RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(text = "운행 상태", color = DarkText, fontSize = 24.sp, fontWeight = FontWeight.Black)
                }
                StatusChip(text = if (isRunning) "ON" else "READY", active = isRunning)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardTile(
                    title = "이번 주행",
                    value = "%.3fkm".format(tripDistanceKm),
                    modifier = Modifier.weight(1f)
                )
                DashboardTile(
                    title = "주행 시간",
                    value = elapsedSeconds.formatDuration(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun DashboardTile(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Panel, G2RoundedCornerShape(22.dp))
            .border(1.dp, Line, G2RoundedCornerShape(22.dp))
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = title, color = GrayText, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Text(text = value, color = DarkText, fontSize = 26.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun SurchargeSelector(
    modifier: Modifier = Modifier,
    selectedSurcharge: SurchargeMode,
    enabled: Boolean,
    onSelectSurcharge: (SurchargeMode) -> Unit
) {
    Card(
        modifier = modifier,
        shape = G2RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.94f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.52f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val expandedPanel = maxWidth >= 700.dp
            val titleFont = if (expandedPanel) 23.sp else 21.sp
            val hintFont = if (expandedPanel) 17.sp else 16.sp
            val buttonHeight = if (expandedPanel) 78.dp else 72.dp
            val buttonFont = if (expandedPanel) 21.sp else 19.sp
            val contentPadding = if (expandedPanel) 20.dp else 18.dp
            val rowGap = if (expandedPanel) 10.dp else 8.dp

        Column(
            modifier = Modifier.padding(horizontal = contentPadding, vertical = 15.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "할증 요금제", color = DarkText, fontSize = titleFont, fontWeight = FontWeight.Black)
                if (!enabled) {
                    Text(
                        text = "주행 중 변경 불가",
                        color = GrayText,
                        fontSize = hintFont,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(rowGap)
            ) {
                SurchargeMode.entries.forEach { mode ->
                    val selected = mode == selectedSurcharge
                    val buttonColor by animateColorAsState(
                        targetValue = if (selected) PrimaryBlue else ButtonGray,
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                        label = "surchargeButtonColor"
                    )
                    val textColor by animateColorAsState(
                        targetValue = if (selected) Color.White else DarkText,
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                        label = "surchargeTextColor"
                    )
                    val disabledButtonColor by animateColorAsState(
                        targetValue = if (selected) SoftBlue else ButtonGray,
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                        label = "surchargeDisabledButtonColor"
                    )
                    val disabledTextColor by animateColorAsState(
                        targetValue = if (selected) PrimaryBlue else GrayText,
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                        label = "surchargeDisabledTextColor"
                    )
                    val scale by animateFloatAsState(
                        targetValue = if (selected) 1.02f else 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        ),
                        label = "surchargeScale"
                    )
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(buttonHeight)
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            },
                        enabled = enabled,
                        onClick = { onSelectSurcharge(mode) },
                        shape = G2RoundedCornerShape(22.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonColor,
                            contentColor = textColor,
                            disabledContainerColor = disabledButtonColor,
                            disabledContentColor = disabledTextColor
                        )
                    ) {
                        Text(text = mode.buttonText, fontSize = buttonFont, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        }
    }
}

@Composable
private fun FareModeSelectScreen(
    onGoRegionalFare: () -> Unit,
    onGoCustomFare: () -> Unit,
    onGoBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 18.dp, end = 18.dp, top = 44.dp, bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StandaloneDetailHeader(
            title = "요금제 설정",
            subtitle = "요금제를 선택하는 방식을 고르세요.",
            onGoBack = onGoBack
        )

        FareModeLargeButton(
            title = "지역별 요금제 선택",
            subtitle = "서울, 경기, 지방 택시 요금 기준으로 자동 계산해요.",
            badge = "기존 방식",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onClick = onGoRegionalFare
        )

        FareModeLargeButton(
            title = "커스텀 요금제 선택",
            subtitle = "기본요금과 100m당 요금을 직접 정해서 계산해요.",
            badge = "직접 설정",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onClick = onGoCustomFare
        )
    }
}

@Composable
private fun FareModeLargeButton(
    title: String,
    subtitle: String,
    badge: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = G2RoundedCornerShape(34.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.96f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.52f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = badge,
                    modifier = Modifier
                        .background(SoftBlue, G2RoundedCornerShape(999.dp))
                        .padding(horizontal = 18.dp, vertical = 10.dp),
                    color = PrimaryBlue,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = title, color = DarkText, fontSize = 36.sp, fontWeight = FontWeight.Black)
                    Text(text = subtitle, color = GrayText, fontSize = 20.sp, fontWeight = FontWeight.Medium, lineHeight = 27.sp)
                }

                Text(
                    text = "선택하기",
                    modifier = Modifier.fillMaxWidth(),
                    color = PrimaryBlue,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
private fun CustomFareSettingsScreen(
    baseFare: Int,
    stepFare: Int,
    useCustomFare: Boolean,
    onSelectCustomFare: (Int, Int) -> Unit,
    onGoBack: () -> Unit,
    onGoHome: () -> Unit
) {
    val baseOptions = listOf(0, 3000, 4800, 5800, 6700, 10000)
    val stepOptions = listOf(50, 100, 120, 140, 200, 300)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 18.dp, end = 18.dp, top = 44.dp, bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        StandaloneDetailHeader(
            title = "커스텀 요금제",
            subtitle = if (useCustomFare) "현재 커스텀 요금제를 사용 중입니다." else "값을 선택하면 커스텀 요금제로 전환됩니다.",
            onGoBack = onGoBack
        )

        CustomFareOptionPanel(
            title = "기본요금",
            subtitle = "주행을 시작하면 바로 적용되는 금액",
            options = baseOptions,
            selectedValue = baseFare,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onSelect = { onSelectCustomFare(it, stepFare) }
        )

        CustomFareOptionPanel(
            title = "100m당 요금",
            subtitle = "주행거리 100m가 늘 때마다 더해지는 금액",
            options = stepOptions,
            selectedValue = stepFare,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onSelect = { onSelectCustomFare(baseFare, it) }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GhostButton(text = "뒤로가기", modifier = Modifier.weight(1f), height = 76.dp, fontSize = 20.sp, onClick = onGoBack)
            OrangeButton(text = "적용 완료", modifier = Modifier.weight(1f), height = 76.dp, onClick = onGoHome)
        }
    }
}

@Composable
private fun CustomFareOptionPanel(
    title: String,
    subtitle: String,
    options: List<Int>,
    selectedValue: Int,
    modifier: Modifier = Modifier,
    onSelect: (Int) -> Unit
) {
    Card(
        modifier = modifier,
        shape = G2RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.96f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.48f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Text(text = title, color = DarkText, fontSize = 30.sp, fontWeight = FontWeight.Black)
                Text(text = subtitle, color = GrayText, fontSize = 17.sp, fontWeight = FontWeight.Medium)
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                options.chunked(3).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowItems.forEach { value ->
                            val selected = value == selectedValue
                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(68.dp),
                                onClick = { onSelect(value) },
                                shape = G2RoundedCornerShape(22.dp),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selected) PrimaryBlue else ButtonGray,
                                    contentColor = if (selected) Color.White else DarkText
                                )
                            ) {
                                Text(text = "${value.formatWon()}원", fontSize = 18.sp, fontWeight = FontWeight.Black)
                            }
                        }
                        repeat(3 - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FareSettingsScreen(
    selectedRegion: FareRegion,
    onSelectRegion: (FareRegion) -> Unit,
    onGoHome: () -> Unit
) {
    StandaloneDetailHeader(
        title = "요금제 선택",
        subtitle = "주행 지역에 맞는 중형택시 요금제를 선택하세요.",
        onGoBack = onGoHome
    )

    FareRegion.entries.forEach { region ->
        val selected = region == selectedRegion
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectRegion(region) },
            shape = G2RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = if (selected) SoftBlue else Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = region.displayName, color = DarkText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "기본 ${region.baseFare.formatWon()}원 / ${region.baseDistanceKm}km",
                        color = GrayText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${region.stepDistanceMeters.toInt()}m당 ${region.stepFare}원 · 할증은 홈에서 직접 선택",
                        color = GrayText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                if (selected) {
                    Text(
                        text = "선택됨",
                        modifier = Modifier
                            .background(Color.White, G2RoundedCornerShape(999.dp))
                            .padding(horizontal = 13.dp, vertical = 8.dp),
                        color = PrimaryBlue,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeSettingsScreen(
    selectedTheme: MeshTheme,
    onSelectTheme: (MeshTheme) -> Unit,
    onGoHome: () -> Unit
) {
    StandaloneDetailHeader(
        title = "테마",
        subtitle = "배경을 선택하세요",
        onGoBack = onGoHome
    )

    MeshTheme.entries.chunked(2).forEach { rowThemes ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            rowThemes.forEach { theme ->
                ThemePreviewCard(
                    theme = theme,
                    selected = theme == selectedTheme,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectTheme(theme) }
                )
            }
            if (rowThemes.size == 1) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ThemePreviewCard(
    theme: MeshTheme,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(150.dp)
            .clickable(onClick = onClick),
        shape = G2RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = theme.drawableRes),
                contentDescription = theme.displayName,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = if (selected) 0.08f else 0.18f))
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.White.copy(alpha = 0.86f))
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = theme.displayName, color = DarkText, fontSize = 18.sp, fontWeight = FontWeight.Black)
                if (selected) {
                    Text(text = "선택됨", color = PrimaryBlue, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun EndRideScreen(
    passengerName: String,
    pendingRide: PendingRide?,
    tollText: String,
    extraText: String,
    selectedTheme: MeshTheme,
    onTollChange: (String) -> Unit,
    onExtraChange: (String) -> Unit,
    onSaveRide: () -> Unit,
    onGoHome: () -> Unit
) {
    val rideFare = pendingRide?.rideFare ?: 0
    val tollFare = tollText.toIntOrNull() ?: 0
    val extraFare = extraText.toIntOrNull() ?: 0
    val totalFare = rideFare + tollFare + extraFare

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        PageHeader(
            title = "$passengerName 손님,",
            subtitle = "오늘 주행은 어떠셨나요?"
        )

        val frostedShape = G2RoundedCornerShape(28.dp)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp),
            shape = frostedShape,
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.42f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = selectedTheme.drawableRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.18f),
                                    Color.Black.copy(alpha = 0.08f),
                                    Color.Black.copy(alpha = 0.22f)
                                )
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(text = "총 결제 예정 금액", color = Color.White.copy(alpha = 0.9f), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = "${totalFare.formatWon()}원",
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.White,
                            fontSize = 68.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.End
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FrostMetricCard(
                            title = "총 주행 시간",
                            value = pendingRide?.durationSeconds?.formatDuration() ?: "00:00:00",
                            modifier = Modifier.weight(1f)
                        )
                        FrostMetricCard(
                            title = "총 주행 거리",
                            value = "%.1fkm".format(pendingRide?.distanceKm ?: 0f),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = G2RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(32.dp), verticalArrangement = Arrangement.spacedBy(22.dp)) {
                Text(text = "정산 입력", color = DarkText, fontSize = 28.sp, fontWeight = FontWeight.Black)
                Text(text = "통행료나 별도 합의한 추가요금이 있으면 입력하세요.", color = GrayText, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                FareField(label = "운행요금", value = rideFare.formatWon(), enabled = false, onValueChange = {})
                FareField(label = "통행요금", value = tollText, enabled = true, onValueChange = onTollChange)
                FareField(label = "추가요금", value = extraText, enabled = true, onValueChange = onExtraChange)
            }
        }

        OrangeButton(text = "주행 완료", modifier = Modifier.fillMaxWidth(), height = 84.dp, onClick = onSaveRide)
    }
}

@Composable
private fun HistoryScreen(
    records: List<RideRecord>,
    onSelectPassenger: (String) -> Unit,
    widgetSelectionMode: Boolean,
    selectedWidgetPassengerName: String?,
    onSelectWidgetPassenger: (String) -> Unit,
    onAddWidgetPassenger: () -> Unit,
    returnButtonText: String,
    onReturnClick: () -> Unit
) {
    PageHeader(
        title = "내역 보기",
        subtitle = if (widgetSelectionMode) "위젯에 추가할 손님을 선택하세요." else "완료한 주행내역을 선택하면 영수증으로 볼 수 있어요."
    )

    OrangeButton(text = returnButtonText, modifier = Modifier.fillMaxWidth(), onClick = onReturnClick)

    val groupedRecords = records
        .groupBy { it.passengerName.trim() }
        .toSortedMap()
        .mapValues { entry -> entry.value.sortedWith(compareByDescending<RideRecord> { it.recordDateOrNull() }.thenByDescending { it.endTime }) }

    if (groupedRecords.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = G2RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Text(
                text = "아직 완료된 주행내역이 없습니다.",
                modifier = Modifier.padding(32.dp),
                color = GrayText,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    } else {
        groupedRecords.forEach { (name, passengerRecords) ->
            val latest = passengerRecords.first()
            val totalFare = passengerRecords.sumOf { it.totalFare }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (widgetSelectionMode) {
                            onSelectWidgetPassenger(name)
                        } else {
                            onSelectPassenger(name)
                        }
                    },
                shape = G2RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (widgetSelectionMode && selectedWidgetPassengerName == name) SoftBlue else Color.White
                ),
                border = if (widgetSelectionMode && selectedWidgetPassengerName == name) {
                    androidx.compose.foundation.BorderStroke(2.dp, PrimaryBlue)
                } else {
                    null
                },
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(text = "$name 손님", color = DarkText, fontSize = 20.sp, fontWeight = FontWeight.Black)
                        Text(text = "총 ${passengerRecords.size}회 이용 · 최근 ${latest.date} ${latest.endTime}", color = GrayText, fontSize = 15.sp)
                    }
                    Text(text = "${totalFare.formatWon()}원", color = PrimaryBlue, fontSize = 24.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }

    if (widgetSelectionMode && selectedWidgetPassengerName != null) {
        OrangeButton(
            text = "추가",
            modifier = Modifier.fillMaxWidth(),
            onClick = onAddWidgetPassenger
        )
    }
}

@Composable
private fun PassengerHistoryScreen(
    passengerName: String?,
    records: List<RideRecord>,
    selectedMonth: YearMonth,
    onChangeMonth: (YearMonth) -> Unit,
    onSelectRecord: (RideRecord) -> Unit,
    onGoBack: () -> Unit,
    onGoFullHistory: () -> Unit
) {
    val name = passengerName ?: return
    var selectedDate by remember(name, selectedMonth) { mutableStateOf<LocalDate?>(null) }
    var monthPickerVisible by remember { mutableStateOf(false) }
    val passengerRecords = records
        .filter { it.passengerName.trim() == name }
        .sortedWith(compareByDescending<RideRecord> { it.recordDateOrNull() }.thenByDescending { it.endTime })
    val monthRecords = passengerRecords.filter { record ->
        record.recordDateOrNull()?.let { YearMonth.from(it) == selectedMonth } == true
    }
    val monthTotalFare = monthRecords.sumOf { it.totalFare }
    val monthSettledFare = monthRecords.filter { it.isSettled }.sumOf { it.totalFare }
    val monthUnsettledFare = monthTotalFare - monthSettledFare
    val visibleRecords = selectedDate?.let { date ->
        monthRecords.filter { it.recordDateOrNull() == date }
    } ?: monthRecords

    PageHeader(
        title = "$name 손님",
        subtitle = "언제 카풀을 이용했고, 얼마가 부과됐는지 알 수 있어요."
    )

    MonthlySettlementSummary(
        month = selectedMonth.monthValue,
        totalFare = monthTotalFare,
        settledFare = monthSettledFare,
        unsettledFare = monthUnsettledFare
    )

    PassengerCalendarCard(
        selectedMonth = selectedMonth,
        records = passengerRecords,
        selectedDate = selectedDate,
        onPreviousMonth = { onChangeMonth(selectedMonth.minusMonths(1)) },
        onNextMonth = { onChangeMonth(selectedMonth.plusMonths(1)) },
        onMonthTitleClick = { monthPickerVisible = true },
        onSelectDate = { selectedDate = it }
    )

    OrangeButton(
        text = "전체 목록",
        modifier = Modifier.fillMaxWidth(),
        onClick = onGoFullHistory
    )
    GhostButton(text = "뒤로가기", modifier = Modifier.fillMaxWidth(), onClick = onGoBack)
    if (visibleRecords.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = G2RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Text(
                text = if (selectedDate == null) "이 달에는 이용 내역이 없습니다." else "이 날짜에는 이용 내역이 없습니다.",
                modifier = Modifier.padding(28.dp),
                color = GrayText,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    } else {
        visibleRecords.forEach { record ->
            PassengerRideRecordCard(record = record, onClick = { onSelectRecord(record) })
        }
    }
    Text(
        text = "최근 30일간의 이용 목록이 표시됩니다",
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp, bottom = 18.dp),
        color = GrayText,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center
    )

    if (monthPickerVisible) {
        MonthPickerDialog(
            currentMonth = selectedMonth,
            onDismiss = { monthPickerVisible = false },
            onSelectMonth = {
                selectedDate = null
                onChangeMonth(it)
                monthPickerVisible = false
            }
        )
    }
}

@Composable
private fun MonthlySettlementSummary(
    month: Int,
    totalFare: Int,
    settledFare: Int,
    unsettledFare: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = G2RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.96f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.46f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MonthlySettlementItem(
                title = "${month}월 누적금액",
                value = "${totalFare.formatWon()}원",
                modifier = Modifier.weight(1f)
            )
            MonthlySettlementItem(
                title = "정산금액",
                value = "${settledFare.formatWon()}원",
                valueColor = PrimaryBlue,
                modifier = Modifier.weight(1f)
            )
            MonthlySettlementItem(
                title = "정산되지 못한 금액",
                value = "${unsettledFare.formatWon()}원",
                valueColor = if (unsettledFare > 0) Color(0xFF424A56) else GrayText,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MonthlySettlementItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = DarkText
) {
    Column(
        modifier = modifier
            .background(Panel, G2RoundedCornerShape(18.dp))
            .padding(horizontal = 10.dp, vertical = 13.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Text(
            text = title,
            color = GrayText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        Text(
            text = value,
            color = valueColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            maxLines = 1
        )
    }
}

@Composable
private fun PassengerFullHistoryScreen(
    passengerName: String?,
    records: List<RideRecord>,
    onSelectRecord: (RideRecord) -> Unit,
    onGoBack: () -> Unit
) {
    val name = passengerName ?: return
    val passengerRecords = records
        .filter { it.passengerName.trim() == name }
        .sortedWith(compareByDescending<RideRecord> { it.recordDateOrNull() }.thenByDescending { it.endTime })
    val totalFare = passengerRecords.sumOf { it.totalFare }

    PageHeader(
        title = "$name 손님 전체 목록",
        subtitle = "이 손님의 모든 카풀 이용 기록입니다."
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = G2RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "$name 손님",
                color = DarkText,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryInfoCard(
                    title = "이용 횟수",
                    value = "${passengerRecords.size}회",
                    modifier = Modifier.weight(1f)
                )
                SummaryInfoCard(
                    title = "총 금액",
                    value = "${totalFare.formatWon()}원",
                    valueColor = PrimaryBlue,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    OrangeButton(text = "뒤로가기", modifier = Modifier.fillMaxWidth(), onClick = onGoBack)

    Text(
        text = "이용 목록",
        color = DarkText,
        fontSize = 24.sp,
        fontWeight = FontWeight.Black
    )
    if (passengerRecords.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = G2RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Text(
                text = "아직 이용 내역이 없습니다.",
                modifier = Modifier.padding(28.dp),
                color = GrayText,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    } else {
        passengerRecords.forEach { record ->
            PassengerRideRecordCard(record = record, onClick = { onSelectRecord(record) })
        }
    }
}

@Composable
private fun SummaryInfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = DarkText
) {
    Card(
        modifier = modifier.height(92.dp),
        shape = G2RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Panel),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, color = GrayText, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(text = value, color = valueColor, fontSize = 24.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun PassengerCalendarCard(
    selectedMonth: YearMonth,
    records: List<RideRecord>,
    selectedDate: LocalDate?,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onMonthTitleClick: () -> Unit,
    onSelectDate: (LocalDate) -> Unit
) {
    val recordDateCounts = records
        .mapNotNull { it.recordDateOrNull() }
        .groupingBy { it }
        .eachCount()
    val firstDay = selectedMonth.atDay(1)
    val leadingEmptyDays = firstDay.dayOfWeek.value % 7
    val days = List(leadingEmptyDays) { null } + (1..selectedMonth.lengthOfMonth()).map { selectedMonth.atDay(it) }
    val calendarCells = days + List((7 - days.size % 7) % 7) { null }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = G2RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GhostButton(text = "이전 달", modifier = Modifier.weight(1f), onClick = onPreviousMonth)
                Text(
                    text = selectedMonth.format(DateTimeFormatter.ofPattern("yyyy년 M월")),
                    modifier = Modifier
                        .weight(1.15f)
                        .clickable(onClick = onMonthTitleClick)
                        .background(Panel, G2RoundedCornerShape(999.dp))
                        .padding(vertical = 12.dp),
                    color = DarkText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )
                GhostButton(text = "다음 달", modifier = Modifier.weight(1f), onClick = onNextMonth)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("일", "월", "화", "수", "목", "금", "토").forEach { dayName ->
                    Text(
                        text = dayName,
                        modifier = Modifier.weight(1f),
                        color = GrayText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            calendarCells.chunked(7).forEach { week ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    week.forEach { date ->
                        val count = date?.let { recordDateCounts[it] } ?: 0
                        CalendarDayCell(
                            dayText = date?.dayOfMonth?.toString().orEmpty(),
                            count = count,
                            selected = date != null && date == selectedDate,
                            onClick = if (date != null && count > 0) {
                                { onSelectDate(date) }
                            } else {
                                null
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    dayText: String,
    count: Int,
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val active = count > 0
    val containerColor by animateColorAsState(
        targetValue = when {
            selected -> Color(0xFF003A8C)
            active -> Color(0xFF005BBF)
            else -> Panel
        },
        animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing),
        label = "calendarCellColor"
    )
    val borderColor by animateColorAsState(
        targetValue = if (active) Color(0xFF003A8C) else Line,
        animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing),
        label = "calendarCellBorder"
    )
    val textColor by animateColorAsState(
        targetValue = if (active) Color.White else DarkText,
        animationSpec = tween(durationMillis = 240, easing = FastOutSlowInEasing),
        label = "calendarCellText"
    )
    Column(
        modifier = modifier
            .height(58.dp)
            .background(
                containerColor,
                G2RoundedCornerShape(16.dp)
            )
            .border(1.dp, borderColor, G2RoundedCornerShape(16.dp))
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(vertical = 7.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = dayText,
            color = textColor,
            fontSize = 17.sp,
            fontWeight = if (active) FontWeight.Black else FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        if (active) {
            Text(
                text = "${count}회",
                color = Color.White.copy(alpha = 0.82f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun MonthPickerDialog(
    currentMonth: YearMonth,
    onDismiss: () -> Unit,
    onSelectMonth: (YearMonth) -> Unit
) {
    var pickerYear by remember(currentMonth) { mutableIntStateOf(currentMonth.year) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            shape = G2RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "년/월 선택", color = DarkText, fontSize = 30.sp, fontWeight = FontWeight.Black)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GhostButton(text = "이전 해", modifier = Modifier.weight(1f), onClick = { pickerYear -= 1 })
                    Text(
                        text = "${pickerYear}년",
                        modifier = Modifier
                            .weight(1f)
                            .height(62.dp)
                            .background(Panel, G2RoundedCornerShape(999.dp))
                            .padding(top = 14.dp),
                        color = DarkText,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )
                    GhostButton(text = "다음 해", modifier = Modifier.weight(1f), onClick = { pickerYear += 1 })
                }

                (1..12).chunked(4).forEach { rowMonths ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowMonths.forEach { month ->
                            val selected = pickerYear == currentMonth.year && month == currentMonth.monthValue
                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(58.dp),
                                onClick = { onSelectMonth(YearMonth.of(pickerYear, month)) },
                                shape = G2RoundedCornerShape(999.dp),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selected) PrimaryBlue else Panel,
                                    contentColor = if (selected) Color.White else DarkText
                                )
                            ) {
                                Text(text = "${month}월", fontSize = 18.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }

                GhostButton(text = "닫기", modifier = Modifier.fillMaxWidth(), onClick = onDismiss)
            }
        }
    }
}

@Composable
private fun PassengerRideRecordCard(record: RideRecord, onClick: () -> Unit) {
    val shape = G2RoundedCornerShape(22.dp)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(text = "${record.date} ${record.endTime}", color = DarkText, fontSize = 19.sp, fontWeight = FontWeight.Black)
                    Text(text = "${record.durationSeconds.formatDuration()} · ${"%.1f".format(record.distanceKm)}km", color = GrayText, fontSize = 15.sp)
                }
                Text(text = "${record.totalFare.formatWon()}원", color = PrimaryBlue, fontSize = 23.sp, fontWeight = FontWeight.Black)
            }
            if (record.isSettled) {
                SettledOverlay(modifier = Modifier.matchParentSize(), shape = shape)
            }
        }
    }
}

@Composable
private fun SettledOverlay(
    modifier: Modifier = Modifier,
    shape: Shape,
    fontSize: androidx.compose.ui.unit.TextUnit = 28.sp
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF6F7782).copy(alpha = 0.66f), shape)
            .border(1.dp, Color.White.copy(alpha = 0.32f), shape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "-정산됨-",
            color = Color.White,
            fontSize = fontSize,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PassengerReceiptBoardScreen(
    passengerName: String?,
    records: List<RideRecord>,
    selectedTheme: MeshTheme,
    onGoBack: () -> Unit,
    onShredRecord: (RideRecord) -> Unit,
    onSettleRecord: (RideRecord) -> Unit
) {
    val name = passengerName ?: return
    val passengerRecords = records
        .filter { it.passengerName.trim().ifBlank { "이름 없는" } == name }
        .sortedWith(compareByDescending<RideRecord> { it.recordDateOrNull() }.thenByDescending { it.endTime })
    val passengerTotalFare = passengerRecords.sumOf { it.totalFare }
    var isAligned by remember { mutableStateOf(false) }
    var detailState by remember { mutableStateOf<BoardDetailState?>(null) }
    var detailVisible by remember { mutableStateOf(false) }

    LaunchedEffect(detailVisible) {
        if (!detailVisible && detailState != null) {
            kotlinx.coroutines.delay(320L)
            detailState = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 18.dp, end = 18.dp, top = 44.dp, bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            PassengerReceiptBoardHeader(
                passengerName = name,
                totalFare = passengerTotalFare
            )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(G2RoundedCornerShape(34.dp))
                .background(Color.White.copy(alpha = 0.18f))
                .border(1.dp, Color.White.copy(alpha = 0.34f), G2RoundedCornerShape(34.dp))
        ) {
            val density = LocalDensity.current
            val scope = rememberCoroutineScope()
            val boardScrollState = rememberScrollState()
            val boardBounceOffset = remember { Animatable(0f) }
            val thumbnailWidth = 178.dp
            val thumbnailHeight = 280.dp
            val columnCount = when {
                maxWidth >= 900.dp -> 4
                maxWidth >= 620.dp -> 3
                else -> 2
            }
            val boardContentHeight = receiptBoardContentHeight(
                records = passengerRecords,
                columnCount = columnCount,
                thumbnailHeight = thumbnailHeight,
                minHeight = maxHeight
            )
            val boardWidthPx = with(density) { maxWidth.toPx() }
            val visibleBoardHeightPx = with(density) { maxHeight.toPx() }
            val contentHeightPx = with(density) { boardContentHeight.toPx() }
            val thumbnailWidthPx = with(density) { thumbnailWidth.toPx() }
            val thumbnailHeightPx = with(density) { thumbnailHeight.toPx() }
            val maxTravelX = (boardWidthPx - thumbnailWidthPx).coerceAtLeast(1f)
            val maxTravelY = (contentHeightPx - thumbnailHeightPx).coerceAtLeast(1f)
            val boardStates = remember(passengerRecords.joinToString(separator = "|") { it.id }, columnCount, boardContentHeight) {
                createReceiptBoardStates(
                    records = passengerRecords,
                    columnCount = columnCount,
                    contentHeight = boardContentHeight,
                    thumbnailHeight = thumbnailHeight
                )
            }
            val boardBounceConnection = remember(boardScrollState) {
                object : NestedScrollConnection {
                    override fun onPostScroll(
                        consumed: Offset,
                        available: Offset,
                        source: NestedScrollSource
                    ): Offset {
                        val pullingDownAtTop = !boardScrollState.canScrollBackward && available.y > 0f
                        val pullingUpAtBottom = !boardScrollState.canScrollForward && available.y < 0f

                        if (pullingDownAtTop || pullingUpAtBottom) {
                            scope.launch {
                                val next = boardBounceOffset.value + available.y * 0.20f
                                boardBounceOffset.snapTo(next)
                            }
                        }

                        return Offset.Zero
                    }

                    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                        boardBounceOffset.animateTo(
                            targetValue = 0f,
                            animationSpec = spring(
                                dampingRatio = 0.82f,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                        return Velocity.Zero
                    }
                }
            }

            if (passengerRecords.isEmpty()) {
                Text(
                    text = "아직 붙여둘 영수증이 없습니다.",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 32.dp),
                    color = GrayText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(boardBounceConnection)
                        .clipToBounds()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer { translationY = boardBounceOffset.value }
                            .verticalScroll(boardScrollState)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(boardContentHeight)
                        ) {
                            TimelineBackground(
                                states = boardStates,
                                maxHeight = boardContentHeight,
                                receiptHeight = thumbnailHeight
                            )

                            boardStates.forEachIndexed { index, state ->
                                DraggableReceiptThumbnail(
                                    state = state,
                                    selectedTheme = selectedTheme,
                                    index = index,
                                    thumbnailWidth = thumbnailWidth,
                                    thumbnailHeight = thumbnailHeight,
                                    maxTravelX = maxTravelX,
                                    maxTravelY = maxTravelY,
                                    visibleBoardHeight = visibleBoardHeightPx,
                                    scrollOffsetPx = boardScrollState.value.toFloat() - boardBounceOffset.value,
                                    isAligned = isAligned,
                                    columnCount = columnCount,
                                    onOpenDetail = { transformOrigin ->
                                        detailState = BoardDetailState(state.record, transformOrigin)
                                        detailVisible = true
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Button(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .height(52.dp)
                    .zIndex(40f),
                onClick = { isAligned = !isAligned },
                shape = G2RoundedCornerShape(999.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.88f),
                    contentColor = PrimaryBlue
                )
            ) {
                Text(text = if (isAligned) "흩뿌리기" else "정렬", fontSize = 17.sp, fontWeight = FontWeight.Black)
            }

            }

            GhostButton(text = "뒤로가기", modifier = Modifier.fillMaxWidth(), onClick = onGoBack)
        }

        AnimatedVisibility(
            visible = detailVisible && detailState != null,
            modifier = Modifier
                .fillMaxSize()
                .zIndex(80f),
            enter = fadeIn(animationSpec = tween(180)) + scaleIn(
                initialScale = 0.58f,
                transformOrigin = detailState?.transformOrigin ?: TransformOrigin.Center,
                animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
            ),
            exit = fadeOut(animationSpec = tween(180)) + scaleOut(
                targetScale = 0.58f,
                transformOrigin = detailState?.transformOrigin ?: TransformOrigin.Center,
                animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
            )
        ) {
            detailState?.record?.let { record ->
                BoardReceiptDetailOverlay(
                    record = record,
                    selectedTheme = record.receiptThemeOr(selectedTheme),
                    onDismiss = { detailVisible = false },
                    onSettle = {
                        onSettleRecord(record)
                        detailVisible = false
                    },
                    onShred = {
                        onShredRecord(record)
                        detailVisible = false
                    }
                )
            }
        }
    }
}

@Composable
private fun PassengerReceiptBoardHeader(
    passengerName: String,
    totalFare: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = G2RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = "$passengerName 손님", color = DarkText, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text(text = "날짜별 영수증을 자유롭게 배치해보세요.", color = GrayText, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "총 이용 금액", color = GrayText, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(text = "${totalFare.formatWon()}원", color = PrimaryBlue, fontSize = 25.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

private class ReceiptBoardItemState(
    val record: RideRecord,
    val date: LocalDate?,
    val lineRatio: Float,
    val minYRatio: Float,
    val maxYRatio: Float,
    val indexInDate: Int,
    val countInDate: Int,
    initialXRatio: Float,
    initialYRatio: Float,
    val rotation: Float
) {
    var xRatio by mutableFloatStateOf(initialXRatio)
    var yRatio by mutableFloatStateOf(initialYRatio)
}

private data class BoardDetailState(
    val record: RideRecord,
    val transformOrigin: TransformOrigin
)

private fun receiptBoardContentHeight(
    records: List<RideRecord>,
    columnCount: Int,
    thumbnailHeight: Dp,
    minHeight: Dp
): Dp {
    if (records.isEmpty()) return minHeight

    val columns = columnCount.coerceAtLeast(1)
    val dateGroups = records.groupBy { it.recordDateOrNull() }
    val totalRows = dateGroups.values.sumOf { recordsInDate ->
        ((recordsInDate.size + columns - 1) / columns).coerceAtLeast(1)
    }
    val dateCount = dateGroups.size.coerceAtLeast(1)
    val rowStep = thumbnailHeight * 0.58f
    val contentHeight = 44.dp +
        (dateCount * 188).dp +
        (thumbnailHeight * dateCount) +
        (rowStep * (totalRows - dateCount).coerceAtLeast(0)) +
        76.dp

    return maxOf(minHeight, contentHeight)
}

private fun createReceiptBoardStates(
    records: List<RideRecord>,
    columnCount: Int,
    contentHeight: Dp,
    thumbnailHeight: Dp
): List<ReceiptBoardItemState> {
    val recordsByDate = records.groupBy { it.recordDateOrNull() }
    val dateOrder = recordsByDate.keys.toList()
    val groupedIndex = mutableMapOf<LocalDate?, Int>()
    val columns = columnCount.coerceAtLeast(1)
    val dateRows = dateOrder.associateWith { date ->
        val count = recordsByDate[date]?.size ?: 0
        ((count + columns - 1) / columns).coerceAtLeast(1)
    }
    val totalRows = dateRows.values.sum().coerceAtLeast(1)
    val contentHeightValue = contentHeight.value.coerceAtLeast(1f)
    val thumbnailHeightValue = thumbnailHeight.value
    val rowStep = (thumbnailHeightValue * 0.58f).coerceAtLeast(130f)
    val dateHeaderHeight = 58f
    val sectionBottomPadding = 72f
    val requiredHeight = dateOrder.sumOf { date ->
        val rows = dateRows[date] ?: 1
        (dateHeaderHeight + thumbnailHeightValue + rowStep * (rows - 1) + sectionBottomPadding).toDouble()
    }.toFloat()
    val scale = if (requiredHeight > contentHeightValue) {
        contentHeightValue / requiredHeight
    } else {
        1f
    }
    val scaledRowStep = rowStep * scale
    val scaledHeaderHeight = dateHeaderHeight * scale
    val scaledBottomPadding = sectionBottomPadding * scale
    var sectionTop = 0f
    val sectionBounds = dateOrder.associateWith { date ->
        val rows = dateRows[date] ?: 1
        val lineY = sectionTop + 28f * scale
        val minY = lineY + scaledHeaderHeight
        val maxY = minY + scaledRowStep * (rows - 1)
        val sectionHeight = scaledHeaderHeight + thumbnailHeightValue * scale + scaledRowStep * (rows - 1) + scaledBottomPadding
        sectionTop += sectionHeight
        Triple(
            (lineY / contentHeightValue).coerceIn(0f, 1f),
            (minY / contentHeightValue).coerceIn(0f, 1f),
            (maxY / contentHeightValue).coerceIn(0f, 1f)
        )
    }

    return records.mapIndexed { index, record ->
        val date = record.recordDateOrNull()
        val indexInDate = groupedIndex.getOrDefault(date, 0)
        groupedIndex[date] = indexInDate + 1
        val countInDate = recordsByDate[date]?.size ?: 1
        val (lineRatio, minYRatio, maxYRatio) = sectionBounds[date] ?: Triple(0.04f, 0.12f, 0.9f)
        val seed = record.id.hashCode() + index * 31
        val verticalWindow = (maxYRatio - minYRatio).coerceAtLeast(0.01f)
        val columnsInDate = minOf(columns, countInDate).coerceAtLeast(1)
        val rowsInDate = ((countInDate + columnsInDate - 1) / columnsInDate).coerceAtLeast(1)
        val row = indexInDate / columnsInDate
        val col = indexInDate % columnsInDate
        val xBase = if (columnsInDate == 1) {
            0.5f
        } else {
            0.07f + (col.toFloat() / (columnsInDate - 1).toFloat()) * 0.86f
        }
        val xJitter = (seededRatio(seed, 19) - 0.5f) * 0.20f
        val rowProgress = if (rowsInDate <= 1) {
            0f
        } else {
            row.toFloat() / (rowsInDate - 1).toFloat()
        }
        val organicJitter = (seededRatio(seed, 41) - 0.5f) * if (rowsInDate <= 1) 0.08f else 0.14f
        val yProgress = (rowProgress + organicJitter).coerceIn(0f, 1f)

        ReceiptBoardItemState(
            record = record,
            date = date,
            lineRatio = lineRatio,
            minYRatio = minYRatio,
            maxYRatio = maxYRatio,
            indexInDate = indexInDate,
            countInDate = countInDate,
            initialXRatio = (xBase + xJitter).coerceIn(0.02f, 0.98f),
            initialYRatio = (minYRatio + yProgress * verticalWindow).coerceIn(minYRatio, maxYRatio),
            rotation = -15f + seededRatio(seed, 73) * 30f
        )
    }
}

private fun seededRatio(seed: Int, salt: Int): Float {
    val value = abs(sin((seed + salt) * 12.9898) * 43758.5453)
    return (value - floor(value)).toFloat()
}

private fun rubberBandOffset(overflow: Float): Float {
    if (overflow == 0f) return 0f
    val distance = abs(overflow)
    val resistance = 1f - (1f / (distance / 180f + 1f))
    return sign(overflow) * 112f * resistance
}

@Composable
private fun TimelineBackground(
    states: List<ReceiptBoardItemState>,
    maxHeight: Dp,
    receiptHeight: Dp
) {
    val anchors = states
        .groupBy { it.date }
        .map { (date, dateStates) -> date to dateStates.first().lineRatio }
        .sortedBy { it.second }

    Box(modifier = Modifier.fillMaxSize()) {
        anchors.forEach { (date, ratio) ->
            val y = (maxHeight - receiptHeight) * ratio.coerceIn(0f, 1f) + 18.dp
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = y)
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = date?.let { "${it.monthValue}월 ${it.dayOfMonth}일" } ?: "날짜 없음",
                    color = DarkText.copy(alpha = 0.48f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.62f))
                )
            }
        }
    }
}

@Composable
private fun BoardReceiptDetailOverlay(
    record: RideRecord,
    selectedTheme: MeshTheme,
    onDismiss: () -> Unit,
    onSettle: () -> Unit,
    onShred: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.18f))
            .padding(start = 18.dp, end = 18.dp, top = 44.dp, bottom = 18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ReceiptDetailPaper(
                record = record,
                selectedTheme = selectedTheme,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReceiptGhostButton(
                    text = "뒤로가기",
                    modifier = Modifier.weight(1f),
                    onClick = onDismiss
                )
                ReceiptPrimaryButton(
                    text = "정산하기",
                    modifier = Modifier.weight(1f),
                    onClick = onSettle
                )
                ReceiptShredIconButton(
                    modifier = Modifier.width(116.dp),
                    onClick = onShred
                )
            }
        }
    }
}

@Composable
private fun ReceiptDetailPaper(
    record: RideRecord,
    selectedTheme: MeshTheme,
    modifier: Modifier = Modifier
) {
    val isNightReceipt = selectedTheme == MeshTheme.Night
    val textColor = if (isNightReceipt) Color.White else DarkText
    val mutedColor = if (isNightReceipt) Color.White.copy(alpha = 0.82f) else GrayText
    val strongLineColor = if (isNightReceipt) Color.White.copy(alpha = 0.72f) else Color(0xFF30343B)
    val softLineColor = if (isNightReceipt) Color.White.copy(alpha = 0.42f) else Color(0xFFB8B8B8)
    val amountColor = if (isNightReceipt) Color(0xFF55B8FF) else PrimaryBlue

    Box(
        modifier = modifier
            .clip(ReceiptPaperShape())
            .border(1.dp, Color.White.copy(alpha = 0.45f), ReceiptPaperShape())
    ) {
        Image(
            painter = painterResource(id = selectedTheme.drawableRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        if (isNightReceipt) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.28f))
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 34.dp, end = 34.dp, top = 24.dp, bottom = 86.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "카풀 영수증", color = textColor, fontSize = 30.sp, fontWeight = FontWeight.Black)
                DashedLine(color = strongLineColor, thickness = 2)
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = "${record.passengerName} 손님,", color = textColor, fontSize = 36.sp, fontWeight = FontWeight.Black)
                Text(text = "카풀미터기를 이용해주셔서 감사합니다", color = mutedColor, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                ThickLine(color = strongLineColor)
            }
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ReceiptRow("탑승일자", record.date, textColor = textColor, valueColor = textColor, amountColor = amountColor)
                ReceiptRow("하차시각", record.endTime, textColor = textColor, valueColor = textColor, amountColor = amountColor)
                DashedLine(color = softLineColor)
                ReceiptRow("총 주행 시간", record.durationSeconds.formatDuration(), textColor = textColor, valueColor = textColor, amountColor = amountColor)
                ReceiptRow("총 주행 거리", "%.1fkm".format(record.distanceKm), textColor = textColor, valueColor = textColor, amountColor = amountColor)
                ReceiptRow("운행요금", "${record.rideFare.formatWon()}원", textColor = textColor, valueColor = textColor, amountColor = amountColor)
                DashedLine(color = softLineColor)
                ReceiptRow("통행요금", "${record.tollFare.formatWon()}원", textColor = textColor, valueColor = textColor, amountColor = amountColor)
                ReceiptRow("추가요금", "${record.extraFare.formatWon()}원", textColor = textColor, valueColor = textColor, amountColor = amountColor)
            }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                DashedLine(color = strongLineColor, thickness = 2)
                Text(text = "총 운행요금", color = mutedColor, fontSize = 22.sp, fontWeight = FontWeight.Black)
                Text(
                    text = "${record.totalFare.formatWon()}원",
                    modifier = Modifier.fillMaxWidth(),
                    color = amountColor,
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.End
                )
            }
        }
        if (record.isSettled) {
            SettledOverlay(shape = ReceiptPaperShape(), fontSize = 34.sp)
        }
    }
}

@Composable
private fun DraggableReceiptThumbnail(
    state: ReceiptBoardItemState,
    selectedTheme: MeshTheme,
    index: Int,
    thumbnailWidth: Dp,
    thumbnailHeight: Dp,
    maxTravelX: Float,
    maxTravelY: Float,
    visibleBoardHeight: Float,
    scrollOffsetPx: Float,
    isAligned: Boolean,
    columnCount: Int,
    onOpenDetail: (TransformOrigin) -> Unit
) {
    val receiptTheme = state.record.receiptThemeOr(selectedTheme)
    val density = LocalDensity.current
    val thumbnailWidthPx = with(density) { thumbnailWidth.toPx() }
    val thumbnailHeightPx = with(density) { thumbnailHeight.toPx() }
    var appeared by remember(state.record.id) { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay((index * 55L).coerceAtMost(360L))
        appeared = true
    }
    val columnsInDate = minOf(columnCount, state.countInDate).coerceAtLeast(1)
    val rowsInDate = ((state.countInDate + columnsInDate - 1) / columnsInDate).coerceAtLeast(1)
    val row = state.indexInDate / columnsInDate
    val rowStartIndex = row * columnsInDate
    val itemsInRow = (state.countInDate - rowStartIndex).coerceIn(1, columnsInDate)
    val colInRow = state.indexInDate - rowStartIndex
    val alignedXRatio = if (itemsInRow == 1) {
        0.5f
    } else {
        val gridStep = if (columnsInDate == 1) 0f else 0.88f / (columnsInDate - 1).toFloat()
        val occupiedWidth = gridStep * (itemsInRow - 1).toFloat()
        0.5f - occupiedWidth / 2f + colInRow * gridStep
    }
    val alignedYRatio = if (rowsInDate == 1) {
        state.minYRatio
    } else {
        state.minYRatio + (row.toFloat() / (rowsInDate - 1).toFloat()) * (state.maxYRatio - state.minYRatio)
    }
    val alignedX = maxTravelX * alignedXRatio.coerceIn(0f, 1f)
    val alignedY = maxTravelY * alignedYRatio.coerceIn(state.minYRatio, state.maxYRatio)
    val freeOffset = Offset(maxTravelX * state.xRatio, maxTravelY * state.yRatio)
    val targetOffset = if (isAligned) Offset(alignedX, alignedY) else freeOffset
    val animatedOffset by animateOffsetAsState(
        targetValue = targetOffset,
        animationSpec = spring(dampingRatio = 0.82f, stiffness = Spring.StiffnessLow),
        label = "receiptBoardOffset"
    )
    val animatedRotation by animateFloatAsState(
        targetValue = if (isAligned) 0f else state.rotation,
        animationSpec = spring(dampingRatio = 0.82f, stiffness = Spring.StiffnessLow),
        label = "receiptBoardRotation"
    )
    val appearScale by animateFloatAsState(
        targetValue = if (appeared) 1f else 0.84f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow),
        label = "receiptBoardAppearScale"
    )
    val appearAlpha by animateFloatAsState(
        targetValue = if (appeared) 1f else 0f,
        animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing),
        label = "receiptBoardAppearAlpha"
    )
    var rubberX by remember(state.record.id) { mutableFloatStateOf(0f) }
    var rubberY by remember(state.record.id) { mutableFloatStateOf(0f) }
    val animatedRubberX by animateFloatAsState(
        targetValue = rubberX,
        animationSpec = spring(dampingRatio = 0.68f, stiffness = Spring.StiffnessLow),
        label = "receiptDragRubberX"
    )
    val animatedRubberY by animateFloatAsState(
        targetValue = rubberY,
        animationSpec = spring(dampingRatio = 0.68f, stiffness = Spring.StiffnessLow),
        label = "receiptDragRubberY"
    )
    val thumbnailInteractionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    (animatedOffset.x + animatedRubberX).roundToInt(),
                    (animatedOffset.y + animatedRubberY).roundToInt()
                )
            }
            .size(width = thumbnailWidth, height = thumbnailHeight)
            .clickable(
                interactionSource = thumbnailInteractionSource,
                indication = null
            ) {
                    val boardWidth = maxTravelX + thumbnailWidthPx
                    onOpenDetail(
                        TransformOrigin(
                            pivotFractionX = ((animatedOffset.x + thumbnailWidthPx / 2f) / boardWidth).coerceIn(0f, 1f),
                            pivotFractionY = ((animatedOffset.y - scrollOffsetPx + thumbnailHeightPx / 2f) / visibleBoardHeight).coerceIn(0f, 1f)
                        )
                    )
            }
            .graphicsLayer {
                rotationZ = animatedRotation
                scaleX = appearScale
                scaleY = appearScale
                alpha = appearAlpha
                shadowElevation = 18f
                shape = ReceiptPaperShape()
                clip = false
            }
            .pointerInput(state.record.id, isAligned, maxTravelX, maxTravelY) {
                detectDragGestures(
                    onDragStart = {
                        rubberX = 0f
                        rubberY = 0f
                    },
                    onDragEnd = {
                        rubberX = 0f
                        rubberY = 0f
                    },
                    onDragCancel = {
                        rubberX = 0f
                        rubberY = 0f
                    }
                ) { change, dragAmount ->
                    change.consume()
                    if (!isAligned) {
                        val currentX = state.xRatio * maxTravelX
                        val currentY = state.yRatio * maxTravelY
                        val proposedX = currentX + dragAmount.x + rubberX
                        val proposedY = currentY + dragAmount.y + rubberY
                        val clampedX = proposedX.coerceIn(0f, maxTravelX)
                        val clampedY = proposedY.coerceIn(0f, maxTravelY)

                        state.xRatio = (clampedX / maxTravelX).coerceIn(0f, 1f)
                        state.yRatio = (clampedY / maxTravelY).coerceIn(0f, 1f)
                        rubberX = rubberBandOffset(proposedX - clampedX)
                        rubberY = rubberBandOffset(proposedY - clampedY)
                    }
                }
            }
    ) {
        ReceiptThumbnailPaper(record = state.record, selectedTheme = receiptTheme)
    }
}

@Composable
private fun ReceiptThumbnailPaper(record: RideRecord, selectedTheme: MeshTheme) {
    val isNightReceipt = selectedTheme == MeshTheme.Night
    val textColor = if (isNightReceipt) Color.White else DarkText
    val mutedColor = if (isNightReceipt) Color.White.copy(alpha = 0.82f) else GrayText
    val lineColor = if (isNightReceipt) Color.White.copy(alpha = 0.54f) else Color(0xFF30343B)
    val amountColor = if (isNightReceipt) Color(0xFF55B8FF) else PrimaryBlue

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(ReceiptPaperShape())
            .border(1.dp, Color.White.copy(alpha = 0.45f), ReceiptPaperShape())
    ) {
        Image(
            painter = painterResource(id = selectedTheme.drawableRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        if (isNightReceipt) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.28f))
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 44.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "카풀 영수증", color = textColor, fontSize = 17.sp, fontWeight = FontWeight.Black)
                DashedLine(color = lineColor, thickness = 1)
            }

            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(text = "${record.passengerName} 손님,", color = textColor, fontSize = 20.sp, fontWeight = FontWeight.Black)
                Text(text = "${record.date}  ${record.endTime}", color = mutedColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                MiniReceiptRow("주행", "%.1fkm".format(record.distanceKm), textColor)
                MiniReceiptRow("시간", record.durationSeconds.formatDuration(), textColor)
                MiniReceiptRow("운행", "${record.rideFare.formatWon()}원", amountColor)
            }

            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "총 운행요금", color = mutedColor, fontSize = 12.sp, fontWeight = FontWeight.Black)
                Text(
                    text = "${record.totalFare.formatWon()}원",
                    modifier = Modifier.fillMaxWidth(),
                    color = amountColor,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.End
                )
            }
        }
        if (record.isSettled) {
            SettledOverlay(shape = ReceiptPaperShape(), fontSize = 22.sp)
        }
    }
}

@Composable
private fun MiniReceiptRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(text = value, color = color, fontSize = 13.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun ReceiptScreen(
    record: RideRecord?,
    selectedTheme: MeshTheme,
    onGoHistory: () -> Unit,
    onGoBack: () -> Unit,
    onGoHome: () -> Unit,
    onShredRecord: (RideRecord) -> Unit,
    onSettleRecord: (RideRecord) -> Unit
) {
    val item = record
    if (item == null) {
        Text(text = "선택된 내역이 없습니다.", color = DarkText, fontSize = 20.sp)
        OrangeButton(text = "홈으로", modifier = Modifier.fillMaxWidth(), onClick = onGoHome)
        return
    }
    val receiptTheme = item.receiptThemeOr(selectedTheme)
    val isNightReceipt = receiptTheme == MeshTheme.Night
    val receiptTextColor = if (isNightReceipt) Color.White else DarkText
    val receiptMutedColor = if (isNightReceipt) Color.White.copy(alpha = 0.82f) else GrayText
    val receiptStrongLineColor = if (isNightReceipt) Color.White.copy(alpha = 0.72f) else Color(0xFF30343B)
    val receiptSoftLineColor = if (isNightReceipt) Color.White.copy(alpha = 0.42f) else Color(0xFFB8B8B8)
    val receiptAmountColor = if (isNightReceipt) Color(0xFF55B8FF) else PrimaryBlue
    val context = LocalContext.current
    val soundScope = rememberCoroutineScope()
    var receiptVisible by remember(item.id) { mutableStateOf(false) }
    var printSoundId by remember(item.id) { mutableIntStateOf(0) }
    var tearSoundId by remember(item.id) { mutableIntStateOf(0) }
    var papercutSoundId by remember(item.id) { mutableIntStateOf(0) }
    var loadedSoundCount by remember(item.id) { mutableIntStateOf(0) }
    var papercutSoundReady by remember(item.id) { mutableStateOf(false) }
    var papercutPlayRequest by remember(item.id) { mutableIntStateOf(0) }
    var playedReceiptSounds by remember(item.id) { mutableStateOf(false) }
    val shredScope = rememberCoroutineScope()
    val shredProgress = remember(item.id) { Animatable(0f) }
    var shredderVisible by remember(item.id) { mutableStateOf(false) }
    var shredPreparing by remember(item.id) { mutableStateOf(false) }
    var shredding by remember(item.id) { mutableStateOf(false) }
    var receiptBackVisible by remember(item.id) { mutableStateOf(false) }
    var shredSuccessVisible by remember(item.id) { mutableStateOf(false) }
    val receiptSoundPool = remember(item.id) {
        SoundPool.Builder()
            .setMaxStreams(3)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .build()
    }

    DisposableEffect(receiptSoundPool, context) {
        receiptSoundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) {
                soundScope.launch {
                    loadedSoundCount += 1
                    if (sampleId == papercutSoundId) {
                        papercutSoundReady = true
                    }
                }
            }
        }
        printSoundId = receiptSoundPool.load(context, R.raw.print_sound, 1)
        tearSoundId = receiptSoundPool.load(context, R.raw.tear_sound, 1)
        papercutSoundId = receiptSoundPool.load(context, R.raw.papercut, 1)

        onDispose {
            receiptSoundPool.release()
        }
    }

    LaunchedEffect(item.id, loadedSoundCount, printSoundId, tearSoundId) {
        if (!playedReceiptSounds && loadedSoundCount >= 2 && printSoundId != 0 && tearSoundId != 0) {
            playedReceiptSounds = true
            receiptVisible = true
            receiptSoundPool.play(printSoundId, 1f, 1f, 1, 0, 1f)
            kotlinx.coroutines.delay(1500L)
            receiptSoundPool.play(tearSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    LaunchedEffect(papercutPlayRequest, papercutSoundReady, papercutSoundId) {
        if (papercutPlayRequest > 0 && papercutSoundReady && papercutSoundId != 0) {
            receiptSoundPool.play(papercutSoundId, 1f, 1f, 1, 0, 0.8f)
        }
    }

    LaunchedEffect(item.id) {
        kotlinx.coroutines.delay(900L)
        if (!receiptVisible) {
            receiptVisible = true
        }
    }

    @Composable
    fun ReceiptPane() {
        val density = LocalDensity.current
        val receiptRotation by animateFloatAsState(
            targetValue = if (receiptBackVisible) 180f else 0f,
            animationSpec = tween(durationMillis = 520, easing = FastOutSlowInEasing),
            label = "receiptFlip"
        )
        val showingBack = receiptRotation > 90f

        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            val shredderWidth = (maxWidth - 16.dp).coerceAtLeast(280.dp)
            val receiptContentWidth = shredderWidth * 0.90f

            AnimatedVisibility(
                visible = receiptVisible,
                enter = slideInVertically(
                    animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
                    initialOffsetY = { -it - 260 }
                ) + fadeIn(animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing)),
                exit = slideOutVertically(
                    animationSpec = tween(durationMillis = 360, easing = FastOutSlowInEasing),
                    targetOffsetY = { -it / 3 }
                ) + fadeOut(animationSpec = tween(durationMillis = 240, easing = FastOutSlowInEasing))
            ) {
                Box(
                    modifier = Modifier
                        .width(receiptContentWidth)
                        .height(1036.dp)
                        .graphicsLayer {
                            rotationY = receiptRotation
                            cameraDistance = 18f * density.density
                        }
                ) {
                    if (showingBack) {
                        ReceiptBackPaper(
                            selectedTheme = receiptTheme,
                            isNightReceipt = isNightReceipt,
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { rotationY = 180f }
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(ReceiptPaperShape())
                        ) {
                            Image(
                                painter = painterResource(id = receiptTheme.drawableRes),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.White.copy(alpha = receiptTheme.receiptPaperOverlayAlpha()))
                            )
                            if (isNightReceipt) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.28f))
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 36.dp, end = 36.dp, top = 24.dp, bottom = 86.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    GlassIconContainer(symbol = "♞", size = 52.dp, fontSize = 28.sp)
                                    Text(text = "카풀 영수증", color = receiptTextColor, fontSize = 34.sp, fontWeight = FontWeight.Black)
                                    DashedLine(color = receiptStrongLineColor, thickness = 2)
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text(text = "${item.passengerName} 손님,", color = receiptTextColor, fontSize = 40.sp, fontWeight = FontWeight.Black)
                                    Text(text = "카풀미터기를 이용해주셔서 감사합니다", color = receiptMutedColor, fontSize = 20.sp, fontWeight = FontWeight.Medium)
                                    ThickLine(color = receiptStrongLineColor)
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                                    ReceiptRow("탑승일자", item.date, textColor = receiptTextColor, valueColor = receiptTextColor, amountColor = receiptAmountColor)
                                    ReceiptRow("하차시각", item.endTime, textColor = receiptTextColor, valueColor = receiptTextColor, amountColor = receiptAmountColor)
                                    DashedLine(color = receiptSoftLineColor)
                                    ReceiptRow("총 주행 시간", item.durationSeconds.formatDuration(), textColor = receiptTextColor, valueColor = receiptTextColor, amountColor = receiptAmountColor)
                                    ReceiptRow("총 주행 거리", "%.1fkm".format(item.distanceKm), textColor = receiptTextColor, valueColor = receiptTextColor, amountColor = receiptAmountColor)
                                    ReceiptRow("운행요금", "${item.rideFare.formatWon()}원", textColor = receiptTextColor, valueColor = receiptTextColor, amountColor = receiptAmountColor)
                                    DashedLine(color = receiptSoftLineColor)
                                    ReceiptRow("통행요금", "${item.tollFare.formatWon()}원", textColor = receiptTextColor, valueColor = receiptTextColor, amountColor = receiptAmountColor)
                                    ReceiptRow("추가요금", "${item.extraFare.formatWon()}원", textColor = receiptTextColor, valueColor = receiptTextColor, amountColor = receiptAmountColor)
                                }

                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    DashedLine(color = receiptStrongLineColor, thickness = 2)
                                    Text(text = "총 운행요금", color = receiptMutedColor, fontSize = 24.sp, fontWeight = FontWeight.Black)
                                    Text(text = "${item.totalFare.formatWon()}원", modifier = Modifier.fillMaxWidth(), color = receiptAmountColor, fontSize = 76.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.End)
                                }
                            }
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val toothWidth = 26f
                                val cutDepth = 17f
                                val paperBottom = size.height - cutDepth - 9f
                                drawPath(
                                    path = pinkingEdgePath(size, toothWidth, cutDepth, paperBottom),
                                    color = Color(0x664A5364),
                                    style = Stroke(width = 2f, cap = StrokeCap.Round)
                                )
                            }
                            ShreddedReceiptOverlay(progress = shredProgress.value)
                            if (item.isSettled) {
                                SettledOverlay(shape = ReceiptPaperShape(), fontSize = 44.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ReceiptActions(modifier: Modifier = Modifier) {
        if (!shredderVisible && !shredPreparing && !shredding && !shredSuccessVisible) {
            if (receiptVisible) {
                Row(
                    modifier = modifier,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ReceiptGhostButton(text = "장부에 저장", modifier = Modifier.weight(1f), onClick = onGoBack)
                    ReceiptPrimaryButton(
                        text = "지금 정산하기",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onSettleRecord(item)
                            onGoBack()
                        }
                    )
                    ReceiptShredIconButton(
                        modifier = Modifier.width(116.dp),
                        onClick = {
                            if (!shredSuccessVisible) {
                                receiptBackVisible = true
                                shredderVisible = true
                            }
                        }
                    )
                }
            } else {
                ReceiptPrimaryButton(text = "장부에 저장", modifier = modifier, onClick = onGoBack)
            }
        }
    }

    @Composable
    fun ShredderLayer(
        shredderModifier: Modifier,
        toastModifier: Modifier
    ) {
        ShredderSheet(
            visible = shredderVisible || shredding,
            progress = shredProgress.value,
            shredding = shredding,
            onShred = {
                if (!shredding && !shredPreparing) {
                    shredPreparing = true
                    shredderVisible = false
                    receiptBackVisible = false
                    shredScope.launch {
                        kotlinx.coroutines.delay(560L)
                        shredding = true
                        papercutPlayRequest += 1
                        shredProgress.snapTo(0f)
                        kotlinx.coroutines.delay(260L)
                        var progress = 0f
                        while (progress < 1f) {
                            kotlinx.coroutines.delay(Random.nextLong(50L, 151L))
                            progress = (progress + Random.nextFloat() * 0.075f + 0.035f).coerceAtMost(1f)
                            shredProgress.animateTo(
                                targetValue = progress,
                                animationSpec = tween(
                                    durationMillis = Random.nextInt(45, 105),
                                    easing = FastOutSlowInEasing
                                )
                            )
                        }
                        kotlinx.coroutines.delay(120L)
                        receiptVisible = false
                        kotlinx.coroutines.delay(360L)
                        shredderVisible = false
                        shredding = false
                        shredPreparing = false
                        onShredRecord(item)
                        shredSuccessVisible = true
                        kotlinx.coroutines.delay(900L)
                        onGoBack()
                        kotlinx.coroutines.delay(300L)
                        shredSuccessVisible = false
                    }
                }
            },
            onCancel = {
                if (!shredding && !shredPreparing) {
                    shredderVisible = false
                    receiptBackVisible = false
                }
            },
            modifier = shredderModifier
        )

        ShredSuccessToast(
            visible = shredSuccessVisible,
            modifier = toastModifier
        )
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ReceiptPane()
            ReceiptActions(modifier = Modifier.fillMaxWidth())
        }

        ShredderLayer(
            shredderModifier = Modifier
                .align(Alignment.BottomCenter)
                .zIndex(8f),
            toastModifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 96.dp)
                .zIndex(9f)
        )
    }
}

@Composable
private fun ReceiptBackPaper(
    selectedTheme: MeshTheme,
    isNightReceipt: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(ReceiptPaperShape())
            .border(1.dp, Color.White.copy(alpha = 0.45f), ReceiptPaperShape())
    ) {
        Image(
            painter = painterResource(id = selectedTheme.drawableRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = if (isNightReceipt) 0.08f else 0.18f))
        )
        if (isNightReceipt) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.28f))
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 38.dp, end = 38.dp, top = 34.dp, bottom = 110.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 44.dp)
                    .background(Color.White.copy(alpha = 0.10f), G2RoundedCornerShape(28.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.22f), G2RoundedCornerShape(28.dp))
            )
            DashedLine(
                color = if (isNightReceipt) Color.White.copy(alpha = 0.42f) else Color(0x5530343B),
                thickness = 2
            )
        }
        Canvas(modifier = Modifier.fillMaxSize()) {
            val toothWidth = 26f
            val cutDepth = 17f
            val paperBottom = size.height - cutDepth - 9f
            drawPath(
                path = pinkingEdgePath(size, toothWidth, cutDepth, paperBottom),
                color = Color(0x664A5364),
                style = Stroke(width = 2f, cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
private fun ShreddedReceiptOverlay(progress: Float) {
    if (progress <= 0.01f) return

    Canvas(modifier = Modifier.fillMaxSize()) {
        val clippedProgress = progress.coerceIn(0f, 1f)
        val mouthY = size.height * (1f - clippedProgress)
        val stripCount = 14
        val stripWidth = size.width / stripCount

        drawRect(
            color = Color.Black.copy(alpha = 0.10f * clippedProgress),
            topLeft = Offset(0f, (mouthY - 7f).coerceAtLeast(0f)),
            size = Size(size.width, 14f)
        )

        repeat(stripCount) { index ->
            val x = index * stripWidth
            val sway = sin(index * 1.73f + clippedProgress * 14f) * 9f
            val lengthNoise = abs(sin(index * 2.11f)) * 30f
            val startY = (mouthY + 10f + lengthNoise).coerceAtMost(size.height)

            drawRect(
                color = Color.White.copy(alpha = 0.09f + clippedProgress * 0.10f),
                topLeft = Offset(x + 2f, startY),
                size = Size((stripWidth - 4f).coerceAtLeast(1f), (size.height - startY).coerceAtLeast(0f))
            )

            if (index > 0) {
                drawLine(
                    color = Color.White.copy(alpha = 0.74f),
                    start = Offset(x + sway * 0.15f, startY),
                    end = Offset(x + sway, size.height),
                    strokeWidth = 4.5f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color(0x554A5364),
                    start = Offset(x + 3f + sway * 0.15f, startY + 10f),
                    end = Offset(x + 3f + sway, size.height),
                    strokeWidth = 1.4f,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

@Composable
private fun ReceiptShredIconButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val shape = G2RoundedCornerShape(999.dp)
    Button(
        modifier = modifier.height(74.dp),
        onClick = onClick,
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1D1D1F),
            contentColor = Color.White
        )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Text(text = "▥", fontSize = 28.sp, fontWeight = FontWeight.Black, lineHeight = 28.sp)
            Text(text = "파쇄", fontSize = 16.sp, fontWeight = FontWeight.Black, lineHeight = 16.sp)
        }
    }
}

@Composable
private fun ShredderSheet(
    visible: Boolean,
    progress: Float,
    shredding: Boolean,
    onShred: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier.fillMaxWidth(),
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 430, easing = FastOutSlowInEasing),
            initialOffsetY = { it + 120 }
        ) + fadeIn(animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing)),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
            targetOffsetY = { it + 80 }
        ) + fadeOut(animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing))
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            val sheetWidth = (maxWidth * 1.08f).coerceAtLeast(360.dp).coerceAtMost(820.dp)
            val sheetHeight = sheetWidth * 0.43f
            val imageHeight = sheetWidth * 0.30f
            val restingOffset = 42.dp
            val riseOffset = -(sheetHeight + 150.dp) * progress.coerceIn(0f, 1f)
            val compact = sheetWidth < 420.dp
            val buttonStackWidth = (sheetWidth * if (compact) 0.58f else 0.60f)
                .coerceAtLeast(if (compact) 280.dp else 360.dp)
                .coerceAtMost(sheetWidth * 0.70f)
            val buttonHeight = if (compact) 48.dp else 58.dp
            val buttonGap = if (compact) 10.dp else 12.dp

            Box(
                modifier = Modifier
                    .width(sheetWidth)
                    .offset(y = restingOffset + if (shredding) riseOffset else 0.dp)
                    .height(sheetHeight)
                    .clipToBounds()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.papercut7_visible),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .width(sheetWidth)
                        .height(imageHeight),
                    contentScale = ContentScale.FillBounds
                )

                if (!shredding) {
                    Text(
                        text = "영수증을 파쇄하시겠습니까?",
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 8.dp),
                        color = Color(0xFF24313D),
                        fontSize = if (compact) 23.sp else 28.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        lineHeight = if (compact) 28.sp else 34.sp,
                        maxLines = 1
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(y = -(sheetHeight * 0.03f))
                            .width(buttonStackWidth),
                        verticalArrangement = Arrangement.spacedBy(buttonGap),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(buttonHeight),
                            onClick = onShred,
                            enabled = !shredding,
                            shape = G2RoundedCornerShape(17.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE8473F),
                                contentColor = Color.White,
                                disabledContainerColor = Color(0xFFE8473F).copy(alpha = 0.55f),
                                disabledContentColor = Color.White
                            )
                        ) {
                            Text(text = "파쇄하기", fontSize = if (compact) 18.sp else 22.sp, fontWeight = FontWeight.Black)
                        }
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(buttonHeight),
                            onClick = onCancel,
                            enabled = !shredding,
                            shape = G2RoundedCornerShape(17.dp),
                            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0x994C555D)),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.88f),
                                contentColor = Color(0xFF4C555D),
                                disabledContainerColor = Color.White.copy(alpha = 0.48f),
                                disabledContentColor = Color(0x884C555D)
                            )
                        ) {
                            Text(text = "취소", fontSize = if (compact) 17.sp else 20.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ShredderBladeRow(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .height(38.dp)
            .background(Color(0xFF171A20).copy(alpha = 0.82f))
    ) {
        val bladeCount = 18
        val gap = size.width / bladeCount
        repeat(bladeCount) { index ->
            val x = index * gap + gap * 0.5f
            drawLine(
                color = Color.White.copy(alpha = 0.65f),
                start = Offset(x, 2f),
                end = Offset(x - gap * 0.10f, size.height - 2f),
                strokeWidth = gap * 0.16f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.Black.copy(alpha = 0.42f),
                start = Offset(x + gap * 0.18f, 0f),
                end = Offset(x + gap * 0.04f, size.height),
                strokeWidth = gap * 0.12f,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun ShredSuccessToast(visible: Boolean, modifier: Modifier = Modifier) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 360, easing = FastOutSlowInEasing),
            initialOffsetY = { it / 2 }
        ) + fadeIn(animationSpec = tween(durationMillis = 240, easing = FastOutSlowInEasing)),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
            targetOffsetY = { it / 2 }
        ) + fadeOut(animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .background(Color(0xEE1D1D1F), G2RoundedCornerShape(999.dp))
                .border(1.dp, Color.White.copy(alpha = 0.18f), G2RoundedCornerShape(999.dp))
                .padding(horizontal = 22.dp, vertical = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "성공적으로 영수증을 파쇄했습니다",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun RunningHorse(modifier: Modifier = Modifier, running: Boolean) {
    val transition = rememberInfiniteTransition(label = "horse")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (running) 360 else 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )
    val lift = if (phase < 0.5f) phase * 2f else (1f - phase) * 2f

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w * 0.5f
        val cy = h * 0.54f - lift * 6f
        val white = Color.White
        drawOval(white, topLeft = Offset(cx - 52f, cy - 22f), size = Size(104f, 38f))
        drawOval(white, topLeft = Offset(cx + 36f, cy - 42f), size = Size(38f, 28f))
        drawLine(white, Offset(cx + 25f, cy - 18f), Offset(cx + 48f, cy - 33f), strokeWidth = 14f, cap = StrokeCap.Round)

        val tail = Path().apply {
            moveTo(cx - 50f, cy - 18f)
            cubicTo(cx - 78f, cy - 40f + lift * 10f, cx - 92f, cy - 12f, cx - 106f, cy - 26f + lift * 8f)
        }
        drawPath(tail, white, style = Stroke(width = 7f, cap = StrokeCap.Round))

        val legShift = if (phase < 0.5f) 16f else -16f
        drawLine(white, Offset(cx - 34f, cy + 10f), Offset(cx - 52f + legShift, cy + 44f), strokeWidth = 6f, cap = StrokeCap.Round)
        drawLine(white, Offset(cx - 12f, cy + 13f), Offset(cx - 2f - legShift, cy + 45f), strokeWidth = 6f, cap = StrokeCap.Round)
        drawLine(white, Offset(cx + 18f, cy + 12f), Offset(cx + 34f + legShift, cy + 43f), strokeWidth = 6f, cap = StrokeCap.Round)
        drawLine(white, Offset(cx + 39f, cy + 8f), Offset(cx + 23f - legShift, cy + 43f), strokeWidth = 6f, cap = StrokeCap.Round)

        drawCircle(white, radius = 5f, center = Offset(cx + 65f, cy - 34f))
    }
}

@Composable
private fun GlassIconContainer(
    symbol: String,
    size: Dp,
    fontSize: androidx.compose.ui.unit.TextUnit,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape
) {
    Box(
        modifier = modifier
            .size(size)
            .background(Color.White.copy(alpha = 0.22f), shape)
            .border(1.dp, Color.White.copy(alpha = 0.28f), shape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(12.dp)
                .background(Color.White.copy(alpha = 0.18f), shape)
        )
        Text(
            text = symbol,
            color = Color.White,
            fontSize = fontSize,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun GlassTextChip(
    text: String,
    fontSize: androidx.compose.ui.unit.TextUnit,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 15.dp,
    verticalPadding: Dp = 9.dp,
    onClick: (() -> Unit)? = null
) {
    val shape = G2RoundedCornerShape(22.dp)
    Box(
        modifier = modifier
            .heightIn(min = 46.dp)
            .background(Color.White.copy(alpha = 0.22f), shape)
            .border(1.dp, Color.White.copy(alpha = 0.24f), shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = verticalPadding),
            color = Color.White,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TextBanner(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .background(SoftBlue, G2RoundedCornerShape(18.dp))
            .border(1.dp, Color(0xFFD7E0FF), G2RoundedCornerShape(18.dp))
            .padding(vertical = 12.dp, horizontal = 14.dp),
        color = PrimaryBlue,
        fontSize = 15.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun StatusChip(text: String, active: Boolean) {
    Text(
        text = text,
        modifier = Modifier
            .background(if (active) PrimaryBlue else Panel, G2RoundedCornerShape(999.dp))
            .padding(horizontal = 15.dp, vertical = 9.dp),
        color = if (active) Color.White else DarkText,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun SummaryBox(title: String, value: String, modifier: Modifier = Modifier, valueColor: Color = DarkText) {
    Column(
        modifier = modifier
            .background(Color.White, G2RoundedCornerShape(18.dp))
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = title, color = DarkText, fontSize = 13.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text(text = value, color = valueColor, fontSize = 17.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    }
}

@Composable
private fun ResultMetricCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = G2RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = title, color = GrayText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = value, color = DarkText, fontSize = 30.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun FrostMetricCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.12f), G2RoundedCornerShape(22.dp))
            .border(1.dp, Color.White.copy(alpha = 0.24f), G2RoundedCornerShape(22.dp))
            .padding(horizontal = 24.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = title, color = Color.White.copy(alpha = 0.86f), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(text = value, color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun FareField(label: String, value: String, enabled: Boolean, onValueChange: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = label, color = DarkText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = value,
            onValueChange = { if (enabled) onValueChange(it) },
            enabled = true,
            readOnly = !enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(74.dp),
            singleLine = true,
            suffix = { Text("원", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            ),
            shape = G2RoundedCornerShape(16.dp)
        )
    }
}

@Composable
private fun AnimatedActionButton(
    text: String,
    modifier: Modifier = Modifier,
    active: Boolean,
    height: Dp = 76.dp,
    fontSize: androidx.compose.ui.unit.TextUnit = 19.sp,
    onClick: () -> Unit
) {
    val containerColor by animateColorAsState(
        targetValue = if (active) Orange else DisabledButtonGray,
        animationSpec = tween(durationMillis = 360, easing = FastOutSlowInEasing),
        label = "$text container"
    )
    val contentColor by animateColorAsState(
        targetValue = if (active) Color.White else Color(0xFF8C96A6),
        animationSpec = tween(durationMillis = 360, easing = FastOutSlowInEasing),
        label = "$text content"
    )

    val shape = G2RoundedCornerShape(22.dp)
    Button(
        modifier = modifier
            .height(height),
        onClick = onClick,
        enabled = active,
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor,
            disabledContentColor = contentColor
        )
    ) {
        Text(text = text, fontSize = fontSize, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun OrangeButton(text: String, modifier: Modifier = Modifier, enabled: Boolean = true, height: Dp = 62.dp, onClick: () -> Unit) {
    val shape = G2RoundedCornerShape(999.dp)
    Button(
        modifier = modifier
            .height(height),
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Orange,
            contentColor = Color.White,
            disabledContainerColor = DisabledButtonGray,
            disabledContentColor = Color(0xFF8C96A6)
        )
    ) {
        Text(text = text, fontSize = 19.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun GhostButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 62.dp,
    fontSize: androidx.compose.ui.unit.TextUnit = 18.sp,
    onClick: () -> Unit
) {
    val shape = G2RoundedCornerShape(999.dp)
    Button(
        modifier = modifier
            .height(height),
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Panel,
            contentColor = DarkText,
            disabledContainerColor = DisabledButtonGray,
            disabledContentColor = Color(0xFFB8B8B8)
        )
    ) {
        Text(text = text, fontSize = fontSize, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DangerButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val shape = G2RoundedCornerShape(22.dp)
    Button(
        modifier = modifier.height(76.dp),
        onClick = onClick,
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE8473F),
            contentColor = Color.White
        )
    ) {
        Text(text = text, fontSize = 20.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun ReceiptPrimaryButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val shape = G2RoundedCornerShape(999.dp)
    Button(
        modifier = modifier
            .height(74.dp),
        onClick = onClick,
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryBlue,
            contentColor = Color.White
        )
    ) {
        Text(text = text, fontSize = 22.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun ReceiptGhostButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val shape = G2RoundedCornerShape(999.dp)
    Button(
        modifier = modifier
            .height(74.dp),
        onClick = onClick,
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = 0.9f),
            contentColor = DarkText
        )
    ) {
        Text(text = text, fontSize = 22.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun PageHeader(title: String, subtitle: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = G2RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = title, color = DarkText, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(text = subtitle, color = GrayText, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun ReceiptRow(
    label: String,
    value: String,
    bold: Boolean = false,
    textColor: Color = DarkText,
    valueColor: Color = DarkText,
    amountColor: Color = valueColor
) {
    val resolvedValueColor = if (value.trim().endsWith("원")) amountColor else valueColor
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = textColor, fontSize = 23.sp, fontWeight = if (bold) FontWeight.Bold else FontWeight.SemiBold)
        Text(text = value, color = resolvedValueColor, fontSize = 24.sp, fontWeight = if (bold) FontWeight.Bold else FontWeight.SemiBold)
    }
}

@Composable
private fun ThickLine(color: Color = Color.Black) {
    Spacer(modifier = Modifier.fillMaxWidth().height(2.dp).background(color))
}

@Composable
private fun DashedLine(color: Color = Color(0xFFB8B8B8), thickness: Int = 1) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(34) {
            Spacer(modifier = Modifier.weight(1f).height(thickness.dp).background(color))
        }
    }
}

@Composable
private fun PinkingCutEdge(selectedTheme: MeshTheme, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.clip(PinkingCutShape())
    ) {
        Image(
            painter = painterResource(id = selectedTheme.drawableRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier.fillMaxSize()
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            val toothWidth = 26f
            val cutDepth = 17f
            val paperBottom = size.height - cutDepth - 9f
            drawPath(
                path = pinkingEdgePath(size, toothWidth, cutDepth, paperBottom),
                color = Color(0x664A5364),
                style = Stroke(width = 2f, cap = StrokeCap.Round)
            )
        }
    }
}

private class ReceiptPaperShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val toothWidth = 26f
        val cutDepth = 17f
        val paperBottom = size.height - cutDepth - 9f
        val radius = with(density) { 12.dp.toPx() }

        val path = Path().apply {
            moveTo(radius, 0f)
            lineTo(size.width - radius, 0f)
            quadraticTo(size.width, 0f, size.width, radius)
            lineTo(size.width, paperBottom)
            addPinkingEdge(size.width, toothWidth, cutDepth, paperBottom)
            lineTo(0f, paperBottom)
            lineTo(0f, radius)
            quadraticTo(0f, 0f, radius, 0f)
            close()
        }

        return Outline.Generic(path)
    }
}

private class PinkingCutShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val toothWidth = 26f
        val cutDepth = 17f
        val paperBottom = size.height - cutDepth - 9f
        return Outline.Generic(pinkingPaperPath(size, toothWidth, cutDepth, paperBottom))
    }
}

private fun pinkingPaperPath(size: Size, toothWidth: Float, cutDepth: Float, paperBottom: Float): Path =
    Path().apply {
        moveTo(0f, 0f)
        lineTo(size.width, 0f)
        lineTo(size.width, paperBottom)
        addPinkingEdge(size.width, toothWidth, cutDepth, paperBottom)
        lineTo(0f, paperBottom)
        lineTo(0f, 0f)
        close()
    }

private fun pinkingEdgePath(size: Size, toothWidth: Float, cutDepth: Float, paperBottom: Float): Path =
    Path().apply {
        moveTo(size.width, paperBottom)
        addPinkingEdge(size.width, toothWidth, cutDepth, paperBottom)
        lineTo(0f, paperBottom)
    }

private fun Path.addPinkingEdge(width: Float, toothWidth: Float, cutDepth: Float, paperBottom: Float) {
    var x = width
    while (x - toothWidth > 0f) {
        quadraticTo(
            x - toothWidth * 0.26f,
            paperBottom + cutDepth * 0.2f,
            max(0f, x - toothWidth / 2f),
            paperBottom + cutDepth
        )
        quadraticTo(
            x - toothWidth * 0.74f,
            paperBottom + cutDepth * 0.2f,
            max(0f, x - toothWidth),
            paperBottom
        )
        x -= toothWidth
    }
}

private enum class AppScreen {
    Home,
    AppInfo,
    Donation,
    Challenges,
    ChallengeBadgeBoard,
    EndRide,
    History,
    PassengerReceiptBoard,
    PassengerHistory,
    PassengerFullHistory,
    Receipt,
    FareModeSelect,
    FareSettings,
    CustomFareSettings,
    ThemeSettings
}

private data class PendingRide(
    val passengerName: String,
    val endedAt: LocalDateTime,
    val durationSeconds: Long,
    val distanceKm: Float,
    val rideFare: Int
)

private data class RideRecord(
    val id: String,
    val passengerName: String,
    val date: String,
    val endTime: String,
    val durationSeconds: Long,
    val distanceKm: Float,
    val rideFare: Int,
    val tollFare: Int,
    val extraFare: Int,
    val totalFare: Int,
    val themeCode: String,
    val isSettled: Boolean = false
)

private data class ChallengeItem(
    val title: String,
    val description: String,
    val progress: Float,
    val progressLabel: String
)

private fun buildChallengeItems(records: List<RideRecord>): List<ChallengeItem> {
    val recentCutoffDate = LocalDate.now().minusDays(30)
    val completedRides = records.size
    val uniquePassengerCount = records
        .map { it.passengerName.trim().ifBlank { "이름 없는" } }
        .distinct()
        .size
    val recentRideCount = records.count { record ->
        record.recordDateOrNull()?.let { !it.isBefore(recentCutoffDate) } == true
    }
    val totalDistanceKm = records.sumOf { it.distanceKm.toDouble() }.toFloat()
    val totalFare = records.sumOf { it.totalFare }
    val totalDurationSeconds = records.sumOf { it.durationSeconds }
    val nightReceiptCount = records.count { it.themeCode == MeshTheme.Night.code }
    val longRideCount = records.count { it.distanceKm >= 5f }
    val shortRideCount = records.count { it.distanceKm in 0f..1f }
    val tollRideCount = records.count { it.tollFare > 0 }
    val extraFareRideCount = records.count { it.extraFare > 0 }
    val premiumFareCount = records.count { it.totalFare >= 10000 }
    val themeCount = records.map { it.themeCode }.distinct().size
    val busiestPassengerRideCount = records
        .groupBy { it.passengerName.trim().ifBlank { "이름 없는" } }
        .values
        .maxOfOrNull { it.size } ?: 0
    val sameDayMaxRideCount = records
        .groupBy { it.date }
        .values
        .maxOfOrNull { it.size } ?: 0

    fun item(title: String, description: String, current: Float, target: Float, label: String): ChallengeItem =
        ChallengeItem(
            title = title,
            description = description,
            progress = if (target <= 0f) 0f else current / target,
            progressLabel = label
        )

    return listOf(
        item(
            title = "첫 영수증 발급",
            description = "첫 번째 카풀 운행을 완료하면 열리는 시작 배지",
            current = completedRides.toFloat(),
            target = 1f,
            label = "$completedRides / 1회"
        ),
        item(
            title = "단골 손님 메모리",
            description = "서로 다른 손님 3명의 카풀 영수증을 모으기",
            current = uniquePassengerCount.toFloat(),
            target = 3f,
            label = "$uniquePassengerCount / 3명"
        ),
        item(
            title = "최근 30일 카풀러",
            description = "최근 30일 안에 10번의 카풀 기록 남기기",
            current = recentRideCount.toFloat(),
            target = 10f,
            label = "$recentRideCount / 10회"
        ),
        item(
            title = "100km 동행",
            description = "친구들과 함께 누적 100km 이동하기",
            current = totalDistanceKm,
            target = 100f,
            label = "%.1fkm / 100km".format(totalDistanceKm)
        ),
        item(
            title = "10만원 정산왕",
            description = "누적 결제 금액 100,000원 달성하기",
            current = totalFare.toFloat(),
            target = 100000f,
            label = "${totalFare.formatWon()}원 / 100,000원"
        ),
        item(
            title = "1시간 안전 운행",
            description = "누적 주행 시간 1시간 채우기",
            current = totalDurationSeconds.toFloat(),
            target = 3600f,
            label = "${totalDurationSeconds.formatDuration()} / 01:00:00"
        ),
        item(
            title = "나이트 영수증 컬렉터",
            description = "나이트 테마로 저장된 영수증 3장 모으기",
            current = nightReceiptCount.toFloat(),
            target = 3f,
            label = "$nightReceiptCount / 3장"
        ),
        item(
            title = "장거리 동행",
            description = "5km 이상 이동한 카풀 기록 5장 모으기",
            current = longRideCount.toFloat(),
            target = 5f,
            label = "$longRideCount / 5회"
        ),
        item(
            title = "동네 한 바퀴",
            description = "1km 이하 짧은 카풀 기록 5장 모으기",
            current = shortRideCount.toFloat(),
            target = 5f,
            label = "$shortRideCount / 5회"
        ),
        item(
            title = "통행료 정산러",
            description = "통행요금이 포함된 영수증 3장 저장하기",
            current = tollRideCount.toFloat(),
            target = 3f,
            label = "$tollRideCount / 3장"
        ),
        item(
            title = "추가요금 협상가",
            description = "추가요금이 포함된 영수증 3장 저장하기",
            current = extraFareRideCount.toFloat(),
            target = 3f,
            label = "$extraFareRideCount / 3장"
        ),
        item(
            title = "만원 넘는 카풀",
            description = "총 운행요금 10,000원 이상 영수증 5장 만들기",
            current = premiumFareCount.toFloat(),
            target = 5f,
            label = "$premiumFareCount / 5장"
        ),
        item(
            title = "테마 수집가",
            description = "서로 다른 테마로 저장된 영수증 5종 모으기",
            current = themeCount.toFloat(),
            target = 5f,
            label = "$themeCount / 5종"
        ),
        item(
            title = "찐 단골 인증",
            description = "같은 손님과 카풀 기록 10장 쌓기",
            current = busiestPassengerRideCount.toFloat(),
            target = 10f,
            label = "$busiestPassengerRideCount / 10회"
        ),
        item(
            title = "하루 카풀 챌린지",
            description = "하루에 카풀 영수증 5장 만들기",
            current = sameDayMaxRideCount.toFloat(),
            target = 5f,
            label = "$sameDayMaxRideCount / 5장"
        )
    )
}

private data class FarePolicy(
    val regionName: String,
    val baseDistanceKm: Float,
    val stepDistanceMeters: Float,
    val baseFare: Int,
    val stepFare: Int,
    val label: String,
    val shortLabel: String
) {
    companion object {
        fun daytime(): FarePolicy = FarePolicy(
            regionName = FareRegion.SEOUL.displayName,
            baseDistanceKm = FareRegion.SEOUL.baseDistanceKm,
            stepDistanceMeters = FareRegion.SEOUL.stepDistanceMeters,
            baseFare = 4800,
            stepFare = 100,
            label = "일반 요금",
            shortLabel = "일반"
        )

        fun from(region: FareRegion, surchargeMode: SurchargeMode): FarePolicy {
            val multiplier = surchargeMode.multiplier
            return FarePolicy(
                regionName = region.displayName,
                baseDistanceKm = region.baseDistanceKm,
                stepDistanceMeters = region.stepDistanceMeters,
                baseFare = (region.baseFare * multiplier).roundToNearestTen(),
                stepFare = (region.stepFare * multiplier).roundToNearestTen(),
                label = surchargeMode.displayName,
                shortLabel = surchargeMode.shortLabel
            )
        }

        fun custom(baseFare: Int, stepFare: Int, surchargeMode: SurchargeMode): FarePolicy {
            val multiplier = surchargeMode.multiplier
            val labelPrefix = "커스텀 요금"
            return FarePolicy(
                regionName = "커스텀",
                baseDistanceKm = 0f,
                stepDistanceMeters = 100f,
                baseFare = (baseFare * multiplier).roundToNearestTen(),
                stepFare = (stepFare * multiplier).roundToNearestTen(),
                label = if (surchargeMode == SurchargeMode.Normal) labelPrefix else "$labelPrefix · ${surchargeMode.displayName}",
                shortLabel = "커스텀"
            )
        }
    }
}

private enum class SurchargeMode(
    val displayName: String,
    val buttonText: String,
    val shortLabel: String,
    val multiplier: Float
) {
    Normal("일반 요금", "일반", "일반", 1f),
    Twenty("20% 할증", "20%", "할증 20%", 1.2f),
    Forty("40% 할증", "40%", "할증 40%", 1.4f)
}

private enum class MeshTheme(
    val code: String,
    val displayName: String,
    val drawableRes: Int
) {
    Pearl("pearl", "펄 코랄", R.drawable.mesh_theme_1),
    Aqua("aqua", "오션 블루", R.drawable.mesh_theme_2),
    Mint("mint", "민트 글로우", R.drawable.mesh_theme_3),
    Violet("violet", "라일락", R.drawable.mesh_theme_4),
    Sunrise("sunrise", "선라이즈", R.drawable.mesh_theme_5),
    Night("night", "나이트", R.drawable.mesh_theme_6),
    Silver("silver", "실버", R.drawable.mesh_theme_7)
}

private fun MeshTheme.backgroundOverlayAlpha(): Float =
    if (this == MeshTheme.Night) 0.40f else 0.60f

private fun MeshTheme.receiptPaperOverlayAlpha(): Float =
    0f

private fun MeshTheme.receiptOuterOverlayAlpha(): Float =
    if (this == MeshTheme.Night) 0f else 0.84f

private fun RideRecord.receiptThemeOr(defaultTheme: MeshTheme): MeshTheme =
    MeshTheme.entries.firstOrNull { it.code == themeCode } ?: defaultTheme

private enum class FareRegion(
    val code: String,
    val displayName: String,
    val baseFare: Int,
    val baseDistanceKm: Float,
    val stepDistanceMeters: Float,
    val stepFare: Int,
    val nightMultiplier: Float,
    val hasSeoulStyleNight: Boolean,
    val nightLabel: String
) {
    SEOUL("seoul", "서울", 4800, 1.6f, 131f, 100, 1.2f, true, "22시 20%, 23-02시 40%"),
    GYEONGGI("gyeonggi", "경기", 4800, 1.6f, 131f, 100, 1.2f, false, "22-04시 20%"),
    INCHEON("incheon", "인천", 4800, 1.6f, 135f, 100, 1.2f, false, "22-04시 20%"),
    BUSAN("busan", "부산", 4800, 2.0f, 132f, 100, 1.2f, false, "22-04시 20%"),
    DAEGU("daegu", "대구", 4500, 1.6f, 130f, 100, 1.2f, false, "22-04시 20%"),
    GWANGJU("gwangju", "광주", 4300, 1.8f, 134f, 100, 1.2f, false, "22-04시 20%"),
    DAEJEON("daejeon", "대전", 4300, 1.8f, 132f, 100, 1.2f, false, "22-04시 20%"),
    ULSAN("ulsan", "울산", 4500, 2.0f, 125f, 100, 1.2f, false, "22-04시 20%"),
    JEJU("jeju", "제주", 4300, 2.0f, 126f, 100, 1.2f, false, "22-04시 20%")
}

private fun Float.roundToNearestTen(): Int = (kotlin.math.round(this / 10f) * 10).toInt()

private fun Long.formatDuration(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60
    return "%02d:%02d:%02d".format(hours, minutes, seconds)
}

private fun Int.formatWon(): String = "%,d".format(this)

private fun FarePolicy.summaryText(): String =
    if (baseDistanceKm <= 0f) {
        "$label · 기본요금 ${baseFare.formatWon()}원 · ${stepDistanceMeters.toInt()}m당 ${stepFare}원"
    } else {
        "$label · 기본 ${baseDistanceKm}km · ${stepDistanceMeters.toInt()}m당 ${stepFare}원"
    }

private fun FarePolicy.distanceRuleText(): String =
    if (baseDistanceKm <= 0f) {
        "시작 직후부터 ${stepDistanceMeters.toInt()}m마다 ${stepFare}원씩 계산돼요"
    } else {
        "${baseDistanceKm}km 이후 ${stepDistanceMeters.toInt()}m마다 ${stepFare}원씩 계산돼요"
    }

private val RideRecordDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yy/MM/dd")

private fun RideRecord.recordDateOrNull(): LocalDate? =
    runCatching { LocalDate.parse(date, RideRecordDateFormatter) }.getOrNull()

private val PrimaryBlue = Color(0xFF0066CC)
private val SoftBlue = Color(0xFFEAF4FF)
private val AppBg = Color(0xFFF5F5F7)
private val Panel = Color(0xFFFAFAFC)
private val ButtonGray = Color(0xFFF0F2F6)
private val DisabledButtonGray = Color(0xFFD3DAE4)
private val Line = Color(0xFFE0E0E0)
private val GlassWhite = Color.White.copy(alpha = 0.94f)
private val Orange = PrimaryBlue
private val Cream = AppBg
private val BlackCard = Color(0xFF1D1D1F)
private val DarkText = Color(0xFF1D1D1F)
private val GrayText = Color(0xFF7A7A7A)
private val NoticeGreen = PrimaryBlue
private val NoticeGreenSoft = SoftBlue
private val HomePagerTopPadding = 28.dp
private val HomePagerBottomPadding = 0.dp
private val HomePagerSectionSpacing = 16.dp
private val HomePagerIndicatorSlotHeight = 30.dp
private val HomePagerIndicatorYOffset = (-8).dp
private val QuickSettingsSupportHeight = 250.dp
private val QuickSettingsSecondaryHeight = 172.dp

private val AstaSans = FontFamily(
    Font(R.font.asta_sans_regular, FontWeight.Normal),
    Font(R.font.asta_sans_medium, FontWeight.Medium),
    Font(R.font.asta_sans_semibold, FontWeight.SemiBold),
    Font(R.font.asta_sans_bold, FontWeight.Bold),
    Font(R.font.asta_sans_extrabold, FontWeight.ExtraBold),
    Font(R.font.asta_sans_extrabold, FontWeight.Black)
)

private val DefaultTypography = Typography()
private val AstaTypography = Typography(
    displayLarge = DefaultTypography.displayLarge.copy(fontFamily = AstaSans),
    displayMedium = DefaultTypography.displayMedium.copy(fontFamily = AstaSans),
    displaySmall = DefaultTypography.displaySmall.copy(fontFamily = AstaSans),
    headlineLarge = DefaultTypography.headlineLarge.copy(fontFamily = AstaSans),
    headlineMedium = DefaultTypography.headlineMedium.copy(fontFamily = AstaSans),
    headlineSmall = DefaultTypography.headlineSmall.copy(fontFamily = AstaSans),
    titleLarge = DefaultTypography.titleLarge.copy(fontFamily = AstaSans),
    titleMedium = DefaultTypography.titleMedium.copy(fontFamily = AstaSans),
    titleSmall = DefaultTypography.titleSmall.copy(fontFamily = AstaSans),
    bodyLarge = DefaultTypography.bodyLarge.copy(fontFamily = AstaSans),
    bodyMedium = DefaultTypography.bodyMedium.copy(fontFamily = AstaSans),
    bodySmall = DefaultTypography.bodySmall.copy(fontFamily = AstaSans),
    labelLarge = DefaultTypography.labelLarge.copy(fontFamily = AstaSans),
    labelMedium = DefaultTypography.labelMedium.copy(fontFamily = AstaSans),
    labelSmall = DefaultTypography.labelSmall.copy(fontFamily = AstaSans)
)
