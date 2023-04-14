package uz.avaz.iticket.myticket.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.avaz.iticket.myticket.dto.RowDto;
import uz.avaz.iticket.myticket.dto.SeatDto;
import uz.avaz.iticket.myticket.model.Hall;
import uz.avaz.iticket.myticket.model.PriceCategory;
import uz.avaz.iticket.myticket.model.Row;
import uz.avaz.iticket.myticket.model.Seat;
import uz.avaz.iticket.myticket.payload.ApiResponse;
import uz.avaz.iticket.myticket.projection.RowAllDataProjection;
import uz.avaz.iticket.myticket.projection.RowProjection;
import uz.avaz.iticket.myticket.repository.HallRepository;
import uz.avaz.iticket.myticket.repository.PriceCategoryRepository;
import uz.avaz.iticket.myticket.repository.RowRepository;
import uz.avaz.iticket.myticket.repository.SeatRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static uz.avaz.iticket.myticket.utils.Constants.*;

@Service
public class RowService {

    @Autowired
    RowRepository rowRepository;

    @Autowired
    HallRepository hallRepository;

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    PriceCategoryRepository priceCategoryRepo;

    public ResponseEntity<?> getAllRows(UUID id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(ERROR,false,null));
        }else{
            List<RowProjection> rowList = rowRepository.findRowsAllByHallId(id);
            return ResponseEntity.ok(new ApiResponse("Success",true,rowList));
        }
    }

    @Transactional(value = Transactional.TxType.NEVER)
    public ResponseEntity<?> addRow(UUID id, RowDto rowDto) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(ERROR,false,null));
        }else{
            Hall hall;
            try {
                hall = hallRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hall not found!"));
            }catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(OBJECT_NOT_FOUND,false,null));
            }
            try {

                Row row = rowRepository.save(new Row(rowDto.getNumber(), hall));
                if (rowDto.getSeats().size()>0) {
                    for (SeatDto seat : rowDto.getSeats()) {
                        PriceCategory priceCategory;
                        try {
                            priceCategory = priceCategoryRepo.findById(seat.getPriceCategoryId()).orElseThrow(() ->
                                    new ResourceNotFoundException("price category not found!"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(OBJECT_NOT_FOUND, false, null));
                        }
                        for (Integer i = seat.getStartSeatNumber(); i <= (seat.getEndSeatNumber() != null ? seat.getEndSeatNumber() : seat.getStartSeatNumber()); i++) {
                            boolean alreadyExist = seatRepository.existsSeatByNumberAndRow(i, row);
                            if (!alreadyExist) {
                                seatRepository.save(new Seat(i, priceCategory, row));
                            } else {
                                    List<UUID> seatByRowId = seatRepository.findSeatByRowId(row.getId());
                                    seatRepository.deleteAllById(seatByRowId);
                                    rowRepository.delete(row);
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(EXISTS, false, null));
                            }
                        }

                    }
                }
                return ResponseEntity.ok(new ApiResponse(SUCCESS_SAVE,true,row));
            }catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(FAILED_TO_SAVE,false,null));
            }

        }
    }

    public ResponseEntity<?> deleteRow( UUID rowId) {
        if ( rowId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(ERROR,false,null));
        }else{
            Row row;
            try {
             row = rowRepository.findById(rowId).orElseThrow(() -> new ResourceNotFoundException("Row not found !"));
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(OBJECT_NOT_FOUND, false, null));
            }
            try {
            List<UUID> seatByRowId = seatRepository.findSeatByRowId(rowId);
            seatRepository.deleteAllById(seatByRowId);
            rowRepository.deleteById(rowId);
            return ResponseEntity.ok(new ApiResponse(SUCCESS_DELETE,true,null));
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(FAILED_TO_DELETE,false,null));
            }
        }
    }

    public ResponseEntity<?> getRowById(UUID rowId) {
        if ( rowId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(ERROR,false,null));
        }else{
            try {
            RowAllDataProjection row = rowRepository.findRowsById(rowId);
            return ResponseEntity.ok(new ApiResponse("Success",true,row));
            }catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(OBJECT_NOT_FOUND, false, null));
            }
        }
    }

    @Transactional
    public ResponseEntity<?> editRow(UUID id, RowDto rowDto) {
        if ( id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(ERROR,false,null));
        }else{
            Row row;
            try{
             row = rowRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Row not found!"));
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(OBJECT_NOT_FOUND, false, null));
            }
            try {
                row.setNumber(rowDto.getNumber());
                Row editRow = rowRepository.save(row);
                return ResponseEntity.ok(new ApiResponse(SUCCESS_EDIT,true,editRow));
            }catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(FAILED_TO_EDIT,false,null));
            }

        }
    }
}
