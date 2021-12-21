package atm;

public class Bank {

    public boolean validateCardId(String id) {
        return false;
    }

    public User getCardUser(String string) {
        return null;
    }

    public boolean validatePin(String cardNumber, int pin) {
        return false;
    }

    public int pinAttLimit(String insertedCard) {
        return 1;
    }

    public int getAvailableFunds(String insertedCard) {
        return 0;
    }

    public void depositFundsWithCard(String insertedCard, int funds) {
    }

    public void withdrawFundsWithCard(String insertedCard, int funds) {
    }

    public static String getBankName() {
        return null;
    }
}
