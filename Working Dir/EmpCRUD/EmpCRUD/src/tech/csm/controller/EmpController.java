package tech.csm.controller;

import java.util.Scanner;

import tech.csm.domain.EmpVO;
import tech.csm.service.EmpService;
import tech.csm.service.EmpServiceImpl;

public class EmpController {
	private static EmpService empService=new EmpServiceImpl();
	
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Scanner ss = new Scanner(System.in);
		
		
		
		int choice;
		do {
			System.out.println("\n1.Add Emp\n2.Delete Emp\n3.Update Emp\n4.Show All Emp\n5.Exit");
			System.out.println("Enter the cooice");
			choice = sc.nextInt();
			switch (choice) {
			case 1:String msg=empService.addEmp(addEmp());
					System.out.println(msg);
				break;
			case 2:System.out.println("Enter the id:");
				Integer id=sc.nextInt();
				EmpVO empVO=empService.deleteEmpById(id);
				if(empVO!=null)
					System.out.println("employee with id "+empVO.getEmpId()+" is deleted ");
				else
					System.out.println("emp not present.check id again");
				break;
			case 3:
				break;
			case 4:
				empService.showAllEmps();
				
				break;
			case 5:
				break;
			default:
				System.out.println("invalid option!!");

			}
		} while (choice != 5);

	}
	
	public static EmpVO addEmp() {
		Scanner ss = new Scanner(System.in);
		EmpVO empVO=new EmpVO();
		System.out.println("Enter empId, name,salary:");
		empVO.setEmpId(ss.nextLine());
		empVO.setName(ss.nextLine());
		empVO.setSal(ss.nextLine());
		return empVO;	
		
	}

}
