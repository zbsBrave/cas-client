package zbs.casclient.config.cas;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.annotation.Resource;

/**
 * @author zhangbaisen
 * @date 2022/3/25 18:33
 */
@Configuration
@EnableConfigurationProperties(CasProperties.class)
public class CasAutoConfig {
    @Resource
    private CasProperties casProperties;
    
    @Bean
    public AuthenticationEntryPoint casAuthEntryPoint(){
        return new CasAuthEntryPoint(casProperties);
    }
    
    @Bean
    public CasTicketValidFilter casTicketValidFilter(){
        CasTicketValidFilter f = new CasTicketValidFilter(casProperties);
        f.setTicketValidator(new Cas30ServiceTicketValidator(casProperties.getServerName()));
        return f;
    }

    @Bean
    public SingleSignOutFilter singleSignOutFilter(){
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

//    @Bean
//    public LogoutFilter requestSingleLogoutFilter(){
//        LogoutFilter logoutFilter = new LogoutFilter(casProperties.getLogoutRedirectUrl(), new SecurityContextLogoutHandler());
//        logoutFilter.setFilterProcessesUrl("logoutCas");
//        return logoutFilter;
//    }
}
