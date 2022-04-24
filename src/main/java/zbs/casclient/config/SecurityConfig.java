package zbs.casclient.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsUtils;
import zbs.casclient.config.cas.CasConfigurer;
import zbs.casclient.config.cas.CasProperties;

@EnableWebSecurity
//@EnableConfigurationProperties(CasProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //指定作用范围
//        http.requestMatchers()
//                        .antMatchers("/scu/*");

        http.authorizeRequests()
                //允许预检请求
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/oauth2/keys").permitAll()//对外暴露公钥
                .antMatchers("/test/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .cors();
        //cas相关
//        http.apply(new CasConfigurer<>());

        //测试oauth2认证
        http.httpBasic();
        
        //资源服务器
//        http.oauth2ResourceServer()
//                .jwt().jwkSetUri("http://localhost:8080/oauth2/keys");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //测试oauth2认证
        auth.userDetailsService(username -> User.withUsername(username)
                .password(new BCryptPasswordEncoder().encode("123"))
                .roles("user").build()).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // 必须重写 void configure(AuthenticationManagerBuilder auth) 方法，
        // 不然报错：A dependency cycle was detected when trying to resolve the AuthenticationManager
        return super.authenticationManagerBean();
    }
}
