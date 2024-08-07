package org.example.gestionfactureapi.tools;

import java.text.DecimalFormat;

public class NumberToText {

    private static final String[] uniteNames1 = {
            "", "un", "deux", "trois", "quatre", "cinq", "six", "sept", "huit", "neuf", "dix",
            "onze", "douze", "treize", "quatorze", "quinze", "seize", "dix-sept", "dix-huit", "dix-neuf"
    };

    private static final String[] dizaineNames = {
            "", "", "vingt", "trente", "quarante", "cinquante", "soixante", "soixante", "quatre-vingt", "quatre-vingt"
    };

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

    private String convertZeroToHundred(int number) {
        int laDizaine = number / 10;
        int lUnite = number % 10;
        String resultat = "";

        if (laDizaine == 1 || laDizaine == 7 || laDizaine == 9) {
            lUnite += 10;
        }

        String laLiaison = (laDizaine > 1 && lUnite > 0) ? "-" : "";
        if (lUnite == 1 && laDizaine != 8) {
            laLiaison = " et ";
        } else if (lUnite == 11 && laDizaine == 7) {
            laLiaison = " et ";
        }

        switch (laDizaine) {
            case 0:
                resultat = uniteNames1[lUnite];
                break;
            case 8:
                if (lUnite == 0) {
                    resultat = dizaineNames[laDizaine];
                } else {
                    resultat = dizaineNames[laDizaine] + laLiaison + uniteNames1[lUnite];
                }
                break;
            default:
                resultat = dizaineNames[laDizaine] + laLiaison + uniteNames1[lUnite];
        }
        return resultat;
    }

    private String convertLessThanOneThousand(int number) {
        int lesCentaines = number / 100;
        int leReste = number % 100;
        String sReste = convertZeroToHundred(leReste);

        String resultat;
        switch (lesCentaines) {
            case 0:
                resultat = sReste;
                break;
            case 1:
                resultat = (leReste > 0) ? "cent " + sReste : "cent";
                break;
            default:
                resultat = uniteNames1[lesCentaines] + " cent " + sReste;
                break;
        }
        return resultat;
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
                words.append(convertLessThanOneThousand(number / 1000)).append("mille ");
            }
            number %= 1000;
        }

        if (number > 0) {
            words.append(convertLessThanOneThousand(number));
        }

        return words.toString().trim();
    }

    private String convertFractionsToFrench(int number) {
        DecimalFormat df = new DecimalFormat("000");
        return df.format(number);
    }

    public static void main(String[] args) {
        NumberToText converter = new NumberToText("1234.567");
        System.out.println(converter.toText());
    }
}
