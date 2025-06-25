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

## Currency Conversion Algorithm

### Key Features

- **Direct Lookup**:
   - Retrieves exchange rates directly from `exchangeRatesList` for quick access and adds them to a HashMap for O(1) time complexity.

- **Graph-Based DFS Search**:
   - If no direct rate is found, it recursively explores all paths to find indirect conversions, while adding the traversed nodes to the HashMap.

- **Efficient Updates**:
   - New exchange rates are added only if they do not already exist, optimizing both time and memory usage.

- **Scalability**:
   - Efficiently handles large currency sets with minimal recomputations, as all rates are calculated initially and stored in a HashMap for fast lookups.

- **Graceful Fallback**:
   - Returns `-1` if no conversion path exists between two currencies.


### How It Works

1. **Initialize**: Collect all unique currencies.
2. **Populate Costs**: For each currency pair, attempt to find an exchange rate using DFS.
3. **DFS Search**: Recursively explore possible exchange paths, updating rates as new paths are found.
4. **Bidirectional Updates**: Store both `from->to` and `to->from` rates for each pair.

### Optimizations

- **Cycle Prevention** ensures no redundant calculations.
- **Efficient Updates** prevent duplication in the `costs` map.
- **Scalable** for large sets of currencies with minimal performance hit.

## Error Handling

Error handling is implemented through custom exceptions in the `Bank` class's search functions and the `CommandFactory`:
- `AccountNotFoundException`
- `CardNotFoundException`
- `UserNotFoundException`
- `CommandNotFoundException`

These exceptions ensure that invalid operations are caught and handled gracefully.

---

## Conclusion

This system demonstrates a comprehensive and structured approach to managing a complex banking ecosystem while adhering to best practices in software design and implementation.


## Feedback

1. Create the assignment in time next time, please
2. Add the corresponding output so that we know what to do (especially the error cases)
3. Expand on the explaining of the project requirements
4. Be more active on the Forum
5. Announce everyone when a change with the checker/tests happens on Teams
