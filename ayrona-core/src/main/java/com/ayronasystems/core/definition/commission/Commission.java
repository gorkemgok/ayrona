package com.ayronasystems.core.definition.commission;


import com.ayronasystems.core.strategy.Position;

public interface Commission {
	double calculate (Position position);
	CommissionType getCommissionType ();
	double getCommission ();
}
