package com.ayronasystems.core.dao.mysql;

import com.ayronasystems.core.configuration.ConfKey;
import com.ayronasystems.core.dao.mysql.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by gorkemgok on 29/12/15.
 */
public class Database {

    private static Logger log = LoggerFactory.getLogger (Database.class);

    private static com.ayronasystems.core.configuration.Configuration conf = com.ayronasystems.core.configuration.Configuration.getInstance ();

    private static Database tick4JDb = null;

    private static Database tick4JRateDb = null;

    public static Database getTickRateDB () throws IOException {
        if ( tick4JRateDb == null ) {
            tick4JRateDb = new Database (
                    conf.getConfDir ()+File.separator+conf.getString (ConfKey.RATEDB_PROPERTIES_FILE),
                    BarModel.class
            );
        }
        return tick4JRateDb;
    }

    public static Database getTick4jDB () throws IOException {
        if ( tick4JDb == null ) {
            tick4JDb = new Database (
                    conf.getConfDir ()+File.separator+conf.getString (ConfKey.DB_PROPERTIES_FILE),
                    AccountModel.class,
                    AccountStrategyPairModel.class,
                    StrategyModel.class,
                    StrategyOptionsModel.class,
                    StrategySessionModel.class,
                    SummaryModel.class,
                    ResultModel.class,
                    UserModel.class
            );
        }
        return tick4JDb;
    }

    private SessionFactory sessionFactory;

    public Database (String propertyFileName, Class... classes) throws IOException {
       log.info ("Creating SessionFactory with property file : "+propertyFileName);
       Properties properties = new Properties ();
       properties.load (new FileInputStream (propertyFileName));
       Configuration configuration = new Configuration ();
       configuration.setProperties (properties);
       for ( Class clazz : classes ) {
           configuration.addAnnotatedClass (clazz);
       }
       StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
       sessionFactory = configuration.buildSessionFactory (standardServiceRegistryBuilder.build());
       log.info ("Created SessionFactory with property file : "+propertyFileName);
    }

    public SessionFactory getSessionFactory(){
        return sessionFactory;
    }

    public void shutDown(){
        getSessionFactory().close();
    }
}
