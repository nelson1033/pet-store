package pet.store.controller.model;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {

    private final PetStoreService petStoreService;

    @Autowired
    public PetStoreController(PetStoreService petStoreService) {
        this.petStoreService = petStoreService;
    }
    
    @GetMapping
    public List<PetStoreData> getAllPetStores() {
    	return petStoreService.retrieveAllPetStores();
    }

    @PostMapping
    public PetStoreData createPetStore(@RequestBody PetStoreData petStoreData) {
        log.info("Received request to create a pet store: {}", petStoreData);
        return petStoreService.savePetStore(petStoreData);
    }
    
    @PutMapping("/{storeId}")
    public PetStoreData updatePetStore(@PathVariable Long storeId, @RequestBody PetStoreData petStoreData) {
        log.info("Received request to update pet store with ID {}: {}", storeId, petStoreData);
        
        petStoreData.setPetStoreId(storeId);
        
        return petStoreService.savePetStore(petStoreData);
    }
    @PostMapping("/{petStoreId}/employee")
    @ResponseStatus(HttpStatus.CREATED)
    public PetStoreEmployee addEmployeeToPetStore(
    		@PathVariable Long petStoreId,
    		@RequestBody PetStoreEmployee employee
    ) {
    	System.out.println("Adding employee to Pet Store with ID: " + petStoreId);
    	
    	return petStoreService.saveEmployee(petStoreId, employee);
    }
        @PostMapping("/{id}/customer")
        public ResponseEntity<String> addCustomerToPetStore(@PathVariable("id") Long petStoreId, @RequestBody PetStoreCustomerDTO customerDTO) {
            petStoreService.addCustomerToPetStore(petStoreId, customerDTO);
            return ResponseEntity.ok("Customer added to pet store successfully");
        }
        @GetMapping("/{storeId}")
        public PetStoreData getPetStoreById(@PathVariable Long storeId) {
            return petStoreService.retrievePetStoreById(storeId);
        }
        @DeleteMapping("/{storeId}")
        public Map<String, String> deletePetStoreById(@PathVariable Long storeId) {
            log.info("Received request to delete pet store with ID: {}", storeId);
            
            petStoreService.deletePetStoreById(storeId);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Pet store deleted successfully");
            return response;
        }

    }

    