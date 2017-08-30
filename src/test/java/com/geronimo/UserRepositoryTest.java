package com.geronimo;

import com.geronimo.dao.UserRepository;
import com.geronimo.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    private UserRepository userRepository;

    // TODO: figure out what can we do with SimpleJpaRepository transaction awareness
    // TODO: changes are flushed before we try select a row that is not yet persisted
    @Test
    public void testUserCouldBeStoredAndRetrieved() {
        User firstUser = new User("firstUsername", "firstPassword");
        firstUser = userRepository.save(firstUser);

        User retrievedUser = userRepository.findByUsername("firstUsername");
        assertEquals(retrievedUser.getId(), firstUser.getId());
        assertEquals(retrievedUser.getUsername(), firstUser.getUsername());
        assertEquals(retrievedUser.getPassword(), firstUser.getPassword());
        assertNotNull(retrievedUser.getDateCreated());
        assertNotNull(retrievedUser.getLastUpdated());
        assertEquals(retrievedUser.getVersion().longValue(), 0L);

        userRepository.delete(firstUser);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
