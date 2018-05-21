package com.lammyhill.vendingmachine;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class VendingMachineCLIIntegrationTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @AfterAll
    public static void teardown(){
        //Restoring input/output streams
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void insertValidCoinUpdateBalance(){
        String input = "1\n1\n5";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        System.setOut(new PrintStream(outContent));

        VendingMachineCLI vendingMachineCLI = new VendingMachineCLI();
        vendingMachineCLI.runVendingMachine();

        assertThat(outContent.toString()).contains("Coin accepted\n" +
                                                    "Your updated balance is: 1");
    }

    @Test
    public void canPurchaseItemAndGetChange(){
        String input = "1\n5\n2\n2\ny\n5";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        System.setOut(new PrintStream(outContent));

        VendingMachineCLI vendingMachineCLI = new VendingMachineCLI();
        vendingMachineCLI.runVendingMachine();

        assertThat(outContent.toString()).contains("Purchased: SNACK\n" +
                                                    "Your change is given back in the following coin(s):\n" +
                                                    "50 pence");
    }

    @Test
    public void canCancelPurchaseAndGetRefund(){
        String input = "1\n5\n2\n2\nn\n3\n5";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        System.setOut(new PrintStream(outContent));

        VendingMachineCLI vendingMachineCLI = new VendingMachineCLI();
        vendingMachineCLI.runVendingMachine();

        assertThat(outContent.toString()).contains("Are you sure you want to purchase SNACK? (y/n)\n" +
                                                    "Purchase cancelled.");
        assertThat(outContent.toString()).contains("Your balance is given back in the following coin(s):\n" +
                                                    "100 pence");

    }

    @Test
    public void canResetVendingMachine(){
        String input = "1\n1\n4\n5";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        System.setOut(new PrintStream(outContent));

        VendingMachineCLI vendingMachineCLI = new VendingMachineCLI();
        vendingMachineCLI.runVendingMachine();

        assertThat(outContent.toString()).contains("Coin accepted\n" +
                "Your updated balance is: 1");
        assertThat(outContent.toString()).contains("Resetting Vending Machine...");
        assertThat(outContent.toString()).contains("Current balance is: 0");
    }
}
