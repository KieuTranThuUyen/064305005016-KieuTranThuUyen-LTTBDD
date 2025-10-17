# Bài Tập Jetpack Compose – Ứng Dụng Giới Thiệu Thành Phần UI Cơ Bản

## **Mục Tiêu**
Xây dựng một ứng dụng Android sử dụng **Jetpack Compose** để giới thiệu và minh hoạ các **thành phần giao diện người dùng (UI Components)** cơ bản trong Compose theo hướng **lập trình khai báo (Declarative UI)**.  
Ứng dụng cho phép người dùng khám phá, xem ví dụ và tương tác trực tiếp với từng loại thành phần UI.

---

## **Kết Quả Đạt Được**
- Hiểu và sử dụng được **Jetpack Compose Navigation** để chuyển giữa các màn hình.  
- Áp dụng các thành phần UI cơ bản:  
  - **Text**, **Image**, **TextField**, **PasswordField**, **Checkbox**, **Switch**, **Card**, **Row**, **Column**.  
- Biết cách **xử lý sự kiện người dùng (state management)** bằng `remember` và `mutableStateOf`.  
- Thiết kế bố cục (layout) đẹp mắt, dễ nhìn, có tính tương tác.

---

## **Giải Thích Ngắn Gọn Các Hàm Chính**

### `MainActivity`
- Là **activity khởi đầu** của ứng dụng.  
- Gọi `setContent` để hiển thị giao diện Compose thông qua hàm `JetpackApp()`.

---

### `JetpackApp()`
- Quản lý **điều hướng (Navigation)** trong ứng dụng bằng `NavHost` và `NavController`.  
- Xác định các **route** tương ứng với từng màn hình con (intro, list, text, image, v.v).

---

### `IntroScreen()`
- Màn hình giới thiệu đầu tiên.  
- Hiển thị thông tin sinh viên, logo và nút **“I’m ready”**.  
- Khi nhấn nút → hiện **Dialog** chào mừng và cho phép chuyển sang **UI Components List**.

---

### `UIComponentListScreen()`
- Liệt kê danh sách các **nhóm thành phần UI**: Display, Input, Selection, Layout.  
- Khi chọn một mục → chuyển sang màn hình demo tương ứng.  
- Có thêm mục “Tự tìm hiểu” để khuyến khích người dùng khám phá thêm.

---

### `TextDetailScreen()`
- Minh họa các kiểu hiển thị chữ (Text): **đậm, nghiêng, gạch chân, gạch xóa, giãn chữ, phối màu**.

---

### `ImageScreen()`
- Hiển thị hình ảnh minh họa từ tài nguyên trong ứng dụng và liên kết ngoài.

---

### `TextFieldScreen()`
- Cho phép nhập dữ liệu trong `OutlinedTextField`.  
- Dữ liệu hiển thị **real-time** ngay dưới ô nhập.

---

### `PasswordFieldScreen()`
- Ô nhập mật khẩu với biểu tượng **ẩn/hiện ký tự**.  
- Sử dụng `PasswordVisualTransformation` và `IconButton` để đổi trạng thái hiển thị.

---

### `CheckboxScreen()`
- Minh họa **checkbox** cơ bản, hiển thị trạng thái `true/false` theo lựa chọn.

---

### `SwitchScreen()`
- Minh họa **công tắc bật/tắt (Switch)** và hiển thị giá trị hiện tại.

---

### `CardScreen()`
- Giới thiệu **thành phần Card** với tiêu đề và nội dung mẫu.

---

### `RowLayoutScreen()`  
- Minh họa **bố cục hàng ngang (Row)**: các phần tử được sắp xếp cạnh nhau.  

---

### `ColumnLayoutScreen()`  
- Minh họa **bố cục cột dọc (Column)**: các phần tử sắp xếp theo chiều dọc.  

---

### `ScreenHeader()`
- Thanh tiêu đề chung cho các màn hình, hỗ trợ **nút quay lại (Back)** nếu cần.  
- Dùng `CenterAlignedTopAppBar` của Material3.



