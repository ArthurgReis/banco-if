package br.com.bancofeira.banco_feira.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bancofeira.banco_feira.service.AdminService;

@RestController
@RequestMapping("/api/admin") 
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

}