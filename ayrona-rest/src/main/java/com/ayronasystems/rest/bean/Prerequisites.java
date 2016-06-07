package com.ayronasystems.rest.bean;

/**
 * Created by gorkemgok on 06/06/16.
 */
public class Prerequisites {

    public static final BeanPrerequisite<AccountBean> SAVE_ACCOUNT = new BeanPrerequisite<AccountBean> () {
        public PrerequisiteBean check (AccountBean accountBean) {
            PrerequisiteBean prerequisiteBean = new PrerequisiteBean ();
            if (accountBean.getName () == null || accountBean.getName ().isEmpty ()){
                prerequisiteBean.shouldBe ("name", PrerequisiteBean.ShouldBe.NOT_EMPTY);
            }
            return prerequisiteBean;
        }
    };

}
