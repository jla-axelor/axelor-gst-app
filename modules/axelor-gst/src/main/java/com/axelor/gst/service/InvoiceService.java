//package com.axelor.gst.service;
//
//import java.util.List;
//
//import com.axelor.db.Query;
//import com.axelor.gst.db.Contact;
//import com.axelor.gst.db.Party;
//import com.axelor.gst.db.repo.PartyRepository;
//import com.axelor.inject.Beans;
//
//public class InvoiceService {
//		public List<Contact> getPrimery(Long id) {
//			List<Contact> contacts = Beans.get(PartyRepository.class).find(id).getContact();
//			List<Contact> primeryContact;
//			for(Contact c : contacts) {
//				if (c.getType().) {
//					primeryContact.add(c);
//				}
//			}
//		}
//}
