package gameprog;

import java.awt.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args){
        (new Main()).run(); // non-static
    }
    public JFrame fr;
    public void run(){
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

        // 無限ループ
        while(true){
            long beg = System.nanoTime();
            move();
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
        Graphics2D g2 = (Graphics2D)fr.getContentPane().getGraphics();
        g2.setColor(Color.white);
        g2.fillRect(0,0,800,600);
        g2.setColor(Color.black);
        g2.fillRect(x,100,100,100);
        x += 3;
    }
}