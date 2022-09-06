package zbs.casclient.io.http;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zbs
 * @since 2022/9/6 11:08
 */
@Data
public class HttpRequest {
    private String version;
    private String method;
    private String url;
    private String body;
    private Map<String, String> headers = new HashMap<>();

    public HttpRequest setHeader(String key, String val) {
        headers.put(key, val);
        return this;
    }
}
