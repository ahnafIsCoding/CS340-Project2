/* Customer class
 * Last Modified Time: 12/12/2023 4 PM
 * Author: Ahnaf Ahmed
 * */

public class Customer implements Runnable {

   // data members
   private int id;
   private String name;
   private Thread thread;
   
   // static variables
   public static int totalCustomer = Main.num_customers;
   

   // constructor
   public Customer(int id) {
     this.setId(id);
     this.setName("Customer-"+id);
     this.thread = new Thread(this, name);
   } // Customer

   // setter
   public void setId(int id) {
      this.id = id;
   } // setId
   
   private void setName(String n) {
      this.name = n;
   } // setName

   // getter
   public int getId() {
      return id;
   } // getId
   
   public String getName() {
      return name;
   } // getName

   public Thread getThread() {
      return thread;
   } // getThread

   // print message
   public void msg(String m) {
      System.out.println("["+(System.currentTimeMillis()-Main.time)+"] "+getName()+": "+m);
  } // msg

   // start the customer thread
   public void start() {
     thread.start();
   } // start
   
   @Override
   public void run() {
      
      try {
         // customers commute to the pet store
         simulateSleep();
         this.msg("commuting to the pet store");
         // Customers commute to the pet store in different ways (simulated by sleep of random time)
         simulateSleep();
         this.msg("arrived at the pet store");
         // Once arrived at the store, the customer will generate a random number between 1 and 10 inclusively
         int randomNum = Main.random.nextInt(10) + 1;
         // If the number is <4, the customer will only buy food and toys for his/her pets. 
         if (randomNum < 4) {
            // only do shopping and leave
            this.msg("wants to buy food and/or toys only");
            shop();
         } else {
            // Otherwise, if the number is even, the customer will be interested in adopting a pet only 
            if (randomNum % 2 == 0) {
               // go straight to pet area
               this.msg("interested in adopting a pet only");
               checkPets();
            } else { // else the customer will first do some shopping and next he/she will also check the pets that are for adoption, maybe they will adopt one.
               // first do some shopping
               this.msg("wants to buy food and/or toys first");
               shop();
               // then go to pet area
               this.msg("also interested in checking the pets");
               checkPets();
            } // else
         } // else

         Main.Mutex.acquire(); // P(Mutex)
         totalCustomer--;
         if (totalCustomer <= 0) {
            Main.PetCustomerSem.release(); // V(PetCustomerSem)
            Main.CustomerSem.release(Main.num_cashiers); // V(CustomerSem)
         } // if
         Main.Mutex.release(); // V(Mutex)

         this.msg("left the store");
         
      } catch (InterruptedException e) {
         e.printStackTrace();
      } // catch
   } // run
   

   // makes customer sleep for a random time
   public void simulateSleep() {
      try {
          Thread.sleep(Main.random.nextInt(5000));
       } catch (InterruptedException e) {
          e.printStackTrace();
       } // catch
   } // simulateSleep

   public void shop() {
      
      try {
         // rush shopping -> increase priority
         int initial_priority = thread.getPriority();
         thread.setPriority(Thread.MAX_PRIORITY);
         simulateSleep();
         // reset priority
         thread.setPriority(initial_priority);
         // browse aisles
         this.msg("browsing aisles");
         simulateSleep();
         
         msg("waiting for cashier");
         Main.CashierSem.acquire(); // P(Cashier)
         
         msg("going to the cahsier");
         Main.CustomerSem.release(); // V(Customer)
         simulateSleep();
      } catch (InterruptedException e) {
         e.printStackTrace();
      } // catch
   } // shop
   
   public void checkPets() {
      
      try {
         // only check if there are any pet left in the store
         Main.Mutex.acquire(); // P(Mutex)
         if (!AdoptionClerk.petAvailable) {
            Main.Mutex.release(); // V(Mutex)
            this.msg("can't check because all pets are adopted");
            return;
         } // if
         Main.Mutex.release(); // V(Mutex)
         
         // waiting in line
         this.msg("Waiting for the visiting area");
         
//         Main.AdoptionClerkSem.acquire(); // P(AdoptionClerkSem)
         
         Main.VisitorSem.acquire(); // P(VisitorSem)

         Main.PetCustomerSem.release(); // V(PetCustomerSem)

         Main.Mutex.acquire(); // P(Mutex)
         if (!AdoptionClerk.petAvailable) {
            Main.Mutex.release(); // V(Mutex)
            Main.VisitorSem.release(); // V(VisitorSem)
            this.msg("can't check because all pets are adopted");
            return;
         } // if
         Main.Mutex.release(); // V(Mutex)
         
         // check all pets
         this.msg("checking all the pets in the room");
         simulateSleep();
         
         Main.Mutex.acquire(); // P(Mutex)
         if (AdoptionClerk.petAvailable && AdoptionClerk.numPetAvailable > 0 && (Main.random.nextInt(10)+1) < 6) {
            AdoptionClerk.numPetAvailable--;
            // adopting
            this.msg("adopting a pet");
            if (AdoptionClerk.numPetAvailable == 0) {
               AdoptionClerk.petAvailable = false;
            } // if
            Main.Mutex.release(); // V(Mutex)
            Main.VisitorSem.release(); // V(VisitorSem)
            
            // customer completes a few forms
            this.msg("completing a few forms");
            simulateSleep();
            // customer takes a break at the coffee center
            this.msg("taking a break at the coffee center");
            Thread.yield();
            Thread.yield();
         } else {
            Main.Mutex.release(); // V(Mutex)
            Main.VisitorSem.release(); // V(VisitorSem)
            // not adopting
            this.msg("not adopting any pets today");
         } // else
      } catch (InterruptedException e) {
         e.printStackTrace();
      } // catch
   } // checkPets
} // class Customer
