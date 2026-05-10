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

internal fun ChallengesScreen(

    records: List<RideRecord>,

    developerDonationCount: Int,

    onGoHome: () -> Unit,

    onOpenBadgeBoard: () -> Unit

) {

    val challenges = buildChallengeItems(records, developerDonationCount)

    val completedCount = challenges.count { it.progress >= 1f }

    val overallProgress = if (challenges.isEmpty()) 0f else completedCount.toFloat() / challenges.size.toFloat()



    StandaloneDetailHeader(

        title = "도전 과제",

        subtitle = "카풀 기록에 따라 자동으로 완수율이 올라가요.",

        onGoBack = onGoHome

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

                ChallengeProgressBar(progress = overallProgress)

            }

        }

    }



    challenges.forEach { challenge ->

        ChallengeCard(challenge = challenge)

    }

}



@Composable

internal fun ChallengeCard(challenge: ChallengeItem) {

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

                painter = painterResource(id = challenge.iconRes),

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

internal fun ChallengeBadgeBoardScreen(

    records: List<RideRecord>,

    developerDonationCount: Int,

    onGoBack: () -> Unit

) {

    val challenges = buildChallengeItems(records, developerDonationCount)

    val completedChallenges = challenges.filter { it.progress >= 1f }



    StandaloneDetailHeader(

        title = "카풀 배지 보드",

        subtitle = "완료한 도전 과제만 모아봤어요.",

        onGoBack = onGoBack

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



}



@Composable

internal fun ChallengeBadgeBoardDialog(

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

internal fun CompletedBadgeItem(challenge: ChallengeItem, modifier: Modifier = Modifier) {

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

                painter = painterResource(id = challenge.iconRes),

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



internal fun ChallengeItem.iconRes(): Int =

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

internal fun ChallengeProgressBar(progress: Float) {

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




