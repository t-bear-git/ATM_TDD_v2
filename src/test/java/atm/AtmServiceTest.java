package atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AtmServiceTest {

    AtmService atm;
    Bank bank;
    CardController cardController;

    @BeforeEach
    void setUp() {
        bank = mock(Bank.class);
        cardController = mock(CardController.class);
        atm = new AtmService(cardController, bank);

    }

    @Test
    void should_returnCardUser_when_cardIsReadInAtm() {
        String expected = "Teddie Liljekvist";
        when(cardController.getCardId()).thenReturn("90220922");
        when(bank.validateCardId("90220922")).thenReturn(true);
        when(bank.getCardUser("90220922")).thenReturn(new User("Teddie Liljekvist", 9022));
        when(bank.pinAttLimit("90220922")).thenReturn(3);

        assertEquals(expected, atm.readCard());

    }

    @Test
    void should_validatePinAndLogIn_when_CorrectPin() {

        String expected = "Pin number correct. Logged in!";
        String cardNumber = "90220922";
        int pin = 2882;

        when(cardController.getCardId()).thenReturn(cardNumber);
        when(cardController.pinCode()).thenReturn(pin);
        when(bank.validatePin(cardNumber, pin)).thenReturn(true);

        assertEquals(expected, atm.enterPin());

    }

    @Test
    void should_validatePinAndShowRemainingAttempts_when_FirstIncorrectPin() {

        String expected = "Pin number incorrect. 2 Attempts left.";
        String cardNumber = "90220922";
        int pin = 2882;

        when(cardController.getCardId()).thenReturn(cardNumber);
        when(cardController.pinCode()).thenReturn(2883);
        when(bank.validatePin(cardNumber, pin)).thenReturn(false);
        when(bank.pinAttLimit(cardNumber)).thenReturn(2);

        assertEquals(expected, atm.enterPin());

    }

    @Test
    void should_BlockCardRead_when_CardIsBlocked() {

        String expected = "Card is blocked.";
        when(cardController.getCardId()).thenReturn("90220922");
        when(bank.validateCardId("90220922")).thenReturn(true);
        when(bank.getCardUser("90220922")).thenReturn(new User("Teddie Liljekvist", 9022));
        when(bank.pinAttLimit("90220922")).thenReturn(0);

        assertEquals(expected, atm.readCard());

    }

    @Test
    void should_ReturnAvailableFunds_when_getAvailableFundsIsCalled() {

        int funds = 100;
        String expected = "The account have " + funds + " available.";
        when(cardController.getCardId()).thenReturn("90220922");
        when(bank.getAvailableFunds("90220922")).thenReturn(funds);


        assertEquals(expected, atm.checkAvailableFunds());

    }

    @Test
    void should_DepositFunds_when_DepositFundsIsCalled() {

        when(cardController.getCardId()).thenReturn("90220922");
        when(cardController.getFunds()).thenReturn(100);

        atm.depositFunds();

        verify(bank).depositFundsWithCard(anyString(), anyInt());
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 50, 100, 500, 1000})
    void should_WithdrawFunds_when_WithdrawFundsIsCalled(int fundsToWithdraw) {

        // int fundsToWithdraw = 500;
        String expected = fundsToWithdraw + " has been withdrawn from account.";
        when(cardController.getCardId()).thenReturn("90220922");
        when(cardController.getFunds()).thenReturn(fundsToWithdraw);
        when(bank.getAvailableFunds("90220922")).thenReturn(1000);

        String actual = atm.withdrawFunds();

        verify(bank).withdrawFundsWithCard(anyString(), anyInt());
        assertEquals(expected , actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 500, 1000})
    void should_NotWithdraw_when_AvailableFundsToLow(int fundsToWithdraw) {

        // int fundsToWithdraw = 500;
        String expected = "Withdraw failed. Account balance to low.";
        when(cardController.getCardId()).thenReturn("90220922");
        when(cardController.getFunds()).thenReturn(fundsToWithdraw);
        when(bank.getAvailableFunds("90220922")).thenReturn(100);


        assertEquals(expected , atm.withdrawFunds());
        verify(bank).getAvailableFunds("90220922");

    }

    @Test
    void should_EjectCard_when_ServiceClosed() {

        when(cardController.getCardId()).thenReturn("90220922");

        atm.closeService();

        verify(cardController).ejectCard();

    }

    // MockedStatic not working.
    @Test
    void should_ShowBankName() {


        try {
            String bankName = "ICA Banken";
            String expected = "You are using " + bankName;
            MockedStatic<Bank> bankMockedStatic = mockStatic(Bank.class);

            bankMockedStatic.when(() -> Bank.getBankName()).thenReturn(bankName);


            String actual = atm.showBankName();

            assertEquals(expected, actual);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}