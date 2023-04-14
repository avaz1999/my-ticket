package uz.avaz.iticket.myticket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.avaz.iticket.myticket.dto.MovieDto;
import uz.avaz.iticket.myticket.enums.MovieStatus;
import uz.avaz.iticket.myticket.model.*;
import uz.avaz.iticket.myticket.payload.ApiResponse;
import uz.avaz.iticket.myticket.projection.MovieProjection;
import uz.avaz.iticket.myticket.repository.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static uz.avaz.iticket.myticket.utils.Constants.*;

@Service
public class MovieService {


    @Autowired
    MovieRepository movieRepository;

    @Autowired
    AttachmentRepository attachmentRepo;

    @Autowired
    CastRepository castRepository;

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    DistributorRepository distributorRepository;

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepo;

    @Transactional
    public ResponseEntity<?> addMovie(MultipartFile file, MovieDto movieDto) {
        try {
            Attachment attachment = attachmentRepo.save(new Attachment(file.getOriginalFilename(), file.getContentType(), file.getSize()));
            AttachmentContent attachmentContent = attachmentContentRepo.save(new AttachmentContent(file.getBytes(), attachment));
            List<Cast> castList = castRepository.findAllById(movieDto.getCastIds());
            List<Distributor> distributorList = distributorRepository.findAllById(movieDto.getDistributorIds());
            List<Genre> genreList = genreRepository.findAllById(movieDto.getGenreIds());
            List<Country> countryList = countryRepository.findAllById(movieDto.getCountryIds());
            Movie movie = movieRepository.save(new Movie(
                    movieDto.getTitle(),
                    movieDto.getDescription(),
                    movieDto.getDurationInMin(),
                    movieDto.getTicketInitPrice(),
                    movieDto.getTrailerVideoUrl(),
                    movieDto.getReleaseDate(),
                    movieDto.getBudget(),
                    MovieStatus.getMovieDisplayStatus(movieDto.getMovieStatus()),
                    movieDto.getDistributorShareInPercentage(),
                    attachment,
                    castList,
                    genreList,
                    countryList,
                    distributorList
            ));
            return ResponseEntity.ok(new ApiResponse(SUCCESS_SAVE, true, movie));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(FAILED_TO_DELETE, false, null));
        }

    }

    public ResponseEntity<?> getAllMovies() {
        List<Movie> movieList = movieRepository.findAll();
        return ResponseEntity.ok(new ApiResponse("Success", true, movieList));
    }

    public ResponseEntity<?> deleteMovie(UUID id) {
        if (id == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(ERROR, false, null));
        try {
            Movie movie = movieRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movie not fond !"));
            AttachmentContent contentByAttachment = attachmentContentRepo.findAttachmentContentByAttachmentId(movie.getPosterImg().getId());
            attachmentContentRepo.delete(contentByAttachment);
            movieRepository.delete(movie);
            return ResponseEntity.ok(new ApiResponse(SUCCESS_DELETE, true, null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(OBJECT_NOT_FOUND, false, null));
        }
    }

    public ResponseEntity<?> editMovie(UUID id, MovieDto movieDto) {
        if (id == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(ERROR, false, null));
        try {
            Movie movie = movieRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movie not found!"));
            movie.setTitle(movieDto.getTitle() != null ? movieDto.getTitle() : movie.getTitle());
            movie.setDescription(movieDto.getDescription() != null ? movieDto.getDescription() : movie.getDescription());
            movie.setBudget(movieDto.getBudget() != null ? movieDto.getBudget() : movie.getBudget());
            movie.setDurationInMin(movieDto.getDurationInMin() != null ? movieDto.getDurationInMin() : movie.getDurationInMin());
            movie.setDistributorShareInPercentage(movieDto.getDistributorShareInPercentage() != null ? movieDto.getDistributorShareInPercentage() : movie.getDistributorShareInPercentage());
            movie.setReleaseDate(movieDto.getReleaseDate() != null ? movieDto.getReleaseDate() : movie.getReleaseDate());
            movie.setTicketInitPrice(movieDto.getTicketInitPrice() != 0 ? movieDto.getTicketInitPrice() : movie.getTicketInitPrice());
            movie.setTrailerVideoUrl(movieDto.getTrailerVideoUrl() != null ? movieDto.getTrailerVideoUrl() : movie.getTrailerVideoUrl());
            movie.setMovieStatus(movieDto.getMovieStatus() != null ? MovieStatus.getMovieDisplayStatus(movieDto.getMovieStatus()) : movie.getMovieStatus());
            Movie editMovie = movieRepository.save(movie);
            return ResponseEntity.ok(new ApiResponse(SUCCESS_EDIT, true, editMovie));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(OBJECT_NOT_FOUND, false, null));
        }

    }

    public ResponseEntity<?> getMovieById(UUID id) {
        if (id == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(ERROR, false, null));
        try {
            MovieProjection movieById = movieRepository.findMovieById(id);
            if (movieById != null) {
                return ResponseEntity.ok(new ApiResponse(SUCCESS, true, movieById));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(OBJECT_NOT_FOUND, false, null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(OBJECT_NOT_FOUND, false, null));
        }
    }
}
