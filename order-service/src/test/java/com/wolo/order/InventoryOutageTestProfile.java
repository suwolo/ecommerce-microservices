package com.wolo.order;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

public class InventoryOutageTestProfile implements QuarkusTestProfile {
    @Override
    public Map<String, String> getConfigOverrides() {

        return Map.of(
                "quarkus.rest-client.inventory-api.url",
                "http://127.0.0.1:1"
        );
    }
}