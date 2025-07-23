package com.example.social_media.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "reqresClient", url = "https://reqres.in/api", configuration = ReqresClientConfig.class)
public interface ReqresClient {
    @GetMapping("/users")
    ReqresResponse getUsers(@RequestParam(value = "page", required = false) Integer page);
}
