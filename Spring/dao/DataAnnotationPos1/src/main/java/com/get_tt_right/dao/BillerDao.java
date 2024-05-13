package com.get_tt_right.dao;

import java.util.List;

import com.get_tt_right.domain.TransactionVo;
import com.get_tt_right.domain.Transactions;

public interface BillerDao {
	String createBill(List<Transactions> transactionsDto);

	List<Transactions> getBill();

}
