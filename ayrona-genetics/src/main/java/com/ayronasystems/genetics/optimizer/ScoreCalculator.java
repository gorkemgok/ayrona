package com.ayronasystems.genetics.optimizer;

import com.ayronasystems.core.backtest.BackTestResult;
import com.ayronasystems.core.backtest.MetricType;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Created by gorkemgok on 17/07/16.
 */
public class ScoreCalculator {

    private final String scoreEquation;

    private final Expression expression;

    public ScoreCalculator (String scoreEquation) {
        this.scoreEquation = scoreEquation;
        ExpressionBuilder expressionBuilder = new ExpressionBuilder (scoreEquation);
        for ( MetricType metricType : MetricType.values ()){
            expressionBuilder.variable (metricType.getEquationSymbol ());
        }
        this.expression = expressionBuilder.build ();
    }

    public double calculate(BackTestResult result){
        for ( MetricType metricType : MetricType.values ()){
            Double value = result.getResultAsDouble (metricType);
            if (value != null && !Double.isNaN (value)){
                expression.setVariable (metricType.getEquationSymbol (), value);
            }
        }
        return expression.evaluate ();
    }

    public String getScoreEquation () {
        return scoreEquation;
    }
}
