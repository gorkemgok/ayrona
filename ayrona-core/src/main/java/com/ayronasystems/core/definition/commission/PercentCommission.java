package com.ayronasystems.core.definition.commission;

import com.ayronasystems.core.Position;

public class PercentCommission implements Commission {

    public static final boolean CHARGE_DOUBLE_COMMISSION = true;

    public static final boolean DONT_CHARGE_DOUBLE_COMMISSION = true;

	private double percent;

    private boolean chargeDoubleFee = false;

	public PercentCommission(double percent) {
		super();
		this.percent = percent;
	}

    public PercentCommission(double percent, boolean chargeDoubleFee) {
        this.percent = percent;
        this.chargeDoubleFee = chargeDoubleFee;
    }

    public double calculate(Position position) {
        if (chargeDoubleFee){
            return (position.getClosePrice() + position.getOpenPrice())* percent / 100;
        }else{
		    return position.getClosePrice() * percent / 100;
        }
	}

    public CommissionType getCommissionType () {
        if (chargeDoubleFee){
            return CommissionType.PERCENTAGE_WITH_DOUBLE_FEE;
        }
        return CommissionType.PERCENTAGE;
    }

    public double getCommission () {
        return percent;
    }

}
