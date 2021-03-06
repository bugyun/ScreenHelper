package vip.ruoyun.screen

import com.android.utils.FileUtils
import org.dom4j.Document
import org.dom4j.DocumentException
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.dom4j.io.SAXReader
import org.dom4j.io.XMLWriter

import java.text.DecimalFormat

/**
 * <?xml version="1.0" encoding="utf-8"?>
 * <resources>
 * <dimen name="x20">7.361963190184049dp</dimen>
 * </resources>
 */
class DimenTools {

    private ArrayList<String> dimenNameList = new ArrayList<>()
    private ArrayList<String> dimenTextList = new ArrayList<>()

    private ScreenExt screenExt
    private String sourcePath

    DimenTools(ScreenExt screenExt, String sourcePath) {
        this.screenExt = screenExt
        this.sourcePath = sourcePath
    }

    //流程
    void dimensCovert() {
        long beginTime = System.currentTimeMillis()
        LogM.log("-------ScreenHelper plugin 插件开始-------")
        LogM.log("===ScreenHelper plugin 清除文件开始===")
        clear()//1.清除之前的文件
        LogM.log("===ScreenHelper plugin 清除文件结束===")
        LogM.log("===ScreenHelper plugin 读取dimens文件开始===")
        read()//2.读取dimens文件
        LogM.log("===ScreenHelper plugin 读取dimens文件结束===")
        LogM.log("===ScreenHelper plugin 写入配置开始===")
        write()//3.创建文件，写入配置
        LogM.log("===ScreenHelper plugin 写入配置结束===")
        LogM.log("-------ScreenHelper plugin 插件结束,花费" + (System.currentTimeMillis() - beginTime) + "ms-------")
    }
    /**
     * 清除之前的文件
     */
    private void clear() {
        try {
            //找到文件res 下 values-sw 的文件
            File resFile = new File(sourcePath + "/src/main/res")
            LogM.log(resFile.toString())
            resFile.listFiles(new FilenameFilter() {
                @Override
                boolean accept(File dir, String name) {
                    //自定义过滤规则
                    return name.startsWith("values-sw")
                }
            }).each { file ->
                LogM.log(file.name)
                FileUtils.deletePath(file)
            }
        } catch (IOException e) {
            LogM.log(e.getMessage())
            e.printStackTrace()
        }
    }

    /**
     * 读取文件
     */
    private void read() {
        try {
            File file = new File(sourcePath + "/src/main/res/values/" + screenExt.dimensFileName + ".xml")
            if (!file.exists()) {
                LogM.log("文件不存在")
                return
            }
            SAXReader saxReader = new SAXReader()
            Document document = saxReader.read(file)
            Element root = document.getRootElement()// 获取根元素
            List<Element> childList = root.elements("dimen")// dimen 获取特定名称的子元素
            for (Element element : childList) {
                LogM.log("attributeValue::" + element.attributeValue("name") + " getText::" + element.getText())
                dimenNameList.add(element.attributeValue("name"))
                dimenTextList.add(element.getText())
            }
        } catch (DocumentException e) {
            LogM.log(e.getMessage())
            e.printStackTrace()
        }
    }

    /**
     * 写入文件
     */
    private void write() {
        for (Integer path : screenExt.smallestWidths) {
            String outPutFile = sourcePath + "/src/main/res/values-sw" + path + "dp/dimens.xml"
//            LogM.log(outPutFile)
            writeSingleFile(outPutFile, path)
        }
    }
    /**
     * 1920 * 1080   480 dpi的设备
     * 计算公式 : px / (DPI / 160) = dp
     * 计算过程 : 1080 / (480 / 160) = 360
     * smallestWidth 最小宽度 的值是 360 dp
     * @param fileName
     * @param size
     */
    private void writeSingleFile(String fileName, int size) {
        Document document = DocumentHelper.createDocument()
        Element rootElement = document.addElement("resources") //创建跟节点 resources
        DecimalFormat decimalFormat = new DecimalFormat(screenExt.decimalFormat) //格式化小数点位数,四舍五入
        for (int i = 0; i < dimenNameList.size(); i++) {
            String text = dimenTextList.get(i)
            if (text.endsWith("dp")) {
                Element dimen = rootElement.addElement("dimen")
                dimen.addAttribute("name", dimenNameList.get(i))
                float textNum = text.substring(0, text.length() - 2) as float
                def dimensNum = ((float) size / screenExt.designSmallestWidth) * textNum
                dimen.setText(decimalFormat.format(dimensNum) + "dp")
            }
        }
        try {
            File fileP = new File(fileName)
            if (!fileP.getParentFile().exists()) {
                fileP.getParentFile().mkdirs()
            }
            FileWriter fileWriter = new FileWriter(fileP)//
            XMLWriter xmlWriter = new XMLWriter(fileWriter)
            xmlWriter.write(document)
            xmlWriter.close()
        } catch (Exception e) {
            LogM.log(e.getMessage())
            e.printStackTrace()
        }
    }
}