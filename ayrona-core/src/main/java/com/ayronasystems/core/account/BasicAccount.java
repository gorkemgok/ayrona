package com.ayronasystems.core.account;

import com.ayronasystems.core.Position;
import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.AccountModel;
import com.ayronasystems.core.dao.model.PositionModel;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.definition.TradeOperationResult;
import com.ayronasystems.core.integration.mt4.MT4AccountRemote;
import com.ayronasystems.core.integration.mt4.MT4Connection;
import com.ayronasystems.core.integration.mt4.MT4ConnectionPool;
import com.ayronasystems.core.strategy.Initiator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 18/05/16.
 */
public class BasicAccount implements Account {

    private String id;

    private String name;

    private List<Position> positionList = new ArrayList<Position> ();

    private List<Position> openPositionList = new ArrayList<Position> ();

    private AccountRemote accountRemote;

    private Dao dao = Singletons.INSTANCE.getDao ();

    public BasicAccount (String id, String name,  AccountRemote accountRemote) {
        this.id = id;
        this.name = name;
        this.accountRemote = accountRemote;
        List<PositionModel> positionModelList = dao.findOpenPositionsByAccountId (id);
        for (PositionModel positionModel : positionModelList){
            Position position = positionModel.toPosition ();
            positionList.add (position);
            openPositionList.add (position);
        }
    }

    public BasicAccount (String id) {
        this.id = id;
        this.name = id;
        this.accountRemote = NoOpAccountRemote.INSTANCE;
    }

    public BasicAccount () {
        id = "DUMMY";
        name = "DUMMY";
        accountRemote = NoOpAccountRemote.INSTANCE;
    }

    public boolean openPosition (Position position) {
        AccountRemoteResponse response = accountRemote.openPosition (position);
        if (response.getTradeOperationResult () == TradeOperationResult.SUCCESSFUL) {
            PositionModel positionModel = dao.createPosition (PositionModel.valueOf (position, this));
            synchronized (this) {
                position.setId (positionModel.getId ());
                positionList.add (position);
                openPositionList.add (position);
            }
            return true;
        }
        return false;
    }

    public boolean closePosition (Position position, Date closeDate, double closePrice) {
        AccountRemoteResponse response = accountRemote.closePosition (position, closeDate, closePrice);
        if (response.getTradeOperationResult () == TradeOperationResult.SUCCESSFUL) {
            synchronized (this) {
                openPositionList.remove (position);
            }
            dao.closePosition (PositionModel.valueOf (position, this));
            return true;
        }
        return false;
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

    public String getName () {
        return name;
    }

    //TODO : take this from here
    public static AccountRemote createAccountRemoteUsing (AccountModel accountModel){
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
