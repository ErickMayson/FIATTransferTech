package com.br.fiattransfer.models.scheduler;

import com.br.fiattransfer.models.request.RequestTransferSchedule;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "T_FIN_TRANSFER")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Transfer {

    @Id
    @Column(name = "FIN_TRANSFER_ID", updatable = false, nullable = false)
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "FIN_TRANSFER_ACCOUNTORIGIN", updatable = false, nullable = false)
    private String accountOrigin;

    @Column(name = "FIN_TRANSFER_ACCOUNTDESTINATION", updatable = false, nullable = false)
    private String accountDestination;

    @Column(name = "FIN_TRANSFER_VALUE", updatable = false, nullable = false, precision = 15, scale = 2)
    private BigDecimal value;

    @Column(name = "FIN_TRANSFER_FEE", updatable = false, nullable = false, precision = 15, scale = 2)
    private BigDecimal feeValue;

    @Column(name = "FIN_TRANSFER_TOTALVALUE", updatable = false, nullable = false, precision = 15, scale = 2)
    private BigDecimal totalValue;

    @Column(name = "FIN_TRANSFER_TRANSFERDATE", updatable = false, nullable = false)
    private Instant transferDate;

    @Column(name = "FIN_TRANSFER_DTCREATED", updatable = false, nullable = false)
    private Instant createdAt;

    @Column(name = "FIN_TRANSFER_DTCANCELED")
    private Instant canceledAt;

    @PrePersist
    public void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public Transfer(RequestTransferSchedule transfer, BigDecimal feeValue, BigDecimal totalValue) {
        this.accountOrigin = transfer.getAccountOrigin();
        this.accountDestination = transfer.getAccountDestination();
        this.value = transfer.getValue();
        this.feeValue = feeValue;
        this.totalValue = totalValue;
        this.transferDate = transfer.getTransferDate();
        this.createdAt = Instant.now();
    }

    public Transfer(String accountOrigin, String accountDestination, BigDecimal value,
                      BigDecimal feeValue, BigDecimal totalValue, Instant transferDate) {
        this.accountOrigin = accountOrigin;
        this.accountDestination = accountDestination;
        this.value = value;
        this.feeValue = feeValue;
        this.totalValue = totalValue;
        this.transferDate = transferDate;
        this.createdAt = Instant.now();
    }

}