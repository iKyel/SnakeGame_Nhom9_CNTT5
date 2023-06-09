# Trò chơi con rắn
* Project của môn học Công Nghệ Java, về trò chơi con rắn (SnakeGame)
* Nhóm thực hiện: nhóm 9
***
## Giới thiệu

Trò chơi con rắn Java là một trò chơi giải trí đơn giản, trong đó người chơi điều khiển con rắn để ăn thức ăn và tránh va chạm với các tường hoặc chính chúng ta. Trò chơi sử dụng Java để lập trình.

![image](https://user-images.githubusercontent.com/111947701/235714505-1ca890ba-95c6-4931-9289-c3bcd20f715b.png)

## Các chức năng có trong trò chơi
* Di chuyển con rắn bằng phím để ăn quả táo
* Tạm dừng trò chơi
* Lưu điểm số vào cơ sở dữ liệu

## Phần mềm và công cụ cần thiết

* [Java JDK 8+](https://www.oracle.com/java/technologies/downloads/#jdk19-windows)
* [Eclipse IDE](https://www.eclipse.org/downloads/)
* [XAMPP](https://www.apachefriends.org/download.html)
* [MySQL-connector-java](https://dev.mysql.com/downloads/connector/j/?os=26)

## Công nghệ sử dụng

* Java [JDK 8+]
* JDBC
* MySql 
## ==================== Lời nói đầu ==================
Chi tiết về bài tập lớn: 
* File code: thực hiện lấy file code bằng cách clone project về Eclipse, được trình bày ở bên dưới.
* File báo cáo pdf và file jar đóng gói project cùng các tài nguyên cần thiết ở trong mục **Release**. Hãy tải xuống tệp *SnakeGame_CNTT5_Nhom9.rar*
* Hướng dẫn cài đặt và chạy trò chơi được thực hiện ở bên dưới.

***

## ================= Khởi tạo database =================

* Bước 1: Mở XAMPP Control Panel.
![image](https://user-images.githubusercontent.com/111947701/235753610-d362f84f-2796-4c15-992d-70bd019a45e3.png)

* Bước 2: Click "Start" Apache và MySQL.
![image](https://user-images.githubusercontent.com/111947701/235753733-6200126f-0bbc-450b-96c5-b6d21bd2c31e.png)


* Bước 3: truy cập: http://localhost/phpmyadmin
![image](https://user-images.githubusercontent.com/111947701/235753825-addfe6bc-af97-423b-b099-d83753865d69.png)

* Bước 4: tạo cơ sở dữ liệu có tên là "scores_nhom9_cntt5_k62", tạo bảng tên "scores_nhom9_cntt5_k62" với 2 cột, cột đầu có tên "player_name" với **Type** là **VARCHAR**, cột thứ 2 có tên "score" với **Type** là **INT**<br>
Có hai cách để làm điều này:<br>
-Cách 1: Các bước thực hiện như sau: New -> Nhập "scores_nhom9_cntt5_k62" trong trường *database name* -> Nhập "scores_nhom9_cntt5_k62" trong trường *Table name* và "2" trong trường "number of column" -> create -> Hàng 1: nhập "player_name" trong trường *Name*, "VARCHAR" trong trường *TYPE*, Hàng 2: nhập "score" trong trường *Name*, "INT" trong trường *TYPE*<br>
-Cách 2: Chọn mục SQL -> nhập câu lệnh: *CREATE DATABASE scores_nhom9_cntt5_k62;* để tạo database -> go -> nhập tiếp câu lệnh sau:<br>
*USE scores_nhom9_cntt5_k62;<br>
CREATE TABLE scores_nhom9_cntt5_k62 (<br>
    player_name VARCHAR(255),<br>
    score INT<br>
)*;<br>
Sau đó ấn go


## =============== Hướng dẫn chạy trò chơi ===============
 Điều cần làm trước tiên:
* Hãy khởi tạo database được trình bày ở bước trên, chắc chắn rằng đã mở *XAMPP Control Panel*, và *Apache* và *MySQL* đã được **start**
* Hãy tải mysql-connector (tải bằng đường link phía trên hoặc gõ trực tiếp lên google), lưu lại đường dẫn đến tệp trong thư mục

### Cách 1: Chạy bằng code trên Eclipse

* Bước 1: Mở Eclipse [Install nếu chưa có]
* Bước 2: Click vào File > Import > Git > Projects From Git (with smart import) > Clone Uri > Copy Url sau vào trường URL: 
https://github.com/iKyel/SnakeGame_Nhom9_CNTT5 > Next > Next > Next > Finish.
* Bước 3: Click vào project, chuột phải chọn Build Path -> Configure Buid Path
* Bước 4: Ấn vào thư viện bị lỗi dưới hình chọn Remove
![loi1](https://user-images.githubusercontent.com/111947701/235816021-5881b9e1-c451-47c3-a09f-5430cd24d7ad.png)
* Bước 5: Thêm thư viện mysql-connector bằng cách vào click vào Modulepath -> Add External JARs -> chọn đường dẫn đến file mysql-connector.jar đã tải về
![loi2](https://user-images.githubusercontent.com/111947701/235816448-59066e97-b2da-45f3-8dac-ef7d315e63bf.png)
* Bước 6: Chạy hàm main trong src/SnakeGame.java


### Cách 2: Chạy bằng file được đóng gói

* Bước 1: Tải tệp *SnakeGame_CNTT5_Nhom9.rar* trong mục **Release** rồi giải nén tệp
* Bước 2: Mở tệp vừa giải nén, chạy file *SnakeGame_Nhom9_CNTT5_K62.jar*

***

## Một vài Screenshots của project

![image](https://user-images.githubusercontent.com/111947701/235725896-e93a74b1-3a8d-4122-b6df-1dc2203e8ad0.png)

![image](https://user-images.githubusercontent.com/111947701/235725925-2d20aa95-1401-48f8-ae15-a8fcaf887185.png)

![image](https://user-images.githubusercontent.com/111947701/235725954-9ec11409-e8e8-43be-8307-6d3d4f5fb529.png)

![image](https://user-images.githubusercontent.com/111947701/235726013-4d63012b-bfc9-45d0-9560-8fe4e27f66f3.png)

![image](https://user-images.githubusercontent.com/111947701/235726065-0e91157e-ed94-4b63-aa87-997f6edf1b0b.png)

***

## Lời kết
* Sản phẩm còn chưa được hoàn thiện, nhóm sẽ cố gắng sửa chữa và nâng cấp.
* Lời cảm ơn chân thành đến sự hướng dẫn của giảng viên.
 




