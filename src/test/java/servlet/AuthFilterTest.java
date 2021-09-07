package servlet;

import dream.filter.AuthFilter;
import dream.model.User;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class AuthFilterTest {

    @Test
    public void doFilterWhenUserNotNull() throws IOException, ServletException {
        AuthFilter filter = new AuthFilter();
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpServletRequest req = mock(HttpServletRequest.class);
        FilterChain chain = mock(FilterChain.class);
        HttpSession session = mock(HttpSession.class);

        when(req.getRequestURI()).thenReturn("");
        when(session.getAttribute("user")).thenReturn(new User());
        when(req.getSession()).thenReturn(session);

        filter.doFilter(req, resp, chain);

        verify(resp, times(0)).sendRedirect(anyString());
    }

    @Test
    public void doFilterWhenUserNull() throws IOException, ServletException {
        AuthFilter filter = new AuthFilter();
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpServletRequest req = mock(HttpServletRequest.class);
        FilterChain chain = mock(FilterChain.class);
        HttpSession session = mock(HttpSession.class);

        when(req.getRequestURI()).thenReturn("");
        when(session.getAttribute("user")).thenReturn(null);
        when(req.getSession()).thenReturn(session);

        filter.doFilter(req, resp, chain);

        verify(resp, times(1)).sendRedirect(anyString());
    }
}
