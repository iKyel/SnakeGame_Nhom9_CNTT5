import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;
import javax.swing.border.Border;

// Tạo lớp Score dùng để lưu điểm người dùng sau khi chơi xong
public class Score extends JFrame {
	
    private JLabel playerNameLabel;
    private JTextField playerNameTextField;
    private JLabel scoreLabel;
    private JTextField scoreTextField;
    private JButton saveButton;
    
    public Score(String playerName, int score) {
        setTitle("Score");
        setSize(300, 200);
        setLocationRelativeTo(null);
        
        // Tạo nhãn và textFeild ở dòng PlayerName
        playerNameLabel = new JLabel("Player name:");
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        playerNameTextField = new JTextField(playerName);
        playerNameTextField.setEditable(false);
        playerNameTextField.setFont(new Font("Arial", Font.PLAIN, 18));
        
        // Tạo nhãn và textFeild ở dòng Score
        scoreLabel = new JLabel("Score:");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreTextField = new JTextField(String.valueOf(score));
        scoreTextField.setEditable(false);
        scoreTextField.setFont(new Font("Arial", Font.PLAIN, 18));
        
        // Tạo button saveScore
        saveButton = new JButton("Save score");
        saveButton.setFont(new Font("Arial", Font.BOLD, 18));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveScore();
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 10, 10));		// Set layout cho inputPanel
        
        // Tạo 1 empty border với 10 pixels ở mỗi bên
        Border margin = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        
        // Tạo border cho input panel
        inputPanel.setBorder(margin);
        
        inputPanel.add(playerNameLabel);
        inputPanel.add(playerNameTextField);
        inputPanel.add(scoreLabel);
        inputPanel.add(scoreTextField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        
        // Tạo border cho button panel
        buttonPanel.setBorder(margin);
        buttonPanel.add(saveButton, BorderLayout.SOUTH);
        
        // ContentPane: Là một đối tượng Container chứa tất cả các thành phần của JFrame.
        getContentPane().setLayout(new BorderLayout());
        
        // Thêm inputPanel và đặt ở vị trí trên cùng
        getContentPane().add(inputPanel, BorderLayout.NORTH);
        
        // Thêm buttonPanel và đặt ở vị trí chính giữa
        getContentPane().add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }
    
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
        this.dispose();
    }
    
    private void saveScore() {
        // Code để lưu điểm số vào cơ sở dữ liệu
    	String playerName = playerNameTextField.getText();
        int score = Integer.parseInt(scoreTextField.getText());

        try {
            String url = "jdbc:mysql://localhost/scores_nhom9_cntt5_k62";
            String username = "root";
            String password = "";
            Connection connection = DriverManager.getConnection(url, username, password);		// Tạo kết nối với csdl

            String sql = "INSERT INTO scores_nhom9_cntt5_k62 (player_name, score) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);			// Tạo câu lệnh truy vấn
            statement.setString(1, playerName);
            statement.setInt(2, score);
            statement.executeUpdate();				// Thực thi câu lệnh SQL (statement)

            connection.close();

            showMessage("Score saved successfully!");		// Thông báo nếu save thành công
        } 
        catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while saving score.");		// Thông báo nếu save ko thành công
        }
    }
    
}
