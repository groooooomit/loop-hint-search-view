package com.bfu.loophintsearchview.preinstall

import java.io.File


fun File.exec(command: () -> String) {
    val cmd = command()
    println()
    println("[$cmd]")
    Runtime.getRuntime().exec(cmd, null, this).inputStream.readAllBytes().decodeToString()
        .apply(::println)
}
