package tubes.util;

public class CurrencyUtil {

    public static String formatShort(long amount) {
        return "Rp " + String.format("%,d", amount).replace(',', '.');
    }
}
