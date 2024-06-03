package tech.csm.runner;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;

import tech.csm.entity.Block;
import tech.csm.entity.Panchayart;
import tech.csm.entity.Village;
import tech.csm.service.BlockServiceImpl;
import tech.csm.service.BlockkService;
import tech.csm.service.PanchayartService;
import tech.csm.service.PanchayartServiceImpl;
import tech.csm.service.VillageService;
import tech.csm.service.VillageServiceImpl;
import tech.csm.util.DBUtil;

public class Runner {
	// get all category service
	private static BlockkService blockkService = new BlockServiceImpl();
	private static PanchayartService panchayartService = new PanchayartServiceImpl();
	private static VillageService villageService = new VillageServiceImpl();

	public static void main(String[] args) {
		Session ses = DBUtil.getSessionFactory().openSession();

		System.out.println(ses);

		Scanner sc = new Scanner(System.in);
		Scanner ss = new Scanner(System.in);
		int choice = 0;
		do {
			System.out.println("----------Menu------------\n1.Add Village\n2.Show all Villages\n3.Exit");
			System.out.println("Enter choice");
			choice = sc.nextInt();
			switch (choice) {
			case 1:
				System.out.println("enter the Village name:");
				String villageName = ss.nextLine();
				System.out.println("Enter the number of people in the Village");
				Integer numberOfPeople = sc.nextInt();
				System.out.println("Enter the block id" + blockkService.getAllBlocks());
				Integer blockId = sc.nextInt();
				System.out.println("Enter panchayart id:" + panchayartService.getPanchayartByBlockId(blockId));
				Integer panchayartId = sc.nextInt();

				Village village = new Village();

				Block b = new Block();
				b.setBlockId(blockId);
				village.setBlock(b);

				village.setVillageName(villageName);
				village.setNoOfPeople(numberOfPeople);

				Panchayart p = new Panchayart();
				p.setPanchayartId(panchayartId);
				village.setPanchayart(p);

				String addVillage = villageService.addVillage(village);
				System.out.println("\n" + addVillage);

				break;

			case 2:
				// get all purchases
				List<Village> villages = villageService.getAllVillages();
				if (villages.size()!=0) {
					System.out.println("\n------List of all villages--------\n");
					for (Village village2 : villages) {
						System.out.println(village2);
					}
				} else {
					System.out.println("\nNo villages to display at the momemnt! choose option to add villages first");

				}
				break;

			case 3:
				System.out.println("\nExiting application, goodbye!!");
				break;

			default:
				System.out.println("Enter a deafult value!");
				break;
			}

		} while (choice != 3);
	}

}
