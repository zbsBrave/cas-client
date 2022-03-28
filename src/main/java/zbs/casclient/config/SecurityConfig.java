package zbs.casclient.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;
import zbs.casclient.config.cas.CasProperties;
import zbs.casclient.config.cas.CasTicketValidFilter;

import javax.annotation.Resource;

/**
 * @author zhangbaisen
 * @date 2022/3/23 12:05
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Resource
    private CasTicketValidFilter casTicketValidFilter;
    @Resource
    private AuthenticationEntryPoint casAuthEntryPoint;
    @Resource
    private CasProperties casProperties;
    @Resource
    private SingleSignOutFilter singleSignOutFilter;

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        super.configure(web);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
        //指定作用范围
//        http.requestMatchers()
//                        .antMatchers("/scu/*");
        
        http.authorizeRequests()
                //允许预检请求
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .cors();
        
        //cas 相关
        http.addFilterBefore(casTicketValidFilter,UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(casAuthEntryPoint);
        http.logout()
                .logoutUrl(casProperties.getClientPathLogout())
                .logoutSuccessUrl(casProperties.getLogoutRedirectUrl())
                .permitAll();
        
        //cas单点登出，会拦截所有请求。认证时保存ticket和session；退出时，清除对应ticket和session。
        //所以要配合 SingleSignOutHttpSessionListener 使用，在session销毁时清除
        //http.addFilterBefore(singleSignOutFilter,CasTicketValidFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }
}
