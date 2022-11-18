package com.bfu.loophintsearchview.preinstall

import org.junit.Test
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class TestPreInstall {

    companion object {
        private const val DIR = "/Users/fubo/Desktop/rongyao-pre-install"
        private const val CHANNEL_APK_NAME = "pre.apk"
        private const val BUILD_TOOLS_DIR = "/Users/fubo/works/devs/android-sdk/build-tools/31.0.0"
        private const val IBUAndroid_DIR = "/Users/fubo/works/trip-gitlab/IBUAndroid"
    }


    @Test
    fun fixedApk() {
//        pickSo()
        generateCmd()
    }

    private fun generateCmd() {
        val dir = File(DIR)
        val channelApk = File(dir, CHANNEL_APK_NAME)
        println("[提取出 so] unzip ${channelApk.name} 'lib/*.so' -d uncompressedlibs")
        println("[删除 apk 内 so] zip -d ${channelApk.name} 'lib/*.so'")
        println("[重新打入 so] zip -D -r -0 ${channelApk.name} uncompressedlibs/lib")
        println("[重新打入 so] zip -D -r -0 ../${channelApk.name} lib")
        println("[zipalign] $BUILD_TOOLS_DIR/zipalign -f -p 4 ${channelApk.name} ${channelApk.nameWithoutExtension}_so.apk")
        println("$BUILD_TOOLS_DIR/zipalign -c -v -p 4 ${channelApk.nameWithoutExtension}_so.apk > out.txt")
        // // apksigner sign --ks (签名地址) --ks-key-alias (别名) --out (签名后的apk地址) (待签名apk地址)
        val outSoApk = File(dir,"${channelApk.nameWithoutExtension}_so.apk")
        val outSignedApk = File(dir,"${channelApk.nameWithoutExtension}_signed.apk")
        println("$BUILD_TOOLS_DIR/apksigner sign --ks $IBUAndroid_DIR/ibu_main/ctrip.keystone --ks-key-alias ctrip --out ${outSignedApk.name} ${outSoApk.name}")
        println("$BUILD_TOOLS_DIR/apksigner verify -v --print-certs")
    }

    private fun pickSo() {
        val dir = File(DIR)
        val channelApk = File(dir, CHANNEL_APK_NAME)
        val channelApkName = channelApk.name
        log { "======================= channelApkName: $channelApkName =======================" }
        log { "开始提取 so" }
        dir.exec { "unzip $channelApkName 'lib/*.so' -d uncompressedlibs" }
        dir.exec { "zip -d $channelApkName 'lib/*.so'" }
        dir.exec { "zip -D -r -0 $channelApkName uncompressedlibs/lib" }
        log { "提取 so 完成" }
    }

    private inline fun log(msg: () -> String) {
        println("${now()} [荣耀预装] ${msg()}")
    }

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ssss")

    private fun now(): String = dateFormat.format(Date())


}