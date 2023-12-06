package com.example.awarehouse.module.worker;

import com.example.awarehouse.exception.exceptions.UserNotFound;
import com.example.awarehouse.exception.exceptions.UserUnauthorized;
import com.example.awarehouse.module.warehouse.Role;
import com.example.awarehouse.util.UserIdSupplier;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WorkerService {
    private final WorkerRepository workerRepository;
    private final UserIdSupplier workerIdSupplier;
    public void register(){
        Jwt token= (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Worker worker = createWorker(token);
        workerRepository.save(worker);
    }
    private Worker createWorker(Jwt token){
        String firstName = token.getClaim("given_name");
        String lastName =  token.getClaim("family_name");
        UUID id = UUID.fromString(token.getClaim("sub"));
        return new Worker( id, firstName, lastName);
    }

    public void validateIfWorkerIsAdmin(UUID workerId){
        getWorkerWithConcreteRole(workerId, Role.ADMIN).orElseThrow(() -> new UserUnauthorized("User is not admin"));
    }

    public Worker getWorker(){
        return workerRepository.findById(workerIdSupplier.getUserId()).orElseThrow(() -> new UserNotFound("User is not admin"));
    }
    private Optional<Worker> getWorkerWithConcreteRole(UUID workerID, Role role){
        return  workerRepository.findWorkerWithConcreteIdAndRole(workerID, role.toString());
    }
}
