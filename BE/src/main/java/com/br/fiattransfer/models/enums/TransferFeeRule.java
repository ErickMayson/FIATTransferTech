package com.br.fiattransfer.models.enums;

import java.math.BigDecimal;

public enum TransferFeeRule {
    SAME_DAY(0, 0, new BigDecimal("3.00"), new BigDecimal("0.025")),
    Upto10Days(1, 10, new BigDecimal("12.00"), BigDecimal.ZERO),
    UpTo20Days(11, 20, BigDecimal.ZERO, new BigDecimal("0.082")),
    UpTo30Days(21, 30, BigDecimal.ZERO, new BigDecimal("0.069")),
    UpTo40Daus(31, 40, BigDecimal.ZERO, new BigDecimal("0.047")),
    UpTo50Days(41, 50, BigDecimal.ZERO, new BigDecimal("0.017"));

    private final int minDays;
    private final int maxDays;
    private final BigDecimal fixedFee;
    private final BigDecimal percentageRate;

    TransferFeeRule(int minDays, int maxDays, BigDecimal fixedFee, BigDecimal percentageRate) {
        this.minDays = minDays;
        this.maxDays = maxDays;
        this.fixedFee = fixedFee;
        this.percentageRate = percentageRate;
    }

    public boolean matches(long daysBetween) {
        return daysBetween >= minDays && daysBetween <= maxDays;
    }

    public BigDecimal calculateFee(BigDecimal transferAmount) {
        return fixedFee.add(transferAmount.multiply(percentageRate));
    }
}