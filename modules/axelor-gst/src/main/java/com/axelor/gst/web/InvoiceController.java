package com.axelor.gst.web;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.axelor.common.StringUtils;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.gst.service.InvoiceService;
import com.axelor.gst.service.ProductService;
import com.axelor.gst.service.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceController {

	public void setPartyContact(ActionRequest req, ActionResponse res) {
		List<Contact> contacts = req.getContext().asType(Invoice.class).getParty().getContact();
		List<Address> addresses = req.getContext().asType(Invoice.class).getParty().getAddress();

		for (Contact c : contacts) {
			if (c.getType().toString().equals("1")) {
				res.setValue("partyContact", c);
				break;
			}
		}
		if (!addresses.isEmpty()) {
		for (Address a : addresses) {
			if (a.getType().toString().equals("1")) {
				res.setValue("invoiceAddress", a);
				break;
			} else if (a.getType().toString().equals("2")) {
				res.setValue("invoiceAddress", a);
				break;
			}
		}
		}
		else {
			res.setValue("invoiceAddress", null);
		}
	}

	public void setShippingAdd(ActionRequest req, ActionResponse res) {
		Invoice invoiceClass = req.getContext().asType(Invoice.class);
		Boolean isTrue =invoiceClass.getUseInvoiceAddressAsShippingAddress();
		Address invoiceAddress = invoiceClass.getInvoiceAddress();

		List<Address> addresses = invoiceClass.getParty().getAddress();

		if (isTrue) {
			res.setValue("shippingAddress", invoiceAddress);
			res.setAttr("shippingAddress", "hidden", true);
		} else {
			for (Address a : addresses) {
				if (a.getType().toString().equals("1")) {
					res.setValue("shippingAddress", a);
					break;
				} else if (a.getType().toString().equals("3")) {
					res.setValue("shippingAddress", a);
					break;
				}
			}
			res.setAttr("shippingAddress", "hidden", false);
		}

	}


	public void setInvoiceNet(ActionRequest req, ActionResponse res) {
		List<InvoiceLine> invoiceLines = req.getContext().asType(Invoice.class).getInvoiceItem();

		List<BigDecimal> newValues = Beans.get(InvoiceService.class).setInvoiceNet(invoiceLines);

		res.setValue("netAmmount", newValues.get(0));
		res.setValue("netIGST", newValues.get(1));
		res.setValue("netCSGT", newValues.get(2));
		res.setValue("netSGST", newValues.get(3));
		res.setValue("grossAmount", newValues.get(4));
	}

	public void setSequence(ActionRequest req, ActionResponse res) {
		Invoice invoiceClass = req.getContext().asType(Invoice.class);
		if (invoiceClass.getStatus().equals("2")) {
			String model = "com.axelor.gst.db.Invoice";
			try {
				if (StringUtils.isEmpty((invoiceClass.getReference()))) {
					String sequence = Beans.get(SequenceService.class).setSequence(model);
					
						Address invoiceAddress = invoiceClass.getInvoiceAddress();

						if (!invoiceAddress.equals(null)) {
							if (!sequence.equals(null))
								res.setValue("reference", sequence);
						}
					}
			} catch (NoSuchElementException e) {
				res.setFlash("Please set Sequence for Invoice");
				res.setValue("status", "1");
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void setSelectedProduct(ActionRequest req, ActionResponse res) {
		Collection<Integer> productIds;

		if (req.getContext().containsKey("_productIds")) {
			productIds = (Collection<Integer>) req.getContext().get("_productIds");
			List<InvoiceLine> lines = Beans.get(ProductService.class).setproduct(productIds);
			res.setValue("invoiceItem", lines);
		}
	}


	public void setInvoiceItemsValues(ActionRequest req, ActionResponse res) {

		Invoice invoiceClass = req.getContext().asType(Invoice.class);
		List<InvoiceLine> lines = invoiceClass.getInvoiceItem();
		if (!lines.isEmpty()) {

			String invoiceAddressStateName = invoiceClass.getInvoiceAddress().getState().getName();
			String companyAddressStateName = invoiceClass.getCompany().getAddress().getState().getName();

			if (!invoiceAddressStateName.equals(null) && !companyAddressStateName.equals(null)) {
				Beans.get(InvoiceLineService.class).setAmountOnchageParty(lines, invoiceAddressStateName,
						companyAddressStateName);
			}
			
			setInvoiceNet(req, res);
		}
	}
	public void filterInvoiceAddress(ActionRequest req , ActionResponse res) {
			List<Long> addressList =  req.getContext().asType(Invoice.class).getParty().getAddress().stream().map(i->i.getId()).collect(Collectors.toList());
			String listStr = org.apache.commons.lang.StringUtils.join(addressList, ",");
			res.setAttr("invoiceAddress", "domain" ,"self.id IN"+ "("+listStr+")");
	}

}
