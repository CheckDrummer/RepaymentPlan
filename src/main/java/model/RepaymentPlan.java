package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RepaymentPlan implements Serializable {

    private LocalDateTime date;
    private BigDecimal annuity;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal initialOutstandingPrincipal;
    private BigDecimal remainingOutstandingPrincipal;

    public RepaymentPlan() {

    }

    public RepaymentPlan(LocalDateTime date, BigDecimal annuity, BigDecimal principal, BigDecimal interest, BigDecimal initialOutstandingPrincipal, BigDecimal remainingOutstandingPrincipal) {
        this.date = date;
        this.annuity = annuity;
        this.principal = principal;
        this.interest = interest;
        this.initialOutstandingPrincipal = initialOutstandingPrincipal;
        this.remainingOutstandingPrincipal = remainingOutstandingPrincipal;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getAnnuity() {
        return annuity;
    }

    public void setAnnuity(BigDecimal annuity) {
        this.annuity = annuity;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getInitialOutstandingPrincipal() {
        return initialOutstandingPrincipal;
    }

    public void setInitialOutstandingPrincipal(BigDecimal initialOutstandingPrincipal) {
        this.initialOutstandingPrincipal = initialOutstandingPrincipal;
    }

    public BigDecimal getRemainingOutstandingPrincipal() {
        return remainingOutstandingPrincipal;
    }

    public void setRemainingOutstandingPrincipal(BigDecimal remainingOutstandingPrincipal) {
        this.remainingOutstandingPrincipal = remainingOutstandingPrincipal;
    }

    @Override
    public String toString() {
        return "RepaymentPlan{" +
                "date=" + date +
                ", annuity=" + annuity +
                ", principal=" + principal +
                ", interest=" + interest +
                ", initialOutstandingPrincipal=" + initialOutstandingPrincipal +
                ", remainingOutstandingPrincipal=" + remainingOutstandingPrincipal +
                '}';
    }
}
