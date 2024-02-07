package com.scrumdogs.superprice.order;

import lombok.Getter;

public class OrderEmail {

    private final static String SUPERPRICE_ADMIN_EMAIL = "admin@superprice.com";
    private final static String DEFAULT_EMAIL_SUBJECT = "Super thanks for your order, ";
    private final static String DETAILS_HEADER = " ~ CUSTOMER DETAILS ~  ";
    private final static String SUMMARY_HEADER = " ~ ORDER SUMMARY ~  ";
    private final static int SUMMARY_FOOTER_OFFSET = 93;   //Footer width to make the $ symbols line up
    private final static int EMAIL_BODY_WIDTH = 120;        //The standard line-width of the email body

    private final String recipient;
    private String subject = DEFAULT_EMAIL_SUBJECT;
    @Getter private final String body;

    public OrderEmail(Order order) {
        this.recipient = order.getDetails().getEmail();
        this.subject += order.getDetails().getName() + "!";
        this.body = generateBodyString(order);
    }

    private String generateBodyString(Order order) {

        return "*".repeat((EMAIL_BODY_WIDTH - DETAILS_HEADER.length())/2) + DETAILS_HEADER +
                "*".repeat((EMAIL_BODY_WIDTH - DETAILS_HEADER.length())/2) + "\n\n" +
                String.format("\tOrder:\t\t%s\n", order.getId()) +
                String.format("\tName:\t\t%s\n", order.getDetails().getName()) +
                String.format("\tPhone:\t\t%s\n", order.getDetails().getPhone()) +
                String.format("\tAddress:\t%s,%s\n", order.getDetails().getAddress(),
                        order.getDetails().getPostcode()) +
                String.format("\tDelivery:\t%s (4-hour window starting at: %s)\n",
                        order.getDetails().getDeliveryDate(),
                        order.getDetails().getDeliveryWindowStart()) +
                String.format("\tNotes:\t\t%s\n\n", order.getDetails().getCustomMessage()) +
                "*".repeat((EMAIL_BODY_WIDTH - SUMMARY_HEADER.length())/2) + SUMMARY_HEADER +
                "*".repeat((EMAIL_BODY_WIDTH - SUMMARY_HEADER.length())/2) + "\n" +
                order.getCart().toString() + "\n" +
                "=".repeat(EMAIL_BODY_WIDTH) + "\n" +
                String.format("%sTotal\t:\t$%8.2f\n", " ".repeat(SUMMARY_FOOTER_OFFSET),
                        order.getCart().getTotalInCents() / 100.0) +
                "#".repeat(EMAIL_BODY_WIDTH) + "\n";
    }

    @Override
    public String toString() {
        return "#".repeat(EMAIL_BODY_WIDTH) + "\n" +
               "FROM:\t\t" + SUPERPRICE_ADMIN_EMAIL + "\n" +
               "TO:\t\t\t" + this.recipient + "\n" +
               "SUBJECT:\t" + this.subject + "\n" +
               "BODY: \n" + this.body;
    }
}
