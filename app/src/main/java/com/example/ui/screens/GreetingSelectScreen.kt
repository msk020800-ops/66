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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChuseokTheme
import com.example.model.GreetingCategory
import com.example.model.GreetingPresets
import com.example.model.RecipientType
import com.example.ui.theme.ChuseokMidnight
import com.example.ui.theme.ChuseokMoonGold
import com.example.ui.theme.ChuseokNavy
import com.example.ui.theme.ChuseokPersimmon
import com.example.ui.theme.ChuseokWarmAmber
import com.example.ui.theme.DeepCharcoal
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.HanjiBorder
import com.example.ui.theme.HanjiIvory
import com.example.ui.theme.HanjiPaper
import com.example.ui.theme.SoftGray

/**
 * 3단계: 인사말 선택 및 AI 인사말 생성 화면
 * 「추석 인사말을 선택하세요」
 * 50~70대 눈높이에 맞춘 큼직한 버튼, 직관적인 인사말 선택 및 수정 기능
 */
@Composable
fun GreetingSelectScreen(
    selectedTheme: ChuseokTheme,
    selectedRecipient: RecipientType,
    recipientDisplay: String,
    selectedCategory: GreetingCategory,
    greetingText: String,
    isGeneratingAi: Boolean,
    errorMessage: String?,
    onSelectCategory: (GreetingCategory) -> Unit,
    onSelectPreset: (String) -> Unit,
    onUpdateGreetingText: (String) -> Unit,
    onTriggerAiGenerate: () -> Unit,
    onCreateCard: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HanjiIvory)
            .verticalScroll(scrollState)
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 선택 상태 요약 뱃지 (테마 & 받는 분)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = ChuseokNavy,
                modifier = Modifier.testTag("summary_badge")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${selectedTheme.icon} ${selectedTheme.title}  |  ${recipientDisplay}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = ChuseokMoonGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 화면 제목: 「추석 인사말을 선택하세요」
        Text(
            text = "추석 인사말을 선택하세요",
            style = MaterialTheme.typography.headlineLarge.copy(
                color = DeepCharcoal,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 25.sp
            ),
            modifier = Modifier.testTag("greeting_screen_title")
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "마음에 드는 문구를 고르시거나 AI에게 생성을 맡겨보세요",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = SoftGray,
                fontSize = 15.sp
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        // 오류 발생 시 중장년층 친화적 안내 메시지
        AnimatedVisibility(visible = errorMessage != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
                    .testTag("error_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFDEEEC))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = onTriggerAiGenerate,
                        colors = ButtonDefaults.buttonColors(containerColor = ChuseokPersimmon),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("🔄 다시 만들기", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 1. ✨ AI에게 인사말 만들기 (가장 눈에 띄는 특별 대형 버튼)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("ai_greeting_button")
                .clickable(enabled = !isGeneratingAi) {
                    onSelectCategory(GreetingCategory.AI_GENERATE)
                },
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedCategory == GreetingCategory.AI_GENERATE) Color(0xFF1E3258) else ChuseokNavy
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (selectedCategory == GreetingCategory.AI_GENERATE) 2.5.dp else 1.dp,
                        color = ChuseokMoonGold,
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(ChuseokMoonGold.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (isGeneratingAi) {
                        CircularProgressIndicator(
                            color = ChuseokMoonGold,
                            modifier = Modifier.size(28.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = ChuseokMoonGold,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "✨ AI에게 인사말 만들기",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = ChuseokMoonGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isGeneratingAi) "따뜻한 추석 인사를 짓고 있어요..." else "${recipientDisplay} 맞춤 따뜻한 글을 자동으로 지어드려요",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = HanjiIvory.copy(alpha = 0.85f),
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2. 일반 추천 카테고리 6개 (따뜻한 인사, 부모님께 감사, 친구에게, 가족에게, 어른께 드리는 인사, 직접 작성)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val categories = listOf(
                GreetingCategory.WARM,
                GreetingCategory.PARENTS_GRATITUDE,
                GreetingCategory.FOR_FAMILY,
                GreetingCategory.FOR_FRIEND,
                GreetingCategory.FOR_ELDERS,
                GreetingCategory.CUSTOM_WRITE
            )

            // 2개씩 2열로 배치
            categories.chunked(2).forEach { rowList ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowList.forEach { category ->
                        val isSelected = category == selectedCategory
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(64.dp)
                                .testTag("category_${category.name.lowercase()}")
                                .clickable { onSelectCategory(category) },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color(0xFFFFF6EE) else Color.White
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) ChuseokPersimmon else HanjiBorder,
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    .padding(horizontal = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = category.icon, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = category.title,
                                        fontSize = 16.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) ChuseokPersimmon else DeepCharcoal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 다른 추천 문구 빠른 선택 칩 (현재 선택 카테고리의 대안 문구들)
        val presets = GreetingPresets.getPresets(selectedCategory, selectedRecipient)
        if (presets.size > 1 && selectedCategory != GreetingCategory.CUSTOM_WRITE) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "다른 추천 문구 골라보기",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = DeepCharcoal
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                presets.forEachIndexed { index, preset ->
                    val isCurrent = preset.trim() == greetingText.trim()
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("preset_option_$index")
                            .clickable { onSelectPreset(preset) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCurrent) Color(0xFFFFF4EC) else Color.White
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = if (isCurrent) 1.5.dp else 1.dp,
                                    color = if (isCurrent) ChuseokPersimmon else HanjiBorder,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "“${preset.replace("\n", " ")}”",
                                fontSize = 14.sp,
                                color = if (isCurrent) ChuseokPersimmon else DeepCharcoal.copy(alpha = 0.85f),
                                maxLines = 2
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        // 3. 카드에 들어갈 최종 인사말 확인 및 직접 수정 박스
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("greeting_preview_box"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = ChuseokPersimmon,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "카드에 담길 인사말 (직접 수정 가능)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepCharcoal,
                            fontSize = 17.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = greetingText,
                    onValueChange = onUpdateGreetingText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("greeting_text_input"),
                    minLines = 4,
                    maxLines = 8,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight.Medium,
                        color = DeepCharcoal
                    ),
                    placeholder = {
                        Text(
                            text = "풍요로운 한가위입니다.\n가족과 함께 웃음 가득하고 행복한 추석 보내세요.\n늘 건강하시고 좋은 일만 가득하시길 바랍니다.",
                            fontSize = 17.sp,
                            lineHeight = 26.sp,
                            color = SoftGray.copy(alpha = 0.6f)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ChuseokPersimmon,
                        unfocusedBorderColor = HanjiBorder,
                        focusedContainerColor = HanjiPaper,
                        unfocusedContainerColor = HanjiPaper
                    ),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "💡 글자를 터치하시면 내용을 더 보태거나 바꿀 수 있습니다.",
                    fontSize = 13.sp,
                    color = SoftGray
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 4. 대망의 [✨ 추석 카드 만들기] 메인 액션 버튼
        Button(
            onClick = onCreateCard,
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .testTag("create_card_button"),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ChuseokPersimmon,
                contentColor = HanjiIvory
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp, pressedElevation = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "✨ 추석 카드 만들기",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
