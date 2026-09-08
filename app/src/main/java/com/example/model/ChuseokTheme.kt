package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.R

/**
 * 여백의 미가 살아있는 한국적인 추석 테마 7가지 (수채화 / 수묵담채화 스타일)
 * 50~70대 중장년층이 쉽게 읽을 수 있도록 넉넉한 여백과 높은 글자 가독성 보장
 */
data class ChuseokTheme(
    val id: Int,
    val icon: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val vibe: String,
    val drawableResId: Int?,
    val bgStartColor: Color,
    val bgEndColor: Color,
    val cardTextColor: Color,
    val cardAccentColor: Color,
    val badgeColor: Color
)

object ChuseokThemes {
    val allThemes: List<ChuseokTheme> = listOf(
        ChuseokTheme(
            id = 1,
            icon = "🌕",
            title = "달빛 머문 고요한 한옥",
            subtitle = "은은한 달빛과 고즈넉한 한옥 처마, 맑은 밤하늘의 여백",
            description = "부드러운 먹선과 수채화 번짐, 여백의 미가 돋보이는 가을밤",
            vibe = "고요하고 운치 있는 수채화",
            drawableResId = R.drawable.img_moon_hanok,
            bgStartColor = Color(0xFF162238),
            bgEndColor = Color(0xFF223554),
            cardTextColor = Color(0xFFFFFBF0),
            cardAccentColor = Color(0xFFF7C844),
            badgeColor = Color(0xFFE89A23)
        ),
        ChuseokTheme(
            id = 2,
            icon = "🌾",
            title = "황금 들판과 가을빛",
            subtitle = "황금빛 벼이삭과 시원하게 트인 가을 하늘의 넉넉한 여백",
            description = "결실의 풍요로움과 맑은 가을 공기가 서정적으로 번지는 수채화",
            vibe = "풍요롭고 따스한 수채화",
            drawableResId = R.drawable.img_golden_harvest,
            bgStartColor = Color(0xFF2E2211),
            bgEndColor = Color(0xFF4D381B),
            cardTextColor = Color(0xFFFFFDF2),
            cardAccentColor = Color(0xFFFBD050),
            badgeColor = Color(0xFFD48B1B)
        ),
        ChuseokTheme(
            id = 3,
            icon = "🥮",
            title = "소반 위의 고운 송편",
            subtitle = "정갈한 소반과 솔향 머금은 송편, 단아한 한지 여백",
            description = "맑고 깨끗한 한지 위에 은은하게 번지는 한가위의 정과 온기",
            vibe = "단아하고 정갈한 수채화",
            drawableResId = R.drawable.img_songpyeon_art,
            bgStartColor = Color(0xFF2E1C18),
            bgEndColor = Color(0xFF4A2B23),
            cardTextColor = Color(0xFFFFFDF5),
            cardAccentColor = Color(0xFFF5BA42),
            badgeColor = Color(0xFFD34826)
        ),
        ChuseokTheme(
            id = 4,
            icon = "🍂",
            title = "주홍빛 감나무와 까치",
            subtitle = "탐스러운 주홍빛 감과 반가운 소식을 물고 온 까치",
            description = "한국 전통 회화의 길조와 넉넉하고 시원한 한지 여백의 조화",
            vibe = "정겹고 서정적인 수채화",
            drawableResId = R.drawable.img_wc_persimmon,
            bgStartColor = Color(0xFF2B201A),
            bgEndColor = Color(0xFF4A3428),
            cardTextColor = Color(0xFFFFFBF2),
            cardAccentColor = Color(0xFFF5A338),
            badgeColor = Color(0xFFD45D28)
        ),
        ChuseokTheme(
            id = 5,
            icon = "🏺",
            title = "기품 있는 백자 달항아리",
            subtitle = "보름달을 닮은 순백의 달항아리와 가을 가지 한 줄기",
            description = "절제된 곡선과 맑은 여백이 전하는 한국 미학의 절정",
            vibe = "품격 있고 맑은 수채화",
            drawableResId = R.drawable.img_wc_moon_jar,
            bgStartColor = Color(0xFF1E242B),
            bgEndColor = Color(0xFF323B47),
            cardTextColor = Color(0xFFFAF7F0),
            cardAccentColor = Color(0xFFE8C868),
            badgeColor = Color(0xFF386B8A)
        ),
        ChuseokTheme(
            id = 6,
            icon = "🏮",
            title = "은은한 청사초롱과 달맞이",
            subtitle = "달맞이 길을 환히 밝히는 청사초롱과 고요한 가을밤",
            description = "홍청 비단의 따스한 불빛과 여운이 감도는 수묵담채화",
            vibe = "따뜻하고 은은한 수채화",
            drawableResId = R.drawable.img_wc_lantern,
            bgStartColor = Color(0xFF1C2233),
            bgEndColor = Color(0xFF2C364F),
            cardTextColor = Color(0xFFFFFBF0),
            cardAccentColor = Color(0xFFF7C854),
            badgeColor = Color(0xFF3D6CA8)
        ),
        ChuseokTheme(
            id = 7,
            icon = "🎑",
            title = "보름달과 은하수 한지",
            subtitle = "은은하게 번지는 황금 보름달과 안개 구름, 넉넉한 한지 여백",
            description = "마음을 편안하게 채워주는 둥근 달과 잔잔한 가을밤의 숨결",
            vibe = "시적이고 평온한 수채화",
            drawableResId = null,
            bgStartColor = Color(0xFF182032),
            bgEndColor = Color(0xFF283450),
            cardTextColor = Color(0xFFFAF6EB),
            cardAccentColor = Color(0xFFF5CD52),
            badgeColor = Color(0xFF26594C)
        )
    )
}
