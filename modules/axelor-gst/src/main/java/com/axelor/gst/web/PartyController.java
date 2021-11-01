package com.axelor.gst.web;

import com.axelor.gst.service.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class PartyController {
	
	public void setPartSequence(ActionRequest req , ActionResponse res) {
		String model = "com.axelor.gst.db.Party";
		String SEQUENCE = Beans.get(SequenceService.class).setSequence(model);
		res.setValue("refrence", SEQUENCE);
	}
}
