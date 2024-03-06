package org.dvn;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {

        int limitForCrptApit = 3;

        CrptApi crptApi = new CrptApi(TimeUnit.MINUTES, limitForCrptApit);

        String s = "{\"description\": { \"participantInn\": \"string\" }, \"doc_id\": \"string\", \"doc_status\": \"string\", \"doc_type\": \"LP_INTRODUCE_GOODS\", \"importRequest\": true, \"owner_inn\": \"string\", \"participant_inn\": \"string\", \"producer_inn\": \"string\", \"production_date\": \"2020-01-23\", \"production_type\": \"string\", \"products\": [ { \"certificate_document\": \"string\", \"certificate_document_date\": \"2020-01-23\", \"certificate_document_number\": \"string\", \"owner_inn\": \"string\", \"producer_inn\": \"string\", \"production_date\": \"2020-01-23\", \"tnved_code\": \"string\", \"uit_code\": \"string\", \"uitu_code\": \"string\" } ], \"reg_date\": \"2020-01-23\", \"reg_number\": \"string\"}";

        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(10000);
                crptApi.sendRequest(s);
            } catch (Exception e) {
                System.out.println("Exception is caught : " + e.getMessage());
            }
            int restRequests = limitForCrptApit - crptApi.getLimitCounter();
            System.out.println("You can send " + restRequests + " requests");
        }

        //builder example
        User user = User.builder().name("Mark").surname("Hoppus").nickname("Blink").build();
    }
}