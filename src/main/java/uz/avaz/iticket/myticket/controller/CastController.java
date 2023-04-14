package uz.avaz.iticket.myticket.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.avaz.iticket.myticket.dto.CastDto;
import uz.avaz.iticket.myticket.model.Cast;
import uz.avaz.iticket.myticket.payload.ApiResponse;
import uz.avaz.iticket.myticket.service.CastService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cast")
public class CastController {

    @Autowired
    CastService castService;

    @GetMapping
    public ResponseEntity<?> getAllCast(){
       List<Cast> castList = castService.getAllCast();
        return ResponseEntity.ok(new ApiResponse("succes",true,castList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCastById(@PathVariable UUID id){
        Cast cast = castService.getCastById(id);
        return ResponseEntity.ok(new ApiResponse("succes",true,cast));
    }

    @PostMapping
    public ResponseEntity<?> addCast(
            @RequestPart(name = "file")MultipartFile file,
            @RequestPart(name = "json") CastDto castDto
            ){
        Cast addCast = castService.addCast(file,castDto);
        if (addCast != null) {
        return ResponseEntity.ok(new ApiResponse("succes",true,addCast));
        }else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("Error",false, null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editCast(@PathVariable UUID id, @RequestBody CastDto castDto){
       Cast cast =  castService.edit(id,castDto);
        if (cast != null) {
            return ResponseEntity.ok(new ApiResponse("Successfully edited",true,cast));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("Error", false, null));
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCast(@PathVariable UUID id){
        castService.deleteCast(id);
        return ResponseEntity.ok(new ApiResponse("Successfully deleted",true,null));
    }
}
