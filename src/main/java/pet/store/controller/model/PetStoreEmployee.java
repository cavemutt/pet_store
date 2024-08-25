package pet.store.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Data
@NoArgsConstructor
public class PetStoreEmployee {
	private Long employeeId;
	private String firstName;
	private String lastName;
	private String title;
	private String phone;
	private EmployeePetStore petStore;

	public PetStoreEmployee(Employee employee) {
		employeeId = employee.getEmployeeId();
		firstName = employee.getFirstName();
		lastName = employee.getLastName();
		title = employee.getTitle();
		phone = employee.getPhone();
		petStore = new EmployeePetStore(employee.getPetStore());
		
	}
	
	@Data
	@NoArgsConstructor
	public static class EmployeePetStore {
		private Long petStoreId;
		private String petStoreName;
		
		public EmployeePetStore(PetStore petStore) {
			petStoreId = petStore.getPetStoreId();
			petStoreName = petStore.getPetStoreName();
		}
	}
}
