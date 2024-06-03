package tech.csm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.csm.model.Customer;
import tech.csm.repo.CustomerRepo;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerRepo customerRepo;
	
	@Override
	public List<Customer> getAllCustomers() {
		return customerRepo.findAll();
	}

}
