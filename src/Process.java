import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Process {

    private int id;
    private States state;
    private int pc;
    private int start;
    private int end;
    private Location location;
    static Scanner sc = new Scanner(System.in);

    public Process(int id, States state, int pc, int start, int end, Location location) {
        this.id = id;
        this.state = state;
        this.pc = pc;
        this.start = start;
        this.end = end;
        this.location = location;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    public int getPC() {
        return pc;
    }

    public void setPC(int pc) {
        this.pc = pc;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getSize(){
        return end - start + 1;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    // toString method
    @Override
    public String toString() {
        return "PCB{" + "id=" + id + ", state=" + state + ", pc=" + pc + ", start=" + start + ", end=" + end + '}';
    }

    /*public void execute() throws IOException{
        pcb.setState(States.RUNNING);
        for (int i = 1; i<= OS.timeSlice && state==States.RUNNING; i++) {
            if (pc<this.inst.size()) {
                String curInst = this.inst.get(pc);
                StringTokenizer st = new StringTokenizer(curInst);
                String instName = st.nextToken();
                System.out.println(curInst+" Of "+this);
                System.out.println("---------------------------------------");
                if (instName.equals("print")) {
                    String toPrint = st.nextToken();
                    OS.print(toPrint,id);
                }
                else if (instName.equals("assign")) {
                    String var = st.nextToken();
                    String value = OS.memory.get("inputTmp"+(pc),id).getStringData();
                    OS.memory.add(var,new Type(value),id);
                    System.out.println("new value for "+var+" "+ value);
                }
                else if(instName.equals("input")){
                    System.out.println("Please enter input value");
                    String inputTmp = sc.next();
                    OS.memory.add("inputTmp"+(pc+1), new Type(inputTmp), id);
                }
                else if (instName.equals("writeFile")) {
                    String fileName = st.nextToken();
                    String data = st.nextToken();
                    OS.writeFile(OS.memory.get(data,id).getStringData(), fileName);
                }
                else if (instName.equals("readFile")) {
                    String fileName = st.nextToken();
                    String value = OS.readFile(fileName);
                    OS.memory.add("inputTmp"+(pc+1), new Type(value), id);
                }
                else if (instName.equals("printFromTo")) {
                    String int1 = st.nextToken();
                    String int2 = st.nextToken();
                    int n1 = OS.memory.get(int1,this.id).getIntData();
                    int n2 = OS.memory.get(int2,this.id).getIntData();
                    OS.printFromTo(n1, n2);
                }
                else if (instName.equals("semWait")) {
                    String resource = st.nextToken();
                    OS.semWait(resource, this);
                }
                else if (instName.equals("semSignal")) {
                    String resource = st.nextToken();
                    OS.semSignal(resource, this);
                }
                pc++;
                if(pc==inst.size()){
                    this.state = States.FINISHED;
                }
                OS.Time++;
                OS.add();
                System.out.println("Time"+OS.Time);
                System.out.println("OS ready queue"+OS.readyQueue);
                System.out.println("Blocked Queue");
                System.out.println("file "+ OS.srcQueue[0]);
                System.out.println("user input "+ OS.srcQueue[1]);
                System.out.println("user output"+ OS.srcQueue[2]);

            }
        }
    }*/

}
