Stock Trading Simulator

A Java-based stock trading simulator that models real-world portfolio management using historical stock data. This application features a graphical user interface (GUI), transaction tracking, and analytical tools to evaluate trading performance.

Features
	•	Interactive GUI built with Java Swing
	•	Simulated stock market using historical CSV data
	•	Buy and sell stocks with real-time balance updates
	•	Portfolio tracking using linked list data structures
	•	Transaction history logging
	•	Performance analytics including:
	•	Return on Investment (ROI)
	•	Net worth tracking over time
	•	Highest and lowest performing holdings
	•	Price history visualization through dynamic charts

Technologies Used
	•	Java
	•	Java Swing (GUI)
	•	Custom Linked List implementation
	•	File I/O (CSV parsing)

How It Works
	1.	The program loads stock data from a CSV file.
	2.	Users start with an initial balance.
	3.	Stocks can be bought and sold over simulated days.
	4.	The system updates portfolio value and balance dynamically.
	5.	Analytics provide insight into trading performance.



 Project Structure
	•	Driver.java – Main GUI and application controller
	•	StockMarket.java – Core trading logic
	•	Stock.java – Stock data model
	•	Transaction.java – Transaction records
	•	Holding.java – Portfolio holdings
	•	LLNode.java – Linked list node implementation

Future Improvements
	•	Add support for real-time API stock data
	•	Implement more advanced trading strategies
	•	Improve UI/UX design
	•	Add persistent storage (save/load sessions)
