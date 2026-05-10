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


internal class G2RoundedCornerShape(
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


@Composable
internal fun RunningHorse(modifier: Modifier = Modifier, running: Boolean) {
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
internal fun GlassIconContainer(
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
internal fun GlassTextChip(
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
internal fun TextBanner(text: String) {
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
internal fun StatusChip(text: String, active: Boolean) {
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
internal fun SummaryBox(title: String, value: String, modifier: Modifier = Modifier, valueColor: Color = DarkText) {
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
internal fun ResultMetricCard(
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
internal fun FrostMetricCard(
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
internal fun FareField(label: String, value: String, enabled: Boolean, onValueChange: (String) -> Unit) {
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

internal enum class HomeButtonIcon {
    Play,
    Stop,
    Gear,
    Star,
    Calculator
}

@Composable
internal fun HomeButtonIconView(
    icon: HomeButtonIcon,
    tint: Color,
    iconSize: Dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(iconSize)) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val strokeWidth = w * 0.105f
        val lineStroke = Stroke(width = strokeWidth, cap = StrokeCap.Round)

        when (icon) {
            HomeButtonIcon.Play -> {
                val path = Path().apply {
                    moveTo(w * 0.34f, h * 0.24f)
                    lineTo(w * 0.74f, h * 0.50f)
                    lineTo(w * 0.34f, h * 0.76f)
                    close()
                }
                drawPath(path = path, color = tint)
            }

            HomeButtonIcon.Stop -> {
                val side = w * 0.46f
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(cx - side / 2f, cy - side / 2f),
                    size = Size(side, side),
                    cornerRadius = CornerRadius(w * 0.09f, w * 0.09f)
                )
            }

            HomeButtonIcon.Gear -> {
                repeat(8) { index ->
                    val angle = (PI * 2.0 / 8.0 * index).toFloat()
                    val start = Offset(
                        x = cx + cos(angle) * w * 0.34f,
                        y = cy + sin(angle) * h * 0.34f
                    )
                    val end = Offset(
                        x = cx + cos(angle) * w * 0.45f,
                        y = cy + sin(angle) * h * 0.45f
                    )
                    drawLine(color = tint, start = start, end = end, strokeWidth = strokeWidth, cap = StrokeCap.Round)
                }
                drawCircle(color = tint, radius = w * 0.25f, center = Offset(cx, cy), style = lineStroke)
                drawCircle(color = tint, radius = w * 0.075f, center = Offset(cx, cy))
            }

            HomeButtonIcon.Star -> {
                val path = Path()
                repeat(10) { index ->
                    val angle = (-PI / 2.0 + PI / 5.0 * index).toFloat()
                    val radius = if (index % 2 == 0) w * 0.43f else w * 0.20f
                    val point = Offset(
                        x = cx + cos(angle) * radius,
                        y = cy + sin(angle) * radius
                    )
                    if (index == 0) {
                        path.moveTo(point.x, point.y)
                    } else {
                        path.lineTo(point.x, point.y)
                    }
                }
                path.close()
                drawPath(path = path, color = tint)
            }

            HomeButtonIcon.Calculator -> {
                val bodyWidth = w * 0.68f
                val bodyHeight = h * 0.78f
                val left = cx - bodyWidth / 2f
                val top = cy - bodyHeight / 2f
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(left, top),
                    size = Size(bodyWidth, bodyHeight),
                    cornerRadius = CornerRadius(w * 0.10f, w * 0.10f),
                    style = lineStroke
                )
                drawLine(
                    color = tint,
                    start = Offset(left + bodyWidth * 0.18f, top + bodyHeight * 0.30f),
                    end = Offset(left + bodyWidth * 0.82f, top + bodyHeight * 0.30f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
                val dotRadius = w * 0.045f
                listOf(0.28f, 0.50f, 0.72f).forEach { xRatio ->
                    listOf(0.52f, 0.72f).forEach { yRatio ->
                        drawCircle(
                            color = tint,
                            radius = dotRadius,
                            center = Offset(left + bodyWidth * xRatio, top + bodyHeight * yRatio)
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun AnimatedActionButton(
    text: String,
    icon: HomeButtonIcon? = null,
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
    Box(modifier = modifier.height(height)) {
        if (active) {
            BottomOnlyDropShadow(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalPadding = 14.dp,
                height = 10.dp,
                yOffset = 4.dp,
                alpha = 0.10f
            )
        }
        Button(
            modifier = Modifier.fillMaxSize(),
            onClick = onClick,
            enabled = active,
            shape = shape,
            contentPadding = PaddingValues(0.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = containerColor,
                disabledContentColor = contentColor
            )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    HomeButtonIconView(
                        icon = it,
                        tint = contentColor,
                        iconSize = if (height >= 84.dp) 24.dp else 22.dp
                    )
                }
                Text(text = text, fontSize = fontSize, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
internal fun OrangeButton(text: String, modifier: Modifier = Modifier, enabled: Boolean = true, height: Dp = 62.dp, onClick: () -> Unit) {
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
internal fun GhostButton(
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
internal fun DangerButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
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
internal fun ReceiptPrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val shape = G2RoundedCornerShape(999.dp)
    Button(
        modifier = modifier
            .height(74.dp),
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryBlue,
            contentColor = Color.White,
            disabledContainerColor = DisabledButtonGray,
            disabledContentColor = Color(0xFF8C96A6)
        )
    ) {
        Text(text = text, fontSize = 22.sp, fontWeight = FontWeight.Black, maxLines = 1)
    }
}

@Composable
internal fun ReceiptGhostButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
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
        Text(text = text, fontSize = 22.sp, fontWeight = FontWeight.Black, maxLines = 1)
    }
}

@Composable
internal fun PageHeader(title: String, subtitle: String) {
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
internal fun ReceiptRow(
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
internal fun ThickLine(color: Color = Color.Black) {
    Spacer(modifier = Modifier.fillMaxWidth().height(2.dp).background(color))
}

@Composable
internal fun DashedLine(color: Color = Color(0xFFB8B8B8), thickness: Int = 1) {
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
internal fun PinkingCutEdge(selectedTheme: MeshTheme, modifier: Modifier = Modifier) {
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

internal class ReceiptPaperShape : Shape {
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

internal class PinkingCutShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val toothWidth = 26f
        val cutDepth = 17f
        val paperBottom = size.height - cutDepth - 9f
        return Outline.Generic(pinkingPaperPath(size, toothWidth, cutDepth, paperBottom))
    }
}

internal fun pinkingPaperPath(size: Size, toothWidth: Float, cutDepth: Float, paperBottom: Float): Path =
    Path().apply {
        moveTo(0f, 0f)
        lineTo(size.width, 0f)
        lineTo(size.width, paperBottom)
        addPinkingEdge(size.width, toothWidth, cutDepth, paperBottom)
        lineTo(0f, paperBottom)
        lineTo(0f, 0f)
        close()
    }

internal fun pinkingEdgePath(size: Size, toothWidth: Float, cutDepth: Float, paperBottom: Float): Path =
    Path().apply {
        moveTo(size.width, paperBottom)
        addPinkingEdge(size.width, toothWidth, cutDepth, paperBottom)
        lineTo(0f, paperBottom)
    }

internal fun Path.addPinkingEdge(width: Float, toothWidth: Float, cutDepth: Float, paperBottom: Float) {
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

internal enum class AppScreen {
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

internal data class PendingRide(
    val passengerName: String,
    val endedAt: LocalDateTime,
    val durationSeconds: Long,
    val distanceKm: Float,
    val rideFare: Int
)

internal data class RideRecord(
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

internal data class RecentPassengerSummary(
    val passengerName: String,
    val rideCount: Int,
    val totalFare: Int,
    val totalDistanceKm: Float,
    val latestDate: String,
    val latestTime: String,
    val latestRecordDate: LocalDate?
)

internal data class ChallengeItem(
    val title: String,
    val description: String,
    val progress: Float,
    val progressLabel: String,
    val iconRes: Int
)

internal fun buildChallengeItems(records: List<RideRecord>, developerDonationCount: Int): List<ChallengeItem> {
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

    fun item(
        title: String,
        description: String,
        current: Float,
        target: Float,
        label: String,
        iconRes: Int
    ): ChallengeItem =
        ChallengeItem(
            title = title,
            description = description,
            progress = if (target <= 0f) 0f else current / target,
            progressLabel = label,
            iconRes = iconRes
        )

    return listOf(
        item(
            title = "첫 후원 트로피",
            description = "개발자를 한 번 이상 후원하면 열리는 감사 배지",
            current = developerDonationCount.toFloat(),
            target = 1f,
            label = "$developerDonationCount / 1회",
            iconRes = R.drawable.best
        ),
        item(
            title = "넘치는 사랑",
            description = "개발자를 두 번 이상 후원하면 완성되는 응원 배지",
            current = developerDonationCount.toFloat(),
            target = 2f,
            label = "$developerDonationCount / 2회",
            iconRes = R.drawable.better
        ),
        item(
            title = "첫 영수증 발급",
            description = "첫 번째 카풀 운행을 완료하면 열리는 시작 배지",
            current = completedRides.toFloat(),
            target = 1f,
            label = "$completedRides / 1회",
            iconRes = R.drawable.challenge_icon_09
        ),
        item(
            title = "단골 손님 메모리",
            description = "서로 다른 손님 3명의 카풀 영수증을 모으기",
            current = uniquePassengerCount.toFloat(),
            target = 3f,
            label = "$uniquePassengerCount / 3명",
            iconRes = R.drawable.challenge_icon_02
        ),
        item(
            title = "최근 30일 카풀러",
            description = "최근 30일 안에 10번의 카풀 기록 남기기",
            current = recentRideCount.toFloat(),
            target = 10f,
            label = "$recentRideCount / 10회",
            iconRes = R.drawable.challenge_icon_19
        ),
        item(
            title = "100km 동행",
            description = "친구들과 함께 누적 100km 이동하기",
            current = totalDistanceKm,
            target = 100f,
            label = "%.1fkm / 100km".format(totalDistanceKm),
            iconRes = R.drawable.challenge_icon_11
        ),
        item(
            title = "10만원 정산왕",
            description = "누적 결제 금액 100,000원 달성하기",
            current = totalFare.toFloat(),
            target = 100000f,
            label = "${totalFare.formatWon()}원 / 100,000원",
            iconRes = R.drawable.challenge_icon_16
        ),
        item(
            title = "1시간 안전 운행",
            description = "누적 주행 시간 1시간 채우기",
            current = totalDurationSeconds.toFloat(),
            target = 3600f,
            label = "${totalDurationSeconds.formatDuration()} / 01:00:00",
            iconRes = R.drawable.challenge_icon_20
        ),
        item(
            title = "나이트 영수증 컬렉터",
            description = "나이트 테마로 저장된 영수증 3장 모으기",
            current = nightReceiptCount.toFloat(),
            target = 3f,
            label = "$nightReceiptCount / 3장",
            iconRes = R.drawable.challenge_icon_21
        ),
        item(
            title = "장거리 동행",
            description = "5km 이상 이동한 카풀 기록 5장 모으기",
            current = longRideCount.toFloat(),
            target = 5f,
            label = "$longRideCount / 5회",
            iconRes = R.drawable.challenge_icon_10
        ),
        item(
            title = "동네 한 바퀴",
            description = "1km 이하 짧은 카풀 기록 5장 모으기",
            current = shortRideCount.toFloat(),
            target = 5f,
            label = "$shortRideCount / 5회",
            iconRes = R.drawable.challenge_icon_14
        ),
        item(
            title = "통행료 정산러",
            description = "통행요금이 포함된 영수증 3장 저장하기",
            current = tollRideCount.toFloat(),
            target = 3f,
            label = "$tollRideCount / 3장",
            iconRes = R.drawable.challenge_icon_07
        ),
        item(
            title = "추가요금 협상가",
            description = "추가요금이 포함된 영수증 3장 저장하기",
            current = extraFareRideCount.toFloat(),
            target = 3f,
            label = "$extraFareRideCount / 3장",
            iconRes = R.drawable.challenge_icon_05
        ),
        item(
            title = "만원 넘는 카풀",
            description = "총 운행요금 10,000원 이상 영수증 5장 만들기",
            current = premiumFareCount.toFloat(),
            target = 5f,
            label = "$premiumFareCount / 5장",
            iconRes = R.drawable.challenge_icon_08
        ),
        item(
            title = "테마 수집가",
            description = "서로 다른 테마로 저장된 영수증 5종 모으기",
            current = themeCount.toFloat(),
            target = 5f,
            label = "$themeCount / 5종",
            iconRes = R.drawable.challenge_icon_03
        ),
        item(
            title = "찐 단골 인증",
            description = "같은 손님과 카풀 기록 10장 쌓기",
            current = busiestPassengerRideCount.toFloat(),
            target = 10f,
            label = "$busiestPassengerRideCount / 10회",
            iconRes = R.drawable.challenge_icon_13
        ),
        item(
            title = "하루 카풀 챌린지",
            description = "하루에 카풀 영수증 5장 만들기",
            current = sameDayMaxRideCount.toFloat(),
            target = 5f,
            label = "$sameDayMaxRideCount / 5장",
            iconRes = R.drawable.challenge_icon_06
        )
    )
}

internal data class FarePolicy(
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

internal enum class SurchargeMode(
    val displayName: String,
    val buttonText: String,
    val shortLabel: String,
    val multiplier: Float
) {
    Normal("일반 요금", "일반", "일반", 1f),
    Twenty("20% 할증", "20%", "할증 20%", 1.2f),
    Forty("40% 할증", "40%", "할증 40%", 1.4f)
}

internal fun autoSurchargeModeFor(region: FareRegion, time: LocalTime): SurchargeMode {
    fun inRange(start: LocalTime, end: LocalTime): Boolean =
        if (start < end) {
            !time.isBefore(start) && time.isBefore(end)
        } else {
            !time.isBefore(start) || time.isBefore(end)
        }

    return when (region) {
        FareRegion.SEOUL -> when {
            inRange(LocalTime.of(23, 0), LocalTime.of(2, 0)) -> SurchargeMode.Forty
            inRange(LocalTime.of(22, 0), LocalTime.of(23, 0)) -> SurchargeMode.Twenty
            inRange(LocalTime.of(2, 0), LocalTime.of(4, 0)) -> SurchargeMode.Twenty
            else -> SurchargeMode.Normal
        }

        FareRegion.GYEONGGI,
        FareRegion.INCHEON,
        FareRegion.BUSAN,
        FareRegion.JEJU -> if (inRange(LocalTime.of(23, 0), LocalTime.of(4, 0))) {
            SurchargeMode.Twenty
        } else {
            SurchargeMode.Normal
        }

        FareRegion.DAEGU,
        FareRegion.GWANGJU,
        FareRegion.DAEJEON,
        FareRegion.ULSAN -> if (inRange(LocalTime.MIDNIGHT, LocalTime.of(4, 0))) {
            SurchargeMode.Twenty
        } else {
            SurchargeMode.Normal
        }
    }
}

internal enum class MeshTheme(
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

internal fun MeshTheme.backgroundOverlayAlpha(): Float =
    if (this == MeshTheme.Night) 0.40f else 0.60f

internal fun MeshTheme.receiptPaperOverlayAlpha(): Float =
    0f

internal fun MeshTheme.receiptOuterOverlayAlpha(): Float =
    if (this == MeshTheme.Night) 0f else 0.84f

internal fun RideRecord.receiptThemeOr(defaultTheme: MeshTheme): MeshTheme =
    MeshTheme.entries.firstOrNull { it.code == themeCode } ?: defaultTheme

internal enum class FareRegion(
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
    GYEONGGI("gyeonggi", "경기", 4800, 1.6f, 131f, 100, 1.2f, false, "23-04시 20%"),
    INCHEON("incheon", "인천", 4800, 1.6f, 135f, 100, 1.2f, false, "23-04시 20%"),
    BUSAN("busan", "부산", 4800, 2.0f, 132f, 100, 1.2f, false, "23-04시 20%"),
    DAEGU("daegu", "대구", 4500, 1.6f, 130f, 100, 1.2f, false, "00-04시 20%"),
    GWANGJU("gwangju", "광주", 4300, 1.8f, 134f, 100, 1.2f, false, "00-04시 20%"),
    DAEJEON("daejeon", "대전", 4300, 1.8f, 132f, 100, 1.2f, false, "00-04시 20%"),
    ULSAN("ulsan", "울산", 4500, 2.0f, 125f, 100, 1.2f, false, "00-04시 20%"),
    JEJU("jeju", "제주", 4300, 2.0f, 126f, 100, 1.2f, false, "23-04시 20%")
}

internal fun Float.roundToNearestTen(): Int = (kotlin.math.round(this / 10f) * 10).toInt()

internal fun Long.formatDuration(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60
    return "%02d:%02d:%02d".format(hours, minutes, seconds)
}

internal fun Int.formatWon(): String = "%,d".format(this)

internal fun normalizePassengerWidgetName(name: String): String =
    name.trim()
        .removeSuffix("손님")
        .trim()

internal fun passengerWidgetKey(name: String): String =
    normalizePassengerWidgetName(name).replace(Regex("\\s+"), "")

internal fun FarePolicy.summaryText(): String =
    if (baseDistanceKm <= 0f) {
        "$label · 기본요금 ${baseFare.formatWon()}원 · ${stepDistanceMeters.toInt()}m당 ${stepFare}원"
    } else {
        "$label · 기본 ${baseDistanceKm}km · ${stepDistanceMeters.toInt()}m당 ${stepFare}원"
    }

internal fun FarePolicy.distanceRuleText(): String =
    if (baseDistanceKm <= 0f) {
        "시작 직후부터 ${stepDistanceMeters.toInt()}m마다 ${stepFare}원씩 계산돼요"
    } else {
        "${baseDistanceKm}km 이후 ${stepDistanceMeters.toInt()}m마다 ${stepFare}원씩 계산돼요"
    }

internal val RideRecordDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yy/MM/dd")

internal fun RideRecord.recordDateOrNull(): LocalDate? =
    runCatching { LocalDate.parse(date, RideRecordDateFormatter) }.getOrNull()

internal val PrimaryBlue = Color(0xFF0066CC)
internal val SoftBlue = Color(0xFFEAF4FF)
internal val AppBg = Color(0xFFF5F5F7)
internal val Panel = Color(0xFFFAFAFC)
internal val ButtonGray = Color(0xFFF0F2F6)
internal val DisabledButtonGray = Color(0xFFD3DAE4)
internal val Line = Color(0xFFE0E0E0)
internal val GlassWhite = Color.White.copy(alpha = 0.94f)
internal val Orange = PrimaryBlue
internal val Cream = AppBg
internal val BlackCard = Color(0xFF1D1D1F)
internal val DarkText = Color(0xFF1D1D1F)
internal val GrayText = Color(0xFF7A7A7A)
internal val NoticeGreen = PrimaryBlue
internal val NoticeGreenSoft = SoftBlue
internal val HomePagerTopPadding = 28.dp
internal val HomePagerBottomPadding = 0.dp
internal val HomePagerSectionSpacing = 16.dp
internal val HomePagerIndicatorSlotHeight = 30.dp
internal val HomePagerIndicatorYOffset = (-8).dp
internal val QuickSettingsSupportHeight = 164.dp
internal val QuickSettingsSecondaryHeight = 164.dp
internal val QuickSettingsInfoHeight = 82.dp
internal val QuickSettingsReceiptHeight = QuickSettingsSecondaryHeight

internal val AstaSans = FontFamily(
    Font(R.font.asta_sans_regular, FontWeight.Normal),
    Font(R.font.asta_sans_medium, FontWeight.Medium),
    Font(R.font.asta_sans_semibold, FontWeight.SemiBold),
    Font(R.font.asta_sans_bold, FontWeight.Bold),
    Font(R.font.asta_sans_extrabold, FontWeight.ExtraBold),
    Font(R.font.asta_sans_extrabold, FontWeight.Black)
)

internal val DefaultTypography = Typography()
internal val AstaTypography = Typography(
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
