
package pet.store.controller.model;

public class PetStoreCustomerDTO {
	private String customerFirstName;
    private String customerLastName;
    private String customerEmail;


    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    public String getCustomerFirstName() {
    	return customerFirstName;
    }
    
    public void setCustomerFirstName(String customerFirstName) {
    	this.customerFirstName = customerFirstName;
    }
}
