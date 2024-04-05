package pet.store.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreCustomerDTO;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {

    private final PetStoreDao petStoreDao;
    private final EmployeeDao employeeDao;
    private final CustomerDao customerDao;

    @Autowired
    public PetStoreService(PetStoreDao petStoreDao, EmployeeDao employeeDao, CustomerDao customerDao) {
        this.petStoreDao = petStoreDao;
        this.employeeDao = employeeDao;
        this.customerDao = customerDao;
    }
    @Transactional
    public List<PetStoreData> retrieveAllPetStores() {
        List<PetStore> petStores = petStoreDao.findAll();
        List<PetStoreData> result = new ArrayList<>();

        for (PetStore petStore : petStores) {
            PetStoreData petStoreData = new PetStoreData(petStore);
            petStoreData.setCustomers(null);
            petStoreData.setEmployees(null); 
            result.add(petStoreData);
        }

        return result;
    }
    
    @Transactional
    public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
        PetStore petStore = petStoreDao.findByPetStoreId(petStoreId);
        Long employeeId = petStoreEmployee.getEmployeeId();
        Employee employee = findOrCreateEmployee(petStoreId, employeeId);

        copyEmployeeFields(employee, petStoreEmployee);

        employee.setPetStore(petStore);
        petStore.getEmployees().add(employee);

        Employee dbEmployee = employeeDao.save(employee);
        return new PetStoreEmployee(dbEmployee);
    }


    private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
        if (employeeId == null) {
            return new Employee();
        } else {
            return findEmployeeById(petStoreId, employeeId);
        }
    }

    private Employee findEmployeeById(Long petStoreId, Long employeeId) {
        Optional<Employee> employeeOptional = employeeDao.findById(employeeId);
        Employee employee = employeeOptional.orElseThrow(() -> new NoSuchElementException("Employee not found"));

        if (!employee.getPetStore().getPetStoreId().equals(petStoreId)) {
            throw new IllegalArgumentException("Employee doesn't belong to the given Pet Store");
        }

        return employee;
    }

    private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
        employee.setEmployeeId(petStoreEmployee.getEmployeeId());
        employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
        employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
        employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
        employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
    }



    public PetStoreData savePetStore(PetStoreData petStoreData) {
        Long petStoreId = petStoreData.getPetStoreId();
        PetStore petStore = findOrCreatePetStore(petStoreId);
        copyPetStoreFields(petStore, petStoreData);
        PetStore savedStore = petStoreDao.save(petStore);
        return new PetStoreData(savedStore);
    }

    public PetStore findOrCreatePetStore(Long petStoreId) {
        if (petStoreId == null) {
            PetStore newPetStore = new PetStore();
            return newPetStore;
        } else {
            Optional<PetStore> existingPetStoreOptional = petStoreDao.findById(petStoreId);
            
            if (existingPetStoreOptional.isPresent()) {
                return existingPetStoreOptional.get();
            } else {
                throw new NoSuchElementException("PetStore with ID " + petStoreId + " not found.");
            }
        }
    }

    public void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
        petStore.setPetStoreId(petStoreData.getPetStoreId());
        petStore.setPetStoreName(petStoreData.getPetStoreName());
        petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
        petStore.setPetStoreCity(petStoreData.getPetStoreCity());
        petStore.setPetStoreState(petStoreData.getPetStoreState());
        petStore.setPetStoreZip(petStoreData.getPetStoreZip());
        petStore.setPetStorePhone(petStoreData.getPetStorePhone());
    }
    @Transactional
    public void addCustomerToPetStore(Long petStoreId, PetStoreCustomerDTO customerDTO) {
    	PetStore petStore = petStoreDao.findByPetStoreId(petStoreId);

        Customer customer = createOrFetchCustomer(customerDTO);

        customer.getPetStores().add(petStore);

        customerDao.save(customer);
    }

    private Customer createOrFetchCustomer(PetStoreCustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setCustomerFirstName(customerDTO.getCustomerFirstName());
        customer.setCustomerLastName(customerDTO.getCustomerLastName());
        customer.setCustomerEmail(customerDTO.getCustomerEmail());

        return customer;
    }
    @Transactional(readOnly = true)
    public PetStoreData retrievePetStoreById(Long storeId) {
        Optional<PetStore> petStoreOptional = petStoreDao.findById(storeId);
        PetStore petStore = petStoreOptional.orElseThrow(() -> new NoSuchElementException("Pet Store not found"));

        PetStoreData petStoreData = new PetStoreData(petStore);

        return petStoreData;
    }
    @Transactional
    public void deletePetStoreById(Long storeId) {
        PetStore petStore = findOrCreatePetStore(storeId);
        
        petStore.getEmployees().clear();
        
        petStoreDao.delete(petStore);
    }

}