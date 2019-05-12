package com.base.hamoud.puffpuff.utils


fun mapNextTokeReminderValueToLong(string: String): Long {
    return when (string) {
        "5 mins" -> {
            5
        }
        "10 mins" -> {
            10
        }
        "15 mins" -> {
            15
        }
        "30 mins" -> {
            30
        }
        "45 mins" -> {
            45
        }
        "60 mins" -> {
            60
        }
        else -> {
            0 // "Off"
        }
    }
}

