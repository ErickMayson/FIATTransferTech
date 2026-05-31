package com.br.fiattransfer.service;

import com.br.fiattransfer.exception.InvalidFeeException;
import com.br.fiattransfer.models.enums.TransferFeeRule;
import com.br.fiattransfer.models.request.RequestTransferSchedule;
import com.br.fiattransfer.models.response.GenericResponse;
import com.br.fiattransfer.models.response.TransferResponse;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class SchedulerService {

    private final TransferRepository transferRepository;

    public ResponseEntity<GenericResponse<?>> getSchedules() {
        try {
            List<TransferResponse> transferResponses = transferRepository.findAll()
                    .stream()
                    .map(transfer -> new TransferResponse(
                            transfer.getId(),
                            transfer.getAccountOrigin(),
                            transfer.getAccountDestination(),
                            transfer.getValue(),
                            transfer.getFeeValue(),
                            transfer.getTotalValue(),
                            transfer.getTransferDate(),
                            transfer.getCreatedAt(),
                            transfer.getCanceledAt()
                    ))
                    .collect(Collectors.toList());

            GenericResponse<List<TransferResponse>> response = new GenericResponse<>(
                    "200",
                    "Transferências recuperadas com sucesso.",
                    transferResponses
            );

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            GenericResponse<String> errorResponse = new GenericResponse<>(
                    "500",
                    "Não foi possível recuperar as transferências agendadas no momento, tente novamente mais tarde.",
                    null
            );
            log.error("{}: {}", errorResponse.getMessage(), e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<GenericResponse<?>> cancelSchedule(String uuid) {
        try {
            Transfer transfer = transferRepository.findById(UUID.fromString(uuid)).orElseThrow(() -> new InvalidFeeException("Transferência não encontrada para o ID fornecido."));
            if (transfer.getCanceledAt() != null) {
                throw new InvalidFeeException("Essa transferência já foi cancelada.");
            }
            transfer.setCanceledAt(Instant.now());
            transferRepository.save(transfer);
            GenericResponse<String> response = new GenericResponse<>("200",
                    "Transferência cancelada com sucesso.",
                    null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidFeeException e) {
            GenericResponse<String> errorResponse = new GenericResponse<>("400", e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            GenericResponse<String> errorResponse = new GenericResponse<>("500",
                    "Não foi possível cancelar essa transferência no momento, tente novamente mais tarde.",
                    null);
            log.error("{}: {}", errorResponse.getMessage(), e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

            GenericResponse<String> errorResponse = new GenericResponse<>("201",
                    "Transferencia agendada com sucesso.",
                    null);
            return new ResponseEntity<>(errorResponse, HttpStatus.CREATED);

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
