

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Memory {

    // 0, 1, 2, 3, 4 -> for the PCBs
    // 5 -> for the ready queue
    // 6 -> for the waiting queue (user input)
    // 7 -> for the waiting queue (user output)
    // 8 -> for the waiting queue (file)
    // 9 -> resources array
    // 10 -> for the start of the memory


    public static Object[] memory;
    public static HashMap<Integer,ArrayList<Tuple>> Variable;
    public int mp; // memory pointer (starts from 9)
    public int processPointer; // pointer to the free space to store a process pcb
    public boolean first;
    public boolean second;
    public int swapPointer; // pointer to the process pcb the will be swapped out
    public HashMap<Integer, Integer> processMap; //  start address -> process id
    public int[] src;
    public Queue<Integer>[] srcQueue;
    public Queue<Integer> readyQueue;
    public Memory(int size) {
        memory = new Object[size];
        src = new int[3];
        processMap = new HashMap<>();
        srcQueue = new LinkedList[3];
        readyQueue = new LinkedList<>();
        for(int i=0;i<3;i++){
            srcQueue[i] = new LinkedList<>();
        }
        mp = 16;
        Variable = new HashMap<>();
    }
    public void addToReadyQueue(int e){
        readyQueue.add(e);
    }
    public void addToBlockedQueue(String src,int p){
        if(src.equals("userInput")){
            srcQueue[0].add(p);
            this.src[0] = p;
        }else if(src.equals("userOutput")){
            srcQueue[1].add(p);
            this.src[1] = p;
        }else{
            srcQueue[2].add(p);
            this.src[2] = p;
        }
    }
    public int[] getSrc(){
        return src;
    }
    public Queue<Integer> getReadyQueue(){
        return readyQueue;
    }
    public Queue<Integer> getBlockedQueueUserInput(){
        return srcQueue[0];
    }
    public Queue<Integer> getBlockedQueueUserOutput(){
        return srcQueue[1];
    }
    public Queue<Integer> getBlockedQueueFile(){
        return srcQueue[2];
    }

    public String getInstruction(int pc,Process p){
        return (String)memory[p.getStart()+pc];
    }

    public int getEnd(int id){
        int i = getId(id);
        return Integer.parseInt(((Tuple)memory[i+3]).getValue());
    }
    public int getStart(int id){
        int i = getId(id);
        return Integer.parseInt(((Tuple)memory[i+3]).getVar());
    }
    public void setState(int id,String State){
        int i = getId(id);
        memory[i+1] = new Tuple("State",State);
    }
    public String getState(int id){
        int i = getId(id);
        return ((Tuple)memory[i+1]).getValue();
    }
    public int getPC(int id){
        int i = getId(id);
        return Integer.parseInt(((Tuple)memory[i+2]).getValue());
    }
    public void incrementPC(int id){
        int i = getId(id);
        memory[i+2] = new Tuple("PC",(getPC(id)+1)+"");
    }
    public String getNext(int id){
        if(memory[getPC(id)+getStart(id)]==null) return null;
        if(memory[getPC(id)+getStart(id)] instanceof Tuple) return null;
        String res = (String)memory[getPC(id)+getStart(id)];
        incrementPC(id);
        return res;
    }
    public int getId(int id){
        for (int i = 0; i < 12; i+=4) {
            if(memory[i]==null) continue;
            int id1 = Integer.parseInt(((Tuple)memory[i]).getValue());
            if(id==id1){
                return i;
            }
        }
        return -1;
    }
    public void readProcess(String path,int id,boolean flag){
        if(!first){
            first = true;
            writeToMemory(13,26,id,path);
            if(flag){
                memory[processPointer++] = new Tuple("id",id+"");
                memory[processPointer++] = new Tuple("State","ReadyM");
                memory[processPointer++] = new Tuple("PC","0");
                memory[processPointer++] = new Tuple(13+"",26+"");
                addToReadyQueue(id);
            }
            else{
                setState(id,"ReadyM");
                int i = getId(id);
                memory[i+3] = new Tuple(13+"",26+"");
            }
        }else if(!second){
            second = true;
            writeToMemory(27,39,id,path);
            if(flag){
                memory[processPointer++] = new Tuple("id",id+"");
                memory[processPointer++] = new Tuple("State","ReadyM");
                memory[processPointer++] = new Tuple("PC","0");
                memory[processPointer++] = new Tuple(27+"",39+"");
                addToReadyQueue(id);
            } else{
                setState(id,"ReadyM");
                int i = getId(id);
                memory[i+3] = new Tuple(27+"",39+"");
            }
        }
        else{
            swap(id,flag,path);
        }
    }
    public void remove(int id){
        int i = getId(id);
        processPointer = i;
        int start = getStart(id);
        if(start==13){
            first = false;
        }
        else{
            second = false;
        }
    }
    public void swap(int id,boolean flag,String path){
        while(true) {
            int id1 = Integer.parseInt(((Tuple) (memory[swapPointer])).getValue());
            String s1 = ((Tuple) memory[swapPointer + 1]).getValue();
            if (!s1.equals("Running") && !s1.equals("ReadyD") && !s1.equals("BlockedD")) {
                int end = Integer.parseInt(((Tuple) memory[swapPointer + 3]).getValue());
                int start = Integer.parseInt(((Tuple) memory[swapPointer + 3]).getVar());
                ArrayList<Tuple> V = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    if(memory[end-i]!=null){
                        V.add((Tuple) memory[end-i]);
                    }
                }
                Variable.put(id1,V);
                if(s1.equals("ReadyM")){
                    memory[swapPointer + 1] = new Tuple("State","ReadyD");
                }
                if(s1.equals("BlockedM")){
                    memory[swapPointer + 1] = new Tuple("State","BlockedD");
                }
                writeToMemory(start,end,id,path);
                if(flag){
                    memory[processPointer++] = new Tuple("id",id+"");
                    memory[processPointer++] = new Tuple("State","ReadyM");
                    memory[processPointer++] = new Tuple("PC","0");
                    memory[processPointer++] = new Tuple(start+"",end+"");
                    getReadyQueue().add(id);
                }else{
                    setState(id,"ReadyM");
                    int i = getId(id);
                    memory[i+3] = new Tuple(start+"",end+"");
                }
                break;
            }
            swapPointer+=4;
            swapPointer%=12;
        }
    }
    public void writeToMemory(int Start,int end,int id,String path){
        try {
            FileReader file = new FileReader(path);
            BufferedReader br = new BufferedReader(file);
            while(br.ready()){
                String line = br.readLine();
                memory[Start++] = line;
            }
            while(Start<(end-3)){
                memory[Start++] =null;
            }
            if(Variable.containsKey(id)){
                ArrayList<Tuple> V = Variable.get(id);
                if(V==null)return;
                for(int i=0;i<V.size();i++){
                    memory[end-3+i] = V.get(i);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            FileOutputStream fos = new FileOutputStream("Program_1.txt");
            fos.write("Amir abdelrahman \n".getBytes());
            fos.write("Amir abdelrahman".getBytes());
            fos.flush();
            fos.close();
        }
        catch (IOException e){
            System.out.println("File not found");
        }
    }
}
