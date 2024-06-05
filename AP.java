import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class AP {
    public static void ap(float currentTime , ArrayList<Process> processes ,Queue<Process> foreGround , 
Queue<Process> backGround , Queue<Process> io ,float quantum , float dispatchLatency ,ArrayList<ProcessTrack> gantChart) {
        LinkedList <ProcessTrack> ioTrack = new LinkedList<>();// keeps track of io executions
        currentTime = processes.get(0).getArrival();// push the current time forward until the first arival
        if (processes.get(0).getType().equals("b")) 
            backGround.add(processes.get(0));   
        if (processes.get(0).getType().equals("f")) 
            foreGround.add(processes.get(0));   
        

        while(!Functions.allCompleted(processes)){
            
             if (!foreGround.isEmpty()) {
                Process p = foreGround.remove();
                int i = Functions.nextBurst(p,"cpu");//index of cpu burt
                ArrayList <Float> remaining = p.getRemaining();
                if (i<0) {
                    i=remaining.size()-1;
                }
                if (remaining.get(i)<=quantum) {//cpu burst can be executed completely
                    gantChart.add(new ProcessTrack(currentTime,currentTime+remaining.get(i),p));
                    currentTime+=remaining.get(i);
                    remaining.set(i,0f);
                    if (remaining.size()==i+1) {//the last cpu burst is done and process is finished
                        p.setCompletionTime(currentTime);
                        Functions.calculateWaitingTime(p);
                        p.setIsComplete(true);
                        p.setInForeQueue(false);
                        p.setInIOQueue(false);    
                    }else{// after this cpu burst there is an io burst
                        p.setIsComplete(false);
                        p.setInForeQueue(false);
                        p.setInIOQueue(true); 
                    }
                }else{//remaining.get(i)>quantum      a part of this cpu burst remaines
                    remaining.set(i,remaining.get(i)-quantum);
                    gantChart.add(new ProcessTrack(currentTime,currentTime+quantum,p));
                    currentTime+=quantum;
                    p.setIsComplete(false);
                    p.setInForeQueue(true);
                    p.setInIOQueue(false); 
                }
                currentTime+=dispatchLatency; 
                Functions.newToReady(currentTime, processes, foreGround,backGround);// add new processes to ready queue 
                if (p.getInForeQueue()) foreGround.add(p);// move a process from executing to ready queue
                if (p.getInIOQueue()) Functions.ioCalculation(currentTime-dispatchLatency, io, ioTrack, p);// send a process to io 
                Functions.ioToReady(currentTime,foreGround,backGround,ioTrack);// move processes from io to ready queue
                
            }
            else if (foreGround.isEmpty()) {// if fore ground is empty go to back ground 
                                
                if(!backGround.isEmpty()){
                    Process p = backGround.remove();
                    int i = Functions.nextBurst(p,"cpu");//index of cpu burt
                    ArrayList <Float> remaining = p.getRemaining();
                    if (i<0) {
                        i=remaining.size()-1;
                    }
                    gantChart.add(new ProcessTrack(currentTime,currentTime+remaining.get(i),p));
                    currentTime+=remaining.get(i);
                    remaining.set(i,0f);
                    if (remaining.size()==i+1) {
                        p.setCompletionTime(currentTime);
                        Functions.calculateWaitingTime(p);
                        p.setIsComplete(true);
                        p.setInBackQueue(false);
                        p.setInIOQueue(false); 
                    }else{
                        p.setIsComplete(false);
                        p.setInBackQueue(false);
                        p.setInIOQueue(true);
                    }
                    currentTime+=dispatchLatency;
                    Functions.newToReady(currentTime, processes, foreGround,backGround);//ceck for new processes
                   
                    // check if fore ground is empty if a fore ground process has arrived go back to it's arrival 
                    if (!foreGround.isEmpty()) {
                        currentTime=foreGround.peek().getArrival();
                        remaining.set(i,gantChart.getLast().getEnd()-currentTime);//executed cpu burst before preemption
                        gantChart.getLast().setEnd(foreGround.peek().getArrival());
                        p.setIsComplete(false);
                        p.setInBackQueue(true);
                        p.setInIOQueue(false);
                        currentTime+=dispatchLatency;
                        if (p.getInBackQueue()) Functions.addFirst(backGround,p);// add the preempted process to the begining of back ground queue

                    }
                    if (p.getInIOQueue()) Functions.ioCalculation(currentTime-dispatchLatency, io, ioTrack, p);// send a process to io 
                    Functions.ioToReady(currentTime,foreGround,backGround, ioTrack);// move a process from io to back ground queue
                }
                else if (backGround.isEmpty()) {
                    currentTime++;
                    Functions.newToReady(currentTime, processes, foreGround, backGround);
                    Functions.ioToReady(currentTime, foreGround, backGround, ioTrack);
                }
            }
        }
    }
}
/* System.out.println("\nend "+currentTime);
                System.out.print("fore :");
                for ( Process f : foreGround) {
                    System.out.print(f.getName());
                }
                System.out.print("\nback :");
                for ( Process f : backGround) {
                    System.out.print(f.getName());
                } */