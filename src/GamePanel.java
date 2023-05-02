
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.*;
import javax.sound.sampled.*;
import java.io.*;

public class GamePanel extends JPanel implements ActionListener{
	
	private static final int SCREEN_WIDTH = 600;		// Chiều dài màn chơi
	private static final int SCREEN_HEIGHT = 600;		// Chiều cao màn chơi
	
	private static final int UNIT_SIZE = 25;			// Kích thước của 1 ô
	private static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT) / (UNIT_SIZE*UNIT_SIZE);		// Số ô
	
	private static int DELAY = 130;		// Tạo thời gian chờ (trì hoãn) giữ 2 khung hình
	
	// Tạo mảng x và y để lưu trữ tọa độ của con rắn
	private int[] x = new int[GAME_UNITS];
	private int[] y = new int[GAME_UNITS];
	
	private int bodyParts = 3;			// Độ dài phần thân của con rắn
	private int appleEaten;				// Số quả táo đã ăn
	
	// Tạo tọa độ x và y của quả táo (0 < x,y < SCREEN_WIDTH/HEIGHT)
	private int appleX;
	private int appleY;
	
	// Tạo mảng x và y để lưu trữ tọa độ của bức tường 
	private int[] wallX = new int[GAME_UNITS];
	private int[] wallY = new int[GAME_UNITS];
	
	// Tạo biến lưu trữ phương hướng đi của con rắn: Right, Left, Up, Down
	private char direction = 'R';
	
	private boolean isRunning = false;		// Ktra con rắn có đang chạy ko?
	
	/* Lớp Timer: Cho phép chúng ta tạo ra một hay nhiều sự kiện định kỳ sau một khoảng thời gian nhất định. 
	              Giúp cập nhật giao diện người dùng của trò chơi hoặc trong việc thực hiện các tác vụ định kỳ tiếp theo.	*/
	private Timer timer;		// Tạo 1 luồng thời gian riêng
	
	/* Lớp Random: Cho phép chúng ta tạo ra một số ngẫu nhiên. 
				   Giúp tạo ra các giá trị ngẫu nhiên cho trò chơi, chẳng hạn như vị trí ban đầu của các đối tượng.			*/
	private Random random;
	
	private boolean isPaused = false;	// Kiểm tra xem chường trình có đang dừng ko?
	
	playSound audio = new playSound(); 	// Sử dụng âm thanh
	private String name;		// Tên người chơi
	
	/* ------------------------------------------------------------------ */
	
	// Nhập tên người chơi
	public void Name() {
		name = JOptionPane.showInputDialog("Nhập tên của bạn:");
	    JOptionPane.showMessageDialog(null, "Xin chào " + name + ", chúc bạn may mắn!");
	}
	
	public GamePanel() {
		Name();
		random = new Random();
		
		// Đặt kích thước ưu tiên của thành phần này trong các bố cục động.
//		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		
		this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		this.setBackground(Color.black);
		
		this.setFocusable(true);			
		this.addKeyListener(new MyKeyAdapter());		// Thêm các sự kiện khi nhấn phim
		
		startGame();
	}
	
	public void level() {
		// Hiển thị dialog box để cho người chơi chọn độ khó
		String[] options = {"Dễ", "Trung bình", "Khó"};
		int choice = JOptionPane.showOptionDialog(this, "Chọn độ khó", "Độ khó", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		// Xử lý lựa chọn của người chơi
		switch (choice) {
			case 0: // Chọn dễ
				DELAY = 200; // Cập nhật tốc độ rắn
				break;
			case 1: // Chọn trung bình
				DELAY = 130; // Cập nhật tốc độ rắn
				break;
			case 2: // Chọn khó
				DELAY = 80; // Cập nhật tốc độ rắn
				break;
			default: // Người dùng đóng dialog box
				System.exit(0); // Thoát khỏi chương trình
				break;
		}
	}
	
	public void startGame() {		// Bắt đầu Game
		level();		// Chọn mức độ chơi
		createWall();
		newApple();
		isRunning = true;
		
		// Đăng ký GamePanel là một ActionListener cho Timer
		timer = new Timer(DELAY, this);			// Cho biết tốc độ của trò chơi
		timer.start();							// Chạy chương trình
	}
	
	public void paintComponent(Graphics g) {		// Vẽ đồ họa lên trên component
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {				// Vẽ
		if (isRunning) {
			 // Vẽ các ô xen kẽ 
		    for (int row = 0; row < SCREEN_WIDTH; row++) {
		        for (int col = 0; col < SCREEN_HEIGHT; col++) {
		            if ((row + col) % 2 == 0) {
		                // Vẽ ô cho các hàng và cột chẵn (Xanh đậm)
		                g.setColor(new Color(130, 215, 136));
		            } 
		            else {
		                // Vẽ ô cho các hàng và cột lẻ (Xanh nhạt)
		                g.setColor(new Color(193, 255, 154));
		            }
		            g.fillRect(col * UNIT_SIZE, row * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
		        }
		    }
			
			// Vẽ quả táo
			g.setColor(new Color(220, 50, 50));
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			// Vẽ đầu và thân con rắn
			for (int i=0; i<bodyParts; i++) {
				if (i==0) {			// Vẽ cho phần đầu
					g.setColor(new Color(200, 150, 255));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
					
					g.setColor(Color.BLACK);
					g.drawRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {				// Vẽ cho phần thân
					g.setColor(new Color(75, 0, 130));		// Color(r, g, b, 255)
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
					
					g.setColor(Color.BLACK);
					g.drawRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			
			// Vẽ tường (Chướng ngại vật) ở góc dưới phải
			for (int i=10; i<=20; i++) {
				g.setColor(new Color(139, 90, 43));
				g.fillRect(wallX[i], wallY[20], UNIT_SIZE, UNIT_SIZE);
				g.fillRect(wallX[20], wallY[i], UNIT_SIZE, UNIT_SIZE);
				
				g.setColor(Color.DARK_GRAY);
				g.drawRect(wallX[i], wallY[20], UNIT_SIZE, UNIT_SIZE);
				g.drawRect(wallX[20], wallY[i], UNIT_SIZE, UNIT_SIZE);
			}
			
			// Vẽ tường (Chướng ngại vật) ở góc trên trái
			for (int i=3; i<=13; i++) {
				g.setColor(new Color(139, 90, 43));
				g.fillRect(wallX[i], wallY[3], UNIT_SIZE, UNIT_SIZE);
				g.fillRect(wallX[3], wallY[i], UNIT_SIZE, UNIT_SIZE);
							
				g.setColor(Color.DARK_GRAY);
				g.drawRect(wallX[i], wallY[3], UNIT_SIZE, UNIT_SIZE);
				g.drawRect(wallX[3], wallY[i], UNIT_SIZE, UNIT_SIZE);
			}
			
			// Vẽ tường (Chướng ngại vật) ở góc dưới trái
			for (int i=6; i<=14; i++) {
				g.setColor(new Color(139, 90, 43));
				g.fillRect(wallX[i], wallY[17], UNIT_SIZE, UNIT_SIZE);
				g.fillRect(wallX[6], wallY[i+3], UNIT_SIZE, UNIT_SIZE);
										
				g.setColor(Color.DARK_GRAY);
				g.drawRect(wallX[i], wallY[17], UNIT_SIZE, UNIT_SIZE);
				g.drawRect(wallX[6], wallY[i+3], UNIT_SIZE, UNIT_SIZE);
			}
			
			// Vẽ tường (Chướng ngại vật) ở góc trên phải
			for (int i=9; i<=17; i++) {
				g.setColor(new Color(139, 90, 43));
				g.fillRect(wallX[i], wallY[6], UNIT_SIZE, UNIT_SIZE);
				g.fillRect(wallX[17], wallY[i-3], UNIT_SIZE, UNIT_SIZE);
													
				g.setColor(Color.DARK_GRAY);
				g.drawRect(wallX[i], wallY[6], UNIT_SIZE, UNIT_SIZE);
				g.drawRect(wallX[17], wallY[i-3], UNIT_SIZE, UNIT_SIZE);
			}
			
			 // Vẽ bảng điểm bên phải
			g.setColor(new Color(175, 215, 136));
			g.fillRect(SCREEN_WIDTH, 0, SCREEN_WIDTH + 250, SCREEN_HEIGHT);		// Tô màu cho bảng điểm
			
			g.setColor(new Color(139, 69, 19));
			g.setFont( new Font("Ink Free",Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Snake Game", (SCREEN_WIDTH + 230 - metrics.stringWidth("Snake Game")), g.getFont().getSize() + 200);
			g.drawString("Score: "+ appleEaten, (SCREEN_WIDTH + SCREEN_WIDTH + 200 - metrics.stringWidth("Score: "+ appleEaten)) / 2, g.getFont().getSize() + 300);
		}
		if (isPaused == false && isRunning == false) 
			gameOver(g);
		
		
	}
	
	// Tạo các bức tường (Chướng ngại vật)
	public void createWall() {
		// Vẽ tường ở bên phải dưới
		for (int i=10; i<=20; i++) {			
			wallX[i]  = i * UNIT_SIZE;			// Đặt Tọa độ của từng wall ngang
			wallY[20] = 20 * UNIT_SIZE;
			
			wallX[20] = 20 * UNIT_SIZE;			// Đặt Tọa độ của từng wall dọc
			wallY[i]  = i * UNIT_SIZE;
		}
		
		// Vẽ tường ở bên trái trên
		for (int i=3; i<=13; i++) {			
			wallX[i]  = i * UNIT_SIZE;			// Đặt Tọa độ của từng wall ngang
			wallY[3]  = 3 * UNIT_SIZE;
			
			wallX[3]  = 3 * UNIT_SIZE;			// Đặt Tọa độ của từng wall dọc
			wallY[i]  = i * UNIT_SIZE;
		}
		
		// Vẽ tường ở bên trái dưới
		for (int i=6; i<=14; i++) {
			wallX[i] = i * UNIT_SIZE;			// Đặt Tọa độ của từng wall ngang
			wallY[17] = 17 * UNIT_SIZE;
			
			wallX[6] = 6 * UNIT_SIZE;			// Đặt Tọa độ của từng wall dọc
			wallY[i+3] = (i+3) * UNIT_SIZE;
		}
		
		// Vẽ tường ở bên phải trên
		for (int i=9; i<=17; i++) {
			wallX[i] = i * UNIT_SIZE;			// Đặt Tọa độ của từng wall ngang
			wallY[6] = 6 * UNIT_SIZE;
					
			wallX[17] = 17 * UNIT_SIZE;			// Đặt Tọa độ của từng wall dọc
			wallY[i-3] = (i-3) * UNIT_SIZE;
		} 

	}
	
	// Sinh ra quả táo mới
	public void newApple() {
		int aX;
		int aY;
		do {
			aX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE);
			aY = random.nextInt(SCREEN_WIDTH/UNIT_SIZE);
			
			appleX = aX * UNIT_SIZE;	
			appleY = aY * UNIT_SIZE;// Random tọa độ x của Apple
		} while (checkDuplicate(appleX, appleY));			// Ktra nếu tọa độ Apple trùng với tọa độ Wall
	}
	
	public boolean checkDuplicate(int aX, int aY) {
		// Kiểm tra có trùng với tường phải dưới
		for(int i=10; i<=20; i++) {
			if((aX == wallX[i] && aY == wallY[20]) || (aX == wallX[20] && aY ==wallY[i])) {
				return true;
			}
		}
		
		for(int i=3; i<=13; i++) {
			if((aX == wallX[i] && aY == wallY[3]) || (aX == wallX[3] && aY ==wallY[i])) {
				return true;
			}
		}
		
		for(int i=6; i<=14; i++) {
			if((aX == wallX[i] && aY == wallY[17]) || (aX == wallX[6] && aY ==wallY[i+3])) {
				return true;
			}
		}
		
		for(int i=9; i<=17; i++) {
			if((aX == wallX[i] && aY == wallY[6]) || (aX == wallX[17] && aY ==wallY[i-3])) {
				return true;
			}
		}
		return false;
	}
	
	// Di chuyển con rắn
	public void move() {				
		// Dịch chuyển phần thân con rắn một đơn vị để tạo được ra nước đi
		for (int i=bodyParts; i>0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch (direction) {
			case 'U':		// Đi lên
				y[0] = (y[0] - UNIT_SIZE)<0 ? (SCREEN_HEIGHT - UNIT_SIZE):(y[0] - UNIT_SIZE);		// y[0]: Tọa độ y của phần đầu con rắn
				break;
			case 'D':		// Đi xuống
				y[0] = Math.abs(y[0] + UNIT_SIZE) % SCREEN_HEIGHT;		
				break;
			case 'L':		// Sang trái
				x[0] = (x[0] - UNIT_SIZE)<0 ? (SCREEN_WIDTH - UNIT_SIZE):(x[0] - UNIT_SIZE);		// x[0]: Tọa độ x của phần đầu con rắn
				break;
			case 'R':		// Sang phải
				x[0] = Math.abs(x[0] + UNIT_SIZE) % SCREEN_WIDTH;		
				break;
		}
	}

	// Ktra việc ăn quả táo và tính điểm
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			appleEaten++;
			newApple();
			audio.playSound("Sound/eatSound.wav");
//			playEatSound();
		}
	}
	
	// Kiểm tra sự va chạm
	public void checkCollisions() {
		// Ktra đầu có chạm vào thân không?
		for (int i=bodyParts; i>0; i--)
			if ((x[0] == x[i]) && (y[0] == y[i]))
				isRunning = false;
		
		// Ktra đầu có chạm vào tường không (Tường - chướng ngại vật) ...
		// Ở bên trái trên
		for (int i=10; i<=20; i++) {							
			if ((x[0] == wallX[i]) && (y[0] == wallY[20]))		// Chạm tường ngang
				isRunning = false;
			
			if ((x[0] == wallX[20]) && (y[0] == wallY[i]))		// Chạm tường dọc
				isRunning = false;
		}
		
		// Ở bên phải dưới
		for (int i=3; i<=13; i++) {							
			if ((x[0] == wallX[i]) && (y[0] == wallY[3]))		// Chạm tường ngang
				isRunning = false;
					
			if ((x[0] == wallX[3]) && (y[0] == wallY[i]))		// Chạm tường dọc
				isRunning = false;
		}
		
		// Ở bên trái dưới
		for (int i=6; i<=14; i++) {							
			if ((x[0] == wallX[i]) && (y[0] == wallY[17]))		// Chạm tường ngang
				isRunning = false;
							
			if ((x[0] == wallX[6]) && (y[0] == wallY[i+3]))		// Chạm tường dọc
				isRunning = false;
		}
		
		// Ở bên phải trên
		for (int i=9; i<=17; i++) {							
			if ((x[0] == wallX[i]) && (y[0] == wallY[6]))		// Chạm tường ngang
				isRunning = false;
									
			if ((x[0] == wallX[17]) && (y[0] == wallY[i-3]))	// Chạm tường dọc
				isRunning = false;
		}
		
/*		// Ktra đầu có chạm vào tường trái không?
		if (x[0] < 0)
		//	isRunning = false;

		// Ktra đầu có chạm vào tường phải không?
		if (x[0] >= SCREEN_WIDTH)
		//	isRunning = false;
		
		// Ktra đầu có chạm vào tường trên không?
		if (y[0] < 0)
		//	isRunning = false;
		
		// Ktra đầu có chạm vào tường dưới không?
		if (y[0] >= SCREEN_HEIGHT)
		//	isRunning = false;							*/
		
		if (!isRunning)
			timer.stop();
	}
	
	public void gameOver(Graphics g) {		// Kết thúc Game
		audio.playSound("Sound/gameOver.wav");
	    JFrame gameFrame = (JFrame) SwingUtilities.getWindowAncestor(GamePanel.this);		// Lấy đối tượng JFrame chứa GamePanel hiện tại
	    gameFrame.dispose();
	    
	    // Tạo một Frame để lưu điểm (saveScore)
	    Score scoreFrame = new Score(name, appleEaten);
	    scoreFrame.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosed(WindowEvent e) {		// Thêm sự kiện khi đóng cửa sổ
	        	JFrame gameOverFrame = new JFrame("Game Over");
	    		gameOverFrame.setSize(400,400);
    		    
	    		JPanel panel = new JPanel();
	    		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));		// Set layout cho panel  
	    		panel.add(Box.createRigidArea(new Dimension(0, 20))); 		// Tạo khoảng cách có kích thước 20 pixel
	    		
	    		// Tạo Label(nhãn) GameOver
	    		JLabel gameOverLabel = new JLabel("Game Over");
	    		gameOverLabel.setFont(new Font("Arial", Font.BOLD, 36)); 			// Chọn một font lớn hơn
	    		gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    		gameOverLabel.setHorizontalAlignment(SwingConstants.CENTER); 		// Căn lề giữa
	    		panel.add(gameOverLabel);
	    		
	    		panel.add(Box.createRigidArea(new Dimension(0, 20))); 		// Tạo khoảng cách có kích thước 20 pixel
	    		
	    		// Tạo Label(nhãn) Score + điểm
	    		JLabel scoreLabel = new JLabel("Score: " + appleEaten);
	    		scoreLabel.setFont(new Font("Arial", Font.BOLD, 36)); 		// Chọn một font lớn hơn
	    		scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    		scoreLabel.setHorizontalAlignment(SwingConstants.CENTER); 		// Căn lề giữa
	    		scoreLabel.setForeground(Color.RED); 		// Chọn màu đỏ
	    		panel.add(scoreLabel);
	    		
	    		panel.add(Box.createRigidArea(new Dimension(0, 20))); 		// Tạo khoảng cách có kích thước 20 pixel
	    		
	    		// Tạo Button(nút) PlayAgain
	    		JButton playAgainButton = new JButton("Play Again");
	    		playAgainButton.setBounds(200, 280, 200, 80);
	    		playAgainButton.setFocusable(false);
	    		playAgainButton.setFont(new Font("Arial",Font.BOLD, 35));
	    		playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	    		playAgainButton.addActionListener(new ActionListener() {		// Thêm sự kiện khi nhấn nút
	    		    @Override
	    		    public void actionPerformed(ActionEvent e) {
	    		        gameOverFrame.dispose();
	    		        // Khởi động lại game ở đây
	    		        new GameFrame();
	    		    }
	    		});
	    		panel.add(playAgainButton);

	    		panel.add(Box.createRigidArea(new Dimension(0, 20))); 		// Tạo khoảng cách 20 pixel
	    		
	    		// Tạo Button(nút) Go to Main Manu
	    		JButton homeButton = new JButton("Go to Main Menu");
	    		homeButton.setBounds(200, 280, 200, 80);
	    		homeButton.setFocusable(false);
	    		homeButton.setFont(new Font("Arial",Font.BOLD, 35));
	    		homeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	    		homeButton.addActionListener(new ActionListener() {			// Thêm sự kiện khi nhấn nút
	    		    @Override
	    		    public void actionPerformed(ActionEvent e) {
	    		        gameOverFrame.dispose();
	    		        new GameMenu();
	    		    }
	    		});
	    		panel.add(homeButton);

	    		panel.add(Box.createRigidArea(new Dimension(0, 20))); 		// Tạo khoảng cách 20 pixel

	    		// Tạo Button(nút) ExitGame
	    		JButton exitButton = new JButton("Exit Game");
	    		exitButton.setBounds(200, 280, 200, 80);
	    		exitButton.setFocusable(false);
	    		exitButton.setFont(new Font("Arial",Font.BOLD, 35));
	    		exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	    		exitButton.addActionListener(new ActionListener() {
	    		    @Override
	    		    public void actionPerformed(ActionEvent e) {
	    		        gameOverFrame.dispose();
	    		        System.exit(0);
	    		    }
	    		});
	    		panel.add(exitButton);

	    		    gameOverFrame.add(panel);
	    		    gameOverFrame.setVisible(true);
	    		    gameOverFrame.setLocationRelativeTo(null);
	        }
	    });
		scoreFrame.setVisible(true);
	    }
	
	@Override
	public void actionPerformed(ActionEvent e) {			// Thực hiện các hành động
		// Code xử lý sự kiện Timer, được thực thi sau mỗi khoảng thời gian DELAY
		if (isRunning) {
			move();					// Di chuyển theo hướng (direction)
			createWall();			// Tạo tường
			checkApple();	
			checkCollisions();		// Ktra có va chạm ko?
		}
		repaint();				// Vẽ lại hình ảnh để cập nhật lại giao diện người dùng
	}

	// Lớp xử lý các sự kiện của phím nhấn
	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:		
					if (direction != 'R') {			// Ktra để tránh TH bị xoay ngược chiều
						direction = 'L';
						audio.playSound("Sound/move_left.wav");
					}
					break;
				case KeyEvent.VK_RIGHT:
					if (direction != 'L') {
						direction = 'R';
						audio.playSound("Sound/move_right.wav");
					}
					break;
				case KeyEvent.VK_UP:
					if (direction != 'D') {			// Ktra để tránh TH bị xoay ngược chiều
						direction = 'U';
						audio.playSound("Sound/move_up.wav");
					}
					break;	
				case KeyEvent.VK_DOWN:
					if (direction != 'U') {			// Ktra để tránh TH bị xoay ngược chiều
						direction = 'D';
						audio.playSound("Sound/move_down.wav");
					}
					break;
				case KeyEvent.VK_SPACE:
	                // Code xử lý khi ấn phím Space
	                if (isRunning) {	                
	                    isRunning = false;
	                    isPaused = true;
	                    timer.stop();
	                } else {
	                    isRunning = true;
	                    isPaused = false;
	                    timer.start();
	                }
	                break;			
			}
		}
	}
	
}
	

