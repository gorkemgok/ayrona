package com.ayronasystems.core.dao.mysql.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gorkemgok on 10/11/15.
 */
@XmlRootElement
@Entity
@Table(name="strategy_session")
public class StrategySessionModel extends BaseModelWithOwner {

    private String sessionId;

    private StrategyOptionsModel strategyOptions;

    private Set<StrategyModel> strategies = new HashSet<StrategyModel> ();

    public StrategySessionModel () {
    }

    public StrategySessionModel (String sessionId, StrategyOptionsModel strategyOptions) {
        this.sessionId = sessionId;
        this.strategyOptions = strategyOptions;
    }

    @XmlTransient
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    public Set<StrategyModel> getStrategies () {
        return strategies;
    }

    public void setStrategies (Set<StrategyModel> strategies) {
        this.strategies = strategies;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "options_id")
    public StrategyOptionsModel getStrategyOptions () {
        return strategyOptions;
    }

    public void setStrategyOptions (StrategyOptionsModel strategyOptions) {
        this.strategyOptions = strategyOptions;
    }

    @Column(name="session_id", nullable = false)
    public String getSessionId () {
        return sessionId;
    }

    public void setSessionId (String sessionId) {
        this.sessionId = sessionId;
    }

}

