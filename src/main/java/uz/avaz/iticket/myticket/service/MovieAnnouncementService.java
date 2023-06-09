package uz.avaz.iticket.myticket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.avaz.iticket.myticket.dto.MovieAnnouncementDto;
import uz.avaz.iticket.myticket.model.Movie;
import uz.avaz.iticket.myticket.model.MovieAnnouncement;
import uz.avaz.iticket.myticket.payload.ApiResponse;
import uz.avaz.iticket.myticket.repository.MovieAnnouncementRepository;
import uz.avaz.iticket.myticket.repository.MovieRepository;
import uz.avaz.iticket.myticket.repository.MovieSessionRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static uz.avaz.iticket.myticket.utils.Constants.*;

@Service
public class MovieAnnouncementService {

    @Autowired
    MovieAnnouncementRepository movieAnnouncementRepo;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    MovieSessionRepository movieSessionRepository;

    public ResponseEntity<?> getAllMovieAnnouncement() {
        List<MovieAnnouncement> announcementRepoAll = movieAnnouncementRepo.findAll();
        return ResponseEntity.ok(new ApiResponse(SUCCESS,true,announcementRepoAll));
    }

    @Transactional
    public ResponseEntity<?> addMovieAnnouncement(MovieAnnouncementDto movieAnnouncementDto) {
        Movie movie;
        try {
            movie = movieRepository.findById(movieAnnouncementDto.getMovieId()).orElseThrow(() -> new ResourceNotFoundException("Movie not found!"));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(OBJECT_NOT_FOUND,false,true));
        }
        try {
            MovieAnnouncement movieAnnouncement = movieAnnouncementRepo.save(new MovieAnnouncement(movie, movieAnnouncementDto.isActive()));
            return ResponseEntity.ok(new ApiResponse(SUCCESS_SAVE,true,movieAnnouncement));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(FAILED_TO_SAVE,false,null));
        }
    }

    @Transactional
    public ResponseEntity<?> editMovieAnnouncement(UUID id, MovieAnnouncementDto movieAnnouncementDto) {
        if (id == null || id.equals("")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(ERROR,false,null));
        MovieAnnouncement movieAnnouncement;
        try {
            movieAnnouncement = movieAnnouncementRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("MovieAnnouncement not found!"));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(OBJECT_NOT_FOUND,false,null));
        }
        try {
            movieAnnouncement.setActive(movieAnnouncementDto.isActive());
            MovieAnnouncement editMovieAnnouncement = movieAnnouncementRepo.save(movieAnnouncement);
            return ResponseEntity.ok(new ApiResponse(SUCCESS_EDIT,true,editMovieAnnouncement));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(FAILED_TO_EDIT,false,null));
        }
    }

    public ResponseEntity<?> deleteMovieAnnouncement(UUID id) {
        if (id == null || id.equals("")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(ERROR,false,null));
        try {
            List<UUID> movieSessionByMovieAnnouncementId = movieSessionRepository.findMovieSessionByMovieAnnouncementId(id);
            movieSessionRepository.deleteAllById(movieSessionByMovieAnnouncementId);
            movieAnnouncementRepo.deleteById(id);
            return ResponseEntity.ok(new ApiResponse(SUCCESS_DELETE,true,null));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(FAILED_TO_DELETE,false,null));
        }
    }
}
