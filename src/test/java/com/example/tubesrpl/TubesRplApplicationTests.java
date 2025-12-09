package com.example.tubesrpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.tubesrpl.controller.DosenController;
import com.example.tubesrpl.model.Tubes;
import com.example.tubesrpl.model.User;
import com.example.tubesrpl.repository.MatkulRepository;
import com.example.tubesrpl.repository.TahapRepository;
import com.example.tubesrpl.repository.TubesRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class TubesRplApplicationTests {
	@InjectMocks
	private DosenController dosenController; //class yang akan dites

	@Mock
	private TubesRepository tubesRepository; //dependencies yang dibutuhkan di class yang akan dites

	@Mock
	private MatkulRepository matkulRepository;

	@Mock
	private TahapRepository tahapRepository;

	private MockMvc mockMvc; //buat setup test environtment

	@BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(dosenController).build(); //bikin simulasi server spring dengan dosenController saja
    }

	@Test
	void contextLoads() {
	}


	//Test akses halaman course details yang dapat menambahkan atau memperbarui tubes
	@Test
	void courseDetailsAccesedByNonDosen() throws Exception{
		User mockUser = new User(); //buat user dengan role mahasiswa
    	mockUser.setRole("mahasiswa"); 

		mockMvc.perform(
			get("/dosen/course/details")
			.param("id", "1") //masukan param untuk "/dosen/course/details"
			.sessionAttr("user", mockUser)
		)
		.andExpect(status().is3xxRedirection()) //jika bukan dosen maka hasil yang diharapkan adalah no access dan di redirect
		.andExpect(redirectedUrl("/login"));
	}

	
	//Tes jika sukses menambahkan tugas besar
	@Test 
	void tryAddTubesSucess() throws Exception{
		mockMvc.perform( //simulasi buat tubes di mata kuliah ASD
			post("/dosen/course/create-api")
				.param("matkulId", "1")
				.param("namaTubes", "Tubes 1 - Algoritma")
				.param("deskripsi", "Implementasi struktur data dasar")
				.param("jmlKelompok", "8")
		)
		.andExpect(status().isOk()) //hasil yang diharapkan adalah status ok dengan body "Success"
		.andExpect(content().string("Success"));
	}
		
	//Tes jika gagal menambahkan tugas besar
	@Test
	void tryAddTubesFail() throws Exception {
		doThrow(new RuntimeException()).when(tubesRepository).createTubes(any(), any(), anyInt(), anyLong()); //nanti jika method tubesRepository createTubes() dipanggil, throw sebuah exception seakan-akan ada error pada server
		
		mockMvc.perform( //jalankan controller dengan param apa saja agar createTubes terpanggil
			post("/dosen/course/create-api")
				.param("namaTubes", "Tubes 1 - Algoritma")
				.param("deskripsi", "Test jika repo atau DB fail")
				.param("jmlKelompok", "8")
				.param("matkulId", "1")
		)
		.andExpect(status().isInternalServerError()) //hasil yang diharapkan adalah server error dengan body "Error"
		.andExpect(content().string("Error"));
	}

	//Tes jika input untuk menambahkan tugas besar tidak lengkap
	@Test 
	void tryAddTubesMissing() throws Exception {
		mockMvc.perform(
			post("/dosen/course/create-api")
				//parameter namaTubes tidak diisi
				.param("deskripsi", "Implementasi struktur data dasar")
				.param("jmlKelompok", "8")
				.param("matkulId", "1")
		)
		.andExpect(status().isInternalServerError()) //hasil yang diharapkan adalah server error dengan body "Error" karena createTubes butuh 4 param termasuk nameTubes
		.andExpect(content().string("Error"));
	}

	//Tes jika input melebihi constraint basis data
	@Test 
	void tryAddTubesMoreThanLimit() throws Exception {
		//jika createTubes() dipanggil, throw exception ini seolah-olah terjadi. Pelanggaran constraint biasanya DataIntegrityViolation
		//kenapa ditambahin: karena mock ga punya akses db beneran 
		doThrow(new DataIntegrityViolationException("Too long")).when(tubesRepository).createTubes(any(), any(), anyInt(), anyLong()); 

		mockMvc.perform(
			post("/dosen/course/create-api")
				.param("namaTubes", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis nat")
				.param("deskripsi", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec.")
				.param("jmlKelompok", "8")
				.param("matkulId", "1")
		)
		.andExpect(status().isInternalServerError()) //hasil yang diharapkan adalah server error dengan body "Error"
		.andExpect(content().string("Error"));
	}
}
