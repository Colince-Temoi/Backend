package tech.csm.dao;

import java.util.List;

import tech.csm.domain.Biller;
import tech.csm.domain.Transactions;

public interface BillerDao {

	String createBill(Biller biller, List<Transactions> transactions);

}
