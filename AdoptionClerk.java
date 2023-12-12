import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class AdoptionClerk implements Runnable {
   
   // data members
   private String num;
   private Thread thread;
   
   // static variables

   
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

   @Override
   public void run() {
      
      try {
         
         while(true) {
            
            Main.Mutex.acquire(); // P(Mutex)
            if (Cashier.totalCashier <= 0) {
               Main.Mutex.release(); // V(Mutex)
               break;
            }
            Main.Mutex.release(); // V(Mutex)
            
            
            
         }
         
         // msg
         
      } catch (Exception e) {
         
      }
   }
   
}
