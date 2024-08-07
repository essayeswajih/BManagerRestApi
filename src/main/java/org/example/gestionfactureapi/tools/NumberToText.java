package org.example.gestionfactureapi.tools;

import java.text.DecimalFormat;

public class NumberToText {

    private static final String[] units = {"", "un", "deux", "trois", "quatre", "cinq", "six", "sept", "huit", "neuf"};
    private static final String[] tens = {"", "dix", "vingt", "trente", "quarante", "cinquante", "soixante", "soixante-dix", "quatre-vingt", "quatre-vingt-dix"};
    private static final String[] hundreds = {"", "cent", "deux cents", "trois cents", "quatre cents", "cinq cents", "six cents", "sept cents", "huit cents", "neuf cents"};

    private double number;

    public NumberToText(String numberStr) {
        try {
            this.number = Double.parseDouble(numberStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + numberStr, e);
        }
    }

    public String toText() {
        if (number < 0) {
            throw new IllegalArgumentException("Number must be non-negative");
        }

        int integerPart = (int) number;
        int fractionalPart = (int) Math.round((number - integerPart) * 1000);

        String integerWords = convertIntegerToFrench(integerPart);
        String fractionalWords = convertFractionsToFrench(fractionalPart);

        return integerWords + " dinars et " + fractionalWords + " MELLIMES";
    }

    private String convertIntegerToFrench(int number) {
        if (number == 0) {
            return "zéro";
        }

        StringBuilder words = new StringBuilder();
        if (number >= 1000) {
            if (number / 1000 == 1) {
                words.append("mille ");
            } else {
                words.append(convertIntegerToFrench(number / 1000)).append(" mille ");
            }
            number %= 1000;
        }
        if (number >= 100) {
            if (number / 100 == 1 && number % 100 == 0) {
                words.append("cent ");
            } else {
                words.append(hundreds[number / 100]).append(" ");
            }
            number %= 100;
        }
        if (number >= 20) {
            if (number == 80) {
                words.append("quatre-vingts ");
                number = 0;
            } else {
                words.append(tens[number / 10]).append(" ");
                number %= 10;
            }
        }
        if (number > 0) {
            words.append(units[number]).append(" ");
        }

        return words.toString().trim();
    }

    private String convertFractionsToFrench(int number) {
        if (number == 0) {
            return "zéro";
        }
        DecimalFormat df = new DecimalFormat("000");
        return df.format(number);
    }

}

