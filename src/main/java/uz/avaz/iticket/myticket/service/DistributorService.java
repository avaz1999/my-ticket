package uz.avaz.iticket.myticket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.avaz.iticket.myticket.dto.DistributorDto;
import uz.avaz.iticket.myticket.model.Attachment;
import uz.avaz.iticket.myticket.model.AttachmentContent;
import uz.avaz.iticket.myticket.model.Distributor;
import uz.avaz.iticket.myticket.repository.AttachmentContentRepository;
import uz.avaz.iticket.myticket.repository.AttachmentRepository;
import uz.avaz.iticket.myticket.repository.DistributorRepository;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class DistributorService {

    @Autowired
    DistributorRepository distributorRepo;

    @Autowired
    AttachmentRepository attachmentRepo;

    @Autowired
    AttachmentContentRepository attachmentContentRepo;

    public List<Distributor> getAllDistributor() {
        return distributorRepo.findAll();
    }

    public Distributor saveDistributor(MultipartFile multipartFile, DistributorDto distributorDto ) {
        try {
            Attachment attachment = attachmentRepo.save(new Attachment(multipartFile.getOriginalFilename(),
                    multipartFile.getContentType(), multipartFile.getSize()));
                    attachmentContentRepo.save(new AttachmentContent(multipartFile.getBytes(), attachment));
            return distributorRepo.save(new Distributor(distributorDto.getName(), distributorDto.getDescription(), attachment));
        } catch (IOException e) {
            e.printStackTrace();
             return null;
        }


    }

    public void deleteDistributor(UUID id) {
        Distributor distributorById = distributorRepo.findDistributorById(id);
        AttachmentContent content = attachmentContentRepo.findAttachmentContentByAttachment(distributorById.getLogo());
        attachmentContentRepo.deleteById(content.getId());
        distributorRepo.deleteById(id);
    }

    public Distributor getDistributorById(UUID id) {
      return   distributorRepo.findDistributorById(id);
    }

    public Distributor editDistributor(DistributorDto distributorDto, UUID id) {
        Distributor distributorById = distributorRepo.findDistributorById(id);
        distributorById.setName(distributorDto.getName());
        distributorById.setDescription(distributorDto.getDescription());
        return distributorRepo.save(distributorById);
    }
}
