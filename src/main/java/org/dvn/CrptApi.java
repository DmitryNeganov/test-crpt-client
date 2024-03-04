package org.dvn;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class CrptApi {

    private static final String BASE_URL = "https://ismp.crpt.ru/";
    private static final String BASE_URN = "api/v3/lk/documents/create";

    private static final Logger log = Logger.getLogger(CrptApi.class.getName());
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public AtomicInteger getLimitCounter() {
        return limitCounter;
    }

    private AtomicInteger limitCounter;
    private int limit;
    private TimeUnit timeUnit;

    public CrptApi(TimeUnit timeUnit, int limit) {
        this.limitCounter = new AtomicInteger(0);
        this.limit = limit;
        this.timeUnit = timeUnit;

        timeLoop();
    }

    private void timeLoop() {
        int oneTimeUnit = 1;
        executorService.schedule(() -> {
            limitCounter.set(0);
            timeLoop();
        }, oneTimeUnit, timeUnit);
    }

    public synchronized void sendRequest(String requestBody) throws Exception {
        //0 -> 1 -> 2 ... limit
        if (limitCounter.get() >= limit) {
            throw new Exception("Limit is over");
        }
        limitCounter.incrementAndGet();
        RestClient restClient = RestClient.create();

        URI uri = null;
        try {
            uri = new URI(BASE_URL + BASE_URN);
        } catch (URISyntaxException e) {
            log.warning("Wrong syntax of URI : " + uri);
            return;
        }

        ResponseEntity<Void> response = restClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .toBodilessEntity();

        log.info("Request to API well done");
    }

    @Getter
    @Setter
    @Builder
    public static class RequestBody {
        private String regNumber;
        private String productionDate;
        private Description description;
        private String docType;
        private String docId;
        private String ownerInn;
        private List<ProductsItem> products;
        private String regDate;
        private String participantInn;
        private String docStatus;
        private boolean importRequest;
        private String productionType;
        private String producerInn;
    }

    @Getter
    @Setter
    @Builder
    public static class ProductsItem {
        private String uituCode;
        private String certificateDocumentDate;
        private String productionDate;
        private String certificateDocumentNumber;
        private String tnvedCode;
        private String certificateDocument;
        private String producerInn;
        private String ownerInn;
        private String uitCode;
    }

    @Getter
    @Setter
    @Builder
    public static class Description {
        private String participantInn;
    }

}

