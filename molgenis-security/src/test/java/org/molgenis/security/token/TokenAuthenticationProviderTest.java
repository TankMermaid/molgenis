package org.molgenis.security.token;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import org.molgenis.security.core.token.TokenService;
import org.molgenis.security.core.token.UnknownTokenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TokenAuthenticationProviderTest {
  private TokenAuthenticationProvider tokenAuthenticationProvider;
  private TokenService tokenService;

  @BeforeMethod
  public void beforeMethod() {
    tokenService = mock(TokenService.class);
    tokenAuthenticationProvider =
        new TokenAuthenticationProvider(tokenService, mock(UserDetailsChecker.class));
  }

  @Test
  public void authenticate() {
    RestAuthenticationToken authToken = new RestAuthenticationToken("token");
    assertFalse(authToken.isAuthenticated());

    when(tokenService.findUserByToken("token"))
        .thenReturn(
            new User("username", "password", Arrays.asList(new SimpleGrantedAuthority("admin"))));

    Authentication auth = tokenAuthenticationProvider.authenticate(authToken);
    assertNotNull(auth);
    assertTrue(auth.isAuthenticated());
    assertEquals(auth.getName(), "username");
    assertEquals(auth.getAuthorities().size(), 1);
    assertEquals(auth.getAuthorities().iterator().next().getAuthority(), "admin");
  }

  @Test(expectedExceptions = AuthenticationException.class)
  public void authenticateInvalidToken() {
    when(tokenService.findUserByToken("token"))
        .thenThrow(new UnknownTokenException("Invalid token"));
    tokenAuthenticationProvider.authenticate(new RestAuthenticationToken("token"));
  }
}
