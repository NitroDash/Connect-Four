package game.state;

import game.main.GameMain;
import game.main.Resources;
import game.model.AI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;

public class PlayState extends State {
	public static int[][] board;
	private static final Color[] chipColors={Color.WHITE,Color.RED,Color.BLACK};
	private static boolean chipDropping,AI;
	private static int chipDestination,playerGoing;
	public static int droppedX;
	private static int droppedY;
	private static int winner;
	private static int AImoveCounter;
	private static int AIintendedMove;
	private static double chipDY,chipY;
	private static AI player;
	public static Random random = new Random();
	public static int AIplayer;
	public static int[] wins = new int[2];
	
	public PlayState(int AIdifficulty) {
		if (AIdifficulty>0) {
			AIplayer=random.nextInt(2)+1;
			player=new AI(AIdifficulty);
			AI=true;
		} else {
			AI=false;
		}
	}
	
	@Override
	public void init() {
		board=new int[7][6];
		chipDropping=false;
		playerGoing=1;
		droppedX=3;
		chipY=100;
		winner=0;
		AIintendedMove=-1;
		if (AIplayer==1) {
			AIintendedMove=player.selectMove();
			AImoveCounter=45;
		}
	}

	@Override
	public void update(float delta) {
		if (AIintendedMove>=0) {
			AImoveCounter--;
			if (AImoveCounter==0) {
				AImoveCounter=15;
				if (AIintendedMove<droppedX) {
					do {
						droppedX--;
						if (droppedX<0) {
							droppedX=board.length-1;
						}
					} while (board[droppedX][0]!=0);
				} else if (AIintendedMove>droppedX) {
					do {
						droppedX++;
						if (droppedX>=board.length) {
							droppedX=0;
						}
					} while (board[droppedX][0]!=0);
				} else {
					droppedY=-1;
					for (int i = 0; i < board[0].length; i++) {
						if (board[droppedX][i]!=0) {
							droppedY=i-1;
							break;
						}
					}
					if (droppedY==-1) {
						droppedY=board[0].length-1;
						}
						chipDestination=95+(droppedY+1)*100;
					chipDropping=true;
					AIintendedMove=-1;
				}
			}
		}
		if (chipDropping) {
			chipY+=chipDY;
			if (chipY>=chipDestination) {
				chipY=chipDestination;
				if (chipDY>2.5) {
					chipDY*=-0.5;
				} else {
					chipDropping=false;
					board[droppedX][droppedY]=playerGoing;
					chipY=100;
					chipDY=0;
					if (checkForRow(playerGoing,4,droppedX,droppedY)) {
						winner=playerGoing;
					} else {
						playerGoing=3-playerGoing;
						int counter=0;
						while (board[droppedX][0]!=0) {
							counter++;
							droppedX++;
							if (droppedX>=board.length) {
								droppedX=0;
							}
							if (counter>=board.length) {
								winner=-1;
								break;
							}
						}
						if (winner==0&&AI&&playerGoing==AIplayer) {
								AIintendedMove=player.selectMove();
							AImoveCounter=45;
						}
					}
				}
			} else {
				chipDY++;
			}
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0,0,GameMain.GAME_WIDTH,GameMain.GAME_HEIGHT);
		if (winner==0) {
			g.setColor(chipColors[playerGoing]);
			g.fillOval(droppedX*100+15, (int)chipY-80, 70, 70);
		}
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[0].length; y++) {
				if (board[x][y]!=0) {
					g.setColor(chipColors[board[x][y]]);
					g.fillRect(x*100,y*100+100,100,100);
				}
				g.drawImage(Resources.boardSquare, x*100,y*100+100,100,100,null);
			}
		}
		if (winner>0) {
			g.setFont(new Font("Times New Roman",0,50));
			g.setColor(chipColors[winner]);
			g.drawString("Player "+winner+" wins!", 200, 80);
		} else if (winner<0) {
			g.setFont(new Font("Times New Roman",0,50));
			g.setColor(chipColors[2]);
			g.drawString("It's a tie!", 250, 80);
		}
	}

	@Override
	public void onClick(MouseEvent e) {
		
	}

	@Override
	public void onKeyPress(KeyEvent e) {
		if (!chipDropping&&winner==0&&(playerGoing!=AIplayer||!AI)) {
			if (e.getKeyCode()==KeyEvent.VK_RIGHT) {
				do {
					droppedX++;
					if (droppedX>=board.length) {
						droppedX=0;
					}
				} while (board[droppedX][0]!=0);
			}
			if (e.getKeyCode()==KeyEvent.VK_LEFT) {
				do {
					droppedX--;
					if (droppedX<0) {
						droppedX=board.length-1;
					}
				} while (board[droppedX][0]!=0);
			}
			if (e.getKeyCode()==KeyEvent.VK_SPACE) {
				droppedY=-1;
				for (int i = 0; i < board[0].length; i++) {
					if (board[droppedX][i]!=0) {
						droppedY=i-1;
						break;
					}
				}
				if (droppedY==-1) {
					droppedY=board[0].length-1;
				}
				chipDestination=95+(droppedY+1)*100;
				chipDropping=true;
			}
		}
		if (winner!=0) {
			if (e.getKeyCode()==KeyEvent.VK_ENTER) {
				setCurrentState(new MenuState());
			}
		}
	}

	@Override
	public void onKeyRelease(KeyEvent e) {
		
	}
	
	public static boolean checkForRow(int player, int length, int x, int y) {
		if (x>=board.length||x<0||y<0||y>=board[0].length) {
			return false;
		}
		if (getRowLength(x,y,1,0,player)>=length) {
			return true;
		}
		if (getRowLength(x,y,1,1,player)>=length) {
			return true;
		}
		if (getRowLength(x,y,1,-1,player)>=length) {
			return true;
		}
		if (getRowLength(x,y,0,1,player)>=length) {
			return true;
		}
		return false;
	}
	
	private static int getRowLength(int x, int y, int dx, int dy, int player) {
		int length = 1;
		int i=1;
		while (getSpot(x+dx*i,y+dy*i)==player) {
			length++;
			i++;
		}
		i=-1;
		while (getSpot(x+dx*i,y+dy*i)==player) {
			length++;
			i--;
		}
		return length;
	}
	
	private static int getSpot(int x, int y) {
		if (x>=board.length||x<0||y<0||y>=board[0].length) {
			return -1;
		} else {
			return board[x][y];
		}
	}
	
	public static boolean checkViableFour(int player, int x, int y) {
		if (x>=board.length||x<0||y<0||y>=board[0].length) {
			return false;
		}
		if (getRowLengthWithSpaces(x,y,1,0,player)>=4) {
			return true;
		}
		if (getRowLengthWithSpaces(x,y,1,1,player)>=4) {
			return true;
		}
		if (getRowLengthWithSpaces(x,y,1,-1,player)>=4) {
			return true;
		}
		if (getRowLengthWithSpaces(x,y,0,1,player)>=4) {
			return true;
		}
		return false;
	}
	
	private static int getRowLengthWithSpaces(int x, int y, int dx, int dy, int player) {
		int playerLength = 1;
		int spaceLength=0;
		int i=1;
		if (getSpot(x+dx,y+dy)==player||getSpot(x-dx,y-dy)==player) {
			while (getSpot(x+dx*i,y+dy*i)==player||getSpot(x+dx*i,y+dy*i)==0) {
				if (getSpot(x+dx*i,y+dy*i)==player) {
					playerLength++;
				} else {
					spaceLength++;
				}
				i++;
			}
			i=-1;
			while (getSpot(x+dx*i,y+dy*i)==player||getSpot(x+dx*i,y+dy*i)==0) {
				if (getSpot(x+dx*i,y+dy*i)==player) {
					playerLength++;
				} else {
					spaceLength++;
				}
				i--;
			}
		}
		if (playerLength>=3) {
			return playerLength+spaceLength;
		} else {
			return 0;
		}
	}
}
