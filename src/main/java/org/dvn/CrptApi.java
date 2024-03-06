package org.dvn;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class CrptApi {

    private static final String BASE_URL = "https://ismp.crpt.ru/";
    private static final String BASE_URN = "api/v3/lk/documents/create";

    private static final Logger log = Logger.getLogger(CrptApi.class.getName());

    public int getLimitCounter() {
        return limitCounter.get();
    }

    private final AtomicInteger limitCounter;
    private int limit;
    private TimeUnit timeUnit;

    public CrptApi(TimeUnit timeUnit, int limit) {
        this.limitCounter = new AtomicInteger(0);
        this.limit = limit;
        this.timeUnit = timeUnit;

        Executors.newSingleThreadExecutor().execute(this::timeLoop);
    }

    private void timeLoop() {
        while (true) {
            try {
                timeUnit.sleep(1);
                System.out.println("Sleep round");
                limitCounter.set(0);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized void sendRequest(String requestBody) throws Exception {
        //0 -> 1 -> 2 ... limit
        if (limitCounter.get() >= limit) {
            throw new Exception("Limit is over");
        }

        //request sending asynchronously
        Executors.newCachedThreadPool().submit(() -> {
                    RestClient restClient = RestClient.create();
                    URI uri = null;
                    try {
                        uri = new URI(BASE_URL + BASE_URN);
                    } catch (URISyntaxException e) {
                        log.warning("Wrong syntax of URI : " + uri);
                    }
                    ResponseEntity<String> response = restClient.post()
                            .uri(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(requestBody)
                            .retrieve().toEntity(String.class);
                }
        );

        System.out.println("request sent");

        limitCounter.incrementAndGet();

    }
}
