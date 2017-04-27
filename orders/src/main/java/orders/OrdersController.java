package orders;

import orders.models.*;
import orders.config.*;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * REST Controller to manage Inventory database
 *
 */
@RestController("ordersController")
public class OrdersController {
	
	Logger logger =  LoggerFactory.getLogger(OrdersController.class);

	@Autowired
	BackendConfig backend;

	/**
	 * check
	 */
	@RequestMapping("/check")
	@ResponseBody String check() {
		return "it works!";
	}

	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	ResponseEntity<?> getOrders() {
                try {
                RESTRequest rest = new RESTRequest("http://"+backend.getHost()+":"+backend.getPort());
                return ResponseEntity.ok(rest.get("/micro/orders",false));
                } catch(Exception e) { e.printStackTrace();return ResponseEntity.status(500).build();}
        }

	@RequestMapping(value = "/orders/{id}", method = RequestMethod.GET)
	ResponseEntity<?> getOrder(@PathVariable long id) {
                try {
                RESTRequest rest = new RESTRequest("http://"+backend.getHost()+":"+backend.getPort());
                return ResponseEntity.ok(rest.get("/micro/orders/"+id,false));
                } catch(Exception e) { e.printStackTrace();return ResponseEntity.status(500).build();}
        }

	/**
	 * @place order - substrack stock from inventory
	 */
	@RequestMapping(value = "/orders", method = RequestMethod.POST)
	ResponseEntity<?> placeOrder(@RequestHeader(value = "ibm-app-user") String user , @RequestBody Orders payload) {
                try {
                if (user == null) return ResponseEntity.notFound().build();
                int[] errlist = new int[3];
                errlist[0] = 404;
                payload.setCustomerId(user);
                RESTRequest rest = new RESTRequest("http://"+backend.getHost()+":"+backend.getPort());
                return ResponseEntity.ok(rest.post("/micro/orders",payload.toString(),errlist));
                } catch(Exception e) { e.printStackTrace();return ResponseEntity.status(500).build();}
	}
}
