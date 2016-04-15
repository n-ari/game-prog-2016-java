package gameprog;

import java.awt.*;
import javax.swing.*;

import java.io.File;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args){
        (new Main()).run(); // non-static
    }
    public JFrame fr;
    public BufferedImage buf;
    public Image dman;
    public void run(){
    	buf = new BufferedImage(800,600,BufferedImage.TYPE_INT_ARGB);
    	
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
        
        try{
            // このtryの中で画像を読み込む
            dman = ImageIO.read(new File("src/d3.png"));
        }catch(Exception e){
            e.printStackTrace();
        }

        // 無限ループ
        while(true){
            long beg = System.nanoTime();
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
    public int x = 0;
    public void move(){
        Graphics2D g2 = (Graphics2D)buf.getGraphics();
        int w = dman.getWidth(fr);
        int h = dman.getHeight(fr);
        g2.drawImage(dman,x,0,w,h,fr);
        x += 3;
    }
}