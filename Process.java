import java.util.ArrayList;
public class Process {

    private String name; 
    private float arrival;
    private ArrayList <Float> burst=new ArrayList<>();
    private ArrayList <Float> remaining=new ArrayList<>();// what's remaned of bursts
    private String type ;
    private float completionTime;// finish of process
    private float turnaroundTime; 
    private float responseTime;
    private float waitingTime;
    private float ioWaitingTime;// time spend in io queue
    private boolean inForeQueue;
    private boolean inBackQueue;  
    private boolean inIOQueue;
    private boolean isComplete;
    
    public Process(  ){
       
    }
   
    public Process(String n,String type ,float a,ArrayList <Float> b){
        name=n;
        arrival=a;
        this.type=type;
        burst=b;
        remaining=new ArrayList<>(b);
    }
    
    public void setInForeQueue (boolean n){
        this.inForeQueue = n;
    }

    public boolean getInForeQueue (){
        return this.inForeQueue;
    }

    public void setInBackQueue (boolean n){
        this.inBackQueue = n;
    }

    public boolean getInBackQueue (){
        return this.inBackQueue;
    }

    public void setInIOQueue (boolean n){
        this.inIOQueue = n;
    }

    public boolean getInIOQueue (){
        return this.inIOQueue;
    }

    public void setIsComplete (boolean n){
        this.isComplete = n;
    }

    public boolean getIsComplete (){
        return this.isComplete;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

     public float getArrival(){
        return arrival;
    }

    public void setArrival(float arrival){
        this.arrival=arrival;
    }

    public ArrayList<Float> getBurst(){
        return burst;
    }

    public void setBurst(ArrayList<Float> burst){
        this.burst=burst;
    }

    public ArrayList<Float> getRemaining(){
        return remaining;
    }

    public void setRemaining(ArrayList<Float> remaining){
        this.remaining=remaining;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type=type;
    }

    public float getCompletionTime(){
        return completionTime;
    }

    public void setCompletionTime(float completionTime){
        this.completionTime=completionTime;
    }

    public float getWaitingTime(){
        return waitingTime;
    }

    public void setWaitingTime(float waitingTime){
        this.waitingTime=waitingTime;
    }
    
    public float getTurnaroundTime(){
        return turnaroundTime;
    }

    public void setTurnaroundTime(float turnaroundTime){
        this.turnaroundTime=turnaroundTime;
    } 

    public float getResponseTime(){
        return responseTime;
    }

    public void setResponseTime(float responseTime){
        this.responseTime=responseTime;
    }

    public void setIoWaitingTime(float ioWaitingTime){
        this.ioWaitingTime=ioWaitingTime;
    }

    public float getIoWaitingTime(){
        return  ioWaitingTime;
    }
} 
