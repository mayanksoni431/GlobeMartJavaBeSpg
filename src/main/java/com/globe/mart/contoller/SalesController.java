package com.globe.mart.contoller;

import com.globe.mart.beans.Sales;
import com.globe.mart.exception.CustomException;
import com.globe.mart.service.SalesRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;


@RestController
public class SalesController {
		@Autowired
		private SalesRepoService srs;

		//get with paging
		@GetMapping({"/sales/page/{pn}","/sales/page"})
		public ResponseEntity<Object> view5(@PathVariable(required=false,name="pn") String pageno) {
			String pn = StringUtils.isEmpty(pageno) ? "0" : pageno;
			return srs.getPage(Integer.parseInt(pn));
		}

		@GetMapping("/sales")
		public ResponseEntity<Object> view1() {
			return srs.getPage(0);
		}

		@GetMapping("/sales/{storeno}/{date}")
		public ResponseEntity<Object> view15(@PathVariable String storeno, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd" ) Date date ) throws CustomException {
			return srs.get(storeno,date);
		}
		
		//add
		@PostMapping("/sales")
		public ResponseEntity<Object> view2(@RequestBody Sales s) throws CustomException {
			return srs.add(s);
		}
		
		//update
		@PutMapping("/sales/{storeno}/{date}")
		public ResponseEntity<Object> view3(@RequestBody Sales s,@PathVariable(name="storeno") String entrynumber,@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date ) throws CustomException {
			return srs.update(s,entrynumber,date);
		}
		
		@PatchMapping("/sales/{storeno}/{date}")
		public ResponseEntity<Object> view35(@RequestBody Map<Object,Object> flds,@PathVariable(name="storeno") String entrynumber ,@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date ) throws CustomException {
			return srs.patch(flds,entrynumber,date);
		}
		
		//delete
		@DeleteMapping("/sales/{storeno}/{date}")
		public ResponseEntity<Object> view4(@PathVariable String storeno, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd" ) Date date ) throws CustomException {
			return srs.delete(storeno,date);
		}
		

		//others
		//search
		@GetMapping({"/sales/search/{col}/{qer}/{pg}","/sales/search/{col}/{qer}","/sales/search/{col}","/sales/search"})
		public ResponseEntity<Object> view6(@PathVariable(required = false) String col, @PathVariable(required = false) String qer, @PathVariable(required = false) String pg) {
			String rcol = StringUtils.isEmpty(col) ? "manager" : col;
			String rqer = StringUtils.isEmpty(qer) ? "m*" : qer+"+";
			int page = Integer.parseInt(StringUtils.isEmpty(pg) ? "0" : pg);
			return srs.getData(rcol,rqer,page);
		}
		

}
