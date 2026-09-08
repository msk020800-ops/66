package com.example.model

/**
 * 추석 카드를 받는 사람 선택 옵션
 */
enum class RecipientType(
    val title: String,
    val defaultAddress: String,
    val icon: String,
    val description: String
) {
    PARENTS("부모님", "사랑하는 부모님께", "👵👴", "은혜롭고 고마우신 부모님"),
    FAMILY("가족", "소중한 가족에게", "👨‍👩‍👧‍👦", "늘 곁에서 든든한 가족"),
    FRIEND("친구", "다정한 친구에게", "🤝", "늘 힘이 되어주는 벗"),
    ACQUAINTANCE("지인", "감사한 지인분께", "🌿", "따뜻한 인연을 맺은 분"),
    TEACHER("선생님", "존경하는 선생님께", "📖", "가르침을 주신 스승님"),
    CUSTOM("직접 입력", "소중한 분께", "✏️", "보내실 분의 성함을 직접 입력");

    fun getDisplayName(customName: String): String {
        return if (this == CUSTOM) {
            val trimmed = customName.trim()
            if (trimmed.isEmpty()) "소중한 분께"
            else if (trimmed.endsWith("님") || trimmed.endsWith("께") || trimmed.endsWith("에게")) trimmed
            else "${trimmed}님께"
        } else {
            defaultAddress
        }
    }
}
