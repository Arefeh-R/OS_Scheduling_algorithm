import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Functions {
    

    public static void addFirst(Queue<Process>backGround,Process p){
        Queue<Process> temp=new LinkedList<>(); 
        while (!backGround.isEmpty()){
            temp.add(backGround.remove());
        }
        backGround.add(p);
        while (!temp.isEmpty()){
            backGround.add(temp.remove());
        }
    }
    public static void calculateWaitingTime (Process p){
        float temp=p.getCompletionTime()-p.getArrival();
        p.setTurnaroundTime(temp);
        for (float i:p.getBurst()) {
            temp-=i;
        }
        temp-=p.getIoWaitingTime();
        if (temp<0) {
            p.setWaitingTime(0);
        }else{
            p.setWaitingTime(temp);
        }
    }

    // calculates the time each process enters io and when it should return to ready queue
    public static void ioCalculation( float currentTime , Queue<Process> io, LinkedList <ProcessTrack> ioTrack,Process p){
        int i = nextBurst(p, "io");//calculate which io burst should be executed
        ArrayList <Float> remaining = p.getRemaining();
        float start,end;
        if (!io.isEmpty()) {
            io.add(p);
            start = ioTrack.getLast().getEnd();
            end = start+remaining.get(i);
            ioTrack.add( new ProcessTrack(start,end,p));   
        }else{
            io.add(p);
            start = currentTime;
            end = start+remaining.get(i);
            ioTrack.add( new ProcessTrack(start,end,p));   
        }
        p.setIoWaitingTime(start-currentTime);
        if(i==1) {
            p.setResponseTime(start-p.getArrival());
        }
    }

    // IO to ready:after finishing IO, process returns to ready queue 
    public static void ioToReady(float currentTime,Queue<Process> foreGround , Queue<Process> backGround , LinkedList <ProcessTrack> ioTrack){
        for (int i = 0; i < ioTrack.size(); i++) {
            ProcessTrack pio = ioTrack.get(i);
            if (currentTime>= pio.getEnd() && !pio.getProcess().getInForeQueue() && !pio.getProcess().getInBackQueue() && pio.getProcess().getInIOQueue() && !pio.getProcess().getIsComplete()) {
                if (pio.getProcess().getType().equals("b")) {
                    pio.getProcess().setInBackQueue(true);
                    backGround.add(pio.getProcess());
                }
                if (pio.getProcess().getType().equals("f")) {
                    pio.getProcess().setInForeQueue(true);
                    foreGround.add(pio.getProcess());
                }
            }
        }
    }

    //new to ready: new process enters the ready queue for the first time 
    public static void newToReady(float currentTime , ArrayList<Process> processes ,
    Queue<Process> foreGround ,Queue<Process> backGround ){
        for (Process p:processes) { 
            if ( p.getArrival() <= currentTime && !p.getInBackQueue() &&!p.getInForeQueue() && !p.getInIOQueue() && !p.getIsComplete()) {
                if (p.getType().equals("b")) {
                    p.setInBackQueue(true);
                    backGround.add(p);     
                }
               if (p.getType().equals("f")) {
                    p.setInForeQueue(true);
                    foreGround.add(p);     
                }
            }
        }

    }
    //determines which burst should be executed next
    public static int nextBurst(Process p ,String a) {// returns index of the cpu burst to be executed
        if(a=="cpu"){
            ArrayList<Float> remaining = p.getRemaining();
            for (int i=0;i<remaining.size();i+=2) {
                if (remaining.get(i)!=0) 
                    return i;
            }
        }
        if (a=="io") {
            ArrayList<Float> remaining = p.getRemaining();
            for (int i=1;i<remaining.size();i+=2) {
                if (remaining.get(i)!=0) 
                    return i;
            }
        }
        return-1;
    }

    //check if all the processes are finished
    public static boolean allCompleted(ArrayList<Process> processes){
        for (Process process : processes) {
            if (process.getIsComplete()==false){ 
                return false;
            }
        }
        return true;
    }
}