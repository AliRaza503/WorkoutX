package com.labz.workoutx.ui.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.labz.workoutx.R
import com.labz.workoutx.ui.navigator.NavigatorObj
import com.labz.workoutx.ui.exts.GradientButton
import com.labz.workoutx.ui.exts.GradientIconButton
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object OnBoardingScreen {
    @Composable
    fun OnboardingScreenComposable(navController: NavController) {
        val pagerState = rememberPagerState(
            pageCount = { 4 },
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(
                Modifier
                    .fillMaxWidth()
            ) {
                when (page) {
                    0 -> OnboardingScreenContent(
                        headingText = "Track Your Goal",
                        bodyText = "Don't worry if you have trouble determining your goals, We can help you determine your goals and track your goals",
                        drawableId = R.drawable.onboarding_1
                    )

                    1 -> OnboardingScreenContent(
                        headingText = "Get Burn",
                        bodyText = "Let’s keep burning, to achieve yours goals, it hurts only temporarily, if you give up now you will be in pain forever",
                        drawableId = R.drawable.onboarding_2
                    )

                    2 -> OnboardingScreenContent(
                        headingText = "Eat Well",
                        bodyText = "Let's start a healthy lifestyle with us, we can determine your diet every day. healthy eating is fun",
                        drawableId = R.drawable.onboarding_3
                    )

                    3 -> OnboardingScreenContent(
                        headingText = "Improve Sleep Quality",
                        bodyText = "Improve the quality of your sleep with us, good quality sleep can bring a good mood in the morning",
                        drawableId = R.drawable.onboarding_4
                    )
                }

                val coroutineScope = rememberCoroutineScope()
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(30.dp)
                        .size(width = 120.dp, height = 60.dp)
                ) {
                    if (page == 3) {
                        GradientButton(
                            onClick = {
                                navController.navigate(NavigatorObj) {
                                    popUpTo(OnBoardingScreen) {
                                        inclusive = true
                                    }
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .fillMaxSize(),
                            text = "Get Started"
                        )
                    } else {
                        GradientIconButton(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(page + 1)
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .fillMaxSize(),
                            icon = painterResource(id = R.drawable.ic_next),
                            contentDescription = "Next"
                        )
                    }
                }
            }
        }
    }
}
