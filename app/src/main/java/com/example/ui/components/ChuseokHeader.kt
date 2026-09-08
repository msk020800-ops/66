package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.theme.ChuseokMidnight
import com.example.ui.theme.ChuseokMoonGold
import com.example.ui.theme.ChuseokNavy
import com.example.ui.theme.ChuseokNavyLight
import com.example.ui.theme.ChuseokPersimmon
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.HanjiIvory
import com.example.ui.theme.HanjiPaper
import com.example.ui.viewmodel.AppStep

/**
 * 중장년층을 위한 크고 명확한 한가위 앱 상단 헤더 및 진행 단계 표시
 */
@Composable
fun ChuseokHeader(
    currentStep: AppStep,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = ChuseokMidnight,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 상단 행 (뒤로가기 버튼 + 앱 메인 타이틀)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onBackClick != null && currentStep != AppStep.THEME_SELECT) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(52.dp)
                            .testTag("back_button")
                            .clip(CircleShape)
                            .background(ChuseokNavyLight)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "이전 단계로 가기",
                            tint = HanjiIvory,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(52.dp))
                }

                // 앱 제목
                Text(
                    text = "🌕 한가위 추석 인사카드",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = ChuseokMoonGold,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("app_title_text")
                )

                // 우측 균형용 Spacer
                Spacer(modifier = Modifier.size(52.dp))
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 부제: 마음을 담아 따뜻한 추석 인사카드를 만들어 보세요
            Text(
                text = "마음을 담아 따뜻한 추석 인사카드를 만들어 보세요",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = HanjiIvory.copy(alpha = 0.88f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("app_subtitle_text")
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 4단계 진행 표시줄 (1: 테마 -> 2: 받는 분 -> 3: 인사말 -> 4: 완성)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppStep.values().forEachIndexed { index, step ->
                    val isCurrent = step == currentStep
                    val isPassed = step.stepNumber < currentStep.stepNumber

                    val badgeBg = when {
                        isCurrent -> ChuseokMoonGold
                        isPassed -> ChuseokPersimmon
                        else -> ChuseokNavyLight
                    }
                    val textColor = when {
                        isCurrent -> ChuseokMidnight
                        isPassed -> HanjiIvory
                        else -> HanjiIvory.copy(alpha = 0.6f)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(badgeBg)
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${step.stepNumber}. ${step.title}",
                            fontSize = 13.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                            color = textColor
                        )
                    }

                    if (index < AppStep.values().size - 1) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .width(12.dp)
                                .height(2.dp)
                                .background(if (isPassed) ChuseokPersimmon else ChuseokNavyLight)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }
    }
}
