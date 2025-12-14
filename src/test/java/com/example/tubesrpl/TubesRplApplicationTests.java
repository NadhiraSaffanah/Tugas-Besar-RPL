// package com.example.tubesrpl;

// // mockito & mockmvc
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyInt;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.Mockito.doThrow;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
// import org.springframework.test.context.bean.override.mockito.MockitoBean; // Perbaikan import
// import org.springframework.dao.DataIntegrityViolationException;
// import org.springframework.test.web.servlet.MockMvc;

// // model & repo
// import com.example.tubesrpl.model.User;
// import com.example.tubesrpl.repository.MatkulRepository;
// import com.example.tubesrpl.repository.TahapRepository;
// import com.example.tubesrpl.repository.TubesRepository;

// @SpringBootTest
// @AutoConfigureMockMvc // jadinya pake AutoConfigure, @ExtendWith dihapus
// class TubesRplApplicationTests {

//     @Autowired
//     private MockMvc mockMvc;

//     // semua yang Mock gw ganti MockitoBean, entah kenapa Mock gabisa di laptop gw

//     @MockitoBean
//     private TubesRepository tubesRepository;

//     @MockitoBean
//     private MatkulRepository matkulRepository;

//     @MockitoBean
//     private TahapRepository tahapRepository;

//     @Test
//     void contextLoads() { 
//     }

//     //Test akses halaman course details yang dapat menambahkan atau memperbarui tubes
//     @Test
//     void courseDetailsAccesedByNonDosen() throws Exception {
//         User mockUser = new User();  //buat user dengan role mahasiswa
//         mockUser.setRole("mahasiswa"); // Set role bukan dosen

//         mockMvc.perform(
//             get("/dosen/course/details")
//             .param("id", "1") //masukan param untuk "/dosen/course/details"
//             .sessionAttr("user", mockUser)
//         )
//         .andExpect(status().is3xxRedirection()) //jika bukan dosen maka hasil yang diharapkan adalah no access dan di redirect
//         .andExpect(redirectedUrl("/login"));
//     }

//     //Tes jika sukses menambahkan tugas besar
//     @Test 
//     void tryAddTubesSucess() throws Exception {
//         mockMvc.perform( //simulasi buat tubes di mata kuliah ASD
//             post("/dosen/course/create-api")
//                 .param("matkulId", "1")
//                 .param("namaTubes", "Tubes 1 - Algoritma")
//                 .param("deskripsi", "Implementasi struktur data dasar")
//                 .param("jmlKelompok", "8")
//         )
//         .andExpect(status().isOk()) //hasil yang diharapkan adalah status ok dengan body "Success"
//         .andExpect(content().string("Success"));
//     }
        
//     //Tes jika gagal menambahkan tugas besar
//     @Test
//     void tryAddTubesFail() throws Exception {
//         //nanti jika method tubesRepository createTubes() dipanggil, throw sebuah exception seakan-akan ada error pada server
//         doThrow(new RuntimeException("DB Error")).when(tubesRepository).createTubes(any(), any(), anyInt(), anyLong());
        
//         mockMvc.perform( //jalankan controller dengan param apa saja agar createTubes terpanggil
//             post("/dosen/course/create-api")
//                 .param("namaTubes", "Tubes 1 - Algoritma")
//                 .param("deskripsi", "Test jika repo atau DB fail")
//                 .param("jmlKelompok", "8")
//                 .param("matkulId", "1")
//         )
//         .andExpect(status().isInternalServerError()) //hasil yang diharapkan adalah server error dengan body "Error"
//         .andExpect(content().string("Error"));
//     }

//     //Tes jika input untuk menambahkan tugas besar tidak lengkap
//     @Test 
//     void tryAddTubesMissing() throws Exception {
//         mockMvc.perform(
//             post("/dosen/course/create-api")
//                 // Param 'namaTubes' SENGAJA DIHILANGKAN
//                 .param("deskripsi", "Implementasi struktur data dasar")
//                 .param("jmlKelompok", "8")
//                 .param("matkulId", "1")
//         )
//         // PERBAIKAN: kalo parameter kurang, Spring returnnya 400 (Bad Request), ga jadi error 500
//         .andExpect(status().isBadRequest()); 
//     }

//     //Tes jika input melebihi constraint basis data
//     @Test 
//     void tryAddTubesMoreThanLimit() throws Exception {
//         //jika createTubes() dipanggil, throw exception ini seolah-olah terjadi. Pelanggaran constraint biasanya DataIntegrityViolation
// 		//kenapa ditambahin: karena mock ga punya akses db beneran
//         doThrow(new DataIntegrityViolationException("Too long"))
//             .when(tubesRepository)
//             .createTubes(any(), any(), anyInt(), anyLong());

//         mockMvc.perform(
//             post("/dosen/course/create-api")
//                 .param("namaTubes", "Lorem ipsum dolor sit amet panjang banget...") 
//                 .param("deskripsi", "Deskripsi panjang...")
//                 .param("jmlKelompok", "8")
//                 .param("matkulId", "1")
//         )
//         .andExpect(status().isInternalServerError()) //hasil yang diharapkan adalah server error dengan body "Error"
//         .andExpect(content().string("Error"));
//     }
// }
