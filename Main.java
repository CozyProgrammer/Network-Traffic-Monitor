import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Main{

                    // ****   All the Variables    ****

    //for counting the all connection
    static int n=0,num = 0,number=0;
    //for storing the connection whole line
    static String line;
    //for storing the component of the Ip address
    static String [] array;
    //for storing the ProcessNames and their counts and Total Number of Processes
    static String [] ProcessNames=new String[50];
    static int [] countProcess=new int[50];
    static int processTotal = 0;


    public static void main (String [] args){

        //opening statements
        System.out.println("Network Traffic Monitor Started");
        System.out.println("Getting active network connections...");
        mainMethod();

        //UDP
        System.out.println("All Non-Established : " + number);

        //showing all the Established connections
        System.out.println("Total LoopBack connections : " + num);

        //showing Private and Internet connections where remote IP is not Private
        System.out.println("Total Private and Internet connections : " + n);

        //showing counting of the processes
        for(int i=0;i<processTotal;i++){{
            System.out.println(ProcessNames[i] + " : " + countProcess[i]);
            }
        }
    }

    private static void mainMethod(){
        //creating object that will read input from the windows
        ProcessBuilder pb=new ProcessBuilder("netstat","-ano");
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
                    String [] mid=remoteHost[0].split("\\.");
                    int one=Integer.parseInt(mid[0]);
                    int two=Integer.parseInt(mid[1]);
                    boolean isPrivate=remoteHost[0].startsWith("192.168") || remoteHost[0].startsWith("10") ||
                            (one==172 && 16<=two && two<=31);

                    //showing the parts of connections
                    if(!(localHost[0].equals("127.0.0.1"))){
                        if(!isPrivate){
                            System.out.println("Process Name: " + processName);
                            System.out.println("Connection: Internet");
                            System.out.println("Protocol: " + array[0]);
                            System.out.println("Local IP: " + localHost[0]);
                            System.out.println("Local Host Port: " + localHost[1]);
                            System.out.println("Remote IP: " + remoteHost[0]);
                            System.out.println("Remote Port: " + remoteHost[1]);
                            System.out.println("PID#: " + array[4]);
                            System.out.println();
                        }else {
                            System.out.println("Process Name: " + processName);
                            System.out.println("Connection: Private");
                            System.out.println("Protocol: " + array[0]);
                            System.out.println("Local IP: " + localHost[0]);
                            System.out.println("Local Host Port: " + localHost[1]);
                            System.out.println("Remote IP: " + remoteHost[0]);
                            System.out.println("Remote Port: " + remoteHost[1]);
                            System.out.println("PID#: " + array[4]);
                            System.out.println();
                        }
                        n++;
                        boolean isPresent=false;
                        for(int i=0;i<processTotal;i++){
                            if(ProcessNames[i].equals(processName)){
                                countProcess[i]++;
                                isPresent=true;
                                break;
                            }
                        }
                        if(!isPresent){
                            ProcessNames[processTotal] = processName;
                            countProcess[processTotal] = 1;
                            processTotal++;
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
        }catch (IOException e){
            System.out.println("Some Error occur");
        }
    }

}