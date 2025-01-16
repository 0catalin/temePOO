# J. POO Morgan Chase & Co.

Disclaimer! This README was initially created by the author and later enhanced by AI to improve clarity and structure.

## Description

I have developed a complex banking system capable of managing users, accounts, and cards. The system supports various functionalities such as adding users, handling accounts and cards, printing transaction details, and generating transaction reports. It also supports a wide array of transactions, including:

- Setting a minimum account balance.
- Checking a card's status.
- Performing payments via banking transfer or card.
- Facilitating shared payments.
- Assigning aliases to accounts.
- Creating a business account, which can provide access to multiple users.
- Implementation of real-time shared payment.
- Implementation of a user plan and cashback system.
---

## Inheritance Structure

### 1. `Account` Class
- Serves as the foundational base class for `ClassicAccount`, `SavingsAccount`, and `BusinessAccount` classes.
- Represents a generic account, encapsulating shared attributes and behaviors.

### 2. `Card` Class
- Serves as the foundational base class for `OneTimeCard` and `RegularCard` classes.
- Represents a generic credit card, encapsulating shared attributes and behaviors.

### 3. `Command` Interface
- A foundational interface for all types of commands.
- Defines a single `execute` function, responsible for executing a specific command.

### 4. `Visitor` Interface
- Acts as the base interface for the `ReportVisitor`, `SpendingsReportVisitor`, and other `Command` classes that require handling different account types.
- Allows visiting the three types of account objects.

### 5. Other Classes and Interfaces
- Includes supporting classes/interfaces such as `Strategy`, `RoleBasedAccessControl`, and others that provide additional functionality.
- Each class follows a clear inheritance structure and adheres to key design principles to ensure maintainability and extensibility.
---

## Design Patterns

---

### 1. Singleton Pattern (`Bank` Class)
#### **Motivation**:
The Singleton Pattern ensures that there is only one instance of the `Bank` class throughout the application. This is critical for maintaining a centralized and consistent management of users, accounts, and transactions. It avoids potential issues arising from having multiple instances of the core class managing the system's state.

#### **Location**:
- Implemented in the `Bank` class.
- The `applyParams` method initializes or resets the `Bank` instance with configurations to ensure consistent state management.

---

### 2. Factory Pattern (Creation of `Command` or `Strategy`)
#### **Motivation**:
The Factory Pattern was used to make the system easily extensible by enabling the creation of new commands and strategies dynamically. This reduces the need for hardcoding and simplifies the addition of new functionalities as the system evolves.

#### **Location**:
- Implemented via the `CommandFactory` abstract class for creating specific `Command` instances.
- Similarly, used for dynamically creating `Strategy` instances based on the input provided.

---

### 3. Visitor Pattern
#### **Motivation**:
The Visitor Pattern was implemented to simplify the addition of new account types and commands without modifying existing code. It also avoids the use of the `instanceOf` keyword, which is considered a poor design practice, ensuring better adherence to OOP principles.

#### **Location**:
- **Account Visitor**: Used to generate reports (e.g., `ReportVisitor`, `SpendingsReportVisitor`, etc.) and handle commands requiring different behaviors for each account type.
- **Card Visitor**: Applied in the `PayOnline` command and for changing card IDs, enabling distinct implementations based on card type.

---

### 4. Command Pattern (Invoker: `CommandParser` Class)
#### **Motivation**:
The Command Pattern was chosen to encapsulate the execution logic of various commands and make it easier to add or modify commands without affecting other parts of the code. It promotes a clean separation of concerns between command execution and parsing logic.

#### **Location**:
- Each command class implements the `execute` method to define its specific behavior.
- The `CommandParser` class acts as the invoker, iterating through commands and executing them in sequence.

---

### 5. Strategy Pattern
#### **Motivation**:
The Strategy Pattern was used to support multiple cashback strategies without affecting other parts of the code. This allows the system to dynamically apply different cashback policies based on specific criteria.

#### **Location**:
- Implemented via the `Strategy` interface, with concrete implementations for different cashback and coupon storage strategies.
- These strategies are applied during payments to valid commerciants.

---

### 6. Builder Pattern
#### **Motivation**:
The Builder Pattern simplifies the creation of objects with optional or varying fields. It was particularly useful for handling `SpendingUserInfo` objects, which may not always require all fields to be initialized for every operation.

#### **Location**:
- Implemented in the `SpendingUserInfoBuilder` class to construct instances of `SpendingUserInfo`.

---

### 7. Observer Pattern
#### **Motivation**:
The Observer Pattern was utilized to track and manage shared payments efficiently, particularly for users with multiple accounts involved in the same transaction. This ensures updates to payment statuses are handled consistently.

#### **Location**:
- Observers are implemented in the `PayAllObserver` class, which is used by both `SplitPaymentInfo` and `SplitPaymentInfoNotEqual` classes.

---

### 8. Role-Based Access Control Pattern
#### **Motivation**:
This pattern ensures that permissions for business accounts are managed dynamically and accurately, based on the roles of users (`owner`, `manager`, `employee`). It allows quick verification of whether a user has permission to perform a specific operation.

#### **Location**:
- Implemented through a `RoleBasedAccessControl` instance in each `BusinessAccount` class.
- Used to determine whether a user is authorized for a specific action.

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
- `CommerciantNotFoundException`
- `EmailNotFoundException`
- `PaymentInfoNotFoundException`
- `StrategyNotFoundException`

These exceptions ensure that invalid operations are caught and handled gracefully.

---
## Usage of Streams

`Usage of Streams`: Streams are employed to efficiently filter `SpendingUserInfos` within the `BusinessReport` command visitors, as well as in `ReportVisitor` and `Report`. They simplify tasks such as filtering or sorting objects (e.g., arranging alphabetically) within an `ArrayList`.

---
## Conclusion

This system demonstrates a comprehensive and structured approach to managing a complex banking ecosystem while adhering to best practices in software design and implementation.


## Feedback

1. Create the assignment in time next time, please
2. Add the corresponding output so that we know what to do (especially the error cases)
3. Expand on the explaining of the project requirements
4. Announce everyone when a change with the checker/tests happens on Teams
