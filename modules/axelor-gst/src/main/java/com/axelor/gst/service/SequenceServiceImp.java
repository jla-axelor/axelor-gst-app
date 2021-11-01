package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.inject.Beans;
import com.google.inject.persist.Transactional;

public class SequenceServiceImp implements SequenceService{
	
	@Override
	@Transactional
	public String setSequence(String model) {
		SequenceRepository repo = Beans.get(SequenceRepository.class);
		Sequence s =  repo.all().fetch().stream().filter(i->i.getModel().getFullName().equals(model)).findFirst().get();
		String SEQUENCE = "";
		if (s != null) {
			
			String prefix = s.getPrefix();
			String suffix = s.getSuffix();
			Integer padding = s.getPadding();
			Integer incrementOf = s.getIncrementOf();
			String pad = new String("0");
			
			for(int i =0;i<padding-1;i++) {
				pad = pad+"0";  
			}
			Integer nextNumber =Integer.parseInt(s.getNextNumber());
			
			SEQUENCE = prefix+pad+nextNumber+suffix;
			 
			nextNumber = nextNumber + incrementOf;
	
			s.setNextNumber(nextNumber.toString());
			repo.save(s);
			
			 System.err.println(s.getNextNumber());
			 System.err.println(s.getIncrementOf());
			 System.err.println(SEQUENCE);
			 return SEQUENCE;
		}
		return SEQUENCE;
	}
}
