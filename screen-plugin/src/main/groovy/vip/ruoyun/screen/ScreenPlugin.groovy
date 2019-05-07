package vip.ruoyun.screen

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by ruoyun on 2018/6/21.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */
class ScreenPlugin implements Plugin<Project> {
    private static final String EXTENSION_NAME = "screen"
    protected static boolean isDebug = true

    @Override
    void apply(Project project) {
        project.extensions.create(EXTENSION_NAME, ScreenExt)
        project.task("dimensCovert") {
            group 'ruoyun'
            doLast {
                ScreenExt ext = project.extensions.getByName(EXTENSION_NAME) as ScreenExt
                def dimenTools = new DimenTools(ext, project.getBuildFile().getParent())
                dimenTools.dimensCovert()
            }
        }

        project.afterEvaluate {
            def screenExt = project.extensions.screen
            isDebug = screenExt.log
            if (screenExt.auto) {
                LogM.log(':dimensCovert')
                def dimenTools = new DimenTools(screenExt, project.getBuildFile().getParent())
                dimenTools.dimensCovert()
            }
        }
    }
}