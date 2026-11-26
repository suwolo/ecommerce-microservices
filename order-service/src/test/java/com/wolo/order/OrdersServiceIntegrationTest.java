package com.wolo.order;

import com.wolo.order.infrastructure.persistence.repository.OrdersRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class OrdersServiceIntegrationTest {

    @Inject
    OrdersRepository ordersRepository;

    @BeforeEach
    @Transactional
    void setUp(){
        ordersRepository.deleteAll();
    }

    @Test
    void shouldCreateOrderSuccessfully() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "customerId": 1,
                            "productId": 1,
                            "quantity": 9
                        }
                        """)
                .when()
                .post("/api/order")
                .then()
                .statusCode(201)
                .body("customerId", equalTo(1))
                .body("productId", equalTo(1))
                .body("quantity", equalTo(9))
                .body("status", equalTo("CREATED"));

        assertEquals(1L, ordersRepository.count());

        var persistedOrder =
                ordersRepository.findAll().firstResult();

        assertNotNull(persistedOrder);
        assertEquals(1L, persistedOrder.getCustomerId());
        assertEquals(1L, persistedOrder.getProductId());
        assertEquals(9L, persistedOrder.getQuantity());
        assertEquals("CREATED", persistedOrder.getStatus().name());
    }

    @Test
    void shouldCreateOrderWhenStockEqualsRequestedQuantity() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "customerId": 1,
                            "productId": 1,
                            "quantity": 10
                        }
                        """)
                .when()
                .post("/api/order")
                .then()
                .statusCode(201)
                .body("quantity", equalTo(10));

        assertEquals(1L, ordersRepository.count());
    }

    @Test
    void shouldRejectOrderWhenStockIsInsufficient() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "customerId": 1,
                            "productId": 4,
                            "quantity": 9
                        }
                        """)
                .when()
                .post("/api/order")
                .then()
                .statusCode(409);

        assertEquals(0L, ordersRepository.count());
    }

    @Test
    void shouldReturn404WhenProductDoesNotExist() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "customerId": 1,
                            "productId": 999,
                            "quantity": 3
                        }
                        """)
                .when()
                .post("/api/order")
                .then()
                .statusCode(404);

        assertEquals(0L, ordersRepository.count());
    }


}