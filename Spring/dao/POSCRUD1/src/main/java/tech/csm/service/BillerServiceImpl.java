package tech.csm.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import tech.csm.dao.BillerDao;
import tech.csm.dao.BillerDaoImpl;
import tech.csm.domain.Biller;
import tech.csm.domain.BillerVo;
import tech.csm.domain.Product;
import tech.csm.domain.TransactionVo;
import tech.csm.domain.Transactions;

public class BillerServiceImpl implements BillerService {
	private BillerDao billerDao;

	public BillerServiceImpl() {
		billerDao = new BillerDaoImpl();
	}

	@Override
	public String createBill(BillerVo billerVo, List<TransactionVo> transactionVos) {

//		Convert the inputs into their respective Dto types
		Biller biller = convertBillerFromVOToDto(billerVo);
		List<Transactions> transactions = convertTransactionsFromVoToDto(transactionVos);
		
		/*
		 * System.out.println(biller); for (Transactions x : transactions) {
		 * System.out.println(x); }
		 */

//		Invoke dao layer method to persist our data
		String msg = billerDao.createBill(biller, transactions);

		return msg;
	}

	private Biller convertBillerFromVOToDto(BillerVo billerVo) {
//		Create stores to hold the respective dto type after conversion
		Biller biller = new Biller();
		
		try {
			biller.setBillDate(new SimpleDateFormat("yyyy-MM-dd").parse(billerVo.getBillDate()));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		biller.setBillerName(billerVo.getBillerName());
		
		biller.setPhoneNumber(billerVo.getPhoneNumber());
		
		biller.setTotalAmount(Double.parseDouble(billerVo.getTotalPurchaseAmount()));
		
//		Biller Id we are not setting: that will be auto-generated from the DB

		return biller;

	}

	private List<Transactions> convertTransactionsFromVoToDto(List<TransactionVo> transactionVos) {
//		Create stores to hold the respective dto type after conversion
		List<Transactions> transactions = new ArrayList<>();
		
		for (TransactionVo tx : transactionVos) {
//			Create a Transactions object to store our converted individual Tx dto data
			Transactions transaction = new Transactions();
			
			transaction.setNumberofUnits(Integer.parseInt(tx.getNumberofUnits()));
			
//			Create a store to hold all converted Product Vos
			Product product = new Product();
			product.setId(Integer.parseInt(tx.getProductVo().getId()));
			product.setName(tx.getProductVo().getName());
			product.setStock(Integer.parseInt(tx.getProductVo().getStock()));
			product.setUnitPrice(Double.parseDouble(tx.getProductVo().getUnitPrice()));
			
//			Now set the Product dto into Tx object
			transaction.setProduct(product);
			
			transaction.setTotalAmount(Double.parseDouble(tx.getTotalAmount()));
			
//			Two things we are not setting: Tx id and Biller object -we need id-: this we will do from the dao layer.
//			Tx id will be auto-generated
//			Biller Id will be auto-generated after insetion of Biller information and then we can get it and insert it into t_tx table
			
			transactions.add(transaction);
		}
		
		return transactions;

	}

}
