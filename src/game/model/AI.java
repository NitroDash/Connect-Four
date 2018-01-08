package game.model;

import game.state.PlayState;

import java.util.Random;

public class AI {
	private int difficulty,player;
	private static final double[] playChances = {1.0,0.95,0.85,0.7,0.5,0.3,0.2};
	
	public AI(int difficulty) {
		this.difficulty=difficulty;
		this.player=PlayState.AIplayer;
	}
	
	public int selectMove() {
		int[] priorities = new int[PlayState.board.length];
		int highestPriority=-1;
		for (int x = 0; x < priorities.length; x++) {
			int y=-1;
			for (int i=PlayState.board[0].length-1;i>=0; i--) {
				if (PlayState.board[x][i]==0) {
					y=i;
					break;
				}
			}
			if (y==-1) {
				priorities[x]=-1;
			} else {
				if (PlayState.checkForRow(player,4,x,y)) {
					priorities[x]=7;
				} else if (PlayState.checkForRow(3-player,4,x,y)) {
					priorities[x]=6;
				} else if (PlayState.checkForRow(3-player,4,x,y-1)){
					priorities[x]=0;
				} else if (PlayState.checkForRow(player,4,x,y-1)&&PlayState.checkForRow(player,4,x,y-2)) {
					priorities[x]=5;
				} else if (PlayState.checkForRow(player,4,x,y-1)) {
					priorities[x]=1;
				} else if (PlayState.checkViableFour(player,x,y)){
					priorities[x]=3;
				} else if (PlayState.checkViableFour(3-player,x,y)){
					priorities[x]=4;
				} else {
					priorities[x]=2;
				}
			}
			if (priorities[x]>highestPriority) {
				highestPriority=priorities[x];
			}
		}
		int options=0;
		for (int i = 0; i < priorities.length; i++) {
			if (priorities[i]==highestPriority) {
				options++;
			}
		}
		int[] legalMoves=new int[options];
		options=0;
		for (int i = 0; i < priorities.length; i++) {
			if (priorities[i]==highestPriority) {
				legalMoves[options]=i;
				options++;
			}
		}
		double probSum=0;
		for (int i = 0;i<legalMoves.length; i++) {
			probSum+=playChances[Math.abs(PlayState.droppedX-legalMoves[i])];
		}
		double choice=PlayState.random.nextDouble()*probSum;
		for (int i = 0; i < legalMoves.length; i++) {
			choice-=playChances[Math.abs(PlayState.droppedX-legalMoves[i])];
			if (choice<=0) {
				return legalMoves[i];
			}
		}
		return 0;
	}
}
