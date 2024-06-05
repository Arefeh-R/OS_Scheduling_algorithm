import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class App {
   
     
    public static void main(String[] args) throws Exception {
    
        float quantum = 0;
        float dispatchLatency = 0;
        float currentTime=0;
        String priority=null;
        int foreGroundTimeSlice=0;
        ArrayList <Process> processes=new ArrayList<>();
        ArrayList <ProcessTrack> gantChart=new ArrayList<>();
        Queue <Process> foreGround = new LinkedList<Process>();
        Queue <Process> backGround = new LinkedList<Process>();
        Queue<Process> io = new LinkedList<Process>();

        try {
            File myObj = new File("C:\\Users\\ASUS\\Documents\\java project\\RR\\src\\in.txt");
            Scanner myReader = new Scanner(myObj);
        
            dispatchLatency=myReader.nextFloat();
            myReader.nextLine();
            priority=myReader.nextLine();
            foreGroundTimeSlice=myReader.nextInt();
            myReader.nextLine();
            quantum=myReader.nextFloat();
            myReader.nextLine();
            while (myReader.hasNextLine()) {
                String str =myReader.nextLine();
                String[] a = str.split(",",0);
                ArrayList<Float> bursts=new ArrayList<>();
               for (int i = 3; i < a.length-1; i++) {
                    bursts.add(Float.valueOf(a[i]));
                }
                Process p = new Process(a[0], a[1], Float.valueOf(a[2]),bursts);
                processes.add(p);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        sort(processes);
        if (priority.equals("AP")) {
            AP.ap(currentTime, processes, foreGround, backGround, io, quantum, dispatchLatency, gantChart);
        }else if (priority.equals("AN")) {
            AN.an(currentTime,processes,foreGround,backGround,io,quantum,dispatchLatency,gantChart);
        }else if (priority.equals("TS")) {
            
        }

       try {
            FileWriter myWriter = new FileWriter("C:\\Users\\ASUS\\Documents\\java project\\RR\\src\\out.txt");
            myWriter.write("Multilevel Queue:"+priority+"\n"+"");
            myWriter.write(print(gantChart));
            myWriter.write("\nAverage Waiting Time: "+avgWaiting(processes));
            myWriter.write("\nAverage Turnaround Time: "+avgTurnaround(processes));
            myWriter.write("\nAverage Response Time: "+avgResponse(processes));
            myWriter.write("\nCPU Utilization: "+utilization(processes,gantChart)+"%");

            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


       
    }
    
    public static float avgWaiting(ArrayList <Process>processes){
        float sum=0;
        for (Process p : processes) {
            sum+=p.getWaitingTime();
        }
        
        return sum/processes.size();
    }

     public static float avgResponse(ArrayList <Process>processes){
        float sum=0;
        for (Process p : processes) {
            sum+=p.getResponseTime();
        }
        
        return sum/processes.size();
    }

    public static float avgTurnaround(ArrayList <Process>processes){
        float sum=0;
        for (Process p : processes) {
            sum+=p.getTurnaroundTime();
        }
        
        return sum/processes.size();
    }
    
    public static float utilization(ArrayList <Process>processes, ArrayList<ProcessTrack> gantchart){
        float sum=0;
            for (Process p : processes) {
                for (int i=0;i<p.getBurst().size();i+=2) {
                sum+=p.getBurst().get(i);
            }
        }
        float cpuUtilization=sum/gantchart.getLast().getEnd();
        return cpuUtilization*100;
    }

    public static String print(ArrayList<ProcessTrack> gantChart){
        String str ;
        str = String.format("Fore ground:\t");
        for (ProcessTrack p:gantChart) {
            if(p.getProcess().getType().equals("f")){
                str+=String.format("  %1.1f|  %s  |%1.1f  ---",p.getStart(),p.getProcess().getName(),p.getEnd());                    
            }
        } 
        str+=String.format("\nback ground:\t");
        for (ProcessTrack p:gantChart) {
            if(p.getProcess().getType().equals("b")){
                str+=String.format("  %1.1f|  %s  |%1.1f  ---",p.getStart(),p.getProcess().getName(),p.getEnd());                    
            }
        } 
        return str;
    }
    public static void sort(ArrayList <Process> p){
        int i, j , n=p.size();
        Process temp;
        boolean swapped;
        for (i = 0; i < n - 1; i++) {
            swapped = false;
            for (j = 0; j < n - i - 1; j++) {
                if (p.get(j).getArrival() > p.get(j+1).getArrival()) {
                    temp = p.get(j);
                    p.set(j,p.get(j+1));
                    p.set(j+1,temp);
                    swapped = true;
                }
            }
            if (swapped == false)
                break;
        }
    }
}
           