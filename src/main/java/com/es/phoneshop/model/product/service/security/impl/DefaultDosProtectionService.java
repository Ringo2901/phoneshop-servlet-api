package com.es.phoneshop.model.product.service.security.impl;

import com.es.phoneshop.model.product.service.security.DosProtectionService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private static volatile DefaultDosProtectionService instance;

    private static final long MAX_AMOUNT_OF_REQUESTS_FROM_SINGLE_IP = 20;
    private static final long MINUTE = 60000000000L;

    private Map<String, IpInfo> ipInfoMap;

    public static DefaultDosProtectionService getInstance() {
        if (instance == null) {
            synchronized (DefaultDosProtectionService.class) {
                if (instance == null) {
                    instance = new DefaultDosProtectionService();
                }
            }
        }
        return instance;
    }

    private DefaultDosProtectionService() {
        ipInfoMap = new ConcurrentHashMap<>();
    }

    @Override
    public boolean isAllowed(String ipAddress) {
        IpInfo ipInfo = ipInfoMap.computeIfAbsent(ipAddress, key -> new IpInfo());

        if (ipInfo.getRequestCount() > MAX_AMOUNT_OF_REQUESTS_FROM_SINGLE_IP) {
            if (System.nanoTime() - ipInfo.getClockBeginTime() < MINUTE) {
                return false;
            } else {
                ipInfo.setRequestCount(0L);
                ipInfo.setClockBeginTime(System.nanoTime());
                return true;
            }
        }

        ipInfo.setRequestCount(ipInfo.getRequestCount() + 1);
        return true;
    }

    public Map<String, IpInfo> getIpInfoMap() {
        return ipInfoMap;
    }

    private static class IpInfo {
        private long clockBeginTime;
        private long requestCount;

        public long getClockBeginTime() {
            return clockBeginTime;
        }

        public void setClockBeginTime(long clockBeginTime) {
            this.clockBeginTime = clockBeginTime;
        }

        public long getRequestCount() {
            return requestCount;
        }

        public void setRequestCount(long requestCount) {
            this.requestCount = requestCount;
        }
    }
}
