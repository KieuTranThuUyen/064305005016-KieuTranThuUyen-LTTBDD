# Bài Tập Jetpack Compose – Ứng Dụng Giới Thiệu Thành Phần UI Cơ Bản

## **Mục Tiêu**
Xây dựng một ứng dụng Android sử dụng **Jetpack Compose** để giới thiệu và minh họa các **thành phần giao diện người dùng (UI Components)** cơ bản trong Compose theo hướng **lập trình khai báo (Declarative UI)**.  
Ứng dụng cho phép người dùng khám phá, xem ví dụ và tương tác trực tiếp với từng loại thành phần UI.

---

## **Kết Quả Đạt Được**
- Hiểu và sử dụng được **Jetpack Compose Navigation** để chuyển giữa các màn hình.  
- Áp dụng các thành phần UI cơ bản:  
  - **Text**, **Image**, **TextField**, **PasswordField**, **Checkbox**, **Switch**, **RadioButton**, **Button**, **Slider**, **ListView**, **GridView**, **ProgressBar**, **Card**, **Row**, **Column**, **Box**, **Spacer**, **IconButton**.  
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
- Xác định các **route** tương ứng với từng màn hình con (intro, list, text, image, v.v.).

---

### `IntroScreen()`
- Màn hình giới thiệu đầu tiên.  
- Hiển thị thông tin sinh viên, logo và nút **“I’m ready”**.  
- Khi nhấn nút → hiện **Dialog** chào mừng và cho phép chuyển sang **UI Components List**.

---

### `UIComponentListScreen()`
- Liệt kê danh sách các **nhóm thành phần UI**: Display, Input, Selection, Layout, Actions.  
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

### `RadioButtonScreen()`
- Minh họa **radio button** cho phép chọn một tùy chọn từ nhiều lựa chọn (ví dụ: giới tính, màu sắc).

---

### `ButtonScreen()`
- Minh họa **nút bấm (Button)** với tính năng đếm số lần nhấn và nút reset.

---

### `SliderScreen()`
- Minh họa **thanh trượt (Slider)** để chọn giá trị trong một khoảng xác định.

---

### `ListViewScreen()`
- Minh họa danh sách cuộn (**LazyColumn**) với các mục hiển thị thông tin và biểu tượng.

---

### `GridViewScreen()`
- Minh họa lưới (**LazyVerticalGrid**) với các mục được sắp xếp theo cột.

---

### `ProgressBarScreen()`
- Minh họa **thanh tiến trình (ProgressBar)** dạng tròn và dạng thẳng với giá trị cập nhật động.

---

### `CardScreen()`
- Giới thiệu **thành phần Card** với tiêu đề, hình ảnh và nút mẫu.

---

### `RowLayoutScreen()`
- Minh họa **bố cục hàng ngang (Row)**: các phần tử được sắp xếp cạnh nhau.

---

### `ColumnLayoutScreen()`
- Minh họa **bố cục cột dọc (Column)**: các phần tử sắp xếp theo chiều dọc.

---

### `BoxScreen()`
- Minh họa **bố cục Box** để chồng lớp và căn chỉnh các phần tử.

---

### `SpacerScreen()`
- Minh họa **Spacer** để tạo khoảng cách giữa các phần tử trong bố cục.

---

### `IconButtonScreen()`
- Minh họa **nút biểu tượng (IconButton)** với các hành động như thích, chia sẻ, xóa, menu.

---

### `ScreenHeader()`
- Thanh tiêu đề chung cho các màn hình, hỗ trợ **nút quay lại (Back)** nếu cần.  
- Dùng `CenterAlignedTopAppBar` của Material3.

---

## **Hướng Dẫn Chạy Ứng Dụng**
1. **Yêu cầu môi trường**:
   - Android Studio phiên bản mới nhất.
   - Kotlin 1.9.0 hoặc cao hơn.
   - Jetpack Compose phiên bản 1.5.0 hoặc cao hơn.
   - Thêm các thư viện cần thiết trong `build.gradle`:
     ```gradle
     implementation "androidx.compose.material3:material3:1.3.0"
     implementation "androidx.navigation:navigation-compose:2.7.0"
     ```
2. **Cài đặt**:
   - Clone repository hoặc giải nén mã nguồn.
   - Mở project trong Android Studio.
   - Sync project với Gradle.
   - Chạy ứng dụng trên emulator hoặc thiết bị Android.

3. **Tài nguyên**:
   - Các hình ảnh sử dụng trong ứng dụng (ví dụ: `compose_logo.png`, `ut_hcm_building.png`, `ut_hcm_campus.png`) cần được thêm vào thư mục `res/drawable`.

---

## **Kết Luận**
Ứng dụng này cung cấp một cái nhìn tổng quan về các thành phần UI cơ bản trong Jetpack Compose, giúp người học nắm bắt cách sử dụng và xây dựng giao diện Android theo cách hiện đại, khai báo. Các màn hình được thiết kế trực quan, dễ tương tác và có thể mở rộng để thêm các tính năng khác.