package inventory;

import inventory.models.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * REST Controller to manage Inventory database
 *
 */
@RestController("inventoryController")
public class InventoryController {
	
	Logger logger =  LoggerFactory.getLogger(InventoryController.class);

	@Autowired
	@Qualifier("inventoryRepo")
	private InventoryRepo itemsRepo;

	@Autowired
	@Qualifier("ordersRepo")
	private OrdersRepo ordersRepo;

	/**
	 * check
	 */
	@RequestMapping("/check")
	@ResponseBody String check() {
		return "it works!";
	}

	/**
	 * @return all items in inventory
	 */
	@RequestMapping(value = "/inventory", method = RequestMethod.GET)
	@ResponseBody Iterable<Inventory> getInventory() {
		return itemsRepo.findAll();
	}

	@RequestMapping(value = "/inventory/{id}", method = RequestMethod.GET)
	ResponseEntity<?> getById(@PathVariable long id) {
		if (!itemsRepo.exists(id)) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(itemsRepo.findOne(id));
	}


	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	@ResponseBody Iterable<Orders> getOrders() {
                return ordersRepo.findAll();
        }

	@RequestMapping(value = "/orders/{id}", method = RequestMethod.GET)
	ResponseEntity<?> getOrder(@PathVariable long id) {
                if (!ordersRepo.exists(id)) {
                        return ResponseEntity.notFound().build();
                }

                return ResponseEntity.ok(ordersRepo.findOne(id));
        }

	/**
	 * @place order - substrack stock from inventory
	 */
	@RequestMapping(value = "/orders", method = RequestMethod.POST)
	ResponseEntity<?> placeOrder(@RequestBody Orders payload) {
                payload.setOrderId(ordersRepo.count()+1);
                System.out.println(payload.toString());
                int num = payload.getCount();
                long itemId = payload.getItemId();
                Inventory inv = itemsRepo.findOne(itemId);
                System.out.println(inv.toString());
                inv.setStock(inv.getStock()-num);
                if (inv.getStock()<0) inv.setStock(0);
                System.out.println(inv.toString());
                ordersRepo.save(payload);
                System.out.println("Order saved ...");
                itemsRepo.save(inv);
                System.out.println("Item saved ...");
		return ResponseEntity.ok("{\"status\": \"OK\"}");
	}
}
