import exceptions.ValidationException;
import model.LoanDetails;
import model.RepaymentPlan;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.RepaymentPlanService;
import service.RepaymentPlanServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


class RepaymentPlanServiceImplTest {

    RepaymentPlanService repaymentPlanService = new RepaymentPlanServiceImpl();

    @Test
    public void testCorrectRequest() {
        LoanDetails loanDetails = new LoanDetails();
        loanDetails.setLoanAmount(BigDecimal.valueOf(5000));
        loanDetails.setNominalRate(BigDecimal.valueOf(5.0));
        loanDetails.setDuration(24);
        loanDetails.setStartDate(LocalDateTime.now().plusMonths(1));

        List<RepaymentPlan> repaymentPlans = repaymentPlanService.calculatePlan(loanDetails);
        Assertions.assertEquals(BigDecimal.valueOf(219.36), repaymentPlans.get(0).getAnnuity());
        Assertions.assertEquals(BigDecimal.valueOf(198.53), repaymentPlans.get(0).getPrincipal());
        Assertions.assertEquals(BigDecimal.valueOf(20.83), repaymentPlans.get(0).getInterest());
        Assertions.assertEquals(BigDecimal.valueOf(219.28), repaymentPlans.get(23).getAnnuity());
    }

    @Test
    public void testRequestWithIncorrectLocalDateTime() {
        LoanDetails loanDetails = new LoanDetails();
        loanDetails.setLoanAmount(BigDecimal.valueOf(5000));
        loanDetails.setNominalRate(BigDecimal.valueOf(5.0));
        loanDetails.setDuration(24);
        loanDetails.setStartDate(LocalDateTime.of(2018, 05, 11, 10, 10, 10));

        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> repaymentPlanService.calculatePlan(loanDetails));
        Assertions.assertTrue(thrown.getMessage().contains("Incorrect input data"));
    }

    @Test
    public void testIncorrectRequestWithNullData() {
        LoanDetails loanDetails = new LoanDetails();
        loanDetails.setDuration(24);
        loanDetails.setStartDate(LocalDateTime.now());

        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> repaymentPlanService.calculatePlan(loanDetails));
        Assertions.assertTrue(thrown.getMessage().contains("Incorrect input data"));
    }
}