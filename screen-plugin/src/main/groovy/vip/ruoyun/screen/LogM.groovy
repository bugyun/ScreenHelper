package vip.ruoyun.screen


class LogM {
    /**
     * 打印日志
     *
     * @param log
     */
    public static void log(Object log) {
        if (ScreenPlugin.isDebug) {
            println(log)
        }
    }
}