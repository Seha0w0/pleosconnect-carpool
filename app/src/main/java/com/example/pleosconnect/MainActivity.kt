package com.example.pleosconnect

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
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
import kotlin.math.sign
import kotlin.math.sin
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
        farePolicy = FarePolicy.from(selectedRegion, selectedSurcharge)

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
                historyReturnScreen = historyReturnScreen,
                historyWidgetSelectionMode = historyWidgetSelectionMode,
                selectedWidgetPassengerName = selectedWidgetPassengerName,
                globalNoticeText = globalNoticeText,
                selectedPassengerName = selectedPassengerName,
                selectedHistoryMonth = selectedHistoryMonth,
                onPassengerNameChange = { passengerName = it },
                onStart = { startRide() },
                onStop = { stopRide() },
                onTestDrive = { testDrive131m() },
                onGoHome = { screen = AppScreen.Home },
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
                onGoFareSettings = { screen = AppScreen.FareSettings },
                onGoThemeSettings = { screen = AppScreen.ThemeSettings },
                onSelectRegion = {
                    selectedRegion = it
                    farePolicy = FarePolicy.from(selectedRegion, selectedSurcharge)
                    saveSelectedRegion(it)
                    screen = AppScreen.Home
                },
                onSelectSurcharge = {
                    if (!isRunning) {
                        selectedSurcharge = it
                        farePolicy = FarePolicy.from(selectedRegion, selectedSurcharge)
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
                    selectedRecord = it
                    screen = AppScreen.Receipt
                },
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
                onGoPassengerMonthHistory = { screen = AppScreen.PassengerHistory }
            )
        }
    }

    private fun startRide() {
        farePolicy = FarePolicy.from(selectedRegion, selectedSurcharge)
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
            totalFare = pending.rideFare + tollFare + extraFare
        )

        rideRecords.add(0, record)
        saveRideRecords(rideRecords)
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
                    totalFare = item.getInt("totalFare")
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
    historyReturnScreen: AppScreen,
    historyWidgetSelectionMode: Boolean,
    selectedWidgetPassengerName: String?,
    globalNoticeText: String?,
    selectedPassengerName: String?,
    selectedHistoryMonth: YearMonth,
    onPassengerNameChange: (String) -> Unit,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onTestDrive: () -> Unit,
    onGoHome: () -> Unit,
    onGoHistory: () -> Unit,
    onGoHistoryBack: () -> Unit,
    onGoFareSettings: () -> Unit,
    onGoThemeSettings: () -> Unit,
    onSelectRegion: (FareRegion) -> Unit,
    onSelectSurcharge: (SurchargeMode) -> Unit,
    onSelectTheme: (MeshTheme) -> Unit,
    onTollChange: (String) -> Unit,
    onExtraChange: (String) -> Unit,
    onSaveRide: () -> Unit,
    onSelectRecord: (RideRecord) -> Unit,
    onGoWidgetPicker: () -> Unit,
    onSelectWidgetPassenger: (String) -> Unit,
    onAddWidgetPassenger: () -> Unit,
    onRemovePassengerWidget: (String) -> Unit,
    onDismissGlobalNotice: () -> Unit,
    onSelectPassenger: (String) -> Unit,
    onChangeHistoryMonth: (YearMonth) -> Unit,
    onGoPassengerHistoryBack: () -> Unit,
    onGoPassengerFullHistory: () -> Unit,
    onGoPassengerMonthHistory: () -> Unit
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
                    AppMeshBackground(
                        selectedTheme = selectedTheme,
                        overlayAlpha = if (screen == AppScreen.Receipt) selectedTheme.receiptOuterOverlayAlpha() else selectedTheme.backgroundOverlayAlpha()
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
                                onSelectSurcharge = onSelectSurcharge
                            )
                        } else {
                            BouncyScrollableColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(18.dp),
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
                                        onGoHome = onGoHome
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
    onSelectSurcharge: (SurchargeMode) -> Unit
) {
    var topNoticeVisible by remember { mutableStateOf(false) }
    var passengerDialogVisible by remember { mutableStateOf(false) }
    var editingPassengerName by remember(passengerName) { mutableStateOf(passengerName) }
    val passengerDialogBlur by animateDpAsState(
        targetValue = if (passengerDialogVisible) 18.dp else 0.dp,
        animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing),
        label = "passengerDialogBlur"
    )
    val pagerState = rememberPagerState(pageCount = { 3 })

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
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TopBar()

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                pageSpacing = 14.dp
            ) { page ->
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    when (page) {
                        0 -> {
                            MeterCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(570.dp),
                                passengerName = passengerName,
                                fare = fare,
                                isRunning = isRunning,
                                tripDistanceKm = tripDistanceKm,
                                elapsedSeconds = elapsedSeconds,
                                farePolicy = farePolicy,
                                selectedTheme = selectedTheme,
                                onEditPassenger = {
                                    editingPassengerName = passengerName
                                    passengerDialogVisible = true
                                }
                            )

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = G2RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = GlassWhite),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.36f)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                            ) {
                                Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        AnimatedActionButton(
                                            text = "주행시작",
                                            modifier = Modifier.weight(1f),
                                            active = !isRunning,
                                            onClick = onStart
                                        )
                                        AnimatedActionButton(
                                            text = "주행종료",
                                            modifier = Modifier.weight(1f),
                                            active = isRunning,
                                            onClick = onStop
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        GhostButton(
                                            text = "요금제",
                                            modifier = Modifier.weight(1f),
                                            onClick = {
                                                if (isRunning) {
                                                    topNoticeVisible = true
                                                } else {
                                                    onGoFareSettings()
                                                }
                                            }
                                        )
                                        GhostButton(
                                            text = "테마",
                                            modifier = Modifier.weight(1f),
                                            onClick = onGoThemeSettings
                                        )
                                        GhostButton(
                                            text = "내역 보기",
                                            modifier = Modifier.weight(1f),
                                            onClick = onGoHistory
                                        )
                                    }
                                }
                            }

                            SurchargeSelector(
                                selectedSurcharge = selectedSurcharge,
                                enabled = !isRunning,
                                onSelectSurcharge = onSelectSurcharge
                            )

                            HomePagerIndicator(
                                currentPage = pagerState.currentPage,
                                pageCount = 3,
                                modifier = Modifier.fillMaxWidth()
                            )

                            DriveStatusPanel(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(218.dp),
                                isRunning = isRunning,
                                tripDistanceKm = tripDistanceKm,
                                elapsedSeconds = elapsedSeconds
                            )
                        }

                        1 -> PassengerWidgetPage(
                            records = records,
                            passengerWidgets = passengerWidgets,
                            currentPage = pagerState.currentPage,
                            onAddClick = onGoWidgetPicker,
                            onRemovePassengerWidget = onRemovePassengerWidget
                        )

                        else -> HomeQuickSettingsPage(
                            isRunning = isRunning,
                            farePolicy = farePolicy,
                            selectedRegion = selectedRegion,
                            selectedSurcharge = selectedSurcharge,
                            currentPage = pagerState.currentPage,
                            onGoFareSettings = onGoFareSettings,
                            onGoThemeSettings = onGoThemeSettings,
                            onGoHistory = onGoHistory
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
private fun PassengerWidgetPage(
    records: List<RideRecord>,
    passengerWidgets: List<String>,
    currentPage: Int,
    onAddClick: () -> Unit,
    onRemovePassengerWidget: (String) -> Unit
) {
    var deleteTargetName by remember { mutableStateOf<String?>(null) }
    val recentCutoffDate = LocalDate.now().minusDays(30)
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

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = G2RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = "최근 손님", color = DarkText, fontSize = 32.sp, fontWeight = FontWeight.Black)
            Text(text = "최근 30일 안의 영수증을 빠르게 열어볼 수 있어요", color = GrayText, fontSize = 18.sp, fontWeight = FontWeight.Medium)
        }
    }

    BouncyScrollableColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (recentPassengerGroups.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = G2RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8ECF4)),
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
        } else {
            recentPassengerGroups.forEach { (name, passengerRecords) ->
                val latest = passengerRecords.first()
                val totalFare = passengerRecords.sumOf { it.totalFare }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = G2RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8ECF4)),
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

    HomePagerIndicator(
        currentPage = currentPage,
        pageCount = 3,
        modifier = Modifier.fillMaxWidth()
    )

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
    onGoHistory: () -> Unit
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
                text = "${selectedRegion.displayName} · ${farePolicy.baseDistanceKm}km 기본 · ${farePolicy.stepDistanceMeters.toInt()}m당 ${farePolicy.stepFare}원",
                color = GrayText,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GhostButton(text = "요금제", modifier = Modifier.weight(1f), onClick = onGoFareSettings)
                GhostButton(text = "테마", modifier = Modifier.weight(1f), onClick = onGoThemeSettings)
                GhostButton(text = "내역", modifier = Modifier.weight(1f), onClick = onGoHistory)
            }
        }
    }

    HomePagerIndicator(
        currentPage = currentPage,
        pageCount = 3,
        modifier = Modifier.fillMaxWidth()
    )
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
    onGoHistory: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
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
                text = "${farePolicy.baseDistanceKm}km 이후 ${farePolicy.stepDistanceMeters.toInt()}m마다 ${farePolicy.stepFare}원씩 계산돼요",
                color = GrayText,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = G2RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = GlassWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GhostButton(text = "요금제", modifier = Modifier.weight(1f), enabled = !isRunning, onClick = onGoFareSettings)
            GhostButton(text = "테마", modifier = Modifier.weight(1f), onClick = onGoThemeSettings)
            GhostButton(text = "내역 보기", modifier = Modifier.weight(1f), onClick = onGoHistory)
        }
    }

    HomePagerIndicator(
        currentPage = currentPage,
        pageCount = 3,
        modifier = Modifier.fillMaxWidth()
    )
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
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(text = "$passengerName 손님", color = Color.White, fontSize = 31.sp, fontWeight = FontWeight.Black)
                        Text(text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm")), color = Color.White.copy(alpha = 0.76f), fontSize = 19.sp, fontWeight = FontWeight.Medium)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        GlassTextChip(
                            text = "수정",
                            fontSize = 15.sp,
                            onClick = onEditPassenger
                        )
                        GlassTextChip(
                            text = farePolicy.label,
                            fontSize = 17.sp,
                            horizontalPadding = 14.dp,
                            verticalPadding = 8.dp
                        )
                    }
                }

                RunningHorse(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.8f),
                    running = isRunning
                )

                Text(
                    text = "${fare.formatWon()}원",
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    fontSize = 70.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.End
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.92f), G2RoundedCornerShape(22.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatusChip(text = if (isRunning) "실제 주행" else "대기 중", active = isRunning)
                        StatusChip(text = farePolicy.shortLabel, active = true)
                    }
                    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "주행거리 ${"%.3f".format(tripDistanceKm)}km", color = DarkText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(text = "주행시간 ${elapsedSeconds.formatDuration()}", color = DarkText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(text = "기본 ${farePolicy.baseDistanceKm}km · ${farePolicy.stepDistanceMeters.toInt()}m당 ${farePolicy.stepFare}원", color = GrayText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
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
    selectedSurcharge: SurchargeMode,
    enabled: Boolean,
    onSelectSurcharge: (SurchargeMode) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = G2RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "할증 요금제", color = DarkText, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                if (!enabled) {
                    Text(
                        text = "주행 중 변경 불가",
                        color = GrayText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SurchargeMode.entries.forEach { mode ->
                    val selected = mode == selectedSurcharge
                    val buttonColor by animateColorAsState(
                        targetValue = if (selected) PrimaryBlue else Panel,
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                        label = "surchargeButtonColor"
                    )
                    val textColor by animateColorAsState(
                        targetValue = if (selected) Color.White else DarkText,
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                        label = "surchargeTextColor"
                    )
                    val disabledButtonColor by animateColorAsState(
                        targetValue = if (selected) SoftBlue else Panel,
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
                            .height(56.dp)
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            },
                        enabled = enabled,
                        onClick = { onSelectSurcharge(mode) },
                        shape = G2RoundedCornerShape(999.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonColor,
                            contentColor = textColor,
                            disabledContainerColor = disabledButtonColor,
                            disabledContentColor = disabledTextColor
                        )
                    ) {
                        Text(text = mode.buttonText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
    TopBar()
    PageHeader(
        title = "요금제 선택",
        subtitle = "주행 지역에 맞는 중형택시 요금제를 선택하세요."
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

    OrangeButton(text = "홈으로", modifier = Modifier.fillMaxWidth(), onClick = onGoHome)
}

@Composable
private fun ThemeSettingsScreen(
    selectedTheme: MeshTheme,
    onSelectTheme: (MeshTheme) -> Unit,
    onGoHome: () -> Unit
) {
    TopBar()
    PageHeader(
        title = "테마",
        subtitle = "배경을 선택하세요"
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

    OrangeButton(text = "홈으로", modifier = Modifier.fillMaxWidth(), onClick = onGoHome)
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

    TopBar()
    PageHeader(
        title = "$passengerName 손님,",
        subtitle = "오늘 주행은 어떠셨나요?"
    )

    val frostedShape = G2RoundedCornerShape(28.dp)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(330.dp),
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
                    .padding(30.dp),
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
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
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
        Column(modifier = Modifier.padding(30.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Text(text = "정산 입력", color = DarkText, fontSize = 28.sp, fontWeight = FontWeight.Black)
            Text(text = "통행료나 별도 합의한 추가요금이 있으면 입력하세요.", color = GrayText, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            FareField(label = "운행요금", value = rideFare.formatWon(), enabled = false, onValueChange = {})
            FareField(label = "통행요금", value = tollText, enabled = true, onValueChange = onTollChange)
            FareField(label = "추가요금", value = extraText, enabled = true, onValueChange = onExtraChange)
        }
    }

    OrangeButton(text = "주행 완료", modifier = Modifier.fillMaxWidth(), onClick = onSaveRide)
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
    TopBar()
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
    val visibleRecords = selectedDate?.let { date ->
        monthRecords.filter { it.recordDateOrNull() == date }
    } ?: monthRecords

    TopBar()
    PageHeader(
        title = "$name 손님",
        subtitle = "언제 카풀을 이용했는지 한눈에 볼 수 있어요."
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
    Text(
        text = selectedDate?.let { "${it.monthValue}월 ${it.dayOfMonth}일 이용 목록" } ?: "이용 목록",
        color = DarkText,
        fontSize = 24.sp,
        fontWeight = FontWeight.Black
    )
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

    TopBar()
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

    GhostButton(text = "뒤로가기", modifier = Modifier.fillMaxWidth(), onClick = onGoBack)

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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = G2RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
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
    }
}

@Composable
private fun ReceiptScreen(
    record: RideRecord?,
    selectedTheme: MeshTheme,
    onGoHistory: () -> Unit,
    onGoHome: () -> Unit
) {
    val item = record
    if (item == null) {
        Text(text = "선택된 내역이 없습니다.", color = DarkText, fontSize = 20.sp)
        OrangeButton(text = "홈으로", modifier = Modifier.fillMaxWidth(), onClick = onGoHome)
        return
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1090.dp)
            .padding(horizontal = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(ReceiptPaperShape())
        ) {
            Image(
                painter = painterResource(id = selectedTheme.drawableRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 38.dp, end = 38.dp, top = 26.dp, bottom = 110.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .width(230.dp)
                            .height(18.dp)
                            .background(Color(0xFF5A5F6A), G2RoundedCornerShape(7.dp))
                    )
                    GlassIconContainer(symbol = "♞", size = 52.dp, fontSize = 28.sp)
                    Text(text = "카풀 영수증", color = DarkText, fontSize = 34.sp, fontWeight = FontWeight.Black)
                    DashedLine(color = Color(0xFF30343B), thickness = 2)
                }

                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(text = "${item.passengerName} 손님,", color = DarkText, fontSize = 40.sp, fontWeight = FontWeight.Black)
                    Text(text = "카풀미터기를 이용해주셔서 감사합니다", color = GrayText, fontSize = 20.sp, fontWeight = FontWeight.Medium)
                    ThickLine()
                }

                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    ReceiptRow("탑승일자", item.date)
                    ReceiptRow("하차시각", item.endTime)
                    DashedLine()
                    ReceiptRow("총 주행 시간", item.durationSeconds.formatDuration())
                    ReceiptRow("총 주행 거리", "%.1fkm".format(item.distanceKm))
                    ReceiptRow("운행요금", "${item.rideFare.formatWon()}원")
                    DashedLine()
                    ReceiptRow("통행요금", "${item.tollFare.formatWon()}원")
                    ReceiptRow("추가요금", "${item.extraFare.formatWon()}원")
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DashedLine(color = Color(0xFF30343B), thickness = 2)
                    Text(text = "총 운행요금", color = GrayText, fontSize = 24.sp, fontWeight = FontWeight.Black)
                    Text(text = "${item.totalFare.formatWon()}원", modifier = Modifier.fillMaxWidth(), color = PrimaryBlue, fontSize = 82.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.End)
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
        }
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        ReceiptGhostButton(text = "내역 보기", modifier = Modifier.weight(1f), onClick = onGoHistory)
        ReceiptPrimaryButton(text = "홈으로", modifier = Modifier.weight(1f), onClick = onGoHome)
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
    val shape = G2RoundedCornerShape(999.dp)
    Box(
        modifier = modifier
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(12.dp)
                .background(Color.White.copy(alpha = 0.20f), shape)
                .border(1.dp, Color.White.copy(alpha = 0.28f), shape)
        )
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
private fun TopBar() {
    val shape = G2RoundedCornerShape(24.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.18f), shape)
            .border(1.dp, Color.White.copy(alpha = 0.30f), shape)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(18.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.32f),
                            Color.White.copy(alpha = 0.14f),
                            Color.White.copy(alpha = 0.22f)
                        )
                    ),
                    shape
                )
        )
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                GlassIconContainer(symbol = "♞", size = 42.dp, fontSize = 22.sp)
                Text(text = "카풀 미터기", color = DarkText, fontSize = 20.sp, fontWeight = FontWeight.Black)
            }
        }
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
private fun AnimatedActionButton(text: String, modifier: Modifier = Modifier, active: Boolean, onClick: () -> Unit) {
    val containerColor by animateColorAsState(
        targetValue = if (active) Orange else Color(0xFFD8DEE8),
        animationSpec = tween(durationMillis = 360, easing = FastOutSlowInEasing),
        label = "$text container"
    )
    val contentColor by animateColorAsState(
        targetValue = if (active) Color.White else Color(0xFF8C96A6),
        animationSpec = tween(durationMillis = 360, easing = FastOutSlowInEasing),
        label = "$text content"
    )

    val shape = G2RoundedCornerShape(999.dp)
    Button(
        modifier = modifier
            .height(62.dp),
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
        Text(text = text, fontSize = 19.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun OrangeButton(text: String, modifier: Modifier = Modifier, enabled: Boolean = true, onClick: () -> Unit) {
    val shape = G2RoundedCornerShape(999.dp)
    Button(
        modifier = modifier
            .height(62.dp),
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Orange,
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFD8DEE8),
            disabledContentColor = Color(0xFF8C96A6)
        )
    ) {
        Text(text = text, fontSize = 19.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun GhostButton(text: String, modifier: Modifier = Modifier, enabled: Boolean = true, onClick: () -> Unit) {
    val shape = G2RoundedCornerShape(999.dp)
    Button(
        modifier = modifier
            .height(62.dp),
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Panel,
            contentColor = DarkText,
            disabledContainerColor = Color(0xFFD8DEE8),
            disabledContentColor = Color(0xFFB8B8B8)
        )
    ) {
        Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
private fun ReceiptRow(label: String, value: String, bold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = DarkText, fontSize = 23.sp, fontWeight = if (bold) FontWeight.Bold else FontWeight.SemiBold)
        Text(text = value, color = DarkText, fontSize = 24.sp, fontWeight = if (bold) FontWeight.Bold else FontWeight.SemiBold)
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
        val radius = with(density) { 26.dp.toPx() }

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
    EndRide,
    History,
    PassengerHistory,
    PassengerFullHistory,
    Receipt,
    FareSettings,
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
    val totalFare: Int
)

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
    if (this == MeshTheme.Night) 0.58f else 0.66f

private fun MeshTheme.receiptOuterOverlayAlpha(): Float =
    if (this == MeshTheme.Night) 0.76f else 0.84f

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

private val RideRecordDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yy/MM/dd")

private fun RideRecord.recordDateOrNull(): LocalDate? =
    runCatching { LocalDate.parse(date, RideRecordDateFormatter) }.getOrNull()

private val PrimaryBlue = Color(0xFF0066CC)
private val SoftBlue = Color(0xFFEAF4FF)
private val AppBg = Color(0xFFF5F5F7)
private val Panel = Color(0xFFFAFAFC)
private val Line = Color(0xFFE0E0E0)
private val GlassWhite = Color.White.copy(alpha = 0.94f)
private val Orange = PrimaryBlue
private val Cream = AppBg
private val BlackCard = Color(0xFF1D1D1F)
private val DarkText = Color(0xFF1D1D1F)
private val GrayText = Color(0xFF7A7A7A)
private val NoticeGreen = PrimaryBlue
private val NoticeGreenSoft = SoftBlue

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



