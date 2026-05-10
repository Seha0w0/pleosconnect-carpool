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

internal fun EndRideScreen(

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

internal fun HistoryScreen(

    records: List<RideRecord>,

    modifier: Modifier = Modifier,

    onSelectPassenger: (String) -> Unit,

    onCreateManualReceipt: (String, LocalDate, Int) -> Unit,

    widgetSelectionMode: Boolean,

    selectedWidgetPassengerName: String?,

    onSelectWidgetPassenger: (String) -> Unit,

    onAddWidgetPassenger: () -> Unit,

    returnButtonText: String,

    onReturnClick: () -> Unit

) {

    var manualReceiptVisible by remember { mutableStateOf(false) }

    val passengerOptions = remember(records) {

        records

            .sortedWith(compareByDescending<RideRecord> { it.recordDateOrNull() }.thenByDescending { it.endTime })

            .map { it.passengerName.trim().ifBlank { "김카풀" } }

            .distinct()

            .take(3)

            .ifEmpty { listOf("김카풀") }

    }

    val groupedRecords = records

        .groupBy { it.passengerName.trim() }

        .toSortedMap()

        .mapValues { entry -> entry.value.sortedWith(compareByDescending<RideRecord> { it.recordDateOrNull() }.thenByDescending { it.endTime }) }



    Box(modifier = modifier) {

        LazyColumn(

            modifier = Modifier.fillMaxSize(),

            verticalArrangement = Arrangement.spacedBy(14.dp),

            contentPadding = PaddingValues(bottom = 6.dp)

        ) {

            item {

                StandaloneDetailHeader(

                    title = "내역 보기",

                    subtitle = if (widgetSelectionMode) "위젯에 추가할 손님을 선택하세요." else "완료한 주행내역을 선택하면 영수증으로 볼 수 있어요.",

                    onGoBack = onReturnClick

                )

            }



            if (!widgetSelectionMode) {

                item {

                    OrangeButton(

                        text = "영수증 만들기",

                        modifier = Modifier.fillMaxWidth(),

                        onClick = { manualReceiptVisible = true }

                    )

                }

            }



            if (groupedRecords.isEmpty()) {

                item {

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

                }

            } else {

                items(

                    items = groupedRecords.entries.toList(),

                    key = { it.key }

                ) { (name, passengerRecords) ->

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

                item {

                    OrangeButton(

                        text = "추가",

                        modifier = Modifier.fillMaxWidth(),

                        onClick = onAddWidgetPassenger

                    )

                }

            }

        }



        ManualReceiptDialog(

            visible = manualReceiptVisible,

            passengerOptions = passengerOptions,

            onDismiss = { manualReceiptVisible = false },

            onConfirm = { selectedName, selectedDate, selectedFare ->

                onCreateManualReceipt(selectedName, selectedDate, selectedFare)

                manualReceiptVisible = false

            }

        )

    }

}



@Composable

internal fun ManualReceiptDialog(

    visible: Boolean,

    passengerOptions: List<String>,

    onDismiss: () -> Unit,

    onConfirm: (String, LocalDate, Int) -> Unit

) {

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    var datePickerVisible by remember { mutableStateOf(false) }

    var passengerName by remember(passengerOptions) { mutableStateOf(passengerOptions.firstOrNull().orEmpty()) }

    var fareText by remember { mutableStateOf("0") }

    val fareValue = fareText.toIntOrNull() ?: 0

    val canConfirm = passengerName.trim().isNotEmpty() && fareValue > 0

    val quickFares = listOf(1000, 3000, 5000, 10000, 20000, 50000)



    AnimatedVisibility(

        visible = visible,

        modifier = Modifier

            .fillMaxSize()

            .zIndex(100f),

        enter = fadeIn(animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing)),

        exit = fadeOut(animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing))

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

            Box(

                modifier = Modifier

                    .fillMaxSize()

                    .background(Color.Black.copy(alpha = 0.46f))

                    .clickable(onClick = onDismiss),

                contentAlignment = Alignment.Center

            ) {

                Card(

                    modifier = Modifier

                        .fillMaxWidth()

                        .padding(horizontal = 22.dp)

                        .clickable(onClick = {}),

                    shape = G2RoundedCornerShape(36.dp),

                    colors = CardDefaults.cardColors(containerColor = Color.White),

                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.72f)),

                    elevation = CardDefaults.cardElevation(defaultElevation = 14.dp)

                ) {

                    Column(

                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 30.dp),

                        verticalArrangement = Arrangement.spacedBy(20.dp)

                    ) {

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                            Text(

                                text = "영수증 만들기",

                                color = DarkText,

                                fontSize = 32.sp,

                                fontWeight = FontWeight.Black

                            )

                            Text(

                                text = "날짜, 금액, 손님 이름을 선택하면 내역에 바로 추가됩니다.",

                                color = GrayText,

                                fontSize = 18.sp,

                                fontWeight = FontWeight.Medium,

                                lineHeight = 25.sp

                            )

                        }



                        ManualReceiptSection(title = "날짜 선택") {

                            Row(

                                modifier = Modifier.fillMaxWidth(),

                                horizontalArrangement = Arrangement.spacedBy(10.dp),

                                verticalAlignment = Alignment.CenterVertically

                            ) {

                                GhostButton(

                                    text = "전날",

                                    modifier = Modifier.weight(0.85f),

                                    height = 66.dp,

                                    fontSize = 17.sp,

                                    onClick = { selectedDate = selectedDate.minusDays(1) }

                                )

                                Text(

                                    text = selectedDate.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일")),

                                    modifier = Modifier

                                        .weight(1.7f)

                                        .height(66.dp)

                                        .background(Panel, G2RoundedCornerShape(999.dp))

                                        .clickable { datePickerVisible = true }

                                        .padding(vertical = 20.dp),

                                    color = DarkText,

                                    fontSize = 19.sp,

                                    fontWeight = FontWeight.Black,

                                    textAlign = TextAlign.Center

                                )

                                GhostButton(

                                    text = "다음날",

                                    modifier = Modifier.weight(0.85f),

                                    height = 66.dp,

                                    fontSize = 17.sp,

                                    onClick = { selectedDate = selectedDate.plusDays(1) }

                                )

                            }

                        }



                        ManualReceiptSection(title = "금액 선택") {

                            OutlinedTextField(

                                value = fareText,

                                onValueChange = { fareText = it.filter(Char::isDigit).take(9) },

                                modifier = Modifier

                                    .fillMaxWidth()

                                    .height(78.dp),

                                singleLine = true,

                                label = { Text("누적 금액", fontSize = 16.sp) },

                                suffix = { Text("원", fontSize = 18.sp, fontWeight = FontWeight.Bold) },

                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                                shape = G2RoundedCornerShape(22.dp),

                                textStyle = MaterialTheme.typography.bodyLarge.copy(

                                    fontSize = 25.sp,

                                    fontWeight = FontWeight.Black,

                                    color = DarkText

                                )

                            )

                            ManualReceiptChipGrid(

                                items = quickFares.map { "+${it.formatWon()}원" },

                                selectedIndex = -1,

                                onSelect = { index ->

                                    val nextFare = (fareText.toIntOrNull() ?: 0) + quickFares[index]

                                    fareText = nextFare.coerceAtMost(999999999).toString()

                                }

                            )

                        }



                        ManualReceiptSection(title = "손님 이름 선택") {

                            OutlinedTextField(

                                value = passengerName,

                                onValueChange = { passengerName = it.take(12) },

                                modifier = Modifier

                                    .fillMaxWidth()

                                    .height(78.dp),

                                singleLine = true,

                                label = { Text("손님 이름", fontSize = 16.sp) },

                                shape = G2RoundedCornerShape(22.dp),

                                textStyle = MaterialTheme.typography.bodyLarge.copy(

                                    fontSize = 24.sp,

                                    fontWeight = FontWeight.Bold,

                                    color = DarkText

                                )

                            )

                            ManualReceiptChipGrid(

                                items = passengerOptions.take(3),

                                selectedIndex = passengerOptions.take(3).indexOf(passengerName),

                                onSelect = { index -> passengerName = passengerOptions[index] }

                            )

                        }



                        Row(

                            modifier = Modifier.fillMaxWidth(),

                            horizontalArrangement = Arrangement.spacedBy(12.dp)

                        ) {

                            GhostButton(

                                text = "취소",

                                modifier = Modifier.weight(1f),

                                height = 72.dp,

                                fontSize = 20.sp,

                                onClick = onDismiss

                            )

                            OrangeButton(

                                text = "확인",

                                modifier = Modifier.weight(1f),

                                enabled = canConfirm,

                                height = 72.dp,

                                onClick = {

                                    onConfirm(passengerName.trim(), selectedDate, fareValue)

                                }

                            )

                        }

                    }

                }

            }

        }

    }



    ManualDatePickerDialog(

        visible = datePickerVisible,

        initialDate = selectedDate,

        onDismiss = { datePickerVisible = false },

        onConfirm = { newDate ->

            selectedDate = newDate

            datePickerVisible = false

        }

    )

}



@Composable

internal fun ManualDatePickerDialog(

    visible: Boolean,

    initialDate: LocalDate,

    onDismiss: () -> Unit,

    onConfirm: (LocalDate) -> Unit

) {

    if (!visible) return



    var workingDate by remember(initialDate) { mutableStateOf(initialDate) }



    Dialog(

        onDismissRequest = onDismiss,

        properties = DialogProperties(usePlatformDefaultWidth = false)

    ) {

        AnimatedVisibility(

            visible = visible,

            enter = fadeIn(animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing)),

            exit = fadeOut(animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing))

        ) {

            Box(

                modifier = Modifier

                    .fillMaxSize()

                    .background(Color.Black.copy(alpha = 0.52f))

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

                            .padding(horizontal = 38.dp)

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

                                    text = "날짜 자세히 설정",

                                    color = DarkText,

                                    fontSize = 30.sp,

                                    fontWeight = FontWeight.Black

                                )

                                Text(

                                    text = workingDate.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일")),

                                    color = PrimaryBlue,

                                    fontSize = 24.sp,

                                    fontWeight = FontWeight.Black

                                )

                            }



                            DateAdjustRow(

                                label = "연도",

                                value = "${workingDate.year}년",

                                onMinus = { workingDate = workingDate.minusYears(1) },

                                onPlus = { workingDate = workingDate.plusYears(1) }

                            )

                            DateAdjustRow(

                                label = "월",

                                value = "${workingDate.monthValue}월",

                                onMinus = { workingDate = workingDate.minusMonths(1) },

                                onPlus = { workingDate = workingDate.plusMonths(1) }

                            )

                            DateAdjustRow(

                                label = "일",

                                value = "${workingDate.dayOfMonth}일",

                                onMinus = { workingDate = workingDate.minusDays(1) },

                                onPlus = { workingDate = workingDate.plusDays(1) }

                            )



                            Row(

                                modifier = Modifier.fillMaxWidth(),

                                horizontalArrangement = Arrangement.spacedBy(12.dp)

                            ) {

                                GhostButton(

                                    text = "취소",

                                    modifier = Modifier.weight(1f),

                                    height = 60.dp,

                                    onClick = onDismiss

                                )

                                OrangeButton(

                                    text = "적용",

                                    modifier = Modifier.weight(1f),

                                    height = 60.dp,

                                    onClick = { onConfirm(workingDate) }

                                )

                            }

                        }

                    }

                }

            }

        }

    }

}



@Composable

internal fun DateAdjustRow(

    label: String,

    value: String,

    onMinus: () -> Unit,

    onPlus: () -> Unit

) {

    Row(

        modifier = Modifier

            .fillMaxWidth()

            .background(Panel, G2RoundedCornerShape(24.dp))

            .padding(12.dp),

        verticalAlignment = Alignment.CenterVertically,

        horizontalArrangement = Arrangement.spacedBy(12.dp)

    ) {

        Text(

            text = label,

            modifier = Modifier.weight(0.9f),

            color = GrayText,

            fontSize = 17.sp,

            fontWeight = FontWeight.Black

        )

        GhostButton(

            text = "-",

            modifier = Modifier.weight(0.75f),

            height = 54.dp,

            fontSize = 24.sp,

            onClick = onMinus

        )

        Text(

            text = value,

            modifier = Modifier.weight(1.35f),

            color = DarkText,

            fontSize = 24.sp,

            fontWeight = FontWeight.Black,

            textAlign = TextAlign.Center

        )

        GhostButton(

            text = "+",

            modifier = Modifier.weight(0.75f),

            height = 54.dp,

            fontSize = 24.sp,

            onClick = onPlus

        )

    }

}



@Composable

internal fun ManualReceiptSection(

    title: String,

    content: @Composable ColumnScope.() -> Unit

) {

    Column(

        modifier = Modifier

            .fillMaxWidth()

            .background(Panel, G2RoundedCornerShape(24.dp))

            .padding(16.dp),

        verticalArrangement = Arrangement.spacedBy(12.dp)

    ) {

        Text(text = title, color = DarkText, fontSize = 19.sp, fontWeight = FontWeight.Black)

        content()

    }

}



@Composable

internal fun ManualReceiptChipGrid(

    items: List<String>,

    selectedIndex: Int,

    onSelect: (Int) -> Unit

) {

    if (items.isEmpty()) return



    items.chunked(3).forEach { rowItems ->

        Row(

            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement = Arrangement.spacedBy(8.dp)

        ) {

            rowItems.forEachIndexed { rowIndex, label ->

                val index = items.indexOf(label).takeIf { it >= 0 } ?: rowIndex

                Button(

                    modifier = Modifier

                        .weight(1f)

                        .height(56.dp),

                    onClick = { onSelect(index) },

                    shape = G2RoundedCornerShape(999.dp),

                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),

                    colors = ButtonDefaults.buttonColors(

                        containerColor = if (index == selectedIndex) PrimaryBlue else Color.White,

                        contentColor = if (index == selectedIndex) Color.White else DarkText

                    )

                ) {

                    Text(text = label, fontSize = 15.sp, fontWeight = FontWeight.Black, maxLines = 1)

                }

            }

            repeat(3 - rowItems.size) {

                Spacer(modifier = Modifier.weight(1f))

            }

        }

    }

}



@Composable

internal fun PassengerHistoryScreen(

    passengerName: String?,

    records: List<RideRecord>,

    selectedMonth: YearMonth,

    modifier: Modifier = Modifier,

    onChangeMonth: (YearMonth) -> Unit,

    onSelectRecord: (RideRecord) -> Unit,

    onSettleMonth: (String, YearMonth) -> Unit,

    onGoBack: () -> Unit,

    onGoFullHistory: () -> Unit

) {

    val name = passengerName ?: return

    var selectedDate by remember(name, selectedMonth) { mutableStateOf<LocalDate?>(null) }

    var monthPickerVisible by remember { mutableStateOf(false) }

    var settlingMonthRequest by remember { mutableStateOf<YearMonth?>(null) }

    var settlingMonthOverlayVisible by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    val scope = rememberCoroutineScope()

    val bounceOffset = remember { Animatable(0f) }

    val passengerRecords = records

        .filter { it.passengerName.trim() == name }

        .sortedWith(compareByDescending<RideRecord> { it.recordDateOrNull() }.thenByDescending { it.endTime })

    val monthRecords = passengerRecords.filter { record ->

        record.recordDateOrNull()?.let { YearMonth.from(it) == selectedMonth } == true

    }

    val monthTotalFare = monthRecords.sumOf { it.totalFare }

    val monthSettledFare = monthRecords.filter { it.isSettled }.sumOf { it.totalFare }

    val monthUnsettledFare = monthTotalFare - monthSettledFare

    val hasMonthUnsettledRecords = monthRecords.any { !it.isSettled }

    val visibleRideRecords = selectedDate?.let { date ->

        monthRecords.filter { it.recordDateOrNull() == date }

    } ?: monthRecords

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



    LaunchedEffect(settlingMonthRequest) {

        val requestedMonth = settlingMonthRequest ?: return@LaunchedEffect

        settlingMonthOverlayVisible = true

        kotlinx.coroutines.delay(1050L)

        onSettleMonth(name, requestedMonth)

        settlingMonthOverlayVisible = false

        kotlinx.coroutines.delay(300L)

        settlingMonthRequest = null

    }



    Box(modifier = modifier) {

        Box(

            modifier = Modifier

                .fillMaxSize()

                .nestedScroll(bounceConnection)

                .clipToBounds()

        ) {

            LazyColumn(

                state = listState,

                modifier = Modifier

                    .fillMaxSize()

                    .graphicsLayer { translationY = bounceOffset.value },

                verticalArrangement = Arrangement.spacedBy(14.dp),

                contentPadding = PaddingValues(bottom = 6.dp)

            ) {

                item {

                    StandaloneDetailHeader(

                        title = "$name 손님",

                        subtitle = "언제 카풀을 이용했고, 얼마가 부과됐는지 알 수 있어요.",

                        onGoBack = onGoBack

                    )

                }

                item {

                    MonthlySettlementSummary(

                        month = selectedMonth.monthValue,

                        totalFare = monthTotalFare,

                        settledFare = monthSettledFare,

                        unsettledFare = monthUnsettledFare

                    )

                }

                item {

                    PassengerCalendarCard(

                        selectedMonth = selectedMonth,

                        records = passengerRecords,

                        selectedDate = selectedDate,

                        onPreviousMonth = { onChangeMonth(selectedMonth.minusMonths(1)) },

                        onNextMonth = { onChangeMonth(selectedMonth.plusMonths(1)) },

                        onMonthTitleClick = { monthPickerVisible = true },

                        onSelectDate = { selectedDate = it }

                    )

                }

                item {

                    Row(

                        modifier = Modifier.fillMaxWidth(),

                        horizontalArrangement = Arrangement.spacedBy(10.dp)

                    ) {

                        OrangeButton(

                            text = "${selectedMonth.monthValue}월 전체 정산하기",

                            modifier = Modifier.weight(1f),

                            enabled = hasMonthUnsettledRecords && settlingMonthRequest == null,

                            height = 58.dp,

                            onClick = { settlingMonthRequest = selectedMonth }

                        )

                        OrangeButton(

                            text = "전체 목록",

                            modifier = Modifier.weight(1f),

                            height = 58.dp,

                            onClick = onGoFullHistory

                        )

                    }

                }

                item {

                    Text(

                        text = "이용 목록",

                        color = DarkText,

                        fontSize = 24.sp,

                        fontWeight = FontWeight.Black

                    )

                }

                if (visibleRideRecords.isEmpty()) {

                    item {

                        Card(

                            modifier = Modifier.fillMaxWidth(),

                            shape = G2RoundedCornerShape(22.dp),

                            colors = CardDefaults.cardColors(containerColor = Color.White),

                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)

                        ) {

                            Text(

                                text = if (selectedDate == null) "이번 달 이용 내역이 없습니다." else "선택한 날짜의 이용 내역이 없습니다.",

                                modifier = Modifier.padding(28.dp),

                                color = GrayText,

                                fontSize = 18.sp,

                                textAlign = TextAlign.Center

                            )

                        }

                    }

                } else {

                    items(

                        items = visibleRideRecords,

                        key = { it.id }

                    ) { record ->

                        PassengerRideRecordCard(record = record, onClick = { onSelectRecord(record) })

                    }

                }

            }

        }



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



        MonthSettlementLoadingOverlay(

            visible = settlingMonthOverlayVisible,

            month = settlingMonthRequest?.monthValue ?: selectedMonth.monthValue

        )

    }

}



@Composable

internal fun MonthSettlementLoadingOverlay(

    visible: Boolean,

    month: Int

) {

    var keepDialogMounted by remember { mutableStateOf(false) }



    LaunchedEffect(visible) {

        if (visible) {

            keepDialogMounted = true

        } else if (keepDialogMounted) {

            kotlinx.coroutines.delay(280L)

            keepDialogMounted = false

        }

    }



    if (!keepDialogMounted) return



    val scrimAlpha by animateFloatAsState(

        targetValue = if (visible) 0.36f else 0f,

        animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing),

        label = "monthSettlementScrimAlpha"

    )



    Dialog(

        onDismissRequest = {},

        properties = DialogProperties(

            usePlatformDefaultWidth = false,

            dismissOnBackPress = false,

            dismissOnClickOutside = false

        )

    ) {

        Box(

            modifier = Modifier

                .fillMaxSize()

                .background(Color.Black.copy(alpha = scrimAlpha)),

            contentAlignment = Alignment.Center

        ) {

            AnimatedVisibility(

                visible = visible,

                enter = fadeIn(animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing)) +

                    scaleIn(

                        initialScale = 0.96f,

                        animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing)

                    ),

                exit = fadeOut(animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing)) +

                    scaleOut(

                        targetScale = 0.98f,

                        animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing)

                    )

            ) {

                    Card(

                        modifier = Modifier

                            .fillMaxWidth()

                            .padding(horizontal = 44.dp),

                        shape = G2RoundedCornerShape(34.dp),

                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.96f)),

                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.72f)),

                        elevation = CardDefaults.cardElevation(defaultElevation = 14.dp)

                    ) {

                        Column(

                            modifier = Modifier

                                .fillMaxWidth()

                                .padding(horizontal = 32.dp, vertical = 34.dp),

                            horizontalAlignment = Alignment.CenterHorizontally,

                            verticalArrangement = Arrangement.spacedBy(18.dp)

                        ) {

                            CircularProgressIndicator(

                                color = PrimaryBlue,

                                strokeWidth = 5.dp,

                                modifier = Modifier.size(58.dp)

                            )

                            Text(

                                text = "${month}월 내역을 정산하고 있어요",

                                modifier = Modifier.fillMaxWidth(),

                                color = DarkText,

                                fontSize = 25.sp,

                                fontWeight = FontWeight.Black,

                                textAlign = TextAlign.Center

                            )

                            Text(

                                text = "잠시만 기다려 주세요.",

                                modifier = Modifier.fillMaxWidth(),

                                color = GrayText,

                                fontSize = 18.sp,

                                fontWeight = FontWeight.Medium,

                                textAlign = TextAlign.Center

                            )

                        }

                    }

            }

        }

    }

}



@Composable

internal fun MonthlySettlementSummary(

    month: Int,

    totalFare: Int,

    settledFare: Int,

    unsettledFare: Int

) {

    Card(

        modifier = Modifier

            .fillMaxWidth()

            .height(126.dp),

        shape = G2RoundedCornerShape(28.dp),

        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.96f)),

        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.46f)),

        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)

    ) {

        Row(

            modifier = Modifier

                .fillMaxSize()

                .padding(horizontal = 12.dp, vertical = 12.dp),

            horizontalArrangement = Arrangement.spacedBy(8.dp),

            verticalAlignment = Alignment.CenterVertically

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

internal fun MonthlySettlementItem(

    title: String,

    value: String,

    modifier: Modifier = Modifier,

    valueColor: Color = DarkText

) {

    Column(

        modifier = modifier

            .fillMaxSize()

            .padding(horizontal = 4.dp, vertical = 4.dp),

        verticalArrangement = Arrangement.spacedBy(10.dp)

    ) {

        Text(

            text = title,

            modifier = Modifier

                .fillMaxWidth()

                .padding(horizontal = 8.dp),

            color = GrayText,

            fontSize = 20.sp,

            fontWeight = FontWeight.Bold,

            lineHeight = 24.sp,

            maxLines = 2

        )

        Box(

            modifier = Modifier

                .fillMaxWidth()

                .weight(1f)

                .background(Panel, G2RoundedCornerShape(22.dp))

                .padding(horizontal = 14.dp, vertical = 10.dp),

            contentAlignment = Alignment.CenterStart

        ) {

            Text(

                text = value,

                color = valueColor,

                fontSize = 32.sp,

                fontWeight = FontWeight.Black,

                lineHeight = 35.sp,

                maxLines = 1

            )

        }

    }

}



@Composable

internal fun PassengerFullHistoryScreen(

    passengerName: String?,

    records: List<RideRecord>,

    modifier: Modifier = Modifier,

    onSelectRecord: (RideRecord) -> Unit,

    onSettleAll: (String) -> Unit,

    onGoBack: () -> Unit

) {

    val name = passengerName ?: return

    var settleAllDialogVisible by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    val scope = rememberCoroutineScope()

    val bounceOffset = remember { Animatable(0f) }

    val passengerRecords = records

        .filter { it.passengerName.trim() == name }

        .sortedWith(compareByDescending<RideRecord> { it.recordDateOrNull() }.thenByDescending { it.endTime })

    val totalFare = passengerRecords.sumOf { it.totalFare }

    val unsettledFare = passengerRecords.filter { !it.isSettled }.sumOf { it.totalFare }

    val hasUnsettledRecords = passengerRecords.any { !it.isSettled }

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



    Box(modifier = modifier) {

        Box(

            modifier = Modifier

                .fillMaxSize()

                .nestedScroll(bounceConnection)

                .clipToBounds()

        ) {

            LazyColumn(

                state = listState,

                modifier = Modifier

                    .fillMaxSize()

                    .graphicsLayer { translationY = bounceOffset.value },

                verticalArrangement = Arrangement.spacedBy(14.dp),

                contentPadding = PaddingValues(bottom = 6.dp)

            ) {

                item {

                    StandaloneDetailHeader(

                        title = "$name 손님 전체 목록",

                        subtitle = "이 손님의 모든 카풀 이용 기록입니다.",

                        onGoBack = onGoBack

                    )

                }

                item {

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

                                SummaryInfoCard(

                                    title = "정산되지 못한 금액",

                                    value = "${unsettledFare.formatWon()}원",

                                    valueColor = if (unsettledFare > 0) Color(0xFF424A56) else GrayText,

                                    modifier = Modifier.weight(1f)

                                )

                            }

                        }

                    }

                }

                item {

                    OrangeButton(

                        text = "전체 정산하기",

                        modifier = Modifier.fillMaxWidth(),

                        enabled = hasUnsettledRecords,

                        height = 62.dp,

                        onClick = { settleAllDialogVisible = true }

                    )

                }

                item {

                    Text(

                        text = "이용 목록",

                        color = DarkText,

                        fontSize = 24.sp,

                        fontWeight = FontWeight.Black

                    )

                }

                if (passengerRecords.isEmpty()) {

                    item {

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

                    }

                } else {

                    items(

                        items = passengerRecords,

                        key = { it.id }

                    ) { record ->

                        PassengerRideRecordCard(record = record, onClick = { onSelectRecord(record) })

                    }

                }

            }

        }



        SettleAllDialog(

            visible = settleAllDialogVisible,

            onDismiss = { settleAllDialogVisible = false },

            onSettleAll = {

                settleAllDialogVisible = false

                onSettleAll(name)

            }

        )

    }

}



@Composable

internal fun SummaryInfoCard(

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

internal fun PassengerCalendarCard(

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

internal fun CalendarDayCell(

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

internal fun MonthPickerDialog(

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

internal fun PassengerRideRecordCard(record: RideRecord, onClick: () -> Unit) {

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

internal fun SettledOverlay(

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

internal fun PassengerReceiptBoardScreen(

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

            StandaloneDetailHeader(

                title = "$name 손님",

                subtitle = "날짜별 영수증을 자유롭게 배치해보세요.",

                onGoBack = onGoBack

            )



            PassengerReceiptBoardSummary(

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

            val boardStates = remember(

                passengerRecords.joinToString(separator = "|") { "${it.id}:${it.isSettled}" },

                columnCount,

                boardContentHeight

            ) {

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

internal fun PassengerReceiptBoardSummary(

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

            Text(text = "총 이용 금액", color = GrayText, fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Column(

                horizontalAlignment = Alignment.End,

                verticalArrangement = Arrangement.spacedBy(4.dp)

            ) {

                Text(text = "${totalFare.formatWon()}원", color = PrimaryBlue, fontSize = 25.sp, fontWeight = FontWeight.Black)

            }

        }

    }

}



internal class ReceiptBoardItemState(

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



internal data class BoardDetailState(

    val record: RideRecord,

    val transformOrigin: TransformOrigin

)



internal fun receiptBoardContentHeight(

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



internal fun createReceiptBoardStates(

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



internal fun seededRatio(seed: Int, salt: Int): Float {

    val value = abs(sin((seed + salt) * 12.9898) * 43758.5453)

    return (value - floor(value)).toFloat()

}



internal fun rubberBandOffset(overflow: Float): Float {

    if (overflow == 0f) return 0f

    val distance = abs(overflow)

    val resistance = 1f - (1f / (distance / 180f + 1f))

    return sign(overflow) * 112f * resistance

}



@Composable

internal fun TimelineBackground(

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

internal fun BoardReceiptDetailOverlay(

    record: RideRecord,

    selectedTheme: MeshTheme,

    onDismiss: () -> Unit,

    onSettle: () -> Unit,

    onShred: () -> Unit

) {

    Box(

        modifier = Modifier

            .fillMaxSize()

            .background(Color(0xFFF6F8FC))

            .padding(start = 18.dp, end = 18.dp, top = 44.dp, bottom = 18.dp)

    ) {

        Column(

            modifier = Modifier.fillMaxSize(),

            verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {

            StandaloneDetailHeader(

                title = "카풀 영수증",

                subtitle = "${record.passengerName} 손님 · ${record.date} ${record.endTime}",

                onGoBack = onDismiss

            )

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

                ReceiptPrimaryButton(

                    text = "지금 정산하기",

                    modifier = Modifier.weight(1f),

                    enabled = !record.isSettled,

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

internal fun ReceiptDetailPaper(

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

internal fun DraggableReceiptThumbnail(

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

    LaunchedEffect(state.record.id) {

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

internal fun ReceiptThumbnailPaper(record: RideRecord, selectedTheme: MeshTheme) {

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

internal fun MiniReceiptRow(label: String, value: String, color: Color) {

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

internal fun ReceiptScreen(

    record: RideRecord?,

    selectedTheme: MeshTheme,

    showDetailHeader: Boolean,

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

    val receiptPaperHeight = if (showDetailHeader) 920.dp else 1036.dp

    val receiptHorizontalPadding = if (showDetailHeader) 30.dp else 36.dp

    val receiptTopPadding = if (showDetailHeader) 20.dp else 24.dp

    val receiptBottomPadding = if (showDetailHeader) 72.dp else 86.dp

    val receiptHeaderGap = if (showDetailHeader) 8.dp else 10.dp

    val receiptPassengerGap = if (showDetailHeader) 9.dp else 12.dp

    val receiptRowsGap = if (showDetailHeader) 11.dp else 15.dp

    val receiptTotalGap = if (showDetailHeader) 6.dp else 10.dp

    val receiptIconSize = if (showDetailHeader) 42.dp else 52.dp

    val receiptIconFont = if (showDetailHeader) 22.sp else 28.sp

    val receiptTitleFont = if (showDetailHeader) 30.sp else 34.sp

    val receiptPassengerFont = if (showDetailHeader) 34.sp else 40.sp

    val receiptThanksFont = if (showDetailHeader) 18.sp else 20.sp

    val receiptTotalLabelFont = if (showDetailHeader) 20.sp else 24.sp

    val receiptTotalAmountFont = if (showDetailHeader) 64.sp else 76.sp

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

            receiptSoundPool.play(papercutSoundId, 0.7f, 0.7f, 1, 0, 0.8f)

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

                        .height(receiptPaperHeight)

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

                                    .padding(

                                        start = receiptHorizontalPadding,

                                        end = receiptHorizontalPadding,

                                        top = receiptTopPadding,

                                        bottom = receiptBottomPadding

                                    ),

                                verticalArrangement = Arrangement.SpaceBetween

                            ) {

                                Column(verticalArrangement = Arrangement.spacedBy(receiptHeaderGap), horizontalAlignment = Alignment.CenterHorizontally) {

                                    GlassIconContainer(symbol = "♞", size = receiptIconSize, fontSize = receiptIconFont)

                                    Text(text = "카풀 영수증", color = receiptTextColor, fontSize = receiptTitleFont, fontWeight = FontWeight.Black)

                                    DashedLine(color = receiptStrongLineColor, thickness = 2)

                                }



                                Column(verticalArrangement = Arrangement.spacedBy(receiptPassengerGap)) {

                                    Text(text = "${item.passengerName} 손님,", color = receiptTextColor, fontSize = receiptPassengerFont, fontWeight = FontWeight.Black)

                                    Text(text = "카풀미터기를 이용해주셔서 감사합니다", color = receiptMutedColor, fontSize = receiptThanksFont, fontWeight = FontWeight.Medium)

                                    ThickLine(color = receiptStrongLineColor)

                                }



                                Column(verticalArrangement = Arrangement.spacedBy(receiptRowsGap)) {

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

                                    verticalArrangement = Arrangement.spacedBy(receiptTotalGap),

                                    horizontalAlignment = Alignment.CenterHorizontally

                                ) {

                                    DashedLine(color = receiptStrongLineColor, thickness = 2)

                                    Text(text = "총 운행요금", color = receiptMutedColor, fontSize = receiptTotalLabelFont, fontWeight = FontWeight.Black)

                                    Text(

                                        text = "${item.totalFare.formatWon()}원",

                                        modifier = Modifier.fillMaxWidth(),

                                        color = receiptAmountColor,

                                        fontSize = receiptTotalAmountFont,

                                        fontWeight = FontWeight.Black,

                                        textAlign = TextAlign.End,

                                        lineHeight = receiptTotalAmountFont

                                    )

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

                    if (showDetailHeader) {

                        ReceiptPrimaryButton(

                            text = "지금 정산하기",

                            modifier = Modifier.weight(1f),

                            enabled = !item.isSettled,

                            onClick = {

                                onSettleRecord(item)

                                onGoBack()

                            }

                        )

                    } else {

                        ReceiptPrimaryButton(

                            text = "장부에 저장",

                            modifier = Modifier.weight(1f),

                            onClick = onGoBack

                        )

                        ReceiptGhostButton(

                            text = "지금 정산하기",

                            modifier = Modifier.weight(1f),

                            onClick = {

                                onSettleRecord(item)

                                onGoBack()

                            }

                        )

                    }

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

                if (!showDetailHeader) {

                    ReceiptPrimaryButton(text = "장부에 저장", modifier = modifier, onClick = onGoBack)

                }

            }

        }

    }



    @Composable

    fun BoxScope.ShredderLayer(

        shredderModifier: Modifier

    ) {

        ShredderSheet(

            visible = shredderVisible || shredding,

            progress = shredProgress.value,

            shredding = shredding,

            onShred = {

                if (!shredding && !shredPreparing) {

                    shredPreparing = true

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

                        shredPreparing = false

                        onShredRecord(item)

                        shredSuccessVisible = true

                        kotlinx.coroutines.delay(1100L)

                        shredSuccessVisible = false

                        kotlinx.coroutines.delay(220L)

                        onGoBack()

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

            modifier = Modifier

                .align(Alignment.TopCenter)

                .padding(top = if (showDetailHeader) 86.dp else 28.dp)

                .zIndex(9f)

        )

    }



    Box(modifier = Modifier.fillMaxWidth()) {

        Column(

            modifier = Modifier.fillMaxWidth(),

            verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {

            if (showDetailHeader) {

                StandaloneDetailHeader(

                    title = "카풀 영수증",

                    subtitle = "${item.passengerName} 손님 · ${item.date} ${item.endTime}",

                    onGoBack = onGoBack

                )

            }

            ReceiptPane()

            ReceiptActions(modifier = Modifier.fillMaxWidth())

        }



        ShredderLayer(

            shredderModifier = Modifier

                .align(Alignment.BottomCenter)

                .zIndex(8f)

        )

    }

}



