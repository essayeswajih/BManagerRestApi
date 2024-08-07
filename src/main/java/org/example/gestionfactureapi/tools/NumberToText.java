package org.example.gestionfactureapi.tools;

import java.text.DecimalFormat;

public class NumberToText {

    private static final String[] units = {"", "un", "deux", "trois", "quatre", "cinq", "six", "sept", "huit", "neuf"};
    private static final String[] teens = {"dix", "onze", "douze", "treize", "quatorze", "quinze", "seize", "dix-sept", "dix-huit", "dix-neuf"};
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

        return integerWords + " dinars et " + fractionalWords + " MILLIMES";
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
            if (number < 30) {
                words.append(tens[2]).append("-").append(units[number - 20]).append(" ");
            } else if (number < 40) {
                words.append(tens[3]).append("-").append(units[number - 30]).append(" ");
            } else if (number < 50) {
                words.append(tens[4]).append("-").append(units[number - 40]).append(" ");
            } else if (number < 60) {
                words.append(tens[5]).append("-").append(units[number - 50]).append(" ");
            } else if (number < 70) {
                words.append(tens[6]).append("-").append(units[number - 60]).append(" ");
            } else if (number < 80) {
                words.append("soixante-dix").append(teens[number - 70]).append(" ");
            } else if (number < 90) {
                words.append(tens[8]).append("-").append(units[number - 80]).append(" ");
            } else {
                words.append("quatre-vingt-dix").append(units[number - 90]).append(" ");
            }
            number = 0;
        }
        if (number > 0 && number < 10) {
            words.append(units[number]).append(" ");
        }

        return words.toString().trim();
    }

    private String convertFractionsToFrench(int number) {
        if (number == 0) {
            return "zéro";
        }
        StringBuilder sb = new StringBuilder();
        if (number >= 100) {
            sb.append(units[number / 100]).append(" cent ");
            number %= 100;
        }
        if (number >= 10) {
            if (number < 20) {
                sb.append(teens[number - 10]).append(" ");
            } else {
                sb.append(tens[number / 10]).append("-");
                number %= 10;
            }
        }
        if (number > 0) {
            sb.append(units[number]).append(" ");
        }
        return sb.toString().trim();
    }

    public static void main(String[] args) {
        NumberToText converter = new NumberToText("1234.567");
        System.out.println(converter.toText());
    }
}
