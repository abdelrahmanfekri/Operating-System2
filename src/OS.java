import java.io.*;
import java.util.*;


public class OS extends Thread{
    static int timeSlice = 2;
    static String path; // path to all files
    static Scanner input; // name of file
    static Memory memo;
    static String programName;
    static int Time =0;
    static boolean flag = true;
    static int ID = 1;
    static Scanner sc = new Scanner(System.in);
    public OS(){

    }
    public static void add() {
        // this files path could be changed
        String firstProgramPath = "Program_1.txt";
        String secondProgramPath = "Program_2.txt";
        String thirdProgramPath = "Program_3.txt";
        if (Time == 0) {
            // read first program
            System.out.println("Time "+Time);
            System.out.println("first Program is added");
            System.out.println("---------------------------------------");
            memo.readProcess(firstProgramPath,ID++,true);
        }
        else if (Time == 1) {
            // read second program
            System.out.println("Time "+Time);
            System.out.println("second Program is added");
            System.out.println("---------------------------------------");
            memo.readProcess(secondProgramPath,ID++,true);
        }
        else if (Time == 4) {
            // read third program
            System.out.println("Time "+Time);
            System.out.println("third Program is added");
            System.out.println("---------------------------------------");
            memo.readProcess(thirdProgramPath,ID++,true);
        }
    }

    public static void printFromTo(int from, int to){
        for(int i = from; i <= to; i++){
            System.out.println(i);
        }
    }

    public static String readFile(String path) throws FileNotFoundException {
        try {
            File file = new File(path+".txt");
            Scanner read = new Scanner(file);
            String s = "";
            while(read.hasNextLine()) {
                s = s+read.nextLine();
            }
            return s;
        }
        catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        return null;
    }

    public static void writeFile(String data, String path) throws IOException {
        try {
            FileOutputStream fos = new FileOutputStream(path+".txt");
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
        }
        catch (IOException e){
            System.out.println("File not found");
        }
    }

    public static void assign(String varName, String value,int id){
        int end = memo.getEnd(id) - 2;
        for(int i=0;i<=2;i++){
            if(memo.memory[end+i]!=null && ((Tuple)memo.memory[end+i]).getVar().equals(varName)){
                memo.memory[end+i] = new Tuple(varName,value);
                return;
            }
        }
        for (int i = 0; i <= 2; i++) {
            if(memo.memory[end+i]==null){
                memo.memory[end+i] = new Tuple(varName,value);
                return;
            }
        }
    }

    public static void print(String varName,int id){
        System.out.println(getValue(varName,id));
    }
    public static String getValue(String varName,int id){
        int end = memo.getEnd(id) - 2;
        for(int i=0;i<=2;i++){
            if(memo.memory[end+i]!=null && ((Tuple)memo.memory[end+i]).getVar().equals(varName)){
                return ((Tuple) memo.memory[end+i]).getValue();
            }
        }
        return null;
    }

    public static void semWait(String input,int id){
        // userInput, userOutput or file
        if(input.equals("file")){
            if(memo.src[2]!=0){
                memo.getBlockedQueueFile().add(id);
                memo.setState(id,"BlockedM");
            }
            else{
                memo.getSrc()[2] = id;
            }
        }
        else if(input.equals("userInput")){
            if(memo.src[0]!=0){
                memo.getBlockedQueueUserInput().add(id);
                memo.setState(id,"BlockedM");
            }
            else{
                memo.getSrc()[0] = id;
            }
        }
        else{
            if(memo.getSrc()[1]!=0){
                memo.getBlockedQueueUserOutput().add(id);
                memo.setState(id,"BlockedM");
            }
            else{
                memo.getSrc()[1] = id;
            }
        }
    }

    public static void semSignal(String input,int id) {
        if (input.equals("file")) {
            if (memo.getSrc()[2] != id) return;
            if (!memo.getBlockedQueueFile().isEmpty()) {
                memo.getSrc()[2] = memo.getBlockedQueueFile().poll();
                String s = memo.getState(memo.getSrc()[2]);
                if(s.equals("BlockedM"))
                    memo.setState(memo.getSrc()[2],"ReadyM");
                else
                    memo.setState(memo.getSrc()[2],"ReadyD");

                memo.getReadyQueue().add(memo.getSrc()[2]);
            } else {
                memo.getSrc()[2] = 0;
            }

        } else if (input.equals("userInput")) {
            if (memo.getSrc()[0] != id) return;
            if (!memo.getBlockedQueueUserInput().isEmpty()) {
                memo.getSrc()[0] = memo.getBlockedQueueUserInput().poll();
                String s = memo.getState(memo.getSrc()[0]);
                if(s.equals("BlockedM"))
                    memo.setState(memo.getSrc()[0],"ReadyM");
                else
                    memo.setState(memo.getSrc()[0],"ReadyD");
                memo.getReadyQueue().add(memo.getSrc()[0]);
            } else {
                memo.getSrc()[0] = 0;
            }
        } else {
            if (memo.getSrc()[1] != id) return;
            if (!memo.getBlockedQueueUserInput().isEmpty()) {
                memo.getSrc()[1] = memo.getBlockedQueueUserInput().poll();
                String s = memo.getState(memo.getSrc()[1]);
                if(s.equals("BlockedM"))
                    memo.setState(memo.getSrc()[1],"ReadyM");
                else
                    memo.setState(memo.getSrc()[1],"ReadyD");
                memo.getReadyQueue().add(memo.getSrc()[1]);
            } else {
                memo.getSrc()[1] = 0;
            }
        }
    }

    public static void execute(int id) throws IOException{
        memo.setState(id,"Running");
        for (int i = 1; i<= OS.timeSlice && memo.getState(id).equals("Running"); i++) {
                int Start = OS.memo.getStart(id);
                if(Start==-1){
                    OS.memo.swap(id,false,"Program_"+id+".txt");
                }
                String curInst = memo.getNext(id);
                if(curInst==null) {
                    memo.setState(id,"Finished");
                    return;
                }
                System.out.println(curInst);
                StringTokenizer st = new StringTokenizer(curInst);
                String instName = st.nextToken();
                System.out.println("---------------------------------------");
                if (instName.equals("print")) {
                    String toPrint = st.nextToken();
                    OS.print(toPrint,id);
                }
                else if (instName.equals("assign")) {
                    String var = st.nextToken();
                    String value = st.nextToken();
                    if(value.equals("input")) {
                        value = sc.next();
                    }else if(value.equals("readFile")) {
                        value = readFile(st.nextToken());
                    }
                    assign(var,value,id);
                }
                else if (instName.equals("writeFile")) {
                    String fileName = st.nextToken();
                    String data = st.nextToken();
                    OS.writeFile(getValue(data,id), fileName);
                }
                else if (instName.equals("printFromTo")) {
                    String int1 = st.nextToken();
                    String int2 = st.nextToken();
                    int n1 = Integer.parseInt(getValue(int1,id));
                    int n2 = Integer.parseInt(getValue(int2,id));
                    OS.printFromTo(n1, n2);
                }
                else if (instName.equals("semWait")) {
                    String resource = st.nextToken();
                    OS.semWait(resource, id);
                }
                else if (instName.equals("semSignal")) {
                    String resource = st.nextToken();
                    OS.semSignal(resource, id);
                }
                OS.Time++;
                OS.add();
                System.out.println("Time"+OS.Time);
                System.out.println("OS ready queue"+memo.getReadyQueue());
                System.out.println("Blocked Queue");
                System.out.println("file "+ memo.getBlockedQueueFile());
                System.out.println("user input "+ memo.getBlockedQueueUserInput());
                System.out.println("user output"+ memo.getBlockedQueueUserOutput());
            }
    }

    public static void init() { // initialize system
        path = "\\\\wsl$\\Ubuntu\\home\\amir\\";
        input = new Scanner(System.in);
        memo = new Memory(40);
        OS os = new OS();
        os.add();
        Dispatcher dis = new Dispatcher();
        dis.start();
        System.out.println("Welcome to the system, please enter program name");
    }

    public static void main(String [] args) throws IOException {
        init();
    }
}
