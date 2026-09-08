package com.example.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChuseokTheme
import com.example.ui.theme.ChuseokMidnight
import com.example.ui.theme.ChuseokMoonGold
import com.example.ui.theme.ChuseokNavy
import com.example.ui.theme.ChuseokNavyLight
import com.example.ui.theme.ChuseokPersimmon
import com.example.ui.theme.ChuseokWarmAmber
import com.example.ui.theme.DeepCharcoal
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.HanjiBorder
import com.example.ui.theme.HanjiIvory
import com.example.ui.theme.HanjiPaper
import com.example.ui.theme.SoftGray
import com.example.util.CardImageSaver

/**
 * 4단계 & 6단계: 완성된 추석 인사카드 화면
 * - 4:5 세로형 모바일 카드
 * - 50~70대 중장년층을 위한 큼직한 액션 버튼:
 *   「📥 이미지 저장」
 *   「🔄 다시 만들기」
 *   「✏️ 인사말 수정」
 *   「🏠 처음으로」
 */
@Composable
fun CardResultScreen(
    theme: ChuseokTheme,
    recipientDisplay: String,
    greetingText: String,
    isSavingImage: Boolean,
    errorMessage: String?,
    savedImageUri: Uri?,
    saveToastMessage: String?,
    onSaveImage: () -> Unit,
    onRegenerate: () -> Unit,
    onUpdateGreetingText: (String) -> Unit,
    onResetToHome: () -> Unit,
    onDismissSaveToast: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var isEditDialogOpen by remember { mutableStateOf(false) }

    // 저장 성공 시 토스트 메시지 안내
    LaunchedEffect(saveToastMessage) {
        if (saveToastMessage != null) {
            Toast.makeText(context, saveToastMessage, Toast.LENGTH_LONG).show()
            onDismissSaveToast()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HanjiIvory)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 성공 배너
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF26594C),
            modifier = Modifier.testTag("result_success_banner")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🎉", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "마음을 담은 추석 카드가 완성되었습니다!",
                    color = HanjiIvory,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 오류 발생 시 안내
        AnimatedVisibility(visible = errorMessage != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .testTag("result_error_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFDEEEC))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = errorMessage ?: "잠시 문제가 생겼어요. 다시 한번 만들어 주세요.",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = ChuseokPersimmon,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onRegenerate,
                        colors = ButtonDefaults.buttonColors(containerColor = ChuseokPersimmon)
                    ) {
                        Text("🔄 다시 만들기", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // ==========================================
        // 4:5 모바일 추석 인사카드 메인 뷰
        // ==========================================
        ChuseokCardPreview(
            theme = theme,
            recipientDisplay = recipientDisplay,
            greetingText = greetingText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .testTag("chuseok_card_preview")
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 저장 완료 시 카카오톡/메시지 공유 바로가기 버튼 표시
        if (savedImageUri != null) {
            Button(
                onClick = {
                    CardImageSaver.shareCard(
                        context = context,
                        imageUri = savedImageUri,
                        message = "풍요롭고 따뜻한 한가위 보내세요 🌕\n$greetingText"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .testTag("share_card_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE500), contentColor = Color(0xFF191919))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "💬 카카오톡 / 메시지로 바로 보내기",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // ==========================================
        // 4가지 핵심 액션 대형 버튼 목록 (50~70대 친화적)
        // 1. 「📥 이미지 저장」
        // 2. 「🔄 다시 만들기」
        // 3. 「✏️ 인사말 수정」
        // 4. 「🏠 처음으로」
        // ==========================================

        // 1. 「📥 이미지 저장」 버튼 (가장 큰 메인 버튼)
        Button(
            onClick = onSaveImage,
            enabled = !isSavingImage,
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .testTag("save_image_button"),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ChuseokPersimmon,
                contentColor = HanjiIvory
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp, pressedElevation = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isSavingImage) {
                    CircularProgressIndicator(
                        color = HanjiIvory,
                        modifier = Modifier.size(26.dp),
                        strokeWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "갤러리에 저장 중...",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "📥 이미지 저장",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2열 버튼: 「🔄 다시 만들기」 & 「✏️ 인사말 수정」
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 2. 「🔄 다시 만들기」
            Button(
                onClick = onRegenerate,
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
                    .testTag("regenerate_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ChuseokNavy,
                    contentColor = ChuseokMoonGold
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "🔄 다시 만들기",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 3. 「✏️ 인사말 수정」
            Button(
                onClick = { isEditDialogOpen = true },
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
                    .testTag("edit_greeting_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B2E24),
                    contentColor = HanjiIvory
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "✏️ 인사말 수정",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 4. 「🏠 처음으로」 (테마 선택으로 돌아가기)
        OutlinedButton(
            onClick = onResetToHome,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .testTag("home_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = DeepCharcoal
            ),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, HanjiBorder)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = DeepCharcoal,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "🏠 처음으로",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepCharcoal
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }

    // 인사말 직접 수정 다이얼로그
    if (isEditDialogOpen) {
        EditGreetingDialog(
            currentGreeting = greetingText,
            onConfirm = { updated ->
                onUpdateGreetingText(updated)
                isEditDialogOpen = false
            },
            onDismiss = { isEditDialogOpen = false }
        )
    }
}

/**
 * 4:5 모바일 추석 인사카드 렌더링 컴포넌트
 */
@Composable
fun ChuseokCardPreview(
    theme: ChuseokTheme,
    recipientDisplay: String,
    greetingText: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(4f / 5f)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = theme.bgStartColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(theme.bgStartColor, theme.bgEndColor)
                    )
                )
        ) {
            // 배경 이미지 (있는 경우)
            if (theme.drawableResId != null) {
                Image(
                    painter = painterResource(id = theme.drawableResId),
                    contentDescription = theme.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxSize(0.55f)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                )
                // 아래쪽으로 자연스럽게 어두워지는 그라디언트
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                0.35f to Color.Transparent,
                                0.65f to theme.bgStartColor.copy(alpha = 0.95f),
                                1.0f to theme.bgEndColor
                            )
                        )
                )
            } else {
                // 이미지가 없는 테마(한지, 가족 등)는 감성 보름달 캔버스 렌더링
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val moonCenter = Offset(size.width / 2f, size.height * 0.28f)
                    val moonRadius = size.width * 0.22f

                    // 달무리 Glow
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0x66FFDC6E), Color.Transparent),
                            center = moonCenter,
                            radius = moonRadius * 1.8f
                        ),
                        radius = moonRadius * 1.8f,
                        center = moonCenter
                    )

                    // 황금 보름달
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFFFF5B4), Color(0xFFF0B937)),
                            center = Offset(moonCenter.x - moonRadius * 0.3f, moonCenter.y - moonRadius * 0.3f),
                            radius = moonRadius
                        ),
                        radius = moonRadius,
                        center = moonCenter
                    )
                }
            }

            // 전통 금박 외곽 테두리 (Traditional Korean Frame)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
                    .border(
                        width = 2.dp,
                        color = GoldAccent.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(5.dp)
                    .border(
                        width = 1.dp,
                        color = GoldAccent.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(12.dp)
                    )
            )

            // 카드 내용 레이아웃
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 상단 테마 뱃지
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0x33000000),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Brush.linearGradient(listOf(GoldAccent, Color.Transparent))
                    )
                ) {
                    Text(
                        text = "${theme.icon} ${theme.title}",
                        color = ChuseokMoonGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // 받는 분 호칭 (예: 사랑하는 부모님께)
                Text(
                    text = recipientDisplay,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = ChuseokMoonGold,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("card_recipient_text")
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 은은한 골드 장식선
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(2.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.Transparent, ChuseokMoonGold, Color.Transparent)
                            )
                        )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 인사말 본문 (큼직하고 여유로운 한글 줄간격)
                Text(
                    text = greetingText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = theme.cardTextColor,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        lineHeight = 27.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .testTag("card_greeting_text")
                )

                Spacer(modifier = Modifier.weight(1f))

                // 하단 덕담 & 전통 붉은 낙관
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 좌측 짧은 문구 ("즐겁고 행복한 한가위 보내세요")
                    Text(
                        text = "즐겁고 행복한 한가위 보내세요",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = ChuseokMoonGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    // 우측 전통 붉은 사각 낙관 ("한가위")
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFB42828))
                            .border(1.5.dp, Color(0xFFEBAA46), RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "한가위",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

/**
 * 완성 화면에서 즉시 인사말을 다듬을 수 있는 다이얼로그
 */
@Composable
fun EditGreetingDialog(
    currentGreeting: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(currentGreeting) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "✏️ 인사말 수정하기",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        },
        text = {
            Column {
                Text(
                    text = "카드에 들어갈 인사말을 원하시는 대로 고쳐보세요.",
                    fontSize = 14.sp,
                    color = SoftGray
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .testTag("dialog_greeting_edit_input"),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp, lineHeight = 25.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ChuseokPersimmon,
                        unfocusedBorderColor = HanjiBorder,
                        focusedContainerColor = HanjiPaper,
                        unfocusedContainerColor = HanjiPaper
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(text) },
                colors = ButtonDefaults.buttonColors(containerColor = ChuseokPersimmon)
            ) {
                Text("적용하기", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소", fontSize = 16.sp, color = SoftGray)
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White
    )
}
