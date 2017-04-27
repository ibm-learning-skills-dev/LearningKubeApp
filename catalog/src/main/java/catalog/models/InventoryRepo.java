package catalog.models;

import java.util.List;
import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Orders Repository
 *
 */

@Repository("itemsRepo")
@Transactional
public interface InventoryRepo extends CrudRepository<Inventory, Long> {
}

