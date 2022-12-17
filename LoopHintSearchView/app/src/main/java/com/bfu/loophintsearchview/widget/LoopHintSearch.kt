package com.bfu.loophintsearchview.widget

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bfu.loophintsearchview.base.App

@ExperimentalAnimationApi
@Composable
fun LoopHintSearch() {
    ConstraintLayout(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(20.dp)
            )
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val (searchIcon, preHintText, nextHintText) = createRefs()
        Icon(
            painter = painterResource(android.R.drawable.ic_menu_search),
            contentDescription = "",
            modifier = Modifier.constrainAs(searchIcon) {
                start.linkTo(parent.start, 12.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        )

        val hint by viewModel<LoopHintSearchViewModel>()
            .hintListFlow.collectAsState("Lets Plan Your Trips")

        AnimatedContent(
            targetState = hint,
            transitionSpec = {
                val slideIn = slideIn(
                    animationSpec = tween(durationMillis = App.ANIM_DURATION.toInt())
                ) {
                    IntOffset(0, it.height)
                }
                val slideOut = slideOut(
                    animationSpec = tween(durationMillis = App.ANIM_DURATION.toInt())
                ) {
                    IntOffset(0, -it.height)
                }
                slideIn with slideOut
            },
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .constrainAs(preHintText) {
                    width = Dimension.fillToConstraints
                    start.linkTo(searchIcon.end)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        ) { targetHint ->
            HintText(text = targetHint)
        }
    }

}

@Composable
fun HintText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = Color.Black,
        maxLines = 1,
        fontSize = 14.sp,
        modifier = modifier
    )
}

@ExperimentalAnimationApi
@Preview(
    showBackground = true,
)
@Composable
fun DefaultPreview() {
    LoopHintSearch()
}