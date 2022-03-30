package zbs.casclient.config.cas;

import lombok.Data;
import org.jasig.cas.client.Protocol;
import org.jasig.cas.client.util.CommonUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangbaisen
 * @date 2022/3/25 18:00
 */
@Data
@ConfigurationProperties(prefix = "cas")
public class CasProperties {
    private final Protocol protocol = Protocol.CAS3;
    private String serverName;
    private String serverLoginUrl;
    private String serverLogoutUrl;
    private String clientName;
    private String clientPathLogout = "/logoutCas";
    private boolean encodeServiceUrl = true;

    /**
     * Whether to send the renew request or not.
     */
    private boolean renew = false;

    /**
     * Whether to send the gateway request or not.
     */
    private boolean gateway = false;

    public final String constructServiceUrl(final HttpServletRequest request, final HttpServletResponse response) {
        //service : The exact url of the service.
        return CommonUtils.constructServiceUrl(request, response, null, this.clientName,
                this.protocol.getServiceParameterName(),
                this.protocol.getArtifactParameterName(), this.encodeServiceUrl);
    }
    
    public final String constructRedirectUrl(String modifiedServiceUrl){
        return CommonUtils.constructRedirectUrl(this.serverLoginUrl,
                getProtocol().getServiceParameterName(), modifiedServiceUrl, this.renew, this.gateway);
    }
    
    /** 登出时重定向到登录页 */
    public final String getLogoutRedirectUrl(){
        return this.serverLogoutUrl + "?service=" + this.getClientName();
    }
    
}
