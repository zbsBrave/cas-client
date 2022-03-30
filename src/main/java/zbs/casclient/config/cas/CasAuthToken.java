package zbs.casclient.config.cas;

import org.jasig.cas.client.validation.Assertion;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CasAuthToken extends UsernamePasswordAuthenticationToken {
    private final Assertion assertion;
    public CasAuthToken(Object principal, Object credentials, Assertion assertion) {
        super(principal, credentials);
        this.assertion = assertion;
    }

    public CasAuthToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Assertion assertion) {
        super(principal, credentials, authorities);
        this.assertion = assertion;
    }

    public Assertion getAssertion() {
        return assertion;
    }

}
