package com.ayronasystems.core.strategy.concurrent;

import com.ayronasystems.core.BasicInitiator;
import com.ayronasystems.core.Order;
import com.ayronasystems.core.Position;
import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.account.Account;
import com.ayronasystems.core.account.AccountBindInfo;
import com.ayronasystems.core.account.BasicAccount;
import com.ayronasystems.core.configuration.ConfigurationConstants;
import com.ayronasystems.core.dao.mongo.MongoDaoTestITCase;
import com.ayronasystems.core.definition.Direction;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.strategy.Initiator;
import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

/**
 * Created by gorkemg on 10.06.2016.
 */
public class RunnableOrderHandlerTest {

    public static Order[] orderArray = new Order[]{
            Order.builder().date(new Date()).direction(Direction.LONG).order(Order.Type.OPEN).price(5).symbol(Symbol.VOB30).description("test").build(),
            Order.builder().date(new Date()).direction(Direction.LONG).order(Order.Type.CLOSE).price(5).symbol(Symbol.VOB30).description("test").build(),
            Order.builder().date(new Date()).direction(Direction.SHORT).order(Order.Type.OPEN).price(5).symbol(Symbol.VOB30).description("test").build(),
            Order.builder().date(new Date()).direction(Direction.LONG).order(Order.Type.CLOSE).price(5).symbol(Symbol.VOB30).description("test").build(),
            Order.builder().date(new Date()).direction(Direction.SHORT).order(Order.Type.OPEN).price(5).symbol(Symbol.VOB30).description("test").build(),
            Order.builder().date(new Date()).direction(Direction.LONG).order(Order.Type.CLOSE).price(5).symbol(Symbol.VOB30).description("test").build(),
            Order.builder().date(new Date()).direction(Direction.SHORT).order(Order.Type.OPEN).price(5).symbol(Symbol.VOB30).description("test").build()
    };

    private List<Order> orderList;

    private List<AccountBindInfo> accountBindInfoList;

    private Initiator initiator;

    private ExecutorService executor;

    @Before
    public void setUp() throws Exception {
        System.setProperty (ConfigurationConstants.PROP_MONGODB_DS, MongoDaoTestITCase.AYRONA_TEST_DB_NAME);

        MongoClient mongoClient = Singletons.INSTANCE.getMongoClient ();
        mongoClient.dropDatabase (MongoDaoTestITCase.AYRONA_TEST_DB_NAME);

        orderList = Arrays.asList(orderArray);
        Account account = new BasicAccount("test");
        Account account2 = new BasicAccount("test2");
        accountBindInfoList = Arrays.asList(new AccountBindInfo[]{
            new AccountBindInfo(account, 1),
            new AccountBindInfo(account2, 2)
        });
        initiator = new BasicInitiator ("TEST1", "TEST1");

        executor = Executors.newFixedThreadPool(10);
    }

    @Test
    public void run() throws Exception {
        for (AccountBindInfo accountBindInfo : accountBindInfoList){
            RunnableOrderHandler runnableOrderHandler = new RunnableOrderHandler(orderList, initiator, accountBindInfo, 0, 0);
            executor.submit(runnableOrderHandler);
        }

        Thread.currentThread().sleep(1000);

        List<Position> positionList1 = accountBindInfoList.get(0).getAccount().getPositions();
        List<Position> positionList2 = accountBindInfoList.get(1).getAccount().getPositions();

        assertEquals(positionList1.size(), positionList2.size());
        assertEquals(4, positionList1.size());
    }

}