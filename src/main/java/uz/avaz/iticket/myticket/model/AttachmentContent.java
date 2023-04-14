package uz.avaz.iticket.myticket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.avaz.iticket.myticket.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Arrays;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "attachment_contents")
public class AttachmentContent extends AbsEntity {
    private byte[] data;


    @OneToOne
    private Attachment attachment;

    public AttachmentContent(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AttachmentContent{" +
                "data=" + Arrays.toString(data) +
                '}';
    }
}
