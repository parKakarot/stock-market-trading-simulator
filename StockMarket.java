package market;

import java.util.ArrayList;

/**
 * The StockMarket simulates a single user's stock trading activities over a period of time.
 * It manages stock data, user portfolio, transaction history, and account balance.
 *
 * Linked Lists are used for transaction history and portfolio holdings, while an array
 * is used for stock data.
 *
 * @author Daniel Park
 * * Note:
 * Based on starter code provided in a university Data Structures course.
 * Core logic (StockMarket.java) implemented by the author.
 */
public class StockMarket {
 
    private Stock[] stockData; // All stock data for the market
    private LLNode<Holding> portfolio; // User's current stock holdings
    private LLNode<Transaction> transactions; // History of all transactions
     
    private double balance; 
    private int currentDay; 
    private int transactionCount; 
    private int numHoldings;

    /**
     * Constructs a new StockMarket instance with the specified initial balance.
     * Initializes all data structures to null and sets default values for tracking variables.
     *
     * @param initialBalance the starting balance for trading operations
     */
    public StockMarket(double initialBalance) {
        this.stockData = null;
        this.portfolio = null;
        this.transactions = null;
        this.balance = initialBalance;
        this.currentDay = 0;
        this.transactionCount = 0;
        this.numHoldings = 0;
    }
   
    /**
     * Creates and initializes the stock data array from a CSV input file.
     * Parses the file to extract stock symbols and their historical price data,
     * creating Stock objects for each symbol with complete price history.
     * 
     * The first row contains a single integer, the number of unique stocks.
     * Each subsequent row contains the symbol for a single stock, followed by its price history.
     *
     * @param inputFile the path to the CSV file containing stock data. 
     */
    public void readStockData(String inputFile) {
        // WRITE YOUR CODE HERE 

        StdIn.setFile(inputFile); // reads the file
        int amtofstock = Integer.parseInt(StdIn.readLine()); //reads the first line to see how much stock is being processed

        stockData= new Stock [amtofstock]; // creates the array 

    // ok, so i need to now go through each line of the input file

        int stockindex = 0;
        
        while(StdIn.hasNextLine()){

            String line = StdIn.readLine();
            String[]parts = line.split(",");
            String label = parts[0];
            
            double[]prices = new double [parts.length -1];
            for ( int i =1; i< parts.length; i++){
                prices[i-1] = Double.parseDouble(parts[i]); 
            }
        

        Stock info = new Stock (label,prices);
        stockData[stockindex] = info;

        stockindex++;
        }
       

        

        
    }
 
    /**
     * Advances the simulation to the next day.
     * Increment currentDay by 1. 
     * Then check if currentDay is less than the length of the price history of ANY stock.
     * (all stocks should have the same price history length)
     *
     * @return true if currentDay is less than the stock price history length
     */
    public boolean nextDay() { 
        // WRITE YOUR CODE HERE 
        int totalDays = stockData[0].getPriceHistory().length;

        if( currentDay < totalDays-1 ){
                currentDay++;

            return true;

        }
        else{
    
            return false;
        }

        }
    

    
        


    /**
     * Updates the account balance based on a transaction.
     * Decreases balance for buy transactions and increases for sell transactions.
     *
     * @param qty the quantity of stocks involved in the transaction
     * @param pricePerShare the price per share at the time of transaction
     * @param type the transaction type (Transaction.BUY decreases balance, Transaction.SELL increases balance)
     */
    public void updateBalance(int quantity, double pricePerShare, String type) {
        // WRITE YOUR CODE HERE
        double totalValue = quantity * pricePerShare;

        if ( type.equals("BUY")){
            balance = balance - totalValue;

        }
        else if( type.equals("SELL")){
            balance = balance + totalValue;

        }
    }

    /**
     * Executes a buy transaction for the specified stock and quantity.
     * Attempts to buy the stock if it exists and sufficient funds are available.
     * 
     * View the assignment description for exact implementation details.
     *
     * @param stockID the unique identifier of the stock to purchase
     * @param qty the quantity of shares to buy
     * @return true if transaction successful, false otherwise
     */
    public boolean buyStock(String stockID, int qty) {

        // WRITE YOUR CODE HERE
        Stock chosenStock = null;
        if( qty<=0){
            return false;
        }

        for ( int i = 0; i < stockData.length; i++){
            if ( stockData[i].getStockID().equals( stockID)){
                 chosenStock = stockData[i];
            }
        }
        if ( chosenStock == null){
            return false;
        }
        

        double currentPrice = chosenStock.getPriceHistory()[currentDay];
        double totalPrice = qty * currentPrice;

        if (balance < totalPrice){
            return false;
        }
       int id = getNextHoldingID();
       Holding IronMan = new Holding (id,chosenStock, qty, totalPrice, currentDay);

        LLNode<Holding> newNode = new LLNode<>(IronMan);
       newNode.setNext(portfolio); //newNode points to what portfolio was pointing to 
       portfolio = newNode; // now sets portfolio to newNode

        numHoldings++;
        updateBalance(qty, currentPrice, "BUY");
        addTransaction(IronMan, qty, currentDay,"BUY" );
        
        return true;

       
       
    }

    /**
     * Executes a sell transaction for the specified stock and quantity.
     * Attempts to sell the stock if a holding for it exists. May have to search through and sell 
     * multiple holdings to reach the desired quantity, and if so, should sell the highest value holdings first (ignoring their cost).
     * 
     * View the assignment description for exact implementation details.
     *
     * @param stockID the unique identifier of the stock to sell
     * @param qty the quantity of shares to sell
     * @return true if transaction successful, false otherwise
     */
    public boolean sellStock(String stockID, int qty) {
        // WRITE YOUR CODE HERE

        if ( qty <=0){
            return false;
        }

LLNode <Holding> current = portfolio;
LLNode<Holding> maxNode = null;
LLNode<Holding> prev = null;
LLNode<Holding> maxPrev = null;
double maxValue = 0.0;

while (current != null) {
    Holding h = current.getData();
    if (h.getStock().getStockID().equals(stockID)) {
        double currentPrice = h.getStock().getPriceHistory()[currentDay];
        double value = currentPrice * h.getQuantity();
        if (value > maxValue) {
            maxValue = value;
            maxNode = current;     // remember the best node
            maxPrev = prev;        // remember its previous node
        }
    }
    prev = current;
    current = current.getNext();

    
}

// no matching holding found
if (maxNode == null) {
    return false;
}
        
       // STEP 3: Extract the Holding and compute sale value
    Holding target = maxNode.getData();
    double currentPrice = target.getStock().getPriceHistory()[currentDay];
    double totalValue = qty * currentPrice;

    // STEP 4: Compare quantity in holding vs quantity to sell
    if (target.getQuantity() > qty) {
        // ---- Partial Sale ----
        target.setQuantity(target.getQuantity() - qty);
        updateBalance(qty, currentPrice, "SELL");
        addTransaction(target, qty, currentDay, "SELL");
        return true;
    } 
    else {
        // ---- Full Sale (remove node) ----
        updateBalance(target.getQuantity(), currentPrice, "SELL");
        addTransaction(target, target.getQuantity(), currentDay, "SELL");

        // Remove this node from the linked list
        if (maxPrev == null) {
            // Node is at the front
            portfolio = portfolio.getNext();
        } else {
            maxPrev.setNext(maxNode.getNext());
        }

        numHoldings--;

        int remaining = qty - target.getQuantity();
        if (remaining > 0) {
            return sellStock(stockID, remaining);
        }

        return true;
    }
}
    /**
     * Adds a new transaction to the transaction history linked list.
     * Creates a Transaction object with the provided details and adds it to the front
     * of the transaction history list. Automatically assigns a unique transaction ID.
     *
     * @param s the Stock object involved in the transaction
     * @param transactionDay the day when the transaction occurred
     * @param type the type of transaction, Transaction.BUY or Transaction.SELL
     */
    public void addTransaction(Holding s, int quantity, int transactionDay, String type) {
        // WRITE YOUR CODE HERE

        double pricePerStock = s.getStock().getPriceHistory()[currentDay];
        double totalPrice = pricePerStock * quantity;


        Transaction Batman = new Transaction ( transactionCount, transactionDay, s.getStock(), quantity, pricePerStock, totalPrice, type);
        LLNode <Transaction> newNode = new LLNode <Transaction>(Batman);
        newNode.setNext(transactions);
        transactions = newNode;
        transactionCount++;


        
    }
    
    /**
     * Calculates the Return on Investment (ROI) for a specific stock in the portfolio.
     * ROI is calculated as ((currentValue - totalCost) / totalCost) * 100.
     *
     * @param stockID the unique identifier of the stock to calculate ROI for
     * @return the ROI as a percentage (positive for gains, negative for losses),
     *         or 0.0 if the stock is not found or original price is zero
     */
  public double calculateROI(String stockID) {
    double totalCurrValue = 0;
    double totalCost = 0;

    LLNode<Holding> current = portfolio;

    while (current != null) {
        Holding h = current.getData();
        String thisStockID = h.getStock().getStockID();

        if (thisStockID.equals(stockID)) {
            double currentPrice = h.getStock().getPriceHistory()[currentDay];
            totalCost += h.getCost();
            totalCurrValue += h.getQuantity() * currentPrice;
        }

        current = current.getNext();
    }

    if (totalCost == 0) {
        return 0.0;
    }

    // calculate ROI
    double ROI = ((totalCurrValue - totalCost) / totalCost) * 100.0;
    return ROI;
}

    /** 
     * Iterates through all portfolio holdings to determine which stocks represent
     * the maximum and minimum total profit values.
     *
     * @return a string array containing the two stock IDs for the extremas.
     */
   public String[] findExtrema() {
  if (portfolio == null) {
    return new String[] {"N/A", "N/A"};
}
    LLNode<Holding> current = portfolio;
    Holding firstHolding = current.getData();

    double currentPrice = firstHolding.getStock().getPriceHistory()[currentDay];
    double firstProfit = (firstHolding.getQuantity() * currentPrice) - firstHolding.getCost();

    double maxProfit = firstProfit;
    double minProfit = firstProfit;
    String maxStockID = firstHolding.getStock().getStockID();
    String minStockID = firstHolding.getStock().getStockID();

    // Move to the next node
    current = current.getNext();

    // Traverse the rest of the list
    while (current != null) {
        Holding h = current.getData();
        double price = h.getStock().getPriceHistory()[currentDay];
        double profit = (h.getQuantity() * price) - h.getCost();

        if (profit > maxProfit) {
            maxProfit = profit;
            maxStockID = h.getStock().getStockID();
        }

        if (profit < minProfit) {
            minProfit = profit;
            minStockID = h.getStock().getStockID();
        }

        current = current.getNext();
    }

    return new String[] { maxStockID, minStockID };
}

    // Getter Methods
    public Stock[] getStockData() {  return stockData; } 
    public LLNode<Holding> getPortfolio() {  return portfolio; } 
    public LLNode<Transaction> getTransactions() {  return transactions; } 
    public double getBalance() {  return balance; } 
    public int getCurrentDay() {  return currentDay; } 
    public int getTransactionCount() {  return transactionCount; } 
    public int getHoldings() {  return numHoldings; }

    public int getNextHoldingID() {
        LLNode<Transaction> current = transactions;
        int ID = 1;
        while (current != null) {
            if (Transaction.BUY.equals(current.getData().getType())) {
                ID++;
            }
            current = current.getNext();
        }
        return ID;
    }
}