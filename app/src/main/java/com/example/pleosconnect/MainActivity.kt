package com.example.pleosconnect

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.Velocity
import ai.pleos.playground.vehicle.Vehicle
import ai.pleos.playground.vehicle.listener.DistanceDrivenListener
import java.time.LocalDateTime
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
    private var passengerName by mutableStateOf("김카풀")
    private var selectedRecord by mutableStateOf<RideRecord?>(null)
    private var pendingRide by mutableStateOf<PendingRide?>(null)
    private var tollText by mutableStateOf("")
    private var extraText by mutableStateOf("")
    private val rideRecords = mutableStateListOf<RideRecord>()
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
                selectedRecord = selectedRecord,
                selectedRegion = selectedRegion,
                selectedSurcharge = selectedSurcharge,
                selectedTheme = selectedTheme,
                onPassengerNameChange = { passengerName = it },
                onStart = { startRide() },
                onStop = { stopRide() },
                onTestDrive = { testDrive131m() },
                onGoHome = { screen = AppScreen.Home },
                onGoHistory = { screen = AppScreen.History },
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
                }
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
    selectedRecord: RideRecord?,
    selectedRegion: FareRegion,
    selectedSurcharge: SurchargeMode,
    selectedTheme: MeshTheme,
    onPassengerNameChange: (String) -> Unit,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onTestDrive: () -> Unit,
    onGoHome: () -> Unit,
    onGoHistory: () -> Unit,
    onGoFareSettings: () -> Unit,
    onGoThemeSettings: () -> Unit,
    onSelectRegion: (FareRegion) -> Unit,
    onSelectSurcharge: (SurchargeMode) -> Unit,
    onSelectTheme: (MeshTheme) -> Unit,
    onTollChange: (String) -> Unit,
    onExtraChange: (String) -> Unit,
    onSaveRide: () -> Unit,
    onSelectRecord: (RideRecord) -> Unit
) {
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
                    AppMeshBackground(selectedTheme = selectedTheme)
                    if (screen == AppScreen.Home) {
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
                            onPassengerNameChange = onPassengerNameChange,
                            onStart = onStart,
                            onStop = onStop,
                            onTestDrive = onTestDrive,
                            onGoHistory = onGoHistory,
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
                            when (screen) {
                                AppScreen.EndRide -> EndRideScreen(
                                    passengerName = passengerName,
                                    pendingRide = pendingRide,
                                    tollText = tollText,
                                    extraText = extraText,
                                    onTollChange = onTollChange,
                                    onExtraChange = onExtraChange,
                                    onSaveRide = onSaveRide,
                                    onGoHome = onGoHome
                                )

                                AppScreen.History -> HistoryScreen(
                                    records = records,
                                    onSelectRecord = onSelectRecord,
                                    onGoHome = onGoHome
                                )

                                AppScreen.Receipt -> ReceiptScreen(
                                    record = selectedRecord,
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

    Column(
        modifier = modifier
            .nestedScroll(bounceConnection)
            .graphicsLayer { translationY = bounceOffset.value }
            .verticalScroll(scrollState),
        verticalArrangement = verticalArrangement,
        content = content
    )
}

@Composable
private fun AppMeshBackground(selectedTheme: MeshTheme) {
    Image(
        painter = painterResource(id = selectedTheme.drawableRes),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = 0.32f))
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
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .background(NoticeGreenSoft, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "!",
                        color = NoticeGreen,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )
                }
                Text(text = text, color = DarkText, fontSize = 30.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

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
    onPassengerNameChange: (String) -> Unit,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onTestDrive: () -> Unit,
    onGoHistory: () -> Unit,
    onGoFareSettings: () -> Unit,
    onGoThemeSettings: () -> Unit,
    onSelectSurcharge: (SurchargeMode) -> Unit
) {
    var topNoticeVisible by remember { mutableStateOf(false) }

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
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TopBar()

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = G2RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = GlassWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(text = "환영합니다", color = DarkText, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                        Text(text = "${selectedRegion.displayName} · ${selectedSurcharge.displayName} · ${selectedTheme.displayName}", color = GrayText, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    }
                    OutlinedTextField(
                        value = passengerName,
                        onValueChange = onPassengerNameChange,
                        modifier = Modifier.weight(1.05f),
                        singleLine = true,
                        label = { Text("손님 이름") },
                        shape = G2RoundedCornerShape(16.dp)
                    )
                }
            }

            SurchargeSelector(
                selectedSurcharge = selectedSurcharge,
                enabled = !isRunning,
                onSelectSurcharge = onSelectSurcharge
            )

            MeterCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(438.dp),
                fare = fare,
                isRunning = isRunning,
                tripDistanceKm = tripDistanceKm,
                elapsedSeconds = elapsedSeconds,
                farePolicy = farePolicy,
                selectedTheme = selectedTheme
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = G2RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = GlassWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OrangeButton(
                            text = "주행시작",
                            modifier = Modifier.weight(1f),
                            enabled = !isRunning,
                            onClick = onStart
                        )
                        GhostButton(
                            text = "주행종료",
                            modifier = Modifier.weight(1f),
                            enabled = isRunning,
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

            DriveStatusPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                isRunning = isRunning,
                tripDistanceKm = tripDistanceKm,
                elapsedSeconds = elapsedSeconds
            )
        }

        TopNotice(
            visible = topNoticeVisible,
            text = "주행 중에는 요금제를 변경할 수 없어요"
        )
    }
}

@Composable
private fun MeterCard(
    modifier: Modifier = Modifier,
    fare: Int,
    isRunning: Boolean,
    tripDistanceKm: Float,
    elapsedSeconds: Long,
    farePolicy: FarePolicy,
    selectedTheme: MeshTheme
) {
    Card(
        modifier = modifier,
        shape = G2RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
                    .background(Color.Black.copy(alpha = 0.46f))
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
                    Text(text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm")), color = Color(0xFFF4F7FF), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = farePolicy.label,
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.18f), G2RoundedCornerShape(999.dp))
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
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
                    fontWeight = FontWeight.Bold,
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
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
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
                    Text(
                        text = if (isRunning) "차량 거리 데이터를 받아 요금을 계산 중이에요" else "주행 시작을 누르면 기록을 준비해요",
                        color = GrayText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
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
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
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
                Text(
                    text = if (enabled) "직접 선택" else "주행 중 변경 불가",
                    color = GrayText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SurchargeMode.entries.forEach { mode ->
                    val selected = mode == selectedSurcharge
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        enabled = enabled,
                        onClick = { onSelectSurcharge(mode) },
                        shape = G2RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = if (selected) 3.dp else 0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selected) PrimaryBlue else Panel,
                            contentColor = if (selected) Color.White else DarkText,
                            disabledContainerColor = if (selected) SoftBlue else Panel,
                            disabledContentColor = if (selected) PrimaryBlue else GrayText
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
            elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 5.dp else 2.dp)
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
        subtitle = "메쉬 그라데이션 배경을 선택하세요."
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
        elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 8.dp else 3.dp)
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

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = G2RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(30.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(text = "총 결제 예정 금액", color = GrayText, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "${totalFare.formatWon()}원",
                modifier = Modifier.fillMaxWidth(),
                color = PrimaryBlue,
                fontSize = 68.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.End
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ResultMetricCard(
                    title = "주행 요금",
                    value = "${rideFare.formatWon()}원",
                    modifier = Modifier.weight(1f)
                )
                ResultMetricCard(
                    title = "통행·추가",
                    value = "${(tollFare + extraFare).formatWon()}원",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ResultMetricCard(
            title = "총 주행 시간",
            value = pendingRide?.durationSeconds?.formatDuration() ?: "00:00:00",
            modifier = Modifier.weight(1f)
        )
        ResultMetricCard(
            title = "총 주행 거리",
            value = "%.1fkm".format(pendingRide?.distanceKm ?: 0f),
            modifier = Modifier.weight(1f)
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = G2RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(modifier = Modifier.padding(30.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Text(text = "정산 입력", color = DarkText, fontSize = 28.sp, fontWeight = FontWeight.Black)
            Text(text = "통행료나 별도 합의한 추가요금이 있으면 입력하세요.", color = GrayText, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            FareField(label = "운행요금", value = rideFare.formatWon(), enabled = false, onValueChange = {})
            FareField(label = "통행요금", value = tollText, enabled = true, onValueChange = onTollChange)
            FareField(label = "추가요금", value = extraText, enabled = true, onValueChange = onExtraChange)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = G2RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            GhostButton(text = "돌아가기", modifier = Modifier.weight(1f), onClick = onGoHome)
            OrangeButton(text = "주행 완료", modifier = Modifier.weight(1f), onClick = onSaveRide)
        }
    }
}

@Composable
private fun HistoryScreen(
    records: List<RideRecord>,
    onSelectRecord: (RideRecord) -> Unit,
    onGoHome: () -> Unit
) {
    TopBar()
    PageHeader(
        title = "내역 보기",
        subtitle = "완료한 주행내역을 선택하면 영수증으로 볼 수 있어요."
    )

    if (records.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = G2RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
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
        records.forEach { record ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectRecord(record) },
                shape = G2RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(text = "${record.passengerName} 손님", color = DarkText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = "${record.date} ${record.endTime} · ${"%.1f".format(record.distanceKm)}km", color = GrayText, fontSize = 14.sp)
                    }
                    Text(text = "${record.totalFare.formatWon()}원", color = PrimaryBlue, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    OrangeButton(text = "홈으로", modifier = Modifier.fillMaxWidth(), onClick = onGoHome)
}

@Composable
private fun ReceiptScreen(
    record: RideRecord?,
    onGoHistory: () -> Unit,
    onGoHome: () -> Unit
) {
    TopBar()
    val item = record
    if (item == null) {
        Text(text = "선택된 내역이 없습니다.", color = DarkText, fontSize = 20.sp)
        OrangeButton(text = "홈으로", modifier = Modifier.fillMaxWidth(), onClick = onGoHome)
        return
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(860.dp)
            .padding(horizontal = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 38.dp),
            shape = G2RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp, bottomStart = 8.dp, bottomEnd = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 38.dp, end = 38.dp, top = 26.dp, bottom = 72.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .width(230.dp)
                            .height(18.dp)
                            .background(Color(0xFF5A5F6A), G2RoundedCornerShape(7.dp))
                    )
                    Text(text = "♞", color = Color.Black, fontSize = 32.sp, textAlign = TextAlign.Center)
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
                    Text(text = "THANK YOU", modifier = Modifier.fillMaxWidth(), color = DarkText, fontSize = 22.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
                }
            }
        }
        PinkingCutEdge(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .align(Alignment.BottomCenter)
        )
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
private fun TopBar() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = G2RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "♞",
                    modifier = Modifier
                        .background(SoftBlue, CircleShape)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    color = PrimaryBlue,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )
                Column {
                    Text(text = "카풀미터기", color = DarkText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
private fun FareField(label: String, value: String, enabled: Boolean, onValueChange: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = label, color = DarkText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
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
private fun OrangeButton(text: String, modifier: Modifier = Modifier, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        modifier = modifier.height(62.dp),
        onClick = onClick,
        enabled = enabled,
        shape = G2RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp, pressedElevation = 1.dp),
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
    Button(
        modifier = modifier.height(62.dp),
        onClick = onClick,
        enabled = enabled,
        shape = G2RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 1.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Panel,
            contentColor = DarkText,
            disabledContainerColor = Color(0xFFE7EBF2),
            disabledContentColor = Color(0xFFB8B8B8)
        )
    ) {
        Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ReceiptPrimaryButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        modifier = modifier.height(74.dp),
        onClick = onClick,
        shape = G2RoundedCornerShape(18.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 1.dp),
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
    Button(
        modifier = modifier.height(74.dp),
        onClick = onClick,
        shape = G2RoundedCornerShape(18.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 1.dp),
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
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
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
private fun PinkingCutEdge(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val toothWidth = 32f
        val cutDepth = 24f
        val paperBottom = size.height - cutDepth - 6f

        val paperPath = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, paperBottom)

            var x = size.width
            while (x > 0f) {
                lineTo(max(0f, x - toothWidth / 2f), paperBottom + cutDepth)
                lineTo(max(0f, x - toothWidth), paperBottom)
                x -= toothWidth
            }

            lineTo(0f, 0f)
            close()
        }

        drawPath(path = paperPath, color = Color.White)

        val edgePath = Path().apply {
            moveTo(size.width, paperBottom)
            var x = size.width
            while (x > 0f) {
                lineTo(max(0f, x - toothWidth / 2f), paperBottom + cutDepth)
                lineTo(max(0f, x - toothWidth), paperBottom)
                x -= toothWidth
            }
        }
        drawPath(
            path = edgePath,
            color = Color(0x884A5364),
            style = Stroke(width = 2.5f, cap = StrokeCap.Round)
        )
    }
}

private enum class AppScreen {
    Home,
    EndRide,
    History,
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

private val PrimaryBlue = Color(0xFF315CFF)
private val SoftBlue = Color(0xFFEAF0FF)
private val AppBg = Color(0xFFF6F8FC)
private val Panel = Color(0xFFF0F3F8)
private val Line = Color(0xFFDDE4EE)
private val GlassWhite = Color.White.copy(alpha = 0.88f)
private val Orange = PrimaryBlue
private val Cream = AppBg
private val BlackCard = Color(0xFF111318)
private val DarkText = Color(0xFF111318)
private val GrayText = Color(0xFF68707D)
private val NoticeGreen = Color(0xFF00A86B)
private val NoticeGreenSoft = Color(0xFFE1F8ED)

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
