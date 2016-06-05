package com.ayronasystems.core.definition.commission;

import com.ayronasystems.core.Position;

public class ConstantSpreadCommission implements Commission {
	private double spread;

	public ConstantSpreadCommission(double spread) {
		super();
		this.spread = spread;
	}

	public double calculate(Position position) {
		return spread;
	}

	public CommissionType getCommissionType () {
		return CommissionType.CONSTANT_SPREAD;
	}

	public double getCommission () {
		return spread;
	}

}
