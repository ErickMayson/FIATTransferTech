package com.br.fiattransfer.service;

import com.br.fiattransfer.exception.InvalidFeeException;
import com.br.fiattransfer.models.enums.TransferFeeRule;
import com.br.fiattransfer.models.request.RequestTransferSchedule;
import com.br.fiattransfer.models.response.GenericResponse;
import com.br.fiattransfer.models.scheduler.Transfer;
import com.br.fiattransfer.repositories.TransferRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@AllArgsConstructor
public class SchedulerService {

    private final TransferRepository transferRepository;

    public ResponseEntity<GenericResponse<?>> getSchedules() {
        GenericResponse<String> errorResponse = new GenericResponse<>("200",
                "Request aceito com sucesso.",
                null);
        return new ResponseEntity<>(errorResponse, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<GenericResponse<?>> saveSchedule(RequestTransferSchedule transfer) {
        try {
            Instant transferDate = transfer.getTransferDate();
            Instant now = Instant.now();
            TransferFeeRule matchedRule = null;


            if (transferDate.isBefore(now)) {
                throw new InvalidFeeException("A data de transfêrencia não pode ser menor que a data atual.");
            }

            if (transferDate.isAfter(now.plus(50, ChronoUnit.DAYS))) {
                throw new InvalidFeeException("A data de transfêrencia deve ser no máximo 50 dias a partir da data atual.");
            }

            long interval = ChronoUnit.DAYS.between(now, transferDate);

            for (TransferFeeRule rule : TransferFeeRule.values()) {
                if (rule.matches(interval)) {
                    matchedRule = rule;
                    break;
                }
            }

            if (matchedRule == null) {
                throw new InvalidFeeException("Não foi possível calcular a taxa para o intervalo de dias informado.");
            }

            BigDecimal appliedFee = matchedRule.calculateFee(transfer.getValue()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal totalValue = transfer.getValue().add(appliedFee);

            transferRepository.save(new Transfer(transfer, appliedFee, totalValue));

            GenericResponse<String> errorResponse = new GenericResponse<>("200",
                    "Transferencia agendada com sucesso.",
                    null);
            return new ResponseEntity<>(errorResponse, HttpStatus.ACCEPTED);

        } catch (InvalidFeeException e) {
            GenericResponse<String> errorResponse = new GenericResponse<>("400", e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            GenericResponse<String> errorResponse = new GenericResponse<>("500",
                    "Não foi possível processar essa transferência no momento, tente novamente mais tarde.",
                    null);
            log.error("{}: {}", errorResponse.getMessage(), e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
