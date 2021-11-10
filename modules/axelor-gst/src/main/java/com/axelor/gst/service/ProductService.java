package com.axelor.gst.service;

import java.util.Collection;
import java.util.List;

import com.axelor.gst.db.InvoiceLine;

public interface ProductService {
	public List<InvoiceLine> setproduct(Collection<Integer> productids);
}
