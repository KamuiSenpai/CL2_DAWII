package com.cibertec.assessment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.assessment.beans.SquareBean;
import com.cibertec.assessment.model.Square;
import com.cibertec.assessment.service.SquareService;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/squares") // Especifica la ruta base para las solicitudes manejadas por este controlador
public class SquareController {

    @Autowired // Anotación de Spring para la inyección de dependencias
    SquareService squareService; // Inyecta una instancia de SquareService en este controlador

    @GetMapping // Maneja las solicitudes HTTP GET
    public ResponseEntity<List<SquareBean>> list() {
        // Retorna una lista de SquareBean encapsulada en un ResponseEntity
        // El método list() de SquareService proporciona la lista de cuadrados
        return new ResponseEntity<>(squareService.list(), HttpStatus.OK); // Retorna una respuesta HTTP 200 OK
    }

    @PostMapping // Maneja las solicitudes HTTP POST
    public ResponseEntity<Square> create(@RequestBody Square s) {
        // Crea un cuadrado recibiendo un objeto Square en el cuerpo de la solicitud HTTP
        // El método create() de SquareService crea un nuevo cuadrado
        return new ResponseEntity<>(squareService.create(s), HttpStatus.CREATED); // Retorna una respuesta HTTP 201 CREATED
    }
}

