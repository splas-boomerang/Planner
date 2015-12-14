import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class Graphical_sample extends JFrame{
	  public static void main(String args[]){
		  Graphical_sample frame = new Graphical_sample("タイトル");
	    frame.setVisible(true);
	  }
	  
	  HashMap<String,JLabel> blocks = new HashMap<String,JLabel>();

	  Graphical_sample(String title){
	    setTitle(title);
	    setBounds(0, 0, 800, 608);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    JPanel p = new JPanel();
	    p.setLayout(null);

	    SetBlock(GenerateLabel("block_a.png",448, 64, 128, 128),p);
	    SetBlock(GenerateLabel("block_b.png",64, 224, 128, 128),p);
	    SetBlock(GenerateLabel("block_c.png",64, 96, 128, 128),p);

	    JLabel arm = GenerateLabel("arm.png",438, 0, 148, 128);
	    p.add(arm);
	    
	    JLabel floor = GenerateLabel("floor.png",0, 480, 640, 128);
	    p.add(floor);
	    
	    Container contentPane = getContentPane();
	    contentPane.add(p, BorderLayout.CENTER);
	  }
	  
	  //arm
	  //448, 64
	  
	  //plain
	  //64, 352
	  //256, 352
	  //448, 352
	  
	  //exist
	  //64, 224
	  //64, 96
	  //256, 224
	  
	  private JLabel GenerateLabel(String imgName, int x, int y, int witdh, int height)
	  {
		    ImageIcon icon = new ImageIcon("./img/"+imgName);
		    JLabel label = new JLabel(icon);
		    label.setBounds(x, y, witdh, height);
		    return label;
	  }
	  
	  private void SetBlock(JLabel label,JPanel p)
	  {
		    blocks.put(label.getIcon().toString(), label);

		    // リスナーを登録
		    MyMouseListener listener = new MyMouseListener(label.getIcon().toString());
		    label.addMouseListener(listener);
		    label.addMouseMotionListener(listener);
		    
		    p.add(label);
	  }
	  
	  private class MyMouseListener extends MouseAdapter
	  {
	    private int dx;
	    private int dy;
	    private String key;
	    
	    public MyMouseListener(String key){
	    	this.key = key;
	    }

	    public void mouseDragged(MouseEvent e) {
	      // マウスの座標からラベルの左上の座標を取得する
	      int x = e.getXOnScreen() - dx;
	      int y = e.getYOnScreen() - dy;
	      blocks.get(key).setLocation(x, y);
	    }

	    public void mousePressed(MouseEvent e) {
	      // 押さえたところからラベルの左上の差を取っておく
	      dx = e.getXOnScreen() - blocks.get(key).getX();
	      dy = e.getYOnScreen() - blocks.get(key).getY();
	    }
	  }
	}