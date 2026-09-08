package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChuseokTheme
import com.example.model.RecipientType
import com.example.ui.theme.ChuseokMidnight
import com.example.ui.theme.ChuseokMoonGold
import com.example.ui.theme.ChuseokNavy
import com.example.ui.theme.ChuseokNavyLight
import com.example.ui.theme.ChuseokPersimmon
import com.example.ui.theme.DeepCharcoal
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.HanjiBorder
import com.example.ui.theme.HanjiIvory
import com.example.ui.theme.HanjiPaper
import com.example.ui.theme.SoftGray

/**
 * 2단계: 받는 사람 선택 화면
 * 「누구에게 보내실까요?」
 * 50~70대 맞춤 크고 시원한 버튼과 깔끔한 직접 입력 필드
 */
@Composable
fun RecipientSelectScreen(
    selectedTheme: ChuseokTheme,
    selectedRecipient: RecipientType,
    customRecipientName: String,
    onSelectRecipient: (RecipientType) -> Unit,
    onUpdateCustomName: (String) -> Unit,
    onProceed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HanjiIvory)
            .verticalScroll(scrollState)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 선택한 테마 요약 칩
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = ChuseokNavy,
            modifier = Modifier.testTag("current_theme_badge")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "선택한 테마: ${selectedTheme.icon} ${selectedTheme.title}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = ChuseokMoonGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 화면 메인 제목: 「누구에게 보내실까요?」
        Text(
            text = "누구에게 보내실까요?",
            style = MaterialTheme.typography.headlineLarge.copy(
                color = DeepCharcoal,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp
            ),
            modifier = Modifier.testTag("recipient_screen_title")
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "받으실 분을 선택하시면 어울리는 인사말을 준비해 드려요",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = SoftGray,
                fontSize = 16.sp
            )
        )

        Spacer(modifier = Modifier.height(22.dp))

        // 받는 사람 6가지 큰 버튼 목록
        RecipientType.values().forEach { recipient ->
            val isSelected = recipient == selectedRecipient
            RecipientButtonCard(
                recipient = recipient,
                isSelected = isSelected,
                onClick = {
                    onSelectRecipient(recipient)
                    if (recipient != RecipientType.CUSTOM) {
                        focusManager.clearFocus()
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // '직접 입력'을 선택했을 때 보여주는 이름 입력 필드
        AnimatedVisibility(visible = selectedRecipient == RecipientType.CUSTOM) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 12.dp)
                    .testTag("custom_name_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = "받으실 분의 이름을 입력하세요",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepCharcoal,
                            fontSize = 18.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "예: 김영희님, 삼촌, 이과장님 등",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = SoftGray,
                            fontSize = 14.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = customRecipientName,
                        onValueChange = onUpdateCustomName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_name_input"),
                        placeholder = {
                            Text(
                                text = "예: 김영희님",
                                fontSize = 18.sp,
                                color = SoftGray.copy(alpha = 0.6f)
                            )
                        },
                        textStyle = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ChuseokPersimmon,
                            unfocusedBorderColor = HanjiBorder,
                            focusedContainerColor = HanjiPaper,
                            unfocusedContainerColor = HanjiPaper
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 다음 단계 버튼: [인사말 선택하러 가기]
        Button(
            onClick = onProceed,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .testTag("proceed_to_greeting_button"),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ChuseokPersimmon,
                contentColor = HanjiIvory
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "인사말 선택하러 가기",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun RecipientButtonCard(
    recipient: RecipientType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) ChuseokPersimmon else HanjiBorder
    val borderWidth = if (isSelected) 2.5.dp else 1.dp
    val bgColor = if (isSelected) Color(0xFFFFF7F2) else Color.White

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("recipient_option_${recipient.name.lowercase()}")
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(borderWidth, borderColor, RoundedCornerShape(18.dp))
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 이모지 아이콘
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) ChuseokPersimmon.copy(alpha = 0.15f) else HanjiPaper),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = recipient.icon,
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = recipient.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = if (isSelected) ChuseokPersimmon else DeepCharcoal
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = recipient.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = SoftGray,
                            fontSize = 14.sp
                        )
                    )
                }

                // 선택 여부 표시 체크 마크
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ChuseokPersimmon),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "선택됨",
                            tint = HanjiIvory,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}
