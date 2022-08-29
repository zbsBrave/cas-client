package zbs.casclient.nacosConfig;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 使用http来 增删改查 nacos配置
 *
 * @author zbs
 * @since 2022/8/26 18:05
 */
public class NacosConfigWithRest {
    public static final String CONFIG_PUBLISH_URI = "/nacos/v1/cs/configs";
    public static final String NAMESPACE_URI = "/nacos/v1/console/namespaces";

    public static void main(String[] args) {
        String namespace = "syhd-test";
        String url = "http://127.0.0.1:8848";
        
        File file = new File("D:\\data\\config-repo\\base-config-file\\a.yml");
        System.out.println(publishConfig(file, url, null, null));
    }

    public static Boolean publishConfigByFolder(String path, String url, String namespace, String group) {
        File folder = new File(path);
        if(!folder.exists() || !folder.isDirectory()){
            return publishConfig(folder, url, namespace, group);
        }
        File[] files = folder.listFiles();
        if(files == null || files.length < 1){
            return false;
        }
        boolean flag = true;
        for (File f : files){
            flag = flag && publishConfig(f, url, namespace, group);
        }
        return flag;
    }

    /**
     * 发布配置
     *
     * @param file      配置文件
     * @param url       nacos服务器地址
     * @param namespace 名称空间
     * @param group     组，为空时默认=DEFAULT_GROUP
     */
    public static Boolean publishConfig(File file, String url, String namespace, String group) {
        if (!file.exists() || file.isDirectory()) {
            return false;
        }
        //模拟表单请求，设置请求头 application/x-www-form-urlencoded
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("namespaceId", namespace);
        //租户信息，对应 Nacos 的命名空间ID字段。指定名称空间后，必须上传该字段，不然配置会发布到public
        param.add("tenant", namespace);
        param.add("group", group == null ? "DEFAULT_GROUP" : group);
        param.add("dataId", file.getName());
        param.add("type", getType(file));
        param.add("content", getContent(file));

        return new RestTemplate().postForObject(url + CONFIG_PUBLISH_URI, new HttpEntity<>(param, headers), Boolean.class);
    }

    private static String getContent(File file) {
        try (InputStream in = new FileInputStream(file)) {
            byte[] buf = new byte[in.available()];
            int readCount = in.read(buf);
            return new String(buf, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getType(File file) {
        try {
            String name = file.getName();
            if (name.endsWith(".properties")) {
                return "properties";
            }
            if (name.endsWith(".yml")) {
                return "yaml";
            }
        } catch (Exception ignore) {
        }

        return null;
    }

    public static Boolean namespace_create(String url, String namespace) {
        //模拟表单请求，设置请求头 application/x-www-form-urlencoded
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        //名称空间id
        param.add("customNamespaceId", namespace);
        //名称空间name
        param.add("namespaceName", namespace);
        //名称空间描述，可为空
        param.add("namespaceDesc", null);

        return new RestTemplate().postForObject(url + NAMESPACE_URI, new HttpEntity<>(param, headers), Boolean.class);

    }

    public static void namespace_del(String url, String namespaceId) {
        if(namespaceId == null || "".equals(namespaceId)){
            return;
        }
        new RestTemplate().delete(url + NAMESPACE_URI + "?namespaceId=" + namespaceId);
    }
}
