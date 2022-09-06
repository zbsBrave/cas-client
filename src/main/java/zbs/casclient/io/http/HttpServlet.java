package zbs.casclient.io.http;

/**
 * @author zbs
 * @since 2022/9/6 11:12
 */
public class HttpServlet {
    public void doGet(HttpRequest request, HttpResponse response) {
        //模拟业务代码
        response.setBody(String.format("<html><h1>Hello World!</h1><h2>%s</h2></html>", request.getUrl()));
    }

    public void doPost(HttpRequest request, HttpResponse response) {
        doGet(request, response);
    }
}
