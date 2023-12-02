package net.slc.dv.database.builder.enums;

import lombok.Getter;

@Getter
public enum ConditionCompareType {
    EQUAL("="),
    NOT_EQUAL("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_EQUAL(">="),
    LESS_THAN_EQUAL("<="),

    IN("IN");

    private final String symbol;

    ConditionCompareType(String symbol) {
        this.symbol = symbol;
    }

    public static ConditionCompareType fromString(String symbol) {
        switch (symbol) {
            case "=":
                return EQUAL;
            case "!=":
                return NOT_EQUAL;
            case ">":
                return GREATER_THAN;
            case "<":
                return LESS_THAN;
            case ">=":
                return GREATER_THAN_EQUAL;
            case "<=":
                return LESS_THAN_EQUAL;
            case "IN":
                return IN;
            default:
                throw new IllegalArgumentException("Invalid symbol: " + symbol);
        }
    }

}
