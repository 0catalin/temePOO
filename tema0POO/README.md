# GwentStone Lite
Disclaimer! This README was initially created by the author and later enhanced by AI to improve clarity and structure.
## Description
GwentStone Lite is a strategic card game where two players compete by deploying cards on a battleground. Each player has a hero, and when a hero’s health reaches zero, the respective player loses the game. The gameplay includes the following stages:

1. **Deck Selection**: Each player begins with multiple card decks and selects one deck.
2. **Deck Shuffling**: The chosen decks are shuffled at the start of the game.
3. **Round Progression**: In each round, players gain mana and draw a card from their deck. They use this mana to place cards on the table or to activate hero abilities.
4. **Actions and Attacks**: Each card can attack once per round, allowing players to strategically weaken or defeat the opposing hero.

## Inheritance Structure

1. **`Card` Class**:
  - Serves as the foundational base class for all cards in the game.
  - Represents a generic card, encapsulating shared attributes and behaviors.

2. **`Minion` and `Hero` Classes**:
  - **`Minion` Class**:
    - Extends the `Card` class, representing cards deployable on the battleground.
    - Includes specialized abilities and attributes tailored for gameplay interactions.
  - **`Hero` Class**:
    - Extends the `Card` class, representing the player's primary character.
    - The defeat of a `Hero` results in the loss of the game for that player.
    - Acts as a base class for specific hero types created by the **HeroFactory**.

## Design Patterns

1. **Singleton Pattern (Game Class)**:
  - Ensures that only one instance of the `Game` class exists throughout the application.
  - Tracks the game’s progress, including fields updated dynamically during gameplay.
  - **Key Implementation**:
    - The `applyParams` function is responsible for initializing and resetting the `Game` instance with new configurations and restarting the game flow as necessary.
  - **Statistics Management**:
    - Maintains a cumulative `statistics` field updated across all test cases, providing valuable insights into the game's performance and outcomes.

2. **Factory Pattern (Hero Creation)**:
  - Utilized through the `HeroFactory` class.
  - Dynamically creates specific hero instances based on parameters, simplifying the management of different hero types while promoting extensibility.

3. **Factory Pattern (Minion Creation)**:
  - Utilized through the `MinionFactory` class.
  - Dynamically creates specific minion instances based on parameters, simplifying the management of different minion types while promoting extensibility.

## Game Logic Flow

The main game logic resides within the `Game` class, which coordinates each round and manages actions. The logic includes:

1. **Setup Phase**: Initializes fields such as players, decks, and heroes.
2. **Action Loop**: Iterates over all actions, ensuring each action produces the correct output based on the game state.

## Potential Improvements

- **Player Class Enhancements**: Moving the current player’s hand into the `Player` class for better encapsulation.
- **Control Structures**: Considering to replace multiple `if` statements with `switch` statements to improve readability and elegance.

## Difficulties and Challenges

- **Understanding Main Logic**: The integration of main game logic with input handling was initially challenging. However, once this flow was understood, it clarified the structure and made implementation smoother.
- **Organizing Classes**: As a first OOP project, organizing classes in an efficient and logical structure was challenging but also rewarding as it improved understanding of OOP principles.
