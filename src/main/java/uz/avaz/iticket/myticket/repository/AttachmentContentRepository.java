package uz.avaz.iticket.myticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.avaz.iticket.myticket.model.Attachment;
import uz.avaz.iticket.myticket.model.AttachmentContent;
import uz.avaz.iticket.myticket.projection.AttachmentContentDataProjection;

import java.util.UUID;

public interface AttachmentContentRepository extends JpaRepository<AttachmentContent, UUID> {
//    @Query(nativeQuery = true,value = "select a from attachment_contents a where a.attachment_id = :id")
    AttachmentContent findAttachmentContentByAttachmentId(UUID id);
    AttachmentContent findAttachmentContentByAttachment(Attachment attachment);
    AttachmentContentDataProjection findAttachmentContentDataByAttachment(Attachment attachment);

}
