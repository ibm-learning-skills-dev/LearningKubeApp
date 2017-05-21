package auth;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Response;

import auth.config.CloudantPropertiesBean;
import auth.model.Customer;

import javax.annotation.PostConstruct;

import java.util.Base64;
import java.util.List;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import auth.config.*;
import auth.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller to manage Customer database
 *
 */
@RestController
public class AuthController {
    
    private static Logger logger =  LoggerFactory.getLogger(AuthController.class);
    private Database cloudant;

    @Autowired
    private CloudantPropertiesBean cloudantProperties;

    @PostConstruct
    private void init() throws MalformedURLException {
        logger.debug(cloudantProperties.toString());

        try {
            logger.info("Connecting to cloudant at: " + "https://" + cloudantProperties.getHost() + ":" + cloudantProperties.getPort());
            final CloudantClient cloudantClient = ClientBuilder.url(new URL("https://" + cloudantProperties.getHost() + ":" + cloudantProperties.getPort()))
                    .username(cloudantProperties.getUsername())
                    .password(cloudantProperties.getPassword())
                    .build();

            cloudant = cloudantClient.database(cloudantProperties.getDatabase(), true);


            // create the design document if it doesn't exist
            if (!cloudant.contains("_design/username_searchIndex")) {
                final Map<String, Object> names = new HashMap<String, Object>();
                names.put("index", "function(doc){index(\"usernames\", doc.username); }");

                final Map<String, Object> indexes = new HashMap<>();
                indexes.put("usernames", names);

                final Map<String, Object> view_ddoc = new HashMap<>();
                view_ddoc.put("_id", "_design/username_searchIndex");
                view_ddoc.put("indexes", indexes);

                cloudant.save(view_ddoc);
            }

        } catch (MalformedURLException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }


    }

  
   
    /**
     * check
     */
    @RequestMapping("/check")
    @ResponseBody String check() {
        return "it works!";
    }

    private Database getCloudantDatabase()  {
        return cloudant;
    }


    private ResponseEntity<?> authenticate(String username, String password) {
     	logger.debug("Authenticating: user=" + username + ", password=" + password);
       
        try {
                if (username == null) {
                        return ResponseEntity.badRequest().body("Missing username");
                }
                final List<Customer> customers = getCloudantDatabase().findByIndex(
                                "{ \"selector\": { \"username\": \"" + username + "\" } }",
                                Customer.class);

                if (customers.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }

                final Customer cust = customers.get(0);

        // TODO: hash password -- in the customer service
        if (!cust.getPassword().equals(password)) {
                // password doesn't match
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // write the customer ID to the response in the header: "API-Authenticated-Credential"
        // this tell API Connect who the access token belongs to/what it corresponds to in
        // the customer database
        return ResponseEntity.ok().header("API-Authenticated-Credential", cust.getCustomerId()).build();


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
 
    
    /**
     * Handle auth header
     * @return HTTP 200 if success
     */
    @RequestMapping(value = "/authenticate", method = RequestMethod.GET)
    @ResponseBody ResponseEntity<?> getAuthenticate(@RequestHeader(value="Authorization", required=false) String authHeader) {
    	logger.info("GET /authenticate: auth header = " + authHeader);
    	
    	if (authHeader == null) {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    	}
    	
    	// authorization string is like "Basic <base64encoded>"
    	final String creds = authHeader.replace("Basic ", "");
    	
    	if (creds == null || creds.length() == 0) {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    	}
    	
    	final String decodedCreds;
    	try {
			decodedCreds = new String(Base64.getDecoder().decode(creds));
    	} catch (Exception e) {
    		// if I can't decode for any reason, HTTP 401
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    	}
    	
    	final String[] split = decodedCreds.split(":");
    	
    	if (split.length != 2) {
    		// wrong format
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    	}
    	
    	return authenticate(split[0], split[1]);
    }
    
   
	/**
	 * Handle login form
     * @return HTTP 200 if success
     */
    @RequestMapping(value = "/authorize", method = RequestMethod.POST)
    @ResponseBody ResponseEntity<?> postAuthenticate(String username, String password, String origURI, String appname) {
    	logger.info("POST /authorize, username=" + username + ", password=" + password + ", appname="+appname);
    	logger.info(origURI);
        try {
                if (username == null) {
                        return ResponseEntity.badRequest().body("Missing username");
                }
                final List<Customer> customers = getCloudantDatabase().findByIndex(
                                "{ \"selector\": { \"username\": \"" + username + "\" } }",
                                Customer.class);

                if (customers.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }

                final Customer cust = customers.get(0);
    	        URI orig = new URI(origURI+"&app-name="+appname+"&username="+cust.getUsername()+"&confirmation="+cust.getPassword());
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setLocation(orig);
                return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    	//return authenticate(username, password);
    }
}
