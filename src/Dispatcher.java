import java.io.IOException;
import java.util.Arrays;

public class Dispatcher extends Thread{

    public Dispatcher(){

    }
    public void run() {
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Time "+OS.Time + " READY Queue in "+OS.memo.getReadyQueue());
            System.out.println("---------------------------------------");
            if(OS.memo.getReadyQueue().isEmpty()==false){
                int id = OS.memo.readyQueue.poll();
                String State = OS.memo.getState(id);
                if(State.equals("ReadyD")){
                    OS.memo.swap(id,false,"Program_"+id+".txt");
                }
                System.out.println(Arrays.toString(OS.memo.memory));
                System.out.println("Current running process "+ id);
                System.out.println("---------------------------------------");
                try {
                    OS.execute(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(!OS.memo.getState(id).equals("Finished") && (!OS.memo.getState(id).equals("BlockedM"))) {
                    OS.memo.setState(id,"ReadyM");
                    OS.memo.getReadyQueue().add(id);
                }
                if(OS.memo.getState(id).equals("Finished")){
                    OS.memo.remove(id);
                }
            }
            else{
                OS.flag = false;
                break;
            }
        }
    }
}
