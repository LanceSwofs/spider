package service.third;

import com.baidu.aip.ocr.AipOcr;
import net.sf.json.util.JSONUtils;
import org.json.JSONObject;
import service.third.entity.CommonResult;
import service.third.entity.WordsResult;
import utils.DateMorpherEx;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaiduAIPService {
    //设置APPID/AK/SK
    private static final String APP_ID = "your app_id";
    private static final String API_KEY = "your api_key";
    private static final String SECRET_KEY = "your secret_key";

    public String ocrStrByLocalImg(String localImgPath){
        net.sf.json.JSONObject jsonObject = ocrByLocalImg(localImgPath);
        Map<String, Class> classMap = new HashMap<String, Class>();
        classMap.put("words_result", WordsResult.class);
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpherEx(new String[]{"yyyy-MM-dd HH:mm:ss"}, (Date) null));
        CommonResult commonResult = (CommonResult) net.sf.json.JSONObject.toBean(net.sf.json.JSONObject.fromObject(jsonObject), CommonResult.class, classMap);
        //未识别到返回空字符串
        if (commonResult.getWords_result_num() == 0) {
            return "";
        }
        //拼接所有字符串
        return commonResult.getWords_result().stream().map(WordsResult::getWords).reduce("", String::concat);
    }


    /**
     * Baidu通用文字识别,本方法使用本地图片
     * (使用本地二进制文件或者网络文件请参考Baidu官方文档)
     * @param localImgPath 本地图片地址
     * @throws IOException
     */
    public net.sf.json.JSONObject ocrByLocalImg(String localImgPath) {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", "proxy_port");  // 设置http代理
//        client.setSocketProxy("proxy_host", "proxy_port");  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
//        System.setProperty("aip.log4j.conf", "to/you/path/log4j.properties");

        // 调用接口

        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("language_type", "CHN_ENG");
        options.put("detect_direction", "true");
        options.put("detect_language", "true");
        options.put("probability", "true");


        // 参数为本地图片路径
        JSONObject res = client.basicAccurateGeneral(localImgPath, options);
        //TODO 异常处理
        return net.sf.json.JSONObject.fromObject(res.toString(2));

        // 参数为本地图片二进制数组
//        byte[] file = readImageFile(image);
//        res = client.basicGeneral(file, options);
//        System.out.println(res.toString(2));


        // 通用文字识别, 图片参数为远程url图片
//        JSONObject res = client.basicGeneralUrl(url, options);
//        System.out.println(res.toString(2));
    }
}
