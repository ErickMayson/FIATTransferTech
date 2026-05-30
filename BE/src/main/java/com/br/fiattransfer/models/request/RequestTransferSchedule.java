package com.br.fiattransfer.models.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class RequestTransferSchedule {

    @NotNull(message = "É necessário definir a conta de origem para a transferência.")
    @Pattern(
            regexp = "^[a-zA-Z0-9]{10}$",
            message = "O número da conta de origem deve conter exatamente 10 caracteres alfanuméricos."
    )
    private String accountOrigin;

    @NotNull(message = "É necessário definir a conta de destino para a transferência.")
    @Pattern(
            regexp = "^[a-zA-Z0-9]{10}$",
            message = "O número da conta de destino deve conter exatamente 10 caracteres alfanuméricos."
    )
    private String accountDestination;

    @NotNull(message = "É necessário definir o valor da transferência.")
    @Positive(message = "O valor da transferência deve ser maior que zero.")
    private BigDecimal value;

    @NotNull(message = "É necessário definir a data de transferência.")
    private Instant transferDate;

    @JsonCreator
    public RequestTransferSchedule(
            @JsonProperty("accountOrigin") String accountOrigin,
            @JsonProperty("accountDestination") String accountDestination,
            @JsonProperty("value") BigDecimal value,
            @JsonProperty("transferDate") Instant transferDate) {
        this.accountOrigin = accountOrigin;
        this.accountDestination = accountDestination;
        this.value = value;
        this.transferDate = transferDate;
    }
}