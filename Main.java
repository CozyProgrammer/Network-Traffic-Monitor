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
            int num=1;

        while (((line=buffer.readLine())!=null)){
            if(line.contains("ESTABLISHED")){
                array=line.trim().split("\\s+");
                System.out.println("Connection#: " + num);
                System.out.println("Protocol: " + array[0]);
                System.out.println("Local Host: " + array[1]);
                System.out.println("Remote: " + array[2]);
                System.out.println("PID#: " + array[4]);
                System.out.println();
                num++;
            n++;}
        }
    }catch (IOException e){
            System.out.println("Some Error occur");
        }

        //showing established connections
        System.out.println("Total Established connections : " + n);
    }
}