package com.example.model

/**
 * 추석 인사말 카테고리 및 예시 문구
 */
enum class GreetingCategory(
    val title: String,
    val icon: String,
    val isAi: Boolean = false,
    val isCustom: Boolean = false
) {
    WARM("따뜻한 인사", "🌕"),
    PARENTS_GRATITUDE("부모님께 감사", "👵👴"),
    FOR_FRIEND("친구에게", "🤝"),
    FOR_FAMILY("가족에게", "👨‍👩‍👧‍👦"),
    FOR_ELDERS("어른께 드리는 인사", "🙇"),
    CUSTOM_WRITE("직접 작성", "✏️", isCustom = true),
    AI_GENERATE("✨ AI에게 인사말 만들기", "✨", isAi = true);
}

object GreetingPresets {
    /**
     * 카테고리와 받는 분에 어울리는 추천 인사말 목록
     */
    fun getPresets(category: GreetingCategory, recipient: RecipientType): List<String> {
        return when (category) {
            GreetingCategory.WARM -> listOf(
                "풍요로운 한가위입니다.\n가족과 함께 웃음 가득하고 행복한 추석 보내세요.\n늘 건강하시고 좋은 일만 가득하시길 바랍니다.",
                "풍성한 수확의 계절 가을,\n마음마저 넉넉해지는 한가위 보내시길 바랍니다.\n밝은 보름달처럼 빛나는 날들만 가득하세요.",
                "더도 말고 덜도 말고 한가위만 같아라.\n마음속 깊이 따스한 온기가 가득한 명절 되시고,\n올 한 해 맺은 결실들이 큰 기쁨으로 이어지길 소망합니다."
            )
            GreetingCategory.PARENTS_GRATITUDE -> listOf(
                "낳아주시고 길러주신 크신 사랑에 늘 감사드립니다.\n올 한가위에도 건강하시고 평안하시기를\n자식으로서 두 손 모아 간절히 기원합니다.\n사랑합니다, 부모님.",
                "늘 아낌없이 베풀어주시는 부모님의 은혜를 되새깁니다.\n이번 추석 명절에도 웃음과 온기가 가득하시길 바라며,\n언제나 건강하시고 오래오래 곁에 머물러 주세요.",
                "멀리서도 늘 부모님의 건강과 행복만을 생각합니다.\n풍성한 보름달처럼 부모님의 일상에\n기쁨과 평안이 넘치시기를 진심으로 바랍니다."
            )
            GreetingCategory.FOR_FRIEND -> listOf(
                "오랜 시간 변함없는 우정에 항상 감사해.\n추석 연휴 동안 일상의 피로는 모두 털어내고,\n맛있는 음식 많이 먹으며 편안하고 즐거운 시간 보내!",
                "밝은 한가위 보름달 보며 네 소원도 꼭 이루어지길 바란다.\n가족들과 함께 웃음꽃 피우는 행복한 명절 보내고,\n조만간 반가운 얼굴로 또 만나자!",
                "더도 말고 덜도 말고 보름달처럼 환하고 넉넉한 추석 보내길!\n늘 응원하고 건강 챙겨라. 즐거운 한가위 보내!"
            )
            GreetingCategory.FOR_FAMILY -> listOf(
                "언제나 서로에게 든든한 버팀목이 되어주는 소중한 가족!\n올 추석에도 따뜻한 밥상 나누며 행복한 추억 많이 만들어요.\n우리 가족 모두 건강하고 화목하길 기원합니다.",
                "함께여서 더욱 소중하고 감사한 우리 가족,\n풍성한 한가위 보름달처럼 모두의 마음에\n사랑과 웃음이 가득 차오르길 바랍니다.",
                "서로를 아끼고 사랑하는 마음으로 맞이하는 한가위입니다.\n가족의 건강과 평안을 빌며, 웃음소리 끊이지 않는\n복된 명절 보내시길 바랍니다."
            )
            GreetingCategory.FOR_ELDERS -> listOf(
                "어르신의 한결같은 보살핌과 은혜에 머리 숙여 감사드립니다.\n풍요롭고 밝은 한가위 명절을 맞이하여\n댁내 평안과 건강을 진심으로 축원하옵니다.",
                "가을의 풍요로움과 함께 감사의 인사를 올립니다.\n항상 베풀어주신 온정을 마음 깊이 새기며,\n어르신의 가정이 늘 건승하시기를 기원합니다.",
                "보름달의 은은한 달빛처럼 어르신의 삶에\n늘 건강과 평화가 함께하시길 두 손 모아 기원합니다.\n행복하고 뜻깊은 추석 보내십시오."
            )
            GreetingCategory.CUSTOM_WRITE -> listOf(
                "마음을 담은 따뜻한 추석 인사를 직접 작성해 보세요."
            )
            GreetingCategory.AI_GENERATE -> listOf(
                "풍요로운 한가위입니다.\n가족과 함께 웃음 가득하고 행복한 추석 보내세요.\n늘 건강하시고 좋은 일만 가득하시길 바랍니다."
            )
        }
    }

    /**
     * 카테고리에 맞는 기본 인사말 1개 선택
     */
    fun getDefaultGreeting(category: GreetingCategory, recipient: RecipientType): String {
        return getPresets(category, recipient).first()
    }
}
