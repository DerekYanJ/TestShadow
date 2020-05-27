package com.yqy.test.loader

import android.content.Context
import android.content.pm.PackageManager
import com.tencent.shadow.core.common.InstalledApk
import com.tencent.shadow.core.loader.ShadowPluginLoader
import com.tencent.shadow.core.loader.managers.ComponentManager
import com.yqy.test.commlib.LoadPluginCallback
import java.util.concurrent.Future

class SimplePluginLoader(private val context: Context) : ShadowPluginLoader(context) {

    private val componentManager = SimpleComponentManager(context)

    override fun getComponentManager(): ComponentManager {
        return componentManager
    }

    override fun loadPlugin(installedApk: InstalledApk): Future<*> {
        val loadParameters = installedApk.getLoadParameters()
        //执行before回调
        val partKey = loadParameters.partKey
        LoadPluginCallback.getCallback().beforeLoadPlugin(partKey)
        //执行原操作
        val future = super.loadPlugin(installedApk)
        mExecutorService.submit {
            try {
                //执行after回调
                future.get()
                val pluginParts = getPluginParts(partKey)
                if (pluginParts != null) {
                    val packageName = pluginParts.application.packageName
                    val applicationInfo = pluginParts.pluginPackageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
                    val classLoader = pluginParts.classLoader
                    val resources = pluginParts.resources
                    LoadPluginCallback.getCallback().afterLoadPlugin(partKey, applicationInfo, classLoader, resources)
                }
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
        return future
    }
}