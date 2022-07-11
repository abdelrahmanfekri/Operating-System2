public class PCB {

    private int id;
    private States state;
    private int pc;
    private int start;
    private int end;
    private Location location;

    public PCB(int id, States state, int pc, int start, int end, Location location) {
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

}
