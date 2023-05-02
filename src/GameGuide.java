import javax.swing.*;
public class GameGuide extends JFrame {
	public GameGuide() {
		String message = "Hướng dẫn chơi game rắn săn mồi:\n\n"
                + "Di chuyển con rắn bằng các nút để ăn các trái táo.\n"
                + "Nếu đầu con rắn chạm vào tường hoặc chạm vào thân của nó, bạn sẽ thua.\n"
                + "Sử dụng các phím mũi tên để di chuyển con rắn.\n"
                + "Ấn nút SPACE để tạm dừng trò chơi. Ấn lại lần nữa để tiếp tục.\n"
                + "Chúc may mắn!";
		
		String arrowKeysImg = "<html><body>Sử dụng các phím "
				+ "<img src=\"file:icon.png\"/>"
				+ " để di chuyển con rắn.</body></html>";
		
		message = message.replace("Sử dụng các phím mũi tên", arrowKeysImg);

		JOptionPane.showMessageDialog(null, message, "Hướng dẫn chơi game", JOptionPane.PLAIN_MESSAGE);
	}
}
