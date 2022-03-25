package zbs.casclient.config;

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
    
   

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
        
        http.authorizeRequests()
                //允许预检请求
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest().authenticated()
                .and()
                .logout().permitAll()
                .logoutUrl("logoutCas")
                .and()
                .csrf().disable()
                .cors();
        
        //cas 相关
        http.addFilterBefore(casTicketValidFilter,UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(casAuthEntryPoint);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }
}
