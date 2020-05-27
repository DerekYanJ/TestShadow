package com.yqy.test.shadow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yqy.test.constant.Constant
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            val partKey = Constant.PART_KEY_PLUGIN_MAIN_APP
            val intent = Intent(this, PluginLoadActivity::class.java)
            intent.putExtra(Constant.KEY_PLUGIN_PART_KEY, partKey)
            intent.putExtra(Constant.KEY_ACTIVITY_CLASSNAME, "com.yqy.test.shadow.lib.SplashActivity")
            startActivity(intent)


        }
    }
}
