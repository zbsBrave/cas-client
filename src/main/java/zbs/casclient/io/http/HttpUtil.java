package zbs.casclient.io.http;

import org.springframework.http.HttpHeaders;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * @author zbs
 * @since 2022/9/6 11:46
 */
public interface HttpUtil {
    
    /**
     GET /static/home.html HTTP/1.1

     Host: localhost:8080

     Connection: keep-alive

     Cache-Control: max-age=0

     Upgrade-Insecure-Requests: 1

     User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36

     Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng;q=0.8,application/signed-exchange;v=b3

     Accept-Encoding: gzip, deflate, br

     Accept-Language: zh-CN,zh;q=0.9
     
     */
    static HttpRequest decode(byte[] data){
        HttpRequest request = new HttpRequest();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));){
            //1，解析起始行： GET /static/home.html HTTP/1.1
            String firstLine = reader.readLine();
            String[] split = firstLine.split(" ");
            request.setMethod(split[0]);
            request.setUrl(split[1]);
            request.setVersion(split[2]);
            
            //2，解析首部，以空行结束. Host: localhost:8080
            while (true){
                String line = reader.readLine();
                if("".equals(line.trim())){
                    //首部以空行结束
                    break;
                }
                int i = line.indexOf(":");
                request.setHeader(line.substring(0,i), line.substring(i+1).trim());
            }
            
            //3，解析body
            StringBuilder sb = new StringBuilder();
            String line;
            while ( (line = reader.readLine()) != null){
                sb.append(line);
            }
            request.setBody( sb.length() > 0 ? sb.toString() : null);

        }catch (Exception e){
            System.out.println("http decode err !!! ");
            e.printStackTrace();
        }
        
        return request;
    }
    
    static byte[] encode(HttpResponse response){
        StringBuilder sb = new StringBuilder();
        //1，响应起始行  HTTP/1.1 200 ok
        sb.append("HTTP/1.1 ")
                .append(response.getCode()).append(" ")
                .append(response.getStatus()).append("\r\n");
        //响应头
        if(response.getBody() != null && response.getBody().length() > 0){
            sb.append("Content-Length:").append(response.getBody().length()).append("\r\n");
            sb.append("Content-Type:text/html\r\n");
        }
        if(response.getHeaders() != null && !response.getHeaders().isEmpty()){
            String headStr = response.getHeaders().entrySet().stream()
                    .map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.joining("\r\n"));
            sb.append(headStr).append("\r\n");
        }
        //首部以空行结束
        sb.append("\r\n");
        if(response.getBody() != null && response.getBody().length() > 0){
            sb.append(response.getBody());
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

}
