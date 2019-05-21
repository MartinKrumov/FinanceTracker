package com.tracker.proxy;

import com.tracker.dto.UserLoginDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "finance-tracker-service")
public interface FinanceTrackerServiceProxy {

    @GetMapping("/api/users/{username}")
    ResponseEntity<UserLoginDTO> getUserByUsername(@PathVariable("username") String username);
}
