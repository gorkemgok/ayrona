package com.ayronasystems.rest.bean;

/**
 * Created by gorkemgok on 06/06/16.
 */
public class Prerequisites {

    public static final BeanPrerequisite<AccountBean> SAVE_ACCOUNT = new BeanPrerequisite<AccountBean> () {
        public PrerequisiteCheck check (AccountBean accountBean) {
            PrerequisiteCheck prerequisiteCheck = new PrerequisiteCheck ();
            if (accountBean.getName () == null || accountBean.getName ().isEmpty ()){
                prerequisiteCheck.shouldBe ("name", PrerequisiteCheck.ShouldBe.NOT_EMPTY);
            }
            if (accountBean.getType () == null){
                prerequisiteCheck.shouldBe ("type", PrerequisiteCheck.ShouldBe.SELECTED);
            }
            if (accountBean.getAccountNo () == null || accountBean.getAccountNo ().isEmpty ()){
                prerequisiteCheck.shouldBe ("accountNo", PrerequisiteCheck.ShouldBe.NOT_EMPTY);
            }
            return prerequisiteCheck;
        }
    };

    public static final BeanPrerequisite<StrategyBean> SAVE_STRATEGY = new BeanPrerequisite<StrategyBean> () {
        public PrerequisiteCheck check (StrategyBean strategyBean) {
            PrerequisiteCheck prerequisiteCheck = new PrerequisiteCheck ();
            if (strategyBean.getName () == null || strategyBean.getName ().isEmpty ()){
                prerequisiteCheck.shouldBe ("name", PrerequisiteCheck.ShouldBe.NOT_EMPTY);
            }
            if (strategyBean.getCode () == null || strategyBean.getCode ().isEmpty ()){
                prerequisiteCheck.shouldBe ("code", PrerequisiteCheck.ShouldBe.NOT_EMPTY);
            }
            if (strategyBean.getSymbol () == null){
                prerequisiteCheck.shouldBe ("symbol", PrerequisiteCheck.ShouldBe.SELECTED);
            }
            if (strategyBean.getPeriod () == null){
                prerequisiteCheck.shouldBe ("period", PrerequisiteCheck.ShouldBe.SELECTED);
            }
            return prerequisiteCheck;
        }
    };

}
