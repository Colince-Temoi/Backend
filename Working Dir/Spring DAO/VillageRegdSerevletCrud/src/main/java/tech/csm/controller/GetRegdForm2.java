package tech.csm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import tech.csm.entity.Block;
import tech.csm.entity.Village;
import tech.csm.service.BlockService;
import tech.csm.service.BlockServiceImpl;
import tech.csm.service.VillageService;
import tech.csm.service.VillageServiceImpl;

public class GetRegdForm2 extends HttpServlet {
	private BlockService blockService;
	private VillageService villageService;
	
	public GetRegdForm2() {
		blockService=new BlockServiceImpl();
		villageService=new VillageServiceImpl();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				
		List<Block> blockList=blockService.getAllBlocks();
		
		
		req.setAttribute("blockList", blockList);
		
		List<Village> villageList=villageService.getAllVillages();
		req.setAttribute("villageList", villageList);
		
		RequestDispatcher rd = req.getRequestDispatcher("villageRegdForm2.jsp");
		rd.forward(req, resp);
	}
}
