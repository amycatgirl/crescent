package space.bunniesin.crescent.nav

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith


val forwardTransition: () -> ContentTransform = {
    (fadeIn(animationSpec = tween(durationMillis = 250)) + slideInHorizontally(
        animationSpec = tween(
            durationMillis = 250,
            easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
        ),
        initialOffsetX = { it }
    )) togetherWith (fadeOut(animationSpec = tween(durationMillis = 200)) + slideOutHorizontally(
        animationSpec = tween(
            durationMillis = 200,
            easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
        ),
        targetOffsetX = { -it }
    ))
}

val backwardTransition: () -> ContentTransform = {
    (fadeIn(animationSpec = tween(durationMillis = 250)) + slideInHorizontally(
        animationSpec = tween(
            durationMillis = 250,
            easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
        ),
        initialOffsetX = { -it }
    )) togetherWith (fadeOut(animationSpec = tween(durationMillis = 200)) + slideOutHorizontally(
        animationSpec = tween(
            durationMillis = 200,
            easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
        ),
        targetOffsetX = { it }
    ))
}