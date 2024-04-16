# CS340-Project2
Pet Store Adoption Event with Semaphores

**Objective:** To create a program that handles **"multi-threading"** while ensuring data integrity by establishing mutual exclusion between threads to eliminate race conditions.

**Language:** Java

**Notable Libraries:** java.util.concurrent.Semaphore

**Notable Classes/Interfaces/Objects:** Runnable interface, Thread object, Semaphore object

**Implementation:**
- Created separate classes for each category of threads.
- Used binary semaphores (mutex) to protect critical sections (counters).
- Used a randomized sleep method to simulate real-world events.
- Logged time for each activity to display elapsed time.

**Outcome:** A Java program simulating a "Pet Store Adoption" event using multi-threads of different categories (Customer, Cashier, Adoption Clerk). It ensures mutual exclusion between threads and protects critical sections by implementing binary semaphores. Finally, it prints a detailed activity log with time as the program output.
