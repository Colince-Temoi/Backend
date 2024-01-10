package tech.get_tt_right.controller;

import java.util.List;
import java.util.Scanner;

import tech.get_tt_right.domain.DepartmentVo;
import tech.get_tt_right.domain.EmployeeVo;
import tech.get_tt_right.service.DepartmentService;
import tech.get_tt_right.service.DepartmentServiceImpl;
import tech.get_tt_right.service.EmployeeService;
import tech.get_tt_right.service.EmployeeServiceImpl;

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

			}
			case 3: {

				break;
			}
			case 4: {

				break;
			}
			case 5: {

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

				System.out.println("\nExiting the application. Goodbye!");
				break;

			default:
				System.out.println("\nInvalid choice. Please try again.");
				break;
			}
		} while (choice != 9);// Loop until the user chooses to exit
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
