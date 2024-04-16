/* Main class
 * Last Modified Time: 12/12/2023 4 PM
 * Author: Ahnaf Ahmed
 * */

// imported libraries
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {

   // global variables
   // initial time (when program is first run)
   public static final long time = System.currentTimeMillis();
   
   // number of threads
   public static final int num_customers = 20;
   public static final int num_cashiers = 3;

   // random number generator
   public static Random random = new Random();
   
   // semaphores
   public static Semaphore Mutex = new Semaphore(1,true);
   public static Semaphore CustomerSem = new Semaphore(0,true); // wait: Cashier, signal: Customer
   public static Semaphore CashierSem = new Semaphore(num_cashiers,true); // wait: Customer, signal: Cashier
   public static Semaphore PetCustomerSem = new Semaphore(0,true); // wait: AdoptionClerk, signal: Customer
   public static Semaphore VisitorSem = new Semaphore(AdoptionClerk.num_visitors,true); // wait: Customer, signal: Customer
   public static Semaphore CashierAdoptionClerkSem = new Semaphore(0,true); // wait: AdoptionClerk, signal: Cashier

//   public static Semaphore AdoptionClerkSem = new Semaphore(0,true);
   
   public static void main(String[] args) {

      System.out.println("Welcome to the Happy Pet Store: Pet-Adoption event");

      // creating 1 adoptation_clerk
      AdoptionClerk adoptionClerk = new AdoptionClerk();
      adoptionClerk.start();
      
      // creating 3 cashier threads
      Cashier[] cashiers = new Cashier[num_cashiers];
      for (int i = 0; i < num_cashiers; i++) {
         cashiers[i] = new Cashier(i+1);
         cashiers[i].start();
      } // for

      // creating 20 customer threads
      Customer[] customers = new Customer[num_customers];
      for (int i = 0; i < num_customers; i++) {
         customers[i] = new Customer(i+1);
         customers[i].start();
      } // for
      
//      System.out.println("Pet store closed!");
      
   } // main

} // class Main
