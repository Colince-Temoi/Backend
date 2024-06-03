package tech.csm.controller;

import java.io.IOException;
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

public class FilterBlock extends HttpServlet {

	private VillageService villageService;
	private BlockService blockService;

	public FilterBlock() {
		villageService = new VillageServiceImpl();
		blockService = new BlockServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer bId = Integer.parseInt(req.getParameter("blockfId"));
		List<Village> villageList = villageService.getVillageByBlockId(bId);
		req.setAttribute("villageList", villageList);

		List<Block> blockList = blockService.getAllBlocks();
		req.setAttribute("blockList", blockList);
		RequestDispatcher rd = req.getRequestDispatcher("villageRegdForm.jsp");
		rd.forward(req, resp);
	}

}
