package com.ayronasystems.core.strategy;

import com.ayronasystems.core.account.AccountBinder;
import com.ayronasystems.core.concurrent.QueueRunnable;

import java.util.List;

/**
 * Created by gorkemgok on 12/05/16.
 */
public interface Strategy<E> extends Initiator, QueueRunnable<E> {

    void process(E exchange);

    List<AccountBinder> getAccountBinderList ();

    void registerAccount(AccountBinder accountBindInfo);

    void deregisterAccount(String accountId);
}
