package com.lammyhill.vendingmachine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VendingMachineSimulatorTest {

    private VendingMachineSimulator vendingMachine;
    private ArrayList<Coin> acceptedCoins;
    private ArrayList<Product> supportedProducts;

    @BeforeEach
    public void setup(){
        acceptedCoins = new ArrayList<Coin>();
        acceptedCoins.add(new Coin("pence", 1));
        acceptedCoins.add(new Coin("pence", 5));
        acceptedCoins.add(new Coin("pence", 20));
        acceptedCoins.add(new Coin("pence", 50));
        acceptedCoins.add(new Coin("pound", 100));

        iAcceptedCoinLoader acceptedCoinLoaderMock = mock(AcceptedCoinLoader.class);
        when(acceptedCoinLoaderMock.loadAcceptedCoins()).thenReturn(acceptedCoins);

        supportedProducts = new ArrayList<Product>();
        supportedProducts.add(new Product("CANDY", 10));
        supportedProducts.add(new Product("SNACK", 50));
        supportedProducts.add(new Product("NUTS", 75));
        supportedProducts.add(new Product("Coke", 150));
        supportedProducts.add(new Product("Bottle Water", 100));

        iInventory inventoryMock = mock(InventorySimulator.class);
        when(inventoryMock.loadSupportedProducts()).thenReturn(supportedProducts);

        vendingMachine = new VendingMachineSimulator(acceptedCoinLoaderMock, inventoryMock);
    }

    @ParameterizedTest
    @ValueSource(ints = {1,5,20,50,100})
    public void shouldAcceptValidCoins(int coinValue) throws UnrecognizedCoinException {
        String denomination = (coinValue % 100 == 0) ?  "pound" : "pence";
        Coin coin = new Coin(denomination, coinValue);
        vendingMachine.insertCoin(coin);


        assertThat(vendingMachine.getBalance()).isEqualTo(coinValue);
    }

    @Test
    public void shouldAcceptMultipleValidCoins() throws UnrecognizedCoinException {
        Coin onePence = new Coin("pence", 1);
        Coin fivePence = new Coin("pence", 5);

        vendingMachine.insertCoin(onePence);
        vendingMachine.insertCoin(fivePence);

        assertThat(vendingMachine.getBalance()).isEqualTo(6);
    }

    @Test
    public void shouldNotAcceptUnrecognisedCoins(){
        Coin tenPenceMock = mock(Coin.class);
        when(tenPenceMock.getValue()).thenReturn(10);
        when(tenPenceMock.getDenomination()).thenReturn("pence");

        assertThrows(UnrecognizedCoinException.class,
                ()->{vendingMachine.insertCoin(tenPenceMock);});


        assertThat(vendingMachine.getBalance()).isEqualTo(0);
    }

    @Test
    public void shouldBeAbleToPurchaseProduct() throws UnrecognizedCoinException, UnrecognizedProductException, InsufficientFundsException {
        Coin fiftyPence = new Coin("pence", 50);
        vendingMachine.insertCoin(fiftyPence);
        Product snack = new Product("SNACK", 50);

        assertThat(vendingMachine.purchaseProduct(snack)).isEqualTo(true);
    }

    @Test
    public void shouldCalculateBalanceAfterPurchase() throws UnrecognizedCoinException, UnrecognizedProductException, InsufficientFundsException {
        Coin fiftyPence = new Coin("pence", 50);
        vendingMachine.insertCoin(fiftyPence);
        Product snack = new Product("SNACK", 50);

        assertThat(vendingMachine.getBalance()).isEqualTo(50);
        vendingMachine.purchaseProduct(snack);
        assertThat(vendingMachine.getBalance()).isEqualTo(0);
    }

    @Test
    public void shouldNotAllowPurchaseOfUnsupportedProduct() throws UnrecognizedCoinException{
        Coin fiftyPence = new Coin("pence", 50);
        vendingMachine.insertCoin(fiftyPence);
        Product juice = new Product("JUICE", 50);

        assertThrows(UnrecognizedProductException.class,
                ()->{vendingMachine.purchaseProduct(juice);});
    }

    @Test
    public void shouldNotDeductBalanceForUnsupportedProduct() throws UnrecognizedCoinException, InsufficientFundsException {
        Coin fiftyPence = new Coin("pence", 50);
        vendingMachine.insertCoin(fiftyPence);
        Product juice = new Product("JUICE", 50);

        try {
            vendingMachine.purchaseProduct(juice);
        }
        catch(UnrecognizedProductException e){
            //catching exception so execution can continue so we can validate balance
        }
        assertThat(vendingMachine.getBalance()).isEqualTo(50);
    }

    @Test
    public void shouldNotDispenseProductIfInsufficientFunds() throws UnrecognizedCoinException,UnrecognizedProductException {
        Coin onePence = new Coin("pence", 1);
        vendingMachine.insertCoin(onePence);
        Product snack = new Product("SNACK", 50);

        assertThrows(InsufficientFundsException.class,
                ()->{vendingMachine.purchaseProduct(new Product("CANDY", 10));});
    }

    @Test
    public void shouldNotChangeBalanceWhenInsufficientFunds() throws UnrecognizedCoinException, UnrecognizedProductException{
        Coin onePence = new Coin("pence", 1);
        vendingMachine.insertCoin(onePence);
        Product snack = new Product("SNACK", 50);

        try {
            vendingMachine.purchaseProduct(snack);
        }
        catch(InsufficientFundsException e){
            //handling exception so execution can continue and validate balance
        }
        assertThat(vendingMachine.getBalance()).isEqualTo(1);
    }

    @Test
    public void shouldReturnChangeAfterPurchase() throws UnrecognizedCoinException, UnrecognizedProductException, InsufficientFundsException {
        Coin fiftyPence = new Coin("pence", 50);
        vendingMachine.insertCoin(fiftyPence);
        Product candy = new Product("CANDY", 10);

        vendingMachine.purchaseProduct(candy);

        ArrayList<Coin> expectedChangeInCoins = new ArrayList<>();
        expectedChangeInCoins.add(new Coin("pence", 20));
        expectedChangeInCoins.add(new Coin("pence", 20));
        assertThat(vendingMachine.returnBalance()).isEqualTo(expectedChangeInCoins);
    }

    @Test
    public void shouldReturnBalanceUsingLeastCoins() throws UnrecognizedCoinException {
        vendingMachine.insertCoin(new Coin("pence", 50));
        vendingMachine.insertCoin(new Coin("pence", 50));
        vendingMachine.insertCoin(new Coin("pence", 50));
        vendingMachine.insertCoin(new Coin("pence", 20));
        vendingMachine.insertCoin(new Coin("pence", 5));
        vendingMachine.insertCoin(new Coin("pence", 1));

        ArrayList expectedChangeInCoins = new ArrayList(acceptedCoins);
        ArrayList actualChangeInCoins = vendingMachine.returnBalance();
        Collections.sort(expectedChangeInCoins);
        Collections.sort(actualChangeInCoins);

        assertThat(actualChangeInCoins).isEqualTo(expectedChangeInCoins);
    }

    @Test
    public void shouldReturnBalanceWithoutPurchase() throws UnrecognizedCoinException{
        Coin fiftyPence = new Coin("pence", 50);
        vendingMachine.insertCoin(fiftyPence);

        ArrayList<Coin> expectedChangeInCoins = new ArrayList<>();
        expectedChangeInCoins.add(fiftyPence);

        assertThat(vendingMachine.returnBalance()).isEqualTo(expectedChangeInCoins);
    }

    @Test
    public void shouldResetBalance() throws UnrecognizedCoinException{
        Coin fiftyPence = new Coin("pence", 50);
        vendingMachine.insertCoin(fiftyPence);

        assertThat(vendingMachine.getBalance()).isEqualTo(50);
        vendingMachine.reset();
        assertThat(vendingMachine.getBalance()).isEqualTo(0);
    }

    @Test
    public void shouldResetAcceptedProducts() throws UnrecognizedCoinException, UnrecognizedProductException, InsufficientFundsException {
        ArrayList<Coin> acceptedCoins = new ArrayList<Coin>(); //Accepted coins before reset
        acceptedCoins.add(new Coin("pence", 100));

        iAcceptedCoinLoader acceptedCoinLoaderMock = mock(AcceptedCoinLoader.class);
        when(acceptedCoinLoaderMock.loadAcceptedCoins()).thenReturn(acceptedCoins);

        ArrayList<Product> supportedProducts = new ArrayList<Product>();
        supportedProducts.add(new Product("CANDY", 10));
        supportedProducts.add(new Product("SNACK", 50));

        ArrayList<Product> supportedProductsReducedList = new ArrayList<Product>();
        supportedProductsReducedList.add(new Product("SNACK", 50));

        iInventory inventoryMock = mock(InventorySimulator.class);
        when(inventoryMock.loadSupportedProducts()).thenReturn(supportedProducts);

        vendingMachine = new VendingMachineSimulator(acceptedCoinLoaderMock, inventoryMock);
        vendingMachine.insertCoin(new Coin("pence", 100));
        vendingMachine.purchaseProduct(new Product("CANDY", 10));

        when(inventoryMock.loadSupportedProducts()).thenReturn(supportedProductsReducedList);

        vendingMachine.reset();

        vendingMachine.insertCoin(new Coin("pence",100));
        vendingMachine.purchaseProduct(new Product("SNACK", 50)); //should not throw exception

        assertThrows(UnrecognizedProductException.class,
                ()->{vendingMachine.purchaseProduct(new Product("CANDY", 10));});
    }

    @Test
    public void shouldResetAcceptedCoins() throws UnrecognizedCoinException{
        ArrayList<Coin> acceptedCoins = new ArrayList<Coin>(); //Accepted coins before reset
        acceptedCoins.add(new Coin("pence", 1));
        acceptedCoins.add(new Coin("pence", 5));

        ArrayList<Coin> acceptedCoinsReducedList = new ArrayList<Coin>(); //Accepted coins after reset

        iAcceptedCoinLoader acceptedCoinLoaderMock = mock(AcceptedCoinLoader.class);
        when(acceptedCoinLoaderMock.loadAcceptedCoins()).thenReturn(acceptedCoins);

        iInventory inventoryMock = mock(InventorySimulator.class);

        vendingMachine = new VendingMachineSimulator(acceptedCoinLoaderMock, inventoryMock);
        vendingMachine.insertCoin(new Coin("pence", 1));
        vendingMachine.insertCoin(new Coin("pence", 5));

        acceptedCoinsReducedList.add(new Coin("pence", 1));
        when(acceptedCoinLoaderMock.loadAcceptedCoins()).thenReturn(acceptedCoinsReducedList);

        vendingMachine.reset();

        vendingMachine.insertCoin(new Coin("pence",1)); //should not throw exception
        assertThrows(UnrecognizedCoinException.class,
                ()->{vendingMachine.insertCoin(new Coin("pence",5));});
    }
}
