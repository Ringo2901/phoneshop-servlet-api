package com.es.phoneshop.model.product.service.security;

public interface DosProtectionService {
    boolean isAllowed(String ipAddress);
}
