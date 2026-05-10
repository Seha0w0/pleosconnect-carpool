package com.example.pleosconnect

import android.content.Context
import android.content.res.Configuration
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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
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
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import ai.pleos.playground.vehicle.Vehicle
import ai.pleos.playground.vehicle.listener.DistanceDrivenListener
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
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
    private var selectedTheme by mutableStateOf(MeshTheme.Aqua)
    private var userSelectedTheme by mutableStateOf(false)
    private var useCustomFare by mutableStateOf(false)
    private var autoSurchargeEnabled by mutableStateOf(false)
    private var customBaseFare by mutableIntStateOf(4800)
    private var customStepFare by mutableIntStateOf(100)
    private var homeInitialPage by mutableIntStateOf(0)
    private var developerDonationCount by mutableIntStateOf(0)

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
        selectedTheme = loadInitialTheme()
        useCustomFare = loadUseCustomFare()
        customBaseFare = loadCustomBaseFare()
        customStepFare = loadCustomStepFare()
        developerDonationCount = loadDeveloperDonationCount()
        farePolicy = activeFarePolicy()

        vehicle = Vehicle(this)
        vehicle.initialize()
        vehicle.getOdometer().registerDistanceDriven(distanceDrivenListener)

        setContent {
            val systemDarkTheme = isSystemInDarkTheme()

            LaunchedEffect(systemDarkTheme, userSelectedTheme) {
                if (!userSelectedTheme) {
                    selectedTheme = if (systemDarkTheme) {
                        MeshTheme.Night
                    } else {
                        loadSelectedTheme(defaultThemeForSystem(isDarkMode = false))
                    }
                }
            }

            LaunchedEffect(isRunning, startedAtMillis) {
                while (isRunning) {
                    elapsedSeconds = ((System.currentTimeMillis() - startedAtMillis) / 1000L).coerceAtLeast(0L)
                    requestCurrentDistanceDriven()
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
                autoSurchargeEnabled = autoSurchargeEnabled,
                customBaseFare = customBaseFare,
                customStepFare = customStepFare,
                historyReturnScreen = historyReturnScreen,
                receiptReturnScreen = receiptReturnScreen,
                historyWidgetSelectionMode = historyWidgetSelectionMode,
                selectedWidgetPassengerName = selectedWidgetPassengerName,
                globalNoticeText = globalNoticeText,
                selectedPassengerName = selectedPassengerName,
                selectedHistoryMonth = selectedHistoryMonth,
                homeInitialPage = homeInitialPage,
                developerDonationCount = developerDonationCount,
                onHomePageChange = { homeInitialPage = it.coerceIn(0, 2) },
                onPassengerNameChange = { passengerName = it },
                onStart = { startRide() },
                onStop = { stopRide() },
                onTestDrive = { testDrive131m() },
                onGoHome = {
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
                onGoCustomFareSettings = {
                    if (autoSurchargeEnabled) {
                        autoSurchargeEnabled = false
                        selectedSurcharge = SurchargeMode.Normal
                        farePolicy = activeFarePolicy()
                    }
                    screen = AppScreen.CustomFareSettings
                },
                onGoThemeSettings = { screen = AppScreen.ThemeSettings },
                onSelectRegion = {
                    useCustomFare = false
                    selectedRegion = it
                    if (autoSurchargeEnabled) {
                        selectedSurcharge = autoSurchargeModeFor(it, LocalTime.now())
                    }
                    farePolicy = activeFarePolicy()
                    saveSelectedRegion(it)
                    saveUseCustomFare(false)
                    screen = AppScreen.Home
                },
                onSelectCustomFare = { baseFare, stepFare ->
                    if (autoSurchargeEnabled) {
                        selectedSurcharge = SurchargeMode.Normal
                    }
                    autoSurchargeEnabled = false
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
                onApplySmartSurcharge = {
                    selectedSurcharge = it
                    farePolicy = activeFarePolicy()
                },
                onAutoSurchargeChange = { enabled ->
                    if (!enabled) {
                        autoSurchargeEnabled = false
                    } else if (useCustomFare) {
                        autoSurchargeEnabled = false
                        globalNoticeText = "커스텀 요금제를 선택한 상태에서는 작동하지 않는 기능입니다"
                    } else {
                        autoSurchargeEnabled = true
                        selectedSurcharge = autoSurchargeModeFor(selectedRegion, LocalTime.now())
                        farePolicy = activeFarePolicy()
                    }
                },
                onShowNotice = { globalNoticeText = it },
                onSelectTheme = {
                    selectedTheme = it
                    userSelectedTheme = true
                    saveSelectedTheme(it)
                    screen = AppScreen.Home
                },
                onTollChange = { tollText = it.filter(Char::isDigit) },
                onExtraChange = { extraText = it.filter(Char::isDigit) },
                onSaveRide = { savePendingRide() },
                onCreateManualReceipt = { manualPassengerName, manualDate, manualFare ->
                    createManualReceipt(manualPassengerName, manualDate, manualFare)
                    Toast.makeText(this, "영수증을 추가했어요", Toast.LENGTH_SHORT).show()
                },
                onSelectRecord = {
                    receiptReturnScreen = screen
                    selectedRecord = it
                    screen = AppScreen.Receipt
                },
                onShredRecord = { shredRideRecord(it) },
                onSettleRecord = { settleRideRecord(it) },
                onSettlePassengerRecords = { settlePassengerRideRecords(it) },
                onSettlePassengerMonthRecords = { name, month -> settlePassengerMonthRideRecords(name, month) },
                onGoReceiptBack = { goReceiptBack() },
                onGoWidgetPicker = {
                    historyWidgetSelectionMode = true
                    selectedWidgetPassengerName = null
                    historyReturnScreen = AppScreen.Home
                    screen = AppScreen.History
                },
                onSelectWidgetPassenger = {
                    selectedWidgetPassengerName = it
                    if (isPassengerWidgetAdded(it)) {
                        globalNoticeText = "이미 추가된 항목입니다"
                    }
                },
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
                onDonationCompleted = { recordDeveloperDonation() },
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
        startDistanceKm = null
        testDistanceKm = currentDistanceKm ?: 500f
        startedAtMillis = System.currentTimeMillis()
        elapsedSeconds = 0L
        pendingRide = null
        tollText = ""
        extraText = ""
        selectedRecord = null
        isRunning = true
        requestCurrentDistanceDriven()
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

    private fun requestCurrentDistanceDriven() {
        if (!::vehicle.isInitialized) return

        vehicle.getOdometer().getCurrentDistanceDriven(
            { distance ->
                runOnUiThread {
                    applyDistanceUpdate(distance)
                }
            },
            { e ->
                Log.e("TaxiMeter", "Odometer current distance error", e)
            }
        )
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

    private fun createManualReceipt(
        manualPassengerName: String,
        manualDate: LocalDate,
        manualFare: Int
    ) {
        val cleanedName = manualPassengerName.trim().ifBlank { passengerName.ifBlank { "김카풀" } }
        val cleanedFare = manualFare.coerceAtLeast(0)
        val record = RideRecord(
            id = "${System.currentTimeMillis()}-manual",
            passengerName = cleanedName,
            date = manualDate.format(DateTimeFormatter.ofPattern("yy/MM/dd")),
            endTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
            durationSeconds = 0L,
            distanceKm = 0f,
            rideFare = cleanedFare,
            tollFare = 0,
            extraFare = 0,
            totalFare = cleanedFare,
            themeCode = selectedTheme.code,
            isSettled = false
        )

        rideRecords.add(0, record)
        saveRideRecords(rideRecords)
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

    private fun settlePassengerRideRecords(passengerName: String) {
        var changed = false
        rideRecords.forEachIndexed { index, record ->
            if (record.passengerName.trim() == passengerName && !record.isSettled) {
                val settledRecord = record.copy(isSettled = true)
                rideRecords[index] = settledRecord
                if (selectedRecord?.id == settledRecord.id) {
                    selectedRecord = settledRecord
                }
                changed = true
            }
        }
        if (changed) {
            saveRideRecords(rideRecords)
        }
    }

    private fun settlePassengerMonthRideRecords(passengerName: String, selectedMonth: YearMonth) {
        var changed = false
        rideRecords.forEachIndexed { index, record ->
            val matchesPassenger = record.passengerName.trim() == passengerName
            val matchesMonth = record.recordDateOrNull()?.let { YearMonth.from(it) == selectedMonth } == true
            if (matchesPassenger && matchesMonth && !record.isSettled) {
                val settledRecord = record.copy(isSettled = true)
                rideRecords[index] = settledRecord
                if (selectedRecord?.id == settledRecord.id) {
                    selectedRecord = settledRecord
                }
                changed = true
            }
        }
        if (changed) {
            saveRideRecords(rideRecords)
        }
    }

    private fun resetRideHistory() {
        rideRecords.clear()
        saveRideRecords(rideRecords)
        developerDonationCount = 0
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_DEVELOPER_DONATION_COUNT, developerDonationCount)
            .apply()
        selectedRecord = null
        selectedPassengerName = null
        selectedWidgetPassengerName = null
        historyWidgetSelectionMode = false
    }

    private fun addSelectedPassengerWidget() {
        val name = normalizePassengerWidgetName(selectedWidgetPassengerName.orEmpty())
        if (name.isEmpty()) return

        if (isPassengerWidgetAdded(name)) {
            globalNoticeText = "이미 추가된 항목입니다"
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
        val key = passengerWidgetKey(name)
        passengerWidgets.removeAll { passengerWidgetKey(it) == key }
        savePassengerWidgets(passengerWidgets)
    }

    private fun isPassengerWidgetAdded(name: String): Boolean {
        val key = passengerWidgetKey(name)
        return key.isNotEmpty() && passengerWidgets.any { passengerWidgetKey(it) == key }
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
                    themeCode = item.optString("themeCode", MeshTheme.Aqua.code),
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

    private fun loadDeveloperDonationCount(): Int =
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getInt(KEY_DEVELOPER_DONATION_COUNT, 0)

    private fun recordDeveloperDonation() {
        developerDonationCount += 1
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_DEVELOPER_DONATION_COUNT, developerDonationCount)
            .apply()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        userSelectedTheme = false
        selectedTheme = if (isSystemDarkMode(newConfig)) {
            MeshTheme.Night
        } else {
            loadSelectedTheme(defaultThemeForSystem(isDarkMode = false))
        }
    }

    private fun loadInitialTheme(): MeshTheme =
        if (isSystemDarkMode()) {
            MeshTheme.Night
        } else {
            loadSelectedTheme(defaultThemeForSystem(isDarkMode = false))
        }

    private fun loadSelectedTheme(defaultTheme: MeshTheme): MeshTheme {
        val code = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(KEY_THEME, null)
        return MeshTheme.entries.firstOrNull { it.code == code } ?: defaultTheme
    }

    private fun defaultThemeForSystem(isDarkMode: Boolean): MeshTheme =
        if (isDarkMode) MeshTheme.Night else MeshTheme.Aqua

    private fun isSystemDarkMode(configuration: Configuration = resources.configuration): Boolean =
        (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

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
        private const val KEY_DEVELOPER_DONATION_COUNT = "developer_donation_count"
    }
}
