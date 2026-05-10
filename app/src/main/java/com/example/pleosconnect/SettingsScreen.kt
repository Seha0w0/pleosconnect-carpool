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
internal fun ResetHistoryDialog(
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
internal fun SettleAllDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onSettleAll: () -> Unit
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
                                text = "전체 정산하기",
                                color = DarkText,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Black,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "정말로 모두 정산하시겠습니까?\n이 작업은 되돌릴 수 없습니다",
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
                                onClick = onSettleAll,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(58.dp),
                                shape = G2RoundedCornerShape(22.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryBlue,
                                    contentColor = Color.White
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
                            ) {
                                Text(text = "모두 정산하기", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun StandaloneDetailHeader(
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
internal fun AppInfoScreen(
    selectedTheme: MeshTheme,
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onResetHistory: () -> Unit
) {
    var showResetDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val resetDialogBlur by animateDpAsState(
        targetValue = if (showResetDialog) 16.dp else 0.dp,
        animationSpec = tween(durationMillis = 360, easing = FastOutSlowInEasing),
        label = "resetDialogBlur"
    )

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .blur(resetDialogBlur)
                .padding(start = 18.dp, end = 18.dp, top = 44.dp, bottom = 18.dp),
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
                            text = "1.2",
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
                    AppInfoRow(title = "소프트웨어 버전", value = "v1.2.4")
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
}

@Composable
internal fun AppInfoRow(title: String, value: String) {
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

internal data class DonationItem(
    val name: String,
    val price: String,
    val imageRes: Int,
    val imageScale: Float = 0.92f
)

internal enum class DonationStage {
    Selection,
    Terms,
    LoadingQr,
    PaymentQr,
    Completing,
    Success
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun DonationScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onGoHome: () -> Unit,
    onDonationCompleted: () -> Unit
) {
    val donationItems = remember {
        listOf(
            DonationItem("🍬 졸음 번쩍 껌 한 통", "1,000원", R.drawable.ggum),
            DonationItem("☕ 시원한 아아 한잔", "1,500원", R.drawable.ice),
            DonationItem("⚡ 시원한 에너지 드링크", "3,500원", R.drawable.monster),
            DonationItem("🥪 맛있는 편의점 샌드위치", "4,500원", R.drawable.sandwich),
            DonationItem("🍕 야근엔 역시 피자", "15,000원", R.drawable.pizza),
            DonationItem("🍗 야식 치킨 수혈", "20,000원", R.drawable.chicken),
            DonationItem("⛽ 가득이요! VIP 주유권", "50,000원", R.drawable.gas, imageScale = 0.76f),
            DonationItem(
                "🛠️ 필요하신 앱/기능 만들어 드립니다",
                "100,000원",
                R.drawable.joker,
                imageScale = 1.12f
            )
        )
    }
    val donationPages = remember(donationItems) { donationItems.chunked(4) }
    val pagerState = rememberPagerState(pageCount = { donationPages.size })
    var selectedDonation by remember { mutableStateOf<DonationItem?>(null) }
    var donationStage by remember { mutableStateOf(DonationStage.Selection) }

    LaunchedEffect(donationStage) {
        when (donationStage) {
            DonationStage.LoadingQr -> {
                kotlinx.coroutines.delay(1500L)
                donationStage = DonationStage.PaymentQr
            }

            DonationStage.Completing -> {
                kotlinx.coroutines.delay(1500L)
                donationStage = DonationStage.Success
            }

            DonationStage.Success -> {
                onDonationCompleted()
                kotlinx.coroutines.delay(1000L)
                onGoHome()
            }

            else -> Unit
        }
    }

    AnimatedContent(
        targetState = donationStage,
        modifier = modifier,
        label = "donationStage"
    ) { stage ->
        when (stage) {
            DonationStage.Selection -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
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
                        DonationPageCard(
                            items = donationPages[page],
                            onDonate = { item ->
                                selectedDonation = item
                                donationStage = if (item.price == "100,000원") {
                                    DonationStage.Terms
                                } else {
                                    DonationStage.LoadingQr
                                }
                            }
                        )
                    }

                    HomePagerIndicatorSlot(
                        currentPage = pagerState.currentPage,
                        pageCount = donationPages.size
                    )
                }
            }

            DonationStage.Terms -> DonationTermsScreen(
                item = selectedDonation ?: donationItems.last(),
                onCancel = { donationStage = DonationStage.Selection },
                onAgree = { donationStage = DonationStage.LoadingQr }
            )

            DonationStage.LoadingQr -> DonationLoadingScreen(text = "네이버페이 결제를 준비하고 있어요")

            DonationStage.PaymentQr -> DonationPaymentScreen(
                item = selectedDonation ?: donationItems.first(),
                onGoBack = { donationStage = DonationStage.Selection },
                onPaymentDone = { donationStage = DonationStage.Completing }
            )

            DonationStage.Completing -> DonationLoadingScreen(text = "결제 완료를 확인하고 있어요")

            DonationStage.Success -> DonationSuccessScreen()
        }
    }
}

@Composable
internal fun DonationPageCard(
    items: List<DonationItem>,
    onDonate: (DonationItem) -> Unit
) {
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
            DonationGridRow(items = items.take(2), modifier = Modifier.weight(1f), onDonate = onDonate)
            DonationGridRow(items = items.drop(2).take(2), modifier = Modifier.weight(1f), onDonate = onDonate)
        }
    }
}

@Composable
internal fun DonationGridRow(
    items: List<DonationItem>,
    modifier: Modifier = Modifier,
    onDonate: (DonationItem) -> Unit
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
                    .fillMaxSize(),
                onDonate = onDonate
            )
        }
        repeat(2 - items.size) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
internal fun DonationItemCard(
    item: DonationItem,
    modifier: Modifier = Modifier,
    onDonate: (DonationItem) -> Unit
) {
    val compactName = item.name.length > 28

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
                        .padding(18.dp)
                        .graphicsLayer {
                            scaleX = item.imageScale
                            scaleY = item.imageScale
                        },
                    contentScale = ContentScale.Fit
                )
            }

            Text(
                text = item.name,
                color = DarkText,
                fontSize = if (compactName) 16.sp else 20.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                lineHeight = if (compactName) 19.sp else 24.sp,
                modifier = Modifier.fillMaxWidth(),
                maxLines = if (compactName) 3 else 2
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
                onClick = { onDonate(item) }
            )
        }
    }
}

@Composable
internal fun DonationLoadingScreen(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp),
            shape = G2RoundedCornerShape(36.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.94f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.72f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 42.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                CircularProgressIndicator(color = PrimaryBlue, strokeWidth = 5.dp)
                Text(
                    text = text,
                    color = DarkText,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
internal fun DonationTermsScreen(
    item: DonationItem,
    onCancel: () -> Unit,
    onAgree: () -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val bounceOffset = remember { Animatable(0f) }
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
                        bounceOffset.snapTo(bounceOffset.value + available.y * 0.18f)
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
    val termsText = """
10만 원이라는 거금을 후원해 주셔서 진심으로 감사합니다! 🎉
후원해 주신 금액은 플레오스 커넥트 앱 내 신규 기능이나 독립된 미니 앱으로 뚝딱 만들어 드리는 데 쓰입니다.

단, 원활한 개발을 위해 결제 전 아래 4가지 조건을 반드시 확인해 주세요.

1. 🛠️제작 난이도 제한
기술적으로 구현 불가능한 기획, 서버 구축, 복잡한 로그인, 결제 연동 등은 불가능합니다.  차량 내에서 돌아가는 화면(UI) 위주의 유틸리티 등의 앱/기능만 제작 가능합니다.

2. 🚧 스토어 심사 탈락 리스크 (매우 중요)
플레오스 앱 심사팀의 규정에 따라 앱 출시가 반려될 수 있습니다. 심사 통과는 최대 2회까지 도전하며, 최종 탈락 시 10만 원은 환불되지 않습니다. (대신 원하실 경우 소스 코드를 메일로 보내드립니다)

3. ⏰ 유지보수 기간
기능/앱 전달(또는 출시) 후 2주 동안 치명적인 버그(화면 멈춤 등)를 무상으로 고쳐 드립니다. 이후 업데이트나 추가 수정은 비용이 발생할 수 있습니다.

4. 💌 진행 방법
결제 완료 후, 원하시는 기획이나 아이디어를 appleslie0@gmail.com 으로 보내주시면 확인 후 답장드리겠습니다!
결제 내역을 스크린샷으로 보내 신원을 확인시켜 주세요.

5. 🌍 함께 만들어가는 생태계 (필독)
의뢰해 주신 소중한 아이디어로 탄생한 기능은 플레오스 생태계를 풍성하게 만드는 데 기여하며, 해당 앱/기능의 서비스 소유권 및 운영 권한은 원작자에게 귀속됩니다. 지속적인 서비스 제공을 위해 화면 내부에 작은 후원 메뉴가 포함될 수 있으며, 본 리워드는 앱의 소유권을 양도하는 것이 아닌 원하시던 기능을 자유롭게 '이용'하실 수 있도록 구현해 드리는 서비스입니다.
    """.trimIndent()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StandaloneDetailHeader(
            title = "후원 전 확인",
            subtitle = "${item.name} · ${item.price}",
            onGoBack = onCancel
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = G2RoundedCornerShape(36.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.78f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(bounceConnection)
                    .graphicsLayer { translationY = bounceOffset.value },
                contentPadding = PaddingValues(horizontal = 26.dp, vertical = 26.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = termsText,
                        color = DarkText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 31.sp
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f)
                    .height(76.dp),
                shape = G2RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonGray,
                    contentColor = DarkText
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
            ) {
                Text(
                    text = "조금 더 고민해볼게요",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 21.sp
                )
            }

            Button(
                onClick = onAgree,
                modifier = Modifier
                    .weight(1f)
                    .height(76.dp),
                shape = G2RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
            ) {
                Text(
                    text = "동의하고 후원하기",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 21.sp
                )
            }
        }
    }
}

@Composable
internal fun DonationPaymentScreen(
    item: DonationItem,
    onGoBack: () -> Unit,
    onPaymentDone: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StandaloneDetailHeader(
            title = "네이버페이 후원",
            subtitle = "QR을 스마트폰으로 촬영해 결제해 주세요",
            onGoBack = onGoBack
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = G2RoundedCornerShape(36.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.78f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.naverpay2),
                    contentDescription = "네이버페이 QR",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(G2RoundedCornerShape(30.dp))
                        .background(Panel)
                        .padding(12.dp),
                    contentScale = ContentScale.Fit
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = G2RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Panel),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Line.copy(alpha = 0.72f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 22.dp, vertical = 18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "선택한 후원",
                            color = GrayText,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = item.name,
                            color = DarkText,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            lineHeight = 29.sp
                        )
                        Text(
                            text = item.price,
                            color = PrimaryBlue,
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }

        OrangeButton(
            text = "결제를 완료하셨다면 이 버튼을 눌러주세요",
            modifier = Modifier.fillMaxWidth(),
            height = 72.dp,
            onClick = onPaymentDone
        )
    }
}

@Composable
internal fun DonationSuccessScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp),
            shape = G2RoundedCornerShape(40.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.72f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 54.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "✓", color = Color(0xFF10B981), fontSize = 58.sp, fontWeight = FontWeight.Black)
                Text(
                    text = "결제 성공",
                    color = DarkText,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "후원해 주셔서 감사합니다",
                    color = GrayText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

internal data class SupportSnack(
    val label: String,
    val imageRes: Int,
    val onClick: () -> Unit = {}
)

@Composable
internal fun DeveloperSupportBanner(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    BottomShadowContainer(
        modifier = modifier.fillMaxWidth(),
        horizontalPadding = 22.dp,
        alpha = 0.10f
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
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
                                Color(0xFFFFC14A),
                                Color(0xFFFF6A3D),
                                Color(0xFFFF2D78),
                                Color(0xFFFF8A00)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    )
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFFFF071).copy(alpha = 0.60f),
                                Color.Transparent
                            ),
                            center = Offset(160f, 80f),
                            radius = 520f
                        )
                    )
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFFF2DB2).copy(alpha = 0.42f),
                                Color.Transparent
                            ),
                            center = Offset(850f, 230f),
                            radius = 620f
                        )
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.34f),
                                    Color.Transparent
                                ),
                                center = Offset(360f, 80f),
                                radius = 780f
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 26.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🥺 불쌍한 밤샘 개발자를\n커피로 후원해 주세요 ☕",
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp
                    )
                }
            }
        }
    }
}



@Composable
internal fun FareModeSelectScreen(
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
internal fun FareModeLargeButton(
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
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Black
                )

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(text = title, color = DarkText, fontSize = 45.sp, fontWeight = FontWeight.Black, lineHeight = 50.sp)
                    Text(text = subtitle, color = GrayText, fontSize = 25.sp, fontWeight = FontWeight.Medium, lineHeight = 33.sp)
                }

                Text(
                    text = "선택하기",
                    modifier = Modifier.fillMaxWidth(),
                    color = PrimaryBlue,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
internal fun CustomFareSettingsScreen(
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

        OrangeButton(text = "적용 완료", modifier = Modifier.fillMaxWidth(), height = 76.dp, onClick = onGoHome)
    }
}

@Composable
internal fun CustomFareOptionPanel(
    title: String,
    subtitle: String,
    options: List<Int>,
    selectedValue: Int,
    modifier: Modifier = Modifier,
    onSelect: (Int) -> Unit
) {
    var customInput by remember(selectedValue) { mutableStateOf(selectedValue.toString()) }
    val customValue = customInput.toIntOrNull()
    val canApplyCustomValue = customValue != null && customValue >= 0

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
            Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                Text(text = title, color = DarkText, fontSize = 34.sp, fontWeight = FontWeight.Black)
                Text(
                    text = subtitle,
                    color = GrayText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 26.sp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = customInput,
                    onValueChange = { customInput = it.filter(Char::isDigit).take(7) },
                    modifier = Modifier
                        .weight(1f)
                        .height(90.dp),
                    singleLine = true,
                    label = {
                        Text(text = "직접 입력", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    },
                    trailingIcon = {
                        Text(
                            text = "원",
                            modifier = Modifier.padding(end = 10.dp),
                            color = GrayText,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = G2RoundedCornerShape(20.dp)
                )
                Button(
                    modifier = Modifier
                        .width(152.dp)
                        .height(90.dp),
                    enabled = canApplyCustomValue,
                    onClick = { customValue?.let(onSelect) },
                    shape = G2RoundedCornerShape(20.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue,
                        contentColor = Color.White,
                        disabledContainerColor = ButtonGray,
                        disabledContentColor = GrayText
                    )
                ) {
                    Text(text = "직접 적용", fontSize = 20.sp, fontWeight = FontWeight.Black)
                }
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
                                    .height(62.dp),
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
internal fun FareSettingsScreen(
    selectedRegion: FareRegion,
    onSelectRegion: (FareRegion) -> Unit,
    onGoBack: () -> Unit
) {
    StandaloneDetailHeader(
        title = "요금제 선택",
        subtitle = "주행 지역에 맞는 중형택시 요금제를 선택하세요.",
        onGoBack = onGoBack
    )

    FareRegion.entries.forEach { region ->
        val selected = region == selectedRegion
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 118.dp)
                .clickable { onSelectRegion(region) },
            shape = G2RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = if (selected) SoftBlue else Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 22.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = region.displayName, color = DarkText, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "기본 ${region.baseFare.formatWon()}원 / ${region.baseDistanceKm}km",
                        color = GrayText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${region.stepDistanceMeters.toInt()}m당 ${region.stepFare}원 · 할증은 홈에서 직접 선택",
                        color = GrayText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                if (selected) {
                    Text(
                        text = "선택됨",
                        modifier = Modifier
                            .background(Color.White, G2RoundedCornerShape(999.dp))
                            .padding(horizontal = 18.dp, vertical = 12.dp),
                        color = PrimaryBlue,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
internal fun ThemeSettingsScreen(
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
internal fun ThemePreviewCard(
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


