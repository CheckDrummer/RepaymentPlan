import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exceptions.ValidationException;
import model.LoanDetails;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import service.RepaymentPlanService;
import service.RepaymentPlanServiceImpl;

public class WebApp {

    public static void main(String[] args) throws LifecycleException {

        RepaymentPlanService repaymentPlanService = new RepaymentPlanServiceImpl();

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("temp");
        tomcat.setPort(8080);

        String contextPath = "/";
        String docBase = new File(".").getAbsolutePath();

        Context context = tomcat.addContext(contextPath, docBase);

        HttpServlet servlet = new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

                PrintWriter out = resp.getWriter();

                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                try {
                    LoanDetails loanDetails = mapper.readValue(req.getReader(), LoanDetails.class);
                    validateLoanDetails(loanDetails);
                    resp.setContentType("application/json");
                    out.print(mapper.writeValueAsString(repaymentPlanService.calculatePlan(loanDetails)));
                } catch (JsonGenerationException | JsonMappingException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    e.printStackTrace();
                } catch (ValidationException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print(e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    e.printStackTrace();
                }
                out.flush();
                out.close();
            }
        };

        String servletName = "RepaymentPlan";
        String urlPattern = "/generate-plan";

        tomcat.addServlet(contextPath, servletName, servlet);
        context.addServletMappingDecoded(urlPattern, servletName);

        tomcat.start();
        tomcat.getServer().await();
    }

    private static void validateLoanDetails (LoanDetails loanDetails) {
        if (loanDetails.getNominalRate().compareTo(BigDecimal.ZERO) < 0
                || loanDetails.getNominalRate() == null
                || loanDetails.getLoanAmount().compareTo(BigDecimal.ZERO) < 0
                || loanDetails.getLoanAmount() == null
                || loanDetails.getDuration() < 0
                || loanDetails.getDuration() == 0
                || loanDetails.getStartDate() == null
                || loanDetails.getStartDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Incorrect input data");
        }
    }
}