package dev.lelonio.square.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.lelonio.square.R
import dev.lelonio.square.ui.theme.Ink

/**
 * The MK Music wordmark.
 *
 * Keep the spaced, technical feel of the original wordmark, but use the new
 * app name rather than drawing the old SQUARE lettering.
 */
@Composable
fun MkMusicWordmark(
    height: Dp,
    modifier: Modifier = Modifier,
    color: Color = Ink,
) {
    Text(
        text = "MK MUSIC",
        modifier = modifier.height(height),
        color = color,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium,
        fontSize = (height.value * 0.82f).sp,
        lineHeight = height.value.sp,
        letterSpacing = (height.value * 0.045f).sp,
        maxLines = 1,
        softWrap = false,
    )
}

/**
 * The new MK Music launcher artwork beside its name.
 */
@Composable
fun AppLockup(
    modifier: Modifier = Modifier,
    iconSize: Dp = 28.dp,
    nameHeight: Dp = 14.dp,
    plate: dev.lelonio.square.ui.glass.backdrop.Backdrop? = null,
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        if (plate == null) {
            AppGlyph(iconSize)
        } else {
            AppPlate(iconSize, plate)
        }
        Spacer(Modifier.width(iconSize * 0.32f))
        MkMusicWordmark(nameHeight)
    }
}

/**
 * A glass pane that holds the new full-colour MK Music logo in the home header.
 */
@Composable
fun AppPlate(
    size: Dp,
    backdrop: dev.lelonio.square.ui.glass.backdrop.Backdrop,
    modifier: Modifier = Modifier,
) {
    val accent = androidx.compose.material3.MaterialTheme.colorScheme.primary
    dev.lelonio.square.ui.glass.LiquidButton(
        onClick = {},
        backdrop = backdrop,
        isInteractive = false,
        flat = true,
        modifier = modifier
            .size(size)
            .border(
                0.8.dp,
                androidx.compose.ui.graphics.Brush.linearGradient(
                    0f to Color.White.copy(alpha = 0.62f),
                    0.5f to Color.White.copy(alpha = 0.20f),
                    1f to Color.White.copy(alpha = 0.06f),
                ),
                androidx.compose.foundation.shape.CircleShape,
            ),
        contentHeight = size,
        contentPadding = 0.dp,
    ) {
        Box(Modifier.size(size), contentAlignment = Alignment.Center) {
            androidx.compose.foundation.Canvas(Modifier.matchParentSize()) {
                drawRect(
                    androidx.compose.ui.graphics.Brush.linearGradient(
                        0f to accent.copy(alpha = 0.62f),
                        0.5f to accent.copy(alpha = 0.30f),
                        1f to accent.copy(alpha = 0.08f),
                        start = androidx.compose.ui.geometry.Offset.Zero,
                        end = androidx.compose.ui.geometry.Offset(size.toPx(), size.toPx()),
                    ),
                )
                drawCircle(
                    androidx.compose.ui.graphics.Brush.radialGradient(
                        listOf(Color.White.copy(alpha = 0.34f), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(
                            this.size.width * 0.30f,
                            this.size.height * 0.20f,
                        ),
                        radius = this.size.width * 0.62f,
                    ),
                )
            }
            AppGlyph(size * 0.76f)
        }
    }
}

/**
 * The full-colour logo supplied for MK Music, used throughout the app's own UI.
 */
@Composable
fun AppGlyph(
    size: Dp,
    modifier: Modifier = Modifier,
    @Suppress("UNUSED_PARAMETER") tint: Color = Ink,
) {
    Image(
        painter = painterResource(R.drawable.mk_music_logo),
        contentDescription = "MK Music",
        contentScale = ContentScale.Fit,
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.22f)),
    )
}
