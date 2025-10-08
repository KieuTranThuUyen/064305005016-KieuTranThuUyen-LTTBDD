# README

## Giới thiệu
Ba bài thực hành trong môn **Lập trình Android với Jetpack Compose** giúp sinh viên làm quen với việc:
- Nhập dữ liệu từ người dùng.  
- Xử lý điều kiện và hiển thị kết quả tương ứng.  
- Tạo giao diện thân thiện bằng Jetpack Compose.

---

## Bài 1: NumberInputApp

### Mục tiêu
- Luyện tập nhập và xử lý số nguyên.  
- Sinh danh sách các số liên tiếp dựa vào dữ liệu người dùng nhập vào.

### Kết quả đạt được
Người dùng nhập số **n**, nhấn **“Tạo”** → ứng dụng hiển thị dãy số từ **1 → n**.  
Nếu nhập sai (số âm, 0, hoặc ký tự không phải số), hiển thị thông báo lỗi **“Dữ liệu bạn nhập không hợp lệ.”**

### Giải thích các hàm chính
- **MainActivity**: khởi tạo ứng dụng và hiển thị hàm `NumberInputApp()`.  
- **NumberInputApp()**:  
  - `inputValue`: lưu dữ liệu người dùng nhập.  
  - `numbers`: danh sách các số được tạo ra.  
  - `errorMessage`: hiển thị lỗi nếu dữ liệu không hợp lệ.  
  - `OutlinedTextField`: nhập số lượng.  
  - `Button`: khi nhấn, dùng `try–catch` để kiểm tra hợp lệ → sinh danh sách `(1..n).toList()`.  
  - `Column` + `Box`: hiển thị danh sách các số, căn giữa và có màu nền.

---

## Bài 2: EmailCheckApp

### Mục tiêu
- Thực hành kiểm tra định dạng chuỗi email do người dùng nhập.  
- Hiển thị thông báo hợp lệ hoặc không hợp lệ theo từng trường hợp.

### Kết quả đạt được
Người dùng nhập địa chỉ email → nhấn **“Kiểm tra”** → ứng dụng hiển thị kết quả:
- “Email không hợp lệ”  
- “Email không đúng định dạng”  
- “Bạn đã nhập email hợp lệ”

### Giải thích các hàm chính
- **MainActivity**: khởi tạo ứng dụng và gọi hàm `EmailCheckScreen()`.  
- **EmailCheckScreen()**:  
  - `email`: lưu nội dung nhập.  
  - `message` và `color`: hiển thị thông báo với màu tương ứng.  
  - `OutlinedTextField`: nhận email người dùng.  
  - `Button`: khi nhấn, kiểm tra:  
    - Rỗng → “Email không hợp lệ”  
    - Không chứa `@` → “Email không đúng định dạng”  
    - Ngược lại → “Bạn đã nhập email hợp lệ”  
  - `Text`: hiển thị thông báo kết quả bên dưới ô nhập.

---

## Bài 3: AgeCheckApp

### Mục tiêu
- Thực hành nhập thông tin cá nhân (họ tên, tuổi).  
- Kiểm tra dữ liệu hợp lệ và phân loại độ tuổi của người dùng.

### Kết quả đạt được
Người dùng nhập **tên** và **tuổi**, nhấn **“Kiểm tra”** → ứng dụng hiển thị:
- “Nam là Người lớn”  
- “Bé Lan là Em bé”  
- “Tuổi phải là số hợp lệ”, v.v.

### Giải thích các hàm chính
- **MainActivity**: khởi chạy giao diện chính với `Practice01Screen()`.  
- **Practice01Screen()**:  
  - `name`, `age`, `result`: lưu dữ liệu nhập và kết quả.  
  - `OutlinedTextField`: nhập họ tên và tuổi.  
  - `Button`: khi nhấn, xử lý điều kiện:  
    - Nếu trống → báo lỗi.  
    - Nếu nhập số → phân loại độ tuổi:
      - `<2`: Em bé  
      - `2–5`: Trẻ em  
      - `6–65`: Người lớn  
      - `>65`: Người già  
  - `Text`: hiển thị kết quả cuối cùng.


