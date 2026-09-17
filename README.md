# Gender Health Care Platform

🌐 **Live Demo**: [http://14.225.192.15/](http://14.225.192.15/)

## 📋 Tổng quan dự án

**Gender Health Care Platform** là một hệ thống quản lý chăm sóc sức khỏe giới tính toàn diện, được phát triển bằng Spring Boot. Hệ thống cung cấp các dịch vụ y tế chuyên biệt về sức khỏe sinh sản, tư vấn trực tuyến, xét nghiệm STI và theo dõi chu kỳ kinh nguyệt.

## 🎯 Mục tiêu chính

- **Quản lý cuộc hẹn y tế**: Đặt lịch khám bệnh, tư vấn trực tuyến và xét nghiệm
- **Hồ sơ y tế điện tử**: Lưu trữ và quản lý thông tin sức khỏe bệnh nhân
- **Tư vấn trực tuyến**: Hệ thống chat và video call với bác sĩ
- **Theo dõi chu kỳ**: Ứng dụng theo dõi chu kỳ kinh nguyệt và sức khỏe sinh sản
- **Quản lý thanh toán**: Tích hợp thanh toán VNPay và tiền mặt
- **Blog y tế**: Chia sẻ kiến thức và thông tin sức khỏe

## 🏗️ Kiến trúc hệ thống

### Tech Stack
- **Backend**: Spring Boot 3.5.0, Java 21
- **Database**: MySQL 8.0
- **Security**: Spring Security + JWT
- **Documentation**: Swagger/OpenAPI 3
- **Real-time**: WebSocket (STOMP)
- **Payment**: VNPay, tiền mặt
- **Cloud Storage**: Cloudinary
- **Video Call**: Zoom API
- **Email**: Spring Mail + Thymeleaf

### Cấu trúc dự án
```
src/main/java/com/S_Health/GenderHealthCare/
├── api/                    # REST Controllers
├── config/                 # Configuration classes
├── controller/             # WebSocket controllers
├── dto/                    # Data Transfer Objects
├── entity/                 # JPA Entities
├── enums/                  # Enumerations
├── exception/              # Exception handling
├── repository/             # Data repositories
├── service/                # Business logic
└── utils/                  # Utility classes
```

## 📊 Mô hình dữ liệu chính

### Core Entities

#### 1. User Management
- **User**: Quản lý người dùng (Customer, Doctor, Staff, Admin)
- **UserRole**: CUSTOMER, DOCTOR, STAFF, ADMIN
- **Specialization**: Chuyên khoa y tế

#### 2. Appointment System
- **Appointment**: Cuộc hẹn chính
- **AppointmentDetail**: Chi tiết dịch vụ trong cuộc hẹn
- **Service**: Dịch vụ y tế (Tư vấn, Xét nghiệm, Điều trị, Combo)
- **Room**: Phòng khám
- **Schedule**: Lịch làm việc của bác sĩ

#### 3. Medical Records
- **MedicalProfile**: Hồ sơ y tế cơ bản
- **MedicalResult**: Kết quả khám bệnh và xét nghiệm
- **TreatmentProtocol**: Phác đồ điều trị

#### 4. Communication
- **ChatSession**: Phiên chat tư vấn
- **ChatMessage**: Tin nhắn chat
- **Notification**: Thông báo hệ thống

#### 5. Content & Feedback
- **Blog**: Bài viết y tế
- **Comment**: Bình luận blog
- **Tag**: Thẻ phân loại
- **ServiceFeedback**: Đánh giá dịch vụ

#### 6. Health Tracking
- **CycleTracking**: Theo dõi chu kỳ kinh nguyệt
- **Symptoms**: Triệu chứng sức khỏe

#### 7. Payment System
- **Payment**: Thanh toán
- **Transaction**: Giao dịch
- **PaymentMethod**: VNPAY, CASH

## 🔧 Chức năng chính

### 1. Hệ thống đặt lịch hẹn
- Đặt lịch khám bệnh trực tiếp
- Tư vấn trực tuyến qua video call
- Đặt lịch xét nghiệm
- Quản lý combo dịch vụ
- Tự động phân phòng theo chuyên khoa

### 2. Quản lý hồ sơ y tế
- Thông tin cơ bản: dị ứng, tiền sử bệnh
- Kết quả khám bệnh và xét nghiệm
- Lịch sử điều trị
- Phác đồ điều trị

### 3. Hệ thống tư vấn trực tuyến
- Chat real-time với bác sĩ
- Video call qua Zoom
- Quản lý phiên tư vấn
- Lưu trữ lịch sử tư vấn

### 4. Theo dõi sức khỏe sinh sản
- Theo dõi chu kỳ kinh nguyệt
- Ghi nhận triệu chứng
- Dự đoán chu kỳ tiếp theo
- Thông báo nhắc nhở

### 5. Hệ thống thanh toán
- Thanh toán VNPay
- Thanh toán tiền mặt
- Quản lý giao dịch

### 6. Blog và chia sẻ kiến thức
- Đăng bài viết y tế
- Hệ thống bình luận
- Phân loại theo tags
- Thống kê lượt xem

## 🚀 API Endpoints chính

### Authentication
- `POST /api/login` - Đăng nhập
- `POST /api/register` - Đăng ký
- `POST /api/google-login` - Đăng nhập Google

### Booking & Appointments
- `POST /api/booking/medicalService` - Đặt lịch hẹn
- `GET /api/appointment/my-appointments` - Lịch hẹn của tôi
- `PUT /api/appointment/{id}/status` - Cập nhật trạng thái

### Medical Services
- `GET /api/services` - Danh sách dịch vụ
- `GET /api/consultants/by-service/{serviceId}` - Bác sĩ theo dịch vụ
- `POST /api/result/consultation` - Nhập kết quả khám

### Chat & Communication
- `POST /api/chat/start` - Bắt đầu chat
- `WebSocket /topic/chat/{sessionId}` - Real-time chat

### Health Tracking
- `POST /api/cycle-track/log` - Ghi nhận chu kỳ
- `GET /api/cycle-track/logs` - Lịch sử theo dõi

### Payment
- `POST /api/payment/vnpay/create` - Tạo thanh toán VNPay

## 🔐 Bảo mật

### Authentication & Authorization
- **JWT Token**: Xác thực người dùng
- **Role-based Access**: Phân quyền theo vai trò
- **Password Encryption**: BCrypt
- **OAuth2**: Google Login

### Security Features
- CORS configuration
- Request validation
- SQL injection prevention
- XSS protection

## 📱 Tích hợp bên ngoài

### Payment Gateways
- **VNPay**: Cổng thanh toán

### Communication
- **Zoom API**: Video conferencing
- **Gmail SMTP**: Email notifications

### Storage
- **Cloudinary**: Image/file storage
- **MySQL**: Primary database

## 🛠️ Cài đặt và triển khai

### Yêu cầu hệ thống
- Java 21+
- MySQL 8.0+
- Maven 3.6+

## 📈 Tính năng nổi bật

### 1. Workflow đặt lịch thông minh
- Tự động phân phòng theo chuyên khoa
- Kiểm tra xung đột lịch hẹn
- Hỗ trợ combo dịch vụ

### 2. Hệ thống y tế toàn diện
- Quản lý hồ sơ bệnh án
- Theo dõi kết quả xét nghiệm
- Phác đồ điều trị cá nhân hóa

### 3. Tư vấn trực tuyến hiện đại
- Chat real-time
- Video call chất lượng cao
- Lưu trữ lịch sử tư vấn

### 4. Theo dõi sức khỏe cá nhân
- Chu kỳ kinh nguyệt
- Triệu chứng sức khỏe
- Nhắc nhở thông minh

## 👥 Đối tượng sử dụng

### Khách hàng (Customer)
- Đặt lịch khám bệnh
- Tư vấn trực tuyến
- Theo dõi sức khỏe
- Xem kết quả khám

### Bác sĩ (Doctor)
- Quản lý lịch khám
- Nhập kết quả khám bệnh
- Xem hồ sơ bệnh nhân

### Nhân viên (Staff)
- Check-in bệnh nhân
- Quản lý lịch hẹn
- Hỗ trợ khách hàng
  
### Quản trị viên (Admin)
- Quản lý người dùng
- Cấu hình hệ thống
- Báo cáo thống kê
- Quản lý nội dung

## 📊 Báo cáo và thống kê

- Thống kê đặt lịch theo thời gian
- Báo cáo doanh thu
- Phân tích hiệu suất dịch vụ
- Thống kê người dùng

## 🔄 Quy trình làm việc

### Quy trình đặt lịch
1. Khách hàng chọn dịch vụ
2. Chọn bác sĩ và thời gian
3. Hệ thống tự động phân phòng
4. Xác nhận và thanh toán
5. Nhận thông báo xác nhận

### Quy trình khám bệnh
1. Check-in tại quầy lễ tân
2. Khám bệnh với bác sĩ
3. Nhập kết quả khám
4. Cập nhật hồ sơ y tế
5. Hoàn thành cuộc hẹn

## 🎯 Tầm nhìn tương lai

- Tích hợp AI cho chẩn đoán hỗ trợ
- Mở rộng dịch vụ telemedicine
- Ứng dụng mobile native
- Tích hợp IoT devices
- Blockchain cho bảo mật dữ liệu y tế
