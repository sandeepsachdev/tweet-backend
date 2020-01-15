package com.eduonix.projectbackend.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Configuration
@EnableCaching
@EnableScheduling
public class CachingManager {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("tweets");
    }

    @CacheEvict(allEntries = true, value = {"tweets"})
    @Scheduled(fixedDelay = 600  * 1000 ,  initialDelay = 500)
    public void reportCacheEvict() {
        System.out.println("Flush Cache ");
    }
}
