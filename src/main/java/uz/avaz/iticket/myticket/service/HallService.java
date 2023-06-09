package uz.avaz.iticket.myticket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.avaz.iticket.myticket.dto.HallDto;
import uz.avaz.iticket.myticket.dto.RowDto;
import uz.avaz.iticket.myticket.dto.SeatDto;
import uz.avaz.iticket.myticket.model.Hall;
import uz.avaz.iticket.myticket.model.PriceCategory;
import uz.avaz.iticket.myticket.model.Row;
import uz.avaz.iticket.myticket.model.Seat;
import uz.avaz.iticket.myticket.payload.ApiResponse;
import uz.avaz.iticket.myticket.projection.HallAllDataProjection;
import uz.avaz.iticket.myticket.repository.HallRepository;
import uz.avaz.iticket.myticket.repository.PriceCategoryRepository;
import uz.avaz.iticket.myticket.repository.RowRepository;
import uz.avaz.iticket.myticket.repository.SeatRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static uz.avaz.iticket.myticket.utils.Constants.*;

@Service
public class HallService {

    @Autowired
    HallRepository hallRepository;

    @Autowired
    PriceCategoryRepository priceCategoryRepo;

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    RowRepository rowRepository;

    @Transactional
    public ResponseEntity<?> addHall(HallDto hallDto) {
        Hall hall = hallRepository.save(new Hall(hallDto.getName(), hallDto.getVipAdditionalFeeInPercent()));
        if (hallDto.getRows().size() > 0) {
            for (RowDto row : hallDto.getRows()) {
                Row saveRow = rowRepository.save(new Row(row.getNumber(), hall));
                for (SeatDto seat : row.getSeats()) {
                    PriceCategory priceCategory;
                    try {
                        priceCategory = priceCategoryRepo.findById(seat.getPriceCategoryId()).orElseThrow(() ->
                                new ResourceNotFoundException("price category not found!"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        for (Row row2 : rowRepository.findRowByHallId(hall.getId())) {
                            List<UUID> seatByRowId = seatRepository.findSeatByRowId(row2.getId());
                            seatRepository.deleteAllById(seatByRowId);
                            rowRepository.delete(row2);
                        }
                        hallRepository.delete(hall);
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(OBJECT_NOT_FOUND, false, null));
                    }
                    for (Integer i = seat.getStartSeatNumber(); i <= (seat.getEndSeatNumber() != null ? seat.getEndSeatNumber() : seat.getStartSeatNumber()); i++) {
                        boolean alreadyExist = seatRepository.existsSeatByNumberAndRow(i, saveRow);
                        if (!alreadyExist) {
                            seatRepository.save(new Seat(i, priceCategory, saveRow));
                        } else {
                            for (Row row2 : rowRepository.findRowByHallId(hall.getId())) {
                                List<UUID> seatByRowId = seatRepository.findSeatByRowId(row2.getId());
                                seatRepository.deleteAllById(seatByRowId);
                                rowRepository.delete(row2);
                            }
                            hallRepository.deleteById(hall.getId());
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(EXISTS, false, null));
                        }
                    }
                }
            }
        }
        return ResponseEntity.ok(new ApiResponse(SUCCESS_SAVE, true, hall));
    }

    public ResponseEntity<?> getAllHalls() {
        List<Hall> hallList = hallRepository.findAll();
        return ResponseEntity.ok(new ApiResponse("Success", true, hallList));
    }

    public ResponseEntity<?> deleteHall(UUID id) {
        try {
            for (Row row : rowRepository.findRowByHallId(id)) {
                List<UUID> seatByRowId = seatRepository.findSeatByRowId(row.getId());
                seatRepository.deleteAllById(seatByRowId);
                rowRepository.delete(row);
            }
            hallRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse(SUCCESS_DELETE, true, null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(FAILED_TO_DELETE, false, null));
        }
    }

    public ResponseEntity<?> editHall(UUID id, HallDto hallDto) {
        try {
            Optional<Hall> optionalHall = hallRepository.findById(id);
            Hall hall = optionalHall.orElseThrow(() -> new ResourceNotFoundException("Hall not found!"));
            hall.setName(hallDto.getName());
            hall.setVipAdditionalFeeInPercent(hallDto.getVipAdditionalFeeInPercent());
            Hall savedHall = hallRepository.save(hall);
            return ResponseEntity.ok(new ApiResponse(SUCCESS_EDIT, true, savedHall));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(OBJECT_NOT_FOUND, false, null));
        }
    }

    public ResponseEntity<?> getHallById(UUID id) {

        try {
      HallAllDataProjection hallsById = hallRepository.findHallsById(id);
            return ResponseEntity.ok(new ApiResponse("Success",true, hallsById));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(EMPTY_LIST,false, null));
        }
    }
}
