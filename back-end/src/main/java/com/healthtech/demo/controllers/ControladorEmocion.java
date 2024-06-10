package com.healthtech.demo.controllers;

import com.healthtech.demo.dto.ActualizarEmocionDTO;
import com.healthtech.demo.dto.CrearEmocionDTO;
import com.healthtech.demo.dto.ListarEmocionDTO;
import com.healthtech.demo.entities.Emocion;
import com.healthtech.demo.services.EmocionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/emocion")
@SecurityRequirement(name = "bearer-key") //Necesario para Swagger UI con Bearer Tokens
public class ControladorEmocion {
    @Autowired
    private EmocionService emocionService;

    /*  PARECE NO FUNCIONAR EL MODELMAPPER CON RECORDS
    @GetMapping("/listaremociones")
    public ResponseEntity<List<ListarEmocionDTO>> listarEmociones() {
        List<Emocion> emocionList = emocionService.getEmociones();
        ModelMapper modelMapper = new ModelMapper();

        List<ListarEmocionDTO> emocionDTOS = emocionList.stream()
                .map(emocion -> modelMapper.map(emocion, ListarEmocionDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(emocionDTOS);
    }*/

    @GetMapping("/listaremociones")
    public ResponseEntity<List<ListarEmocionDTO>> listarEmociones() {
        List<Emocion> emocionList = emocionService.getEmociones();
        List<ListarEmocionDTO> emocionDTO = emocionList.stream()
                .map(emocion -> new ListarEmocionDTO(
                        emocion.getId(),
                        emocion.getFechaCreacion(),
                        emocion.getDescripcion(),
                        emocion.getAccion(),
                        emocion.getTipoEmocion())).collect(Collectors.toList());
        return ResponseEntity.ok(emocionDTO);
    }

    @Transactional
    @PostMapping({"/crear"})
    public ResponseEntity<CrearEmocionDTO> crearEmocion(@RequestBody @Valid CrearEmocionDTO emocion) {
        emocionService.saveEmocion(emocion);
        return ResponseEntity.status(HttpStatus.CREATED).body(emocion);
    }

    @GetMapping("/seleccionar/{id}")
    public ResponseEntity<ListarEmocionDTO> seleccionarEmocion(@PathVariable Long id) {
        Emocion emocionSeleccionada = emocionService.elegirEmocion(id);
        ListarEmocionDTO emocionDTO = new ListarEmocionDTO(
                emocionSeleccionada.getId(),
                emocionSeleccionada.getFechaCreacion(),
                emocionSeleccionada.getDescripcion(),
                emocionSeleccionada.getAccion(),
                emocionSeleccionada.getTipoEmocion());
        return ResponseEntity.ok(emocionDTO);
    }

    @Transactional
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity eliminarEmocion(@PathVariable Long id) {
        emocionService.deleteEmocion(id);
        return ResponseEntity.ok("Eliminado exitosamente");
    }

    @Transactional
    @PutMapping("/modificar")
    public ResponseEntity<ActualizarEmocionDTO> actualizarEmocion(@RequestBody @Valid ActualizarEmocionDTO emocion) {
        Emocion emocionModificada = emocionService.modificarEmocion(emocion);
        ActualizarEmocionDTO emocionDTO = new ActualizarEmocionDTO(
                emocionModificada.getId(),
                emocionModificada.getTipoEmocion(),
                emocionModificada.getDescripcion(),
                emocionModificada.getAccion());
        return ResponseEntity.ok(emocionDTO);
    }
}