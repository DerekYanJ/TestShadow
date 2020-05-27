package com.yqy.test.shadow

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Process
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.webkit.WebView
import com.tencent.shadow.core.common.LoggerFactory
import com.tencent.shadow.dynamic.host.DynamicRuntime
import com.tencent.shadow.dynamic.host.PluginManager
import com.tencent.shadow.sample.host.lib.HostUiLayerProvider
import com.yqy.test.shadow.manager.Shadow
import java.io.File

/**
 * @desc
 * @author derekyan
 * @date 2020/5/25
 */
class App : Application() {

    private var mPluginManager: PluginManager? = null


    companion object {
        private lateinit var context: Context
        private lateinit var self: App

        fun getContext(): Context {
            return context
        }

        //获取Application实例一定要用这个方法，不要重新创建
        fun instance(): App {
            return self
        }
    }

    override fun onCreate() {
        super.onCreate()

        self = this
        context = this


//        detectNonSdkApiUsageOnAndroidP()
//        setWebViewDataDirectorySuffix()
        LoggerFactory.setILoggerFactory(AndroidLogLoggerFactory())

        if (isProcess(this, ":plugin")) {
            //在全动态架构中，Activity组件没有打包在宿主而是位于被动态加载的runtime，
            //为了防止插件crash后，系统自动恢复crash前的Activity组件，此时由于没有加载runtime而发生classNotFound异常，导致二次crash
            //因此这里恢复加载上一次的runtime
            DynamicRuntime.recoveryRuntime(this)
        }


        //复制asset插件到file中
        PluginHelper.getInstance().init(this)

        HostUiLayerProvider.init(this)

    }


    fun loadPluginManager(apk: File?) {
        if (mPluginManager == null) {
            mPluginManager = Shadow.getPluginManager(apk)
        }
    }


    fun getPluginManager(): PluginManager? {
        return mPluginManager
    }

    private fun isProcess(
        context: Context,
        processName: String
    ): Boolean {
        var currentProcName = ""
        val manager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == Process.myPid()) {
                currentProcName = processInfo.processName
                break
            }
        }
        return currentProcName.endsWith(processName)
    }

    private fun setWebViewDataDirectorySuffix() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return
        }
        WebView.setDataDirectorySuffix(getProcessName())
    }

    private fun detectNonSdkApiUsageOnAndroidP() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return
        }
        val builder = VmPolicy.Builder()
        builder.detectNonSdkApiUsage()
        StrictMode.setVmPolicy(builder.build())
    }
}