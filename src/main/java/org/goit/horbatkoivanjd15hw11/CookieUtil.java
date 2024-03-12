package org.goit.horbatkoivanjd15hw11;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

  public static String getCookieValue(HttpServletRequest request, String name, String defaultValue) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(name)) {
          return cookie.getValue();
        }
      }
    }
    return defaultValue;
  }

  public static void createCookie(HttpServletResponse response, String name, String value, int maxAge) {
    Cookie cookie = new Cookie(name, value);
    cookie.setMaxAge(maxAge);
    response.addCookie(cookie);
  }
}
