package tech.csm.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import tech.csm.dao.BillDao;
import tech.csm.dao.BillDaoImpl;
import tech.csm.domain.Bill;
import tech.csm.domain.BillProduct;
import tech.csm.domain.BillProductVO;
import tech.csm.domain.BillVO;
import tech.csm.domain.Product;

public class BillServiceImpl implements BillService {

	private BillDao billDao;
	public BillServiceImpl() {
		billDao=new BillDaoImpl();
	}
	
	@Override
	public String createBill(BillVO bvo, List<BillProductVO> bpvoList) {

		Bill bill = new Bill();
		bill.setCName(bvo.getCName());
		bill.setCPhone(bvo.getCPhone());
		try {
			bill.setBillDate(new SimpleDateFormat("yyyy-MM-dd").parse(bvo.getBillDate()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		bill.setTotalAmount(Double.parseDouble(bvo.getTotalAmount()));
		
		List<BillProduct> bpList=new ArrayList<>();
		for(BillProductVO x:bpvoList) {
			BillProduct bp=new BillProduct();
			bp.setNoOfUnits(Integer.parseInt(x.getNoOfUnits()));
			Product p=new Product();
			p.setName(x.getProductVo().getName());
			p.setProuctId(Integer.parseInt(x.getProductVo().getProuctId()));
			p.setQnty(Integer.parseInt(x.getProductVo().getQnty()));
			p.setUnitPrice(Double.parseDouble(x.getProductVo().getUnitPrice()));
			bp.setProduct(p);
			bpList.add(bp);
		}
		
		return billDao.createBill(bill,bpList);
	}

}
