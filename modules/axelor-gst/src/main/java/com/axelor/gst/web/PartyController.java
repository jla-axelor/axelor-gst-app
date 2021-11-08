package com.axelor.gst.web;

import com.axelor.gst.service.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class PartyController {
	
	public void setPartSequence(ActionRequest req , ActionResponse res) {
		String model = "com.axelor.gst.db.Party";
		String sequence = Beans.get(SequenceService.class).setSequence(model);
		if(sequence.equals(null)) {
			res.setError("Please set Sequence for party");
		}
		else {
			res.setValue("refrence", sequence);
		}
	}
}
