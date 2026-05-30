    package com.br.fiattransfer.controller;

    import com.br.fiattransfer.models.response.GenericResponse;
    import com.br.fiattransfer.service.SchedulerService;
    import io.swagger.v3.oas.annotations.tags.Tag;
    import lombok.AllArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;


    @RestController
    @RequestMapping(value = "/v1/api")
    @CrossOrigin(value = "*")
    @Tag(name = "Scheduler", description = "Controller to manage scheduled transfers")
    @Slf4j
    @AllArgsConstructor
    public class ScheduleController {

        private final SchedulerService schedulerService;

        @RequestMapping(method = RequestMethod.GET, value = "/schedules", produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        public ResponseEntity<GenericResponse<?>> getSchedules(){

            return schedulerService.getSchedules();
        }


    }
