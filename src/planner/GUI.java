package planner;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class GUI extends JFrame {
	private JPanel contentPane;
	GraphicalPlanner startArrange;
	GraphicalPlanner progressArrange;
	GraphicalPlanner goalArrange;
	int index = 0;
	ArrayList<ArrayList<String>> progressStates;
	ArrayList<String> ProgressResult;
	final JTextArea area;
	final ArrayList<String> list2 = new ArrayList<String>();
	private JLabel[] runLabels;
	JLabel lblCount;
	JLabel frame;
	JPanel uiPane;
	JLayeredPane layerPane;


	public static void main(String[] args)
	{
		GUI frame = new GUI();
		frame.setVisible(true);
	}

	public JLabel GenerateLabel(String imgName, int x, int y)
	{
		ImageIcon icon = new ImageIcon("./img/" + imgName);
		MediaTracker tracker = new MediaTracker(this);
		Image smallImg = icon.getImage().getScaledInstance(
				(int) (icon.getIconWidth() * 0.5), -1,Image.SCALE_SMOOTH);
		tracker.addImage(smallImg, 1);
		ImageIcon smallIcon = new ImageIcon(smallImg);
		JLabel label = new JLabel(smallIcon);
		label.setBounds((int) (x * 0.5), (int) (y * 0.5)
				, smallIcon.getIconWidth(), smallIcon.getIconHeight());
		return label;
	}

	public GUI()
	{
		setTitle("Planner");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//ウィンドウのタイトルバーと枠を考慮
		getContentPane().setPreferredSize(
				new Dimension(960,540));
		pack();
		//setBounds(100, 100, 1024, 768);


		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		//contentPane.setBackground(Color.gray);
		contentPane.setLayout(null);
		setContentPane(contentPane);


		float graphical_scale = 0.3f;

		//背景ポーン
		JPanel backPane = new JPanel();
		backPane.setBounds(0, 0, 960, 540);
		backPane.setOpaque(false);
		backPane.setLayout(null);
		contentPane.add(backPane,0);

		//配置設定ポーン
		JPanel graphicalSettingPane = new JPanel();
		graphicalSettingPane.setBounds(0, 252, 960, 288);
		graphicalSettingPane.setLayout(null);
		contentPane.add(graphicalSettingPane,0);
		//黒帯
		JPanel black = new JPanel();
		black.setBounds(0, 525, 960, 15);
		black.setBackground(Color.black);
		black.setLayout(null);
		contentPane.add(black,0);

		//UIポーン
		uiPane = new JPanel();
		uiPane.setBounds(0, 0, 960, 540);
		uiPane.setOpaque(false);
		uiPane.setLayout(null);
		contentPane.add(uiPane,0);

		//初期配置
		JLabel lblStart = new JLabel("Start");
		lblStart.setBounds(50, 400, 100, 25);
		graphicalSettingPane.add(lblStart);

		startArrange = new GraphicalPlanner(graphical_scale,true);
		startArrange.setBounds(0, 0, startArrange.getWidth(),startArrange.getHeight());
		graphicalSettingPane.add(startArrange);

		//完了配置
		JLabel lblFinish = new JLabel("Finish");
		lblFinish.setBounds(800, 400,100, 25);

		graphicalSettingPane.add(lblFinish);

		goalArrange = new GraphicalPlanner(graphical_scale,true);
		goalArrange.setBounds(576, 0, goalArrange.getWidth(),goalArrange.getHeight());
		graphicalSettingPane.add(goalArrange);


		//実行ボタン
		runLabels = new JLabel[2];
		for(int i=0;i<2;++i){
			runLabels[i] = GenerateLabel("run_"+i+".png", 800, 700);
			uiPane.add(runLabels[i]);
		}
		runLabels[0].addMouseListener(new myListener());
		runLabels[1].setVisible(false);
		runLabels[1].setOpaque(false);

		//途中配置
		progressArrange = new GraphicalPlanner(0.25f,false);
		progressArrange.setBounds(345, 30, progressArrange.getWidth(),progressArrange.getHeight());
		uiPane.add(progressArrange);

		frame = GenerateLabel("frame.png", 590, 10);
		uiPane.add(frame);

		//		JLabel lblProcess = new JLabel("Process");
		//		lblProcess.setBounds(200, 30, 61, 15);
		//		progressArrange.add(lblProcess);

		//途中経過テキストポーン
		JPanel uiProgressPane = new JPanel();
		uiProgressPane.setBounds(650, 10, 200, 232);
		//		uiProgressPane.setOpaque(false);
		uiProgressPane.setBackground(Color.white);
		uiProgressPane.setLayout(null);
		uiPane.add(uiProgressPane,0);

		//スクロール表示
		JScrollPane scrollPane_2 = new JScrollPane();

		scrollPane_2.setBounds(10, 25, 180, 180);
		uiProgressPane.add(scrollPane_2);
		area = new JTextArea();
		scrollPane_2.setViewportView(area);

		lblCount = new JLabel("実行前");
		lblCount.setBounds(10, 5, 61, 15);
		uiProgressPane.add(lblCount);

		JButton btnBack = new JButton("Back");
		btnBack.setBounds(10, 205, 80, 25);
		btnBack.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						if(progressStates == null){return;}
						index = Math.max(--index,0);
						DisplayState();
					}
				});
		uiProgressPane.add(btnBack);

		JButton btnGo = new JButton("Go");
		btnGo.setBounds(110, 205, 80, 25);
		btnGo.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						if(progressStates == null){return;}
						index = Math.min(++index,progressStates.size()-1);
						DisplayState();
					}
				});
		uiProgressPane.add(btnGo);





		final JLabel label = new JLabel();

		JPanel labelPanel = new JPanel();
		labelPanel.add(label);



		/*final JTextField text = new JTextField(10);
		text.setBounds(60,130,120,30);
		contentPane.add(text);
		JButton btnFcreate = new JButton("file create");
		btnFcreate.setBounds(60,180,120,30);
		btnFcreate.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						try{
							File fl = new File("./file/"+text.getText()+".txt");
							  fl.createNewFile();
							}catch(IOException er){
							  System.out.println("例外が発生しました");
							}

					}});
		contentPane.add(btnFcreate);*/
		JButton btnfile = new JButton("data open");

		btnfile.setBounds(60,70,150,50);

		btnfile.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						//JFileChooser filechooser = new JFileChooser();
						//int selected = filechooser.showOpenDialog(label);
						//if (selected == JFileChooser.APPROVE_OPTION){
							//final File file = filechooser.getSelectedFile();
						 File file = new File("./file/start.txt");
						 File file2 = new File("./file/finish.txt");
							try{
								FileReader filereader = new FileReader(file);
								FileReader filereader2 = new FileReader(file2);
								ArrayList<String> list = new ArrayList<String>();
								ArrayList<String> list2 = new ArrayList<String>();
							      if (checkBeforeReadfile(file)&&checkBeforeReadfile(file2)){
							        BufferedReader br = new BufferedReader(new FileReader(file));
							        BufferedReader br2 = new BufferedReader(new FileReader(file2));
							        String str,str2;
							        while((str = br.readLine()) != null){
							          list.add(str);
							        }
							        while((str2 = br2.readLine()) != null){
								          list2.add(str2);
								        }
							        startArrange.SetBlockArrangement(new ArrayList<String>(list));
									goalArrange.SetBlockArrangement(new ArrayList<String>(list2));
							        br.close();
							        br2.close();
							        filereader.close();
							      }else{
							        System.out.println("ファイルが見つからないか開けません");
							      }
							    }catch(FileNotFoundException err){
							      System.out.println(err);
							    }catch(IOException er){
							      System.out.println(er);
							    }


					}
				});
		contentPane.add(btnfile);
		contentPane.add(labelPanel);

		JButton btnsave = new JButton("data save");

		btnsave.setBounds(60,150,150,50);

		btnsave.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						//JFileChooser filechooser = new JFileChooser();
						//int selected = filechooser.showSaveDialog(label);
						//if (selected == JFileChooser.APPROVE_OPTION){
							//final File file = filechooser.getSelectedFile();
							 File file = new File("./file/start.txt");
							 File file2 = new File("./file/finish.txt");
							try{

								if (checkBeforeWritefile(file)&&checkBeforeWritefile(file2)){
									PrintWriter filewriter = new PrintWriter(file);
									PrintWriter filewriter2 = new PrintWriter(file2);
									ArrayList<String> str2 = progressStates.get(0);
									//filewriter.write("初期状態"+"\r\n");
									for(String str3 : str2)
									filewriter.print(str3+"\r\n");
									//filewriter.write("終了状態"+"\r\n");
									ArrayList<String> str4 = progressStates.get(progressStates.size() - 1);
									for(String str5 : str4)
									filewriter2.write(str5+"\r\n");
									filewriter.close();
									filewriter2.close();
								}else{
									System.out.println("ファイルに書き込めません");
								}
							}catch(IOException error){
								System.out.println(error);
							}
						}
				});
		contentPane.add(btnsave);

		JButton btnRandom = new JButton("random");
		btnRandom.setBounds(60,10,150,50);
		btnRandom.addActionListener(
				new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Random();
					startArrange.SetBlockArrangement(new ArrayList<String>(list2));
					Random();
					goalArrange.SetBlockArrangement(new ArrayList<String>(list2));

					}
				});
		contentPane.add(btnRandom);




		//		ImageIcon icon1 = new ImageIcon("./img/ya.png");
		//		JLabel label1 = new JLabel(icon1);
		//		label1.setBounds(300, 450, 250, 250);
		//		label1.addMouseListener(new MouseListener()
		//		{
		//			public void mouseClicked(MouseEvent arg0) {
		//				area.setText("");
		//				index = 0;
		//				Planner planner = new Planner();
		//				ArrayList<String> initialState = startArrange.getCurrentState();
		//				ArrayList<String> goalList = goalArrange.getCurrentState();
		//				planner.start(goalList,initialState);
		//				progressStates = planner.ProgressStates;
		//				ProgressResult = planner.ProgressResult;
		//				lblCount.setText("計" + (progressStates.size()-1) + "回\n");
		//				DisplayState();
		//			}
		//			public void mouseEntered(MouseEvent arg0) {
		//			}
		//			public void mouseExited(MouseEvent arg0) {
		//			}
		//			public void mousePressed(MouseEvent arg0) {
		//			}
		//			public void mouseReleased(MouseEvent arg0) {
		//			}
		//		});
		//
		//		contentPane.add(label1);

	}
	private void DisplayState()
	{
		if(index == 0){
			area.setText("初期状態\n");
		}else if(index == progressStates.size() - 1){
			area.setText("終了状態\n");
		}else{
			area.setText("移動"+index+"回目\n");
		}
		if(ProgressResult.size()>0 && index > 0){
			area.append(ProgressResult.get(index-1) + "\n");
		}
		area.append(" --- \n");
		ArrayList<String> states = progressStates.get(index);
		for(String str : states){
			area.append(str + "\n");
		}
		progressArrange.SetBlockArrangement(new ArrayList<String>(states));
		uiPane.add(frame,0);
		uiPane.repaint();
	}
	private class myListener extends MouseAdapter{
		//ドラッグ開始時の処理
		public void mousePressed(MouseEvent e)
		{
			runLabels[0].setVisible(false);
			runLabels[1].setVisible(true);
		}
		//ドラッグ終了時の処理
		public void mouseReleased(MouseEvent e)
		{
			runLabels[0].setVisible(true);
			runLabels[1].setVisible(false);
		}
		public void mouseClicked(MouseEvent e){
			area.setText("");
			index = 0;
			Planner planner = new Planner();
			ArrayList<String> initialState = startArrange.getCurrentState();
			ArrayList<String> goalList = goalArrange.getCurrentState();
			if(planner.start(goalList,initialState)){
			}else{
				area.setText("ブロック数が一致しません");
				progressStates = null;
				ProgressResult = null;
				lblCount.setText("エラーが発生しました");
				return;
			}
			progressStates = planner.ProgressStates;
			ProgressResult = planner.ProgressResult;
			lblCount.setText("計" + (progressStates.size()-1) + "回\n");

			DisplayState();
		}
	}
	private static boolean checkBeforeWritefile(File file){
		if (file.exists()){
			if (file.isFile() && file.canWrite()){
				return true;
			}
		}

		return false;
	}
	private static boolean checkBeforeReadfile(File file){
	    if (file.exists()){
	      if (file.isFile() && file.canRead()){
	        return true;
	      }
	    }

	    return false;
	  }

	/**
	 * 
	 */
	/**
	 * 
	 */
	/**
	 * 
	 */
	public void Random(){
		ArrayList<String> list = new ArrayList<String>();
		list.clear();
		list2.clear();
		list.add("ontable A");
		list.add("ontable B");
		list.add("ontable C");
		Collections.shuffle(list);
		String first = list.get(0);
		list2.add(first);
		if(first.equals("ontable A")){
			list.clear();
			list.add("clear A");
			list.add("B on A");
			list.add("C on A");
			Collections.shuffle(list);
			list2.add(list.get(0));
			if((list.get(0)).equals("clear A")){
				list.clear();
				list.add("ontable B");
				list.add("ontable C");
				Collections.shuffle(list);
				list2.add((list.get(0)));
				if((list.get(0)).equals("ontable B")){
					list.clear();
					list.add("C on B");
					list.add("clear B");
					Collections.shuffle(list);
					list2.add((list.get(0)));
					if((list.get(0)).equals("clear B")){
						list.clear();
						list.add("ontable C");
						list.add("handEmpty");
						Collections.shuffle(list);
						list2.add((list.get(0)));
						if((list.get(0)).equals("ontable C")){
							list2.add("clear C");
							list2.add("handEmpty");

						}
					}
					else if((list.get(0)).equals("C on B")){
						list2.add("clear C");
						list2.add("handEmpty");
					}
				}
				else if((list.get(0)).equals("ontable C")){
					list.clear();
					list.add("B on C");
					list.add("clear C");
					Collections.shuffle(list);
					list2.add((list.get(0)));
					if((list.get(0)).equals("clear C")){
						list.clear();
						list.add("ontable B");
						list.add("handEmpty");
						Collections.shuffle(list);
						list2.add((list.get(0)));
						if((list.get(0)).equals("ontable B")){
							list2.add("clear B");
							list2.add("handEmpty");
						}
					}
					else if((list.get(0)).equals("B on C")){
						list2.add("clear B");
						list2.add("handEmpty");
					}
				}
			}
			else if((list.get(0)).equals("B on A")){
				list.clear();
				list.add("clear B");
				list.add("C on B");
				Collections.shuffle(list);
				list2.add(list.get(0));
				if(list.get(0).equals("clear B")){
					list.clear();
					list.add("ontable C");
					list.add("handEmpty");
					Collections.shuffle(list);
					list2.add(list.get(0));
					if((list.get(0)).equals("ontable C")){
						list2.add("clear C");
						list2.add("handEmpty");
					}

				}
				else if(list.get(0).equals("C on B")){
					list2.add("clear C");
					list2.add("handEmpty");
				}
			}
			else if((list.get(0)).equals("C on A")){
				list.clear();
				list.add("clear C");
				list.add("B on C");
				Collections.shuffle(list);
				list2.add(list.get(0));
				if(list.get(0).equals("clear C")){
					list.clear();
					list.add("ontable B");
					list.add("handEmpty");
					Collections.shuffle(list);
					list2.add(list.get(0));
					if((list.get(0)).equals("ontable B")){
						list2.add("clear B");
						list2.add("handEmpty");
					}

				}
				else if(list.get(0).equals("B on C")){
					list2.add("clear B");
					list2.add("handEmpty");
				}
			}

			}
		else if(first.equals("ontable B")){
			list.clear();
			list.add("clear B");
			list.add("C on B");
			list.add("A on B");
			Collections.shuffle(list);
			list2.add(list.get(0));
			if((list.get(0)).equals("clear B")){
				list.clear();
				list.add("ontable C");
				list.add("ontable A");
				Collections.shuffle(list);
				list2.add((list.get(0)));
				if((list.get(0)).equals("ontable C")){
					list.clear();
					list.add("A on C");
					list.add("clear C");
					Collections.shuffle(list);
					list2.add((list.get(0)));
					if((list.get(0)).equals("clear C")){
						list.clear();
						list.add("ontable A");
						list.add("handEmpty");
						Collections.shuffle(list);
						list2.add((list.get(0)));
						if((list.get(0)).equals("ontable A")){
							list2.add("clear A");
							list2.add("handEmpty");
						}
					}
					else if((list.get(0)).equals("A on C")){
						list2.add("clear A");
						list2.add("handEmpty");
					}
				}
				else if((list.get(0)).equals("ontable A")){
					list.clear();
					list.add("C on A");
					list.add("clear A");
					Collections.shuffle(list);
					list2.add((list.get(0)));
					if((list.get(0)).equals("clear A")){
						list.clear();
						list.add("ontable C");
						list.add("handEmpty");
						Collections.shuffle(list);
						list2.add((list.get(0)));
						if((list.get(0)).equals("ontable C")){
							list2.add("clear C");
							list2.add("handEmpty");
						}
					}
					else if((list.get(0)).equals("C on A")){
						list2.add("clear C");
						list2.add("handEmpty");
					}
				}
			}
			else if((list.get(0)).equals("C on B")){
				list.clear();
				list.add("clear C");
				list.add("A on C");
				Collections.shuffle(list);
				list2.add(list.get(0));
				if(list.get(0).equals("clear C")){
					list.clear();
					list.add("ontable A");
					list.add("handEmpty");
					Collections.shuffle(list);
					list2.add(list.get(0));
					if((list.get(0)).equals("ontable A")){
						list2.add("clear A");
						list2.add("handEmpty");
					}

				}
				else if(list.get(0).equals("A on C")){
					list2.add("clear A");
					list2.add("handEmpty");
				}
			}
			else if((list.get(0)).equals("A on B")){
				list.clear();
				list.add("clear A");
				list.add("C on A");
				Collections.shuffle(list);
				list2.add(list.get(0));
				if(list.get(0).equals("clear A")){
					list.clear();
					list.add("ontable C");
					list.add("handEmpty");
					Collections.shuffle(list);
					list2.add(list.get(0));
					if((list.get(0)).equals("ontable C")){
						list2.add("clear C");
						list2.add("handEmpty");
					}

				}
				else if(list.get(0).equals("C on A")){
					list2.add("clear C");
					list2.add("handEmpty");
				}
			}

			}//unnza
		else if(first.equals("ontable C")){
			list.clear();
			list.add("clear C");
			list.add("A on C");
			list.add("B on C");
			Collections.shuffle(list);
			list2.add(list.get(0));
			if((list.get(0)).equals("clear C")){
				list.clear();
				list.add("ontable A");
				list.add("ontable B");
				Collections.shuffle(list);
				list2.add((list.get(0)));
				if((list.get(0)).equals("ontable A")){
					list.clear();
					list.add("B on A");
					list.add("clear A");
					Collections.shuffle(list);
					list2.add((list.get(0)));
					if((list.get(0)).equals("clear A")){
						list.clear();
						list.add("ontable B");
						list.add("handEmpty");
						Collections.shuffle(list);
						list2.add((list.get(0)));
						if((list.get(0)).equals("ontable B")){
							list2.add("clear B");
							list2.add("handEmpty");
						}
					}
					else if((list.get(0)).equals("B on A")){
						list2.add("clear B");
						list2.add("handEmpty");
					}
				}
				else if((list.get(0)).equals("ontable B")){
					list.clear();
					list.add("A on B");
					list.add("clear B");
					Collections.shuffle(list);
					list2.add((list.get(0)));
					if((list.get(0)).equals("clear B")){
						list.clear();
						list.add("ontable A");
						list.add("handEmpty");
						Collections.shuffle(list);
						list2.add((list.get(0)));
						if((list.get(0)).equals("ontable A")){
							list2.add("clear A");
							list2.add("handEmpty");
						}
					}
					else if((list.get(0)).equals("A on B")){
						list2.add("clear A");
						list2.add("handEmpty");
					}
				}
			}
			else if((list.get(0)).equals("A on C")){
				list.clear();
				list.add("clear A");
				list.add("B on A");
				Collections.shuffle(list);
				list2.add(list.get(0));
				if(list.get(0).equals("clear A")){
					list.clear();
					list.add("ontable B");
					list.add("handEmpty");
					Collections.shuffle(list);
					list2.add(list.get(0));
					if((list.get(0)).equals("ontable B")){
						list2.add("clear B");
						list2.add("handEmpty");
					}

				}
				else if(list.get(0).equals("B on A")){
					list2.add("clear B");
					list2.add("handEmpty");
				}
			}
			else if((list.get(0)).equals("B on C")){
				list.clear();
				list.add("clear B");
				list.add("A on B");
				Collections.shuffle(list);
				list2.add(list.get(0));
				if(list.get(0).equals("clear B")){
					list.clear();
					list.add("ontable A");
					list.add("handEmpty");
					Collections.shuffle(list);
					list2.add(list.get(0));
					if((list.get(0)).equals("ontable A")){
						list2.add("clear A");
						list2.add("handEmpty");
					}

				}
				else if(list.get(0).equals("A on B")){
					list2.add("clear A");
					list2.add("handEmpty");
				}
			}

			}
	}
}
