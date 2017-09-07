package com.geronimo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.math.BigInteger;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@GeronimoSpringTest
public class GeronimoDatabaseTest {

    private EntityManager entityManager;

    @Test
    public void testDatabaseConnectionEstablished() {
        Query nativeQuery = entityManager.createNativeQuery("SELECT 1");
        assertEquals(nativeQuery.getSingleResult(), BigInteger.valueOf(1L));
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
