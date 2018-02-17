package com.geronimo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@RunWith(SpringRunner.class)
@GeronimoSpringTest
public class GeronimoDatabaseTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testDatabaseConnectionEstablished() {
        assertThat(entityManager.createNativeQuery("SELECT 1").getSingleResult(), is(BigInteger.valueOf(1L)));
    }
}
