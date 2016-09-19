package com.ayronasystems.genetics.examples.bridge;

import com.ayronasystems.genetics.core.ast.AbstractNode;
import com.ayronasystems.genetics.core.ast.FunctionNode;
import com.ayronasystems.genetics.core.ast.NodeType;

/**
 * Created by gorkemgok on 30/03/16.
 */
public class Person extends AbstractNode {

    private String identifier;

    private int moveTime;

    public Person (String identifier) {
        this.identifier = identifier;
    }

    public Person (String identifier, int moveTime) {
        this.identifier = identifier;
        this.moveTime = moveTime;
    }

    public NodeType getType () {
        return BPNodeTypes.TRM_PERSON_WITH;
    }

    public void setParentNode (FunctionNode parentNode) {

    }

    public String getIdentifier () {
        return identifier;
    }

    public int getMoveTime () {
        return moveTime;
    }

    @Override
    public boolean equals (Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass () != o.getClass () ) {
            return false;
        }

        Person person = (Person) o;

        return identifier != null ? identifier.equals (person.identifier) : person.identifier == null;

    }

    @Override
    public int hashCode () {
        return identifier != null ? identifier.hashCode () : 0;
    }

    @Override
    public String toString () {
        return "Person{" +
                "identifier='" + identifier + '\'' +
                ", moveTime=" + moveTime +
                '}';
    }

    public Person copy (){
        return new Person(identifier, moveTime);
    }

    public int getDepth () {
        return 1;
    }
}
