package uz.avaz.iticket.myticket.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.avaz.iticket.myticket.model.Attachment;
import uz.avaz.iticket.myticket.payload.ApiResponse;
import uz.avaz.iticket.myticket.service.AttachmentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {

    @Autowired
    AttachmentService attachmentService;

    @GetMapping
    public ResponseEntity<?> getAllAttachment(){
       List<Attachment> attachments = attachmentService.getAllAttachment();
       return ResponseEntity.ok(new ApiResponse("Success", true,attachments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAttachmentContentById(@PathVariable UUID id){
        return attachmentService.getAttachmentContentById(id);
    }

    @PostMapping
    public ResponseEntity<?> saveAttachment(@RequestPart("file")MultipartFile multipartFile){
       Attachment attachment = attachmentService.saveAttachment(multipartFile);
        if (attachment != null) {
            return ResponseEntity.ok(new ApiResponse("Successfully added", true, attachment));
        } else {
            return ResponseEntity.ok(new ApiResponse("Could not save", false, null));
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAttachment(@PathVariable UUID id){
        attachmentService.deleteAttachment(id);
        return ResponseEntity.ok(new ApiResponse("Successfully deleted", true, null));
    }
}
