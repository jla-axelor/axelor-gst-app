package com.axelor.gst.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.axelor.common.StringUtils;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.service.InvoiceService;
import com.axelor.gst.service.ProductService;
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
		
		try {
			if(!StringUtils.isEmpty(req.getContext().getParent().asType(Invoice.class).getInvoiceAddress().getState().getName()) && 
				!StringUtils.isEmpty(req.getContext().getParent().asType(Invoice.class).getCompany().getAddress().getState().getName())) {
					
				String invoiceAddressStateName = req.getContext().getParent().asType(Invoice.class).getInvoiceAddress().getState().getName();
				String companyAddressStateName = req.getContext().getParent().asType(Invoice.class).getCompany().getAddress().getState().getName();
				
				BigDecimal netAmount = req.getContext().asType(InvoiceLine.class).getNetAmount();
				BigDecimal gstRate =  req.getContext().asType(InvoiceLine.class).getGstRate();
				
				BigDecimal two = new BigDecimal(2);
				
				BigDecimal sgstAndCgst = (netAmount.add(gstRate)).divide(two);
				
				if (!(invoiceAddressStateName).equals(companyAddressStateName)) {
					res.setValue("IGST", (netAmount.add(gstRate)));
					res.setValue("grossAmount", (netAmount.add(gstRate)).add(netAmount));
				}
				else {
					res.setValue("SGST", sgstAndCgst);	
					res.setValue("CGST", sgstAndCgst);
					res.setValue("grossAmount", sgstAndCgst.add(netAmount));
				}
			}
		}
		catch(NullPointerException e) {
				res.setError("State of invoice or company address is not set");
		}
	}
	
	public void setInvoiceNet(ActionRequest req , ActionResponse res) {
		List<InvoiceLine> invoiceLines = req.getContext().asType(Invoice.class).getInvoiceItem();

		List<BigDecimal> newValues = Beans.get(InvoiceService.class).setInvoiceNet(invoiceLines);
		
		res.setValue("netAmmount", newValues.get(0));
		res.setValue("netIGST", newValues.get(1));
		res.setValue("netCSGT", newValues.get(2));
		res.setValue("netSGST", newValues.get(3));
		res.setValue("grossAmount", newValues.get(4));
	}
	
	public void setSequence(ActionRequest req , ActionResponse res) {
		if (req.getContext().asType(Invoice.class).getStatus().equals("2")) {
			String model = "com.axelor.gst.db.Invoice";
			try {
				if(StringUtils.isEmpty((req.getContext().asType(Invoice.class).getReference()))) {
					String sequence = Beans.get(SequenceService.class).setSequence(model);
				
					Address invoiceAddress = req.getContext().asType(Invoice.class).getInvoiceAddress();
						
					if (!invoiceAddress.equals(null)) {
						if(!sequence.equals(null))
							res.setValue("reference", sequence);
					}
				}
			}
			catch (NoSuchElementException e) {
				res.setFlash("Please set Sequence for Invoice");
				res.setValue("status", "1");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setSelectedProduct(ActionRequest req , ActionResponse res) {
		Collection<Integer> productids ;
		try {
			if(req.getContext().containsKey("_id")) {
//				ActionViewBuilder actionViewBuilder = ActionView.define("Product").model(Product.class.getName()).add("grid","product_grid");
//				res.setView(actionViewBuilder.map());
//				res.setCanClose(true);
//				res.setError("Please select Product");
					productids = (Collection<Integer>)req.getContext().get("_id");
//					System.err.println(productids.stream().map(String::valueOf).collect(Collectors.joining(",")));
					List<InvoiceLine> lines = Beans.get(ProductService.class).setproduct(productids);
					System.err.println("Runing... ");
					res.setValue("invoiceItem", lines);
			}
		}
		catch(NullPointerException e) {
			System.err.println("exception running...");
			}
		}
	
	public void validateButton(ActionRequest req , ActionResponse res) {
		try {
			
			if(!StringUtils.isEmpty(req.getContext().asType(Invoice.class).getParty().getName())) {
				setShippingAdd(req, res);
			}
		}
		catch (NullPointerException e) {
			res.setError("Please set party");
		}
	}
	
}
