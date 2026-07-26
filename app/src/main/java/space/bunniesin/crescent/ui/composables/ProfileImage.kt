package space.bunniesin.crescent.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.bunniesin.crescent.models.api.Presence
import space.bunniesin.crescent.ui.theme.Blue500
import space.bunniesin.crescent.ui.theme.Gray400
import space.bunniesin.crescent.ui.theme.Green500
import space.bunniesin.crescent.ui.theme.Red500
import space.bunniesin.crescent.ui.theme.RevoltTheme
import space.bunniesin.crescent.ui.theme.Yellow700
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent

@Composable
fun ProfileImage(
    fallback: String,
    url: String? = null,
    presence: Presence? = null,
    size: Dp = 32.dp
) {
    SubcomposeAsyncImage(
        model = url,
        contentDescription = null,
        modifier = Modifier
            .size(size)
            .aspectRatio(1f)
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
            .drawWithCache {
                val path = Path()
                path.addOval(
                    Rect(
                        topLeft = Offset.Zero,
                        bottomRight = Offset(size.toPx(), size.toPx())
                    )
                )
                onDrawWithContent {
                    clipPath(path) {
                        this@onDrawWithContent.drawContent()
                    }

                    val dotSize = size / 5
                    // Ignore about my bad code bc when is just -> each condition, but I want to check if these are on there.
                    if (presence == Presence.ONLINE || presence == Presence.FOCUS || presence == Presence.IDLE || presence == Presence.DO_NOT_DISTURB || presence == Presence.OFFLINE) {
                        drawCircle(
                            Color.Transparent,
                            radius = dotSize.toPx(),
                            center = Offset(
                                x = size.toPx() - dotSize.toPx() * .8f,
                                y = size.toPx() - dotSize.toPx() * .8f
                            ),
                            blendMode = BlendMode.SrcOut
                        )
                        drawCircle(
                            when (presence) {
                                Presence.ONLINE -> Green500
                                Presence.DO_NOT_DISTURB -> Red500
                                Presence.FOCUS -> Blue500
                                Presence.IDLE -> Yellow700
                                Presence.OFFLINE -> Gray400
                            }, radius = dotSize.toPx() * 0.7f,
                            center = Offset(
                                x = size.toPx() - dotSize.toPx() * .8f,
                                y = size.toPx() - dotSize.toPx() * .8f
                            )
                        )
                    }
                }
            },
    ) {
        val state = painter.state
        when (state) {
            is AsyncImagePainter.State.Loading -> {
                Column(
                    Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeWidth = 2.dp
                    )
                }
            }

            is AsyncImagePainter.State.Error,
            is AsyncImagePainter.State.Empty -> {
                Box(
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        fallback.substring(0, 1),
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 15.sp
                    )
                }
            }

            is AsyncImagePainter.State.Success -> {
                SubcomposeAsyncImageContent(
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}


@Preview
@Composable
fun ProfileImagePreview() {
    RevoltTheme {
        Column {
            ProfileImage("Username")
            ProfileImage(
                "Username",
                "https://images.unsplash.com/photo-1712135596173-2bb522bcfd88?q=80&w=1538&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            )
            ProfileImage(
                "Username",
                "https://images.unsplash.com/photo-1712135596173-2bb522bcfd88?q=80&w=1538&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                Presence.ONLINE
            )
            ProfileImage(
                "Username",
                "https://images.unsplash.com/photo-1712135596173-2bb522bcfd88?q=80&w=1538&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                Presence.FOCUS
            )
            ProfileImage(
                "Username",
                "https://images.unsplash.com/photo-1712135596173-2bb522bcfd88?q=80&w=1538&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                Presence.DO_NOT_DISTURB
            )
            ProfileImage(
                "Username",
                "https://images.unsplash.com/photo-1712135596173-2bb522bcfd88?q=80&w=1538&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                Presence.IDLE
            )
            ProfileImage(
                "Username",
                "https://images.unsplash.com/photo-1712135596173-2bb522bcfd88?q=80&w=1538&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                Presence.OFFLINE
            )
        }
    }
}
