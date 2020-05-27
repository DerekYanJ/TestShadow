package com.yqy.test.shadow.maneger

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.tencent.shadow.dynamic.host.EnterCallback
import com.yqy.test.constant.Constant.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SamplePluginManager(val mCurrentContext: Context) : FastPluginManager(mCurrentContext) {

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    override fun getName(): String {
        return "plugin-maneger"
    }

    override fun getPluginProcessServiceName(partKey: String): String {
        return "com.tencent.shadow.sample.host.PluginProcessPPS"
    }

    override fun enter(context: Context, fromId: Long, bundle: Bundle, enterCallback: EnterCallback?) {
        if (fromId > 1000) {
            startActivity(context, bundle, enterCallback)
        } else {
            throw IllegalArgumentException("不认识的fromId:$fromId")
        }
    }

    private fun startActivity(context: Context, bundle: Bundle, callback: EnterCallback?) {
        val pluginZipPath = bundle.getString(KEY_PLUGIN_ZIP_PATH)
        val pluginPartKey = bundle.getString(KEY_PLUGIN_PART_KEY)
        val activityClass = bundle.getString(KEY_ACTIVITY_CLASSNAME)
        val extraBundle = bundle.getBundle(KEY_EXTRA_KEY)

        requireNotNull(pluginZipPath)
        requireNotNull(pluginPartKey)
        requireNotNull(activityClass)

        if (callback != null) {
            val view = LayoutInflater.from(mCurrentContext).inflate(R.layout.view_plugin_manager_loading, null)
            callback.onShowLoadingView(null)
        }

        executorService.execute {
            try {
                val installPlugin = installPlugin(pluginZipPath, null, true)
                val intent = Intent().setComponent(ComponentName(context, activityClass))
                extraBundle?.let(intent::putExtras)
                startPluginActivity(context, installPlugin, pluginPartKey, intent)
            } catch (e: Exception) {
                throw RuntimeException("启动插件时发生了错误。", e)
            } finally {
                callback?.onCloseLoadingView()
                callback?.onEnterComplete()
            }
        }

    }

}