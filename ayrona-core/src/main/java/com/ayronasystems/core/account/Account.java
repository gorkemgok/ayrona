package com.ayronasystems.core.account;

import com.ayronasystems.core.Initiator;
import com.ayronasystems.core.Position;
import com.ayronasystems.core.definition.Symbol;

import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 12/05/16.
 */
public interface Account {

    boolean openPosition (Position position);

    boolean closePosition (Position position, Date closeDate, double closePrice);

    List<Position> getOpenPositions(Symbol symbol, Initiator initiator);

    List<Position> getPositions();
}
