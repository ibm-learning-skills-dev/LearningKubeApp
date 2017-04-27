package inventory.models;

import java.util.List;
import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Orders Repository
 * 
 */

@Repository("ordersRepo")
@Transactional
public interface OrdersRepo extends CrudRepository<Orders, Long> {
}
