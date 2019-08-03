package com.tracker.common.validatior;

import com.google.common.base.Joiner;
import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * Validate password against given rules.
 * <p>Example: character, length...</p>
 *
 * @author Martin Krumov
 */
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 64;
    //TODO: define password rules
//    private static final int UPPER_CASE_CHARS = 1;
//    private static final int LOWER_CASE_CHARS = 1;
//    private static final int DIGITS_NUMBER = 1;
    private static final List<Rule> PWD_RULES = List.of(
            new LengthRule(MIN_LENGTH, MAX_LENGTH),
//            new CharacterRule(EnglishCharacterData.UpperCase, UPPER_CASE_CHARS),
//            new CharacterRule(EnglishCharacterData.LowerCase, LOWER_CASE_CHARS),
//            new CharacterRule(EnglishCharacterData.Digit, DIGITS_NUMBER),
            new WhitespaceRule()
    );

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (isNull(password)) {
            return false;
        }

        PasswordValidator validator = new PasswordValidator(PWD_RULES);

        RuleResult result = validator.validate(new PasswordData(password));

        if (result.isValid()) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                Joiner.on(" ").join(validator.getMessages(result)))
                .addConstraintViolation();
        return false;
    }

}
