package vip.ruoyun.screenlayout

import android.content.res.Resources
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import vip.ruoyun.screen.ScreenHelper

class MainActivity : AppCompatActivity() {

    private var isOpenScreen: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun getResources(): Resources {
        if (isOpenScreen) {
            return ScreenHelper.applyAdapt(super.getResources(), 450f, ScreenHelper.ScreenMode.WIDTH_DP)
        } else {
            return ScreenHelper.closeAdapt(super.getResources())
        }
    }
}
