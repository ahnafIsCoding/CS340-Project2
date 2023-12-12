
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
         simulateSleep();
         msg("Commuting");
         simulateSleep();
         msg("Arrived");
         
         simulateSleep();
         msg("Shopping");
         simulateSleep();
         msg("Waiting for cashier");
         Main.CashierSem.acquire(); // P(Cashier)
         msg("Going to the cahsier");
         Main.CustomerSem.release(); // V(Customer)
   
         Main.Mutex.acquire(); // P(Mutex)
         totalCustomer--;
         if (totalCustomer <= 0) {
            Main.CustomerSem.release(Main.num_cashiers);
         }
         System.out.println("Customer left: "+totalCustomer);
         Main.Mutex.release(); // V(Mutex)
      
      } catch (Exception e) {
         e.printStackTrace();
      }
      
      msg("Left");
   }
   

   // makes customer sleep for a random time
   public void simulateSleep() {
      try {
          Thread.sleep(Main.random.nextInt(5000));
       } catch (InterruptedException e) {
          e.printStackTrace();
       } // catch
   } // simulateSleep

}
