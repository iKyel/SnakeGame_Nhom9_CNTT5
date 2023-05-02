import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class playSound {
	void playSound(String soundName) {
	    try {
	        // Tải tệp âm thanh vào
	        File soundFile = new File(soundName);
	        AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
	        
	        // Tạo Clip từ AudioInputStream
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioIn);
	        
	        // Phát âm thanh
	        clip.start();
	    } 
	    catch (Exception e) {
	        // Xử lý lỗi
	        System.out.println("Error: " + e.getMessage());
	    }
	}
}
