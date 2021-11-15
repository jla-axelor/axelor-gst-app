package com.axelor.gst.web;

import java.util.List;
import java.util.stream.Collectors;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.City;
import com.axelor.gst.db.Country;
import com.axelor.gst.db.State;
import com.axelor.gst.db.repo.CountryRepository;
import com.axelor.gst.db.repo.StateRepository;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class AddressContoller {
	public void setStateAndCountry(ActionRequest req,ActionResponse res) {
		Address address = req.getContext().asType(Address.class);
		City city = address.getCity();
		List<State> state = Beans.get(StateRepository.class).all().fetch().stream().filter(i->i.getName().equals(city.getState())).collect(Collectors.toList());
		List<Country> country = Beans.get(CountryRepository.class).all().fetch().stream().filter(i->i.getName().equals(city.getCountry())).collect(Collectors.toList());
		if (!state.isEmpty() && !country.isEmpty()) {
			res.setValue("state", state.get(0));
			res.setValue("country", country.get(0));
		}
		else {
			res.setError("State or Country are not available in configuration");
		}
	}
}
