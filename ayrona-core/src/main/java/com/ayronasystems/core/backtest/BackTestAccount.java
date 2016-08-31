package com.ayronasystems.core.backtest;

import com.ayronasystems.core.account.Account;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.strategy.Initiator;
import com.ayronasystems.core.strategy.Position;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 29/08/16.
 */
public class BackTestAccount implements Account{

    private List<Position> positionList = new ArrayList<Position> ();

    private List<Position> openPositionList = new ArrayList<Position> ();

    public String getId () {
        return "BACKTEST";
    }

    public String getName () {
        return "BACKTEST";
    }

    public boolean openPosition (Position position) {
        position.setId ("");
        position.setOpenDate (position.getIdealOpenDate ());
        position.setOpenPrice (position.getIdealOpenPrice ());
        positionList.add (position);
        openPositionList.add (position);
        return true;
    }

    public boolean closePosition (Position position, Date closeDate, double closePrice) {
        position.close (closeDate, closePrice);
        position.setCloseDate (position.getIdealCloseDate ());
        position.setClosePrice (position.getIdealClosePrice ());
        openPositionList.remove (position);
        return true;
    }

    public List<Position> getOpenPositions (Symbol symbol, Initiator initiator) {
        List<Position> foundPositionList = new ArrayList<Position> ();
        for ( Position position : openPositionList ) {
            if (!position.isClosed () && position.getSymbol ().equals (symbol) && position.getInitiator ().isSameInitiator (initiator)){
                foundPositionList.add (position);
            }
        }
        return foundPositionList;
    }

    public List<Position> getPositions () {
        return new ArrayList<Position> (positionList);
    }
}
