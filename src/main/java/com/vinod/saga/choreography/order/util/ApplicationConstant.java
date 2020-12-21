package com.vinod.saga.choreography.order.util;

public final class ApplicationConstant {

    /**
     * Instantiates a new application constant.
     */
    private ApplicationConstant() {
    }

    //SQS
    public static final String ORDER_CREATED_SQS = "Order_Created";

    public static final Integer FORMAT_LEADING_ZERO = 8;

    /**
     * The Enum Order Status.
     */
    public enum OrderStatus {
        CREATED("Created"),
        ACCEPTED("Accepted"),
        REJECTED("Rejected"),
        DELIVERED("Delivered");

        /** The value. */
        private final String value;

        OrderStatus(final String value) {
            this.value = value;
        }
        
        public String value() {
            return this.value;
        }
    }
}
