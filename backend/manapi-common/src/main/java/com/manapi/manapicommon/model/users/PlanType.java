package com.manapi.manapicommon.model.users;

import java.time.Duration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

public enum PlanType {

	FREE,
	PREMIUM,
	ENTERPRISE;

	/** Determine rate limit to Bucket4j */
	public Bandwidth getLimit() {

		Long capacity;
		switch (name()) {
			case "PREMIUM":
				capacity = 10L;
				break;
			case "ENTERPRISE":
				capacity = 100L;
				break;
			default:
				capacity = 5L;
				break;
		}

		return Bandwidth.classic(capacity, Refill.intervally(50, Duration.ofMinutes(1)));
	}

}
