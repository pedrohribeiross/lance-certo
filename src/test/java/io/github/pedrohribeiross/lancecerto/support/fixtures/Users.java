package io.github.pedrohribeiross.lancecerto.support.fixtures;

import io.github.pedrohribeiross.lancecerto.user.User;
import io.github.pedrohribeiross.lancecerto.user.UserRole;

public final class Users {

    private Users() {}

    public static User any(){
        return new User(null, "mock user", "mock@email.com", UserRole.BIDDER);
    }
}
