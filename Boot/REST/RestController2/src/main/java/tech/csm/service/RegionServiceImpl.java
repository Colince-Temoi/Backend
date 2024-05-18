package tech.csm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.csm.aspects.EmailService;
import tech.csm.aspects.JmsService;
import tech.csm.dao.RegionDao;
import tech.csm.domain.Region;

@Service
public class RegionServiceImpl implements RegionService {

	 @Autowired
	    private RegionDao regionDao;
	 
	 @Autowired
	    private EmailService emailService;

	 @Autowired
	    private JmsService jmsService;

	    @Override
	    public Region getRegionById(Integer regionId) {
	        return regionDao.getRegionById(regionId);
	    }

	    @Override
	    public Integer saveAddress(Region region) {
	    	System.out.println("Exceuting Method");
	    	Integer regionId = regionDao.saveAddress(region);
	    	// Simulate email and JMS operations
	        emailService.sendEmail("example@example.com", "New Region Saved", "Region " + region.getRegionName() + " has been saved.");
	        jmsService.sendMessage("regionQueue", "Region " + region.getRegionName() + " has been saved.");
	        return regionId;
	    }

}
