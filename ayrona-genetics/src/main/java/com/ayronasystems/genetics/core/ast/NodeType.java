package com.ayronasystems.genetics.core.ast;

/**
 * Created by gorkemg on 28.03.2016.
 */
public class NodeType {

    public static final NodeType BOOLEAN = new NodeType(0);

    public static final NodeType DOUBLE = new NodeType(1);

    public static final NodeType INT = new NodeType(2);

    public static final NodeType STRING = new NodeType(3);

    private final int type;

    public NodeType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeType nodeType = (NodeType) o;

        return type == nodeType.type;

    }

    @Override
    public int hashCode() {
        return type;
    }
}
