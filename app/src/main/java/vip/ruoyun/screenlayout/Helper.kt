package vip.ruoyun.screenlayout

import android.content.res.Resources
import vip.ruoyun.screen.ScreenHelper

/**
 * Created by ruoyun on 2019-05-08.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:帮助类
 */

class Helper {

    companion object {
        @JvmField
        var isOpenScreen: Boolean = true

        @JvmStatic
        fun apply(resources: Resources): Resources {
            if (isOpenScreen) {
                return ScreenHelper.applyAdapt(resources, 480f, ScreenHelper.ScreenMode.WIDTH_DP)
            } else {
                return ScreenHelper.closeAdapt(resources)
            }
        }
    }

}