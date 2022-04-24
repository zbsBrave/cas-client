/**
 * @author zbs
 * @since 2022/4/6
 *
 *  资源服务器
 *
 * 需要引入：
 *      1，spring-boot-starter-security
 *      2，spring-boot-starter-oauth2-resource-server
 * 配置：
 *      
 *      spring.security.oauth2.resourceserver.jwt.issuer-uri
 *      spring.security.oauth2.resourceserver.jwt.jwk-set-uri
 *

BearerTokenAuthenticationFilter
Authorization
access_token

JwtAuthenticationProvider

    jwkSetUri 一般是授权服务器提供的获取JWK配置的well-known端点，用来校验JWT Token。
    jwsAlgorithm 指定jwt使用的算法，默认 RSA-256。
    issuerUri 获取OAuth2.0 授权服务器元数据的端点。
    publicKeyLocation 用于解码的公钥路径，作为资源服务器来说将只能持有公钥，不应该持有私钥

BearerTokenAuthenticationFilter
JwtAuthenticationToken

NimbusJwtDecoder.withJwkSetUri(uri).build();
 
 
 */
package zbs.casclient.config.oauth2.resource;