package pet.store.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.Employee;

@Data
@NoArgsConstructor
public class PetStoreEmployee {
    private Long employeeId;
    private String employeeFirstName;
    private String employeeLastName;
    private String employeePhone;
    private String employeeJobTitle;

    public PetStoreEmployee(Employee employee) {
        this.employeeId = employee.getEmployeeId();
        this.employeeFirstName = employee.getEmployeeFirstName();
        this.employeeLastName = employee.getEmployeeLastName();
        this.employeePhone = employee.getEmployeePhone();
        this.employeeJobTitle = employee.getEmployeeJobTitle();
    }

 
    public PetStoreEmployee(Long employeeId, String employeeFirstName, String employeeLastName, String employeePhone, String employeeJobTitle) {
        this.employeeId = employeeId;
        this.employeeFirstName = employeeFirstName;
        this.employeeLastName = employeeLastName;
        this.employeePhone = employeePhone;
        this.employeeJobTitle = employeeJobTitle;
    }
    public Long getEmployeeId() {
        return this.employeeId;
    }

}