package com.axelor.gst.web;

import java.util.List;

import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.repo.CompanyRepository;
import com.axelor.gst.db.repo.ContactRepository;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceController {
//	public List<Company> getDefault(){
//		List<Company> compines = (List<Company>) Beans.get(CompanyRepository.class).all().fetch().get(1);
//		Company comany = compines.get(1); 
//		
//	}*/
	
	public void getPrimeryConact(ActionRequest req , ActionResponse res) {
//		Contact contact = req.getContext().asType(Contact.class);
		List<Contact> contacts = Beans.get(ContactRepository.class).all().fetch();
		
		
	}
}
