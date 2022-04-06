/**
 * 
 *  oauth2授权服务器
 *      1，新版本的授权服务器在开发中，还未完善
 *          spring-security-oauth2-authorization-server
 *      2，所以这里使用老版本的oauth2授权
 *          参考：https://github.com/jgrandja/spring-security-oauth-2-4-migrate.git
 * 
 *      相关端口实现细节查看 org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint
 * 
 * 
 * ------------------------ 暴露的端口 ------------------------
 *      /oauth/authorize   GET/POST  org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint
 *      /oauth/check_token      org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint
 *      /oauth/confirm_access   org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint
 *      /oauth/error            org.springframework.security.oauth2.provider.endpoint.WhitelabelErrorEndpoint
 *      /oauth/token  GET/POST  org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
 *      /oauth/token_key        org.springframework.security.oauth2.provider.endpoint.TokenKeyEndpoint
 * 
 * 
 * ------------------------ 流程 ------------------------
 * 1，获取code
 *  http://localhost:8080/oauth/authorize
 *      ?client_id=messaging-client&redirect_uri=http://app.example.com/oauth2/callback&response_type=code&scope=message.read%20message.write&state=eUVjbC
 *      client_id：客户端的ID，必选项
 *      response_type：授权类型，必选项，此处的值固定为"code"
 *      redirect_uri：重定向URI，可选项
 *      scope：申请的权限范围，可选项
 *      state：可选项，表示客户端的当前状态，可以指定任意值，认证服务器会原封不动地返回这个值
 *      
 * 2，用户确认授权，userApprovalHandler
 *      会重定向到 http://app.example.com/oauth2/callback?code=c4884y&state=eUVjbC
 *      
 * 3，根据 code 换取 accessToken，POST。此处最好由客户端后台来请求，因为包含客户端密钥这类敏感信息
 *  http://localhost:8080/oauth/token
 *      ?code=tHCDPJ&client_id=messaging-client&client_secret=secret&grant_type=authorization_code&redirect_uri=http://app.example.com/oauth2/callback
 *      code：上面获取的code
 *      client_id：客户端的ID，必选项
 *      client_secret：客户端的密钥，必选项
 *      grant_type：授权类型，必选项，authorization_code
 *      redirect_uri：重定向URI，必选项
 *  返回：access_token，token_type，refresh_token，expires_in，scope，jti
 *     
 * 4，获取到access token之后，就可以在Authorization header中使用token，进行对资源服务器的请求访问
 *      curl -H "Authorization: Bearer 2YotnFZFEjr1zCsicMWpAA" http://app.example.com
 *      
 * @author zbs
 * @since 2022/4/2 18:26
 */
package zbs.casclient.config.oauth2.auth;