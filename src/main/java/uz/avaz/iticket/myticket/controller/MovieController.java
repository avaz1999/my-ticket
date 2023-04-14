package uz.avaz.iticket.myticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.avaz.iticket.myticket.dto.MovieDto;
import uz.avaz.iticket.myticket.service.MovieService;

import java.util.UUID;

@RestController
@RequestMapping("/api/movie")
public class MovieController {

    @Autowired
    MovieService movieService;

    @GetMapping
    public ResponseEntity<?> getAllMovies(){
      return   movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable UUID id){
        return movieService.getMovieById(id);
    }

    @PostMapping
    public ResponseEntity<?> addMovie(@RequestPart("file")MultipartFile file, @RequestPart("json") MovieDto movieDto){
      return   movieService.addMovie(file,movieDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editMovie(@PathVariable UUID id, @RequestBody MovieDto movieDto){
        return movieService.editMovie(id,movieDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable UUID id){
        return movieService.deleteMovie(id);
    }
}
