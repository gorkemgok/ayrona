package com.ayronasystems.core.strategy;

import com.ayronasystems.core.account.AccountBindInfo;
import com.ayronasystems.core.concurrent.QueueRunnable;

import java.util.List;

/**
 * Created by gorkemgok on 12/05/16.
 */
public interface Strategy<E> extends Initiator, QueueRunnable<E> {

    void process(E exchange);

    List<AccountBindInfo> getAccountBindInfoList();
}
