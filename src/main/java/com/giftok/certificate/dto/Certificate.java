package com.giftok.certificate.dto;

import com.giftok.certificate.annotation.Collection;
import com.giftok.certificate.message.CertificateMessageOuterClass.CertificateMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Collection("certificates")
public class Certificate extends Dto {
    private User owner;
    private Currency currency;
    private int amount;
    private String text;

    public static CertificateMessage toMessage(Certificate certificate) {
        return null;
    }

    public static Certificate fromMessage(CertificateMessage message) {
       return null;
    }
}
