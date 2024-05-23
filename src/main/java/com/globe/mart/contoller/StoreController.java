package com.globe.mart.contoller;

import com.globe.mart.beans.Store;
import com.globe.mart.exception.CustomException;
import com.globe.mart.service.StoreRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class StoreController {
	
	@Autowired
	private StoreRepoService srs;
	
	//get all stores
	@GetMapping("/stores")
	public ResponseEntity<Object> view1() throws CustomException {
		return srs.getPage(0);
	}

	//get with paging
	@GetMapping({"/stores/page/{page}","/stores/page"})
	public ResponseEntity<Object> view5(@PathVariable(required=false,name="page") String pageno) {
		String pn = StringUtils.isEmpty(pageno) ? "0" : pageno;
		return srs.getPage(Integer.parseInt(pn));
	}
	
	//get a store
	@GetMapping("/stores/{storeno}")
	public ResponseEntity<Object> view15(@PathVariable String storeno) throws CustomException {
		return srs.get(storeno);
	}
	
	//add
	@PostMapping("/stores")
	public ResponseEntity<Object> view2(@RequestBody Store s) throws CustomException {
		return srs.add(s);
	}
	
	//for naming convention
	//try for unique using spring programming 
	
	//update
	@PutMapping("/stores/{storeno}")
	public ResponseEntity<Object> view3(@RequestBody Store s,@PathVariable String storeno ) throws CustomException {
		return srs.update(s,storeno);
	}
	
	//update subset
	@PatchMapping(path = "/stores/{storeno}")
	public ResponseEntity<Object> view35(@RequestBody Map<Object ,Object > flds,@PathVariable String storeno ) throws CustomException {
		return srs.patch(flds,storeno);
	}
	
	@DeleteMapping("/stores/{storeno}")
	public ResponseEntity<Object> view4(@PathVariable String storeno ) throws CustomException {
		return srs.delete(storeno);
	}
	
	//other
	//combine paging with search also
	@GetMapping({"/stores/search/{col}/{qer}/{pg}","/stores/search/{col}/{qer}","/stores/search/{col}","/stores/search"})
	public ResponseEntity<Object> view6(@PathVariable(required = false) String col, @PathVariable(required = false) String qer, @PathVariable(required = false) String pg) {
		String rcol = StringUtils.isEmpty(col) ? "name" : col;
		String rqer = StringUtils.isEmpty(qer) ? "U*" : qer+"+";
		int page = Integer.parseInt(StringUtils.isEmpty(pg) ? "0" : pg);
		return srs.getData(rcol,rqer,page);
	}
	
	
}
