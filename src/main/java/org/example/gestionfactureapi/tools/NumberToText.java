package org.example.gestionfactureapi.tools;

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
            if (number < 70) {
                words.append(tens[number / 10]);
                if (number % 10 != 0) {
                    words.append("-").append(units[number % 10]);
                }
            } else if (number < 80) {
                words.append("soixante-").append(teens[number - 70]);
            } else {
                words.append("quatre-vingt");
                if (number % 10 != 0) {
                    words.append("-").append(units[number % 10]);
                }
            }
            number = 0;
        } else if (number >= 10) {
            words.append(teens[number - 10]);
            number = 0;
        }
        if (number > 0 && number < 10) {
            words.append(units[number]);
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
                sb.append(tens[number / 10]);
                if (number % 10 != 0) {
                    sb.append("-").append(units[number % 10]);
                }
            }
        } else if (number > 0) {
            sb.append(units[number]);
        }
        return sb.toString().trim();
    }

    public static void main(String[] args) {
        NumberToText converter = new NumberToText("213.772");
        System.out.println(converter.toText());  // Output: deux cents treize dinars et sept cent soixante-douze MILLIMES
    }
}
