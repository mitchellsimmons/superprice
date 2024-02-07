package com.scrumdogs.superprice.order;

import lombok.Getter;

@Getter
public enum OrderStatus {
    EMAILED(0),
    APPROVED(1),
    SUBMITTED(2);

    private final int code;

    OrderStatus(int code) {
        this.code = code;
    }

    public String toString() {
        return switch (this) {
            case EMAILED -> "Order emailed to delivery partner organisation.";
            case APPROVED -> "Order approved by SuperPrice.";
            case SUBMITTED -> "Order being reviewed by SuperPrice.";
            //No default case: compiler prevents values outside enum range
        };
    }
}
