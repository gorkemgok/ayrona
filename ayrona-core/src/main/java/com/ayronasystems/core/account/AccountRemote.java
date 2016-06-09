package com.ayronasystems.core.account;

import com.ayronasystems.core.Position;

import java.util.Date;

/**
 * Created by gorkemg on 09.06.2016.
 */
public interface AccountRemote {

    public AccountRemoteResponse openPosition(Position position);

    public AccountRemoteResponse closePosition (Position position, Date closeDate, double closePrice);

}
