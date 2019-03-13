package service;

import model.LoanDetails;
import model.RepaymentPlan;

import java.math.BigDecimal;
import java.util.List;

public interface RepaymentPlanService {
    List<RepaymentPlan> calculatePlan(LoanDetails loanDetails);
    }
