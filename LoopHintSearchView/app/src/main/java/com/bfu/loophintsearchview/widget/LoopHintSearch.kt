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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.bfu.loophintsearchview.base.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

@ExperimentalAnimationApi
@Composable
fun LoopHintSearch(hints: List<String> = emptyList()) {
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

        val (searchIcon, preHintText) = createRefs()

        Icon(
            painter = painterResource(android.R.drawable.ic_menu_search),
            contentDescription = "",
            modifier = Modifier.constrainAs(searchIcon) {
                start.linkTo(parent.start, 12.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        )

        var hint by remember { mutableStateOf("") }

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

        HintText(
            text = hint,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .constrainAs(preHintText) {
                    width = Dimension.fillToConstraints
                    start.linkTo(searchIcon.end)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
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
        Text(
            text = targetHint,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp,
        )
    }
}

@ExperimentalAnimationApi
@Preview(
    showBackground = true,
)
@Composable
fun DefaultPreview() {
    LoopHintSearch()
}