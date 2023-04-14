package com.bfu.loophintsearchview

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")

fun log(msg: () -> String) {
    val time = LocalDateTime.now().format(dateFormatter)
    val threadName = Thread.currentThread().name
    println("$time [$threadName] ${msg()}")
}