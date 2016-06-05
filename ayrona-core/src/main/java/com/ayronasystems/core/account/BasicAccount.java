package com.ayronasystems.core.account;

import com.ayronasystems.core.Initiator;
import com.ayronasystems.core.Position;
import com.ayronasystems.core.definition.Symbol;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 18/05/16.
 */
public class BasicAccount implements Account {

    private List<Position> positionList = new ArrayList<Position> ();

    private List<Position> openPositionList = new ArrayList<Position> ();

    public boolean openPosition (Position position) {
        positionList.add (position);
        openPositionList.add (position);
        position.setIdealOpenDate (position.getOpenDate ());
        position.setIdealOpenPrice (position.getOpenPrice ());
        return true;
    }

    public boolean closePosition (Position position, Date closeDate, double closePrice) {
        position.close (closeDate, closePrice);
        position.setIdealCloseDate (closeDate);
        position.setIdealClosePrice (closePrice);
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
        return positionList;
    }
}
