package com.tracker.service;


import com.tracker.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    /**
     * Save user.
     *
     * @param user the user
     * @return the saved user
     */
    User save(User user);

    /**
     * Returns a {@link Page} of users.
     *
     * @param pageable the {@link Pageable}
     * @return the page of {@link User}
     */
    Page<User> findAll(Pageable pageable);

    /**
     * Register new {@link User}.
     *
     * @param user the user
     * @return the saved user
     */
    User register(User user);

    /**
     * Complete registration.
     *
     * @param verificationCode the token code
     */
    void completeRegistration(String verificationCode);

    /**
     * Validates token exists and it's not expired.
     *
     * @param tokenCode given token code
     */
    void validateToken(String tokenCode);

    /**
     * Initiate reset password for given user.
     *
     * @param email the email of the user
     */
    void resetPassword(String email);

    /**
     * Complete password reset.
     *
     * @param resetCode the token
     * @param password  the new password
     */
    void completePasswordReset(String resetCode, String password);

    /**
     * Locks the account of user by username.
     *
     * @param username the username
     */
    void lockByUsername(String username);
}
