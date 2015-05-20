package zy.game.minesweeper;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class MineButton extends JButton {
	/**
	 * 
	 */
	private MineButton mb = this;
	private static final long serialVersionUID = 1L;
	private boolean hasMine = false;
	private int mineCount = 0;
	
	
	static ImageIcon ONE = new ImageIcon("images/1.png");
	static ImageIcon TWO = new ImageIcon("images/2.png");
	static ImageIcon THREE = new ImageIcon("images/3.png");
	static ImageIcon FOUR = new ImageIcon("images/4.png");
	static ImageIcon FIVE = new ImageIcon("images/5.png");
	static ImageIcon SIX = new ImageIcon("images/6.png");
	static ImageIcon SEVEN = new ImageIcon("images/7.png");
	static ImageIcon EIGHT = new ImageIcon("images/8.png");
	static ImageIcon MINE = new ImageIcon("images/bomb.png");
	static ImageIcon MINEBOMB = new ImageIcon("images/bombEx.png");
	static ImageIcon WRONGBOMB = new ImageIcon("images/wrongBomb.png");
	static ImageIcon FLAG = new ImageIcon("images/flag.png");
	static ImageIcon BLANK = new ImageIcon("images/pressed.png");
	static ImageIcon NORMAL = new ImageIcon("images/normal.png");
	
	private static final int buttonL = 32;
	State state = State.CLOSED;
	GameFrame gf;
	int col, row;

	public MineButton(GameFrame gf, int r, int c){
		this.gf = gf;
		col = c;
		row = r;
		this.setPreferredSize(new Dimension(buttonL, buttonL));
		this.setFont(new Font(null, Font.PLAIN, 20));
		this.setBorderPainted(false);
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				System.out.println("clicked");
				/*
				if(gf.gameOver){
					gf.f.setTitle("MineSweeper v0.1 Game Over");
					return;
				}
				if(state == State.OPENED)
					return;
				
				
				//left click
				if(e.getButton() == MouseEvent.BUTTON1) {
					if(hasMine){
						//mb.setIcon(MINEBOMB);
						//gf.f.repaint();
						gf.setGameOver();
						gf.updateGameOver(mb, false);
						System.out.println("LEFT: mine");
						
					}else if (state == State.CLOSED) {
						state = State.OPENED;
						if(mineCount == 0){	
							mb.setIcon(BLANK);
							//UPDATE ARROUND BUTTON
							updateArroundButtons(mb);
							System.out.println("LEFT: empty");
						}else{
							mb.setIcon(getNumIcon());
							gf.decreaseNumNotMine();
							System.out.println("LEFT: num");
							
						}						
					}
					if(gf.getNumNotMine() == 0){
						//JOptionPane.showMessageDialog(null, "YOU WON!");
						gf.updateGameOver(mb, true);
					}
					//System.out.println(gf.getNumNotMine()+"");
				//right click	
				}else if (e.getButton() == MouseEvent.BUTTON3){
					if (state == State.CLOSED) {
						state = State.FLAGED;
						mb.setIcon(FLAG);
						System.out.println("RIGHT: set flag");
					}else if (state == State.FLAGED){
						state = State.CLOSED;
						mb.setIcon(NORMAL);
						System.out.println("RIGHT: back normal");
					}else{
						System.out.println("QIPA "+state);
					}
				}
				*/
				
			}
			
			@Override
			public void mousePressed(MouseEvent e){
				if(gf.gameOver){
					gf.f.setTitle("MineSweeper v0.1 Game Over");
					return;
				}
				
				
				//both right and left down
				int onMask = MouseEvent.BUTTON1_DOWN_MASK & MouseEvent.BUTTON3_DOWN_MASK;
				int offMask = MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.BUTTON3_DOWN_MASK;
				
				if ((e.getModifiersEx() & (onMask | offMask)) == offMask){
			       //System.out.println("Both Down!");
				   //update around buttons
					System.out.println("double key down!");
					expandArroundButtons();
					System.out.println("double " + gf.getNumNotMine()+"");
					if(gf.getNumNotMine() == 0){
						gf.updateGameOver(mb, true);
						//JOptionPane.showMessageDialog(null, "YOU WON!");
					}
		        }else{
		        	if(state == State.OPENED)
						return;
					//left click
					if(e.getButton() == MouseEvent.BUTTON1) {
						if(hasMine){
							//mb.setIcon(MINEBOMB);
							//gf.f.repaint();
							gf.setGameOver();
							gf.updateGameOver(mb, false);
							System.out.println("LEFT: mine");
							
						}else if (state == State.CLOSED) {
							state = State.OPENED;
							if(mineCount == 0){	
								mb.setIcon(BLANK);
								//UPDATE ARROUND BUTTON
								updateArroundButtons(mb);
								System.out.println("LEFT: empty");
							}else{
								mb.setIcon(getNumIcon());
								gf.decreaseNumNotMine();
								System.out.println("LEFT: num");
								
							}						
						}
						if(gf.getNumNotMine() == 0){
							//JOptionPane.showMessageDialog(null, "YOU WON!");
							gf.updateGameOver(mb, true);
						}
						//System.out.println(gf.getNumNotMine()+"");
					//right click	
					}else if (e.getButton() == MouseEvent.BUTTON3){
						if (state == State.CLOSED) {
							state = State.FLAGED;
							mb.setIcon(FLAG);
							System.out.println("RIGHT: set flag");
						}else if (state == State.FLAGED){
							state = State.CLOSED;
							mb.setIcon(NORMAL);
							System.out.println("RIGHT: back normal");
						}else{
							System.out.println("QIPA "+state);
						}
					}
		        }
			}
			
		});
	}
	
	
	private void expandArroundButtons(){
		if(this.state == State.OPENED && this.mineCount > 0){
			int count = this.mineCount;
			MineButton bomb = null;
			ArrayList<MineButton> toExpand = new ArrayList();
			ArrayList<MineButton> neighbor = this.getAroundButtons();
			for(MineButton b: neighbor){
				if(b.hasMine && b.state != State.FLAGED)
					bomb = b;
				if(b.state == State.OPENED)
					continue;
				if(b.state == State.FLAGED)
					count--;
				else
					toExpand.add(b);
			}
			if(count == 0){
				if(bomb!=null){
					gf.setGameOver();
					gf.updateGameOver(this, false);
					return;
				}
				for(MineButton b : toExpand){
					if(b.state != State.OPENED){
						b.state = State.OPENED;
						if(b.mineCount == 0){							
							b.setIcon(BLANK);
							//UPDATE ARROUND BUTTON
							updateArroundButtons(b);
						}else{
							b.setIcon(b.getNumIcon());
							gf.decreaseNumNotMine();
							
						}
					}
				}
			}
		}
	}
	
	public ArrayList<MineButton> getAroundButtons(){
		ArrayList<MineButton> list = new ArrayList();
		MineButton[][] buttons = gf.buttons;
		MineButton b;
		int c = this.col;
		int r = this.row;
		if(r-1 >=0 && c-1 >= 0){
			b = buttons[r-1][c-1];
			list.add(b);
		}
		
		if(r-1 >=0){
			b = buttons[r-1][c];
			list.add(b);
		}
		
		if(r-1 >=0 && c+1 < gf.getCol()){
			b = buttons[r-1][c+1];
			list.add(b);
		}
		
		if(c-1 >= 0){
			b = buttons[r][c-1];
			list.add(b);
		}
		
		if(c+1 < gf.getCol()){
			b = buttons[r][c+1];
			list.add(b);
		}
		
		if(r+1 < gf.getRow() && c-1 >=0){
			b = buttons[r+1][c-1];
			list.add(b);
		}
		
		if(r+1 < gf.getRow()){
			b = buttons[r+1][c];
			list.add(b);
		}
		
		if(c+1 < gf.getCol() && r+1 < gf.getRow()){
			b = buttons[r+1][c+1];
			list.add(b);
		}
			 
		return list;
	}
	
	public int getMineCount(){
		return this.mineCount;
	}
	
	public void showMineCount(){
		if(!this.hasMine() ){
			if(this.mineCount > 0)
				this.setIcon(getNumIcon());
			else
				this.setIcon(BLANK);
		}
	}
	public void updateArroundButtons(MineButton button) {
		System.out.println("get into UAB");
		Queue<MineButton> queue = new LinkedList();
		queue.add(button);
		MineButton[][] buttons = gf.buttons;
		while(!queue.isEmpty()){
			MineButton curButton = queue.poll();
			gf.decreaseNumNotMine();
			int c = curButton.col;
			int r = curButton.row;
			int COL_CNT = gf.getCol();
			int ROW_CNT = gf.getRow();
			MineButton tmp;
			if(r-1>=0 && c-1>=0 && !buttons[r-1][c-1].hasMine()){
				tmp = buttons[r-1][c-1];
				if(tmp.state == State.CLOSED) {
					if (tmp.getMineCount() == 0) 
						queue.add(tmp);
					else{
						gf.decreaseNumNotMine();
					}
					tmp.showMineCount();
					tmp.state = State.OPENED;
				}
			}
			if(r-1>=0 && !buttons[r-1][c].hasMine()){
				tmp = buttons[r-1][c];
				if(tmp.state == State.CLOSED) {
					if (tmp.getMineCount() == 0) 
						queue.add(tmp);
					else{
						gf.decreaseNumNotMine();
					}
					tmp.showMineCount();
					tmp.state = State.OPENED;
				}
			}
			
			if(r-1>=0 && c+1 < COL_CNT && !buttons[r-1][c+1].hasMine()){
				tmp = buttons[r-1][c+1];
				if(tmp.state == State.CLOSED) {
					if (tmp.getMineCount() == 0) 
						queue.add(tmp);
					else{
						gf.decreaseNumNotMine();
					}
					tmp.showMineCount();
					tmp.state = State.OPENED;
				}
			}
			
			if(c-1>=0 && !buttons[r][c-1].hasMine()){
				tmp = buttons[r][c-1];
				if(tmp.state == State.CLOSED) {
					if (tmp.getMineCount() == 0) 
						queue.add(tmp);
					else{
						gf.decreaseNumNotMine();
					}
					tmp.showMineCount();
					tmp.state = State.OPENED;
				}
			}
			
			if(c+1<COL_CNT && !buttons[r][c+1].hasMine()){
				tmp = buttons[r][c+1];
				if(tmp.state == State.CLOSED) {
					if (tmp.getMineCount() == 0) 
						queue.add(tmp);
					else{
						gf.decreaseNumNotMine();
					}
					tmp.showMineCount();
					tmp.state = State.OPENED;
				}
			}
			
			if(r+1 < ROW_CNT && !buttons[r+1][c].hasMine()){
				tmp = buttons[r+1][c];
				if(tmp.state == State.CLOSED) {
					if (tmp.getMineCount() == 0) 
						queue.add(tmp);
					else{
						gf.decreaseNumNotMine();
					}
					tmp.showMineCount();
					tmp.state = State.OPENED;
				}
			}
			if(r+1<ROW_CNT && c-1>=0 && !buttons[r+1][c-1].hasMine()){
				tmp = buttons[r+1][c-1];
				if(tmp.state == State.CLOSED) {
					if (tmp.getMineCount() == 0) 
						queue.add(tmp);
					else{
						gf.decreaseNumNotMine();
					}
					tmp.showMineCount();
					tmp.state = State.OPENED;
				}
			}
			if(r+1 < ROW_CNT && c+1 < COL_CNT && !buttons[r+1][c+1].hasMine()){
				tmp = buttons[r+1][c+1];
				if(tmp.state == State.CLOSED) {
					if (tmp.getMineCount() == 0) 
						queue.add(tmp);
					else{
						gf.decreaseNumNotMine();
					}
					tmp.showMineCount();
					tmp.state = State.OPENED;
				}				
			}
		}
		System.out.println("get OUT into UAB");
	}
	
	private ImageIcon getNumIcon(){
		ImageIcon c = null;
		switch(mineCount){
			case 1:
				c = ONE;
				break;
			case 2:
				c = TWO;
				break;
			case 3:
				c = THREE;
				break;
			case 4:
				c= FOUR;
				break;
			case 5:
				c = FIVE;
				break;
			case 6:
				c = SIX;
				break;
			case 7:
				c = SEVEN;
				break;
			case 8:
				c = EIGHT;
				break;
		}
		
		return c;
	}
	
	
	public void setHasMine(){
		this.hasMine = true;
	}
	
	public boolean hasMine(){
		return hasMine;
	}
	
	public void setMineCount(int count){
		this.mineCount = count;
	}
	
}

enum State{
	OPENED, CLOSED, FLAGED
}