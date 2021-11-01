package com.axelor.gst.web;

import java.util.List;

import com.axelor.gst.db.Contact;
import java.math.BigDecimal;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.service.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceController {

	public void setPartyContact(ActionRequest req , ActionResponse res) {
		List<Contact> contacts = req.getContext().asType(Invoice.class).getParty().getContact();
		List<Address> addresses = req.getContext().asType(Invoice.class).getParty().getAddress();
		for(Contact c : contacts) {
			if (c.getType().toString().equals("1")) {
				res.setValue("partyContact", c);
				break;
			}
		}
		for(Address a : addresses) {
			if (a.getType().toString().equals("1")) {	
				res.setValue("invoiceAddress", a);
				break;
			}
			else if(a.getType().toString().equals("2")) {
				res.setValue("invoiceAddress", a);
				break;
			}
		}
	}
	
	public void setShippingAdd(ActionRequest req , ActionResponse res) {
		Boolean isTrue = req.getContext().asType(Invoice.class).getUseInvoiceAddressAsShippingAddress();
		Address invoiceAddress = req.getContext().asType(Invoice.class).getInvoiceAddress();
		
		List<Address> addresses = req.getContext().asType(Invoice.class).getParty().getAddress();
		
		if (isTrue) {
			res.setValue("shippingAddress", invoiceAddress);
			res.setAttr("shippingAddress", "hidden", true);
		}
		else {
			for(Address a : addresses) {
				if (a.getType().toString().equals("1")) {	
					res.setValue("shippingAddress", a);
					break;
				}
				else if (a.getType().toString().equals("3")) {	
					res.setValue("shippingAddress", a);
					break;
				}
			}
			res.setAttr("shippingAddress", "hidden", false);
		}
		
	}
	
	
	public void setAmount(ActionRequest req , ActionResponse res) {
		
		Address invoiceAddress = req.getContext().getParent().asType(Invoice.class).getInvoiceAddress();
		Address companyAddress = req.getContext().getParent().asType(Invoice.class).getCompany().getAddress();
		
		BigDecimal netAmount = req.getContext().asType(InvoiceLine.class).getNetAmount();
		BigDecimal gstRate =  req.getContext().asType(InvoiceLine.class).getGstRate();
		
		BigDecimal two = new BigDecimal(2);
		
		BigDecimal sgstAndCgst = (netAmount.add(gstRate)).divide(two);
		
		if (!invoiceAddress.equals(companyAddress)) {
			res.setValue("IGST", (netAmount.add(gstRate)));
			res.setValue("grossAmount", (netAmount.add(gstRate)).add(netAmount));
		}
		else {
			res.setValue("SGST", sgstAndCgst);	
			res.setValue("CGST", sgstAndCgst);
			res.setValue("grossAmount", sgstAndCgst.add(netAmount));
		}
	}
	
	public void setInvoiceNet(ActionRequest req , ActionResponse res) {
		List<InvoiceLine> invoiceLines = req.getContext().asType(Invoice.class).getInvoiceItem();
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
		res.setValue("netAmmount", netAmount);
		res.setValue("netIGST", netIGST);
		res.setValue("netCSGT", netCGST);
		res.setValue("netSGST", netSGST);
		res.setValue("grossAmount", grossAmount);
	}
	
	public void setSequence(ActionRequest req , ActionResponse res) {
		if (req.getContext().asType(Invoice.class).getStatus().equals("2")) {
			
			String model = "com.axelor.gst.db.Invoice";
			String SEQUENCE = Beans.get(SequenceService.class).setSequence(model);
			res.setValue("refrence", SEQUENCE);
		}
	}
	
}
