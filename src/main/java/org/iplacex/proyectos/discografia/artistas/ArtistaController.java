package org.iplacex.proyectos.discografia.artistas;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api")

public class ArtistaController {

    @Autowired
    private IArtistaRepository artistaRepository;

    // Controlador POST/ Metodo para crear nuevo artita. inserción de registro, retornando dicho objeto)
    @PostMapping(
        value = "/artista",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleInsertArtistaRequest(@RequestBody Artista artista){
        try {
            Artista savedArtista = artistaRepository.save(artista);
            return new ResponseEntity<>(savedArtista, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear artista: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Controlador GET/ Metodo para permitir obtener todos los registros de la colección “artistas”
    @GetMapping(
        value = "/artistas",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Artista>> HandleGetAristasRequest() {
        List<Artista> artistas = artistaRepository.findAll();
        return new ResponseEntity<>(artistas, HttpStatus.OK);
    }

    // Controlador GET/ Metodo para permitir obtener un registro en base a su id
    @GetMapping(
        value = "/artista/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleGetArtistaRequest (@PathVariable String id){
        Optional<Artista> artista = artistaRepository.findById(id);
        if (artista.isPresent()) {
            return new ResponseEntity<>(artista.get(), HttpStatus.OK);    
        } else {
            return new ResponseEntity<>("Artista no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    // Controlador PUT/ Metodo para permitir actualizar un registro en base a su id y el objeto entregado
    @PutMapping(
        value = "/artista/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleUpdateArtistaRequest(
        @PathVariable String id,
        @RequestBody Artista artistaActualizado
    ){
        // Verificar si el artista existe
        if (!artistaRepository.existsById(id)) {
            return new ResponseEntity<>("Artita no encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            artistaActualizado.set_id(id);
            Artista artistaGuardado = artistaRepository.save(artistaActualizado);
            return new ResponseEntity<>(artistaGuardado, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar artista: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Controlador DELETE/ Metodo para permitir eliminar un registro en base a su id
    @DeleteMapping(
        value = "/artista/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleDeleteArtistaRequest(@PathVariable String id) {
        // Verificar si el artista existe
        if (!artistaRepository.existsById(id)) {
            return new ResponseEntity<>("Artista no encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            artistaRepository.deleteById(id);
            return new ResponseEntity<>("Artista eliminado correctamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar artista: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
