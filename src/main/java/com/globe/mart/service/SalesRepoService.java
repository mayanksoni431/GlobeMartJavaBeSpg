package com.globe.mart.service;

import com.globe.mart.beans.Sales;
import com.globe.mart.exception.CustomException;
import com.globe.mart.repo.SalesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SalesRepoService {
	@Autowired
	private SalesRepo sr;
	@Autowired
	private MongoTemplate mongotemplate;

	//get with paging
	public ResponseEntity<Object> getPage(int pageno) {
		int pageSize = 10;
		String orderBy = "dateCreated";
		Pageable pageRequest = PageRequest.of(pageno, pageSize, Sort.Direction.ASC,orderBy);
		Page<Sales> page = sr.findAll(pageRequest);

		//page
		long eleTotal = page.getTotalElements();
		long pageTotal = page.getTotalPages();
		List<Sales> contList =  page.getContent();
		int clen = contList.size();
		LinkedHashMap<Object,Object> lst1 = new LinkedHashMap<Object,Object>();
		lst1.put("totalElements",eleTotal);
		lst1.put("totalPages",pageTotal);
		lst1.put("content",contList);
		lst1.put("content-length",clen);

		//outer
		LinkedHashMap<Object,Object> lst = new LinkedHashMap<Object,Object>();
		lst.put("status","success");
		lst.put("data",lst1);

		return new ResponseEntity<Object>(lst,HttpStatus.OK);

	}
//
//	public ResponseEntity<Object> getAll() throws CustomException {
//		try {
//			List<Sales> lst = sr.findAll();
//			LinkedHashMap<Object,Object> hmap = new LinkedHashMap<Object, Object>();
//			hmap.put("status","success");
//			hmap.put("data",lst);
//			return new ResponseEntity<Object>(hmap,HttpStatus.OK);
//		}catch (Exception e){
//			throw new CustomException("500","Internal Error Occured.");
//		}
//	}

	public ResponseEntity<Object> get(String storeno,Date date) throws CustomException {
		if(existsByStoreNoAndDate(storeno,date)) {
			Sales s = findByStoreNoAndDate(storeno,date);
			LinkedHashMap<Object,Object> hmap = new LinkedHashMap<Object, Object>();
			hmap.put("status","success");
			hmap.put("data",s);
			return new ResponseEntity<Object>(hmap,HttpStatus.OK);
		}
		else {
			throw new CustomException("404","Sales Not Found");
		}
	}

	//add
	public ResponseEntity<Object> add(Sales s) throws CustomException {
		if(existsByStoreNoAndDate(s.getStoreNo(), s.getSalesDate())){
			throw new CustomException("409","Sales-Entry already exist");
		}else if(s.getStoreNo()==null || s.getSalesDate()==null){
			throw new CustomException("400","Store Number and Date can't be null");
		}else if(s.getStoreNo().equals("") || s.getSalesDate().equals("")){
			throw new CustomException("400","Store Number and Date can't be empty");
		}
		else{
			try{
				s.setDateCreated(new Date());
				s.setDateLastModified(new Date());
				sr.insert(s);
				LinkedHashMap<Object,Object> lst = new LinkedHashMap<Object,Object>();
				lst.put("code","200");
				lst.put("status","success");
				return new ResponseEntity<Object>(lst,HttpStatus.OK);
			}catch (Exception e){
				throw new CustomException("400","Bad Request");
			}
		}
	}

	//update
	public ResponseEntity<Object> update(Sales newSales,String storeno,Date date) throws CustomException {
		if(!existsByStoreNoAndDate(storeno, date)) {
			throw new CustomException("404","Sales Not Found");
		}
		else{
			Sales oldSales = findByStoreNoAndDate(storeno, date);

			oldSales.setDateLastModified(new Date());
			oldSales.setManagerName(newSales.getManagerName());
			oldSales.setManagerId(newSales.getManagerId());

			oldSales.setNoOfPOStr(newSales.getNoOfPOStr());
			oldSales.setTotalPOSAmt(newSales.getTotalPOSAmt());

			oldSales.setNoOfOnlinetr(newSales.getNoOfOnlinetr());
			oldSales.setTotalOnlineOrderAmt(newSales.getTotalOnlineOrderAmt());

			oldSales.setNoOfReturns(newSales.getNoOfReturns());
			oldSales.setTotalReturnsAmt(newSales.getTotalReturnsAmt());

			oldSales.setTotalDiscountAmt(newSales.getTotalDiscountAmt());

			oldSales.setTotalSalesAmt(newSales.getTotalSalesAmt());

			sr.save(oldSales);
			LinkedHashMap<Object,Object> lst = new LinkedHashMap<Object,Object>();
			lst.put("status","success");
			lst.put("data",oldSales);
			return(new ResponseEntity<Object>(lst,HttpStatus.OK));
		}
	}

	//update some
	public ResponseEntity<Object> patch(Map<Object ,Object > flds, String storeno,Date date) throws CustomException {
		if(!existsByStoreNoAndDate(storeno, date)) {
			throw new CustomException("404","Sales Not Found");
		}else{
			Date dt = getISODate(date);
			Query q = new Query();
			Update upd = new Update();
			flds.forEach((k,v)->{
				upd.set((String) k, v);
			});
			q.addCriteria(Criteria.where("storeNo").is(storeno));
			q.addCriteria(Criteria.where("salesDate").is(dt));
			mongotemplate.updateFirst(q, upd, Sales.class);

			LinkedHashMap<Object,Object> lst = new LinkedHashMap<Object,Object>();
			lst.put("code","200");
			lst.put("status","success");
			return(new ResponseEntity<Object>(lst,HttpStatus.OK));
		}
	}

	//delete
	public ResponseEntity<Object> delete(String storeno,Date date) throws CustomException {
		if(existsByStoreNoAndDate(storeno, date)) {
			Date dt = getISODate(date);
			Query q = new Query();
			q.addCriteria(Criteria.where("storeNo").is(storeno));
			q.addCriteria(Criteria.where("salesDate").is(dt));
			mongotemplate.remove(q,Sales.class);
			LinkedHashMap<Object,Object> lst = new LinkedHashMap<Object,Object>();
			lst.put("code","200");
			lst.put("status","success");
			return(new ResponseEntity<Object>(lst,HttpStatus.OK));
		}else {
			throw new CustomException("404","Sales Not Found");
		}
	}

	//check exist
	public boolean existsByStoreNoAndDate(String s, Date date){
		Date dt = getISODate(date);
		Query q = new Query();
		q.addCriteria(Criteria.where("storeNo").is(s));
		q.addCriteria(Criteria.where("salesDate").is(dt));
		boolean res = mongotemplate.exists(q,Sales.class);
		return res;
	}

	private Date getISODate(Date date) {
		String sdt = null;
		Date dt = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000+00:00");
			//arranged string date
			sdt =  sdf.format(date);
			//iso date
			sdf.setTimeZone(TimeZone.getTimeZone("ISO"));
			dt = sdf.parse(sdt);
		}catch (ParseException e){
			e.printStackTrace();
		}
		finally {
			return dt;
		}
	}

	//find exist
	public Sales findByStoreNoAndDate(String s, Date date) {
		Date dt = getISODate(date);
		Query q = new Query();
		q.addCriteria(Criteria.where("storeNo").is(s));
		q.addCriteria(Criteria.where("salesDate").is(dt));
		List<Sales> res = mongotemplate.find(q,Sales.class);
		return res.get(0);
	}

	//search
	public ResponseEntity<Object> getData(String rcol, String rqer, int pageNo) {
		int pageSize = 10;
		String orderBy = "dateCreated";

		Query q = new Query();
		q.addCriteria(Criteria.where(rcol).regex(rqer));
		Pageable pageRequest = PageRequest.of(pageNo, pageSize, Sort.Direction.ASC,orderBy);
		q.with(pageRequest);

		List<Sales> lst = mongotemplate.find(q,Sales.class);

		Page<Sales> page = PageableExecutionUtils.getPage(
				lst,
				pageRequest,
				() -> mongotemplate.count(Query.of(q).limit(-1).skip(-1), Sales.class));

		long eleTotal = page.getTotalElements();
		long pageTotal = page.getTotalPages();
		List<Sales> contList =  page.getContent();
		int clen = contList.size();

		//page
		LinkedHashMap<Object,Object> lst1 = new LinkedHashMap<Object,Object>();
		lst1.put("totalElements",eleTotal);
		lst1.put("totalPages",pageTotal);
		lst1.put("content",contList);
		lst1.put("content-length",clen);

		//outer
		LinkedHashMap<Object,Object> lst2 = new LinkedHashMap<Object,Object>();
		lst2.put("status","success");
		lst2.put("data",lst1);

		return new ResponseEntity<Object>(lst2,HttpStatus.OK);
	}










//	//get
//	public List<Sales> getAll() {
//		return slr.findAll();
//	}
//
//	public ResponseEntity<Sales> get(String entryno) {
//		if(slr.existsById(entryno)) {
//			Optional<Sales> objs = slr.findById(entryno);
//			Sales s = objs.get();
//			return new ResponseEntity<Sales>(s,HttpStatus.OK);
//		}
//		else {
//			return new ResponseEntity<Sales>(HttpStatus.NOT_FOUND);
//		}
//	}
//
//	//add
//	public void add(Sales s) {
//		s.setDateCreated(new Date());
//		slr.insert(s);
//	}
//
//	//update
//	public ResponseEntity<Sales> update(Sales newSales, String entrynumber) {
//		if(existsByEntry(entrynumber)) {
//			Optional<Sales> opOldSl = slr.findById(entrynumber);
//			Sales oldSales = opOldSl.get();
//
//			oldSales.setSalesNo(newSales.getSalesNo());
//
//			oldSales.setManagerId(newSales.getManagerId());
//			oldSales.setManagerName(newSales.getManagerName());
//
//			oldSales.setSalesDate(newSales.getSalesDate());
//
//			oldSales.setNoOfPOStr(newSales.getNoOfPOStr());
//			oldSales.setTotalPOSAmt(newSales.getTotalPOSAmt());
//
//			oldSales.setNoOfOnlinetr(newSales.getNoOfOnlinetr());
//			oldSales.setTotalOnlineOrderAmt(newSales.getTotalOnlineOrderAmt());
//
//			oldSales.setNoOfReturns(newSales.getNoOfReturns());
//			oldSales.setTotalReturnsAmt(newSales.getTotalReturnsAmt());
//
//			oldSales.setTotalDiscountAmt(newSales.getTotalDiscountAmt());
//			oldSales.setTotalSalesAmt(newSales.getTotalSalesAmt());
//
//			oldSales.setDateLastModified(new Date());
//
//			slr.save(oldSales);
//
//			return(new ResponseEntity<Sales>(HttpStatus.OK));
//		}else {
////			newSales.setId(entrynumber);
//			newSales.setDateCreated(new Date());
//			return(new ResponseEntity<Sales>(newSales,HttpStatus.CREATED));
//		}
//	}
//
//
//	//update some
//	public ResponseEntity<Sales> patch(Map<Object, Object> flds, String entrynumber) {
//		if(slr.existsById(entrynumber)) {
//			Optional<Sales> osl = slr.findById(entrynumber);
//			Sales st = osl.get();
//			Query q = new Query();
//			Update upd = new Update();
//			flds.forEach((k,v)->{
//				upd.set((String) k, v);
//			});
//			q.addCriteria(Criteria.where("entryId").is(entrynumber));
//			mongotemplate.updateFirst(q, upd, Sales.class);
//			return new ResponseEntity<Sales>(HttpStatus.OK);
//		}
//		else {
//			return new ResponseEntity<Sales>(HttpStatus.NOT_FOUND);
//		}
//	}
//
//
//
//
//	//check
//	private double check(double nAmt, double oAmt) {
//		if(nAmt==0.0) {
//			return oAmt;
//		}
//		else {
//			return nAmt;
//		}
//	}
//
//	//check
//	private Date check(Date ndate, Date odate2) {
//		if(ndate==null) {
//			return odate2;
//		}
//		else {
//			return ndate;
//		}
//	}
//
//	//check
//	private String check(String newValue, String oldValue) {
//		if(newValue.equals("$")) {
//			return oldValue;
//		}else {
//			return newValue;
//		}
//	}
//
//	//check
//	private int check(int newValue, int oldValue) {
//		if(newValue==0) {
//			return oldValue;
//		}else {
//			return newValue;
//		}
//	}
//
//	//check
//	private boolean existsByEntry(String entrynumber) {
//		return slr.existsById(entrynumber);
//	}
//
//	//delete
//	public void delete(String salesno) {
//		slr.deleteById(salesno);
//	}
//
//
//	//other
//	//pages
//	public List<Sales> getPage(int pageno) {
//		int pagesize = 2;
//		String orderBy = "entryno";
//		Pageable pageable = PageRequest.of(pageno,pagesize,Sort.Direction.ASC,orderBy);
//		Page<Sales> pg = slr.findAll(pageable);
//		return pg.getContent();
//	}
//
//	//search
//	public List<Sales> getData(String rcol, String rqer) {
//		Query query = new Query();
//		query.addCriteria(Criteria.where(rcol).regex(rqer));
//		return mongotemplate.find(query, Sales.class);
//	}

}
