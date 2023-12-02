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
        switch (type) {
            case "ASC":
                return ASC;
            case "DESC":
                return DESC;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

}
