package com.email.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class HrDetailsList {
	
	@JsonProperty("hrDetails")
	@NotNull(message = "hrDetails cannot be null")
	@NotEmpty(message = "hrDetails cannot be empty")
	@Valid
	private List<HrDetailsRequest> hrDetails;

	public List<HrDetailsRequest> getHrDetails() {
		return hrDetails;
	}

	public void setHrDetails(List<HrDetailsRequest> hrDetails) {
		this.hrDetails = hrDetails;
	}

	@Override
	public String toString() {
		return "HrDetailsList{" +
				"hrDetails=" + (hrDetails != null ? hrDetails.size() + " items" : "null") +
				'}';
	}
}
