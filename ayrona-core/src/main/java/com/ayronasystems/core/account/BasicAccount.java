package com.ayronasystems.core.account;

import com.ayronasystems.core.strategy.Initiator;
import com.ayronasystems.core.Position;
import com.ayronasystems.core.dao.model.AccountModel;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.integration.mt4.MT4AccountRemote;
import com.ayronasystems.core.integration.mt4.MT4Connection;
import com.ayronasystems.core.integration.mt4.MT4ConnectionPool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 18/05/16.
 */
public class BasicAccount implements Account {

    private String id;

    private List<Position> positionList = new ArrayList<Position> ();

    private List<Position> openPositionList = new ArrayList<Position> ();

    private AccountRemote accountRemote;

    public BasicAccount (String id, AccountRemote accountRemote) {
        this.id = id;
        this.accountRemote = accountRemote;
    }

    public BasicAccount (String id) {
        this.id = id;
        this.accountRemote = NoOpAccountRemote.INSTANCE;
    }

    public BasicAccount () {
        id = "SIMULATION";
        accountRemote = NoOpAccountRemote.INSTANCE;
    }

    public synchronized boolean openPosition (Position position) {
        positionList.add (position);
        openPositionList.add (position);
        position.setIdealOpenDate (position.getOpenDate ());
        position.setIdealOpenPrice (position.getOpenPrice ());
        return true;
    }

    public synchronized boolean closePosition (Position position, Date closeDate, double closePrice) {
        position.close (closeDate, closePrice);
        position.setIdealCloseDate (closeDate);
        position.setIdealClosePrice (closePrice);
        openPositionList.remove (position);
        return true;
    }

    public synchronized List<Position> getOpenPositions (Symbol symbol, Initiator initiator) {
        List<Position> foundPositionList = new ArrayList<Position> ();
        for ( Position position : openPositionList ) {
            if (!position.isClosed () && position.getSymbol ().equals (symbol) && position.getInitiator ().isSameInitiator (initiator)){
                foundPositionList.add (position);
            }
        }
        return foundPositionList;
    }

    public synchronized List<Position> getPositions () {
        return new ArrayList<Position> (positionList);
    }

    public String getId () {
        return id;
    }

    //TODO : take this from here
    public static AccountRemote createUsing(AccountModel accountModel){
        MT4ConnectionPool mt4ConnectionPool = MT4ConnectionPool.getInstance();
        if (accountModel.getType() == AccountModel.Type.MT4){
            MT4Connection mt4Connection = mt4ConnectionPool.getConnection(
                    accountModel.getLoginDetail().getServer(),
                    accountModel.getLoginDetail().getId(),
                    accountModel.getLoginDetail().getPassword()
            );
            return new MT4AccountRemote(mt4Connection);
        }
        return NoOpAccountRemote.INSTANCE;
    }
}
