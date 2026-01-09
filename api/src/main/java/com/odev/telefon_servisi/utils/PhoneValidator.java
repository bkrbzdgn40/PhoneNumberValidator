package com.odev.telefon_servisi.utils;

public class PhoneValidator {
    public static class ValidationResult {
        public boolean hasNonZeroDigit;
        public boolean sumFirstEqualsLast;
        public boolean sumOddEqualsEven;
        public boolean isValid;
        public ValidationResult(boolean hasNonZeroDigit, boolean sumFirstEqualsLast, boolean sumOddEqualsEven) {
            this.hasNonZeroDigit = hasNonZeroDigit;
            this.sumFirstEqualsLast = sumFirstEqualsLast;
            this.sumOddEqualsEven = sumOddEqualsEven;
            this.isValid = hasNonZeroDigit && sumFirstEqualsLast && sumOddEqualsEven;
        }
    }

    public static ValidationResult validate(String phone) {

        if (phone == null || !phone.matches("\\d{6}")) {
            return new ValidationResult(false, false, false);
        }


        int[] digits = new int[6];
        int toplam = 0;
        for (int i = 0; i < 6; i++) {
            digits[i] = phone.charAt(i) - '0';
            toplam += digits[i];
        }


        boolean rule1 = (toplam > 0);


        int firstHalf = digits[0] + digits[1] + digits[2];
        int secondHalf = digits[3] + digits[4] + digits[5];
        boolean rule2 = (firstHalf == secondHalf);


        int tek = digits[0] + digits[2] + digits[4];  // 1., 3., 5. basamaklar
        int cift = digits[1] + digits[3] + digits[5]; // 2., 4., 6. basamaklar
        boolean rule3 = (tek == cift);


        return new ValidationResult(rule1, rule2, rule3);
    }

}
