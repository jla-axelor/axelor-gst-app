package com.axelor.gst.web;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.axelor.common.StringUtils;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.repo.PartyRepository;
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
			List<Party> partyList = Beans.get(PartyRepository.class).all().fetch().stream().filter(i->i.getReference().equals(sequence)).collect(Collectors.toList());
			if(!partyList.isEmpty())
				res.setError(sequence +": Sequence is already exist");
			else {
				if(!sequence.equals(null))
					res.setValue("reference", sequence);
				}
			}
		}
		catch (NoSuchElementException e) {
			res.setError("Please set Sequence for party");
		}
	}
}
