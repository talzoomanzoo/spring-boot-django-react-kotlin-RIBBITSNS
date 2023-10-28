package com.hippoddung.ribbit.ui.screens.carditems

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.viewmodel.HomeViewModel
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CardTopBar(post: RibbitPost,
               homeViewModel: HomeViewModel,
               userId: Int,
               navController: NavHostController) {
    Row {
        Text(
            text = "No." + post.id.toString(),
            fontSize = 14.sp,
            modifier = Modifier.padding(4.dp),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = post.user.email,
            fontSize = 14.sp,
            modifier = Modifier.padding(4.dp),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = calculationTime(targetDateTimeStr = post.createdAt),
            fontSize = 14.sp,
            modifier = Modifier.padding(4.dp),
            style = MaterialTheme.typography.headlineSmall
        )
        if (post.editedAt != null) {
            Text(
                text = calculationTime(targetDateTimeStr = post.editedAt) + "수정됨",
                fontSize = 14.sp,
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.headlineSmall
            )
        }
        RibbitDropDownMenu(
            post = post,
            homeViewModel = homeViewModel,
            userId = userId,
            navController = navController)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculationTime(targetDateTimeStr: String): String {
    val currentDateTime = LocalDateTime.now()

    val formatterWith6Digits = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    val formatterWith5Digits = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSS")
    val formatterWith4Digits = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSS")
    val formatterWith3Digits = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")

    var value = ""

    var targetDateTime: LocalDateTime? = null

    try {
        targetDateTime = LocalDateTime.parse(targetDateTimeStr, formatterWith6Digits)
    } catch (e: Exception) {
        try {
            targetDateTime = LocalDateTime.parse(targetDateTimeStr, formatterWith5Digits)
        } catch (e: Exception) {
            try {
                targetDateTime = LocalDateTime.parse(targetDateTimeStr, formatterWith4Digits)
            } catch (e: Exception) {
                try {
                    targetDateTime = LocalDateTime.parse(targetDateTimeStr, formatterWith3Digits)
                } catch (e: Exception) {
                    Log.d("HippoLog, CardTopBar", "${e.message}")
                    return "확인중"
                }
            }
        }
    }
    val differenceValue = Duration.between(targetDateTime, currentDateTime).toMillis()

    when {
        differenceValue < 60000 -> {
            value = "방금 전"
        }

        differenceValue < 3600000 -> {
            value = TimeUnit.MILLISECONDS.toMinutes(differenceValue).toString() + "분 전"
        }

        differenceValue < 86400000 -> {
            value = TimeUnit.MILLISECONDS.toHours(differenceValue).toString() + "시간 전"
        }

        differenceValue < 604800000 -> {
            value = TimeUnit.MILLISECONDS.toDays(differenceValue).toString() + "일 전"
        }

        differenceValue < 2419200000 -> {
            value = (TimeUnit.MILLISECONDS.toDays(differenceValue) / 7).toString() + "주 전"
        }

        differenceValue < 31556952000 -> {
            value = (TimeUnit.MILLISECONDS.toDays(differenceValue) / 30).toString() + "개월 전"
        }

        else -> {
            value = (TimeUnit.MILLISECONDS.toDays(differenceValue) / 365).toString() + "년 전"
        }
    }
    return value
}