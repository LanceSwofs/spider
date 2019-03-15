import net.sf.json.JSONArray;
import org.apache.commons.io.IOUtils;
import service.third.BaiduAIPService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class EnterpriseEnrollInfoService {

    private static final String ENTERPRISE_ID = "123456";
    private static final String SCREENSHOT_DIR = "res" + File.separator + "screenshot";
    private static final String IMG_FORMAT = "png";

    /**
     * 读取选择器文件
     * @return
     * @throws IOException
     */
    private List<Map<String, Object>> read() throws IOException{
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("selector.json");
        String s = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        return  (List<Map<String, Object>>)JSONArray.fromObject(s);
    }

    public static void main(String[] args) throws Exception {
        EnterpriseEnrollInfoService enterpriseEnrollInfoService = new EnterpriseEnrollInfoService();
        //获取所有需要的元素及selector
        List<Map<String, Object>> selectorList = enterpriseEnrollInfoService.read();
        //截图到指定目录
        SpiderUtil.elementScreenshot("https://www.tianyancha.com/company/2448279431", ENTERPRISE_ID
                , selectorList, "#_container_baseInfo > table.table.-striped-col.-border-top-none > tbody > tr:nth-child(1) > td:nth-child(1)", 130);
        //读取目录下文件并调用百度文字识别
        enterpriseEnrollInfoService.enterpriseOCR(selectorList);
    }

    public void enterpriseOCR(List<Map<String, Object>> selectorList) throws IOException {
        String canonicalPath = new File("").getCanonicalPath();
        selectorList.forEach(selectorMap -> {
            String words = new BaiduAIPService().ocrStrByLocalImg( canonicalPath + File.separator
                    + SCREENSHOT_DIR + File.separator + ENTERPRISE_ID + File.separator
                    + selectorMap.get("key").toString() + "." + IMG_FORMAT);
            selectorMap.put("words", words);
        });
        System.out.println(selectorList);
    }
}
