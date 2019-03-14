package service;

import exceptions.ValidationException;
import model.LoanDetails;
import model.RepaymentPlan;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RepaymentPlanServiceImpl implements RepaymentPlanService {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    private static final BigDecimal ONE = new BigDecimal(1);
    private static final BigDecimal TWELVE = new BigDecimal(12);
    private static final BigDecimal DAYS_IN_MONTH = new BigDecimal(30);
    private static final BigDecimal DAYS_IN_YEAR = new BigDecimal(360);

    @Override
    public List<RepaymentPlan> calculatePlan(LoanDetails loanDetails) {

        validateLoanDetails(loanDetails);

        List<RepaymentPlan> repaymentPlanList = new ArrayList<>();
        int duration = loanDetails.getDuration();

        BigDecimal annuity = calculateAnnuity(loanDetails);

        for (int i = 0; i < duration; i++) {
            RepaymentPlan repaymentPlan = new RepaymentPlan();

            BigDecimal initialOutstandingPrincipal = loanDetails.getLoanAmount();
            BigDecimal interest = calculateInterest(loanDetails);
            BigDecimal principal;

            if (annuity.compareTo(initialOutstandingPrincipal) > 0) {
                principal = initialOutstandingPrincipal;
                annuity = principal.add(interest);
            } else {
                principal = annuity.subtract(interest);
            }

            BigDecimal remainingOutstandingPrincipal = initialOutstandingPrincipal.subtract(principal);
            LocalDateTime startDate = loanDetails.getStartDate();

            repaymentPlan.setAnnuity(annuity);
            repaymentPlan.setInterest(interest);
            repaymentPlan.setPrincipal(principal);
            repaymentPlan.setInitialOutstandingPrincipal(initialOutstandingPrincipal);
            repaymentPlan.setRemainingOutstandingPrincipal(remainingOutstandingPrincipal);
            repaymentPlan.setDate(startDate);

            repaymentPlanList.add(repaymentPlan);

            loanDetails.setDuration(loanDetails.getDuration()-1);
            loanDetails.setStartDate(loanDetails.getStartDate().plusMonths(1));
            loanDetails.setLoanAmount(remainingOutstandingPrincipal);
        }

        return repaymentPlanList;
    }


    static private BigDecimal calculateAnnuity (LoanDetails loanDetails) {

        MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
        BigDecimal nominalRatePercentage = loanDetails.getNominalRate().divide(ONE_HUNDRED).divide(TWELVE,mc);
        BigDecimal loanAmount = loanDetails.getLoanAmount();
        int durationInMonth = loanDetails.getDuration();

        /*
        Annuity formula:

        (nominalRatePercentage * loanAmount)
        /
        (1 - 1 / ((1 + nominalRatePercentage)^durationInMonth))

         */
        BigDecimal fractionTop = loanAmount.multiply(nominalRatePercentage);
        BigDecimal fractionLowerPart = ONE.subtract(ONE.divide(nominalRatePercentage.add(ONE).pow(durationInMonth,mc),mc));
        return fractionTop.divide(fractionLowerPart,mc).setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    static private BigDecimal calculateInterest (LoanDetails loanDetails) {

        MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
        BigDecimal nominalRatePercentage = loanDetails.getNominalRate().divide(ONE_HUNDRED);
        BigDecimal loanAmount = loanDetails.getLoanAmount();

        return nominalRatePercentage.multiply(DAYS_IN_MONTH,mc).multiply(loanAmount,mc).divide(DAYS_IN_YEAR,mc)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private static void validateLoanDetails (LoanDetails loanDetails) {
        if (loanDetails.getNominalRate() == null
                || loanDetails.getNominalRate().compareTo(BigDecimal.ZERO) < 0
                || loanDetails.getLoanAmount() == null
                || loanDetails.getLoanAmount().compareTo(BigDecimal.ZERO) < 0
                || loanDetails.getDuration() == 0
                || loanDetails.getDuration() < 0
                || loanDetails.getStartDate() == null
                || loanDetails.getStartDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Incorrect input data");
        }
    }

}
