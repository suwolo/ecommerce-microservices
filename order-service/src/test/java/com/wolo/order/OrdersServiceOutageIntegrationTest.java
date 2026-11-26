package com.wolo.order;

import com.wolo.order.infrastructure.persistence.repository.OrdersRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestProfile(InventoryOutageTestProfile.class)
class OrdersServiceOutageIntegrationTest {
    @Inject
    OrdersRepository ordersRepository;

    @BeforeEach
    @Transactional
    void setUp() {
        ordersRepository.deleteAll();
    }

    @Test
    void shouldReturn503WhenInventoryServiceIsUnavailable() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "customerId": 1,
                            "productId": 1,
                            "quantity": 3
                        }
                        """)
                .when()
                .post("/api/order")
                .then()
                .statusCode(503);

        assertEquals(0L, ordersRepository.count());
    }
}