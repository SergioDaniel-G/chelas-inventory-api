package com.micheladas.chelas.authservice;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IpService {

    private final Map<String, Set<String>> userKnownIps = new ConcurrentHashMap<>();

    public boolean isKnownIp(String email, String ip) {
        return userKnownIps.getOrDefault(email, Collections.emptySet()).contains(ip);
    }

    public void registerIp(String email, String ip) {
        // MOVE TO A DISTRIBUTED CACHE (REDIS) FOR HORIZONTAL SCALABILITY
        userKnownIps.computeIfAbsent(email, k -> new HashSet<>()).add(ip);
    }

}
