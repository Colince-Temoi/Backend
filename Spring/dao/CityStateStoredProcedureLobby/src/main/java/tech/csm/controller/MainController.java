package tech.csm.controller;


import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import tech.csm.entity.City;
import tech.csm.entity.CityVo;
import tech.csm.entity.StateVo;
import tech.csm.service.CityService;
import tech.csm.service.CityServiceImpl;
import tech.csm.service.StateService;
import tech.csm.service.StateServiceImpl;
import tech.csm.util.DBUtil;

public class MainController {

	private static Scanner scan;
	
	private static StateService stateService;
	private static CityService cityService;

	public MainController() {
		scan = new Scanner(System.in);
		
		stateService = new StateServiceImpl();
		cityService = new CityServiceImpl();

	}

	public static void main(String[] args) throws SQLException {

//		Create object of controller to initialize things up
		new MainController();
		
		/*
		 * List<StateVo> stateVoList = stateService.getAllStates();
		 * 
		 * StateVo stateVo = stateService.getStateById();
		 */
		
		/*
		 * System.out.println(stateVo);
		 * 
		 * System.out.println();
		 * 
		 * for (StateVo x : stateVoList) { System.out.println(x); }
		 */

		int choice;
		char ck = 'n';
		do {
			System.out.println("\n1.Add City Inf\n2.Show All Cities) \n" + "3.Delete City By City Id\n"
					+ "4.Update City By City Id\n" + "5.Exit\n" + "Enter your Choice : ");
			choice = scan.nextInt();
			switch (choice) {
			case 1:
//				System.out.println("Add City Inf");
				System.out.println("Coonection object: "+DBUtil.getConnection());
				
//				Create an object to store city data
				CityVo cityVo = new CityVo();
				
				
//				Pick the required inputs from the user while setting them up to the just created City object store
				System.out.println("Enter City name");
				cityVo.setCityName(scan.next());
				
				
				System.out.println("Enter state id: "+stateService.getAllStates());
				String id = scan.next();
				cityVo.setCityId(id);
				
				cityVo.setStateVo(stateService.getStateById(Integer.parseInt(id)));
				
//				Now invoke service layer method to save this City Object
				String msg = cityService.createCity(cityVo);
				
				System.out.println(msg);
				
				
				
				break;
			case 2:
//				System.out.println("Show All Cities in a given State");
				List<CityVo> cityVoList = cityService.getAllCities();
				
				for (CityVo x : cityVoList) {
					System.out.println(x);
				}
				
				break;

			case 3:
				System.out.println("Delete City By City Id");
				break;
			case 4:
				System.out.println("Update City By City Id");
				break;
			case 5:
				System.out.println("Are you sure [y/n]:");
				ck = scan.next().charAt(0);
				if (ck == 'y')
					DBUtil.closeConnection();
				break;
			default:
				System.out.println("invalid option!!");
			}

		} while (choice != 3 || (choice == 3 && ck != 'y'));

	}

}
