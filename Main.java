import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.sql.Time;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main{
    //for storing the connection whole line
    static String line;
    //for storing the component of the Ip address
    static String [] array;

    public static void main (String [] args){

        //opening statements
        System.out.println("Network Traffic Monitor Started");
        System.out.println("Getting active network connections...");
        choices();
    }
    private static void choices(){
        Scanner sc=new Scanner(System.in);
        boolean isRoot=true;
        while (isRoot){
            System.out.println("Choose Option");
            System.out.println("1).Show All the Private/Public Connections");
            System.out.println("2).Show Top n Connection");
            System.out.println("3).Show Specific Connection");
            System.out.println("4).Exit");
            String in=sc.nextLine();
            switch (in){
                case "1":{
                    showAllProcesses();
                    System.out.println("If Want to quit the process Press (q)");
                    String q=sc.nextLine();
                    if(q.equals("q")){
                        stopProcess();
                        System.out.println("Scanning Closed");
                    }
                    break;
                }
                case "2":{
                    showTopProcesses();
                    String q=sc.nextLine();
                    if(q.equals("q")){
                        stopProcess();
                        System.out.println("Scanning Closed");
                    }
                    break;
                }
                case "3":{
                    specificProcess(sc);
                    break;
                }
                case "4":{
                    System.out.println("Program Terminated");
                    isRoot=false;
                    break;
                }
                default:{
                }
            }
        }
        sc.close();
    }

    static int count;
    private static ScheduledExecutorService service;
    private static boolean shouldExist;


    private static void showAllProcesses(){
        stopProcess();
        count=1;
        HashMap<String, Integer> connections = new HashMap<>();

        HashSet <String> oldConnections=new HashSet<>(200);
        HashSet <String> newConnections=new HashSet<>(50);
        HashSet<String> currentConnections = new HashSet<>();

        shouldExist=false;
        service= Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(() -> {
            if(!shouldExist){
                try (FileWriter fileWriter=new FileWriter("D:\\Connections\\connections.txt");){
                fileWriter.write("********* SCAN" + count + "  **********\n");
                System.out.println("********* SCAN" + count + "  **********");
                System.out.println();
                count++;
                //creating object that will read input from the windows
                ProcessBuilder pb = new ProcessBuilder("netstat", "-ano");
                int processCount;
                String[] names = new String[50];
                try {
                    //starting the process initiated by Process Builder
                    Process process = pb.start();

                    //converting the bytes into characters
                    InputStreamReader input = new InputStreamReader(process.getInputStream());

                    oldConnections.addAll(currentConnections);
                    currentConnections.clear();

                    //reading the characters and amending line by line
                    BufferedReader buffer = new BufferedReader(input);
                    while (((line = buffer.readLine()) != null)) {
                        if (line.contains("ESTABLISHED")) {
                            array = line.trim().split("\\s+");
                            //name of process
                            String filter = "PID eq " + array[4];

                            //creating Object that will take lines from the data
                            ProcessBuilder pb1 = new ProcessBuilder("tasklist", "/FI", filter);
                            String line1;

                            //Process start
                            Process process1 = pb1.start();

                            //Converting the bitsBytes into characters by program
                            InputStreamReader input1 = new InputStreamReader(process1.getInputStream());

                            //Read line by line but the program itself
                            BufferedReader buffer1 = new BufferedReader(input1);
                            String processName = "";

                            //for Taking the Program name
                            while ((line1 = buffer1.readLine()) != null) {
                                if (!(line1.isEmpty())) {
                                    String[] array1 = line1.trim().split("\\s+");
                                    if (array1[0].contains(".exe")) {
                                        processName = array1[0];
                                    }
                                }
                            }
                            //dividing the localhost into Ip and Port Parts
                            String[] localHost = array[1].split(":");
                            //dividing the Remote into Ip and Port Parts
                            String[] remoteHost = array[2].split(":");
                            if (remoteHost[0].contains(":")) continue;
                            String[] mid = remoteHost[0].split("\\.");
                            int one = Integer.parseInt(mid[0]);
                            int two = Integer.parseInt(mid[1]);
                            // InetAddress RemoteHostName=InetAddress.getByName(remoteHost[0]);
                            boolean isPrivate = (remoteHost[0].startsWith("192.168") || remoteHost[0].startsWith("10.") ||
                                    (one == 172 && 16 <= two && two <= 31));

                            //showing the parts of connections
                            String InternetDetails="Process Name: " + processName + " , ConnectionType: Internet\n"
                                    +"Protocol: " + array[0]+"\nLocal IP: " + localHost[0] + " , Local Host Port: " + localHost[1]+
                                    "\nRemote IP: " + remoteHost[0] + " , Remote Port: " + remoteHost[1]+
                                    "\nPID#: " + array[4];

                            String privateDetails="Process Name: " + processName + " , ConnectionType: Private\n"
                                    +"Protocol: " + array[0]+"\nLocal IP: " + localHost[0] + " , Local Host Port: " + localHost[1]+
                                    "\nRemote IP: " + remoteHost[0] + " , Remote Port: " + remoteHost[1]+
                                    "\nPID#: " + array[4];

                            if (!(localHost[0].equals("127.0.0.1"))) {
                                if (!isPrivate && !(oldConnections.contains(InternetDetails))) {
                                    newConnections.add(InternetDetails);
                                } else if (isPrivate && !(oldConnections.contains(privateDetails))){
                                    newConnections.add(privateDetails);

                                }
                            }
                            if (!(processName.isEmpty())) {
                                if (connections.containsKey(processName)) {
                                    processCount = connections.get(processName);
                                    processCount++;
                                    connections.put(processName, processCount);
                                } else {
                                    connections.put(processName, 1);
                                }
                            }
                        }
                    }

                    if(!(oldConnections.isEmpty())){
                        System.out.println("<<<<<< Old Connections >>>>>");
                        for(String details:oldConnections){
                            System.out.println(details);
                            System.out.println();
                        }
                    }else {
                        System.out.println("<<<<<< No Old Connection >>>>>");
                        System.out.println();
                    }
                    if(!(newConnections.isEmpty())){
                            fileWriter.write("<<<<<< New Connections >>>>>\n");
                            System.out.println("<<<<<< New Connections >>>>>");
                            for(String details:newConnections){
                                fileWriter.write(details+"\n\n");
                                System.out.println(details);
                                currentConnections.add(details);
                                System.out.println();

                            }
                        fileWriter.write("\n");
                    }else {
                        System.out.println("<<<<<< No New Connection >>>>>");
                        System.out.println();
                    }
                    oldConnections.clear();
                    newConnections.clear();
                    ArrayList<Map.Entry<String, Integer>> forSort = new ArrayList<>(connections.entrySet());
                    Collections.sort(forSort, (e1, e2) ->
                            e2.getValue().compareTo(e1.getValue()));
                    fileWriter.write("******************************\n");
                    System.out.println("******************************");
                    for (Map.Entry<String, Integer> entry : forSort) {
                        fileWriter.write(entry.getKey() + " : " + entry.getValue()+"\n");
                        System.out.println(entry.getKey() + " : " + entry.getValue());
                        System.out.println("******************************");
                        fileWriter.write("******************************\n");
                        fileWriter.write("\n");

                    }
                    fileWriter.write("\n");
                    fileWriter.close();
                    System.out.println("If Want to quit the process Press (q)");
                    connections.clear();
                }catch (IOException e) {
                    System.out.println("Some Error Occur");
                }
            }catch (Exception e) {
                System.out.println("Some Error Occur");
            }
        }},0,6, TimeUnit.SECONDS);
    }

    private static void showTopProcesses(){
        stopProcess();
        count=1;
        HashMap<String, Integer> connections = new HashMap<>();
        shouldExist=false;
        service= Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(()->{
            if(!shouldExist){
                System.out.println("********* SCAN" + count + "  **********");
                count++;
                //creating object that will read input from the windows
                ProcessBuilder pb = new ProcessBuilder("netstat", "-ano");
                int processCount;
                String[] names = new String[50];
                try {
                    //starting the process initiated by Process Builder
                    Process process = pb.start();

                    //converting the bytes into characters
                    InputStreamReader input = new InputStreamReader(process.getInputStream());

                    //reading the characters and amending line by line
                    BufferedReader buffer = new BufferedReader(input);

                    while (((line = buffer.readLine()) != null)) {
                        if (line.contains("ESTABLISHED")) {
                            array = line.trim().split("\\s+");
                            String filter = "PID eq " + array[4];
                            ProcessBuilder pb1 = new ProcessBuilder("tasklist", "/FI", filter);
                            String line1;
                            Process process1 = pb1.start();
                            InputStreamReader input1 = new InputStreamReader(process1.getInputStream());
                            BufferedReader buffer1 = new BufferedReader(input1);
                            String processName = "";
                            while ((line1 = buffer1.readLine()) != null) {
                                if (!(line1.isEmpty())) {
                                    String[] array1 = line1.trim().split("\\s+");
                                    if (array1[0].contains(".exe")) {
                                        processName = array1[0];
                                    }
                                }
                            }
                            String[] localHost = array[1].split(":");
                            String[] remoteHost = array[2].split(":");
                            if (remoteHost[0].contains(":")) continue;
                            String[] mid = remoteHost[0].split("\\.");
                            int one = Integer.parseInt(mid[0]);
                            int two = Integer.parseInt(mid[1]);
                            boolean isPrivate = (remoteHost[0].startsWith("192.168") || remoteHost[0].startsWith("10.") ||
                                    (one == 172 && 16 <= two && two <= 31));
                            if (!(processName.isEmpty())) {
                                if (connections.containsKey(processName)) {
                                    processCount = connections.get(processName);
                                    processCount++;
                                    connections.put(processName, processCount);
                                } else {
                                    connections.put(processName, 1);
                                }
                            }
                        }
                    }
                    ArrayList<Map.Entry<String, Integer>> forSort = new ArrayList<>(connections.entrySet());
                    Collections.sort(forSort, (e1, e2) ->
                            e2.getValue().compareTo(e1.getValue()));
                    int n = 1;
                    System.out.println("Top 5 Connections....");
                    System.out.println("******************************");
                    for (Map.Entry<String, Integer> entry : forSort) {
                        if (n <=5) {
                            System.out.println(entry.getKey() + " : " + entry.getValue());
                            n++;
                        }
                        else
                            break;
                    }
                    System.out.println("******************************");
                    System.out.println("If Want to quit the process Press (q)");
                    connections.clear();
                }catch (IOException e) {
                    System.out.println("Some Error Occur");
                }
            }
        },0,6,TimeUnit.SECONDS);
    }

    private static void specificProcess(Scanner sc){

        HashMap<String, Integer> connections = new HashMap<>();
        ProcessBuilder pb = new ProcessBuilder("netstat", "-ano");
        int processCount;
        String[] names = new String[50];
        try {
            //starting the process initiated by Process Builder
            Process process = pb.start();

            //converting the bytes into characters
            InputStreamReader input = new InputStreamReader(process.getInputStream());

            //reading the characters and amending line by line
            BufferedReader buffer = new BufferedReader(input);

            while (((line = buffer.readLine()) != null)) {
                if (line.contains("ESTABLISHED")) {
                    array = line.trim().split("\\s+");
                    String filter = "PID eq " + array[4];
                    ProcessBuilder pb1 = new ProcessBuilder("tasklist", "/FI", filter);
                    String line1;
                    Process process1 = pb1.start();
                    InputStreamReader input1 = new InputStreamReader(process1.getInputStream());
                    BufferedReader buffer1 = new BufferedReader(input1);
                    String processName = "";
                    while ((line1 = buffer1.readLine()) != null) {
                        if (!(line1.isEmpty())) {
                            String[] array1 = line1.trim().split("\\s+");
                            if (array1[0].contains(".exe")) {
                                processName = array1[0];
                            }
                        }
                    }
                    String[] localHost = array[1].split(":");
                    String[] remoteHost = array[2].split(":");
                    if (remoteHost[0].contains(":")) continue;
                    String[] mid = remoteHost[0].split("\\.");
                    int one = Integer.parseInt(mid[0]);
                    int two = Integer.parseInt(mid[1]);
                    boolean isPrivate = (remoteHost[0].startsWith("192.168") || remoteHost[0].startsWith("10.") ||
                            (one == 172 && 16 <= two && two <= 31));
                    if (!(processName.isEmpty())) {
                        if (connections.containsKey(processName)) {
                            processCount = connections.get(processName);
                            processCount++;
                            connections.put(processName, processCount);
                        } else {
                            connections.put(processName, 1);
                        }
                    }
                }
            }
            ArrayList<Map.Entry<String, Integer>> forSort = new ArrayList<>(connections.entrySet());
            Collections.sort(forSort, (e1, e2) ->
                    e2.getValue().compareTo(e1.getValue()));

            System.out.println("Choose Option");

            for (int i = 0; i < forSort.size(); i++) {
                System.out.println(
                        (i + 1) + ". " +
                                forSort.get(i).getKey());
            }

            int choice =
                    Integer.parseInt(sc.nextLine());

            if (choice >= 1 && choice <= forSort.size()) {

                Map.Entry<String, Integer> selected =
                        forSort.get(choice - 1);
                System.out.println("******************************");
                System.out.println(selected.getKey() + " : " + selected.getValue());
                System.out.println("******************************");
            }

        }catch (IOException e) {
            System.out.println("Some Error Occur");
        }
    }

    private static void stopProcess(){
        shouldExist=true;
        if(service!= null){
            service.shutdown();
        }
    }
}