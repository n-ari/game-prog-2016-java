package gameprog;

import java.awt.*;
import javax.swing.*;

import java.io.File;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.awt.event.*;

public class Main {
    public static void main(String[] args){
        (new Main()).run(); // non-static
    }
    public JFrame fr;
    public BufferedImage buf;
    public boolean[] keybef, keynow, keynext;
    public Image dman;
    public void run(){
    	buf = new BufferedImage(800,600,BufferedImage.TYPE_INT_ARGB);

        keybef = new boolean[256];
        keynow = new boolean[256];
        keynext = new boolean[256];
        for(int i=0;i<256;++i)
            keybef[i] = keynow[i] = keynext[i] = false;
    	
        // ウィンドウ生成
        fr = new JFrame("タイトル");
        // 閉じるボタンの挙動設定
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // ウィンドウサイズ変更不可に
        fr.setResizable(false);
        // ウィンドウの中身のサイズを調節 ここでは横800 x 縦600
        fr.getContentPane().setPreferredSize(new Dimension(800, 600));
        // 表示
        fr.setVisible(true);
        // サイズ調整
        fr.pack();
        // キーリスナー登録
        fr.addKeyListener(new keyclass());
        
        try{
            // このtryの中で画像を読み込む
            dman = ImageIO.read(new File("src/d3.png"));
        }catch(Exception e){
            e.printStackTrace();
        }

        // 無限ループ
        while(true){
            long beg = System.nanoTime();
            for(int i=0;i<256;++i){
                keybef[i] = keynow[i];
                keynow[i] = keynext[i];
            }
            Graphics2D g2 = (Graphics2D)buf.getGraphics();
            g2.setColor(Color.white);
            g2.fillRect(0, 0, 800, 600);
            move();
            g2 = (Graphics2D)fr.getContentPane().getGraphics();
            g2.drawImage(buf, 0, 0, 800, 600, fr);
            // 60FPS用
            long range = System.nanoTime() - beg;
            long sleeptime = (16666666L - range)/1000000L;
            if(sleeptime < 0) sleeptime = 0;
            try{
                Thread.sleep(sleeptime);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public int x = 400,y = 300;
    public int time = 0;
    public void move(){
        Graphics2D g2 = (Graphics2D)buf.getGraphics();
        int v = 6;
        int w = dman.getWidth(fr)/4;
        int h = dman.getHeight(fr)/4;
        if(isPressed(KeyEvent.VK_RIGHT)){
            x += v;
        }
        if(isPressed(KeyEvent.VK_LEFT)){
            x -= v;
        }
        if(isPressed(KeyEvent.VK_DOWN)){
            y += v;
        }
        if(isPressed(KeyEvent.VK_UP)){
            y -= v;
        }
        // 画面外に出さない
        if(x>800-w) x = 800-w;
        if(x<0) x = 0;
        if(y>600-h) y = 600-h;
        if(y<0) y = 0;
        g2.drawImage(dman,x,y,w,h,fr);

        time ++;
        float sec = (float)time/60f;
        g2.setFont(new Font(Font.SERIF, Font.PLAIN, 24));
        g2.setColor(Color.black);
        g2.drawString(String.format("%.4f秒",sec),0,25);
    }
    public boolean isPressed(int key){
        return keynow[key];
    }
    public boolean onPressed(int key){
        return !keybef[key] && keynow[key];
    }
    public class keyclass implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {}
        @Override
        public void keyPressed(KeyEvent e) {
            keynext[e.getKeyCode()] = true;
        }
        @Override
        public void keyReleased(KeyEvent e) {
            keynext[e.getKeyCode()] = false;
        }
    }
}