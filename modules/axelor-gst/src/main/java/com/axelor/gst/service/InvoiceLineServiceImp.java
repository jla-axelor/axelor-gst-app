package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.axelor.gst.db.InvoiceLine;
import com.axelor.inject.Beans;
import com.google.inject.persist.Transactional;

public class InvoiceLineServiceImp implements InvoiceLineService {
	
	@Override
	public List<BigDecimal> setAmount(BigDecimal netAmount, BigDecimal gstRate , String invoiceAddressStateName,String companyAddressStateName) {
		
		List<BigDecimal> amounts = new ArrayList<>(); 
		
		BigDecimal two = new BigDecimal(2);
		
		BigDecimal grossAmount;
		
		BigDecimal sgstAndCgst = (netAmount.multiply(gstRate)).divide(new BigDecimal(100)).divide(two);
		
		if (!(invoiceAddressStateName).equals(companyAddressStateName)) {
			BigDecimal IGST =  netAmount.add(gstRate);
			grossAmount = (netAmount.add(gstRate)).add(netAmount);
			amounts = Arrays.asList(IGST,grossAmount);
		}
		else {
			grossAmount = sgstAndCgst.add(netAmount);
			amounts = Arrays.asList(sgstAndCgst,grossAmount);
		}
		return amounts;
	}
	
	@Override
	@Transactional
	public void setAmountOnchageParty(List<InvoiceLine> lines, String invoiceAddressStateName,
			String companyAddressStateName) {
		for(InvoiceLine line : lines) {
			BigDecimal netAmount = line.getNetAmount();
			BigDecimal gstRate =  line.getGstRate();
			
			BigDecimal grossAmount;
			
			List<BigDecimal> amounts = Beans.get(InvoiceLineService.class).setAmount(netAmount, gstRate, invoiceAddressStateName, companyAddressStateName);
			
			if (!(invoiceAddressStateName).equals(companyAddressStateName)) {
				grossAmount = amounts.get(1);
				line.setSGST(new BigDecimal(0));
				line.setCGST(new BigDecimal(0));
				BigDecimal IGST =  amounts.get(0);
				line.setIGST(IGST);
				line.setGrossAmount(grossAmount);
				
			}
			else {
				BigDecimal sgstAndCgst = amounts.get(0);
				grossAmount = amounts.get(1);
				line.setSGST(sgstAndCgst);
				line.setCGST(sgstAndCgst);
				line.setGrossAmount(grossAmount);
				line.setIGST(new BigDecimal(0));
			}
		}
		
	}
}
