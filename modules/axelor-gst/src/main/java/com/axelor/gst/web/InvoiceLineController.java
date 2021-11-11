package com.axelor.gst.web;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceLineController {
	public void setAmountOnNew(ActionRequest req, ActionResponse res) {

		try {
			Invoice invoiceClass = req.getContext().getParent().asType(Invoice.class);
			InvoiceLine inviceLineClass = req.getContext().asType(InvoiceLine.class);

			String invoiceAddressStateName = invoiceClass.getInvoiceAddress().getState().getName();
			String companyAddressStateName = invoiceClass.getCompany().getAddress().getState().getName();

			if (!invoiceAddressStateName.equals(null) && !companyAddressStateName.equals(null)) {

				BigDecimal netAmount = inviceLineClass.getNetAmount();
				BigDecimal gstRate = inviceLineClass.getGstRate();

				BigDecimal grossAmount;

				List<BigDecimal> amounts = Beans.get(InvoiceLineService.class).setAmount(netAmount, gstRate,
						invoiceAddressStateName, companyAddressStateName);

				if (!(invoiceAddressStateName).equals(companyAddressStateName)) {
					BigDecimal IGST = amounts.get(0);
					grossAmount = amounts.get(1);
					res.setValue("IGST", IGST);
					res.setValue("grossAmount", grossAmount);
				} else {
					BigDecimal sgstAndCgst = amounts.get(0);
					grossAmount = amounts.get(1);
					res.setValue("SGST", sgstAndCgst);
					res.setValue("CGST", sgstAndCgst);
					res.setValue("grossAmount", grossAmount);
				}
			}
		} catch (NullPointerException e) {
			res.setError("State of invoice or company address is not set");
		}
	}

}
