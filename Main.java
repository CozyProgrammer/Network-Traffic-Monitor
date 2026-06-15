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


                //Process_Name getting command Task_list
                String filter="PID eq " + array[4];

                //creating Object that will take lines from the data
                ProcessBuilder pb1=new ProcessBuilder("tasklist","/FI",filter);
                String line1;

                    //Process start
                    Process process1=pb1.start();

                    //Converting the bitsBytes into characters by program
                    InputStreamReader input1=new InputStreamReader(process1.getInputStream());

                    //Read line by line but the program itself
                    BufferedReader buffer1=new BufferedReader(input1);
                    String processName="";

                    //for Taking the Program name
                    while ((line1= buffer1.readLine())!= null){
                        if(!(line1.isEmpty())){
                        String [] array1=line1.trim().split("\\s+");
                        if(array1[0].contains(".exe")){
                            processName=array1[0];
                        }}
                    }

                    //dividing the localhost into Ip and Port Parts
                String [] localHost=array[1].split(":");
                    //dividing the Remote into Ip and Port Parts
                String [] remoteHost=array[2].split(":");

                //showing the parts of connections
                System.out.println("Process Name: " + processName);
                System.out.println("Protocol: " + array[0]);
                System.out.println("Local IP: " + localHost[0]);
                System.out.println("Local Host Port: " + localHost[1]);
                System.out.println("Remote IP: " + remoteHost[0]);
                System.out.println("Remote Port: " + remoteHost[1]);
                System.out.println("PID#: " + array[4]);

                System.out.println();
            n++;}
        }
    }catch (IOException e){
            System.out.println("Some Error occur");
        }

        //showing established connections
        System.out.println("==================================");
        System.out.println("Total Established connections : " + n);
        System.out.println("==================================");
    }
}