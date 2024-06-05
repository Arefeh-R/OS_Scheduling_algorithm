public class ProcessTrack{
    private float start;
    private float end;
    private Process p;
    public ProcessTrack (){}
     public ProcessTrack( float start,float end,Process p){
        this.start=start;
        this.end=end;
        this.p=p;
     }
     public void setaStart (float start){
        this.start=start;
     }
     public float getStart(){
        return start;
     }
     public void setEnd (float end){
        this.end=end;
     }
     public float getEnd(){
        return end;
     }
     public void setProcess (Process p){
        this.p=p;
     }
     public Process getProcess(){
        return p;
     }

}