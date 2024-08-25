package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreCustomer;
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

	@Autowired
	private PetStoreDao petStoreDao;
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private CustomerDao customerDao;;

	@Transactional(readOnly = false)
	public PetStoreData savePetStore(PetStoreData petStoreData) {
		Long petStoreId = petStoreData.getPetStoreId();
		PetStore petStore = findOrCreatePetStore(petStoreId);
		
		copyPetStoreFields(petStore, petStoreData);
		return new PetStoreData(petStoreDao.save(petStore));
	}

	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.setPetStoreName(petStoreData.getPetStoreName());
	}

	private PetStore findOrCreatePetStore(Long petStoreId) {
		PetStore petStore;
		
		if(Objects.isNull(petStoreId)) {
			petStore = new PetStore();
		} else {
			petStore = findPetStoreById(petStoreId);
		}
		return petStore;
	}

	private PetStore findPetStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId).orElseThrow();
	}

	@Transactional(readOnly = true)
	public List<PetStoreData> retrieveAllPetStores() {
		List<PetStore> petStores = petStoreDao.findAll();
		List<PetStoreData> response = new LinkedList<>();
		for (PetStore petStore : petStores) {
//			response.add(new PetStoreData(petStore));
			PetStoreData psd = new PetStoreData(petStore);
			psd.getCustomers().clear();
			psd.getEmployees().clear();
			response.add(psd);
		}
		return response;
	}

	@Transactional(readOnly = true)
	public PetStoreData retrievePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		return new PetStoreData(petStore);
	}

	@Transactional(readOnly = false)
	public void deletePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		petStoreDao.delete(petStore);
	}

	@Transactional(readOnly = false)
	public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
		PetStore petStore = findPetStoreById(petStoreId);
		Long employeeId = petStoreEmployee.getEmployeeId();
		Employee employee = findOrCreateEmployee(petStoreId, employeeId);
		copyEmployeeFields(employee, petStoreEmployee);
		employee.setPetStore(petStore);
		petStore.getEmployees().add(employee);
		Employee dbEmployee = employeeDao.save(employee);
		return new PetStoreEmployee(dbEmployee);
	}

	private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		employee.setFirstName(petStoreEmployee.getFirstName());
		employee.setLastName(petStoreEmployee.getLastName());
		employee.setTitle(petStoreEmployee.getTitle());
		employee.setPhone(petStoreEmployee.getPhone());
		
	}

	private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
		Employee employee;
		if (Objects.isNull(employeeId)) {
			employee = new Employee();
		} else {
			employee = findEmployeeById(petStoreId, employeeId);
		}
		return employee;
	}

	private Employee findEmployeeById(Long petStoreId, Long employeeId) {
		Employee employee = employeeDao.findById(employeeId).orElseThrow();
		if(employee.getPetStore().getPetStoreId() == petStoreId) {
			return employee;
		} else {
			throw new IllegalArgumentException("Employee ID is not valid");
		}
	}

	@Transactional(readOnly = false)
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		PetStore petStore = findPetStoreById(petStoreId);
		Long customerId = petStoreCustomer.getCustomerId();
		Customer customer = findOrCreateCustomer(petStoreId, customerId);
		copyCustomerFields(customer, petStoreCustomer);
		customer.setPetStore(petStore);
		petStore.getCustomers().add(customer);
		Customer dbCustomer = customerDao.save(customer);
		return new PetStoreCustomer(dbCustomer);
	}

	private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		customer.setFirstName(petStoreCustomer.getFirstName());
		customer.setLastName(petStoreCustomer.getLastName());
	}

	private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
		Customer customer;
		if (Objects.isNull(customerId)) {
			customer = new Customer();
		} else {
			customer = findCustomerById(petStoreId, customerId);
		}
		return customer;
	}

	private Customer findCustomerById(Long petStoreId, Long customerId) {
		Customer customer = customerDao.findById(customerId).orElseThrow();
		if(customer.getPetStore().getPetStoreId() == petStoreId) {
			return customer;
		} else {
			throw new IllegalArgumentException("Customer ID is not valid");
		}
	}
	
	
}
