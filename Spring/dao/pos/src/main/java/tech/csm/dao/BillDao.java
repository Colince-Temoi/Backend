package tech.csm.dao;

import java.util.List;

import tech.csm.domain.Bill;
import tech.csm.domain.BillProduct;

public interface BillDao {

	String createBill(Bill bill, List<BillProduct> bpList);

}
