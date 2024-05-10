package tech.csm.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import tech.csm.domain.BillProductVO;
import tech.csm.domain.BillVO;
import tech.csm.domain.ProductVO;
import tech.csm.service.BillService;
import tech.csm.service.BillServiceImpl;
import tech.csm.service.ProductService;
import tech.csm.service.ProductServiceImpl;
import tech.csm.util.DBUtil;

public class POSController {

	private static ProductService productService = new ProductServiceImpl();
	private static BillService billService = new BillServiceImpl();

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		Scanner ss = new Scanner(System.in);

		int choice;
		char ck = 'n';
		do {
			System.out.println("\n1.Create Bill\n2.Show Bill\n" + "3.Exit\n" + "Enter your Choice : ");
			choice = sc.nextInt();
			switch (choice) {
			case 1:
				BillVO bvo = new BillVO();
				List<BillProductVO> bpvoList = new ArrayList<>();
				System.out.println("Enter customer name");
				bvo.setCName(ss.nextLine().trim());
				System.out.println("Enter customer phone no");
				bvo.setCPhone(ss.nextLine().trim());
				bvo.setBillDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				char ch = 'y';
				Double totalPrice = 0.0;
				do {
					BillProductVO bpvo = new BillProductVO();
					
					System.out.println("Enter product Id:" + productService.getAllProducts());
					int pid = sc.nextInt();
					ProductVO pvo = productService.getProductById(pid);
					bpvo.setProductVo(pvo);
					System.out.println("Enter the no of units:");
					int nop = sc.nextInt();
					if (nop <= Integer.parseInt(pvo.getQnty())) {
						bpvo.setNoOfUnits(String.valueOf(nop));
						bpvo.setProductVo(pvo);
						totalPrice += Double.parseDouble(pvo.getUnitPrice()) * nop;
						bpvoList.add(bpvo);
					} else
						System.out.println("Insufficient product qnty, as we have only "
								+ productService.getProductById(pid).getQnty() + " units");
					System.out.println("Want to add more products[y/n]:");
					ch = ss.nextLine().charAt(0);
				} while (ch == 'y');
				bvo.setTotalAmount(totalPrice.toString());
				String msg = billService.createBill(bvo, bpvoList);
				
				for (BillProductVO x : bpvoList) {
					System.out.println(x);
				}
				

				

				break;
			case 2:

				break;

			case 3:

				System.out.println("Are you sure [y/n]:");
				ck = ss.nextLine().charAt(0);
				if (ck == 'y')
					DBUtil.closeConnection();
				break;
			default:
				System.out.println("invalid option!!");
			}

		} while (choice != 3 || (choice == 3 && ck != 'y'));

	}

}
