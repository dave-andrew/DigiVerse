package net.slc.dv.database.builder.enums;

import lombok.Getter;

@Getter
public enum OrderByType {
    ASC("ASC"),
    DESC("DESC");

    private final String type;

    OrderByType(String type) {
        this.type = type;
    }

    public static OrderByType fromString(String type) {
        for (OrderByType orderByType : OrderByType.values()) {
            if (orderByType.getType().equalsIgnoreCase(type)) {
                return orderByType;
            }
        }
        return null;
    }

}
