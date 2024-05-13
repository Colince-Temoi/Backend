package com.get_tt_right.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.get_tt_right.dao.BillerDao;
import com.get_tt_right.domain.BillerVo;
import com.get_tt_right.domain.ProductVo;
import com.get_tt_right.domain.TransactionVo;
import com.get_tt_right.domain.Transactions;

@Service
public class BillerServiceImpl implements BillerService {
	@Autowired
	private BillerDao billerDao;
	
    private ModelMapper modelMapper = new ModelMapper(); // Could be injected via Spring

    
	@Override
	public String createBill(List<TransactionVo> transactions) {

		System.out.println();
		List<Transactions> transactionsDto = transactions.stream()
                .map(transactionVo -> modelMapper.map(transactionVo, Transactions.class))
                .collect(Collectors.toList());

		String msg = billerDao.createBill(transactionsDto);
		
		return "Working!";
	}

	
	@Override
	public List<TransactionVo> showBill() {
	    List<Transactions> transactionsDto = billerDao.getBill();
	    
	    // Convert TransactionDto to TransactionVo using ModelMapper
	    List<TransactionVo> transactionsVo = transactionsDto.stream()
	            .map(transactionDto -> {
	                TransactionVo transactionVo = modelMapper.map(transactionDto, TransactionVo.class);
	                
	                // Manual conversion for nested objects BillerVo and ProductVo
	                BillerVo billerVo = modelMapper.map(transactionDto.getBillerVo(), BillerVo.class);
	                ProductVo productVo = modelMapper.map(transactionDto.getProduct(), ProductVo.class);
	                
	                transactionVo.setBillerVo(billerVo);
	                transactionVo.setProductVo(productVo);
	                
	                return transactionVo;
	            })
	            .collect(Collectors.toList());
	    
//	    transactionsVo.forEach(x->System.out.println(x));
	    
	    return transactionsVo;
	}
//	@Override
//	public List<TransactionVo> showBill() {
//		List<Transactions> transactionsDto = billerDao.getBill();
//		
//		
//		
//		return null;
//	}

}
