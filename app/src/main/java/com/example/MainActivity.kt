package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.ChuseokHeader
import com.example.ui.screens.CardResultScreen
import com.example.ui.screens.GreetingSelectScreen
import com.example.ui.screens.RecipientSelectScreen
import com.example.ui.screens.ThemeSelectScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppStep
import com.example.ui.viewmodel.ChuseokCardViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ChuseokCardApp()
            }
        }
    }
}

/**
 * 한가위 추석 인사카드 메인 화면
 * 50~70대 중장년층이 쉽게 사용할 수 있는 4단계 직관적 플로우:
 * 1단계: 테마 선택 (10개 큰 카드)
 * 2단계: 받는 분 선택 (부모님, 가족, 친구, 지인, 선생님, 직접 입력)
 * 3단계: 인사말 선택 (카테고리, 추천 문구, ✨ AI에게 인사말 만들기)
 * 4단계: 4:5 모바일 카드 완성 및 저장 (📥 이미지 저장, 🔄 다시 만들기, ✏️ 인사말 수정, 🏠 처음으로)
 */
@Composable
fun ChuseokCardApp(
    viewModel: ChuseokCardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // 시스템 뒤로가기 버튼 처리
    BackHandler(enabled = uiState.currentStep != AppStep.THEME_SELECT) {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("chuseok_card_main_scaffold"),
        topBar = {
            ChuseokHeader(
                currentStep = uiState.currentStep,
                onBackClick = if (uiState.currentStep != AppStep.THEME_SELECT) {
                    { viewModel.navigateBack() }
                } else null
            )
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = uiState.currentStep,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            label = "StepTransition"
        ) { step ->
            when (step) {
                AppStep.THEME_SELECT -> {
                    ThemeSelectScreen(
                        onSelectTheme = { theme -> viewModel.selectTheme(theme) }
                    )
                }

                AppStep.RECIPIENT_SELECT -> {
                    RecipientSelectScreen(
                        selectedTheme = uiState.selectedTheme,
                        selectedRecipient = uiState.selectedRecipient,
                        customRecipientName = uiState.customRecipientName,
                        onSelectRecipient = { recipient -> viewModel.selectRecipient(recipient) },
                        onUpdateCustomName = { name -> viewModel.updateCustomRecipientName(name) },
                        onProceed = { viewModel.proceedToGreetingStep() }
                    )
                }

                AppStep.GREETING_SELECT -> {
                    GreetingSelectScreen(
                        selectedTheme = uiState.selectedTheme,
                        selectedRecipient = uiState.selectedRecipient,
                        recipientDisplay = uiState.recipientDisplay,
                        selectedCategory = uiState.selectedGreetingCategory,
                        greetingText = uiState.greetingText,
                        isGeneratingAi = uiState.isGeneratingAi,
                        errorMessage = uiState.errorMessage,
                        onSelectCategory = { category -> viewModel.selectGreetingCategory(category) },
                        onSelectPreset = { preset -> viewModel.selectPresetGreeting(preset) },
                        onUpdateGreetingText = { text -> viewModel.updateGreetingText(text) },
                        onTriggerAiGenerate = { viewModel.generateAiGreeting() },
                        onCreateCard = { viewModel.createCard() }
                    )
                }

                AppStep.CARD_RESULT -> {
                    CardResultScreen(
                        theme = uiState.selectedTheme,
                        recipientDisplay = uiState.recipientDisplay,
                        greetingText = uiState.greetingText,
                        isSavingImage = uiState.isSavingImage,
                        errorMessage = uiState.errorMessage,
                        savedImageUri = uiState.savedImageUri,
                        saveToastMessage = uiState.saveToastMessage,
                        onSaveImage = { viewModel.saveCardImage(context) },
                        onRegenerate = { viewModel.regenerateCard() },
                        onUpdateGreetingText = { updated -> viewModel.updateGreetingText(updated) },
                        onResetToHome = { viewModel.resetToHome() },
                        onDismissSaveToast = { viewModel.dismissSaveToast() }
                    )
                }
            }
        }
    }
}

/**
 * 로보레틱 및 스크린샷 테스트 호환성 유지용 Composable
 */
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
