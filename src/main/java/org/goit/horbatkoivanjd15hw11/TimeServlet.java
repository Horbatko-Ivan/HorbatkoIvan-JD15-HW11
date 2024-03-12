package org.goit.horbatkoivanjd15hw11;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@SuppressWarnings("checkstyle:MissingJavadocType")
@WebServlet(name = "TimeServlet", value = "/time")
public class TimeServlet extends HttpServlet {

  private TemplateEngine templateEngine;

  @Override
  public void init() {
    ServletContext servletContext = getServletContext();
    ServletContextTemplateResolver templateResolver =
        new ServletContextTemplateResolver(servletContext);
    templateResolver.setTemplateMode("HTML");
    templateResolver.setPrefix("/WEB-INF/templates/");
    templateResolver.setSuffix(".html");
    templateResolver.setCacheable(false);

    templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(templateResolver);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("text/html");
    String timezone = req.getParameter("timezone");
    LocalDateTime currentTime;
    String formattedTime;

    if (timezone != null && !timezone.isEmpty()) {
      if (timezone.contains(" ")) {
        timezone = timezone.replace(" ", "+");
      }
      ZoneId zone = ZoneId.of(timezone);
      currentTime = LocalDateTime.now(zone);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      formattedTime = currentTime.format(formatter);

      CookieUtil.createCookie(resp, "lastTimezone", timezone, 30 * 24 * 60 * 60);
    } else {
      timezone = CookieUtil.getCookieValue(req, "lastTimezone", "UTC");
      ZoneId zone = ZoneId.of(timezone);
      currentTime = LocalDateTime.now(zone);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      formattedTime = currentTime.format(formatter);
    }

    WebContext ctx = new WebContext(req, resp, req.getServletContext());
    ctx.setVariable("formattedTime", formattedTime);
    ctx.setVariable("timezone", timezone);

    templateEngine.process("time-template", ctx, resp.getWriter());
  }
}
