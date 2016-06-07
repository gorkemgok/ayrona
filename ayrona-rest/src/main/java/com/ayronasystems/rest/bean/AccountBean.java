package com.ayronasystems.rest.bean;

import com.ayronasystems.core.dao.model.AccountModel;
import com.ayronasystems.core.dao.model.LoginDetail;
import org.bson.types.ObjectId;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by gorkemgok on 06/06/16.
 */
@XmlRootElement
public class AccountBean {

    private String id;

    private String name;

    private String type;

    private String accountNo;

    private String accountPassword;

    private String server;

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public String getAccountNo () {
        return accountNo;
    }

    public void setAccountNo (String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountPassword () {
        return accountPassword;
    }

    public void setAccountPassword (String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public String getServer () {
        return server;
    }

    public void setServer (String server) {
        this.server = server;
    }

    public static AccountBean valueOf(AccountModel accountModel){
        AccountBean accountBean = new AccountBean ();
        accountBean.setId (accountModel.getId ());
        accountBean.setName (accountModel.getAccountantName ());
        accountBean.setType (accountModel.getType ().toString ());
        accountBean.setAccountNo (accountModel.getLoginDetail ().getId ());
        accountBean.setAccountPassword (accountModel.getLoginDetail ().getPassword ());
        accountBean.setServer (accountModel.getLoginDetail ().getServer ());
        return accountBean;
    }

    public AccountModel toAccountModel(){
        AccountModel accountModel = new AccountModel ();
        accountModel.setId (new ObjectId (id));
        accountModel.setType (AccountModel.Type.valueOf (type));
        LoginDetail loginDetail = new LoginDetail ();
        loginDetail.setId (accountNo);
        loginDetail.setPassword (accountPassword);
        loginDetail.setServer (server);
        accountModel.setLoginDetail (loginDetail);
        return accountModel;
    }
}
