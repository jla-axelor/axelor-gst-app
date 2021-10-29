package com.axelor.gst.web;

import java.util.List;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class SequenceController {
	
	public void setSequence(ActionRequest req , ActionResponse res) {
		String SEQUENCE_MODEL = "com.axelor.gst.db.Party";
		String SEQUENCE = new String();
		
		List<Sequence> sequence = Beans.get(SequenceRepository.class).all().fetch();
		for(Sequence s : sequence) {
			System.err.println(s.getModel().getFullName());
			if (s.getModel().getFullName().equals(SEQUENCE_MODEL)) {
				String prifix = s.getPrifix();
				String suffix = s.getSuffix();
				Integer padding = s.getPadding();
				Integer incrementOf = s.getIncrementOf();
				String pad = new String("0");
				for(int i =0;i<padding-1;i++) {
					pad = pad+"0";  
				}
				Integer nextNumber =Integer.parseInt(s.getNextNumber());
				SEQUENCE = prifix+pad+nextNumber+suffix;
				 nextNumber =+ incrementOf;
				 s.setNextNumber(nextNumber.toString());
				 System.err.println(s.getNextNumber());
				 System.err.println(s.getIncrementOf());
				 break;
			}
		}
		res.setValue("refrence", SEQUENCE);
		System.err.println("---------------------");
		System.err.println(SEQUENCE);
		
	}
}
