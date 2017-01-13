// Artsem Holdvekht
// CIS254
// Frogger
// 2 December 2015
//
import java.awt.*;
import java.awt.event.*; 
import javax.swing.*;
/* Subclass of JFrame that handles all the typical frame tasks, especially 
 * being a listner for ActionEvents and KeyEvents.
 */

 public class FroggerFrame extends JFrame implements KeyListener, ActionListener
 {
        public static final int DELAY = 400;
        public static final String GAME_FILE = "world.txt";

        private FroggerComponent frogger;
        private JButton newGame;
        private JLabel label;
        private javax.swing.Timer timer;
        int level = 1;
        int round = 1;
        int dDelay = 0;

 	public FroggerFrame()
 	{
 		setTitle("Frogger");
 		Container container = getContentPane( ); 
		container.setLayout(new BorderLayout( ));
		frogger = new FroggerComponent(GAME_FILE);
		container.add(frogger, BorderLayout.CENTER);
		frogger.addKeyListener(this); // adding the KeyListener
		frogger.setFocusable(true); // setting focusable to true

		JPanel panel = new JPanel( ); 


		newGame = new JButton("New Game");
		newGame.addActionListener(this);
		newGame.setFocusable(false);
		panel.add(newGame);

		label = new JLabel("Level: " + level);
		label.setFocusable(false);
		panel.add(label);
        validate();
        repaint();

		timer = new javax.swing.Timer(DELAY, this);

		if(frogger.isWin())
		{
			System.out.println("WE WON");
			level = level + 1;
			validate();

		}
        repaint();
		container.add(panel, BorderLayout.SOUTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frogger.requestFocusInWindow( );
		pack( );
		setVisible(true);

 	}


 	// keyTyped and keyReleased are required because we are implementing 
	// the KeyListener interface. Both should be ignored,
	// since we only care about key presses.
	public void keyTyped(KeyEvent e){}
	public void keyReleased(KeyEvent e) {}
	// For keyPressed: hand off the key code of the event to the // PacmanComponent to deal with (slides 110-111)
	public void keyPressed(KeyEvent e)
	{
		frogger.key(e.getKeyCode( ));
	}


	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(newGame))
		{
			frogger.reset();
			timer.start();

		}
        else if (frogger.isWin())
        {
            level += 1;
            if(level <=2)
            {
                dDelay = (int) (DELAY * 0.85);
            }
            else
            {
                dDelay = (int) (dDelay * 0.85);
            }

                //System.out.println(dDelay);
            timer.setDelay(dDelay);
            label.setText("Level: " + level);

                //System.out.println(level);
            frogger.reset();



		}
        else if (e.getSource().equals(timer))
        {
			frogger.tick(round++);

		}
        repaint();
	}

	// Our usual simple main
	public static void main(String[ ] args) 
	{
		FroggerFrame frame = new FroggerFrame( );
	}

 }