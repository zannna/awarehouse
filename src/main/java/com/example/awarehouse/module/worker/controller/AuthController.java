package com.example.awarehouse.module.worker.controller;

import com.example.awarehouse.module.worker.WorkerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.example.awarehouse.util.Constants.*;

@Controller
@RequestMapping(URI_VERSION_V1+URI_AUTH)
public class AuthController {
    WorkerService workerService;

    public AuthController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping
    void register() {
        workerService.register();
    }
}
