package com.manapi.manapigateway.jwt;

import java.time.Duration;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ConcurrentReferenceHashMap;

import com.manapi.manapigateway.model.users.User;
import com.manapi.manapigateway.service.UserService;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

@Service
public class RateLimiterService {

    /**
     * Cache for specific user
     */
    Map<String, Bucket> bucketCache = new ConcurrentReferenceHashMap<>();

    @Autowired
    private UserService userService;

    /**
     * Map bucket in cache
     * 
     * @param username
     * @return bucket
     */
    public Bucket resolveBucket(String username) {
        return bucketCache.computeIfAbsent(username, this::newBucket);
    }

    /**
     * Total unregistered users
     * 
     * @return bucket
     */
    private Bucket withoutUser() {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(5L, Refill.intervally(10, Duration.ofMinutes(1))))
                .build();
    }

    /**
     * Create new {@link Bucket}
     * 
     * @param username
     * @return bucket
     */
    private Bucket newBucket(String username) {
        User u = userService.findUserByUsername(username);
        if (StringUtils.isEmpty(username) || u == null) {
            return withoutUser();
        }
        return Bucket.builder().addLimit(u.getPlan().getLimit()).build();
    }

}
