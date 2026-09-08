package com.example.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.ChuseokTheme
import com.example.model.ChuseokThemes
import com.example.model.GreetingCategory
import com.example.model.GreetingPresets
import com.example.model.RecipientType
import com.example.service.GeminiGreetingService
import com.example.util.CardImageSaver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppStep(val stepNumber: Int, val title: String) {
    THEME_SELECT(1, "테마 선택"),
    RECIPIENT_SELECT(2, "받는 분 선택"),
    GREETING_SELECT(3, "인사말 작성"),
    CARD_RESULT(4, "완성된 카드")
}

data class CardUiState(
    val currentStep: AppStep = AppStep.THEME_SELECT,
    val selectedTheme: ChuseokTheme = ChuseokThemes.allThemes.first(),
    val selectedRecipient: RecipientType = RecipientType.PARENTS,
    val customRecipientName: String = "",
    val selectedGreetingCategory: GreetingCategory = GreetingCategory.WARM,
    val greetingText: String = "",
    val isGeneratingAi: Boolean = false,
    val isSavingImage: Boolean = false,
    val errorMessage: String? = null,
    val savedImageUri: Uri? = null,
    val saveToastMessage: String? = null,
    val isEditingGreetingDialog: Boolean = false
) {
    val recipientDisplay: String
        get() = selectedRecipient.getDisplayName(customRecipientName)
}

class ChuseokCardViewModel(
    private val geminiService: GeminiGreetingService = GeminiGreetingService()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CardUiState())
    val uiState: StateFlow<CardUiState> = _uiState.asStateFlow()

    init {
        // 초기 기본 인사말 세팅
        resetGreetingForCurrentSelection()
    }

    /**
     * 1단계: 테마 선택 후 2단계(받는 분 선택)로 이동
     */
    fun selectTheme(theme: ChuseokTheme) {
        _uiState.update {
            it.copy(
                selectedTheme = theme,
                currentStep = AppStep.RECIPIENT_SELECT,
                errorMessage = null
            )
        }
    }

    /**
     * 2단계: 받는 사람 선택
     */
    fun selectRecipient(recipient: RecipientType) {
        _uiState.update {
            it.copy(
                selectedRecipient = recipient,
                errorMessage = null
            )
        }
        resetGreetingForCurrentSelection()
    }

    /**
     * 2단계: 직접 입력 이름 수정
     */
    fun updateCustomRecipientName(name: String) {
        _uiState.update {
            it.copy(customRecipientName = name)
        }
    }

    /**
     * 2단계 완료 후 3단계(인사말 선택)로 이동
     */
    fun proceedToGreetingStep() {
        resetGreetingForCurrentSelection()
        _uiState.update {
            it.copy(
                currentStep = AppStep.GREETING_SELECT,
                errorMessage = null
            )
        }
    }

    /**
     * 3단계: 인사말 카테고리 선택
     */
    fun selectGreetingCategory(category: GreetingCategory) {
        _uiState.update {
            it.copy(
                selectedGreetingCategory = category,
                errorMessage = null
            )
        }
        if (category == GreetingCategory.AI_GENERATE) {
            generateAiGreeting()
        } else if (!category.isCustom) {
            val defaultMsg = GreetingPresets.getDefaultGreeting(category, _uiState.value.selectedRecipient)
            _uiState.update { it.copy(greetingText = defaultMsg) }
        }
    }

    /**
     * 3단계: 특정 추천 문구 선택
     */
    fun selectPresetGreeting(greeting: String) {
        _uiState.update {
            it.copy(greetingText = greeting, errorMessage = null)
        }
    }

    /**
     * 인사말 직접 수정
     */
    fun updateGreetingText(newText: String) {
        _uiState.update {
            it.copy(greetingText = newText, errorMessage = null)
        }
    }

    /**
     * ✨ AI에게 인사말 만들기 호출
     */
    fun generateAiGreeting() {
        val currentState = _uiState.value
        _uiState.update {
            it.copy(
                isGeneratingAi = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            val result = geminiService.generateChuseokGreeting(
                recipientType = currentState.selectedRecipient,
                customRecipientName = currentState.customRecipientName,
                theme = currentState.selectedTheme
            )

            result.fold(
                onSuccess = { generatedGreeting ->
                    _uiState.update {
                        it.copy(
                            isGeneratingAi = false,
                            greetingText = generatedGreeting,
                            selectedGreetingCategory = GreetingCategory.AI_GENERATE,
                            errorMessage = null
                        )
                    }
                },
                onFailure = {
                    _uiState.update {
                        it.copy(
                            isGeneratingAi = false,
                            errorMessage = "잠시 문제가 생겼어요. 다시 한번 만들어 주세요."
                        )
                    }
                }
            )
        }
    }

    /**
     * [✨ 추석 카드 만들기] 버튼 클릭 -> 4단계(완성 카드 화면)로 이동
     */
    fun createCard() {
        val state = _uiState.value
        if (state.greetingText.isBlank()) {
            val fallback = GreetingPresets.getDefaultGreeting(state.selectedGreetingCategory, state.selectedRecipient)
            _uiState.update { it.copy(greetingText = fallback) }
        }
        _uiState.update {
            it.copy(
                currentStep = AppStep.CARD_RESULT,
                savedImageUri = null,
                errorMessage = null
            )
        }
    }

    /**
     * 완성 화면에서 이미지 저장
     */
    fun saveCardImage(context: Context) {
        val state = _uiState.value
        _uiState.update { it.copy(isSavingImage = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val bitmap = CardImageSaver.renderCardBitmap(
                    context = context,
                    theme = state.selectedTheme,
                    recipientText = state.recipientDisplay,
                    greetingText = state.greetingText
                )

                val result = CardImageSaver.saveBitmapToGallery(context, bitmap)
                result.fold(
                    onSuccess = { uri ->
                        _uiState.update {
                            it.copy(
                                isSavingImage = false,
                                savedImageUri = uri,
                                saveToastMessage = "갤러리에 카드가 저장되었습니다!\n사진 앨범이나 카카오톡에서 전송해 보세요."
                            )
                        }
                    },
                    onFailure = {
                        _uiState.update {
                            it.copy(
                                isSavingImage = false,
                                errorMessage = "잠시 문제가 생겼어요. 다시 한번 만들어 주세요."
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSavingImage = false,
                        errorMessage = "잠시 문제가 생겼어요. 다시 한번 만들어 주세요."
                    )
                }
            }
        }
    }

    fun dismissSaveToast() {
        _uiState.update { it.copy(saveToastMessage = null) }
    }

    /**
     * 완성 화면: 「🔄 다시 만들기」
     */
    fun regenerateCard() {
        // AI로 다시 새로운 인사말을 생성하거나 변형 문구 적용
        generateAiGreeting()
    }

    /**
     * 완성 화면: 「✏️ 인사말 수정」 다이얼로그 표시/숨김
     */
    fun setEditingGreetingDialog(show: Boolean) {
        _uiState.update { it.copy(isEditingGreetingDialog = show) }
    }

    /**
     * 완성 화면: 「🏠 처음으로」 (테마 선택 화면으로 복귀)
     */
    fun resetToHome() {
        _uiState.update {
            it.copy(
                currentStep = AppStep.THEME_SELECT,
                errorMessage = null,
                savedImageUri = null,
                saveToastMessage = null
            )
        }
    }

    /**
     * 이전 단계로 이동 (뒤로 가기)
     */
    fun navigateBack() {
        _uiState.update { state ->
            val prevStep = when (state.currentStep) {
                AppStep.THEME_SELECT -> AppStep.THEME_SELECT
                AppStep.RECIPIENT_SELECT -> AppStep.THEME_SELECT
                AppStep.GREETING_SELECT -> AppStep.RECIPIENT_SELECT
                AppStep.CARD_RESULT -> AppStep.GREETING_SELECT
            }
            state.copy(currentStep = prevStep, errorMessage = null)
        }
    }

    private fun resetGreetingForCurrentSelection() {
        val state = _uiState.value
        val defaultText = GreetingPresets.getDefaultGreeting(
            state.selectedGreetingCategory,
            state.selectedRecipient
        )
        _uiState.update { it.copy(greetingText = defaultText) }
    }
}
