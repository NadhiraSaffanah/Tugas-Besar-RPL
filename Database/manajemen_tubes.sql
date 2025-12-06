-- =======================
-- DROP TABLES (RESET SAFE)
-- =======================
DROP TABLE IF EXISTS kelompok CASCADE;
DROP TABLE IF EXISTS matkul CASCADE;
DROP TABLE IF EXISTS matkul_semester CASCADE;
DROP TABLE IF EXISTS penilaian CASCADE;
DROP TABLE IF EXISTS semester CASCADE;
DROP TABLE IF EXISTS tahap_tubes CASCADE;
DROP TABLE IF EXISTS user_matkul CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS anggota_kelompok CASCADE;

-- =======================
-- USERS
-- =======================
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    nama VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('mahasiswa','dosen', 'admin')),
    npm VARCHAR(10) UNIQUE,
    no_induk VARCHAR(10) UNIQUE,
    foto_profil VARCHAR(255), 
	status_aktif VARCHAR(20) NOT NULL CHECK (status_aktif IN ('Aktif', 'non-Aktif'))
);

INSERT INTO users (nama, email, password_hash, role, npm, no_induk, foto_profil, status_aktif) VALUES
--DOSEN
('Dr. Andi Pratama', 'andi.pratama@kampus.edu', 'hashedpass', 'dosen', NULL, 'D001', NULL, 'Aktif'),
('Dr. Siti Lestari', 'siti.lestari@kampus.edu', 'hashedpass', 'dosen', NULL, 'D002', NULL, 'Aktif'),
('Prof. Budi Santoso', 'budi.santoso@kampus.edu', 'hashedpass', 'dosen', NULL, 'D003', NULL, 'Aktif'),
('Dr. Rina Kumala', 'rina.kumala@kampus.edu', 'hashedpass', 'dosen', NULL, 'D004', NULL, 'Aktif'),
('Dr. Joko Setiawan', 'joko.setiawan@kampus.edu', 'hashedpass', 'dosen', NULL, 'D005', NULL, 'Aktif'),
--MAHASISWA 
('Nadhira Saffanah', 'nadhira01@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230001', NULL, NULL, 'Aktif'),
('Rizki Ramadhan', 'rizki.ramadhan@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230002', NULL, NULL, 'Aktif'),
('Aulia Rahmadani', 'aulia.rahma@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230003', NULL, NULL, 'Aktif'),
('Fadhil Akbar', 'fadhil.akbar@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230004', NULL, NULL, 'Aktif'),
('Salsabila Putri', 'salsa.putri@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230005', NULL, NULL, 'Aktif'),
('Irfan Maulana', 'irfan.maulana@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230006', NULL, NULL, 'Aktif'),
('Dinda Maharani', 'dinda.maharani@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230007', NULL, NULL, 'Aktif'),
('Rafi Hidayat', 'rafi.hidayat@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230008', NULL, NULL, 'Aktif'),
('Aisyah Aulia', 'aisyah.aulia@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230009', NULL, NULL, 'Aktif'),
('Bagas Saputra', 'bagas.saputra@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230010', NULL, NULL, 'Aktif'),
('Putri Amelia', 'putri.amelia@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230011', NULL, NULL, 'Aktif'),
('Raka Pratama', 'raka.pratama@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230012', NULL, NULL, 'Aktif'),
('Aldeano Prakoso', 'aldeano.prakoso@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230013', NULL, NULL, 'Aktif'),
('Tasya Nurhaliza', 'tasya.nurhaliza@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230014', NULL, NULL, 'Aktif'),
('Rahmat Hidayat', 'rahmat.hidayat@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230015', NULL, NULL, 'Aktif'),
('Melati Ayuning', 'melati.ayuning@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230016', NULL, NULL, 'Aktif'),
('Dwiki Aditya', 'dwiki.aditya@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230017', NULL, NULL, 'Aktif'),
('Nurul Fadilah', 'nurul.fadilah@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230018', NULL, NULL, 'Aktif'),
('Gilang Pratama', 'gilang.pratama@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230019', NULL, NULL, 'Aktif'),
('Rani Putri', 'rani.putri@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230020', NULL, NULL, 'Aktif'),
('Kevin Saputra', 'kevin.saputra@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230021', NULL, NULL, 'Aktif'),
('Fauzan Hidayat', 'fauzan.hidayat@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230022', NULL, NULL, 'Aktif'),
('Dewi Lestari', 'dewi.lestari@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230023', NULL, NULL, 'Aktif'),
('Ardiansyah Putra', 'ardiansyah.putra@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230024', NULL, NULL, 'Aktif'),
('Siti Nurjanah', 'siti.nurjanah@mahasiswa.edu', 'hashedpass', 'mahasiswa', '230025', NULL, NULL, 'Aktif');


-- =======================
-- MATKUL 
-- =======================
CREATE TABLE matkul (
    id BIGSERIAL PRIMARY KEY,
    nama_matkul VARCHAR(100) NOT NULL,
	kelas_matkul VARCHAR(10)
);

INSERT INTO matkul (nama_matkul, kelas_matkul) VALUES
('Algoritma dan Struktur Data', 'A'),
('Algoritma dan Struktur Data', 'B'),
('Basis Data', 'A'),
('Basis Data', 'C'),
('Pemrograman Berorientasi Objek', 'A'),
('Pemrograman Berorientasi Objek', 'B'),
('Jaringan Komputer', 'A'),
('Sistem Operasi', 'A'),
('Sistem Operasi', 'B'),
('Rekayasa Perangkat Lunak', 'A');

-- =======================
-- SEMESTER 
-- =======================
CREATE TABLE semester (
    id BIGSERIAL PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
	jenis_semester VARCHAR(20) NOT NULL CHECK (jenis_semester IN ('ganjil','genap')),
	status_aktif VARCHAR(20) NOT NULL CHECK (status_aktif IN ('Aktif', 'non-Aktif'))
);

INSERT INTO semester (start_date, end_date, jenis_semester, status_aktif) VALUES 
('2023-08-01', '2023-12-31', 'ganjil', 'Aktif'),
('2024-02-01', '2024-06-30', 'genap', 'Aktif'),
('2024-08-01', '2024-12-31', 'ganjil', 'Aktif'),
('2025-02-01', '2025-06-30', 'genap', 'Aktif');

-- ==============================================
-- MATKUL-SEMESTER (many-to-many matkul-semester)
-- ==============================================
CREATE TABLE matkul_semester (
    semester_id BIGINT NOT NULL REFERENCES semester(id) ON DELETE CASCADE,
    matkul_id BIGINT NOT NULL REFERENCES matkul(id) ON DELETE CASCADE,
    PRIMARY KEY (semester_id, matkul_id)
);

INSERT INTO matkul_semester VALUES 
-- Semester 1
(1, 1),
(1, 2),
(1, 3),
(1, 4),
-- Semester 2
(2, 5),
(2, 6),
(2, 7),
-- Semester 3
(3, 8),
(3, 9),
-- Semester 4
(4, 10);

-- ======================================
-- USER-MATKUL (many-to-many user-matkul) 
-- ======================================
CREATE TABLE user_matkul (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    matkul_id BIGINT NOT NULL REFERENCES matkul(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, matkul_id)
);

INSERT INTO user_matkul VALUES 
-- DOSEN
(1, 1), (1, 2),
(2, 3), (2, 4),
(3, 5), (3, 6),
(4, 7), (4, 8),
(5, 9), (5, 10),
--MAHASISWA 
(6, 1), (6, 3), (6, 5),
(7, 1), (7, 2),
(8, 3), (8, 4),
(9, 5), (9, 7),
(10, 2), (10, 8),
(11, 1), (11, 6), (11, 9),
(12, 4), (12, 7),
(13, 3), (13, 8),
(14, 1), (14, 10),
(15, 2), (15, 6),
(16, 5), (16, 9),
(17, 7), (17, 10),
(18, 3), (18, 6),
(19, 4), (19, 8),
(20, 2), (20, 9),
(21, 1), (21, 5),
(22, 3), (22, 7),
(23, 4), (23, 6),
(24, 8), (24, 10),
(25, 5), (25, 9),
(26, 1), (26, 3),
(27, 2), (27, 4),
(28, 6), (28, 8),
(29, 7), (29, 10),
(30, 1), (30, 9);

-- ======================================
-- TUBES  
-- ======================================
CREATE TABLE tubes (
    id BIGSERIAL PRIMARY KEY,
    nama_tubes VARCHAR(100) NOT NULL,
    deskripsi VARCHAR(255),
	jml_kelompok BIGINT,
	matkul_id BIGINT NOT NULL REFERENCES matkul(id) ON DELETE CASCADE
);

INSERT INTO tubes (nama_tubes, deskripsi, jml_kelompok, matkul_id) VALUES 
('Tubes 1 - Algoritma', 'Implementasi struktur data dasar', 8, 1),
('Tubes 2 - Algoritma', 'Penerapan linked list dan stack', 6, 1),
('Tubes Basis Data', 'Perancangan ERD dan normalisasi tabel', 10, 3),
('Mini Project SQL', 'Membuat query CRUD dan relasi', 7, 3),
('Tubes PBO', 'Membuat sistem OOP dengan inheritance', 5, 5),
('Project Final PBO', 'Aplikasi console dengan design pattern', 6, 5),
('Tubes Jaringan', 'Simulasi jaringan menggunakan Cisco PT', 4, 7),
('Tugas Sistem Operasi', 'Simulasi penjadwalan proses', 7, 8),
('Project Final Sistem Operasi', 'Implementasi algoritma deadlock', 6, 8),
('Tubes RPL', 'Dokumentasi UML dan class diagram', 9, 10),
('Final Project RPL', 'Build aplikasi sederhana berbasis tim', 8, 10),
('Tubes Pemrograman Web', 'Website CRUD dengan routing dan template', 10, 9);

-- ======================================
-- KELOMPOK
-- ======================================
CREATE TABLE kelompok (
	id BIGSERIAL PRIMARY KEY,
    nama_kelompok VARCHAR(100) NOT NULL,
	jml_anggota BIGINT,
	tubes_id BIGINT NOT NULL REFERENCES tubes(id) ON DELETE CASCADE,
	UNIQUE (nama_kelompok, tubes_id)
);

INSERT INTO kelompok (nama_kelompok, jml_anggota, tubes_id) VALUES 
('Kelompok A', 5, 1),
('Kelompok B', 5, 1),
('Kelompok A', 6, 2),
('Kelompok B', 6, 2),
('Kelompok A', 7, 3),
('Kelompok B', 7, 3),
('Kelompok A', 4, 4),
('Kelompok B', 4, 4),
('Kelompok A', 6, 5),
('Kelompok B', 6, 5),
('Kelompok A', 5, 6),
('Kelompok B', 5, 6),
('Kelompok A', 3, 7),
('Kelompok A', 6, 8),
('Kelompok B', 6, 8);

-- ======================================
-- TAHAP TUBES
-- ======================================
CREATE TABLE tahap_tubes (
	id BIGSERIAL PRIMARY KEY,
    nama_tahap VARCHAR(100) NOT NULL,
	deskripsi VARCHAR(255),
	rubrik_penilaian VARCHAR(255), 
	tanggal_akhir DATE NOT NULL, 
	status_penilaian VARCHAR(20) NOT NULL CHECK (status_penilaian IN ('Graded', 'Not Graded')) DEFAULT 'Not Graded',   -- default value
	tubes_id BIGINT NOT NULL REFERENCES tubes(id) ON DELETE CASCADE,
	status_visibility_nilai VARCHAR(20) NOT NULL CHECK (status_visibility_nilai IN ('Hide', 'Unhide')) DEFAULT 'Unhide'          -- default value
);

INSERT INTO tahap_tubes (nama_tahap, deskripsi, rubrik_penilaian, tanggal_akhir, status_penilaian, tubes_id) VALUES 
('Tahap 1 - Proposal', 
 'Pengumpulan ide dan rancangan awal.', 
 'Penilaian berdasarkan kelengkapan proposal dan kejelasan ide.', 
 '2025-03-10', 
 1),

('Tahap 2 - Desain Sistem', 
 'Pembuatan diagram class, flow, dan arsitektur.', 
 'Penilaian berdasarkan ketepatan desain dan kesesuaian requirement.', 
 '2025-03-20', 
 1),

('Tahap 3 - Implementasi', 
 'Pengembangan kode sesuai desain yang telah disetujui.', 
 'Penilaian berdasarkan kualitas kode dan pemenuhan fitur.', 
 '2025-04-05', 
 1),

 ('Tahap 1 - Proposal', 
 'Pengumpulan ide dan rancangan awal.', 
 'Penilaian berdasarkan kelengkapan proposal dan kejelasan ide.', 
 '2025-05-10', 
 2),

('Tahap 2 - Desain Sistem', 
 'Pembuatan diagram class, flow, dan arsitektur.', 
 'Penilaian berdasarkan ketepatan desain dan kesesuaian requirement.', 
 '2025-05-20',  
 2),

('Tahap 3 - Implementasi', 
 'Pengembangan kode sesuai desain yang telah disetujui.', 
 'Penilaian berdasarkan kualitas kode dan pemenuhan fitur.', 
 '2025-06-05',  
 2);

-- ======================================
-- ANGGOTA-KELOMPOK
-- ======================================
CREATE TABLE anggota_kelompok (
    kelompok_id BIGINT NOT NULL REFERENCES kelompok(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, kelompok_id)
);

INSERT INTO anggota_kelompok VALUES 
-- Kelompok 1
(1, 6),
(1, 7),
(1, 8),
(1, 9),
(1, 10),
-- Kelompok 2
(2, 11),
(2, 12),
(2, 13),
(2, 14),
(2, 15),
-- Kelompok 3
(3, 16),
(3, 17),
(3, 18),
(3, 19),
(3, 20),
-- Kelompok 4
(4, 21),
(4, 22),
(4, 23),
(4, 24),
(4, 25),
-- Kelompok 5
(5, 6),
(5, 11),
(5, 16),
(5, 21),
(5, 26);

-- ======================================
-- PENILAIAN
-- ======================================
CREATE TABLE penilaian (
	nilai NUMERIC(5,2),
	komentar VARCHAR(255),
	tahap_id BIGINT NOT NULL REFERENCES tahap_tubes(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, tahap_id)
);

INSERT INTO penilaian (nilai, komentar, tahap_id, user_id) VALUES 
-- Tahap 1 (10 mahasiswa dinilai)
(85.50, 'Good job', 1, 6),
(78.00, 'Needs improvement', 1, 7),
(92.00, 'Excellent', 1, 8),
(88.25, 'Great work', 1, 9),
(74.50, 'Cukup', 1, 10),
(90.00, 'Nice', 1, 11),
(81.25, 'Good effort', 1, 12),
(76.00, 'Perlu revisi', 1, 14),
(89.00, 'Mantap', 1, 15),
(84.75, 'Good job', 1, 16),
-- Tahap 2 (8 mahasiswa)
(88.00, 'Good job', 2, 6),
(80.25, 'Improved', 2, 7),
(94.50, 'Excellent', 2, 8),
(85.25, 'Nice', 2, 9),
(72.00, 'Cukup', 2, 10),
(83.25, 'Good effort', 2, 12),
(90.00, 'Mantap', 2, 15),
(87.75, 'Nice', 2, 21),
-- Tahap 3 (12 mahasiswa)
(90.00, 'Great design', 3, 6),
(82.50, 'Improved well', 3, 7),
(95.00, 'Excellent', 3, 8),
(87.75, 'Nice structure', 3, 9),
(73.50, 'Could be better', 3, 10),
(93.00, 'Good', 3, 11),
(84.25, 'Solid', 3, 12),
(97.00, 'Outstanding', 3, 13),
(79.50, 'Perlu detail', 3, 14),
(89.75, 'Nice', 3, 15),
(88.25, 'Bagus', 3, 16),
(82.75, 'Ok', 3, 17),
-- Tahap 4 (6 mahasiswa)
(91.00, 'Good presentation', 4, 6),
(83.00, 'Improved speaking', 4, 7),
(96.00, 'Excellent', 4, 8),
(89.50, 'Clear delivery', 4, 9),
(94.00, 'Great job', 4, 11),
(85.25, 'Nice slides', 4, 12);

-- ======================================
-- NOTIFIKASI
-- ======================================
CREATE TABLE notifikasi (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    tipe VARCHAR(50),
    konten TEXT,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT NOW()
);