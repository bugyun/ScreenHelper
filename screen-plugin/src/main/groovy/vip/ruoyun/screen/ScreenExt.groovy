package vip.ruoyun.screen

/**
 * Created by ruoyun on 2018/6/21.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 * ldpi:mdpi:hdpi:xhdpi:xxhdpi=3:4:6:8:12
 *
 * 384,392,400,410,411,432,480,533,592,600,640,662,720,768,800,811,820,960,961,1024,1280,1365
 *
 */
class ScreenExt {

    String dimensFileName = "dimens"

    /**
     * 设计稿的 dpi 或 ppi
     * 设计原型图为 720*1080  320ppi 或 320dpi
     * 设计图 1080*1920  420dpi 或 420ppi
     * 苹果设计稿750 × 1334   326ppi  android :720 × 1080   320ppi
     *  适配Android 3.2以上   大部分手机的sw值集中在  300-460之间
     *
     *  1920 * 1080   480 dpi的设备
     *  计算公式 : px / (DPI / 160) = dp
     *  计算过程 : 1080 / (480 / 160) = 360
     *  smallestWidth 最小宽度 的值是 360 dp
     *
     *  1929 * 1080  420dip 的设备
     *  计算公式 : px / (DPI / 160) = dp
     *  计算过程 : 1080 / (420 / 160) = 411
     *  smallestWidth 最小宽度 的值是 411 dp
     */
    int designSmallestWidth = 375 //设计稿最小宽度

    /**
     * 小数点位数,默认一位小数,四舍五入
     */
    String decimalFormat = "#.#"

    boolean auto = false

    boolean log = false

    Set<Integer> smallestWidths = new HashSet<>()

    def dimensFileName(String dimensFileName) {
        this.dimensFileName = dimensFileName
    }

    def decimalFormat(String decimalFormat) {
        this.decimalFormat = decimalFormat
    }

    def designSmallestWidth(int designSmallestWidth) {
        this.designSmallestWidth = designSmallestWidth
    }

    def smallestWidths(Integer... sw) {
        this.smallestWidths.addAll(sw)
    }

    def auto(boolean auto) {
        this.auto = auto
    }

    def log(boolean log) {
        this.log = log
    }


}