package tech.get_tt_right.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tech.get_tt_right.dao.EmployeeDao;
import tech.get_tt_right.dao.EmployeeDaoImpl;
import tech.get_tt_right.domain.Department;
import tech.get_tt_right.domain.DepartmentVo;
import tech.get_tt_right.domain.Employee;
import tech.get_tt_right.domain.EmployeeVo;

public class EmployeeServiceImpl implements EmployeeService {

//	Secondary Dependency
	private Employee employee;
	private EmployeeDao employeeDao;

	public EmployeeServiceImpl() {
		employee = new Employee();
		employeeDao = new EmployeeDaoImpl();

	}
	
	
	@Override
    public String addEmployee(EmployeeVo employeeVo) {
        // Convert EmployeeVo to Employee
        Employee employee = convertToDto(employeeVo);
//        System.out.println(employee);

        // Call the dao layer
        return employeeDao.addEmployee(employee); 
    }
	
	 @Override
	    public EmployeeVo getEmployeeById(int employeeId) {
	        Employee employee = employeeDao.getEmployeeById(employeeId);

	       // Convert Employee to EmployeeVo
	        EmployeeVo employeeVo= convertToVo(employee);

	        return employeeVo;
	    }
	 
	 
	 public static EmployeeVo convertToVo(Employee employeeDto) {
	        if (employeeDto == null) {
	            return null;
	        }

	        EmployeeVo employeeVo = new EmployeeVo();
	        employeeVo.setEmpId(String.valueOf(employeeDto.getEmpId()));
	        employeeVo.setName(employeeDto.getName());
	        employeeVo.setHire_date(employeeDto.getHire_date().toString());
	        employeeVo.setSalary(String.valueOf(employeeDto.getSalary()));
	        employeeVo.setIsDeleted(employeeDto.getIsDeleted());

	        DepartmentVo departmentVo = new DepartmentVo();
	        departmentVo.setDepartmentId(String.valueOf(employeeDto.getDepartment().getDepartmentId()));
	        departmentVo.setDepartmentName(employeeDto.getDepartment().getDepartmentName());
	        employeeVo.setDepartment(departmentVo);

	        return employeeVo;
	    }

	    public static Employee convertToDto(EmployeeVo employeeVo) {
	        if (employeeVo == null) {
	            return null;
	        }

	        Employee employeeDto = new Employee();
	        employeeDto.setEmpId(Integer.parseInt(employeeVo.getEmpId()));
	        employeeDto.setName(employeeVo.getName());
	        employeeDto.setHire_date(new Date(employeeVo.getHire_date())); // Assuming hire_date is a String in 'yyyy-MM-dd' format
	        employeeDto.setSalary(Double.parseDouble(employeeVo.getSalary()));
	        employeeDto.setIsDeleted(employeeVo.getIsDeleted());

	        Department departmentDto = new Department();
	        departmentDto.setDepartmentId(Integer.parseInt(employeeVo.getDepartment().getDepartmentId()));
	        departmentDto.setDepartmentName(employeeVo.getDepartment().getDepartmentName());
	        employeeDto.setDepartment(departmentDto);

	        return employeeDto;
	    }
	 

//    // Helper method for conversion
//    private Employee convertVoToEmployee(EmployeeVo employeeVo) {
//        Employee employee = new Employee();
//
//        employee.setName(employeeVo.getName());
//        employee.setHire_date(convertStringToDate(employeeVo.getHire_date())); // Assuming YYYY-MM-DD format
//        employee.setSalary(Double.parseDouble(employeeVo.getSalary()));
//        employee.setIsDeleted(employeeVo.getIsDeleted());
//
//        // Assuming your DepartmentVo and Department are designed appropriately for conversion
//        Department department = new Department();
//        department.setDepartmentId(Integer.parseInt(employeeVo.getDepartment().getDepartmentId()));
//        department.setDepartmentName(employeeVo.getDepartment().getDepartmentName());
//        employee.setDepartment(department);
//
//        return employee;
//    }
//
//    // Helper function to convert a date string to Date object 
//    private Date convertStringToDate(String dateString) {
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            return sdf.parse(dateString);
//        } catch (ParseException e) {
//            // Handle the date parsing exception appropriately
//            System.err.println("Error parsing date: " + e.getMessage());
//            return null; // Or some default value
//        }
//    }
//	@Override
//	public String addEmployee(EmployeeVo employeeVo) {
////		Convertions from Vo to Dto type
//		Employee employee = convertFromVoToDto(employeeVo);
//
////		Invoke Dao layer method to perform the persistance.
//		String msg = employeeDao.addEmployee(employee);
//
//		return msg;
//	}

//	public Employee convertFromVoToDto(EmployeeVo evo) {
//
////		Conversion from Vo to Dto
////		employee.setEmpId(Integer.parseInt(evo.getEmpId()));
//		employee.setName(evo.getName());
//		employee.setSalary(Double.parseDouble(evo.getSalary()));
//		employee.setEmpId(Integer.parseInt(evo.getEmpId()));
//
//		try {
//			employee.setHire_date(new SimpleDateFormat("dd-MM-yyyy").parse(evo.getHire_date()));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		employee.setIsDeleted(evo.getIsDeleted());
//
////		Convert DepartmentVo object into Department dto type
//
////		Create a DepartmentVo object in which to store data after completion of the convertions.
//		Department department1 = new Department();
//
//		department1.setDepartmentId(Integer.parseInt(evo.getDepartment().getDepartmentId()));
//		department1.setDepartmentName(evo.getDepartment().getDepartmentName());
//
//		employee.setDepartment(department1);
//
//		return employee;
//
//	}

//    private EmployeeVo convertEmployeeToVo(Employee employee) {
//        EmployeeVo employeeVo = new EmployeeVo();
//
//        employeeVo.setEmpId(Integer.toString(employee.getEmpId()));
//        employeeVo.setName(employee.getName());
//        employeeVo.setHire_date(employee.getHire_date().toString()); 
//                // Format the date to the desired format in EmployeeVo if needed
//        employeeVo.setSalary(String.valueOf(employee.getSalary()));
//        employeeVo.setIsDeleted(employee.getIsDeleted());
//  
//        // Convert Department object to DepartmentVo
//        DepartmentVo departmentVo = new DepartmentVo();
//        Department department = employee.getDepartment();
//        if (department != null ) {
//            departmentVo.setDepartmentId(String.valueOf(department.getDepartmentId()));
//            departmentVo.setDepartmentName(department.getDepartmentName());
//        }
//        employeeVo.setDepartment(departmentVo);   
//
//        return employeeVo;
//    } 
    
	// Convert Employee to EmployeeVo
//	private EmployeeVo convertToEmployeeVo(Employee employee) {
//		EmployeeVo employeeVo = new EmployeeVo();
//		employeeVo.setEmpId(String.valueOf(employee.getEmpId()));
//		employeeVo.setName(employee.getName());
//		employeeVo.setSalary(String.valueOf(employee.getSalary()));
//
//		// Format the Date as a String
//		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//		employeeVo.setHire_date(dateFormat.format(employee.getHire_date()));
//
//		// EmployeeVo has DepartmentVo type reference and I need to only set the id to
//		// DepartmentVo
//		// Set department ID to DepartmentVo
//		DepartmentVo departmentVo = new DepartmentVo();
//		departmentVo.setDepartmentId(String.valueOf(employee.getDepartment().getDepartmentId()));
//		employeeVo.setDepartment(departmentVo);
//
//		// Additional mappings if needed
//
//		return employeeVo;
//
//	}

//	@Override
//	public EmployeeVo getEmployeeById(int employeeId) {
//		// Call DAO method to retrieve Employee by ID
//		Employee employee = employeeDao.getEmployeeById(employeeId);
//		EmployeeVo employeeVo = null;
//
//		// Check if the employee is found
//		if (employee != null) {
//			// Convert Employee to EmployeeVo
//			employeeVo = convertToEmployeeVo(employee);
//		}
//		return employeeVo;
//	}

//	@Override
//	public List<EmployeeVo> getAllEmployees() {
//		List<Employee> employees = employeeDao.getAllEmployees();
//		List<EmployeeVo> employeeVos = new ArrayList<>();
//		// Invoke a function that converts from Dto to Vo type
//		for (Employee employee : employees) {
//			EmployeeVo employeeVo = convertToEmployeeVo(employee);
//			employeeVos.add(employeeVo);
//		}
//
//		// Return the list of Vo types to the caller.
//		return employeeVos;
//
//	}
	
	
	  @Override
	    public List<EmployeeVo> getAllEmployees() {
	        List<Employee> employeeDTOs = employeeDao.getAllEmployees();
	        List<EmployeeVo> employeeVOs = new ArrayList<>();

	        for (Employee employeeDTO : employeeDTOs) {
	            EmployeeVo employeeVO = new EmployeeVo();

	            employeeVO.setEmpId(employeeDTO.getEmpId().toString()); 
	            employeeVO.setName(employeeDTO.getName());

	            // Format hire_date to String
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Customize format if needed
	            employeeVO.setHire_date(dateFormat.format(employeeDTO.getHire_date()));

	            employeeVO.setSalary(employeeDTO.getSalary().toString());
	            employeeVO.setIsDeleted(employeeDTO.getIsDeleted());

	            // Assuming you have a method to convert Department DTO to Department VO:
	            employeeVO.setDepartment(convertDepartmentToVO(employeeDTO.getDepartment())); 

	            employeeVOs.add(employeeVO);
	        }
	        
	        employeeVOs.forEach(x->System.out.println(x));

	        return employeeVOs;
	    }

	    // Helper method for converting Department DTO to VO
	    private DepartmentVo convertDepartmentToVO(Department departmentDTO) { 
	        DepartmentVo departmentVo = new DepartmentVo();
	        departmentVo.setDepartmentId(departmentDTO.getDepartmentId().toString()); 
	        departmentVo.setDepartmentName(departmentDTO.getDepartmentName()); 
	        return departmentVo; 
	    }

//	@Override
//	public String updateEmployee(EmployeeVo updatedEmployee) {
//	    // Check if the employee with the given ID exists and is not soft-deleted
//	    Employee existingEmployee = employeeDao.getEmployeeById(Integer.parseInt(updatedEmployee.getEmpId()));
//	    if (existingEmployee != null && !"YES".equals(existingEmployee.getIsDeleted())) {
//	        // Employee with the given ID is not soft-deleted
//	        // Convert EmployeeVo to Employee
//
////	    	Converting id from inside this method
//	    	employee.setEmpId(Integer.parseInt(updatedEmployee.getEmpId()));
////	    	The rest other properties will be converted from the method: convertFromVoToDto
//	        Employee employeeToUpdate = convertFromVoToDto(updatedEmployee);
//
//	        // Invoke DAO method to update the employee
//	        String msg = employeeDao.updateEmployee(employeeToUpdate);
//
//	        return msg;
//	    } else {
//	        return "Failed to update employee. Employee not found or is soft-deleted.";
//	    }
//	}


	@Override
	public String deleteEmployee(int deleteEmployeeId) {
	    // Call DAO method to delete Employee by ID
	    String deleteMessage = employeeDao.deleteEmployee(deleteEmployeeId);

	    return deleteMessage;
	}
	@Override
	public String getReportBySalary(double salary) {
	    List<Employee> employees = employeeDao.getEmployeesBySalary(salary);

	    if (!employees.isEmpty()) {
	        StringBuilder report = new StringBuilder("Salary Report for Employees with Salary >= " + salary + ":\n\n");
	        for (Employee employee : employees) {
	            report.append("Employee ID: ").append(employee.getEmpId()).append(", ");
	            report.append("Name: ").append(employee.getName()).append(", ");
	            report.append("Salary: ").append(employee.getSalary()).append("\n");
	        }
	        return report.toString();
	    } else {
	        return "No employees found with salary >= " + salary;
	    }
	}

	@Override
	public String getReportByHireDate(String hireDate) {
	    try {
	        Date formattedDate = new SimpleDateFormat("dd/MM/yyyy").parse(hireDate);
	        List<Employee> employees = employeeDao.getEmployeesByHireDate(formattedDate);

	        if (!employees.isEmpty()) {
	            StringBuilder report = new StringBuilder("Hire Date Report for Employees hired on >= " + hireDate + ":\n\n");
	            for (Employee employee : employees) {
	                report.append("Employee ID: ").append(employee.getEmpId()).append(", ");
	                report.append("Name: ").append(employee.getName()).append(", ");
	                report.append("Hire Date: ").append(employee.getHire_date()).append("\n");
	            }
	            return report.toString();
	        } else {
	            return "No employees found hired on " + hireDate;
	        }
	    } catch (ParseException e) {
	        return "Invalid date format. Please use dd/MM/yyyy.";
	    }
	    
	}

	@Override
	public String getReportByDepartment(String departmentId) {
	    try {
	        int deptId = Integer.parseInt(departmentId);
	        List<Employee> employees = employeeDao.getEmployeesByDepartment(deptId);

	        if (!employees.isEmpty()) {
	            StringBuilder report = new StringBuilder("Department Report for Employees in Department ID " + departmentId + ":\n\n");
	            for (Employee employee : employees) {
	                report.append("Employee ID: ").append(employee.getEmpId()).append(", ");
	                report.append("Name: ").append(employee.getName()).append(", ");
	                report.append("Department ID: ").append(employee.getDepartment().getDepartmentId()).append("\n");
	            }
	            return report.toString();
	        } else {
	            return "No employees found in Department ID " + departmentId;
	        }
	    } catch (NumberFormatException e) {
	        return "Invalid department ID format. Please provide a valid integer.";
	    }
	}


	@Override
	public String updateEmployee(EmployeeVo updatedEmployee) {		
		return employeeDao.updateEmployee(convertToDto(updatedEmployee));
	}


//	@Override
//	public List<EmployeeVo> getAllEmployees() {
//		// TODO Auto-generated method stub
//		return null;
//	}




}
