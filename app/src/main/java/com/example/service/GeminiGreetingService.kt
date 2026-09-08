package com.example.service

import android.util.Log
import com.example.BuildConfig
import com.example.model.ChuseokTheme
import com.example.model.RecipientType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * Gemini AI를 활용한 맞춤형 추석 인사말 생성 서비스
 * - BuildConfig.GEMINI_API_KEY 가 설정되어 있으면 Gemini 3.5 Flash REST API를 호출
 * - API 키가 없거나 네트워크 장애 시에도 50~70대 맞춤 정갈한 명절 인사말을 즉시 생성하여 사용자 경험 보장
 */
class GeminiGreetingService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun generateChuseokGreeting(
        recipientType: RecipientType,
        customRecipientName: String,
        theme: ChuseokTheme
    ): Result<String> = withContext(Dispatchers.IO) {
        val recipientName = recipientType.getDisplayName(customRecipientName)
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        // 1. 만약 GEMINI_API_KEY가 유효하게 설정되어 있다면 Gemini 3.5 Flash API 호출
        if (!apiKey.isNullOrBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = buildPrompt(recipientName, theme)
                val requestJson = JSONObject().apply {
                    val contents = JSONArray().apply {
                        val contentObj = JSONObject().apply {
                            val parts = JSONArray().apply {
                                put(JSONObject().apply { put("text", prompt) })
                            }
                            put("parts", parts)
                        }
                        put(contentObj)
                    }
                    put("contents", contents)

                    val generationConfig = JSONObject().apply {
                        put("temperature", 0.7)
                        put("topP", 0.9)
                        put("maxOutputTokens", 300)
                    }
                    put("generationConfig", generationConfig)
                }

                val mediaType = "application/json; charset=utf-8".toMediaType()
                val body = requestJson.toString().toRequestBody(mediaType)
                val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

                val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                    val jsonResponse = JSONObject(responseBody)
                    val candidates = jsonResponse.optJSONArray("candidates")
                    val firstCandidate = candidates?.optJSONObject(0)
                    val content = firstCandidate?.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    val text = parts?.optJSONObject(0)?.optString("text")

                    if (!text.isNullOrBlank()) {
                        val cleanedText = text.trim()
                            .replace("\"", "")
                            .replace("'", "")
                            .trim()
                        return@withContext Result.success(cleanedText)
                    }
                } else {
                    Log.w("GeminiGreeting", "API returned non-success: ${response.code} $responseBody")
                }
            } catch (e: Exception) {
                Log.e("GeminiGreeting", "Error calling Gemini API: ${e.message}", e)
            }
        }

        // 2. 키가 없거나 네트워크 실패 시 맞춤형 고품격 한국어 Chuseok 템플릿 알고리즘 생성
        val fallbackGreeting = generatePersonalizedFallback(recipientType, customRecipientName, theme)
        Result.success(fallbackGreeting)
    }

    private fun buildPrompt(recipientName: String, theme: ChuseokTheme): String {
        return """
            당신은 한국의 정취와 예의를 깊이 이해하는 명절 인사말 작가입니다.
            받는 분: $recipientName
            선택한 추석 테마: ${theme.title} (${theme.subtitle})
            분위기: ${theme.vibe}

            [요청 사항]
            1. 50~70대 중장년층 어르신들과 가족 친지분들이 카카오톡이나 메시지로 주고받기에 가장 알맞은 따뜻하고 정중한 한국어 추석 인사말을 작성해주세요.
            2. 전체 3~4줄 내외 (한 줄당 15~25자, 총 60~100자 내외)로 가독성 높게 줄바꿈을 해주세요.
            3. 받는 사람에 어울리는 예의와 진심을 담아주세요.
            4. 다른 부연 설명이나 따옴표(""), 인사말 제목 없이 오직 카드에 들어갈 본문 텍스트만 출력해주세요.
        """.trimIndent()
    }

    /**
     * 다양하고 자연스러운 테마 및 수신자 맞춤형 정갈한 한가위 인사말
     */
    private fun generatePersonalizedFallback(
        recipientType: RecipientType,
        customRecipientName: String,
        theme: ChuseokTheme
    ): String {
        val pool = when (recipientType) {
            RecipientType.PARENTS -> listOf(
                "언제나 큰 사랑으로 품어주시는 은혜에 깊이 감사드립니다.\n올 한가위에도 둥근 보름달처럼 마음 편안하시고,\n늘 건강하고 행복하시길 두 손 모아 기원합니다.\n사랑합니다, 부모님.",
                "풍요로운 결실의 계절 한가위를 맞아\n부모님의 따스한 사랑과 보살핌을 다시금 되새깁니다.\n맛있는 음식 많이 드시고 오래오래 건강하세요.",
                "더도 말고 덜도 말고 한가위 보름달처럼,\n부모님의 일상에 늘 기쁨과 평안이 가득하시기를 소망합니다.\n늘 감사하고 존경합니다."
            )
            RecipientType.FAMILY -> listOf(
                "언제나 서로에게 가장 큰 힘이 되어주는 우리 가족,\n풍성한 한가위 보름달처럼 모두의 마음에\n사랑과 웃음꽃이 활짝 피어나길 기원합니다.",
                "함께여서 더없이 고맙고 따뜻한 한가위입니다.\n올 가을 맺은 알찬 결실처럼 우리 가족의 앞날에도\n늘 행복과 번영이 가득하길 소망합니다.",
                "온 가족이 둘러앉아 정을 나누는 복된 명절입니다.\n가족 모두 건강하시고, 넉넉하고 평화로운 추석 연휴 보내세요."
            )
            RecipientType.FRIEND -> listOf(
                "오랜 세월 변함없이 함께해 주는 벗이 있어 참 든든하다.\n추석 연휴 동안 모든 걱정은 잠시 내려놓고,\n보름달처럼 밝고 넉넉하고 즐거운 시간 보내길 바란다!",
                "환한 한가위 보름달처럼 네 앞길도 환하게 빛나길 응원해.\n가족들과 맛있는 음식 나누며 웃음 가득한 명절 보내고,\n조만간 기쁜 얼굴로 또 보자!",
                "더도 말고 덜도 말고 한가위만 같아라!\n소중한 사람들과 따뜻한 정 나누며\n몸도 마음도 풍요로운 복된 추석 보내길 바라."
            )
            RecipientType.ACQUAINTANCE -> listOf(
                "보내주신 따뜻한 성원과 배려에 깊이 감사드립니다.\n풍요롭고 밝은 한가위 명절을 맞이하여\n귀하와 가정에 늘 평안과 행운이 가득하시길 기원합니다.",
                "한 해의 결실을 맺는 뜻깊은 계절입니다.\n마음마저 넉넉해지는 풍성한 한가위 보내시고,\n하시는 모든 일에 큰 보람과 번창이 함께하시길 바랍니다.",
                "보름달의 은은한 달빛처럼 평온하고 따뜻한 추석입니다.\n소중한 분들과 정겨운 시간 보내시며\n늘 건강하시고 건승하시기를 진심으로 축원합니다."
            )
            RecipientType.TEACHER -> listOf(
                "선생님의 따뜻한 가르침과 이끌어주심을 늘 감사히 기억합니다.\n풍요롭고 평화로운 한가위 명절을 맞이하여\n늘 강건하시고 댁내에 복이 가득하시길 기원합니다.",
                "언제나 삶의 나침반이 되어주신 선생님의 은혜에 감사드립니다.\n보름달처럼 밝고 넉넉한 한가위 보내시기를\n마음 깊이 존경을 담아 인사 올립니다.",
                "마음으로 늘 가르침을 새기며 살아가고 있습니다.\n올 추석 연휴에는 평안히 쉬시며,\n가족 친지들과 행복하고 복된 시간 보내십시오."
            )
            RecipientType.CUSTOM -> {
                val name = customRecipientName.trim().ifEmpty { "소중한 분" }
                listOf(
                    "${name}님과 함께할 수 있어 늘 감사하고 든든합니다.\n올 한가위에는 보름달처럼 밝은 웃음 가득하시고,\n마음속 깊이 넉넉하고 행복한 명절 보내시길 바랍니다.",
                    "풍요로운 한가위 명절을 맞아 ${name}님의 가정에\n건강과 행복이 가득하기를 두 손 모아 기원합니다.\n즐겁고 뜻깊은 추석 연휴 되세요.",
                    "더도 말고 덜도 말고 한가위 보름달처럼,\n${name}님의 모든 날들이 환하고 복되기를 소망합니다.\n늘 건강하시고 웃음 가득한 명절 보내세요."
                )
            }
        }
        return pool[Random.nextInt(pool.size)]
    }
}
