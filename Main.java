import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Main{
    public static void main (String [] args){

        //opening statements
        System.out.println("Network Traffic Monitor Started");
        System.out.println("Getting active network connections...");

        //for counting the all connection
        int n=0;

        //creating object that will read input from the windows
        ProcessBuilder pb=new ProcessBuilder("netstat","-ano");
        try{
            //starting the process initiated by Process Builder
        Process process=pb.start();

        //converting the bytes into characters
        InputStreamReader input=new InputStreamReader(process.getInputStream());

        //reading the characters and amending line by line
        BufferedReader buffer=new BufferedReader(input);

        //for storing the connection whole line
            String line;

            String [] array;

        while (((line=buffer.readLine())!=null)){
            if(line.contains("ESTABLISHED")){
                array=line.trim().split("\\s+");

                //Asking Program to read the Process name
                //Process name getting command tasklist /FI
                String filter="PID eq " + array[4];
                ProcessBuilder pb1=new ProcessBuilder("tasklist","/FI",filter);
                String line1;
                    Process process1=pb1.start();
                    InputStreamReader input1=new InputStreamReader(process1.getInputStream());
                    BufferedReader buffer1=new BufferedReader(input1);
                    while ((line1= buffer1.readLine())!= null)
                    System.out.println(line1);


                System.out.println("Protocol: " + array[0]);
                System.out.println("Local Host: " + array[1]);
                System.out.println("Remote: " + array[2]);
                System.out.println("PID#: " + array[4]);
                System.out.println();
            n++;}
        }
    }catch (IOException e){
            System.out.println("Some Error occur");
        }

        //showing established connections
        System.out.println("Total Established connections : " + n);
    }
}