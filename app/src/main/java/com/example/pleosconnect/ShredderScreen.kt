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

internal fun ReceiptBackPaper(

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

internal fun ShreddedReceiptOverlay(progress: Float) {

    if (progress <= 0.01f) return



    Canvas(modifier = Modifier.fillMaxSize()) {

        val clippedProgress = progress.coerceIn(0f, 1f)

        val mouthY = size.height * (1f - clippedProgress)

        val stripCount = 14

        val stripWidth = size.width / stripCount



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

internal fun ReceiptShredIconButton(modifier: Modifier = Modifier, onClick: () -> Unit) {

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

internal fun ShredderSheet(

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

            animationSpec = tween(durationMillis = 120, easing = FastOutSlowInEasing),

            targetOffsetY = { it + 80 }

        )

    ) {

        BoxWithConstraints(

            modifier = Modifier.fillMaxWidth(),

            contentAlignment = Alignment.BottomCenter

        ) {

            val sheetWidth = (maxWidth * 0.92f).coerceAtLeast(340.dp)

            val compact = sheetWidth < 420.dp

            val wideLayout = sheetWidth >= 980.dp

            val expanded = sheetWidth >= 700.dp && !wideLayout

            val sheetHeight = when {

                compact -> 210.dp

                wideLayout -> 300.dp

                expanded -> 276.dp

                else -> 246.dp

            }

            val buttonStackWidth = (sheetWidth * when {

                wideLayout -> 0.64f

                compact -> 0.62f

                else -> 0.68f

            })

                .coerceAtLeast(if (compact) 292.dp else 420.dp)

                .coerceAtMost(sheetWidth * if (wideLayout) 0.72f else 0.78f)

            val buttonHeight = when {

                compact -> 54.dp

                wideLayout -> 72.dp

                expanded -> 66.dp

                else -> 64.dp

            }

            val buttonGap = if (wideLayout) 14.dp else 12.dp



            Box(

                modifier = Modifier

                    .width(sheetWidth)

                    .height(sheetHeight)

            ) {

                if (!shredding) {

                    Text(

                        text = "영수증을 파쇄하시겠습니까?",

                        modifier = Modifier

                            .align(Alignment.TopCenter)

                            .padding(top = if (compact) 10.dp else 14.dp),

                        color = Color(0xFF24313D),

                        fontSize = if (compact) 23.sp else 28.sp,

                        fontWeight = FontWeight.Black,

                        textAlign = TextAlign.Center,

                        lineHeight = if (compact) 28.sp else 34.sp,

                        maxLines = 1

                    )

                    Column(

                        modifier = Modifier

                            .align(Alignment.BottomCenter)

                            .padding(bottom = if (compact) 8.dp else 12.dp)

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

                            shape = G2RoundedCornerShape(if (expanded) 22.dp else 18.dp),

                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),

                            colors = ButtonDefaults.buttonColors(

                                containerColor = Color(0xFFE8473F),

                                contentColor = Color.White,

                                disabledContainerColor = Color(0xFFE8473F),

                                disabledContentColor = Color.White

                            )

                        ) {

                            Text(

                                text = "파쇄하기",

                                fontSize = when {

                                    compact -> 20.sp

                                    wideLayout -> 24.sp

                                    else -> 23.sp

                                },

                                fontWeight = FontWeight.Black

                            )

                        }

                        Button(

                            modifier = Modifier

                                .fillMaxWidth()

                                .height(buttonHeight),

                            onClick = onCancel,

                            enabled = !shredding,

                            shape = G2RoundedCornerShape(if (expanded) 22.dp else 18.dp),

                            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0x994C555D)),

                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),

                            colors = ButtonDefaults.buttonColors(

                                containerColor = Color.White,

                                contentColor = Color(0xFF4C555D),

                                disabledContainerColor = Color.White,

                                disabledContentColor = Color(0xFF4C555D)

                            )

                        ) {

                            Text(

                                text = "취소",

                                fontSize = when {

                                    compact -> 19.sp

                                    wideLayout -> 22.sp

                                    else -> 21.sp

                                },

                                fontWeight = FontWeight.Black

                            )

                        }

                    }

                }

            }

        }

    }

}



@Composable

internal fun ShredderBladeRow(modifier: Modifier = Modifier) {

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

internal fun ShredSuccessToast(visible: Boolean, modifier: Modifier = Modifier) {

    AnimatedVisibility(

        visible = visible,

        modifier = modifier,

        enter = slideInVertically(

            animationSpec = tween(durationMillis = 360, easing = FastOutSlowInEasing),

            initialOffsetY = { -it - 24 }

        ) + fadeIn(animationSpec = tween(durationMillis = 240, easing = FastOutSlowInEasing)),

        exit = slideOutVertically(

            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),

            targetOffsetY = { -it - 24 }

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



