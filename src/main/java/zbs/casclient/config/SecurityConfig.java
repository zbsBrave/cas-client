package zbs.casclient.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsUtils;
import zbs.casclient.config.cas.CasConfigurer;
import zbs.casclient.config.cas.CasProperties;

@EnableWebSecurity
@EnableConfigurationProperties(CasProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        super.configure(web);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
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
        //cas相关
        http.apply(new CasConfigurer<>());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }
}
