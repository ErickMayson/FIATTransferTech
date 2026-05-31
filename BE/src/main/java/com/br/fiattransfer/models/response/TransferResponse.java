package com.br.fiattransfer.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse {

    private UUID id;
    private String accountOrigin;
    private String accountDestination;
    private BigDecimal value;
    private BigDecimal feeValue;
    private BigDecimal totalValue;
    private Instant transferDate;
    private Instant createdAt;
    private Instant canceledAt;

}
