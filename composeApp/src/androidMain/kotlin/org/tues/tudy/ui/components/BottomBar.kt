package org.tues.tudy.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.tues.tudy.R
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.PrimaryColor1

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val selected = false
    val animatedColor by animateColorAsState(
        targetValue = if (selected) PrimaryColor1 else BaseColor80
    )

    val studyIcon = if (selected) R.drawable.study_filled else R.drawable.study_outlined
    val calendarIcon = if (selected) R.drawable.calendar_filled else R.drawable.calendar_outlined
    val plusIcon = if (selected) R.drawable.plus_filled else R.drawable.plus_outlined
    val homeIcon = if (selected) R.drawable.home_filled else R.drawable.home_outlined
    val profileIcon = if (selected) R.drawable.profile_filled else R.drawable.profile_outlined

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(BaseColor0)
    ) {


        // BACKGROUND IMAGE
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            Image(
                painter = painterResource(R.drawable.bottom_bar_outline),
                contentDescription = "Bottom bar outline",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .clickable() {
                        navController.navigate(Routes.STUDY)
                    },
                contentScale = ContentScale.FillBounds
            )
        }

        // FOREGROUND CONTENT
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.Space0)
                .height(72.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Row(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(studyIcon),
                        contentDescription = "Study",
                        tint = animatedColor,
                        modifier = Modifier.clickable {
                            navController.navigate(Routes.STUDY)
                        }
                    )
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(calendarIcon),
                        contentDescription = "Calendar",
                        tint = animatedColor,
                        modifier = Modifier.clickable {
                            navController.navigate(Routes.CALENDAR)
                        }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(plusIcon),
                    contentDescription = "Plus",
                    tint = PrimaryColor1,
                    modifier = Modifier.clickable { navController.navigate(Routes.ADD) }
                )
            }

            Row(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(homeIcon),
                        contentDescription = "Home",
                        tint = animatedColor,
                        modifier = Modifier.clickable {
                            navController.navigate(Routes.HOME)
                        }
                    )
                }

                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(profileIcon),
                        contentDescription = "Profile",
                        tint = animatedColor,
                        modifier = Modifier.clickable {
                            navController.navigate(Routes.PROFILE)
                        }
                    )
                }
            }

        }
    }
}

