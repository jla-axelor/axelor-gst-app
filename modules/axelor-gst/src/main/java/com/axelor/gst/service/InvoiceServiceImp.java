package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.inject.Beans;
import com.google.inject.persist.Transactional;

public class InvoiceServiceImp implements InvoiceService {
	@Override
	public List<BigDecimal> setInvoiceNet(List<InvoiceLine> invoiceLines) {
		BigDecimal netAmount = new BigDecimal("0");
		BigDecimal netIGST = new BigDecimal("0");
		BigDecimal netCGST = new BigDecimal("0");
		BigDecimal netSGST = new BigDecimal("0");
		BigDecimal grossAmount = new BigDecimal("0");
		
		for(InvoiceLine a : invoiceLines) {
			netSGST = netSGST.add(a.getSGST());	
			netCGST = netCGST.add(a.getCGST());
			netIGST = netIGST.add(a.getIGST());
			netAmount = netAmount.add(a.getNetAmount());
			grossAmount = grossAmount.add(a.getGrossAmount());
		}
		List<BigDecimal> netValues = Arrays.asList(netAmount,netIGST,netCGST,netSGST,grossAmount);
		return netValues;
	}
	
	
	
}
