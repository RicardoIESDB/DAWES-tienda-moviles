package com.dwes.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerController {
    //Redireccinamos la raiz de la aplicacion a la interfaz de Swagger
    @GetMapping("/")
    public String redirectToSwagger(){
        return "redirect:/swagger-ui.html";
    }
}
