package pet.store.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.Customer;
import pet.store.entity.PetStore;

@Data
@NoArgsConstructor
public class PetStoreCustomer {
	private Long customerId;
	private String firstName;
	private String lastName;
	private CustomerPetStore petStore;
	
	public PetStoreCustomer(Customer customer) {
		customerId = customer.getCustomerId();
		firstName = customer.getFirstName();
		lastName = customer.getLastName();
		petStore = new CustomerPetStore(customer.getPetStore());
	}
	
	@Data
	@NoArgsConstructor
	public static class CustomerPetStore {
		private Long petStoreId;
		private String petStoreName;
		
		public CustomerPetStore(PetStore petStore) {
			petStoreId = petStore.getPetStoreId();
			petStoreName = petStore.getPetStoreName();
		}
	}
}
