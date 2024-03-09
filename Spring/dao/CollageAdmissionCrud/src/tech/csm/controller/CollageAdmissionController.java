package tech.csm.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import tech.csm.domain.AdmissionDetailsVo;
import tech.csm.domain.CollageVo;
import tech.csm.service.AdmissionDetailsService;
import tech.csm.service.AdmissionDetailsServiceImpl;
import tech.csm.service.CollageService;
import tech.csm.service.CollageServiceImpl;
import tech.csm.util.DbUtil;

public class CollageAdmissionController {

//	primitive dependencies

//	Secondary dependencies
	private static Scanner scanner;
	private static CollageService collageService;
	private static AdmissionDetailsService admissionDetailsService;

	public CollageAdmissionController() {
		scanner = new Scanner(System.in);
		collageService = new CollageServiceImpl();
		admissionDetailsService = new AdmissionDetailsServiceImpl();
	}

	public static void main(String[] args) {

//	Invoking CollageAdmissiinController constructor.	
		new CollageAdmissionController();

		Integer choice = null;
		Character exitConfirmation = 'n';

		do {

			System.out.println("\n-----------Menu-------------" + "\n1.Make Admission." + "\n2.Cancel Admission by Id."
					+ "\n3.Modify Admission By id." + "\n4.Admission Report." + "\n5.Exit" + "\n\n Enter your choice: \n");

			choice = scanner.nextInt();

			switch (choice) {
			case 1: {

				AdmissionDetailsVo admissionDetailsVo = admissionDetailsInput();

//				Check is slots available to make an admission, else return a suitable message to the user
				if (Integer.parseInt(admissionDetailsVo.getCollageVo().getNoOfSeats()) > 0) {
//					Admit a Candidate
					String message = admissionDetailsService.saveCandidateAdmissionDetails(admissionDetailsVo);
//					System.out.println("\nAdmission sucessful in " + admissionDetailsVo.getCollageVo().getCollageName()+ " College!");
					System.out.println(message+" " + admissionDetailsVo.getCollageVo().getCollageName()+ " is the College!");

				} else {
//					Return a suitable message
					System.out.println("\nAdmission uncessful in " + admissionDetailsVo.getCollageVo().getCollageName()
							+ " College! Try another college");

				}

				break;
			}

			case 2: {
			    System.out.println("\nEnter Admission ID to cancel: ");
			    int admissionId = scanner.nextInt();
			    String cancellationMessage = admissionDetailsService.cancelAdmissionById(admissionId);
			    System.out.println(cancellationMessage);
			    break;
			}

			case 3: {
			    System.out.println("\nEnter Admission ID to modify: ");
			    int admissionId = scanner.nextInt();
			    AdmissionDetailsVo modifiedAdmissionDetails = admissionDetailsInput();
			    String modificationMessage = admissionDetailsService.modifyAdmissionById(admissionId, modifiedAdmissionDetails);
			    System.out.println(modificationMessage);
			    break;
			}

			case 4: {
			    System.out.println("\n-----Admission Report-----");
			    List<AdmissionDetailsVo> admissionReport = admissionDetailsService.getAdmissionReport();
			    if (admissionReport.isEmpty()) {
			        System.out.println("No admissions found.");
			    } else {
			        for (AdmissionDetailsVo admission : admissionReport) {
			            System.out.println(admission);
			        }
			    }
			    break;
			}


			case 5: {
				System.out.println("Are you sure you want to exit the application[y/n]");
				exitConfirmation = scanner.next().charAt(0);
				if (exitConfirmation == 'y') {
					DbUtil.closeConnection();
					System.out.println("\nExiting Application. Good Bye!");
					break;
				} else {
					break;
				}
			}
			default:
				System.out.println("\nInvalid choice. Please try again.");
				break;
			}

			/*
			 * For the while condition below-truth table knowledge is required. In case of
			 * AND when left side evaluates to false right side will not be checked. In case
			 * of OR when left side evaluates to true right side will not be checked. For
			 * AND both LHS and RHS must evaluate to true to make the condition. For OR if
			 * at least one side evaluates to true, that makes the condition true.
			 */
		} while (choice != 5 || exitConfirmation != 'y');// Loop until the user chooses to exit
	}

//	behavior to get Admission details; candidate name, phone,address,college id, admission date
	private static AdmissionDetailsVo admissionDetailsInput() {
//		Creating an object to store||hold input
		AdmissionDetailsVo admissionDetailsVo = new AdmissionDetailsVo();

		System.out.println("--------Input admission details------------\n");
		System.out.println("Enter Candidate Name: \n");
		admissionDetailsVo.setCandidateName(scanner.next());
		System.out.println("\nEnter Candidate Phone: \n");
		admissionDetailsVo.setCandidatePhone(scanner.next());
		System.out.println("\nEnter Candidate Address: \n");
		admissionDetailsVo.setCandidateAddress(scanner.next());
//		System.out.println("Enter Admission Date [dd/MM/yyyy]: \n");
		admissionDetailsVo.setAdmission_date(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

		System.out.println("\n-----List of Collages Available-----\n");
		List<CollageVo> collageVo = collageService.getAllCollages();

		for (CollageVo collageVo2 : collageVo) {
			System.out.println(collageVo2);
		}

//		We need to get the Specific department based on the input we have recieved from the user.
		System.out.println("\nEnter Collage id: \n");
		CollageVo vo = collageService.getCollageById(Integer.parseInt(scanner.next()));
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

//		System.out.println(vo);

		admissionDetailsVo.setCollageVo(vo);

		return admissionDetailsVo;

	}
}
