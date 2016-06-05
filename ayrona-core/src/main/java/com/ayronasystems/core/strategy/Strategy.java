package com.ayronasystems.core.strategy;

import com.ayronasystems.core.Initiator;
import com.ayronasystems.core.account.Account;
import com.ayronasystems.core.account.AccountBindInfo;
import com.ayronasystems.core.exception.PrerequisiteException;

import java.util.List;

/**
 * Created by gorkemgok on 12/05/16.
 */
public interface Strategy<E> extends Initiator {

    void process(E exchange) throws PrerequisiteException;

    List<AccountBindInfo> getAccountBindInfoList();
}
