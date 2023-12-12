import java.util.Vector;

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


// Main.Mutex.acquire(); // P(Mutex)
// boolean allCustomerServed = Customer.totalCustomer <= 0;
// Main.Mutex.release(); // V(Mutex)
   
   @Override
   public void run() {
      
      msg("Started shift");

      try {
      
         while(true) {
            

            msg("Waiting for customer");
            Main.CustomerSem.acquire(); // P(Customer)
            Main.Mutex.acquire(); // P(Mutex)
            if (Customer.totalCustomer <= 0) {
               Main.Mutex.release(); // V(Mutex)
               break;
            }
            Main.Mutex.release(); // V(Mutex)
            
            msg("Helping the next customer");
            thread.sleep(1000);
            msg("Checkout complete");
            Main.CashierSem.release(); // V(Cashier)
           
         }
         
         msg("All customers left");
         
         Main.Mutex.acquire(); // P(Mutex)
         totalCashier--;
         System.out.println("Cashier left: "+totalCashier);
         Main.Mutex.release(); // V(Mutex)
         
         msg("Left");

      } catch (Exception e) {
            
      }
   }

}
