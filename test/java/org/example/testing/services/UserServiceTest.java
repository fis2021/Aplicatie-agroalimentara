package org.example.testing.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserService {
    UserService us = new UserService();

    @Test
    void initUserDatabase() {
        assertDoesNotThrow(() -> us.initUserDatabase());
    }

    @Test
    void initUserDatabaseShouldThrow() {
        assertThrows(
                UsernameAlreadyExistsException.class,
                () -> us.initUserDatabase()
        );
    }
}