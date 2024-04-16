/* AdoptionClerk class
 * Last Modified Time: 12/12/2023 4 PM
 * Author: Ahnaf Ahmed
 * */

public class AdoptionClerk implements Runnable {
   
   // data members
   private String num;
   private Thread thread;
   
   // constant variables
   public static final int num_visitors = 3;
   public static final int num_pets = 12;
   
   // static variables
   public static boolean petAvailable = true;
   public static int numPetAvailable = num_pets;
   

   // default constructor
   public AdoptionClerk() {
     setName("AdoptionClerk");
     this.thread = new Thread(this, num);
   } // AdoptionClerk

   // setter
   private void setName(String num) {
      this.num = num;
   } // setName
   
   // getter
   public String getName() {
      return num;
   } // getName
   
   public Thread getThread() {
      return thread;
   } // getThread
   
   // print message
   public void msg(String m) {
      System.out.println("["+(System.currentTimeMillis()-Main.time)+"] "+getName()+": "+m);
  } // msg

   public void start() {
     thread.start();
   } // start

   // check if there are any pets available
//   public static boolean petInStore() {
//      return (availablePets.get() > 0);
//   } // petInStore

   @Override
   public void run() {
      
      try {
         this.msg("started their shift");
         
         while(true) {
            
            // break if no cashier remaining
            Main.Mutex.acquire(); // P(Mutex)
            if (Cashier.totalCashier <= 0) {
               Main.Mutex.release(); // V(Mutex)
               break;
            } // if
            Main.Mutex.release(); // V(Mutex)

            // break if no pet remaining
            Main.Mutex.acquire(); // P(Mutex)
            if (numPetAvailable == 0) {
               petAvailable = false;
               this.msg("no more pet available");
               
               this.msg("asking all waiting customer to leave");
               Main.VisitorSem.release(Main.VisitorSem.getQueueLength()); // V(VisitorSem)
               Main.Mutex.release(); // V(Mutex)
               break;
            } // if
            Main.Mutex.release(); // V(Mutex)
            
            this.msg("waiting for customer");
            Main.PetCustomerSem.acquire(); // P(PetCustomerSem)
            
            // break if no customer remaining
            Main.Mutex.acquire(); // P(Mutex)
            if (Customer.totalCustomer <= 0) {
               Main.Mutex.release(); // V(Mutex)
               break;
            } // if
            Main.Mutex.release(); // V(Mutex)
            
//            // waiting for available slots
//            Main.VisitorSem.acquire(); // P(VisitorSem)
            
//            this.msg("Adoption clerk is available");
//            Main.AdoptionClerkSem.release(); // V(AdoptionClerkSem)
//            Main.CashierAdoptionClerkSem.release(); // V(CashierAdoptionClerkSem)

            this.msg("announcing the next customer");
         } // while
         
         // wait for all cashier to leave
         Main.CashierAdoptionClerkSem.acquire(); // P(CashierAdoptionClerkSem)
         this.msg("closing the store");
         Thread.sleep(1000);
         this.msg("left the store");
      } catch (Exception e) {
         e.printStackTrace();
      } // catch
   } // run
} // class AdoptionClerk
