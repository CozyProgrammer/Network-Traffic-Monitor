public class Main{
    public static void main (String [] args){

        //opening statements
        System.out.println("Network Traffic Monitor Started");
        System.out.println("Getting active network connections...");

        //creating object that will read input from the windows
        ProcessBuilder pb=new ProcessBuilder("netstat","-ano");
        try{
            //starting the process initiated by Process Builder
        Process process=pb.start();
    }catch (Exception e){
            System.out.println("Some Error occur");
        }
    }
}