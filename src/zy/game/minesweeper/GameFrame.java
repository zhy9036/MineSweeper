package zy.game.minesweeper;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameFrame{
	
	private int MINE_CNT = 10;
	private int ROW_CNT = 9;
	private int COL_CNT = 9;
	private int NumNotMine; // = ROW_CNT*COL_CNT - MINE_CNT;
	MineButton[][] buttons;// = new MineButton[ROW_CNT][COL_CNT];
	
	//JFrame main = new JFrame("MineSweeper");
	JFrame f = new JFrame("MineSweeper v0.1");
	JMenuBar bar;
	JMenu menuGame;
	JPanel main;// = new JPanel(new GridLayout(ROW_CNT, COL_CNT));
	JLabel mineCount;// = new JLabel("Mine Count: " + MINE_CNT);
	
	boolean gameOver = false;
	
	public GameFrame(){
		
		initGame();
		initMenu();
		int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocation((width - f.getWidth())/2, (height - f.getHeight())/2);
		f.setResizable(false);
		f.setVisible(true);
		//System.out.println("finish...");
		
	}
	
	private void initGame(){
		System.out.println("game init...");
		gameOver = false;
		main = new JPanel(new GridLayout(ROW_CNT, COL_CNT));
		main.removeAll();
		f.getContentPane().removeAll();
		buttons = new MineButton[ROW_CNT][COL_CNT];
		NumNotMine = ROW_CNT*COL_CNT - MINE_CNT;
		mineCount = new JLabel("Mine Count: " + MINE_CNT);
		for(int r = 0; r < ROW_CNT; r++){
			for(int c = 0; c < COL_CNT; c++){
				buttons[r][c] = new MineButton(this, r, c);
				buttons[r][c].setIcon(MineButton.NORMAL);
				main.add(buttons[r][c]);
			}
		}
		System.out.println("mine genrating...");
		f.getContentPane().add(main, BorderLayout.CENTER);
		f.getContentPane().add(mineCount, BorderLayout.NORTH);
		
		//init board
		initMines();
		calculateMineCount();
		main.revalidate();
		//f.repaint();
	}
	
	private void paraInitGame(int col, int row, int mineCount){
		this.COL_CNT = col;
		this.ROW_CNT = row;
		this.MINE_CNT = mineCount;
		initGame();
		
	}
	
	private void initMenu(){
		bar = new JMenuBar();
		menuGame = new JMenu("game");
		JMenu diffculty = new JMenu("Diffculty");
		menuGame.setMnemonic(KeyEvent.VK_G);
		JMenuItem newGameItem = new JMenuItem("New Game");
		newGameItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				initGame();
				f.revalidate();
				f.pack();
				f.repaint();
				f.setVisible(true);
			}			
		});
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic('e');
		exitItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
			
		});
		menuGame.add(newGameItem);
		JMenuItem easyItem = new JMenuItem("Easy");
		JMenuItem midItem = new JMenuItem("Medium");
		JMenuItem hardItem = new JMenuItem("Hard");
		
		easyItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				paraInitGame(9,9,10);
				f.revalidate();
				f.pack();
				f.repaint();
				f.setVisible(true);
				
			}
			
		});
		
		midItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				paraInitGame(16,16,40);
				f.revalidate();
				f.pack();
				f.repaint();
				
				f.setVisible(true);
				
			}
			
		});
		
		hardItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				paraInitGame(31,16,99);
				f.revalidate();
				f.pack();
				f.repaint();
				
				f.setVisible(true);
				
			}
			
		});
		diffculty.add(easyItem);
		diffculty.add(midItem);
		diffculty.add(hardItem);
		menuGame.add(diffculty);
		menuGame.add(exitItem);
		bar.add(menuGame);
		f.setJMenuBar(bar);
	}
	
	
	public int getNumNotMine(){
		return this.NumNotMine;
	}
	
	public int getCol(){
		return this.COL_CNT;
	}
	
	public int getRow(){
		return this.ROW_CNT;
	}
	
	public void decreaseNumNotMine(){
		this.NumNotMine--;
	}
	
	public void updateGameOver(MineButton mb, boolean win){
		for(int r = 0; r < ROW_CNT; r++){
			for(int c = 0; c<COL_CNT; c++){
				if(mb != buttons[r][c] && buttons[r][c].state == State.CLOSED
						&& buttons[r][c].hasMine() && !win){
					buttons[r][c].setIcon(MineButton.MINE);
				}
				if(win && buttons[r][c].state == State.CLOSED)
					buttons[r][c].setIcon(MineButton.FLAG);
			}
		}
		if(!win){
			if(mb.hasMine())
				mb.setIcon(MineButton.MINEBOMB);
			else{
				ArrayList<MineButton> list = mb.getAroundButtons();
				for(MineButton b:list){
					if(b.hasMine() && b.state == State.CLOSED){
						b.setIcon(MineButton.MINEBOMB);
					}
					if(!b.hasMine() && b.state == State.FLAGED)
						b.setIcon(MineButton.WRONGBOMB);
				}
			}
			JOptionPane.showMessageDialog(null, "YOU STEPED ON MINE! :(");
		}else{
			JOptionPane.showMessageDialog(null, "YOU WON :)");
		}
	}
	
	
	public void setGameOver(){
		this.gameOver = true;
	}
	private void initMines(){
		System.out.println("initMines...");
		boolean[][] map = new boolean[ROW_CNT][COL_CNT];
		Random random = new Random();
		for (int i = 0; i < MINE_CNT; i++){ 
			int r,c;
			do{
				r = random.nextInt(ROW_CNT);
				c = random.nextInt(COL_CNT);
			}while(map[r][c]);
			map[r][c] = true;
			buttons[r][c].setHasMine();
		}
		System.out.println("initMines finished...");
	}
	
	private void calculateMineCount(){
		System.out.println("calculate count...");
		for (int r = 0 ; r < ROW_CNT; r++) {
			for (int c = 0; c < COL_CNT; c++) {
				int count = 0;
				if(!buttons[r][c].hasMine()){
					ArrayList<MineButton> list = buttons[r][c].getAroundButtons();
					for(MineButton b:list){
						if(b.hasMine()) count++;
					}
				}
				buttons[r][c].setMineCount(count);
			}
		}
		System.out.println("calculate finished...");
	}
}
