package net.slc.dv.database.builder.enums;

import lombok.Getter;

@Getter
public enum ConditionCompareType {
    EQUAL("="),
    NOT_EQUAL("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_EQUAL(">="),
    LESS_THAN_EQUAL("<=");

    private final String symbol;

    ConditionCompareType(String symbol) {
        this.symbol = symbol;
    }

    public static ConditionCompareType fromString(String symbol) {
        for (ConditionCompareType type : ConditionCompareType.values()) {
            if (type.symbol.equals(symbol)) {
                return type;
            }
        }
        return null;
    }

}
