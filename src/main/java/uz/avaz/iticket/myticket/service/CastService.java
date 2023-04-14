package uz.avaz.iticket.myticket.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.avaz.iticket.myticket.dto.CastDto;
import uz.avaz.iticket.myticket.enums.CastType;
import uz.avaz.iticket.myticket.model.Attachment;
import uz.avaz.iticket.myticket.model.AttachmentContent;
import uz.avaz.iticket.myticket.model.Cast;
import uz.avaz.iticket.myticket.repository.AttachmentContentRepository;
import uz.avaz.iticket.myticket.repository.AttachmentRepository;
import uz.avaz.iticket.myticket.repository.CastRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CastService {

    @Autowired
    CastRepository castRepository;

    @Autowired
    AttachmentRepository attachmentRepo;

    @Autowired
    AttachmentContentRepository attachmentContentRepo;

    public List<Cast> getAllCast() {
        return castRepository.findAll();
    }

    public Cast getCastById(UUID id) {
        Optional<Cast> byId = castRepository.findById(id);
        return byId.orElseGet(byId::get);
    }

    public Cast addCast(MultipartFile file, CastDto castDto) {
        try {
            Attachment attachment = attachmentRepo.save(new Attachment(file.getOriginalFilename(), file.getContentType(), file.getSize()));
            attachmentContentRepo.save(new AttachmentContent(file.getBytes(), attachment));
           return castRepository.save(new Cast(castDto.getFullName(),attachment, CastType.getCastDisplayType(castDto.getCastType())));
        } catch (Exception e){
            return null;
        }
    }

    public void deleteCast(UUID id) {
        Optional<Cast> byId = castRepository.findById(id);
        Attachment attachment = attachmentRepo.findAttachmentById(byId.get().getPhoto().getId());
        AttachmentContent contentByAttachment = attachmentContentRepo.findAttachmentContentByAttachment(attachment);
        attachmentContentRepo.deleteById(contentByAttachment.getId());
        castRepository.deleteById(id);
    }

    public Cast edit(UUID id, CastDto castDto) {
        try {
        Optional<Cast> byId = castRepository.findById(id);
            if (byId.isPresent()) {
                Cast cast=byId.get();
                cast.setFullName(castDto.getFullName());
                cast.setCastType(CastType.getCastDisplayType(castDto.getCastType()));
                return castRepository.save(cast);
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
