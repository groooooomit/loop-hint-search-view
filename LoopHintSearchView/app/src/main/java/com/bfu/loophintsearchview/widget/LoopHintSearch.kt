package com.bfu.loophintsearchview.widget

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.bfu.loophintsearchview.R
import com.bfu.loophintsearchview.base.App
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@ExperimentalAnimationApi
@Composable
fun LoopHintSearch(hints: List<String> = emptyList(), onClick: (String) -> Unit = {}) {

    var hint by remember { mutableStateOf(hints.firstOrNull().orEmpty()) }

    ConstraintLayout(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(20.dp)
            )
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick(hint)
            }
    ) {

        val (searchIcon, preHintText) = createRefs()

        /* 搜索图标. */
        Image(
            painter = painterResource(R.drawable.ic_search),
            contentDescription = "",
            modifier = Modifier
                .size(30.dp)
                .constrainAs(searchIcon) {
                    start.linkTo(parent.start, 12.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )


        /* 搜索提示文本. */
        HintText(
            text = hint,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .constrainAs(preHintText) {
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                    start.linkTo(searchIcon.end)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )

        /* 轮播任务. */
        if (hints.isNotEmpty()) {
            LaunchedEffect(hints) {
                while (isActive) {
                    hints.forEach {
                        hint = it
                        delay(App.ANIM_DURATION + App.ITEM_SHOWING_DURATION)
                    }
                }
            }
        }
    }

}

@ExperimentalAnimationApi
@Composable
fun HintText(text: String, modifier: Modifier = Modifier) {
    AnimatedContent(
        targetState = text,
        transitionSpec = {
            val slideIn = slideInVertically(tween(durationMillis = App.ANIM_DURATION.toInt())) {
                it
            }
            val slideOut = slideOutVertically(tween(durationMillis = App.ANIM_DURATION.toInt())) {
                -it
            }
            slideIn with slideOut
        },
        modifier = modifier
    ) { targetHint ->
        Box(contentAlignment = Alignment.CenterStart) {
            Text(
                text = targetHint,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
            )
        }
    }
}

@ExperimentalAnimationApi
@Preview(
    showBackground = true,
)
@Composable
fun DefaultPreview() {
    LoopHintSearch(listOf("Lets Plan Your Trips"))
}