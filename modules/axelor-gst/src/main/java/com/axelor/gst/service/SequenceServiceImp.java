package com.axelor.gst.service;

import com.axelor.common.StringUtils;
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
		String sequence = "";
		if (s != null) {
			
			String prefix = s.getPrefix();
			String suffix = new String("");
			suffix = s.getSuffix();
			Integer padding = s.getPadding();
			Integer incrementOf = s.getIncrementOf();
			Integer nextNumber =Integer.parseInt(s.getNextNumber());
			String number = String.format("%0"+padding+"d", nextNumber);
			
			if(!StringUtils.isEmpty(suffix)) {
				sequence = prefix+number+suffix;
			}
			else {
				sequence = prefix+number;
			}
			
			nextNumber = nextNumber + incrementOf;
	
			s.setNextNumber(nextNumber.toString());
			repo.save(s);
			return sequence;
		}
		return sequence;
	}
}
