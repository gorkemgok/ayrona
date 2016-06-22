package com.ayronasystems.core.configuration;

import java.io.File;

import static com.ayronasystems.core.configuration.ConfigurationConstants.*;

public enum ConfKey {

    CSV_DIR (PROP_CSV_DIR, System.getProperty ("user.dir")+"/hist"),

    CONFIG_DIR (PROP_CONFIG_DIR, System.getProperty(JBOSS_SERVER_CONFIG_DIR) + File.separator),

    DB_PROPERTIES_FILE (PROP_DB_PROPERTIESFILE, TICK4J_HIBERNATE_DB_PROPERTIES_FILE_NAME),

    RATEDB_PROPERTIES_FILE (PROP_RATEDB_PROPERTIESFILE, TICK4J_HIBERNATE_RATEDB_PROPERTIES_FILE_NAME),

    HOST (PROP_HOST, ENV_HOST, "localhost"),

    SERVICE_LIST (PROP_SERVICES, "AOS=y,GS=y,LDS=y,BTS=y"),

    SERVICE_ATE_URL (PROP_REST_SERVICE_ATE_URL, "http://localhost:8080/ayrona-ate/rest/v1"),

    SERVICE_DS_URL (PROP_REST_SERVICE_DS_URL, "http://localhost:8080/ayrona-ds/rest/v1"),

    SERVICE_BTE_URL (PROP_REST_SERVICE_BTE_URL, "http://localhost:8080/ayrona-bte/rest/v1"),

    LORD_OF_THE_TOKEN (PROP_LORD_OF_THE_TOKEN, LORDOFTHETOKEN),

    MT4_JFX_AK (PROP_MT4_JFX_AK, ENV_MT4_JFX_AK, "578623472"),

    MT4_JFX_HOST (PROP_MT4_JFX_HOST, ENV_MT4_JFX_HOST, "localhost"),

    MT4_JFX_PORT (PROP_MT4_JFX_PORT, ENV_MT4_JFX_PORT, "7790"),

    MT4_TERMINAL_HOST (PROP_MT4_TERMINAL_HOST, ENV_MT4_TERMINAL_HOST, "localhost"),

    MT4_TERMINAL_PORT (PROP_MT4_TERMINAL_PORT, ENV_MT4_TERMINAL_PORT, "7788"),

    MT4_DS_LISTENER_BROKER (PROP_MT4_DS_LISTENER_BROKER, ENV_MT4_DS_LISTENER_BROKER, "AtaOnline-Demo"),

    MT4_DS_LISTENER_LOGIN (PROP_MT4_DS_LISTENER_LOGIN, ENV_MT4_DS_LISTENER_LOGIN, "1218368283"),

    MT4_DS_LISTENER_PASSWORD (PROP_MT4_DS_LISTENER_PASSWORD, ENV_MT4_DS_LISTENER_PASSWORD, "gm7xtnn"),

    AMQ_URI (PROP_AMQ_URI, ENV_AMQ_URI, "tcp://localhost:61616"),

    CONSUL_HOST (PROP_CONSUL_HOST, ENV_CONSUL_HOST, "localhost"),

    CONSUL_PORT (PROP_CONSUL_PORT, ENV_CONSUL_PORT, "8500"),

    MONGODB_HOST (PROP_MONGODB_HOST, ENV_MONGODB_HOST, "localhost"),

    MONGODB_PORT (PROP_MONGODB_PORT, ENV_MONGODB_PORT, "27017"),

    MONGODB_DS (PROP_MONGODB_DS, ENV_MONGODB_DS, "ayrona"),

    MONGODB_MDS (PROP_MONGODB_MDS, ENV_MONGODB_MDS, "ayrona_marketdata"),

    ATE_REST_URL(PROP_ATE_REST_URL, ENV_ATE_REST_URL, "http://localhost:8051/ayrona-ate/rest/v1"),

    ATE_REST_TOKEN (PROP_ATE_REST_TOKEN, ENV_ATE_REST_TOKEN, LORDOFTHETOKEN),

    ATA_GTP_HOST (PROP_ATA_GTP_HOST, ENV_ATA_GTP_HOST, "172.16.192.40"),

    ATA_DATA_IP (PROP_ATA_DATA_IP, ENV_ATA_DATA_IP, "172.16.192.40"),

    ATA_DATA_PORT (PROP_ATA_DATA_PORT, ENV_ATA_DATA_PORT, "7000")
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