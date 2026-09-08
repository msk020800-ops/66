package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChuseokTheme
import com.example.model.ChuseokThemes
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
 * 1단계: 첫 화면 - 10가지 추석 테마 선택 화면
 * 50~70대 중장년층 사용자를 위해 버튼과 글자를 큼직하고 시원하게 배치
 */
@Composable
fun ThemeSelectScreen(
    onSelectTheme: (ChuseokTheme) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(HanjiIvory),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // 섹션 안내 배너
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("theme_guide_banner"),
                shape = RoundedCornerShape(16.dp),
                color = ChuseokNavy,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🎨",
                        fontSize = 32.sp
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "한국적 수채화 테마 7가지 (여백의 미)",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = ChuseokMoonGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 21.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "맑은 수채화와 넉넉한 여백이 어우러진 테마를 골라보세요.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = HanjiIvory.copy(alpha = 0.9f),
                                fontSize = 15.sp,
                                lineHeight = 21.sp
                            )
                        )
                    }
                }
            }
        }

        items(ChuseokThemes.allThemes, key = { it.id }) { theme ->
            ThemeCardItem(
                theme = theme,
                onClick = { onSelectTheme(theme) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ThemeCardItem(
    theme: ChuseokTheme,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("theme_card_${theme.id}")
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp, pressedElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.5.dp,
                    color = HanjiBorder,
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // 상단 헤더: 순번 + 테마 아이콘 + 테마 이름 + 화살표
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 번호 및 아이콘 원형 뱃지
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(HanjiPaper),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = theme.icon,
                            fontSize = 30.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${theme.id}. ${theme.title}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = DeepCharcoal
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // 분위기 태그
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(theme.badgeColor.copy(alpha = 0.12f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = theme.vibe,
                                color = theme.badgeColor,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // 선택 화살표
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ChuseokNavy.copy(alpha = 0.08f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "선택하기",
                            tint = ChuseokNavy,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 설명 문구 (부제 및 분위기)
                Text(
                    text = "• ${theme.subtitle}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = DeepCharcoal.copy(alpha = 0.85f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 22.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "• ${theme.description}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = SoftGray,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                )

                // 테마에 연결된 직관적인 수채화 미리보기 표시
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp)
                        .clip(RoundedCornerShape(14.dp))
                ) {
                    if (theme.drawableResId != null) {
                        Image(
                            painter = painterResource(id = theme.drawableResId),
                            contentDescription = theme.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color(0x66000000))
                                    )
                                )
                        )
                    } else {
                        // 7번째 테마: 은은한 보름달과 한지 수묵 번짐 캔버스
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color(0xFF1B263B), Color(0xFF2E3E58))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                                val centerOffset = androidx.compose.ui.geometry.Offset(size.width * 0.5f, size.height * 0.5f)
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        colors = listOf(Color(0x77FFDC6E), Color.Transparent),
                                        center = centerOffset,
                                        radius = 70f
                                    ),
                                    radius = 70f,
                                    center = centerOffset
                                )
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        colors = listOf(Color(0xFFFFF7C8), Color(0xFFE8B84B)),
                                        center = centerOffset,
                                        radius = 35f
                                    ),
                                    radius = 35f,
                                    center = centerOffset
                                )
                            }
                            Text(
                                text = "🎑 은은한 보름달과 넉넉한 한지 여백",
                                color = HanjiIvory,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
