# GwentStone Lite

## Description
GwentStone Lite is a strategic card game where two players compete by deploying cards on a battleground. Each player has a hero, and when a hero’s health reaches zero, the respective player loses the game. The gameplay includes the following stages:

1. **Deck Selection**: Each player begins with multiple card decks and selects one deck.
2. **Deck Shuffling**: The chosen decks are shuffled at the start of the game.
3. **Round Progression**: In each round, players gain mana and draw a card from their deck. They use this mana to place cards on the table or to activate hero abilities.
4. **Actions and Attacks**: Each card can attack once per round, allowing players to strategically weaken or defeat the opposing hero.

## Inheritance Structure

The project’s object-oriented structure is centered on inheritance:

- **`Card` Class**: Serves as the base class, representing a generic card.
- **`Minion` and `Hero` Classes**: These are subclasses that extend `Card` with specific functionalities:
  - **Minion**: Represents typical cards that can be deployed on the battleground with unique abilities.
  - **Hero**: Represents each player's main character. When a hero is defeated, the game is lost for that player.

## Design Pattern - Singleton

The `Game` class uses the **Singleton pattern** to ensure that only one instance of the game exists at a time. This instance tracks the game’s progress and updates fields accordingly for each test case.

- **Implementation**: The `applyParams` function in `main` sets up the game by updating relevant fields and restarting the flow as needed.
- **Game Statistics**: The `Game` class also maintains a `statistics` field, which is updated only once, providing cumulative data across tests.

## Game Logic Flow

The main game logic resides within the `Game` class, which coordinates each round and manages actions. The logic includes:

1. **Setup Phase**: Initializes fields such as players, decks, and heroes.
2. **Action Loop**: Iterates over all actions, ensuring each action produces the correct output based on the game state.

## Potential Improvements

- **Player Class Enhancements**: Moving the current player’s hand into the `Player` class for better encapsulation.
- **Control Structures**: Consideing to replace multiple `if` statements with `switch` statements to improve readability and elegance.

## Difficulties and Challenges

- **Understanding Main Logic**: The integration of main game logic with input handling was initially challenging. However, once this flow was understood, it clarified the structure and made implementation smoother.
- **Organizing Classes**: As a first OOP project, organizing classes in an efficient and logical structure was challenging but also rewarding as it improved understanding of OOP principles.
