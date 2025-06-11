

**Snake Game (Java Swing)**

A modern version of the classic Snake game built using Java Swing. This version includes real-time scoring, dynamically spawning food items, and interactive dialogs.


**Features:**

* Smooth snake movement using arrow keys.
* Red food dots appear randomly **over time**, not only when eaten.
* The number of food items is randomly chosen between 3 and 7 at any time.
* A live **score counter** displays during gameplay.
* Final score is shown in a **Game Over dialog** with options to restart or exit.
* Clean design with customizable settings.

---

**How It Works:**

* **Controls:** Use the arrow keys (← ↑ ↓ →) to move the snake.
* **Food Spawning:**

  * At game start, 3–7 food items appear randomly on the grid.
  * New food continues to spawn every 3 seconds using a timer, up to a maximum limit (e.g., 20 food dots).
* **Scoring:**

  * Each food eaten increases the score by 1.
  * Score is shown on the top-left during the game and in the Game Over popup.
* **Game Over:**

  * The game ends if the snake runs into the wall or itself.
  * A dialog appears showing the final score and offering to try again or exit.

---

**Requirements:**

* Java JDK 8 or later.
* A Java IDE (like IntelliJ IDEA or Eclipse) or just a terminal.

---

**How to Run the Game:**

1. Save the code to a file named `SnakeGame.java`.
2. Open terminal/command prompt and go to that directory.
3. Compile the code:

   ```
   javac SnakeGame.java
   ```
4. Run the program:

   ```
   java SnakeGame
   ```

Or open it in your IDE and run the `main()` method directly.

---

**Optional Improvements You Can Try:**

* Add sound effects when the snake eats food.
* Make food expire after a few seconds.
* Add obstacles or special power-ups.
* Replace the square shapes with custom images.

---

**Summary:**

This project is a great way to practice Java Swing and game logic:

* Uses `JPanel` and custom drawing (`paintComponent()`).
* Implements real-time input with `KeyAdapter`.
* Manages timers for both game ticks and food spawning.
* Displays interactive messages using `JOptionPane`.

