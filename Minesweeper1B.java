import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Minesweeper1B extends JFrame implements ActionListener,MouseListener
{
	JToggleButton[][] board;
	JPanel boardPanel;
	boolean firstClick, gameOn;
	int numMines;
	int clickCount;

	ImageIcon flagIcon,mineIcon;
	ImageIcon[] numbers;



	public Minesweeper1B()
	{
		mineIcon=new ImageIcon("Minesweeper Images/mine0.png");
		mineIcon=new ImageIcon(
			mineIcon.getImage().getScaledInstance(40,40,Image.SCALE_SMOOTH));

		flagIcon=new ImageIcon("Minesweeper Images/flag.png");
		flagIcon=new ImageIcon(
			flagIcon.getImage().getScaledInstance(40,40,Image.SCALE_SMOOTH));

		numbers=new ImageIcon[9];
		for(int x=1;x<9;x++)
		{
			numbers[x]=new ImageIcon("Minesweeper Images/"+x+".png");
			numbers[x]=new ImageIcon(
				numbers[x].getImage().getScaledInstance(40,40,Image.SCALE_SMOOTH));

		}
		try
		{
			UIManager.setLookAndFeel(
				"com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		}catch(Exception e){}

		UIManager.put("ToggleButton.select",new Color(255,128,130));


		createBoard(9,9,10);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

	}
	public void createBoard(int rows,int cols, int nm)
	{
		if(boardPanel!=null)
			this.remove(boardPanel);

		clickCount=0;
		gameOn=true;
		firstClick=true;
		numMines=nm;
		board=new JToggleButton[rows][cols];
		boardPanel=new JPanel();
		boardPanel.setLayout(new GridLayout(rows,cols));
		for(int r=0;r<rows;r++)
		{
			for(int c=0;c<cols;c++)
			{
				board[r][c]=new JToggleButton();
				//delete me! It was just a test to see if the image worked
				//board[r][c].setIcon(numbers[1]);
				board[r][c].putClientProperty("row",r);
				board[r][c].putClientProperty("col",c);
				board[r][c].putClientProperty("state",0);
				board[r][c].setFocusable(false);
				board[r][c].setOpaque(true);

				board[r][c].setMargin(new Insets(0,0,0,0));

				board[r][c].addMouseListener(this);
				boardPanel.add(board[r][c]);
			}
		}
		this.add(boardPanel,BorderLayout.CENTER);
		this.setSize(cols*50,rows*50);
		this.revalidate();
	}

	public void dropMines(int row,int col)
	{
		int count=0;
		while(count<numMines)
		{
			int randR=(int)(Math.random()*board.length);
			int randC=(int)(Math.random()*board[0].length);
			int state=(int)(board[randR][randC].getClientProperty("state"));
			if(state!=10 && Math.abs(randR-row)>1 && Math.abs(randC-col)>1)
			{
				board[randR][randC].putClientProperty("state",10);
				for(int r=randR-1;r<=randR+1;r++)
				{
					for(int c=randC-1;c<=randC+1;c++)
					{
						try
						{
							state=(int)(board[r][c].getClientProperty("state"));
							if(state!=10)
							{
								board[r][c].putClientProperty("state",state+1);
							}
						}
						catch(ArrayIndexOutOfBoundsException e)
						{
						}
					}
				}




				count++;
			}
		}
/*
		for(int r=0;r<board.length;r++)
		{
			for(int c=0;c<board[0].length;c++)
			{
				int state=(int)(board[r][c].getClientProperty("state"));
				board[r][c].setText(""+state);
			}
		}
*/
	}

	public void actionPerformed(ActionEvent e)
	{
	}

	public void mouseReleased(MouseEvent e)
	{
		if(gameOn)
		{
			int row=(int)(((JToggleButton)e.getComponent()).getClientProperty("row"));
			int col=(int)(((JToggleButton)e.getComponent()).getClientProperty("col"));
			int state=(int)(((JToggleButton)e.getComponent()).getClientProperty("state"));
			if(e.getButton()==MouseEvent.BUTTON1
											&& board[row][col].isEnabled())
			{
				if(firstClick)
				{
					firstClick=false;
					dropMines(row,col);
				}

				if(state==10)
				{
					//board[row][col].setIcon(mineIcon);
					showMines();
					JOptionPane.showMessageDialog(null,"You are a loser!");
					gameOn=false;
				}
				else
				{
					board[row][col].setEnabled(false);
					board[row][col].setSelected(true);
					clickCount++;
					expand(row,col);

					if(clickCount==board.length*board[0].length-numMines)
						JOptionPane.showMessageDialog(null,"You are a winner!");

				}
			}
			if(e.getButton()==MouseEvent.BUTTON3)
			{
				//what happens in here?
				//SET FLAGS!
				if(!board[row][col].isSelected())
				{
					if(board[row][col].getIcon()==null)
					{
						board[row][col].setIcon(flagIcon);
						board[row][col].setDisabledIcon(flagIcon);
						board[row][col].setEnabled(false);
					}
					else
					{
						board[row][col].setIcon(null);
						board[row][col].setDisabledIcon(null);
						board[row][col].setEnabled(true);

					}
				}

			}



		}
		else // this goes with the gameOn if-statement
		{
			//reset the game
		}
	}
	public void showMines()
	{
		for(int r=0;r<board.length;r++)
		{
			for(int c=0;c<board[0].length;c++)
			{
				board[r][c].setEnabled(false);

				int state=(int)board[r][c].getClientProperty("state");
				if(state==10)
				{
					board[r][c].setIcon(mineIcon);
					board[r][c].setDisabledIcon(mineIcon);
				}
			}
		}

	}
	public void expand(int row,int col)
	{

		int state=(int)board[row][col].getClientProperty("state");

		if(!board[row][col].isSelected())
		{
			clickCount++;
			board[row][col].setSelected(true);
			board[row][col].setEnabled(false);
		}

		if(state!=0)
		{
			//board[row][col].setText(""+state);
			board[row][col].setIcon(numbers[state]);
			board[row][col].setDisabledIcon(numbers[state]);

		}
		else
		{
			for(int r=row-1;r<=row+1;r++)
			{
				for(int c=col-1;c<=col+1;c++)
				{
					try
					{
						if(!board[r][c].isSelected())
							expand(r,c);
					}
					catch(ArrayIndexOutOfBoundsException e)
					{
					}

				}
			}

		}

	}



	public void mousePressed(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}



	public static void main(String[] args)
	{
		Minesweeper1B app=new Minesweeper1B();
	}
}