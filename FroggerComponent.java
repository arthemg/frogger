// Artsem Holdvekht
// CIS254
// Frogger
// 2 December 2015
//
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import java.io.BufferedReader;



public class FroggerComponent extends JComponent
{
	// size of the game grid
	public static final int WIDTH = 20;
	public static final int HEIGHT = 7;

	// initial pixel size for each grid square
	public static final int PIXELS = 50;

	// image filenames for car, lily, and frog
	public static final String[] IMAGES = new String[]{"frog.png", "car.png", "lily.png"};

	// colors for ROAD, WATER, and DIRT
	public static final Color[] COLORS = new Color[]{Color.BLACK, Color.BLUE, Color.GRAY};
	private Image frog;
	private Image car;
	private Image lily;

	// code to store what is in each square in the grid
	public static final int EMPTY = 0;
	public static final int CAR = 1;
	public static final int LILY = 2;

	private int[][] grid = new int[WIDTH][HEIGHT];
	Row[] rows = new Row[HEIGHT];
	private int frogX = 0;
	private int frogY = 6;
	private boolean dead;

	/*
	   Utility method to read in an Image object.
	   If the image can not load,
	   prints error out and returns null.
	   Uses Java standard ImageIO.read() method.
	*/

	private Image readImage(String filename)
	{
		Image image = null;
		try
		{
			image = ImageIO.read(new File(filename));
		}
		catch (IOException e)
		{
			System.out.println("Failed to load image '" + filename + "'");
			e.printStackTrace();
		}
		return (image);
	}

	private void readRow(String file)
	{
		try
		{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			int count = 0;

			while ((line != null) && (line != "\n") && (line != "\r"))
			{
				Row r = new Row(line);
				rows[count] = r;
				line = br.readLine();
				count++;
			}
		}
		catch (IOException ex)
		{
			System.out.println("File not found!");
		}
	}

	public FroggerComponent(String filename)
	{
		setPreferredSize(new Dimension(WIDTH * PIXELS, HEIGHT * PIXELS));
		readRow(filename);

		// cycle through the IMAGES array to rad every image in the array
		for (int i = 0; i < IMAGES.length; i++)
		{
			readImage(IMAGES[i]);
			//System.out.println(IMAGES[i]);
		}
	}

	public void reset()
	{
		for (int x = 0; x < grid.length; x++)
		{
			for (int y = 0; y < grid[x].length; y++)
			{
				grid[x][y] = EMPTY;

				dead = false;
				frogX = 0;
				frogY = 6;
			}
			repaint();
		}
	}

	public void moveBy(int dx, int dy)
	{
		if ((frogX + dx >= 0 && (frogX + dx) < WIDTH) && (frogY + dy >= 0 && frogY + dy < HEIGHT))
		{
			frogX += dx;
			frogY += dy;
		}
	}

	public boolean isWin()
	{

		return (frogY == 0);
	}

	public void key(int code)
	{
		if(!dead)
		{
			if (code == KeyEvent.VK_UP)
			{
				moveBy(0, -1);
			}
			else if (code == KeyEvent.VK_DOWN)
			{
				moveBy(0, 1);
			}
			else if (code == KeyEvent.VK_LEFT)
			{
				moveBy(-1, 0);
			}
			else if (code == KeyEvent.VK_RIGHT)
			{
				moveBy(1, 0);
			}
		}

		if ((rows[frogY].getType() == Row.WATER && grid[frogX][frogY] != LILY))
		{
			dead = true;
			//System.out.println("FROG X Y WATER " + grid[frogX][frogY] +"\n");

		}
		else if((grid[frogX][frogY] == CAR && rows[frogY].getType() == Row.ROAD))
		{
			dead = true;
			//System.out.println("FROG X Y CAr collision" + grid[frogX][frogY] +"\n");
		}
		repaint();
	}

	public void paintComponent(Graphics g) {
		for (int column = 0; column < grid.length; column++)
		{
			for (int row = 0; row < grid[column].length; row++)
			{
				try
				{
					car = readImage(IMAGES[1]);
					lily = readImage(IMAGES[2]);

					if (rows[row].getType() == Row.WATER)
					{
						g.setColor(COLORS[1]);
						g.fillRect(column * PIXELS, row * PIXELS, PIXELS, PIXELS);

						if(grid[column][row] == EMPTY)
						{
							g.fillRect(column * PIXELS, row * PIXELS, PIXELS, PIXELS);
						}
						else if(grid[column][row] == LILY )
						{
							g.drawImage(lily, column * PIXELS,row * PIXELS, PIXELS, PIXELS, null);
						}

					}
					else if (rows[row].getType() == Row.ROAD)
					{
						g.setColor(COLORS[0]);
						g.fillRect(column * PIXELS, row * PIXELS, PIXELS, PIXELS);

						if(grid[column][row] == EMPTY)
						{
							g.fillRect(column * PIXELS, row * PIXELS, PIXELS, PIXELS);
						}
						else if(grid[column][row] == CAR )
						{

							g.drawImage(car, column * PIXELS,row * PIXELS, PIXELS, PIXELS, null);
						}

					}
					else if (rows[row].getType() == Row.DIRT)
					{
						g.setColor(COLORS[2]);
						g.fillRect(column * PIXELS, row * PIXELS, PIXELS, PIXELS);
					}

				}
				catch (Exception ex)
				{
					System.out.println(rows[column] + "Columns/Row do not exist!");
				}

				frog = readImage(IMAGES[0]);
				g.drawImage(frog, frogX * PIXELS, frogY * PIXELS, PIXELS, PIXELS, null);

				if (dead == true)
				{
					g.setColor(Color.WHITE);
					g.drawLine(frogX * 50, frogY * 50, frogX * PIXELS + 50, (frogY * 50) + 50);
					g.drawLine(frogX * 50, frogY * 50 + 50, frogX * PIXELS + 50, frogY * PIXELS);
					//System.out.println(frogX + "G COMPONENT X\n");
					//System.out.println(frogY + "G COMPONENT Y\n");
					if(isWin())
					{
						//System.out.println("WE WON!");
						reset();

					}
				}
			}
		}
	}

	public void tick(int round)
	{
		for (int c = HEIGHT - 2; c >= 1; c--)
		{
			Row r = rows[c];

			if (r.isTurn(round))
			{
				int index = WIDTH - 1;
				int horLimit = WIDTH;

				while (index > 0)
				{
					if(frogX == index && frogY == c && grid[index][c] == LILY)
					{
						//System.out.println("FROG [" + frogX +"]["+ frogY +"]" + grid[frogX][frogY]);
						//System.out.println("FROG [" + frogX +"]["+ frogY +"]");
						//System.out.println("Grid index [" +index+"]["+c+"]"+ grid[index][c]);
						frogX = index + 1;
							if(frogX == horLimit)
							{
								//System.out.println("Index + 1 " + index+ 1);
								//System.out.println("Index position " + index);
								dead = true;
								frogX= index;
								//System.out.println("Is dead: " + dead);

							}


						//System.out.println("FROG After x y[" + frogX +"]["+ frogY +"]");

					}
					else if(frogX == index && frogY == c && grid[index - 1][c] == CAR)
					{
						dead = true;

					}

					grid[index][c] = grid[index - 1][c];
					//System.out.println("Grid index [" +index+"]["+c+"]"+ grid[index][c]);


					index--;
				}

				grid[index][c] = EMPTY;
			}

			if (r.isAdd())
			{
				if((r.getType() == Row.ROAD) && (!dead))
				{
					grid[0][c] = CAR;
				}
				else if((r.getType() == Row.WATER) && (!dead))
				{
					grid[0][c] = LILY;
				}
			}
		}

		repaint();

	}
}
