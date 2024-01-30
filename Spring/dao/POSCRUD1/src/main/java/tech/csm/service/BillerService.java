package tech.csm.service;

import java.util.List;

import tech.csm.domain.BillerVo;
import tech.csm.domain.TransactionVo;

public interface BillerService {

	String createBill(BillerVo billerVo, List<TransactionVo> transactionVos);

}
