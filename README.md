# J. POO Morgan Chase & Co.

Disclaimer! This README was initially created by the author and later enhanced by AI to improve clarity and structure.

## Description

I have developed a complex banking system capable of managing users, accounts, and cards. The system supports various functionalities such as adding users, handling accounts and cards, printing transaction details, and generating transaction reports. It also supports a wide array of transactions, including:

- Setting a minimum account balance.
- Checking a card's status.
- Performing payments via banking transfer or card.
- Facilitating shared payments.
- Assigning aliases to accounts.

---

## Inheritance Structure

### 1. `Account` Class
- Serves as the foundational base class for `ClassicAccount` and `SavingsAccount` classes.
- Represents a generic account, encapsulating shared attributes and behaviors.

### 2. `Card` Class
- Serves as the foundational base class for `OneTimeCard` and `RegularCard` classes.
- Represents a generic credit card, encapsulating shared attributes and behaviors.

### 3. `Command` Interface
- A foundational interface for all types of commands.
- Defines a single `execute` function, responsible for executing a specific command.

### 4. `Visitor` Interface
- Acts as the base interface for the `ReportVisitor` and `SpendingsReportVisitor` classes.
- Allows visiting the two types of account objects.

---

## Design Patterns

### 1. Singleton Pattern (`Bank` Class)
- Ensures that only one instance of the `Bank` class exists throughout the application.
- Tracks all input information and manages users.

#### Key Implementation
- The `applyParams` function initializes or resets the `Bank` instance with new configurations, ensuring seamless management and consistent state across the application.

---

### 2. Factory Pattern (`Command` Creation)
- Utilized through the `CommandFactory` abstract class.
- Dynamically creates specific `Command` instances based on the input processed by the `InputParser` class.

---

### 3. Visitor Pattern

#### a) **Report Commands**
- Implemented to generate two different types of reports (`ReportVisitor` and `SpendingsReportVisitor`) for two types of accounts.
- Ensures compliance with Object-Oriented Programming (OOP) principles and avoids the use of the `instanceOf` keyword, which is considered a poor design practice.

#### b) **PayOnlineVisitor**
- Used for the `PayOnline` command.
- Executes payments differently based on the type of card specified in the input.

---

### 4. Command Pattern (Invoker: `CommandParser` Class)
- All command classes include an `execute` function.
- The `CommandParser` class acts as the invoker, iterating through all commands and executing them in sequence, encapsulating command execution logic.

---

## Currency Graph

### Key Features

1. **Direct Lookup**:
    - Initially attempts to find the exchange rate in `exchangeRatesList`, a pre-initialized list of known exchange rates.
    - Ensures quick results for directly mapped currency pairs.

2. **Graph-Based Search**:
    - Performs a Breadth-First Search (BFS) if no direct match is found.
    - Dynamically explores the graph to ensure all possible paths to the target currency are considered.

3. **Dynamic Updates**:
    - Adds reverse exchange rates and intermediary paths dynamically during BFS.
    - Enhances the completeness of `exchangeRatesList` for future queries.

4. **Cycle Prevention**:
    - Uses a `visited` set during BFS to prevent revisiting nodes, avoiding infinite loops and redundant calculations.

5. **Efficiency**:
    - Ensures exchange rates are only added to `exchangeRatesList` if they do not already exist, preventing duplication and optimizing memory usage.

6. **Graceful Fallback**:
    - If no valid exchange path exists, the method returns `-1` as a sentinel value, indicating that the exchange is not possible.

---

## Error Handling

Error handling is implemented through custom exceptions in the `Bank` class's search functions:
- `AccountNotFoundException`
- `CardNotFoundException`
- `UserNotFoundException`

These exceptions ensure that invalid operations are caught and handled gracefully.

---

## Potential Improvements

1. **Enhanced Data Structures**:
    - Using a `Map` instead of an `ArrayList` for the `exchangeRatesList` field would improve lookup efficiency and overall performance.

2. **Scalability**:
    - Introducing additional optimizations in graph traversal, such as weighted pathfinding, could improve performance for larger datasets.

---

## Conclusion

This system demonstrates a comprehensive and structured approach to managing a complex banking ecosystem while adhering to best practices in software design and implementation.
