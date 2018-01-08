package game.state;

import game.main.GameMain;
import game.main.Resources;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MenuState extends State {

	@Override
	public void init() {
			
	}

	@Override
	public void update(float delta) {
		// Do Nothing
		
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.CYAN);
		g.fillRect(0, 0,GameMain.GAME_WIDTH,GameMain.GAME_HEIGHT);
		g.setColor(Color.BLACK);
		g.fillRect(0,200,700,20);
		g.fillRect(0,350,700,20);
		g.setFont(new Font("Times New Roman",0,100));
		g.drawString("Connect Four", 70, 130);
		g.setFont(new Font("Times New Roman",0,70));
		g.drawString("vs. Player",200,300);
		g.drawString("vs. Computer",160,450);
	}

	@Override
	public void onClick(MouseEvent e) {
		if (e.getY()<360) {
			setCurrentState(new PlayState(0));
		} else {
			setCurrentState(new PlayState(1));
		}
	}

	@Override
	public void onKeyPress(KeyEvent e) {
		
	}

	@Override
	public void onKeyRelease(KeyEvent e) {
		
	}

}
