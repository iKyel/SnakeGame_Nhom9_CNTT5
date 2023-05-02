
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import java.awt.Cursor;
import java.io.*;
import javax.sound.sampled.*;

public class GameMenu extends JFrame implements ActionListener{
	
	JLabel title;
	JButton play, score, howPlay, mute;
	private Clip clip;		// Tạo 1 luồng âm thanh riêng
	private boolean isMuted = true; 		// Biến để theo dõi trạng thái của nút mute
	playSound audio = new playSound();
	
	public GameMenu() {
		this.setTitle("Snake Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		this.setSize(600, 600);
		this.setLocationRelativeTo(null);
		
		// Điều chỉnh nhãn title
		setTitle();
		this.add(title);
		
		// Điều chỉnh các button
		setButton();
		this.add(play);
		this.add(score);
		this.add(howPlay);
		this.add(mute);
		
		addMouseEvents();
		this.setVisible(true);
		
		try {
		    // Tải tệp âm thanh vào
		    File soundFile = new File("Sound/backgroundAudio.wav");
		    AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
		    
		    // Tạo Clip từ AudioInputStream
		    clip = AudioSystem.getClip();
		    clip.open(audioIn);
		        
		    // Phát âm thanh liên tục
		    clip.loop(Clip.LOOP_CONTINUOUSLY);
		} 
		catch (Exception e) {
			// Xử lý lỗi
		    System.out.println("Error: " + e.getMessage());
		}
	}
	
	// Chỉnh nhãn title
	public void setTitle() {
	    title = new JLabel("Snake Game");
	    title.setFont(new Font("Snap ITC", Font.BOLD, 70));
	    title.setBounds(0, 0, 600, 200);
	    title.setHorizontalAlignment(JLabel.CENTER);
	    title.setVerticalAlignment(JLabel.CENTER);
	}
	
	// Chỉnh các button
	public void setButton() {
		
		// Nút âm thanh
		mute = new JButton();
		mute.setText("Turn off sound");
		mute.setFocusable(false);
		mute.setBounds(440, 500, 140, 30);
		
		mute.addActionListener(this); 		//Thêm sự kiện cho nút tắt âm thanh
		
		// Tạo nút Start
		play = new JButton();
		play.setBounds(200, 180, 200, 80);
		play.setFocusable(false);
		play.setFont(new Font("Ink Free",Font.BOLD, 50));
		play.setText("Start");
		play.setFocusPainted(false); 			// Bỏ đường viền khi focus vào nút
		play.setCursor(new Cursor(Cursor.HAND_CURSOR)); 		// Đổi hình con trỏ chuột thành hình tay khi rê chuột qua nút
		
		// Tạo border ban đầu
		Border defaultBorder = BorderFactory.createLineBorder(new Color(51, 153, 255), 2);
		play.setBorder(defaultBorder);
		
		// Tạo border khi hover
		Border hoverBorder = BorderFactory.createLineBorder(Color.GREEN, 2);

		play.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseEntered(MouseEvent e) {
		        // Đổi phông chữ khi di chuột đến
		        Font font1 = new Font("Ink Free", Font.ITALIC, 50);
		        play.setFont(font1);
		        play.setForeground(Color.RED);
		        play.setBorder(hoverBorder);
		    }
		    @Override
		    public void mouseExited(MouseEvent e) {
		        // Trở lại phông chữ ban đầu khi di chuột ra khỏi nút
		        Font font = new Font("Ink Free", Font.BOLD, 50);
		        play.setFont(font);
		        play.setForeground(null);
		        play.setBorder(defaultBorder);
		    }
		});
		play.addActionListener(this);
		
		// Tạo nút Score
		score = new JButton();
		score.setBounds(200, 280, 200, 80);
		score.setFocusable(false);
		score.setFont(new Font("Ink Free",Font.BOLD, 55));
		score.setText("Score");
		score.setBorder(defaultBorder);
		score.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseEntered(MouseEvent e) {
		        // Đổi phông chữ khi di chuột đến
		        Font font = score.getFont();
		        score.setFont(font.deriveFont(font.getStyle() | Font.ITALIC));
		        score.setForeground(Color.RED);
		        score.setBorder(hoverBorder);
		    }
		    @Override
		    public void mouseExited(MouseEvent e) {
		        // Trở lại phông chữ ban đầu khi di chuột ra khỏi nút
		        Font font = score.getFont();
		        score.setFont(font.deriveFont(font.getStyle() & ~Font.ITALIC));
		        score.setForeground(null);
		        score.setBorder(defaultBorder);
		    }
		});
		score.addActionListener(this);
		
		// Tạo nút GamePlay
		howPlay = new JButton();
		howPlay.setBounds(200, 380, 200, 80);
		howPlay.setFocusable(false);
		howPlay.setFont(new Font("Ink Free",Font.BOLD, 55));
		howPlay.setText("Guide");
		howPlay.setBorder(defaultBorder);
		howPlay.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseEntered(MouseEvent e) {
		        // Đổi phông chữ khi di chuột đến
		        Font font = howPlay.getFont();
		        howPlay.setFont(font.deriveFont(font.getStyle() | Font.ITALIC));
		        howPlay.setForeground(Color.RED);
		        howPlay.setBorder(hoverBorder);
		    }
		    @Override
		    public void mouseExited(MouseEvent e) {
		        // Trở lại phông chữ ban đầu khi di chuột ra khỏi nút
		        Font font = howPlay.getFont();
		        howPlay.setFont(font.deriveFont(font.getStyle() & ~Font.ITALIC));
		        howPlay.setForeground(null);
		        howPlay.setBorder(defaultBorder);
		    }
		});
		howPlay.addActionListener(this);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// Thực hiện hành động khi nút đưuọc nhấn
		if (e.getSource() == play) {		// Khi nhấn nút Play
			this.dispose();
			new GameFrame();
			clip.stop();
		}
		if(e.getSource() == howPlay) {		// Khi nhấn nút Guide
			new GameGuide();
		}
		if(e.getSource() == score) {		// Khi nhấn nút Score
			new ScoreboardFrame();
		}
		if (e.getSource() == mute) {		// Khi nhấn nút âm thanh
			isMuted = !isMuted; 	// Đổi trạng thái của biến isMuted
		    if (isMuted) {
		        // Nếu đang tắt tiếng, bật tiếng lên
		        clip.loop(Clip.LOOP_CONTINUOUSLY);
		        mute.setText("Turn off sound");
		    } 
		    else {
		        // Nếu đang bật tiếng, tắt tiếng đi
		        clip.stop();
		        mute.setText("Turn on sound");
		    }   
		}	
	}
	
	// Thêm các sự kiện của chuột
	private void addMouseEvents() {
	    play.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {		// Khi chuột di vào nút (button)
	        	audio.playSound("Sound/click.wav");
	        }
	    });
	    score.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	        	audio.playSound("Sound/click.wav");
	        }
	    });
	    howPlay.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	        	audio.playSound("Sound/click.wav");
	        }
	    });
	}
	
}
