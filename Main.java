import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.*;

public class Main{

    //for counting the all connection
    static int n=0,num = 0,number=0;
    //for storing the connection whole line
    static String line;
    //for storing the component of the Ip address
    static String [] array;

    static HashMap <String,Integer> connections=new HashMap<>(50);

    public static void main (String [] args){

        //opening statements
        System.out.println("Network Traffic Monitor Started");
        System.out.println("Getting active network connections...");
        int wait=1;
        try {
            while (true){
                connections.clear();
                System.out.println("**********Scan #:" + wait + "************");
                mainMethod();
                Thread.sleep(5000);
                System.out.println();
            wait++;
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        //UDP
        System.out.println("All Non-Established : " + number);

        //showing all the Established connections
        System.out.println("Total LoopBack connections : " + num);

        //showing Private and Internet connections where remote IP is not Private
        System.out.println("Total Private and Internet connections : " + n);
    }

    private static void mainMethod(){

        //creating object that will read input from the windows
        ProcessBuilder pb=new ProcessBuilder("netstat","-ano");

        int processCount;

        try{
            //starting the process initiated by Process Builder
            Process process=pb.start();

            //converting the bytes into characters
            InputStreamReader input=new InputStreamReader(process.getInputStream());

            //reading the characters and amending line by line
            BufferedReader buffer=new BufferedReader(input);

            while (((line=buffer.readLine())!=null)){
                if(line.contains("ESTABLISHED")){
                    array=line.trim().split("\\s+");
                    //name of process
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
                    String [] mid=remoteHost[0].split("\\.");
                    int one=Integer.parseInt(mid[0]);
                    int two=Integer.parseInt(mid[1]);

                   // InetAddress RemoteHostName=InetAddress.getByName(remoteHost[0]);

                    boolean isPrivate=(remoteHost[0].startsWith("192.168") || remoteHost[0].startsWith("10.") ||
                            (one==172 && 16<=two && two<=31));

                    //showing the parts of connections
                    if(!(localHost[0].equals("127.0.0.1"))){
                        if(!isPrivate){
                            System.out.println("Process Name: " + processName +" , ConnectionType: Internet");
                            System.out.println("Protocol: " + array[0]);
                            System.out.println("Local IP: " + localHost[0] + " , Local Host Port: " +localHost[1]);
                          //  System.out.println("Remote Host Name : " + RemoteHostName.getHostName());
                            System.out.println("Remote IP: " + remoteHost[0] + " , Remote Port: " + remoteHost[1]);
                            System.out.println("PID#: " + array[4]);
                            System.out.println();
                        }else {
                            System.out.println("Process Name: " + processName +" , ConnectionType: Private");
                            System.out.println("Protocol: " + array[0]);
                            System.out.println("Local IP: " + localHost[0] +" , Local Host Port: " +localHost[1]);
                            // System.out.println("Remote Host Name : " + RemoteHostName.getHostName());
                            System.out.println("Remote IP: " + remoteHost[0] + " , Remote Port: " + remoteHost[1]);
                            System.out.println("PID#: " + array[4]);
                            System.out.println();
                        }
                        n++;
                        if(!(processName.isEmpty())){
                           if(connections.containsKey(processName)){
                               processCount= connections.get(processName);
                               processCount++;
                               connections.put(processName,processCount);
                           }
                           else {
                          connections.put(processName,1);
                           }
                        }
                    }
                    else{
                        num++;
                    }
                }
                else {
                    number++;
                }
            }

            ArrayList<Map.Entry<String,Integer>> forSort=new ArrayList<>(connections.entrySet());
            Collections.sort(forSort,(e1,e2) ->
                    e2.getValue().compareTo(e1.getValue()));
            for(Map.Entry<String,Integer> entry:forSort){
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }

        }catch (IOException e){
            System.out.println("Some Error Occur");
        }
    }

}