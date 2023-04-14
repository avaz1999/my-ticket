package uz.avaz.iticket.myticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.avaz.iticket.myticket.model.PayType;

import java.util.UUID;

public interface PayTypeRepository extends JpaRepository<PayType, UUID> {

    PayType findPayTypeByName(String payTypeName);
}
