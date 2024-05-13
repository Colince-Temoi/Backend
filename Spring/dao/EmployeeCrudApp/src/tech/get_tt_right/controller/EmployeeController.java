package tech.get_tt_right.controller;

import java.util.List;
import java.util.Scanner;

import tech.get_tt_right.domain.DepartmentVo;
import tech.get_tt_right.domain.EmployeeVo;
import tech.get_tt_right.service.DepartmentService;
import tech.get_tt_right.service.DepartmentServiceImpl;
import tech.get_tt_right.service.EmployeeService;
import tech.get_tt_right.service.EmployeeServiceImpl;
import tech.get_tt_right.util.DbUtil;

public class EmployeeController {

//	Secondary dependencies
	private static Scanner scan;
	private static EmployeeVo employeeVo;
	private static DepartmentService departmentService;
	private static EmployeeService employeeService;

	public EmployeeController() {
		scan = new Scanner(System.in);
		employeeVo = new EmployeeVo();
		departmentService = new DepartmentServiceImpl();
		employeeService = new EmployeeServiceImpl();

	}

//	Report - Nothing but getting data and projecting it to the user.
//	Inputs: name, sal, hire_date,dept_name, etc
	/*
	 * When adding an employee, each one of them has department information. So
	 * while getting department information of a particular employee we have to
	 * first of all show to the user what are all the departments
	 * available-showAllDepartments functionlaity-Nothing but department information
	 * we need to populate from the DB end before starting any development. This
	 * funtionality we initially need before peforming any insertion operation.
	 * 
	 * When a user is performing selection of a single department then -getDeptById
	 * funtionality is required as well.
	 */
	public static void main(String[] args) {
		int choice;
		Character exitConfirmation = null;

		new EmployeeController();

		do {
			System.out.println("\n-----------Menu-------------" + "\n1.Add Employee." + "\n2.Search Employee by Id."
					+ "\n3.Show All Employees." + "\n4.Update Employee by Id." + "\n5.Delete an Employee by id"
					+ "\n6.Get Report By Salary" + "\n7.Get Report By Hire_date." + "\n8.Get report by Department"
					+ "\n9.Exit\n" + "\n Enter you choice: \n");

			choice = scan.nextInt();

			switch (choice) {
			case 1:
				List<DepartmentVo> departmentVos = departmentService.getAllDepartments();
//				departmentVos.forEach(x->System.out.println(x));
				DepartmentVo departmentVo = departmentService.getDepartmentById("10");
//				System.out.println(departmentVo);
				String message = employeeService.addEmployee(addEmployee());
				System.out.println(message);	
				break;

			case 2: {
				System.out.println("Enter Employee Id to search:");
				int employeeId = scan.nextInt();
				EmployeeVo foundEmployee = employeeService.getEmployeeById(employeeId);
				if (foundEmployee != null) {
					System.out.println("Employee details: \n" + foundEmployee.toString());
				} else {
					System.out.println("Employee with ID " + employeeId + " not found.");
				}
				break;
			}
			case 3: {
				List<EmployeeVo> allEmployees = employeeService.getAllEmployees();
				if (!allEmployees.isEmpty()) {
					System.out.println("All Employees: \n");
					for (EmployeeVo evo : allEmployees) {
//						System.out.println(evo.toString() + "\n");
					}
				} else {
					System.out.println("No employees found.");
				}
				break;
			}
			case 4: {
				System.out.println("Enter Employee Id to update:");
				Integer updateEmployeeId = scan.nextInt();

				// Check if the employee with the given ID exists and is not soft-deleted
				EmployeeVo existingEmployee = employeeService.getEmployeeById(updateEmployeeId);
//				System.out.println(existingEmployee);
				if (existingEmployee != null && !"YES".equals(existingEmployee.getIsDeleted())) {
					// Employee with the given ID is not soft-deleted
					EmployeeVo updatedEmployee = addEmployee(); // You can reuse the addEmployee() method for updates
					updatedEmployee.setEmpId(updateEmployeeId.toString()); // Set the employee ID to update the correct record
					String updateMessage = employeeService.updateEmployee(updatedEmployee);
					System.out.println(updateMessage);
				} else {
					System.out.println("Employee with ID " + updateEmployeeId + " not found or is soft-deleted.");
				}
				break;

			}
			case 5: {
				System.out.println("Enter Employee Id to delete:");
				int deleteEmployeeId = scan.nextInt();
				String deleteMessage = employeeService.deleteEmployee(deleteEmployeeId);
				System.out.println(deleteMessage);
				break;
			}
			case 6: {
				System.out.println("Enter salary to get report:");
				double salary = scan.nextDouble();

				// Call service method to get the report by salary
				String salaryReport = employeeService.getReportBySalary(salary);

				// Print the report
				System.out.println(salaryReport);
				break;
			}
			case 7: {
				System.out.println("Enter hire date to get report [dd/mm/yyyy]:");
				String hireDate = scan.next();

				// Call service method to get the report by hire date
				String hireDateReport = employeeService.getReportByHireDate(hireDate);

				// Print the report
				System.out.println(hireDateReport);
				break;
			}
			case 8: {
				System.out.println("Enter department ID to get report:");
			    String departmentId = scan.next();
			    
			    // Call service method to get the report by department
			    String departmentReport = employeeService.getReportByDepartment(departmentId);
			    
			    // Print the report
			    System.out.println(departmentReport);
			    break;
			}

			case 9:
				System.out.println("Are you sure you want to exit the application[y/n]");
				exitConfirmation = scan.next().charAt(0);

				if (exitConfirmation == 'y') {
					DbUtil.closeConnection();
					System.out.println("\nExiting the application. Goodbye!");
					break;
				} else {
					break;
				}

			default:
				System.out.println("\nInvalid choice. Please try again.");
				break;
			}

			/*
			 * For the while condotion below-truth table knowledge is required. In case of
			 * AND when left side evelautes to false right side will not be checked. In case
			 * of OR when left side evelautes to true right side will not be checked.
			 */
		} while (choice != 9 || exitConfirmation != 'y');// Loop until the user chooses to exit
	}
	
	
	// Helper method for user input
	public static EmployeeVo  addEmployee() {
        Scanner scanner = new Scanner(System.in);
        EmployeeVo employeeVo = new EmployeeVo();

        System.out.println("Enter employee name:");
        employeeVo.setName(scanner.nextLine()); 

        System.out.println("Enter employee hire date (YYYY-MM-DD):");
        employeeVo.setHire_date(scanner.nextLine());

        System.out.println("Enter employee salary:");
        employeeVo.setSalary(scanner.nextLine());

        employeeVo.setIsDeleted("NO"); // Hardcode as per instructions

        // Display departments and get user selection
        List<DepartmentVo> departments = departmentService.getAllDepartments();
        System.out.println("Available Departments:");
        for (int i = 0; i < departments.size(); i++) {
            System.out.println((i + 1) + ". " + departments.get(i).toString());
        }

        System.out.println("Select department number:");
        int departmentIndex = scanner.nextInt() - 1; // Adjust for zero-based indexing
        scanner.nextLine(); // Consume newline from previous input

        // Ensure valid department selection
        if (departmentIndex < 0 || departmentIndex >= departments.size()) {
            System.out.println("Invalid department selection.");
            return null; // Or provide a way to retry
        }

        DepartmentVo selectedDepartment = departments.get(departmentIndex);
        employeeVo.setDepartment(selectedDepartment);
        
//        System.out.println(employeeVo);

        return employeeVo;
    }
	

////	Behavior to receive input
//	public static EmployeeVo addEmployee() {
//
//		System.out.println("\n-------Insert Records into the t_emp table-----\n");
//		System.out.println("Enter Employee name");
//		employeeVo.setName(scan.next());
//		System.out.println("Enter Employee Hire date [dd/mm/yyyy]");
//		employeeVo.setHire_date(scan.next());
//		System.out.println("Enter Employee Salary");
//		employeeVo.setSalary(scan.next());
//
//		employeeVo.setIsDeleted("NO");
//		System.out.println("Available departements");
//
////		Displaying all Departments information to the user to pick one while inputing a new employees details.
//		List<DepartmentVo> departmentVos = departmentService.getAllDepartments();
//		if (departmentVos != null) {
//			System.out.println(departmentVos.toString());
//		} else {
//			System.out.println("There are no departments to display at the momemnt!");
//		}
//
////	We need to get the Specific department based on the input we have recieved from the user.
//		System.out.println("Enter Employee department id.");
//		DepartmentVo departmentVo = departmentService.getDepartmentById(scan.next());
////		System.out.println(departmentVo.toString());
//		employeeVo.setDepartment(departmentVo);
//
//		return employeeVo;
//	}

}
