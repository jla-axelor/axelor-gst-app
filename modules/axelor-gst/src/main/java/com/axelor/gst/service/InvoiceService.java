package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.gst.db.InvoiceLine;

public interface InvoiceService {
	public List<BigDecimal> setInvoiceNet(List<InvoiceLine> invoiceLines);
}
