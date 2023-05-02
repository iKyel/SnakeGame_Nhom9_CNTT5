
import javax.swing.JFrame;

public class GameFrame extends JFrame {
	
	public GameFrame() {
		this.setTitle("Snake Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		this.setSize(850, 638);
		this.add(new GamePanel());			// Thêm GamePanel vào khung GameFrame
//		this.pack();						// Tự động thay đổi kích thước của JFrame dựa trên kích thước của Component mà nó chứa
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
	}

}
