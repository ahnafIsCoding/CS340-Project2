/* Cashier class
 * Last Modified Time: 12/12/2023 4 PM
 * Author: Ahnaf Ahmed
 * */

public class Cashier implements Runnable {

   // data members
   private int id;
   private String num;
   private Thread thread;
   
   // static variables
   public static int totalCashier = Main.num_cashiers;
   

   // constructor
   public Cashier(int id) {
     this.id = id;
     setName("Cashier-"+id);
     this.thread = new Thread(this, num);
   } // Cashier

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
   
   
   @Override
   public void run() {
      
      msg("started their shift");

      try {
      
         while(true) {
            
            msg("waiting for customer");
            Main.CustomerSem.acquire(); // P(Customer)
            
            // leave if no customer remaining
            Main.Mutex.acquire(); // P(Mutex)
            if (Customer.totalCustomer <= 0) {
               Main.Mutex.release(); // V(Mutex)
               break;
            } // if
            Main.Mutex.release(); // V(Mutex)
            
            msg("helping the next customer");
            Thread.sleep(1000);
            msg("checkout complete");
            
            // cashier became available
            Main.CashierSem.release(); // V(Cashier)
         } // while
         
         msg("no more customer left to help");
         
         Main.Mutex.acquire(); // P(Mutex)
         totalCashier--;
         if(totalCashier <= 0) {
            Main.PetCustomerSem.release();
//            Main.CashierAdoptionClerkSem.acquire();
            Main.CashierAdoptionClerkSem.release(); // V(CashierAdoptionClerkSem)
         } // if
         Main.Mutex.release(); // V(Mutex)

         msg("left the store");

      } catch (Exception e) {
          e.printStackTrace();
      } // catch
   } // run
} // class Cashier
