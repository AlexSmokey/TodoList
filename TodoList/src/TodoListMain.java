import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class TodoListMain implements Runnable{

	// creates the frame
	final static JFrame myFrame = new JFrame();
	
	// height of the frame
	final static int HEIGHT = 300;
	final static int WIDTH = 800;
	final static Font myFont = new Font("Verdana", Font.BOLD, 24);
	private Activity main;
	private Timer timer;
	private ArrayList<Activity> activities;
	private JPanel timePanel;
	private JPanel activityPanel;
	private JLabel activity;
	private JLabel time;
	private JPanel buttons;
	private JButton pauseButton;
	private JButton deleteButton;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu activityMenu;
	private JMenuItem newMenuItem;
	private JMenuItem openMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem newActivityMenuItem;
	
	/**
	 * Creates the game frame and implements the exit and new game buttons
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		SwingUtilities.invokeLater(new TodoListMain());
	}


	public void run() {
			
		main = null;
		activities = new ArrayList<Activity>();
		
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (main != null) {
					main.increment();
					time.setText(main.getTime() + " seconds");
				}
			}
		});
		
		
		
		// sets frame size
		myFrame.setSize(WIDTH, HEIGHT);
		// sets frame title
		myFrame.setTitle("TODO List");
		// creates panel
		activityPanel = new JPanel();
		myFrame.add(activityPanel, BorderLayout.NORTH);
		activity = new JLabel();
		activity.setFont(myFont);
		activityPanel.add(activity);
		activity.setText("CHOOSE AN ACTIVITY FROM ACTIVITY TAB\n\n");
		
		
		timePanel = new JPanel();
		myFrame.add(timePanel, BorderLayout.CENTER);
		time = new JLabel();
		time.setFont(myFont);
		timePanel.add(time);
		time.setText("---");
		
		
		
		buttons = new JPanel();
		myFrame.add(buttons, BorderLayout.SOUTH);
		pauseButton = new JButton("START");
		buttons.add(pauseButton);
		ActionListener pause = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (main != null) {
					if (timer.isRunning()) {
						timer.stop();
						pauseButton.setText("START");
					} else {
						timer.start();
						pauseButton.setText("PAUSE");
					}
				}
			}

		};
		pauseButton.addActionListener(pause);

		deleteButton = new JButton("DELETE");
		buttons.add(deleteButton);
		// creates a new game when new game button is pressed
		ActionListener delete = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (main != null) {
					String temp = main.getName();
					activities.remove(main);
					main = null;
					timer.stop();
					pauseButton.setText("START");
					activity.setText("CHOOSE AN ACTIVITY FROM ACTIVITY TAB\n\n");
					time.setText("---");
					Component[] options = activityMenu.getMenuComponents();
					JMenuItem mi;
					for (int i = 0; i < options.length; i++) {
						mi = (JMenuItem)options[i];
						if (temp.equals(mi.getText())) {
							activityMenu.remove(mi);
							break;
						}
					}
				}
			}

		};
		deleteButton.addActionListener(delete);
		
		
		
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		newMenuItem = new JMenuItem("New");
		newMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = JOptionPane.showConfirmDialog(myFrame, "Would you like to save first?");
				
				if(choice == JOptionPane.YES_OPTION) {
					if (save()) {
						activities.clear();
						main = null;
						timer.stop();
						pauseButton.setText("START");
						activity.setText("CHOOSE AN ACTIVITY FROM ACTIVITY TAB\n\n");
						time.setText("---");
						Component[] options = activityMenu.getMenuComponents();
						for (int i = 0; i < options.length; i++) {
							if (!options[i].equals(newActivityMenuItem)) {
								activityMenu.remove(options[i]);
							}
						}
					} else {
						JOptionPane.showMessageDialog(null, "Data was not saved!", "Warning!", JOptionPane.INFORMATION_MESSAGE);
					}
				} else if (choice == JOptionPane.NO_OPTION) {
					activities.clear();
					main = null;
					timer.stop();
					pauseButton.setText("START");
					activity.setText("CHOOSE AN ACTIVITY FROM ACTIVITY TAB\n\n");
					time.setText("---");
					Component[] options = activityMenu.getMenuComponents();
					for (int i = 0; i < options.length; i++) {
						if (!options[i].equals(newActivityMenuItem)) {
							activityMenu.remove(options[i]);
						}
					}
				} 
			}
			
		});
		fileMenu.add(newMenuItem);
		openMenuItem = new JMenuItem("Open");
		openMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				open();
			}
			
		});
		fileMenu.add(openMenuItem);
		saveMenuItem = new JMenuItem("Save");
		saveMenuItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
			
		});
		fileMenu.add(saveMenuItem);
		
		
		
		activityMenu = new JMenu("Activity");
		newActivityMenuItem = new JMenuItem("New Activity");
		newActivityMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane
						.showInputDialog("What is the name of this activity");
				
				if (name.equals("") || name.length() > 40) {
					name = "Some Activity";
				}
				Activity tempActivity = new Activity(name);
				activities.add(tempActivity);
				JMenuItem temp = new JMenuItem(name);
				temp.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						main = tempActivity;
						activity.setText(tempActivity.getName());
						time.setText(tempActivity.getTime() + " seconds");
						if (!timer.isRunning()) {
							timer.start();
							pauseButton.setText("PAUSE");
						}
					}
					
				});
				activityMenu.add(temp);
			}
			
		});
		activityMenu.add(newActivityMenuItem);
		activityMenu.addSeparator();
		menuBar.add(fileMenu);
	    menuBar.add(activityMenu);
		
		// put the menu bar on the frame
	    myFrame.setJMenuBar(menuBar);
	    myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    myFrame.pack();
	    myFrame.setLocationRelativeTo(null);
	    myFrame.setVisible(true);
	}
	
	public boolean save() {
		String fileName = JOptionPane.showInputDialog("What would you like to name your activity list?");
		String str = "";
		for (int i = 0; i < activities.size(); i++) {
			str = str + activities.get(i).getName() +":" + activities.get(i).getTime() + "\n";
		}
	    BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(fileName));
			writer.write(str);
		    writer.close();
		} catch (Exception e) {
			return false;
		}
		return true;

	}
	public void open() {
		//TODO
	}

}
