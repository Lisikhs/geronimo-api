package com.geronimo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeronimoDatabaseTest {

    private EntityManagerFactory factory;

    @Test
    public void testDatabaseConnectionEstablished() {
        EntityManager entityManager = factory.createEntityManager();
        Query nativeQuery = entityManager.createNativeQuery("SELECT 1");
        assertEquals(nativeQuery.getSingleResult(), BigInteger.valueOf(1L));
    }


    @Autowired
    public void setEntityManagerFactory(EntityManagerFactory factory) {
        this.factory = factory;
    }
}
