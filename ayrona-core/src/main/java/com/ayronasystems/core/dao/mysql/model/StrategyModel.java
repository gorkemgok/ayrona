package com.ayronasystems.core.dao.mysql.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gorkemgok on 22/02/15.
 */
@XmlRootElement
@Entity
@Table(name="strategy")
public class StrategyModel extends BaseModel {

    private String formula;

    private double score;

    private StrategySessionModel strategySessionModel;

    private Set<SummaryModel> summaries = new HashSet<SummaryModel> ();

    private StrategyOptionsModel strategyOptionsModel;

    private boolean isFavorite = false;

    private boolean isSeen = false;

    public StrategyModel () {
    }

    public StrategyModel (String formula, double score, StrategySessionModel strategySessionModel) {
        this.formula = formula;
        this.score = score;
        this.strategySessionModel = strategySessionModel;
        this.strategyOptionsModel = strategySessionModel.getStrategyOptions ();
    }

    @Column(name="formula", nullable = false)
    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    @Column(name="score", nullable = false)
    public double getScore () {
        return score;
    }

    public void setScore (double score) {
        this.score = score;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "session_id", nullable = false)
    public StrategySessionModel getStrategySessionModel () {
        return strategySessionModel;
    }

    public void setStrategySessionModel (StrategySessionModel strategySessionModel) {
        this.strategySessionModel = strategySessionModel;
        this.strategyOptionsModel = strategySessionModel.getStrategyOptions ();
    }

    @SuppressWarnings("JpaAttributeTypeInspection")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "strategy_id")
    public Set<SummaryModel> getSummaries () {
        return summaries;
    }

    public void setSummaries (Set<SummaryModel> summaries) {
        this.summaries = summaries;
    }

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "options_id")
    public StrategyOptionsModel getStrategyOptionsModel () {
        return strategyOptionsModel;
    }

    public void setStrategyOptionsModel (StrategyOptionsModel strategyOptionsModel) {
        this.strategyOptionsModel = strategyOptionsModel;
    }

    @Column(name = "is_favorite")
    public boolean isFavorite () {
        return isFavorite;
    }

    public void setFavorite (boolean favorite) {
        isFavorite = favorite;
    }

    @Column(name = "is_seen")
    public boolean isSeen () {
        return isSeen;
    }

    public void setSeen (boolean seen) {
        isSeen = seen;
    }
}
