package zbs.casclient.config.cas;

import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

/**
 * CasProperties, CasTicketValidFilter, casAuthEntryPoint, logout
 *      1，CasProperties cas相关配置
 *      2，CasTicketValidFilter cas认证拦截器，拦截所有携带ticket的请求，向cas服务器验证ticket
 *      3，casAuthEntryPoint 认证失败重定向到cas登录页
 *      4，logout 登出
 *          这里没有配置cas单点登出 SingleSignOutFilter ，因为这里暂时不需要
 *              SingleSignOutFilter会拦截所有请求。认证时保存ticket和session；退出时，清除对应ticket和session。
 *              所以要配合 SingleSignOutHttpSessionListener 使用，在session销毁时清除
 *                  SingleSignOutFilter = new SingleSignOutFilter();
 *                  singleSignOutFilter.setIgnoreInitConfiguration(true);
 *                  http.addFilterBefore(singleSignOutFilter,CasTicketValidFilter.class)
 * @author zhangbaisen
 * @date 2022/3/29 10:42
 */
public class CasConfigurer<B extends HttpSecurityBuilder<B>, T extends CasConfigurer<B, T>>
        extends AbstractHttpConfigurer<T, B> {
    private CasProperties casProperties;
    private CasTicketValidFilter filter;
    private AuthenticationEntryPoint casAuthEntryPoint;
    private AuthenticationSuccessHandler successHandler;

    @Override
    public void init(B http) throws Exception {
        System.out.println("1, CasConfigurer init -------------------------------------");

        ApplicationContext context = http.getSharedObject(ApplicationContext.class);
        if (this.casProperties == null) {
            this.casProperties = context.getBean(CasProperties.class);
        }
        if (this.casAuthEntryPoint == null) {
            this.casAuthEntryPoint = createDefaultCasAuthEntryPoint();
        }


        LogoutConfigurer<B> logoutConfigurer = getBuilder().getConfigurer(LogoutConfigurer.class);
        if (logoutConfigurer != null) {
            logoutConfigurer
                    .logoutUrl(casProperties.getClientPathLogout())
                    .logoutSuccessUrl(casProperties.getLogoutRedirectUrl())
                    .permitAll();
        }

        ExceptionHandlingConfigurer<B> exceptionConfigurer = getBuilder().getConfigurer(ExceptionHandlingConfigurer.class);
        if (exceptionConfigurer != null) {
            exceptionConfigurer.authenticationEntryPoint(casAuthEntryPoint);
        }

    }


    @Override
    public void configure(B http) throws Exception {
        System.out.println("2, CasConfigurer config -------------------------------------");

        //设置cas认证过滤器
        if (this.filter == null) {
            this.filter = createDefaultCasTicketValidFilter();
        }
        if (this.successHandler != null) {
            this.filter.setSuccessHandler(this.successHandler);
        }

        SessionAuthenticationStrategy sessionAuthenticationStrategy = http
                .getSharedObject(SessionAuthenticationStrategy.class);
        if (sessionAuthenticationStrategy != null) {
            this.filter.setSessionStrategy(sessionAuthenticationStrategy);
        }

        RememberMeServices rememberMeServices = http.getSharedObject(RememberMeServices.class);
        if (rememberMeServices != null) {
            this.filter.setRememberMeServices(rememberMeServices);
        }

        CasTicketValidFilter f = postProcess(this.filter);
        setOrder(http, f);
    }

    public void setOrder(B http, CasTicketValidFilter f) {
        http.addFilterBefore(f, UsernamePasswordAuthenticationFilter.class);
    }

    public CasTicketValidFilter createDefaultCasTicketValidFilter() {
        CasTicketValidFilter f = new CasTicketValidFilter(this.casProperties);
        f.setTicketValidator(new Cas30ServiceTicketValidator(casProperties.getServerName()));
        return f;
    }

    public AuthenticationEntryPoint createDefaultCasAuthEntryPoint() {
        return new CasAuthEntryPoint(this.casProperties);
    }

    public T setCasProperties(CasProperties casProperties) {
        this.casProperties = casProperties;
        return getSelf();
    }

    public T setCasAuthEntryPoint(AuthenticationEntryPoint casAuthEntryPoint) {
        this.casAuthEntryPoint = casAuthEntryPoint;
        return getSelf();
    }

    public T setSuccessHandler(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
        return getSelf();
    }

    private T getSelf() {
        return (T) this;
    }
}
