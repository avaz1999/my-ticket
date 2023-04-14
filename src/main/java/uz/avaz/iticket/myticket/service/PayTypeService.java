package uz.avaz.iticket.myticket.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.avaz.iticket.myticket.model.Attachment;
import uz.avaz.iticket.myticket.model.AttachmentContent;
import uz.avaz.iticket.myticket.model.PayType;
import uz.avaz.iticket.myticket.payload.ApiResponse;
import uz.avaz.iticket.myticket.repository.AttachmentContentRepository;
import uz.avaz.iticket.myticket.repository.AttachmentRepository;
import uz.avaz.iticket.myticket.repository.PayTypeRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.UUID;

import static uz.avaz.iticket.myticket.utils.Constants.*;

@Service
public class PayTypeService {

    @Autowired
    PayTypeRepository payTypeRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepository;


    @Transactional
    public ResponseEntity<?> addPayType(MultipartFile file, String name) {
        if (file.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(ERROR,false,null));
        try {
        Attachment attachment = attachmentRepository.save(new Attachment(file.getOriginalFilename(), file.getContentType(), file.getSize()));
            AttachmentContent attachmentContent = attachmentContentRepository.save(new AttachmentContent(file.getBytes(), attachment));
        payTypeRepository.save(new PayType(name,attachment));
        return ResponseEntity.ok(new ApiResponse(SUCCESS_SAVE,true,null));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(FAILED_TO_SAVE,false,null));
        }
    }

    public ResponseEntity<?> getAllPayType() {
        return ResponseEntity.ok(new ApiResponse(SUCCESS,true, payTypeRepository.findAll()));
    }

    public ResponseEntity<?> deletePayType(UUID id) {
        if (id == null || id.equals("")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(ERROR,false,null));
        try {
            PayType payType = payTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PayType not found!"));
            AttachmentContent contentByAttachment = attachmentContentRepository.findAttachmentContentByAttachment(payType.getLogo());
            attachmentContentRepository.delete(contentByAttachment);
            attachmentRepository.delete(payType.getLogo());
            payTypeRepository.delete(payType);
            return ResponseEntity.ok(new ApiResponse(SUCCESS_DELETE,true,null));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(FAILED_TO_DELETE,false,null));
        }
    }

    public ResponseEntity<?> getPayTypeById(UUID id) {
        if (id == null || id.equals("")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(ERROR,false,null));
        try {
            PayType payType = payTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PayType not found!"));
            return ResponseEntity.ok(new ApiResponse(SUCCESS,true,payType));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(OBJECT_NOT_FOUND,false,null));
        }

    }
}
