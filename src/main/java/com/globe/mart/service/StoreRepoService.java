package com.globe.mart.service;

import com.globe.mart.beans.Store;
import com.globe.mart.exception.CustomException;
import com.globe.mart.repo.StoreRepo;
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

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StoreRepoService {
	@Autowired
	private StoreRepo sr;

	@Autowired
	private MongoTemplate mongotemplate;

	//get with paging
	public ResponseEntity<Object> getPage(int pageno) {
		int pageSize = 10;
		String orderBy = "storeno";
		Pageable pageRequest = PageRequest.of(pageno, pageSize, Sort.Direction.ASC,orderBy);
		Page<Store> page = sr.findAll(pageRequest);

		//page
		long eleTotal = page.getTotalElements();
		long pageTotal = page.getTotalPages();
		List<Store> contList =  page.getContent();
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
//			List<Store> lst = sr.findAll();
//			LinkedHashMap<Object,Object> hmap = new LinkedHashMap<Object, Object>();
//			hmap.put("status","success");
//			hmap.put("data",lst);
//			return new ResponseEntity<Object>(hmap,HttpStatus.OK);
//		}catch (Exception e){
//			throw new CustomException("500","Internal Error Occured.");
//		}
//	}

	public ResponseEntity<Object> get(String storeno) throws CustomException {
		if(existsByStoreNo(storeno)) {
			Store s = findByStoreNo(storeno);
			LinkedHashMap<Object,Object> hmap = new LinkedHashMap<Object, Object>();
			hmap.put("status","success");
			hmap.put("data",s);
			return new ResponseEntity<Object>(hmap,HttpStatus.OK);
		}
		else {
			throw new CustomException("404","Store Not Found");
		}
	}

	//add
	public ResponseEntity<Object> add(Store s) throws CustomException {
		if(existsByStoreNo(s.getStoreNo())){
			throw new CustomException("409","The Store with specified store number already exist");
		}else if(s.getStoreNo()==null){
			throw new CustomException("400","Store Number can't be null");
		}else if(s.getStoreNo().equals("")){
			throw new CustomException("400","Store Number can't be empty");
		}
		else{
			try{
				s.setDateCreated(new Date());
				s.setDateModified(new Date());
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
	public ResponseEntity<Object> update(Store newStore,String storeno) throws CustomException {
		if(!existsByStoreNo(storeno)) {
			throw new CustomException("404","Store Not Found");
		}
		else{
			Store oldStore = findByStoreNo(storeno);
			oldStore.setName(newStore.getName());
			oldStore.setCity(newStore.getCity());
			oldStore.setState(newStore.getState());
			oldStore.setCountry(newStore.getCountry());
			oldStore.setManager(newStore.getManager());
			oldStore.setManagerId(newStore.getManagerId());
			oldStore.setManagerEmail(newStore.getManagerEmail());
			oldStore.setManagerMobNo(newStore.getManagerMobNo());
			oldStore.setNoOfEmployees(newStore.getNoOfEmployees());
			oldStore.setDateModified(new Date());
			sr.save(oldStore);
			LinkedHashMap<Object,Object> lst = new LinkedHashMap<Object,Object>();
			lst.put("status","success");
			lst.put("data",oldStore);
			return(new ResponseEntity<Object>(lst,HttpStatus.OK));
		}
	}

	//update some
	public ResponseEntity<Object> patch(Map<Object ,Object > flds, String storeno) throws CustomException {
		if(!existsByStoreNo(storeno)) {
			throw new CustomException("404","Store Not Found");
		}else{
			Store st = findByStoreNo(storeno);
			Query q = new Query();
			Update upd = new Update();
			flds.forEach((k,v)->{
				upd.set((String) k, v);
			});
			q.addCriteria(Criteria.where("storeNo").is(storeno));
			mongotemplate.updateFirst(q, upd, Store.class);

			LinkedHashMap<Object,Object> lst = new LinkedHashMap<Object,Object>();
			lst.put("code","200");
			lst.put("status","success");
			return(new ResponseEntity<Object>(lst,HttpStatus.OK));
		}
	}

	//delete
	public ResponseEntity<Object> delete(String storeno) throws CustomException {
		if(existsByStoreNo(storeno)) {
			Query q = new Query();
			q.addCriteria(Criteria.where("storeNo").is(storeno));
			mongotemplate.remove(q,Store.class);
			LinkedHashMap<Object,Object> lst = new LinkedHashMap<Object,Object>();
			lst.put("code","200");
			lst.put("status","success");
			return(new ResponseEntity<Object>(lst,HttpStatus.OK));
		}else {
			throw new CustomException("404","Store Not Found");
		}
	}

	public boolean existsByStoreNo(String s) {
		Query q = new Query();
		q.addCriteria(Criteria.where("storeNo").is(s));
		boolean res = mongotemplate.exists(q,Store.class);
		return res;
	}

	public Store findByStoreNo(String s) {
		Query q = new Query();
		q.addCriteria(Criteria.where("storeNo").is(s));
		List<Store> res = mongotemplate.find(q,Store.class);
		return res.get(0);
	}

	//search
	public ResponseEntity<Object> getData(String rcol, String rqer, int pageNo) {
		int pageSize = 10;
		String orderBy = "storeno";

		Query q = new Query();
		q.addCriteria(Criteria.where(rcol).regex(rqer));
		Pageable pageRequest = PageRequest.of(pageNo, pageSize, Sort.Direction.ASC,orderBy);
		q.with(pageRequest);

		List<Store> lst = mongotemplate.find(q,Store.class);

		Page<Store> page = PageableExecutionUtils.getPage(
				lst,
				pageRequest,
				() -> mongotemplate.count(Query.of(q).limit(-1).skip(-1), Store.class));

		long eleTotal = page.getTotalElements();
		long pageTotal = page.getTotalPages();
		List<Store> contList =  page.getContent();
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

}
