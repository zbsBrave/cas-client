package zbs.casclient.io.http;

import lombok.Data;

import java.util.Map;

/**
 * @author zbs
 * @since 2022/9/6 11:10
 */
@Data
public class HttpResponse {
    private String version;
    private int code = 200;
    private String status = "ok";
    private String body;
    private Map<String, String> headers;
}
