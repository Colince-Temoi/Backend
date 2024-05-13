package com.get_tt_right.service;

import java.util.List;

import com.get_tt_right.domain.TransactionVo;

public interface BillerService {

	String createBill(List<TransactionVo> transactions);

	List<TransactionVo> showBill();

}
