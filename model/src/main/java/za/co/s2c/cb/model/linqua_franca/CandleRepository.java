package za.co.s2c.cb.model.linqua_franca;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CandleRepository extends CrudRepository<Candle, UUID> {
}
