# Vending Machine Simulator

## Original Brief
You need to design a Vending Machine which follows following requirements

- Accepts coins of 1, 5, 20, 50 pence well as one-pound coin
- Allow user to select products in pence e.g. CANDY (10), SNACK (50), NUTS (75), Coke (150), Bottle Water (100)
- Allow user to take refund by cancelling the request.
- Return selected product and remaining change if any
- Allow reset operation for vending machine supplier

## Pre-requisites
Prequisites to run this app are:
- Java 1.8
- Maven 3.x

## Run Tests
Run the following command in the project directory
```
mvn clean test
```

## Run App
Run the following command in the project directory
```
mvn exec:java -D"exec.mainClass"="com.lammyhill.vendingmachine.VendingMachineRunner"
```
