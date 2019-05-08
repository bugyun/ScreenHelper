package vip.ruoyun.screenlayout

import android.app.Application
import android.content.res.Resources

/**
 * Created by ruoyun on 2019-05-08.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */

class App : Application() {

    override fun getResources(): Resources {
        return Helper.apply(super.getResources())
    }

}