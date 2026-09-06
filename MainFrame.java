import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{
   public MainFrame(){

       //setting the features of the main window
       super("Network Traffic Tool");
       setSize(1100,700);
       setLocationRelativeTo(null);
       setDefaultCloseOperation(EXIT_ON_CLOSE);

       //setting the Panels in the main window so that it can feature different things
       JPanel top=new JPanel();
       top.setBackground(Color.darkGray);
       JPanel left=new JPanel();
       top.setPreferredSize(new Dimension(1100,70));
       left.setBackground(Color.lightGray);
       left.setPreferredSize(new Dimension(220, 600));
       JPanel main=new JPanel();
       main.setBackground(Color.GRAY);

       //adjusting the panels in the main window
       setLayout(new BorderLayout());
       add(top,BorderLayout.NORTH);
       add(left,BorderLayout.WEST);
       add(main,BorderLayout.CENTER);

       //setting the layout of the top panel so the button arrangement can be done automatically
       top.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));

       //adding the buttons in the top panel
       JButton startButton = new JButton("START");
       startButton.setPreferredSize(new Dimension(130, 40));
       startButton.setBackground(Color.WHITE);
       startButton.setForeground(Color.BLACK);
       startButton.setFocusPainted(false);

       JButton stopButton = new JButton("STOP");
       stopButton.setPreferredSize(new Dimension(130, 40));
       stopButton.setBackground(Color.WHITE);
       stopButton.setForeground(Color.BLACK);
       stopButton.setFocusPainted(false);

       JButton saveButton = new JButton("SAVE REPORT");
       saveButton.setPreferredSize(new Dimension(150, 40));
       saveButton.setBackground(Color.WHITE);
       saveButton.setForeground(Color.BLACK);
       saveButton.setFocusPainted(false);

       top.add(startButton);
       top.add(stopButton);
       top.add(saveButton);

       //setting the layout for the left panel
       left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
       left.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

       //setting the Label for the main heading
       JLabel monitorLabel = new JLabel("MONITOR CONTROLS");
       monitorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
       monitorLabel.setForeground(Color.WHITE);
       monitorLabel.setFont(new Font("Arial", Font.BOLD, 16));

       //setting the Buttons in the LEFT panel
       JButton showAllButton = new JButton("SHOW ALL CONNECTIONS");
       showAllButton.setPreferredSize(new Dimension(180, 40));
       showAllButton.setMaximumSize(new Dimension(200, 40));
       showAllButton.setAlignmentX(Component.CENTER_ALIGNMENT);
       showAllButton.setBackground(Color.DARK_GRAY);
       showAllButton.setForeground(Color.WHITE);
       showAllButton.setFocusPainted(false);

       JButton topNButton = new JButton("TOP N CONNECTIONS");
       topNButton.setPreferredSize(new Dimension(180, 40));
       topNButton.setMaximumSize(new Dimension(200, 40));
       topNButton.setAlignmentX(Component.CENTER_ALIGNMENT);
       topNButton.setBackground(Color.DARK_GRAY);
       topNButton.setForeground(Color.WHITE);
       topNButton.setFocusPainted(false);

       JButton specificButton = new JButton("SPECIFIC PROCESS");
       specificButton.setPreferredSize(new Dimension(180, 40));
       specificButton.setMaximumSize(new Dimension(200, 40));
       specificButton.setAlignmentX(Component.CENTER_ALIGNMENT);
       specificButton.setBackground(Color.DARK_GRAY);
       specificButton.setForeground(Color.WHITE);
       specificButton.setFocusPainted(false);

       //setting the labels in the left label
       JLabel statisticsLabel = new JLabel("STATISTICS");
       statisticsLabel.setPreferredSize(new Dimension(180, 35));
       statisticsLabel.setMaximumSize(new Dimension(200, 35));
       statisticsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
       statisticsLabel.setForeground(Color.WHITE);
       statisticsLabel.setFont(new Font("Arial", Font.BOLD, 16));

       JLabel totalConnectionsLabel = new JLabel("Total Connections: 0");
       totalConnectionsLabel.setPreferredSize(new Dimension(180, 30));
       totalConnectionsLabel.setMaximumSize(new Dimension(200, 30));
       totalConnectionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
       totalConnectionsLabel.setForeground(Color.WHITE);

       //adding all the components in the left panel
       left.add(monitorLabel);
       left.add(Box.createVerticalStrut(20));

       left.add(showAllButton);
       left.add(Box.createVerticalStrut(10));

       left.add(topNButton);
       left.add(Box.createVerticalStrut(10));

       left.add(specificButton);
       left.add(Box.createVerticalStrut(30));

       left.add(statisticsLabel);
       left.add(Box.createVerticalStrut(20));

       left.add(totalConnectionsLabel);
       left.add(Box.createVerticalStrut(10));

       //setting the layout of the main panel
       main.setLayout(new BorderLayout());
       main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

       //Components of the main panel
       JLabel outputLabel = new JLabel("NETWORK CONNECTIONS");
       outputLabel.setForeground(Color.WHITE);
       outputLabel.setFont(new Font("Arial", Font.BOLD, 16));
       outputLabel.setHorizontalAlignment(SwingConstants.CENTER);
       outputLabel.setPreferredSize(new Dimension(0, 35));

       JTextArea outputArea = new JTextArea();
       outputArea.setEditable(false);
       outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
       outputArea.setLineWrap(false);
       outputArea.setWrapStyleWord(false);
       outputArea.setBackground(Color.BLACK);
       outputArea.setForeground(Color.WHITE);
       outputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

       JScrollPane scrollPane = new JScrollPane(outputArea);

       scrollPane.setPreferredSize(new Dimension(0, 0));
       scrollPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

       main.add(outputLabel, BorderLayout.NORTH);
       main.add(scrollPane, BorderLayout.CENTER);

       setVisible(true);
   }

}
