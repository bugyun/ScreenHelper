package vip.ruoyun.screenlayout

import android.content.res.Resources
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun getResources(): Resources {
        return Helper.apply(super.getResources())
    }
}
