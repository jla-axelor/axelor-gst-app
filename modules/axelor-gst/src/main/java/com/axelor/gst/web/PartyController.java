package com.axelor.gst.web;

import java.util.NoSuchElementException;

import com.axelor.common.StringUtils;
import com.axelor.gst.db.Party;
import com.axelor.gst.service.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class PartyController {
	
	public void setPartSequence(ActionRequest req , ActionResponse res) {
		String model = "com.axelor.gst.db.Party";
		try {
		if(StringUtils.isEmpty((req.getContext().asType(Party.class).getReference()))) {
			String sequence = Beans.get(SequenceService.class).setSequence(model);
			if(!sequence.equals(null))
				res.setValue("reference", sequence);
		}
		}
		catch (NoSuchElementException e) {
			res.setError("Please set Sequence for party");
		}
	}
}
