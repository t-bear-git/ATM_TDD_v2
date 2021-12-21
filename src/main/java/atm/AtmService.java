package atm;

public class AtmService {

    CardController cardController;
    Bank bank;
    User atmUser;
    String insertedCard;

    AtmService(CardController cardController, Bank bank) {
        this.cardController = cardController;
        this.bank = bank;
    }

    public String readCard() {

        if (insertedCard == null) {
            getCardID();
        }
        int remainingTries = bank.pinAttLimit(insertedCard);
        if (remainingTries > 0) {
            if (bank.validateCardId(insertedCard)) {
                atmUser = bank.getCardUser(insertedCard);
                return atmUser.getName();
            }
        } else if (remainingTries == 0) {
            return "Card is blocked.";
        }
        return "Failed to read card. Try again.";

    }

    public String enterPin() {

        if (insertedCard == null) {
            getCardID();
        }
        int pin = cardController.pinCode();
        if (bank.validatePin(insertedCard, pin)) {
            return "Pin number correct. Logged in!";
        } else {
            int remainingTries = bank.pinAttLimit(insertedCard);
            return "Pin number incorrect. " + remainingTries + " Attempts left.";
        }
    }

    public String checkAvailableFunds() {

        if (insertedCard == null) {
            getCardID();
        }

        int funds = bank.getAvailableFunds(insertedCard);

        return "The account have " + funds + " available.";

    }

    public void depositFunds() {

        if (insertedCard == null) {
            getCardID();
        }
        int funds = cardController.getFunds();

        if (funds > 10) {
            bank.depositFundsWithCard(insertedCard, funds);
        }

    }

    public String withdrawFunds() {

        if (insertedCard == null) {
            getCardID();
        }

        int fundsToWithdraw = cardController.getFunds();
        int availableFunds = bank.getAvailableFunds(insertedCard);

        if (fundsToWithdraw <= availableFunds) {
            bank.withdrawFundsWithCard(insertedCard, fundsToWithdraw);
            return fundsToWithdraw + " has been withdrawn from account.";
        } else {
            return "Withdraw failed. Account balance to low.";
        }

    }

    public String showBankName() {

        if (insertedCard == null) {
            getCardID();
        }
        return "You are using " + Bank.getBankName();

    }

    public void closeService() {

        if (insertedCard == null) {
            getCardID();
        }
        cardController.ejectCard();
        insertedCard = null;

    }

    private void getCardID(){
        insertedCard = cardController.getCardId();
    }



}
