package gameprog;

import java.awt.*;
import javax.swing.*;

import java.io.File;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.awt.event.*;

import java.util.ArrayList;

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
    public ArrayList<Float> bxList, byList, bvxList, bvyList;
    public int br = 5;
    public int time = 0;
    // state 0:Title 1:Game 2:Result
    public int state = 0;
    public void move(){
        Graphics2D g2 = (Graphics2D)buf.getGraphics();
        if(state==0){
            g2.setColor(Color.black);
            g2.setFont(new Font(Font.SERIF, Font.PLAIN, 48));
            g2.drawString("ゲーム入門制作",30,100);

            g2.setFont(new Font(Font.SERIF, Font.PLAIN, 24));
            g2.drawString("Zキーを押してスタート",400,400);

            if(onPressed(KeyEvent.VK_Z)){
                state = 1;
                // 初期化
                x = 400;
                y = 300;
                bxList = new ArrayList<>();
                byList = new ArrayList<>();
                bvxList = new ArrayList<>();
                bvyList = new ArrayList<>();
                time = 0;
            }
        }else if(state == 1){
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
	        
	        if(time%60==0){
                // [0,1) の範囲の乱数を2π倍して [0,2π) の範囲の乱数にする
                float angle = (float)Math.random() * (float)Math.PI * 2f;
                // [0,1) の範囲を4倍して3足すことで [3,7) の範囲の乱数にする
                float len = (float)Math.random() * 4f + 3f;
                float vx = (float)Math.cos(angle) * len;
                float vy = (float)Math.sin(angle) * len;
                bxList.add(400f);
                byList.add(0f);
                bvxList.add(vx);
                bvyList.add(vy);
            }

            for(int i=0;i<bxList.size();i++){
                float bx,by,bvx,bvy;
                bx = bxList.get(i);
                by = byList.get(i);
                bvx = bvxList.get(i);
                bvy = bvyList.get(i);
                bx += bvx;
                by += bvy;
                // 完全に画面外に出たらループさせる
                if(bx <= 0-br){
                    bx = 800+br;
                }else if(bx >= 800+br){
                    bx = 0-br;
                }
                if(by <= 0-br){
                    by = 600+br;
                }else if(by >= 600+br){
                    by = 0-br;
                }
                bxList.set(i,bx);
                byList.set(i,by);
                g2.setColor(Color.blue);
                g2.fillOval((int)(bx-br),(int)(by-br),2*br,2*br);
            }
            
	        time ++;
	        float sec = (float)time/60f;
	        g2.setFont(new Font(Font.SERIF, Font.PLAIN, 24));
	        g2.setColor(Color.black);
	        g2.drawString(String.format("%.4f秒",sec),0,25);

            // 当たったらゲームオーバー
            // D言語くんの中心座標・当たり判定半径
            int cx = x + w/2;
            int cy = y + h/2;
            int cr = 10;
            // check
            for(int i=0;i<bxList.size();++i){
                float bx,by;
                bx = bxList.get(i);
                by = byList.get(i);
                float dx = (cx - bx);
                float dy = (cy - by);
                float dr = (cr + br);
                if(dx*dx + dy*dy <= dr*dr){
                    // ゲームオーバーに移行
                    state = 2;
                    break;
                }
            }
        }else if(state == 2){
            g2.setColor(Color.black);
            g2.setFont(new Font(Font.SERIF, Font.PLAIN, 32));
            g2.drawString("ゲームオーバー",30,100);

            float sec = (float)time/60f;
            g2.setColor(Color.red);
            g2.setFont(new Font(Font.SERIF, Font.PLAIN, 48));
            g2.drawString(String.format("スコア : %.5f 秒",sec),100,200);

            g2.setColor(Color.black);
            g2.setFont(new Font(Font.SERIF, Font.PLAIN, 24));
            g2.drawString("Zキーでコンティニュー",400,500);

            if(onPressed(KeyEvent.VK_Z)){
                state = 0;
            }
        }
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