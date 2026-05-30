# Library Management Swing

## De tai

Xay dung ung dung quan ly muon tra sach thu vien bang Java Swing.

Ung dung ho tro thu thu hoac nguoi quan ly thu vien thuc hien cac chuc nang co ban:

- Quan ly danh sach sach.
- Quan ly nguoi muon sach.
- Lap phieu muon sach.
- Ghi nhan tra sach.
- Tim kiem sach va nguoi muon.
- Thong ke so luong sach va phieu muon.
- Luu va doc du lieu bang file text.

## Thanh vien nhom

| Thanh vien | Vai tro chinh |
|---|---|
| Nguyen Trung Dung | Model, quan ly sach, quan ly nguoi muon |
| Nguyen Cao Thang | Muon/tra sach, File I/O, xu ly ngoai le |
| Nguyen Dang Khoa | Java Swing GUI, testing, bao cao |

## Phan cong cong viec

| Thanh vien | File/chuc nang phu trach | Mo ta |
|---|---|---|
| Nguyen Trung Dung | `Searchable.java` | Interface dung cho cac doi tuong co kha nang tim kiem. |
| Nguyen Trung Dung | `Book.java` | Lop dai dien cho sach, quan ly so luong va tim kiem sach. |
| Nguyen Trung Dung | `Person.java` | Lop cha chua thong tin chung cua con nguoi. |
| Nguyen Trung Dung | `Reader.java` | Lop dai dien cho nguoi muon sach, ke thua tu `Person`. |
| Nguyen Trung Dung | Mot phan `LibraryManager.java` | Them, sua, xoa, tim kiem sach va nguoi muon. |
| Nguyen Cao Thang | `BorrowRecord.java` | Lop dai dien cho phieu muon sach. |
| Nguyen Cao Thang | Mot phan `LibraryManager.java` | Xu ly muon sach, tra sach va thong ke lien quan. |
| Nguyen Cao Thang | `FileHandler.java` | Luu va doc danh sach sach, nguoi muon, phieu muon tu file. |
| Nguyen Cao Thang | Cac file test File I/O | Kiem tra doc/ghi du lieu bang console. |
| Nguyen Dang Khoa | `MainFrame.java` | Giao dien Java Swing gom cac tab Sach, Nguoi muon, Muon/Tra, Thong ke. |
| Nguyen Dang Khoa | `Main.java` | Diem bat dau chuong trinh, load du lieu va mo giao dien. |
| Nguyen Dang Khoa | Tai lieu testing va bao cao | Tong hop test case, anh demo va noi dung bao cao. |

## Cau truc thu muc

```text
library-management-swing/
├── README.md
├── data/
│   ├── books.txt
│   ├── readers.txt
│   └── borrow_records.txt
└── src/
    ├── app/
    │   └── Main.java
    ├── model/
    │   ├── Book.java
    │   ├── BorrowRecord.java
    │   ├── Person.java
    │   └── Reader.java
    ├── service/
    │   └── LibraryManager.java
    ├── storage/
    │   └── FileHandler.java
    ├── test/
    │   ├── TestBookFileIO.java
    │   ├── TestBorrowRecordFileIO.java
    │   ├── TestLibraryManager.java
    │   └── TestReaderFileIO.java
    ├── ui/
    │   └── MainFrame.java
    └── util/
        └── Searchable.java
```

## Cong nghe su dung

- Ngon ngu: Java
- Giao dien: Java Swing
- Luu tru du lieu: File text
- Kiem thu: Console test va test thu cong tren giao dien
- Cong cu co the dung: IntelliJ IDEA, Eclipse, NetBeans hoac terminal voi `javac`

## Chuc nang chinh

### Quan ly sach

- Them sach moi.
- Sua thong tin sach.
- Xoa sach khi sach khong dang duoc muon.
- Tim kiem sach theo ma, ten, tac gia, the loai.
- Quan ly tong so luong va so luong sach con lai.

### Quan ly nguoi muon

- Them nguoi muon.
- Sua thong tin nguoi muon.
- Xoa nguoi muon khi khong co phieu muon dang mo.
- Tim kiem nguoi muon theo ma, ten, so dien thoai, email, dia chi.

### Muon va tra sach

- Tao phieu muon cho nguoi muon va sach duoc chon.
- Kiem tra sach con so luong truoc khi muon.
- Giam so luong sach con lai khi muon thanh cong.
- Ghi nhan ngay tra va trang thai da tra.
- Tang lai so luong sach con lai khi tra thanh cong.
- Khong cho tra mot phieu muon nhieu lan.

### Luu va doc file

- Luu danh sach sach vao `data/books.txt`.
- Luu danh sach nguoi muon vao `data/readers.txt`.
- Luu danh sach phieu muon vao `data/borrow_records.txt`.
- Tu dong tra ve danh sach rong neu file chua ton tai trong lan chay dau.
- Bo qua dong du lieu sai dinh dang khi doc file.

### Thong ke

- Tong so dau sach.
- Tong so ban sach.
- So sach dang duoc muon.
- So phieu muon chua tra.

## Cach chay chuong trinh

### Cach 1: Chay bang IDE

1. Mo project bang IntelliJ IDEA, Eclipse hoac NetBeans.
2. Dam bao thu muc `src` duoc cau hinh la source folder.
3. Mo file `src/app/Main.java`.
4. Chay method `main`.

### Cach 2: Chay bang terminal

Bien dich source code:

```bash
javac -d out $(find src -name "*.java")
```

Chay ung dung:

```bash
java -cp out app.Main
```

## Cach chay test console

Sau khi bien dich:

```bash
java -cp out app.TestLibraryManager
java -cp out app.TestBookFileIO
java -cp out app.TestReaderFileIO
java -cp out app.TestBorrowRecordFileIO
```

## Ghi chu khi demo

- Neu chay lan dau chua co thu muc `data`, chuong trinh van co the mo binh thuong.
- Khi them, sua, xoa, muon hoac tra sach, du lieu se duoc luu lai vao file.
- Khong nen nhap ky tu `;` trong du lieu vi file text dang dung dau `;` de tach cot.
- Neu muon test lai tu dau, co the sao luu hoac xoa du lieu trong thu muc `data`.

## Noi dung OOP the hien trong do an

- Encapsulation: cac thuoc tinh trong model duoc dat `private` va truy cap qua getter/setter.
- Inheritance: `Reader` ke thua tu `Person`.
- Polymorphism: `Reader` override `getDisplayInfo`.
- Interface: `Book` va `Reader` implements `Searchable`.
- Exception handling: xu ly loi nhap lieu, loi muon/tra va loi doc/ghi file.
- Separation of concerns: tach rieng `model`, `service`, `storage`, `ui`, `app`.
