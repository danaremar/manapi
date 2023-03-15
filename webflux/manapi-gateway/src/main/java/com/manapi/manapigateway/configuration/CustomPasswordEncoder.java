package com.manapi.manapigateway.configuration;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordEncoder implements PasswordEncoder {

    @Value("${manapi.security.password.secret}")
    private String secret;

    @Value("${manapi.security.password.iterations}")
    private Integer iteration;

    @Value("${manapi.security.password.length}")
    private Integer keylength;

    /**
     * More info (https://www.owasp.org/index.php/Hashing_Java)
     * @param rawPassword password without encode
     * @return encoded password
     */
    @Override
    public String encode(CharSequence rawPassword) {
        try {
            byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
                    .generateSecret(new PBEKeySpec(rawPassword.toString().toCharArray(), secret.getBytes(), iteration, keylength))
                    .getEncoded();

            return Base64.getEncoder()
                    .encodeToString(result);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
    
}
