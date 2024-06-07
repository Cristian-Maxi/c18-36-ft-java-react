package com.healthtech.demo.services;

import com.healthtech.demo.dto.ActualizarEmocionDTO;
import com.healthtech.demo.dto.CrearEmocionDTO;
import com.healthtech.demo.entities.Emocion;
import com.healthtech.demo.manejoErrores.ValidacionDeIntegridad;
import com.healthtech.demo.repositories.EmocionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmocionService implements IEmocionService {

    @Autowired
    private EmocionRepository emocionRepository;

    @Override
    public List<Emocion> getEmociones() {
        List<Emocion> listaEmociones = emocionRepository.findAll();
        return listaEmociones;
    }

    @Override
    public Emocion saveEmocion(CrearEmocionDTO emocionDTO) {
        Emocion emocionGuardada = emocionRepository.save(new Emocion(emocionDTO));
        return emocionGuardada;
    }

    @Override
    public void deleteEmocion(Long id) {
        emocionRepository.deleteById(id);
    }

    @Override
    public Emocion modificarEmocion(ActualizarEmocionDTO emocionDTO) {
        if (!emocionRepository.findById(emocionDTO.id()).isPresent()) {
            throw new ValidacionDeIntegridad("El id de emocion no fue encontrado");
        }
        Emocion emocionElegida = emocionRepository.getReferenceById(emocionDTO.id());
        emocionElegida.ActualizarEmocion(emocionDTO);
        return emocionElegida;
    }

    @Override
    public Emocion elegirEmocion(Long id) {
        if (!emocionRepository.findById(id).isPresent()) {
            throw new ValidacionDeIntegridad("El id de la emocion no fue encontrado");
        }
        Emocion emocion = emocionRepository.getReferenceById(id);
        return emocion;
    }
}