    package com.br.fiattransfer.controller;

    import com.br.fiattransfer.models.request.RequestTransferSchedule;
    import com.br.fiattransfer.models.response.GenericResponse;
    import com.br.fiattransfer.service.SchedulerService;
    import io.swagger.v3.oas.annotations.tags.Tag;
    import lombok.AllArgsConstructor;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import javax.validation.Valid;


    @RestController
    @RequestMapping(value = "/v1/api")
    @CrossOrigin(value = "*")
    @Tag(name = "Scheduler", description = "Controller to manage scheduled transfers")
    @AllArgsConstructor
    public class ScheduleController {

        private final SchedulerService schedulerService;

        @RequestMapping(method = RequestMethod.POST, value = "/schedules/save", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<GenericResponse<?>> saveSchedule(@Valid @RequestBody RequestTransferSchedule transfer){
            return schedulerService.saveSchedule(transfer);
        }

        @RequestMapping(method = RequestMethod.GET, value = "/schedules", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<GenericResponse<?>> getSchedules(){
            return schedulerService.getSchedules();
        }

        @RequestMapping(method = RequestMethod.PUT, value = "/schedules/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<GenericResponse<?>> cancelSchedule(@RequestParam("transfer") String transferId){
            return schedulerService.cancelSchedule(transferId);
        }


    }
