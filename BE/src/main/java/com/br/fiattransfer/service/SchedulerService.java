package com.br.fiattransfer.service;

import com.br.fiattransfer.models.response.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {

    public ResponseEntity<GenericResponse<?>> getSchedules() {
        GenericResponse<String> errorResponse = new GenericResponse<>("200", "Request aceito com sucesso.", null);
        return new ResponseEntity<>(errorResponse, HttpStatus.ACCEPTED);
    }

}
