@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

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

@Composable
internal fun CarpoolMeterApp(
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
    autoSurchargeEnabled: Boolean,
    customBaseFare: Int,
    customStepFare: Int,
    historyReturnScreen: AppScreen,
    receiptReturnScreen: AppScreen,
    historyWidgetSelectionMode: Boolean,
    selectedWidgetPassengerName: String?,
    globalNoticeText: String?,
    selectedPassengerName: String?,
    selectedHistoryMonth: YearMonth,
    homeInitialPage: Int,
    developerDonationCount: Int,
    onHomePageChange: (Int) -> Unit,
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
    onApplySmartSurcharge: (SurchargeMode) -> Unit,
    onAutoSurchargeChange: (Boolean) -> Unit,
    onShowNotice: (String) -> Unit,
    onSelectTheme: (MeshTheme) -> Unit,
    onTollChange: (String) -> Unit,
    onExtraChange: (String) -> Unit,
    onSaveRide: () -> Unit,
    onCreateManualReceipt: (String, LocalDate, Int) -> Unit,
    onSelectRecord: (RideRecord) -> Unit,
    onShredRecord: (RideRecord) -> Unit,
    onSettleRecord: (RideRecord) -> Unit,
    onSettlePassengerRecords: (String) -> Unit,
    onSettlePassengerMonthRecords: (String, YearMonth) -> Unit,
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
    onDonationCompleted: () -> Unit,
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
                                useCustomFare = useCustomFare,
                                autoSurchargeEnabled = autoSurchargeEnabled,
                                initialPage = homeInitialPage,
                                records = records,
                                passengerWidgets = passengerWidgets,
                                onHomePageChange = onHomePageChange,
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
                                onApplySmartSurcharge = onApplySmartSurcharge,
                                onAutoSurchargeChange = onAutoSurchargeChange,
                                onShowNotice = onShowNotice,
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
                                onGoBack = onGoSettingsPage,
                                onGoHome = onGoHome,
                                onDonationCompleted = onDonationCompleted
                            )
                        } else if (targetScreen == AppScreen.History) {
                            HistoryScreen(
                                records = records,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 18.dp, end = 18.dp, top = 44.dp, bottom = 18.dp),
                                onSelectPassenger = onSelectPassenger,
                                onCreateManualReceipt = onCreateManualReceipt,
                                widgetSelectionMode = historyWidgetSelectionMode,
                                selectedWidgetPassengerName = selectedWidgetPassengerName,
                                onSelectWidgetPassenger = onSelectWidgetPassenger,
                                onAddWidgetPassenger = onAddWidgetPassenger,
                                returnButtonText = if (historyReturnScreen == AppScreen.Receipt) "뒤로가기" else "홈으로",
                                onReturnClick = onGoHistoryBack
                            )
                        } else if (targetScreen == AppScreen.PassengerHistory) {
                            PassengerHistoryScreen(
                                passengerName = selectedPassengerName,
                                records = records,
                                selectedMonth = selectedHistoryMonth,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 18.dp, end = 18.dp, top = 44.dp, bottom = 18.dp),
                                onChangeMonth = onChangeHistoryMonth,
                                onSelectRecord = onSelectRecord,
                                onSettleMonth = onSettlePassengerMonthRecords,
                                onGoBack = onGoPassengerHistoryBack,
                                onGoFullHistory = onGoPassengerFullHistory
                            )
                        } else if (targetScreen == AppScreen.PassengerFullHistory) {
                            PassengerFullHistoryScreen(
                                passengerName = selectedPassengerName,
                                records = records,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 18.dp, end = 18.dp, top = 44.dp, bottom = 18.dp),
                                onSelectRecord = onSelectRecord,
                                onSettleAll = onSettlePassengerRecords,
                                onGoBack = onGoPassengerMonthHistory
                            )
                        } else if (targetScreen == AppScreen.AppInfo) {
                            AppInfoScreen(
                                selectedTheme = selectedTheme,
                                modifier = Modifier.fillMaxSize(),
                                onGoBack = onGoSettingsPage,
                                onResetHistory = onResetHistory
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
                                        modifier = Modifier.fillMaxWidth(),
                                        onSelectPassenger = onSelectPassenger,
                                        onCreateManualReceipt = onCreateManualReceipt,
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
                                        modifier = Modifier.fillMaxWidth(),
                                        onChangeMonth = onChangeHistoryMonth,
                                        onSelectRecord = onSelectRecord,
                                        onSettleMonth = onSettlePassengerMonthRecords,
                                        onGoBack = onGoPassengerHistoryBack,
                                        onGoFullHistory = onGoPassengerFullHistory
                                    )

                                    AppScreen.PassengerFullHistory -> PassengerFullHistoryScreen(
                                        passengerName = selectedPassengerName,
                                        records = records,
                                        modifier = Modifier.fillMaxWidth(),
                                        onSelectRecord = onSelectRecord,
                                        onSettleAll = onSettlePassengerRecords,
                                        onGoBack = onGoPassengerMonthHistory
                                    )

                                    AppScreen.Receipt -> ReceiptScreen(
                                        record = selectedRecord,
                                        selectedTheme = selectedTheme,
                                        showDetailHeader = receiptReturnScreen != AppScreen.Home,
                                        onGoHistory = onGoHistory,
                                        onGoBack = onGoReceiptBack,
                                        onGoHome = onGoHome,
                                        onShredRecord = onShredRecord,
                                        onSettleRecord = onSettleRecord
                                    )

                                    AppScreen.FareSettings -> FareSettingsScreen(
                                        selectedRegion = selectedRegion,
                                        onSelectRegion = onSelectRegion,
                                        onGoBack = onGoFareSettings
                                    )

                                    AppScreen.ThemeSettings -> ThemeSettingsScreen(
                                        selectedTheme = selectedTheme,
                                        onSelectTheme = onSelectTheme,
                                        onGoHome = onGoHome
                                    )

                                    AppScreen.Challenges -> ChallengesScreen(
                                        records = records,
                                        developerDonationCount = developerDonationCount,
                                        onGoHome = onGoSettingsPage,
                                        onOpenBadgeBoard = onGoChallengeBadgeBoard
                                    )

                                    AppScreen.ChallengeBadgeBoard -> ChallengeBadgeBoardScreen(
                                        records = records,
                                        developerDonationCount = developerDonationCount,
                                        onGoBack = onGoChallenges
                                    )

                                    AppScreen.AppInfo -> Unit

                                    AppScreen.Donation -> DonationScreen(
                                        onGoBack = onGoSettingsPage,
                                        onGoHome = onGoHome,
                                        onDonationCompleted = onDonationCompleted
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
internal fun BouncyScrollableColumn(
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
internal fun AppMeshBackground(selectedTheme: MeshTheme, overlayAlpha: Float = selectedTheme.backgroundOverlayAlpha()) {
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
internal fun TopNotice(visible: Boolean, text: String) {
    val longNotice = text.length >= 24
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
            colors = CardDefaults.cardColors(containerColor = Color.White),
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
                        symbol = "⚠",
                        size = if (longNotice) 50.dp else 58.dp,
                        fontSize = if (longNotice) 25.sp else 30.sp
                    )
                }
                Text(
                    text = text,
                    modifier = Modifier.weight(1f),
                    color = DarkText,
                    fontSize = if (longNotice) 23.sp else 30.sp,
                    fontWeight = FontWeight.Black,
                    maxLines = 1
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HomeScreen(
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
    useCustomFare: Boolean,
    autoSurchargeEnabled: Boolean,
    initialPage: Int,
    records: List<RideRecord>,
    passengerWidgets: List<String>,
    onHomePageChange: (Int) -> Unit,
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
    onApplySmartSurcharge: (SurchargeMode) -> Unit,
    onAutoSurchargeChange: (Boolean) -> Unit,
    onShowNotice: (String) -> Unit,
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
    var homePagerOverflow by remember { mutableFloatStateOf(0f) }
    val homePagerReleaseOffset = remember { Animatable(0f) }
    val homePagerBounceOffset = if (homePagerOverflow != 0f) {
        rubberBandOffset(homePagerOverflow)
    } else {
        homePagerReleaseOffset.value
    }
    val homePagerBounceConnection = remember(pagerState) {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (source != NestedScrollSource.UserInput) return Offset.Zero

                if (homePagerOverflow != 0f) {
                    val nextOverflow = homePagerOverflow + available.x
                    return if (sign(nextOverflow) == sign(homePagerOverflow)) {
                        homePagerOverflow = nextOverflow
                        Offset(available.x, 0f)
                    } else {
                        val consumedX = -homePagerOverflow
                        homePagerOverflow = 0f
                        Offset(consumedX, 0f)
                    }
                }

                val pageIsSettled = abs(pagerState.currentPageOffsetFraction) < 0.001f
                val pullingRightAtStart = pageIsSettled &&
                    pagerState.currentPage == 0 &&
                    available.x > 0f
                val pullingLeftAtEnd = pageIsSettled &&
                    pagerState.currentPage == 2 &&
                    available.x < 0f

                return if (pullingRightAtStart || pullingLeftAtEnd) {
                    homePagerOverflow += available.x
                    Offset(available.x, 0f)
                } else {
                    Offset.Zero
                }
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                val releaseOffset = rubberBandOffset(homePagerOverflow)
                homePagerOverflow = 0f
                if (releaseOffset != 0f) {
                    homePagerReleaseOffset.snapTo(releaseOffset)
                    homePagerReleaseOffset.animateTo(
                        targetValue = 0f,
                        animationSpec = spring(
                            dampingRatio = 0.86f,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                }
                return Velocity.Zero
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        onHomePageChange(pagerState.currentPage)
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
                modifier = Modifier
                    .weight(1f)
                    .nestedScroll(homePagerBounceConnection)
                    .clipToBounds()
                    .graphicsLayer { translationX = homePagerBounceOffset },
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
                            selectedTheme = selectedTheme,
                            currentPage = pagerState.currentPage,
                            onAddClick = onGoWidgetPicker,
                            onRemovePassengerWidget = onRemovePassengerWidget,
                            onPassengerClick = onOpenPassengerReceiptBoard
                        )

                        else -> HomeQuickSettingsPage(
                            isRunning = isRunning,
                            farePolicy = farePolicy,
                            selectedRegion = selectedRegion,
                            selectedSurcharge = selectedSurcharge,
                            useCustomFare = useCustomFare,
                            autoSurchargeEnabled = autoSurchargeEnabled,
                            currentPage = pagerState.currentPage,
                            onGoFareSettings = onGoFareSettings,
                            onGoThemeSettings = onGoThemeSettings,
                            onGoHistory = onGoHistory,
                            onGoInfo = onGoInfo,
                            onGoDonation = onGoDonation,
                            onGoChallenges = onGoChallenges,
                            onApplySmartSurcharge = onApplySmartSurcharge,
                            onAutoSurchargeChange = onAutoSurchargeChange,
                            onShowNotice = onShowNotice
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
internal fun HomeDashboardPage(
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
internal fun HomeActionPanel(
    modifier: Modifier = Modifier,
    isRunning: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onGoFareSettings: () -> Unit,
    onGoThemeSettings: () -> Unit,
    onGoHistory: () -> Unit
) {
    Box(modifier = modifier) {
        BottomOnlyDropShadow(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalPadding = 18.dp,
            alpha = 0.10f
        )
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = G2RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = GlassWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.36f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val expandedPanel = maxWidth >= 700.dp
                val primaryButtonHeight = if (expandedPanel) 88.dp else 78.dp
                val secondaryButtonHeight = if (expandedPanel) 66.dp else 58.dp
                val primaryButtonFont = if (expandedPanel) 25.sp else 23.sp
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
                            icon = HomeButtonIcon.Play,
                            modifier = Modifier.weight(1f),
                            active = !isRunning,
                            height = primaryButtonHeight,
                            fontSize = primaryButtonFont,
                            onClick = onStart
                        )
                        AnimatedActionButton(
                            text = "주행종료",
                            icon = HomeButtonIcon.Stop,
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
                        CompactGhostButton(text = "요금제 설정", icon = HomeButtonIcon.Gear, modifier = Modifier.weight(1f), height = secondaryButtonHeight, fontSize = secondaryButtonFont, onClick = onGoFareSettings)
                        CompactGhostButton(text = "테마", icon = HomeButtonIcon.Star, modifier = Modifier.weight(1f), height = secondaryButtonHeight, fontSize = secondaryButtonFont, onClick = onGoThemeSettings)
                        CompactGhostButton(text = "정산하기", icon = HomeButtonIcon.Calculator, modifier = Modifier.weight(1f), height = secondaryButtonHeight, fontSize = secondaryButtonFont, onClick = onGoHistory)
                    }
                }
            }
        }
    }
}

@Composable
internal fun BottomOnlyDropShadow(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 12.dp,
    height: Dp = 14.dp,
    yOffset: Dp = 6.dp,
    alpha: Float = 0.12f
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
            .height(height)
            .offset(y = yOffset)
            .blur(10.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = alpha)
                    )
                ),
                shape = G2RoundedCornerShape(999.dp)
            )
    )
}

@Composable
internal fun BottomShadowContainer(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 18.dp,
    height: Dp = 14.dp,
    yOffset: Dp = 6.dp,
    alpha: Float = 0.10f,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        BottomOnlyDropShadow(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalPadding = horizontalPadding,
            height = height,
            yOffset = yOffset,
            alpha = alpha
        )
        content()
    }
}

@Composable
internal fun CompactGhostButton(
    text: String,
    icon: HomeButtonIcon? = null,
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                HomeButtonIconView(
                    icon = it,
                    tint = if (enabled) DarkText else Color(0xFFB8B8B8),
                    iconSize = if (height >= 64.dp) 21.dp else 19.dp
                )
            }
            Text(text = text, fontSize = fontSize, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
        }
    }
}

@Composable
internal fun PassengerWidgetPage(
    records: List<RideRecord>,
    passengerWidgets: List<String>,
    selectedTheme: MeshTheme,
    currentPage: Int,
    onAddClick: () -> Unit,
    onRemovePassengerWidget: (String) -> Unit,
    onPassengerClick: (String) -> Unit
) {
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
    val recentRideRecords = records
        .filter { record ->
            record.recordDateOrNull()?.let { !it.isBefore(recentCutoffDate) } == true
        }
        .sortedWith(
            compareByDescending<RideRecord> { it.recordDateOrNull() }
                .thenByDescending { it.endTime }
        )
    val recentPassengerSummaries = recentRideRecords
        .groupBy { it.passengerName.trim().ifBlank { "이름 없는" } }
        .map { (name, passengerRecords) ->
            val sortedRecords = passengerRecords.sortedWith(
                compareByDescending<RideRecord> { it.recordDateOrNull() ?: LocalDate.MIN }
                    .thenByDescending { it.endTime }
            )
            val latest = sortedRecords.first()
            RecentPassengerSummary(
                passengerName = name,
                rideCount = sortedRecords.size,
                totalFare = sortedRecords.sumOf { it.totalFare },
                totalDistanceKm = sortedRecords.sumOf { it.distanceKm.toDouble() }.toFloat(),
                latestDate = latest.date,
                latestTime = latest.endTime,
                latestRecordDate = latest.recordDateOrNull()
            )
        }
        .sortedWith(
            compareByDescending<RecentPassengerSummary> { it.latestRecordDate ?: LocalDate.MIN }
                .thenByDescending { it.latestTime }
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
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(13.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(G2RoundedCornerShape(26.dp))
                            .background(Color.White)
                            .border(1.dp, Color.White.copy(alpha = 0.62f), G2RoundedCornerShape(26.dp))
                            .padding(horizontal = 24.dp, vertical = 22.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "최근 손님", color = DarkText, fontSize = 34.sp, fontWeight = FontWeight.Black)
                        Text(
                            text = "최근 30일간의 이용 내역과 영수증을 빠르게 열어볼 수 있어요",
                            color = GrayText,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 25.sp
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
                            if (recentPassengerSummaries.isEmpty()) {
                                item {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = G2RoundedCornerShape(26.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.72f)),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                                    ) {
                                        Text(
                                            text = "최근 30일간의 주행 내역이 표시됩니다",
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
                                    items = recentPassengerSummaries,
                                    key = { it.passengerName }
                                ) { summary ->
                                    RecentPassengerSummaryCard(
                                        summary = summary,
                                        onClick = { onPassengerClick(summary.passengerName) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        HomePagerIndicatorSlot(currentPage = currentPage)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun RecentPassengerSummaryCard(
    summary: RecentPassengerSummary,
    onClick: () -> Unit
) {
    val shape = G2RoundedCornerShape(26.dp)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(98.dp)
            .clip(shape)
            .clickable(onClick = onClick),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.72f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 19.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "${summary.passengerName} 손님",
                    color = DarkText,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "총 ${summary.rideCount}회 이용 · 최근 ${summary.latestDate} ${summary.latestTime}",
                    color = GrayText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = "${summary.totalFare.formatWon()}원",
                color = PrimaryBlue,
                fontSize = 25.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
internal fun MonthlyRevenueSummaryCard(
    revenue: Int,
    rideCount: Int,
    distanceKm: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = G2RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.58f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 26.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = "이번 달 누적 수익", color = DarkText, fontSize = 22.sp, fontWeight = FontWeight.Black)
                Text(
                    text = "총 ${rideCount}회 · ${"%.1f".format(distanceKm)}km",
                    color = GrayText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = "${revenue.formatWon()}원",
                modifier = Modifier.padding(start = 18.dp),
                color = PrimaryBlue,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
internal fun ChallengeEntryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    BottomShadowContainer(
        modifier = modifier.fillMaxWidth(),
        horizontalPadding = 20.dp,
        alpha = 0.09f
    ) {
        Button(
            modifier = Modifier.fillMaxSize(),
            onClick = onClick,
            shape = G2RoundedCornerShape(28.dp),
            contentPadding = PaddingValues(0.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.46f)),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.94f),
                contentColor = DarkText
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp, vertical = 22.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_challenge_badge),
                    contentDescription = null,
                    modifier = Modifier.size(62.dp)
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(7.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "도전 과제",
                        color = DarkText,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "카풀 기록에 따라 배지와 완수율이 자동으로 올라가요",
                        color = GrayText,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}


@Composable
internal fun PassengerWidgetSlot(
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
internal fun ConfirmDeleteWidgetDialog(
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
                    text = "삭제하시겠습니까?",
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
internal fun HomeOverviewPage(
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
internal fun HomeQuickSettingsPage(
    isRunning: Boolean,
    farePolicy: FarePolicy,
    selectedRegion: FareRegion,
    selectedSurcharge: SurchargeMode,
    useCustomFare: Boolean,
    autoSurchargeEnabled: Boolean,
    currentPage: Int,
    onGoFareSettings: () -> Unit,
    onGoThemeSettings: () -> Unit,
    onGoHistory: () -> Unit,
    onGoInfo: () -> Unit,
    onGoDonation: () -> Unit,
    onGoChallenges: () -> Unit,
    onApplySmartSurcharge: (SurchargeMode) -> Unit,
    onAutoSurchargeChange: (Boolean) -> Unit,
    onShowNotice: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(HomePagerSectionSpacing)
    ) {
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            val sectionGap = 12.dp
            val expandedPanel = maxWidth >= 700.dp
            val summaryCardHeight = if (expandedPanel) 238.dp else 220.dp
            val fareRuleCardHeight = if (expandedPanel) 224.dp else 208.dp

            BouncyScrollableColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(sectionGap)
            ) {
                BottomShadowContainer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(summaryCardHeight),
                    horizontalPadding = 20.dp,
                    alpha = 0.08f
                ) {
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        shape = G2RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = GlassWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp, vertical = 22.dp),
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
                }

                BottomShadowContainer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(fareRuleCardHeight),
                    horizontalPadding = 20.dp,
                    alpha = 0.08f
                ) {
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        shape = G2RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(22.dp),
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
                }

                ChallengeEntryButton(
                    modifier = Modifier.height(QuickSettingsReceiptHeight),
                    onClick = onGoChallenges
                )

                SmartSettingsCard(
                    modifier = Modifier.height(QuickSettingsSecondaryHeight),
                    autoSurchargeEnabled = autoSurchargeEnabled,
                    onAutoSurchargeChange = { enabled ->
                        if (enabled && useCustomFare) {
                            onShowNotice("커스텀 요금제를 선택한 상태에서는 작동하지 않는 기능입니다")
                        } else {
                            onAutoSurchargeChange(enabled)
                        }
                    }
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
        }

        HomePagerIndicatorSlot(currentPage = currentPage)
    }
}

@Composable
internal fun QuickReceiptSettingsCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    BottomShadowContainer(
        modifier = modifier.fillMaxWidth(),
        horizontalPadding = 20.dp,
        alpha = 0.08f
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = G2RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.96f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.46f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Text(
                        text = "영수증 빠른 추가",
                        color = DarkText,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "놓친 주행도 장부에 바로 기록할 수 있어요",
                        color = GrayText,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 22.sp
                    )
                }
                OrangeButton(
                    text = "설정하기",
                    modifier = Modifier.width(150.dp),
                    height = 62.dp,
                    onClick = onClick
                )
            }
        }
    }
}


@Composable
internal fun SettingsInfoButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    BottomShadowContainer(
        modifier = modifier.height(QuickSettingsInfoHeight),
        horizontalPadding = 22.dp,
        alpha = 0.08f
    ) {
        Button(
            modifier = Modifier.fillMaxSize(),
            onClick = onClick,
            shape = G2RoundedCornerShape(24.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Panel,
                contentColor = DarkText
            )
        ) {
            Text(text = "정보", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
internal fun SupportSnackButton(snack: SupportSnack) {
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
internal fun SmartSettingsCard(
    modifier: Modifier = Modifier,
    autoSurchargeEnabled: Boolean,
    onAutoSurchargeChange: (Boolean) -> Unit
) {
    BottomShadowContainer(
        modifier = modifier.fillMaxWidth(),
        horizontalPadding = 20.dp,
        alpha = 0.08f
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = G2RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlassIconContainer(
                    symbol = "⏰",
                    size = 62.dp,
                    fontSize = 34.sp
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "스마트 설정", color = DarkText, fontSize = 29.sp, fontWeight = FontWeight.Black)
                    Text(
                        text = "지역 및 시간 기반 자동 할증",
                        color = DarkText,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "선택한 지역과 현재 시간을 인식해 할증 요금을 자동으로 적용합니다",
                        color = GrayText,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 22.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .width(122.dp)
                        .height(92.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Switch(
                        checked = autoSurchargeEnabled,
                        onCheckedChange = onAutoSurchargeChange,
                        modifier = Modifier.graphicsLayer {
                            scaleX = 1.58f
                            scaleY = 1.58f
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
}

@Composable
internal fun HomePagerIndicatorSlot(
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
internal fun HomePagerIndicator(
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
internal fun PassengerEditDialogOverlay(
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
internal fun MeterCard(
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
internal fun MeterStatusChip(
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
internal fun MeterMetricItem(
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
internal fun DriveStatusPanel(
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
internal fun DashboardTile(
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
internal fun SurchargeSelector(
    modifier: Modifier = Modifier,
    selectedSurcharge: SurchargeMode,
    enabled: Boolean,
    onSelectSurcharge: (SurchargeMode) -> Unit
) {
    Box(modifier = modifier) {
        BottomOnlyDropShadow(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalPadding = 18.dp,
            alpha = 0.08f
        )
        Card(
            modifier = Modifier.fillMaxSize(),
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
}



