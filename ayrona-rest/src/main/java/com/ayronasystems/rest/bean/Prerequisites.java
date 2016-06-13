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

}
