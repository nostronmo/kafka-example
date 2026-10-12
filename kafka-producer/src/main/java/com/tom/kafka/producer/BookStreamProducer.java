package com.tom.kafka.producer;

import java.time.Duration;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@KafkaStatusInterface
@RequiredArgsConstructor
public class BookStreamProducer {

	private final BookProducer producer;
	private final GenerateData genData;
	
    private Disposable subscription;

    public void startStreaming(int speed) {
        if (subscription != null && !subscription.isDisposed()) {
            log.warn("Stream already running");
            return;
        }
        
        subscription = Flux.interval(Duration.ofMillis(speed))
        		.publishOn(Schedulers.boundedElastic())
                .flatMap(tick -> Mono.fromRunnable(() -> {
                    Book book = genData.processGenerateAnBook();
                    producer.sendBook(book);
                    log.info("Sent book: {}", book.getTitle());
                })).onErrorResume(e -> {
                	log.error("Error sending data.");
                	return Mono.empty();
                })
                .subscribe();
        
        log.info("Reactive book streaming started with speed {} ms", speed);
    }

    public void stopStreaming() {
        if (subscription != null) {
            subscription.dispose();
            subscription = null;
            log.warn("Reactive book streaming stopped");
        }
    }
    
}
