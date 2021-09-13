package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.Random;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable,KeyListener{
    
    public int PONTUAÇÃO = -1;
    String SCORE = "PONTUAÇÃO: " + PONTUAÇÃO;
    Font SCORE_FONT = new Font("Consolas", Font.BOLD, 18);
    FontMetrics SCORE_METRICA = this.getFontMetrics(SCORE_FONT);
    
    public Node[] nodeSnake = new Node[10];
    
    public boolean left, right, up, down;
    
    public int score = -1;
    
    public int spd = 0;
    
    public int macaX = 0, macaY = 0;
    
    public Game(){
        this.setPreferredSize(new Dimension(600, 600));
        for(int i = 0; i < nodeSnake.length; i++){
            nodeSnake[i] = new Node(0,0);
        }
        this.addKeyListener(this);
    }
    
    public void tick() {
        
        for(int i = nodeSnake.length - 1; i > 0; i--){
            nodeSnake[i].x = nodeSnake[i-1].x;
            nodeSnake[i].y = nodeSnake[i-1].y;
        }
        
        if(nodeSnake[0].x+10 < 0){
            nodeSnake[0].x = 600;
        }else if(nodeSnake[0].x >= 600){
            nodeSnake[0].x = -10;
        }
        
        if(nodeSnake[0].y+10 < 0){
            nodeSnake[0].y = 600;
        }else if(nodeSnake[0].y >= 600){
            nodeSnake[0].y = -10;
        }
        
        if(right){
            nodeSnake[0].x+=spd;
        }else if(up){
            nodeSnake[0].y-=spd;
        }else if(down){
            nodeSnake[0].y+=spd;
        }else if(left){
            nodeSnake[0].x-=spd;
        }
        
        if(new Rectangle(nodeSnake[0].x, nodeSnake[0].y, 10, 10).intersects(new Rectangle(macaX, macaY, 10, 10))){
            macaX = new Random().nextInt(600-10);
            macaY = new Random().nextInt(600-10);
            score++;
            spd++;
            PONTUAÇÃO++;
            System.out.println("Pontos: " + score);
        }
    }

    public void render() {
        
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 600, 600);
        for (Node nodeSnake1 : nodeSnake) {
            g.setColor(Color.BLUE);
            g.fillRect(nodeSnake1.x, nodeSnake1.y, 10, 10);
        }
        desenharPontuacao(g);
        
        g.setColor(Color.red);
        g.fillRect(macaX, macaY, 10, 10);
        
        g.dispose();
        bs.show();
    }
    
    public static void main(String[] args) {
        Game game = new Game();
        JFrame frame = new JFrame("Snake");
        frame.add(game);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        new Thread(game).start();
    }

    @Override
    public void run() {
        
        while(true){
            tick();
            render();
            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException ex) {
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT -> {
                right = true;
                down = false;
                left = false;
                up = false;
            }
            case KeyEvent.VK_LEFT -> {
                left = true;
                right = false;
                down = false;
                up = false;
            }
            case KeyEvent.VK_UP -> {
                up = true;
                right = false;
                left = false;
                down = false;
            }
            case KeyEvent.VK_DOWN -> {
                down = true;
                right = false;
                left = false;
                up = false;
            }
            default -> {
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void desenharPontuacao(Graphics g) {
        SCORE = "PONTUAÇÃO: " + PONTUAÇÃO;
        SCORE_METRICA = this.getFontMetrics(SCORE_FONT);
        g.setColor(Color.white);
        g.setFont(SCORE_FONT);
        g.drawString(SCORE, (600 - SCORE_METRICA.stringWidth(SCORE)) - 10, 600 - 10);
    }
}
