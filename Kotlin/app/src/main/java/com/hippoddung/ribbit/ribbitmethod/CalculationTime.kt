package com.hippoddung.ribbit.ribbitmethod

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
fun calculationTime(targetDateTimeStr: String): String {
    val currentDateTime = LocalDateTime.now()
    val formatterWithNoDigits = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
//    val formatterWith6Digits = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
//    val formatterWith5Digits = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSS")
//    val formatterWith4Digits = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSS")
//    val formatterWith3Digits = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")

    val value: String

    val targetDateTime: LocalDateTime?

    try {
        targetDateTime = LocalDateTime.parse(targetDateTimeStr.substring(0, 19), formatterWithNoDigits)
    } catch (e: Exception) {
        Log.d("HippoLog, CardTopBar", "${e.message}")
        return "확인중"
    }

//    try {
//        targetDateTime = LocalDateTime.parse(targetDateTimeStr, formatterWith6Digits)
//    } catch (e: Exception) {
//        try {
//            targetDateTime = LocalDateTime.parse(targetDateTimeStr, formatterWith5Digits)
//        } catch (e: Exception) {
//            try {
//                targetDateTime = LocalDateTime.parse(targetDateTimeStr, formatterWith4Digits)
//            } catch (e: Exception) {
//                try {
//                    targetDateTime = LocalDateTime.parse(targetDateTimeStr, formatterWith3Digits)
//                } catch (e: Exception) {
//                    Log.d("HippoLog, CardTopBar", "${e.message}")
//                    return "확인중"
//                }
//            }
//        }
//    }
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