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
						System.out.println(evo.toString() + "\n");
					}
				} else {
					System.out.println("No employees found.");
				}
				break;
			}
			case 4: {

				System.out.println("Enter Employee Id to update:");
				Integer updateEmployeeId = scan.nextInt();
				EmployeeVo updatedEmployee = addEmployee(); // You canreuse the addEmployee() method for updates
				updatedEmployee.setEmpId(updateEmployeeId.toString()); // Set the employee ID to update the correct record
				String updateMessage = employeeService.updateEmployee(updatedEmployee);
				System.out.println(updateMessage);
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

				break;
			}
			case 7: {

				break;
			}
			case 8: {

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

//	Behavior to receive input
	public static EmployeeVo addEmployee() {

		System.out.println("\n-------Insert Records into the t_emp table-----\n");
		System.out.println("Enter Employee name");
		employeeVo.setName(scan.next());
		System.out.println("Enter Employee Hire date [dd/mm/yyyy]");
		employeeVo.setHire_date(scan.next());
		System.out.println("Enter Employee Salary");
		employeeVo.setSalary(scan.next());

		employeeVo.setIsDeleted("NO");
		System.out.println("Available departements");

//		Displaying all Departments information to the user to pick one while inputing a new employees details.
		List<DepartmentVo> departmentVos = departmentService.getAllDepartments();
		if (departmentVos != null) {
			System.out.println(departmentVos.toString());
		} else {
			System.out.println("There are no departments to display at the momemnt!");
		}

//	We need to get the Specific department based on the input we have recieved from the user.
		System.out.println("Enter Employee department id.");
		DepartmentVo departmentVo = departmentService.getDepartmentById(scan.next());
//		System.out.println(departmentVo.toString());
		employeeVo.setDepartment(departmentVo);

		return employeeVo;
	}

}
