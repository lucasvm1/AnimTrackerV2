package com.lucasvm.animtrackerv2.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

@Converter
@Component
public class AttributeEncryptor implements AttributeConverter <String, String>{

    private final TextEncryptor textEncryptor;

    public AttributeEncryptor(@Value("${app.encryption.secret}") String secret, @Value("${app.encryption.salt}") String salt) {
        this.textEncryptor = Encryptors.text(secret, salt);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        return textEncryptor.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return textEncryptor.decrypt(dbData);
    }


}
