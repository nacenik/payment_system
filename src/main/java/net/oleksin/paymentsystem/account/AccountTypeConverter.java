package net.oleksin.paymentsystem.account;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class AccountTypeConverter implements AttributeConverter<AccountType, String> {
    @Override
    public String convertToDatabaseColumn(AccountType accountType) {
        if (accountType == null) {
            return null;
        }

        return accountType.getAccountType();
    }

    @Override
    public AccountType convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }

        return AccountType.fromString(s);
    }
}
