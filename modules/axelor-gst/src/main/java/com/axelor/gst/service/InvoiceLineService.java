package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.gst.db.InvoiceLine;

public interface InvoiceLineService {
	public List<BigDecimal> setAmount(BigDecimal netAmount , BigDecimal gstRate ,String invoiceAddressStateName,String companyAddressStateName);
	public void setAmountOnchageParty(List<InvoiceLine> lines ,String invoiceAddressStateName,String companyAddressStateName);
}
