package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.service.SequenceServiceImp;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.repo.ProductRepo;
import com.axelor.gst.service.SequenceService;

public class GstModul  extends AxelorModule{
	@Override
	protected void configure() {
		bind(SequenceService.class).to(SequenceServiceImp.class);
		bind(SequenceRepository.class);
		bind(ProductRepository.class).to(ProductRepo.class);
	}
}
