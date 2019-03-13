package service;

import model.LoanDetails;
import model.RepaymentPlan;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RepaymentPlanServiceImpl implements RepaymentPlanService {

    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    public static final BigDecimal ONE = new BigDecimal(1);
    public static final BigDecimal DAYS_IN_MONTH = new BigDecimal(30);
    public static final BigDecimal DAYS_IN_YEAR = new BigDecimal(360);

    @Override
    public List<RepaymentPlan> calculatePlan(LoanDetails loanDetails) {
        List<RepaymentPlan> repaymentPlanList = new ArrayList<>();

        int duration = loanDetails.getDuration();
        LocalDateTime startDate = loanDetails.getStartDate();

        for (int i = 0; i < duration; i++) {
            RepaymentPlan repaymentPlan = new RepaymentPlan();

            BigDecimal annuity = calculateAnnuity(loanDetails);
            BigDecimal interest = calculateInterest(loanDetails);
            BigDecimal principal = annuity.subtract(interest);
            BigDecimal initialOutstandingPrincipal = loanDetails.getLoanAmount();
            BigDecimal remainingOutstandingPrincipal = initialOutstandingPrincipal.subtract(principal);

            repaymentPlan.setannuity(annuity);
            repaymentPlan.setInterest(interest);
            repaymentPlan.setPrincipal(principal);
            repaymentPlan.setInitialOutstandingPrincipal(initialOutstandingPrincipal);
            repaymentPlan.setRemainingOutstandingPrincipal(remainingOutstandingPrincipal);
            repaymentPlan.setDate(startDate);

            repaymentPlanList.add(repaymentPlan);

            loanDetails.setDuration(loanDetails.getDuration()-1);
            loanDetails.setStartDate(loanDetails.getStartDate().minusMonths(1));
            loanDetails.setLoanAmount(remainingOutstandingPrincipal);

        }

        return repaymentPlanList;
    }


    static private BigDecimal calculateAnnuity (LoanDetails loanDetails) {

        MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
        BigDecimal nominalRatePercentage = loanDetails.getNominalRate().divide(ONE_HUNDRED);
        BigDecimal loanAmount = loanDetails.getLoanAmount();
        int durationInMonth = loanDetails.getDuration();

        BigDecimal upperAmount = loanAmount.multiply(nominalRatePercentage);
        BigDecimal lowerAmount = ONE.subtract(ONE.divide(nominalRatePercentage.add(ONE).pow(durationInMonth,mc),mc));
        return upperAmount.divide(lowerAmount,mc).setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    static private BigDecimal calculateInterest (LoanDetails loanDetails) {

        MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
        BigDecimal nominalRatePercentage = loanDetails.getNominalRate().divide(ONE_HUNDRED);
        BigDecimal loanAmount = loanDetails.getLoanAmount();

        return nominalRatePercentage.multiply(DAYS_IN_MONTH,mc).multiply(loanAmount,mc).divide(DAYS_IN_YEAR,mc)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}
