# Báo cáo môn Lập trình di động

## Câu 1: Mong muốn và định hướng của bạn là gì sau khi học xong môn học?

- Sau khi hoàn thành môn học, em mong muốn có thể nắm vững kiến thức nền tảng về lập trình di động, từ cách thiết kế giao diện, xử lý dữ liệu cho đến việc triển khai một ứng dụng hoàn chỉnh.  
- Em hy vọng sau môn học sẽ có khả năng tự mình xây dựng những ứng dụng cơ bản để áp dụng vào thực tế.  
- Định hướng tiếp theo của em là không chỉ dừng lại ở mức độ cơ bản, mà còn tiếp tục rèn luyện, học hỏi thêm các công nghệ mới như Flutter, React Native, Kotlin hay Swift để nâng cao trình độ.  
- Ngoài ra, em cũng muốn phát triển kỹ năng làm việc nhóm, tư duy logic và khả năng giải quyết vấn đề để sau này có thể tham gia vào các dự án phát triển phần mềm thực tế, thậm chí có thể tự xây dựng ứng dụng phục vụ nhu cầu học tập, giải trí và công việc tương lai.  

## Câu 2: Theo bạn, trong tương lai gần (10 năm) lập trình di động có phát triển không? Giải thích tại sao?

- Theo quan điểm của em, trong 10 năm tới lập trình di động chắc chắn sẽ tiếp tục phát triển mạnh mẽ.  
- Thứ nhất, điện thoại thông minh ngày nay đã trở thành vật dụng thiết yếu đối với hầu hết mọi người, từ học sinh, sinh viên cho đến người đi làm, vì vậy nhu cầu về các ứng dụng di động sẽ không ngừng tăng lên.  
- Thứ hai, ứng dụng di động không chỉ phục vụ giải trí như trò chơi hay mạng xã hội, mà còn được sử dụng trong rất nhiều lĩnh vực quan trọng như giáo dục, y tế, tài chính, thương mại điện tử, quản lý công việc và điều khiển thiết bị thông minh.  
- Thứ ba, sự ra đời và phát triển của các công nghệ mới như AI, IoT, mạng 5G/6G, VR, AR sẽ thúc đẩy lập trình di động ngày càng sáng tạo và mang lại nhiều trải nghiệm tiện ích hơn cho người dùng.  
- Cuối cùng, thị trường lao động trong lĩnh vực này vẫn luôn rộng mở, khi các doanh nghiệp công nghệ lớn nhỏ đều có nhu cầu tuyển dụng lập trình viên di động.  

## Câu 3: Xây dựng ứng dụng ProfileApp

### Mô tả bài tập
Xây dựng ứng dụng Android đơn giản bằng **Jetpack Compose** để hiển thị thông tin cá nhân.  
Ứng dụng gồm **ảnh đại diện (avatar)**, **tên sinh viên**, và **lớp/trường**. Ngoài ra có **TopAppBar** với nút **Quay lại** và **Chỉnh sửa**.

### Mục tiêu
- Làm quen với **Compose + Material 3** trong Android Studio.  
- Hiểu cách bố cục giao diện bằng `Scaffold`, `TopAppBar`, `Column`.  
- Biết cách sử dụng `Image`, `Text`, `IconButton` và `Modifier`.

### Kết quả đạt được
Ứng dụng chạy thành công, hiển thị giao diện như yêu cầu:
- Avatar hình tròn.  
- Tên và lớp được căn giữa màn hình.  
- Có thanh công cụ (TopAppBar) với nút Back và Edit.

### Giải thích các hàm chính
- **`setContent {}`**: Khởi tạo giao diện Compose trong Activity.  
- **`Scaffold()`**: Tạo khung bố cục chính của màn hình.  
- **`ProfileTopBar()`**: Tùy chỉnh `TopAppBar` với 2 nút `Back` và `Edit`.  
- **`ProfileScreen()`**: Hiển thị nội dung chính (Avatar, Tên, Lớp).  
- **`Image()`**: Hiển thị ảnh avatar, kết hợp `clip(CircleShape)` để bo tròn.  
- **`Text()`**: Hiển thị họ tên và lớp/trường.  
- **`Modifier`**: Dùng để căn giữa, đặt kích thước và khoảng cách (`padding`, `size`, `Spacer`).  
