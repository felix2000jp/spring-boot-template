package dev.felix2000jp.springboottemplate.system.scheduling;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.modulith.events.CompletedEventPublications;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@EnableScheduling
class CleanCompletedEventsJob {

    private final CompletedEventPublications completeEvents;

    @Value("${events.schedule.complete-event-older-than-in-minutes}")
    private int completeEventOlderThanInMinutes;

    CleanCompletedEventsJob(CompletedEventPublications completeEvents) {
        this.completeEvents = completeEvents;
    }

    @Scheduled(cron = "${events.schedule.complete-event-cron-job}")
    void cleanCompletedEvents() {
        var duration = Duration.ofMinutes(completeEventOlderThanInMinutes);
        completeEvents.deletePublicationsOlderThan(duration);
    }

}
