package com.ayronasystems.genetics.examples.bridge;

import com.ayronasystems.genetics.core.ast.AbstractFunctionNode;
import com.ayronasystems.genetics.core.ast.FunctionNode;
import com.ayronasystems.genetics.core.ast.Node;
import com.ayronasystems.genetics.core.ast.NodeType;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by gorkemgok on 30/03/16.
 */
public class PersonSelector extends AbstractFunctionNode implements FunctionNode {

    private List<Person> personList = new ArrayList<Person> ();

    public List<Person> getPersonList () {
        return personList;
    }

    public void addPerson (Person person) {
        personList.add (person);
        notifyObserversToAdd (person);
    }

    public NodeType getType () {
        return BPNodeTypes.FN_PERSON_SELECTOR;
    }

    public void setChildNode (Node newNode, int index) {
        if (index < personList.size ()){
            notifyObserversToReplace (personList.get (index), newNode);
            personList.set (index, (Person)newNode);
        }
        throw new NoSuchElementException ("No Node to set for index "+index);
    }

    public List<? extends Node> getNestedNodes () {
        return personList;
    }

    @Override
    public String toString () {
        return "PersonSelector{" +
                "personList=" + personList +
                '}';
    }

    public boolean replaceChildNode (Node oldNode, Node newNode) {
        int i = 0;
        for ( Person person :
                personList ) {
            if (person.equals (oldNode)){
                personList.set (i, (Person)newNode);
                return true;
            }
            i++;
        }
        return false;
    }

    public PersonSelector copy (){
        PersonSelector personSelector = new PersonSelector ();
        for ( Person person:
               personList) {
            personSelector.addPerson (person.copy ());
        }
        return personSelector;
    }

    public int getDepth () {
        return 2;
    }
}
