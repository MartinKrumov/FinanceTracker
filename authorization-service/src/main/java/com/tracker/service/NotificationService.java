package com.tracker.service;

public interface NotificationService {

    /**
     * Send verification email.
     *
     * @param email             email of the user
     * @param verificationToken the verification token
     */
    void sendVerificationEmail(String email, String verificationToken);

    /**
     * Send password reset email.
     *
     * @param email      email of the user
     * @param resetToken the reset token
     */
    void sendPasswordResetEmail(String email, String resetToken);

    //TODO: ?after password reset?
}
