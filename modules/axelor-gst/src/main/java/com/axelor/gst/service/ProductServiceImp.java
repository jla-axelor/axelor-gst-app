package com.axelor.gst.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.inject.Beans;

public class ProductServiceImp implements ProductService {
	@Override
	public List<InvoiceLine> setproduct(Collection<Integer> productids) {
		List<InvoiceLine> lines = new ArrayList<>();
		if(!productids.equals(null)) {
			for(Integer i : productids) {
				Long l = new Long(i);	
				Product product = Beans.get(ProductRepository.class).find(l);
				InvoiceLine line = new InvoiceLine();
				line.setProduct(product);
				line.setItem(product.getName()+":"+product.getCode());
				line.setGstRate(product.getGstRate());
				lines.add(line);
			}
		}
		return lines;
	}
}
