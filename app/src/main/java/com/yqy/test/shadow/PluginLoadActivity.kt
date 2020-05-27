package com.yqy.test.shadow

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tencent.shadow.dynamic.host.EnterCallback
import com.yqy.test.constant.Constant
import kotlinx.android.synthetic.main.activity_plugin_load.*

class PluginLoadActivity : AppCompatActivity() {


    private val mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plugin_load)

        startPlugin()
    }

    fun startPlugin() {
        PluginHelper.getInstance().singlePool.execute(Runnable {
            App.instance()
                .loadPluginManager(PluginHelper.getInstance().pluginManagerFile)
            val bundle = Bundle()
            bundle.putString(
                Constant.KEY_PLUGIN_ZIP_PATH,
                PluginHelper.getInstance().pluginZipFile.absolutePath
            )
            bundle.putString(
                Constant.KEY_PLUGIN_PART_KEY,
                intent.getStringExtra(Constant.KEY_PLUGIN_PART_KEY)
            )
            bundle.putString(
                Constant.KEY_ACTIVITY_CLASSNAME,
                intent.getStringExtra(Constant.KEY_ACTIVITY_CLASSNAME)
            )
           App.instance().getPluginManager()
                ?.enter(
                    this@PluginLoadActivity,
                    Constant.FROM_ID_START_ACTIVITY,
                    bundle,
                    object : EnterCallback {
                        override fun onShowLoadingView(view: View?) {
                            Log.e("PluginLoad", "onShowLoadingView")
                            mHandler.post(Runnable {
                                if (view != null)
                                    container.addView(view)
                            })
                        }

                        override fun onCloseLoadingView() {
                            Log.e("PluginLoad", "onCloseLoadingView")
                            finish()
                        }

                        override fun onEnterComplete() {
                            Log.e("PluginLoad", "onEnterComplete")
                        }
                    })
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        container.removeAllViews()
    }
}
