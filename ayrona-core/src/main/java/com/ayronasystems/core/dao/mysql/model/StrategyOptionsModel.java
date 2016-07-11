package com.ayronasystems.core.dao.mysql.model;

import com.ayronasystems.core.backtest.MetricType;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.definition.Symbols;
import com.ayronasystems.core.definition.commission.Commission;
import com.ayronasystems.core.definition.commission.CommissionType;
import com.ayronasystems.core.definition.commission.ConstantSpreadCommission;
import com.ayronasystems.core.definition.commission.PercentCommission;
import com.ayronasystems.core.service.SymbolSeriesManager;
import com.ayronasystems.core.strategy.StrategyOptions;
import com.ayronasystems.core.timeseries.moment.Bar;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by gorkemgok on 30/09/15.
 */
//TODO: Get rid of this class
@XmlRootElement
@Entity
@Table(name="strategy_options")
public class StrategyOptionsModel extends BaseModel{

    private Symbol symbol;

    private Period period;

    private CommissionType commissionType;

    private double commission;

    private boolean twoWayPositionsAllowed;

    private boolean processHoldAsOpposite;

    private boolean processBothAsOpposite;

    private MetricType fitnessMetric;

    private Interval trainingInterval;

    private Interval testInterval;

    public StrategyOptionsModel (Symbol symbol, Period period, Commission commission, boolean twoWayPositionsAllowed, boolean processHoldAsOpposite, boolean processBothAsOpposite, MetricType fitnessMetric, Interval trainingInterval, Interval testInterval) {
        this.symbol = symbol;
        this.period = period;
        this.commissionType = commission.getCommissionType ();
        this.commission = commission.getCommission ();
        this.twoWayPositionsAllowed = twoWayPositionsAllowed;
        this.processHoldAsOpposite = processHoldAsOpposite;
        this.processBothAsOpposite = processBothAsOpposite;
        this.fitnessMetric = fitnessMetric;
        this.trainingInterval = trainingInterval;
        this.testInterval = testInterval;
    }

    public StrategyOptionsModel(){

    }

    @Enumerated(EnumType.STRING)
    @Column(name="symbol", nullable = false)
    public Symbol getSymbol () {
        return symbol;
    }

    public void setSymbol (Symbol symbol) {
        this.symbol = symbol;
    }

    @Enumerated(EnumType.STRING)
    @Column(name="period", nullable = false)
    public Period getPeriod () {
        return period;
    }

    public void setPeriod (Period period) {
        this.period = period;
    }

    @Enumerated(EnumType.STRING)
    @Column(name="commission_type", nullable = false)
    public CommissionType getCommissionType () {
        return commissionType;
    }

    public void setCommissionType (CommissionType commissionType) {
        this.commissionType = commissionType;
    }

    @Column(name="commission", nullable = false)
    public double getCommission () {
        return commission;
    }

    public void setCommission (double commission) {
        this.commission = commission;
    }

    @Column(name="is_two_position_allowed", nullable = false)
    public boolean isTwoWayPositionsAllowed () {
        return twoWayPositionsAllowed;
    }

    public void setTwoWayPositionsAllowed (boolean twoWayPositionsAllowed) {
        this.twoWayPositionsAllowed = twoWayPositionsAllowed;
    }

    @Column(name="is_process_hold_as_opposite", nullable = false)
    public boolean isProcessHoldAsOpposite () {
        return processHoldAsOpposite;
    }

    public void setProcessHoldAsOpposite (boolean processHoldAsOpposite) {
        this.processHoldAsOpposite = processHoldAsOpposite;
    }

    @Column(name="is_process_both_as_opposite", nullable = false)
    public boolean isProcessBothAsOpposite () {
        return processBothAsOpposite;
    }

    public void setProcessBothAsOpposite (boolean processBothAsOpposite) {
        this.processBothAsOpposite = processBothAsOpposite;
    }

    @Enumerated(EnumType.STRING)
    @Column(name="fitness_metric", nullable = false)
    public MetricType getFitnessMetric () {
        return fitnessMetric;
    }

    public void setFitnessMetric (MetricType fitnessMetric) {
        this.fitnessMetric = fitnessMetric;
    }

    @Columns(columns={@Column(name="trainig_start_interval"),@Column(name="trainig_end_interval")})
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentInterval")
    public Interval getTrainingInterval () {
        return trainingInterval;
    }

    public void setTrainingInterval (Interval trainingInterval) {
        this.trainingInterval = trainingInterval;
    }

    @Columns(columns={@Column(name="test_start_interval"),@Column(name="test_end_interval")})
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentInterval")
    public Interval getTestInterval () {
        return testInterval;
    }

    public void setTestInterval (Interval testInterval) {
        this.testInterval = testInterval;
    }

    public static StrategyOptionsModel valueOf (StrategyOptions strategyOptions){
        return new StrategyOptionsModel (
                strategyOptions.getBaseSeries ()
                               .getSymbol (),
                strategyOptions.getBaseSeries ()
                               .getPeriod (),
                strategyOptions.getCommission (),
                strategyOptions.isTwoWayPositionsAllowed (),
                strategyOptions.isProcessHoldAsOpposite (),
                strategyOptions.isProcessBothAsOpposite (),
                strategyOptions.getFitnessMetric (),
                strategyOptions.getTrainingInterval (),
                strategyOptions.getTestInterval ()

        );
    }

    public StrategyOptions toStrategyOptions(){
        return toStrategyOptions (this.getTrainingInterval ().getStart ().toDate (),
                                  this.getTestInterval ().getEnd ().toDate ());
    }

    public StrategyOptions toStrategyOptionsFromNow(){
        //TODO : Zaman araligindan serie olusturmaya bir cosum bul
        DateTime dateTime = new DateTime ().withTime (0, 0, 0, 0);
        return toStrategyOptions (this.getTrainingInterval ().getEnd ().toDate (),
                                  dateTime.toDate ());
    }

    public StrategyOptions toStrategyOptions(Date baseSeriesBeginDate, Date baseSeriesEndDate){
        Commission commission;
        CommissionType commissionType = this.getCommissionType ();
        switch ( commissionType ){
            case CONSTANT_SPREAD:
                commission = new ConstantSpreadCommission (this.getCommission ());
                break;
            case PERCENTAGE:
                commission = new PercentCommission (this.getCommission ());
                break;
            case PERCENTAGE_WITH_DOUBLE_FEE:
                commission = new PercentCommission (this.getCommission (), true);
                break;
            default:
                commission = new ConstantSpreadCommission (this.getCommission ());
        }

        StrategyOptions strategyOptions = null;
        strategyOptions = new StrategyOptions.Builder ()
                .baseSeries (SymbolSeriesManager.getManager (Bar.class)
                                                .get (Symbols.of ("TEST"), Period.M5,
                                                      baseSeriesBeginDate, baseSeriesEndDate))
                .commission (commission)
                .trainingInterval (this.getTrainingInterval ())
                .testInterval (this.getTestInterval ())
                .isTwoWayPositionAllowed (this.isTwoWayPositionsAllowed ())
                .processHoldAsOpposite (this.isProcessHoldAsOpposite ())
                .processBothAsOpposite (this.isProcessBothAsOpposite ())
                .fitnessMetric (this.getFitnessMetric ())
                .build ();

        return strategyOptions;
    }
}
