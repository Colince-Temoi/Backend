// CityStateStoredProcedure.java
package tech.csm.controller;

import tech.csm.service.CityService;
import tech.csm.service.CityServiceImpl;
import tech.csm.service.StateService;
import tech.csm.service.StateServiceImpl;
import tech.csm.entity.CityVO;
import tech.csm.entity.State;
import tech.csm.entity.StateVO;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CityStateStoredProcedure {

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        CityService cityService = new CityServiceImpl();
        StateService stateService = new StateServiceImpl();
        

        while (true) {
            System.out.println("1. Add City\n2. Show All Cities in a given state\n3. Delete City By ID\n4. Update City by ID\n5. EXIT");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    List<StateVO> states = stateService.getAllStates();
                    for (StateVO state : states) {
                        System.out.println(state.getStateId() + " - " + state.getStateName());
                    }
                    System.out.println("Enter state id:");
                    int stateId = scanner.nextInt();
                    StateVO state = stateService.getStateById(stateId);
                    if (state != null) {
                        System.out.println("Enter city name:");
                        String cityName = scanner.next();
                        CityVO cityVO = new CityVO();
                        cityVO.setCityName(cityName);
                        cityVO.setStateId(String.valueOf(stateId));
                        cityService.addCity(cityVO);
                    } else {
                        System.out.println("State not found");
                    }
                    break;
                case 2:
                    List<StateVO> statesForSearch = stateService.getAllStates();
                    for (StateVO state1 : statesForSearch) {
                        System.out.println(state1.getStateId() + " - " + state1.getStateName());
                    }
                    System.out.println("Enter state id:");
                    int stateIdForSearch = scanner.nextInt();
                    List<CityVO> cities = cityService.getAllCitiesByStateId(stateIdForSearch);
                    if (!cities.isEmpty()) {
                        for (CityVO city : cities) {
                            System.out.println(city.getCityId() + " - " + city.getCityName());
                        }
                    } else {
                        System.out.println("No cities found in the given state");
                    }
                    break;
                case 3:
                    System.out.println("Enter city id to delete:");
                    int cityIdToDelete = scanner.nextInt();
                    cityService.deleteCity(cityIdToDelete);
                    break;
                case 4:
                    System.out.println("Enter city id to update:");
                    int cityIdToUpdate = scanner.nextInt();
                    CityVO cityToUpdate = cityService.getCityById(cityIdToUpdate);
                    if (cityToUpdate != null) {
                        System.out.println("Enter new city name:");
                        String newCityName = scanner.next();
                        cityToUpdate.setCityName(newCityName);
                        cityService.updateCity(cityToUpdate);
                    } else {
                        System.out.println("City not found");
                    }
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}