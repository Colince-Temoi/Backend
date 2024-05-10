package tech.csm.service;

import java.util.List;

import tech.csm.domain.BillProductVO;
import tech.csm.domain.BillVO;

public interface BillService {

	String createBill(BillVO bvo, List<BillProductVO> bpvoList);

}
