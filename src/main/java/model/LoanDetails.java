package model;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


public class LoanDetails implements Serializable {

    private BigDecimal loanAmount;
    private BigDecimal nominalRate;
    private int duration;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime startDate;

    public LoanDetails() {
    }

    public LoanDetails(BigDecimal loanAmount, BigDecimal nominalRate, int duration, LocalDateTime startDate) {
        this.loanAmount = loanAmount;
        this.nominalRate = nominalRate;
        this.duration = duration;
        this.startDate = startDate;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getNominalRate() {
        return nominalRate;
    }

    public void setNominalRate(BigDecimal nominalRate) {
        this.nominalRate = nominalRate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }


    @Override
    public String toString() {
        return "LoanDetails{" +
                "loanAmount=" + loanAmount +
                ", nominalRate=" + nominalRate +
                ", duration=" + duration +
                ", startDate=" + startDate +
                '}';
    }
}
