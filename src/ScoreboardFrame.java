import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

// Tạo lớp Bảng điểm chứa tên người chơi và điểm đã lưu
public class ScoreboardFrame extends JFrame {
	private JTable table;

    public ScoreboardFrame() {

        // Tạo kết nối tới cơ sở dữ liệu
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/scores_nhom9_cntt5_k62", "root", "");
        } 
        catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Đếm số bản ghi trong bảng scores
        int numRows = 0;
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM scores_nhom9_cntt5_k62")) {
            if (rs.next()) {
                numRows = rs.getInt(1);
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Tạo bảng và chèn dữ liệu vào bảng
        List<String> columns = new ArrayList<>();
        columns.add("Player Name");
        columns.add("Score");
        DefaultTableModel model = new DefaultTableModel(columns.toArray(), numRows);		// Tạo cấu trức bảng mặc định
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM scores_nhom9_cntt5_k62")) {
            int row = 0;
            while (rs.next()) {
                String name = rs.getString("player_name");
                int score = rs.getInt("score");
                model.setValueAt(name, row, 0);
                model.setValueAt(score, row, 1);
                row++;
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Hiển thị bảng lên màn hình
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane);
        
        setTitle("Scoreboard");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
