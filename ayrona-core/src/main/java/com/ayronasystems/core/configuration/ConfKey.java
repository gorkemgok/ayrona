package com.ayronasystems.core.configuration;

import java.io.File;

import static com.ayronasystems.core.configuration.ConfigurationConstants.*;

public enum ConfKey {

    HOST (PROP_HOST, "localhost"),

    SERVICE_LIST (PROP_SERVICES, "AOS=y,GS=y,LDS=y,BTS=y"),

    SERVICE_GS_URL (PROP_REST_SERVICE_GS_URL, "http://localhost:8080/ayrona-service/rest/"),

    SERVICE_AOS_URL (PROP_REST_SERVICE_AOS_URL, "http://localhost:8080/ayrona-service/rest/"),

    SERVICE_DS_URL (PROP_REST_SERVICE_DS_URL, "http://localhost:8080/ayrona-service/rest/"),

    SERVICE_BTS_URL (PROP_REST_SERVICE_BTS_URL, "http://localhost:8080/ayrona-service/rest/"),

    LORD_OF_THE_TOKEN (PROP_LORD_OF_THE_TOKEN, null),

    CSV_DIR (PROP_CSV_DIR, System.getProperty ("user.dir")+"/hist"),

    CONFIG_DIR (PROP_CONFIG_DIR,
                System.getProperty(JBOSS_SERVER_CONFIG_DIR)

    +File.separator),

    JFX_HOST (PROP_JFX_HOST, "localhost"),

    JFX_PORT (PROP_JFX_PORT, "7790"),

    DB_PROPERTIES_FILE (PROP_DB_PROPERTIESFILE,
                        TICK4J_HIBERNATE_DB_PROPERTIES_FILE_NAME),

    RATEDB_PROPERTIES_FILE (PROP_RATEDB_PROPERTIESFILE,
                            TICK4J_HIBERNATE_RATEDB_PROPERTIES_FILE_NAME),

    LDS_TERMINAL_HOST (PROP_LDS_TERMINAL_HOST, "localhost"),

    LDS_TERMINAL_PORT (PROP_LDS_TERMINAL_PORT, "7788"),

    LDS_TERMINAL_BROKER (PROP_LDS_TERMINAL_BROKER, "AtaOnline-Demo"),

    LDS_TERMINAL_USER_NAME (PROP_LDS_TERMINAL_USER_NAME, "1218368283"),

    LDS_TERMINAL_USER_PASSWORD (PROP_LDS_TERMINAL_USER_PASSWORD, "gm7xtnn"),

    LDS_AMQ_URI (PROP_LDS_AMQ_URI, "tcp://localhost:61616"),

    AOS_AMQ_URI (PROP_AOS_AMQ_URI, "tcp://localhost:61616"),

    AOS_TERMINAL_HOST (PROP_AOS_TERMINAL_HOST, "localhost"),

    AOS_TERMINAL_PORT (PROP_AOS_TERMINAL_PORT, "7788"),

    GS_AMQ_URI (PROP_GS_AMQ_URI, "tcp://localhost:61616"),

    GS_THREAD_COUNT (PROP_GS_THREAD_COUNT, "2"),

    BTS_AMQ_URI (PROP_BTS_AMQ_URI, "tcp://localhost:61616"),

    CONSUL_HOST (PROP_CONSUL_HOST, ENV_CONSUL_HOST, "localhost"),

    CONSUL_PORT (PROP_CONSUL_PORT, ENV_CONSUL_PORT, "8500"),

    MONGODB_HOST (PROP_MONGODB_HOST, ENV_MONGODB_HOST, "localhost"),

    MONGODB_PORT (PROP_MONGODB_PORT, ENV_MONGODB_PORT, "27017")

    ;

    private String name;

    private String envName;

    private String defaultValue;

    ConfKey (String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.envName = null;
    }

    ConfKey (String name, String envName, String defaultValue) {
        this.name = name;
        this.envName = envName;
        this.defaultValue = defaultValue;
    }

    public String getName () {
        return name;
    }

    public String getDefaultValue () {
        return defaultValue;
    }

    public String getEnvName () {
        return envName;
    }

    public boolean hasEnvName(){
        return envName != null;
    }
}