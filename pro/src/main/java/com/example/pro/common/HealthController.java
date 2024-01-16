package com.example.pro.common;

import com.example.pro.common.response.BasicResponse;
import com.example.pro.common.response.ResponseUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "Health Check";
    }

    @GetMapping("/api/test")
    public BasicResponse<String> apiTest() {
        return ResponseUtil.success("API Health Check");
    }
}
