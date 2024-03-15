package com.example.awarehouse.module.product.mapper;

import com.example.awarehouse.module.product.util.CurrencyProvider;

import java.util.Currency;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceConverter {
    public static Double extractDecimalNumberFromString(String textWithNumber) {
        Pattern pattern = Pattern.compile("\\d+[.,]?\\d*");
        Matcher matcher = pattern.matcher(textWithNumber);
        if (matcher.find()) {
            return extractDecimalNumberFromString(matcher);
        } else {
            return 0.0;
        }
    }
    private static Double extractDecimalNumberFromString(Matcher matcher) {
        String decimalPrice = matcher.group();
        decimalPrice = removeUnsupportedSignsFromStringNumber(decimalPrice);
        return convertToDouble(decimalPrice);
    }
    private static String removeUnsupportedSignsFromStringNumber(String number) {
        if (number.contains(",")) number = number.replaceAll(",", ".");
        return number;
    }

    private static Double convertToDouble(String number) {
        try {
            return Double.valueOf(number);
        } catch (NumberFormatException ex) {
            return 0.0;
        }

    }
    public static Currency extractCurrency(String price) {
        Pattern currencyPattern = Pattern.compile("\\p{L}+", Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = currencyPattern.matcher(price);
        if (matcher.find()) {
            String currencyString = matcher.group();
            try{
                return Currency.getInstance(currencyString);
            }catch (IllegalArgumentException e){
                return CurrencyProvider.currencyNameMap.get(currencyString);
            }
        }
        return null;
    }
}
