package zbs.casclient.config.cas;

import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CasTicketValidFilter extends OncePerRequestFilter {
    private final CasProperties casProperties;
    
    private TicketValidator ticketValidator;

    private SessionAuthenticationStrategy sessionStrategy = new NullAuthenticatedSessionStrategy();
    private RememberMeServices rememberMeServices = new NullRememberMeServices();
    protected ApplicationEventPublisher eventPublisher;
    private AuthenticationSuccessHandler successHandler;
    
    public CasTicketValidFilter(CasProperties casProperties){
        this.casProperties = casProperties;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String ticket = request.getParameter(casProperties.getProtocol().getArtifactParameterName());
        if(CommonUtils.isNotBlank(ticket)){
            try {
                String service = casProperties.constructServiceUrl(request, response);
                
                final Assertion assertion = this.ticketValidator.validate(ticket,service);
                UserDetails userDetails = loadUserByAssertion(assertion);
                Authentication casAuthToken = new CasAuthToken(userDetails.getUsername(), ticket, userDetails.getAuthorities(), assertion);
                
                this.sessionStrategy.onAuthentication(casAuthToken, request, response);
                
                successfulAuthentication(request, response, filterChain, casAuthToken);
                
                if(this.successHandler != null){
                    this.successHandler.onAuthenticationSuccess(request, response, casAuthToken);
                }else {
                    response.sendRedirect(service);
                }
            }catch (final TicketValidationException ex){
                log.error("cas auth err",ex);
                throw new BadCredentialsException(ex.getMessage(), ex);
            }
        }
        
        filterChain.doFilter(request,response);
    }

    /**
     * 参考 AbstractAuthenticationProcessingFilter
     */
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
//        SecurityContextHolder.getContext().setAuthentication(authResult);
        
        this.rememberMeServices.loginSuccess(request, response, authResult);
        if (this.eventPublisher != null) {
            this.eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
        }
    }

    protected UserDetails loadUserByAssertion(final Assertion assertion) {
        return User.withUsername(assertion.getPrincipal().getName()).password("no_passwd").roles("user").build();
    }



    public void setTicketValidator(TicketValidator ticketValidator) {
        this.ticketValidator = ticketValidator;
    }

    public void setSessionStrategy(SessionAuthenticationStrategy sessionStrategy) {
        this.sessionStrategy = sessionStrategy;
    }

    public void setRememberMeServices(RememberMeServices rememberMeServices) {
        this.rememberMeServices = rememberMeServices;
    }

    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void setSuccessHandler(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }
}
