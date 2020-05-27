package com.yqy.test.loader

import android.content.ComponentName
import android.content.Context
import android.util.Log
import com.tencent.shadow.core.loader.infos.ContainerProviderInfo
import com.tencent.shadow.core.loader.managers.ComponentManager

class SimpleComponentManager(private val context: Context) : ComponentManager() {

    override fun getBroadcastInfoList(partKey: String): List<BroadcastInfo> {
        Log.e("SimpleComponentManager", "getBroadcastInfoList\tpartKey = [${partKey}]")
        return emptyList()
    }

    /**
     * 配置插件Activity 到 壳子Activity的对应关系
     *
     * @param pluginActivity 插件Activity
     * @return 壳子Activity
     */
    override fun onBindContainerActivity(pluginActivity: ComponentName): ComponentName {
        Log.e("SimpleComponentManager", "onBindContainerActivity\tpluginActivity = [${pluginActivity}]")
        return ComponentName(context, "com.yqy.test.shadow.runtime.PluginDefaultProxyActivity")
    }

    /**
     * 配置对应宿主中预注册的壳子contentProvider的信息
     */
    override fun onBindContainerContentProvider(pluginContentProvider: ComponentName): ContainerProviderInfo {
        Log.e("SimpleComponentManager", "onBindContainerContentProvider\tpluginContentProvider = [${pluginContentProvider}]")
        return ContainerProviderInfo(
                "com.tencent.shadow.core.runtime.container.PluginContainerContentProvider",
                "com.yqy.test.shadow.plugin.authority.dynamic"
        )
    }

}