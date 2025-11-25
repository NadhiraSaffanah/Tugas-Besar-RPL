--
-- PostgreSQL database dump
--

\restrict QJ8nxVsWwqPUkToyTqEQGGEfPOVeASyrccLhKFL21HlffQaTSNobTRfvydXcyUa

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

-- Started on 2025-11-25 20:11:56

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 234 (class 1259 OID 16609)
-- Name: AnggotaKelompok; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."AnggotaKelompok" (
    idanggota_kelompok integer CONSTRAINT "AnggotaKelompok_idAnggotaKelompok_not_null" NOT NULL,
    idkelompok integer CONSTRAINT "AnggotaKelompok_idKelompok _not_null" NOT NULL,
    userid integer NOT NULL
);


ALTER TABLE public."AnggotaKelompok" OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 16608)
-- Name: AnggotaKelompok_idAnggotaKelompok_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."AnggotaKelompok" ALTER COLUMN idanggota_kelompok ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."AnggotaKelompok_idAnggotaKelompok_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 231 (class 1259 OID 16523)
-- Name: matkul_semester; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.matkul_semester (
    idmatkul_semester integer CONSTRAINT "MatkulSemester _idMatkulSemester_not_null" NOT NULL,
    idmatkul integer CONSTRAINT "MatkulSemester _idMatkul _not_null" NOT NULL,
    idsemester integer CONSTRAINT "MatkulSemester _idSemester _not_null" NOT NULL
);


ALTER TABLE public.matkul_semester OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 16522)
-- Name: MatkulSemester _idMatkulSemester_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.matkul_semester ALTER COLUMN idmatkul_semester ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."MatkulSemester _idMatkulSemester_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 225 (class 1259 OID 16488)
-- Name: penilaian; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.penilaian (
    idpenilaian integer CONSTRAINT "Penilaian_idPenilaian _not_null" NOT NULL,
    nilai double precision,
    komentar character varying(255),
    iduser integer,
    "idTahap" integer
);


ALTER TABLE public.penilaian OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 16487)
-- Name: Penilaian_idPenilaian _seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.penilaian ALTER COLUMN idpenilaian ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Penilaian_idPenilaian _seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 227 (class 1259 OID 16495)
-- Name: semester; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.semester (
    idsemester integer CONSTRAINT "Semester _idSemester _not_null" NOT NULL,
    startdate date CONSTRAINT "Semester _startDate_not_null" NOT NULL,
    enddate date CONSTRAINT "Semester _endDate_not_null" NOT NULL,
    jenis_semester character varying(10) CONSTRAINT "Semester _jenisSemester _not_null" NOT NULL
);


ALTER TABLE public.semester OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 16494)
-- Name: Semester _idSemester _seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.semester ALTER COLUMN idsemester ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Semester _idSemester _seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 221 (class 1259 OID 16460)
-- Name: tubes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tubes (
    idtubes integer CONSTRAINT "Tubes_idTubes_not_null" NOT NULL,
    deskripsi character varying(255),
    jmlkelompok integer CONSTRAINT "Tubes_jmlKelompok_not_null" NOT NULL,
    idmatkul integer
);


ALTER TABLE public.tubes OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 16541)
-- Name: Tubes_idTubes_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.tubes ALTER COLUMN idtubes ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Tubes_idTubes_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 222 (class 1259 OID 16468)
-- Name: kelompok; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.kelompok (
    idkelompok integer CONSTRAINT "Kelompok_idKelompok_not_null" NOT NULL,
    nama character varying(50) CONSTRAINT "Kelompok_nama_not_null" NOT NULL,
    "jmlAnggota" integer CONSTRAINT "Kelompok_jmlAnggota_not_null" NOT NULL,
    idtubes integer
);


ALTER TABLE public.kelompok OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 16685)
-- Name: kelompok_idkelompok_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.kelompok ALTER COLUMN idkelompok ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.kelompok_idkelompok_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 220 (class 1259 OID 16452)
-- Name: matkul; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.matkul (
    idmatkul integer CONSTRAINT "Matkul _idMatkul _not_null" NOT NULL,
    nama character varying(50) CONSTRAINT "Matkul _nama _not_null" NOT NULL,
    kelas character varying(10) CONSTRAINT "Matkul _kelas _not_null" NOT NULL
);


ALTER TABLE public.matkul OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 16639)
-- Name: matkul_idmatkul_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.matkul ALTER COLUMN idmatkul ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.matkul_idmatkul_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 223 (class 1259 OID 16476)
-- Name: tahap_tubes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tahap_tubes (
    "idTahap" integer CONSTRAINT "TahapTubes_idTahap_not_null" NOT NULL,
    nama character varying(50) CONSTRAINT "TahapTubes_nama _not_null" NOT NULL,
    deskripsi character varying(255),
    rubrik character varying(255),
    tanggal date CONSTRAINT "TahapTubes_tanggal _not_null" NOT NULL,
    status_penilaian character varying(50) CONSTRAINT "TahapTubes_statusPenilaian_not_null" NOT NULL,
    idtubes integer
);


ALTER TABLE public.tahap_tubes OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 16674)
-- Name: tahap_tubes_idTahap_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.tahap_tubes ALTER COLUMN "idTahap" ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."tahap_tubes_idTahap_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 229 (class 1259 OID 16505)
-- Name: user_matkul; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_matkul (
    iduser_matkul integer CONSTRAINT "userMatkul _idUserMatkul _not_null" NOT NULL,
    idmatkul integer CONSTRAINT "userMatkul _idMatkul _not_null" NOT NULL,
    iduser integer
);


ALTER TABLE public.user_matkul OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 16504)
-- Name: userMatkul _idUserMatkul _seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.user_matkul ALTER COLUMN iduser_matkul ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."userMatkul _idUserMatkul _seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 219 (class 1259 OID 16441)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    iduser integer CONSTRAINT "users_id _not_null" NOT NULL,
    name character varying(50) CONSTRAINT "users_name _not_null" NOT NULL,
    email character varying(50) CONSTRAINT "users_email _not_null" NOT NULL,
    password character varying(50) CONSTRAINT "users_password _not_null" NOT NULL,
    role character varying(10) CONSTRAINT "users_role _not_null" NOT NULL,
    npm character varying(10),
    "noInduk" character varying(10),
    foto character varying(255)
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 5093 (class 0 OID 16609)
-- Dependencies: 234
-- Data for Name: AnggotaKelompok; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."AnggotaKelompok" (idanggota_kelompok, idkelompok, userid) FROM stdin;
61	11	1
62	11	2
63	11	3
64	11	4
65	11	5
66	12	6
67	12	7
68	12	8
69	12	9
70	12	10
71	13	11
72	13	12
73	13	13
74	13	14
75	13	15
76	14	16
77	14	17
78	14	18
79	14	19
80	14	20
81	15	21
82	15	22
83	15	23
84	15	24
85	15	25
86	16	26
87	16	27
88	16	28
89	16	29
90	16	30
91	17	1
92	17	2
93	17	3
94	17	4
95	17	5
96	18	6
97	18	7
98	18	8
99	18	9
100	18	10
101	19	11
102	19	12
103	19	13
104	19	14
105	19	15
106	20	16
107	20	17
108	20	18
109	20	19
110	20	20
111	21	21
112	21	22
113	21	23
114	21	24
115	21	25
116	22	26
117	22	27
118	22	28
119	22	29
120	22	30
\.


--
-- TOC entry 5081 (class 0 OID 16468)
-- Dependencies: 222
-- Data for Name: kelompok; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.kelompok (idkelompok, nama, "jmlAnggota", idtubes) FROM stdin;
11	Kelompok 1	5	1
12	Kelompok 2	5	1
13	Kelompok 3	5	1
14	Kelompok 4	5	1
15	Kelompok 5	5	1
16	Kelompok 6	5	1
17	Kelompok 7	5	2
18	Kelompok 8	5	2
19	Kelompok 9	5	2
20	Kelompok 10	5	2
21	Kelompok 11	5	2
22	Kelompok 12	5	2
23	Kelompok 13	5	3
24	Kelompok 14	5	3
25	Kelompok 15	5	3
26	Kelompok 16	5	3
27	Kelompok 17	5	3
28	Kelompok 18	5	3
\.


--
-- TOC entry 5079 (class 0 OID 16452)
-- Dependencies: 220
-- Data for Name: matkul; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.matkul (idmatkul, nama, kelas) FROM stdin;
1	Dasar Pemrograman	A
2	Basis Data	A
3	Struktur Data	B
4	Sistem Operasi	A
5	Jaringan Komputer	C
6	Algoritma & Pemrograman	B
7	Rekayasa Perangkat Lunak	A
8	Kalkulus	A
9	Arsitektur Komputer	B
10	Artificial Intelligence	C
11	Dasar Pemrograman	B
12	Basis Data	B
13	Struktur Data	C
14	Sistem Operasi	C
15	Jaringan Komputer	A
\.


--
-- TOC entry 5090 (class 0 OID 16523)
-- Dependencies: 231
-- Data for Name: matkul_semester; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.matkul_semester (idmatkul_semester, idmatkul, idsemester) FROM stdin;
1	1	1
2	2	1
3	3	1
4	4	1
5	5	1
6	6	1
7	7	1
8	8	1
9	9	2
10	10	2
11	11	2
12	12	2
13	13	2
14	14	2
15	15	2
\.


--
-- TOC entry 5084 (class 0 OID 16488)
-- Dependencies: 225
-- Data for Name: penilaian; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.penilaian (idpenilaian, nilai, komentar, iduser, "idTahap") FROM stdin;
1	68		1	1
2	87	Komentar user 2 tahap 1	2	1
3	74		3	1
4	45		4	1
5	1		5	1
6	96		6	1
7	24		7	1
8	85		8	1
9	40	Komentar user 9 tahap 1	9	1
10	92		10	1
11	52	Komentar user 11 tahap 1	11	1
12	4		12	1
13	4	Komentar user 13 tahap 1	13	1
14	44	Komentar user 14 tahap 1	14	1
15	9	Komentar user 15 tahap 1	15	1
16	95		16	1
17	65		17	1
18	15	Komentar user 18 tahap 1	18	1
19	88		19	1
20	68	Komentar user 20 tahap 1	20	1
21	64		21	1
22	26	Komentar user 22 tahap 1	22	1
23	85		23	1
24	62		24	1
25	65	Komentar user 25 tahap 1	25	1
26	45		26	1
27	22		27	1
28	69		28	1
29	26	Komentar user 29 tahap 1	29	1
30	38	Komentar user 30 tahap 1	30	1
31	67		1	2
32	32	Komentar user 2 tahap 2	2	2
33	28	Komentar user 3 tahap 2	3	2
34	97	Komentar user 4 tahap 2	4	2
35	79	Komentar user 5 tahap 2	5	2
36	56	Komentar user 6 tahap 2	6	2
37	22		7	2
38	50		8	2
39	15		9	2
40	48	Komentar user 10 tahap 2	10	2
41	78	Komentar user 11 tahap 2	11	2
42	91	Komentar user 12 tahap 2	12	2
43	67	Komentar user 13 tahap 2	13	2
44	21	Komentar user 14 tahap 2	14	2
45	24		15	2
46	21	Komentar user 16 tahap 2	16	2
47	5	Komentar user 17 tahap 2	17	2
48	88	Komentar user 18 tahap 2	18	2
49	41	Komentar user 19 tahap 2	19	2
50	18		20	2
51	58		21	2
52	5		22	2
53	39	Komentar user 23 tahap 2	23	2
54	55	Komentar user 24 tahap 2	24	2
55	94		25	2
56	98	Komentar user 26 tahap 2	26	2
57	86	Komentar user 27 tahap 2	27	2
58	94	Komentar user 28 tahap 2	28	2
59	77		29	2
60	93	Komentar user 30 tahap 2	30	2
61	19		1	3
62	75		2	3
63	21	Komentar user 3 tahap 3	3	3
64	50	Komentar user 4 tahap 3	4	3
65	26		5	3
66	81	Komentar user 6 tahap 3	6	3
67	46		7	3
68	91		8	3
69	24	Komentar user 9 tahap 3	9	3
70	37	Komentar user 10 tahap 3	10	3
71	85		11	3
72	45	Komentar user 12 tahap 3	12	3
73	56	Komentar user 13 tahap 3	13	3
74	44	Komentar user 14 tahap 3	14	3
75	2	Komentar user 15 tahap 3	15	3
76	51		16	3
77	63		17	3
78	2	Komentar user 18 tahap 3	18	3
79	60	Komentar user 19 tahap 3	19	3
80	42	Komentar user 20 tahap 3	20	3
81	34		21	3
82	83		22	3
83	51	Komentar user 23 tahap 3	23	3
84	63		24	3
85	50	Komentar user 25 tahap 3	25	3
86	13	Komentar user 26 tahap 3	26	3
87	63	Komentar user 27 tahap 3	27	3
88	39		28	3
89	1		29	3
90	46	Komentar user 30 tahap 3	30	3
91	61		1	4
92	53		2	4
93	32		3	4
94	60	Komentar user 4 tahap 4	4	4
95	95	Komentar user 5 tahap 4	5	4
96	25		6	4
97	76	Komentar user 7 tahap 4	7	4
98	24	Komentar user 8 tahap 4	8	4
99	49	Komentar user 9 tahap 4	9	4
100	31		10	4
101	94		11	4
102	19	Komentar user 12 tahap 4	12	4
103	77		13	4
104	97	Komentar user 14 tahap 4	14	4
105	38		15	4
106	84		16	4
107	57		17	4
108	79		18	4
109	4	Komentar user 19 tahap 4	19	4
110	2	Komentar user 20 tahap 4	20	4
111	26	Komentar user 21 tahap 4	21	4
112	43	Komentar user 22 tahap 4	22	4
113	38	Komentar user 23 tahap 4	23	4
114	96		24	4
115	59	Komentar user 25 tahap 4	25	4
116	96	Komentar user 26 tahap 4	26	4
117	23	Komentar user 27 tahap 4	27	4
118	77		28	4
119	36	Komentar user 29 tahap 4	29	4
120	64	Komentar user 30 tahap 4	30	4
121	69	Komentar user 1 tahap 5	1	5
122	77		2	5
123	49		3	5
124	12		4	5
125	14		5	5
126	79	Komentar user 6 tahap 5	6	5
127	45		7	5
128	13		8	5
129	16	Komentar user 9 tahap 5	9	5
130	32	Komentar user 10 tahap 5	10	5
131	76		11	5
132	77		12	5
133	14	Komentar user 13 tahap 5	13	5
134	33		14	5
135	90	Komentar user 15 tahap 5	15	5
136	90	Komentar user 16 tahap 5	16	5
137	20		17	5
138	98		18	5
139	31		19	5
140	63		20	5
141	13	Komentar user 21 tahap 5	21	5
142	26		22	5
143	92	Komentar user 23 tahap 5	23	5
144	82	Komentar user 24 tahap 5	24	5
145	57	Komentar user 25 tahap 5	25	5
146	81	Komentar user 26 tahap 5	26	5
147	23		27	5
148	81	Komentar user 28 tahap 5	28	5
149	72	Komentar user 29 tahap 5	29	5
150	87		30	5
151	33	Komentar user 1 tahap 6	1	6
152	68	Komentar user 2 tahap 6	2	6
153	99	Komentar user 3 tahap 6	3	6
154	38	Komentar user 4 tahap 6	4	6
155	8		5	6
156	11	Komentar user 6 tahap 6	6	6
157	99	Komentar user 7 tahap 6	7	6
158	2		8	6
159	92		9	6
160	55		10	6
161	66		11	6
162	2	Komentar user 12 tahap 6	12	6
163	31	Komentar user 13 tahap 6	13	6
164	48	Komentar user 14 tahap 6	14	6
165	4		15	6
166	92		16	6
167	58		17	6
168	15		18	6
169	65	Komentar user 19 tahap 6	19	6
170	36		20	6
171	98		21	6
172	90		22	6
173	32	Komentar user 23 tahap 6	23	6
174	14		24	6
175	34		25	6
176	36	Komentar user 26 tahap 6	26	6
177	78		27	6
178	40	Komentar user 28 tahap 6	28	6
179	50		29	6
180	30		30	6
181	82	Komentar user 1 tahap 7	1	7
182	73		2	7
183	77	Komentar user 3 tahap 7	3	7
184	69		4	7
185	90	Komentar user 5 tahap 7	5	7
186	78	Komentar user 6 tahap 7	6	7
187	41		7	7
188	81		8	7
189	40	Komentar user 9 tahap 7	9	7
190	81		10	7
191	52		11	7
192	72		12	7
193	6		13	7
194	5		14	7
195	63		15	7
196	22	Komentar user 16 tahap 7	16	7
197	2		17	7
198	71	Komentar user 18 tahap 7	18	7
199	3		19	7
200	74		20	7
201	81		21	7
202	88		22	7
203	62	Komentar user 23 tahap 7	23	7
204	58		24	7
205	9		25	7
206	90		26	7
207	30		27	7
208	62	Komentar user 28 tahap 7	28	7
209	17	Komentar user 29 tahap 7	29	7
210	97		30	7
211	20		1	8
212	30		2	8
213	30		3	8
214	63	Komentar user 4 tahap 8	4	8
215	52	Komentar user 5 tahap 8	5	8
216	80	Komentar user 6 tahap 8	6	8
217	9	Komentar user 7 tahap 8	7	8
218	11	Komentar user 8 tahap 8	8	8
219	69		9	8
220	24	Komentar user 10 tahap 8	10	8
221	11		11	8
222	91	Komentar user 12 tahap 8	12	8
223	85		13	8
224	40	Komentar user 14 tahap 8	14	8
225	59		15	8
226	30		16	8
227	36		17	8
228	22	Komentar user 18 tahap 8	18	8
229	52	Komentar user 19 tahap 8	19	8
230	30	Komentar user 20 tahap 8	20	8
231	98		21	8
232	25	Komentar user 22 tahap 8	22	8
233	1		23	8
234	90		24	8
235	27	Komentar user 25 tahap 8	25	8
236	37		26	8
237	98		27	8
238	47	Komentar user 28 tahap 8	28	8
239	61	Komentar user 29 tahap 8	29	8
240	57	Komentar user 30 tahap 8	30	8
241	4		1	9
242	68		2	9
243	43	Komentar user 3 tahap 9	3	9
244	10	Komentar user 4 tahap 9	4	9
245	30		5	9
246	84		6	9
247	74	Komentar user 7 tahap 9	7	9
248	73	Komentar user 8 tahap 9	8	9
249	88		9	9
250	90		10	9
251	50	Komentar user 11 tahap 9	11	9
252	88		12	9
253	38		13	9
254	96	Komentar user 14 tahap 9	14	9
255	8	Komentar user 15 tahap 9	15	9
256	2	Komentar user 16 tahap 9	16	9
257	68	Komentar user 17 tahap 9	17	9
258	67		18	9
259	97		19	9
260	90	Komentar user 20 tahap 9	20	9
261	19	Komentar user 21 tahap 9	21	9
262	31	Komentar user 22 tahap 9	22	9
263	30		23	9
264	9		24	9
265	50	Komentar user 25 tahap 9	25	9
266	63	Komentar user 26 tahap 9	26	9
267	62		27	9
268	31	Komentar user 28 tahap 9	28	9
269	61	Komentar user 29 tahap 9	29	9
270	2		30	9
\.


--
-- TOC entry 5086 (class 0 OID 16495)
-- Dependencies: 227
-- Data for Name: semester; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.semester (idsemester, startdate, enddate, jenis_semester) FROM stdin;
1	2021-08-01	2021-12-31	GANJIL
2	2022-01-01	2022-06-30	GENAP
3	2022-08-01	2022-12-31	GANJIL
4	2023-01-01	2023-06-30	GENAP
5	2023-08-01	2023-12-31	GANJIL
6	2024-01-01	2024-06-30	GENAP
7	2024-08-01	2024-12-31	GANJIL
8	2025-01-01	2025-06-30	GENAP
\.


--
-- TOC entry 5082 (class 0 OID 16476)
-- Dependencies: 223
-- Data for Name: tahap_tubes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tahap_tubes ("idTahap", nama, deskripsi, rubrik, tanggal, status_penilaian, idtubes) FROM stdin;
1	Tahap 1 - Proposal	Pengumpulan proposal awal	Struktur Proposal	2025-03-01	BELUM DINILAI	1
2	Tahap 2 - Progress Review	Presentasi progres tengah	Progres Fungsional	2025-04-01	BELUM DINILAI	1
3	Tahap 3 - Final Presentation	Presentasi akhir project	Fungsionalitas	2025-05-01	BELUM DINILAI	1
4	Tahap 1 - Proposal	Pengumpulan proposal awal	Struktur Proposal	2025-03-01	BELUM DINILAI	2
5	Tahap 2 - Progress Review	Presentasi progres tengah	Progres Fungsional	2025-04-01	BELUM DINILAI	2
6	Tahap 3 - Final Presentation	Presentasi akhir project	Fungsionalitas	2025-05-01	BELUM DINILAI	2
7	Tahap 1 - Proposal	Pengumpulan proposal awal	Struktur Proposal	2025-03-01	BELUM DINILAI	3
8	Tahap 2 - Progress Review	Presentasi progres tengah	Progres Fungsional	2025-04-01	BELUM DINILAI	3
9	Tahap 3 - Final Presentation	Presentasi akhir project	Fungsionalitas	2025-05-01	BELUM DINILAI	3
\.


--
-- TOC entry 5080 (class 0 OID 16460)
-- Dependencies: 221
-- Data for Name: tubes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tubes (idtubes, deskripsi, jmlkelompok, idmatkul) FROM stdin;
1	Tubes untuk Matkul 1	5	1
2	Tubes untuk Matkul 2	4	2
3	Tubes untuk Matkul 3	3	3
4	Tubes untuk Matkul 4	6	4
5	Tubes untuk Matkul 5	4	5
6	Tubes untuk Matkul 6	3	6
7	Tubes untuk Matkul 7	5	7
8	Tubes untuk Matkul 8	4	8
9	Tubes untuk Matkul 9	3	9
10	Tubes untuk Matkul 10	5	10
11	Tubes untuk Matkul 11	4	11
12	Tubes untuk Matkul 12	3	12
13	Tubes untuk Matkul 13	6	13
14	Tubes untuk Matkul 14	4	14
15	Tubes untuk Matkul 15	5	15
\.


--
-- TOC entry 5088 (class 0 OID 16505)
-- Dependencies: 229
-- Data for Name: user_matkul; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_matkul (iduser_matkul, idmatkul, iduser) FROM stdin;
1	1	1
2	1	2
3	1	3
4	1	4
5	1	5
6	1	6
7	1	7
8	1	8
9	1	9
10	1	10
11	1	11
12	1	12
13	1	13
14	1	14
15	1	15
16	1	16
17	1	17
18	1	18
19	1	19
20	1	20
21	1	21
22	1	22
23	1	23
24	1	24
25	1	25
26	1	26
27	1	27
28	1	28
29	1	29
30	1	30
31	2	1
32	2	2
33	2	3
34	2	4
35	2	5
36	2	6
37	2	7
38	2	8
39	2	9
40	2	10
41	2	11
42	2	12
43	2	13
44	2	14
45	2	15
46	2	16
47	2	17
48	2	18
49	2	19
50	2	20
51	2	21
52	2	22
53	2	23
54	2	24
55	2	25
56	2	26
57	2	27
58	2	28
59	2	29
60	2	30
61	3	1
62	3	2
63	3	3
64	3	4
65	3	5
66	3	6
67	3	7
68	3	8
69	3	9
70	3	10
71	3	11
72	3	12
73	3	13
74	3	14
75	3	15
76	3	16
77	3	17
78	3	18
79	3	19
80	3	20
81	3	21
82	3	22
83	3	23
84	3	24
85	3	25
86	3	26
87	3	27
88	3	28
89	3	29
90	3	30
91	4	1
92	4	2
93	4	3
94	4	4
95	4	5
96	4	6
97	4	7
98	4	8
99	4	9
100	4	10
101	4	11
102	4	12
103	4	13
104	4	14
105	4	15
106	4	16
107	4	17
108	4	18
109	4	19
110	4	20
111	4	21
112	4	22
113	4	23
114	4	24
115	4	25
116	4	26
117	4	27
118	4	28
119	4	29
120	4	30
121	5	1
122	5	2
123	5	3
124	5	4
125	5	5
126	5	6
127	5	7
128	5	8
129	5	9
130	5	10
131	5	11
132	5	12
133	5	13
134	5	14
135	5	15
136	5	16
137	5	17
138	5	18
139	5	19
140	5	20
141	5	21
142	5	22
143	5	23
144	5	24
145	5	25
146	5	26
147	5	27
148	5	28
149	5	29
150	5	30
\.


--
-- TOC entry 5078 (class 0 OID 16441)
-- Dependencies: 219
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (iduser, name, email, password, role, npm, "noInduk", foto) FROM stdin;
1	Agus Pratama	aguspratama32@example.com	pass123	MAHASISWA	618000001	\N	\N
2	Siti Lestari	siti.lestari91@example.com	pass123	MAHASISWA	618000002	\N	\N
3	Rizky Ramadhan	rizkyramdhn44@example.com	pass123	MAHASISWA	618000003	\N	\N
4	Dewi Anggraini	dewiangg28@example.com	pass123	MAHASISWA	618000004	\N	\N
5	Fajar Saputra	fajarspt77@example.com	pass123	MAHASISWA	618000005	\N	\N
6	Nabila Putri	nabilaptri55@example.com	pass123	MAHASISWA	618000006	\N	\N
7	Yoga Prasetyo	yogaprst20@example.com	pass123	MAHASISWA	618000007	\N	\N
8	Putri Ayu Lestari	putrilstry61@example.com	pass123	MAHASISWA	618000008	\N	\N
9	Muhammad Reza	mreza182@example.com	pass123	MAHASISWA	618000009	\N	\N
10	Anisa Fauziah	anisafz89@example.com	pass123	MAHASISWA	618000010	\N	\N
11	Bagas Setiawan	bagasset11@example.com	pass123	MAHASISWA	618000011	\N	\N
12	Arum Widyasari	arumwdys52@example.com	pass123	MAHASISWA	618000012	\N	\N
13	Ramadhan Putra	ramdhptr03@example.com	pass123	MAHASISWA	618000013	\N	\N
14	Citra Maharani	citra.mhrn10@example.com	pass123	MAHASISWA	618000014	\N	\N
15	Heri Santoso	herisant99@example.com	pass123	MAHASISWA	618000015	\N	\N
16	Aulia Rahma	auliarah90@example.com	pass123	MAHASISWA	618000016	\N	\N
17	Yusuf Hidayat	yusfhdyt49@example.com	pass123	MAHASISWA	618000017	\N	\N
18	Nadya Amalia	nadyaaml77@example.com	pass123	MAHASISWA	618000018	\N	\N
19	Farhan Akbar	farhanakbr12@example.com	pass123	MAHASISWA	618000019	\N	\N
20	Aulia Putri	aulptr81@example.com	pass123	MAHASISWA	618000020	\N	\N
21	Rafi Pradipta	rafiprdp17@example.com	pass123	MAHASISWA	618000021	\N	\N
22	Intan Sari	intansari33@example.com	pass123	MAHASISWA	618000022	\N	\N
23	Joko Purnomo	jokopur78@example.com	pass123	MAHASISWA	618000023	\N	\N
24	Melati Kusuma	melatiksm20@example.com	pass123	MAHASISWA	618000024	\N	\N
25	Gilang Septian	gilangspt04@example.com	pass123	MAHASISWA	618000025	\N	\N
26	Rani Oktaviani	ranioktv90@example.com	pass123	MAHASISWA	618000026	\N	\N
27	Wahyu Nugroho	wahyungrh70@example.com	pass123	MAHASISWA	618000027	\N	\N
28	Nina Safitri	ninsaftr92@example.com	pass123	MAHASISWA	618000028	\N	\N
29	Ilham Prakoso	ilhamprks02@example.com	pass123	MAHASISWA	618000029	\N	\N
30	Rina Maharani	rinamhrn84@example.com	pass123	MAHASISWA	618000030	\N	\N
31	Aditya Kurniawan	adityakrwn73@example.com	pass123	MAHASISWA	618000031	\N	\N
32	Lidya Amelia	lidyaamlia65@example.com	pass123	MAHASISWA	618000032	\N	\N
33	Rio Wahyudi	riowhyd33@example.com	pass123	MAHASISWA	618000033	\N	\N
34	Salsa Zahra	salsazhr21@example.com	pass123	MAHASISWA	618000034	\N	\N
35	Ardiansyah Putra	ardiptra77@example.com	pass123	MAHASISWA	618000035	\N	\N
36	Yuliana Desi	yulids31@example.com	pass123	MAHASISWA	618000036	\N	\N
37	Faisal Rahman	faisalrmn60@example.com	pass123	MAHASISWA	618000037	\N	\N
38	Anjani Lestari	anjanilstr20@example.com	pass123	MAHASISWA	618000038	\N	\N
39	Taufik Hidayat	taufik.hdy98@example.com	pass123	MAHASISWA	618000039	\N	\N
40	Mega Aprilia	megaaprl99@example.com	pass123	MAHASISWA	618000040	\N	\N
41	Rendy Pratama	rendyprt35@example.com	pass123	MAHASISWA	618000041	\N	\N
42	Sari Utami	sariutmi08@example.com	pass123	MAHASISWA	618000042	\N	\N
43	Kevin Mahendra	kevinmhd19@example.com	pass123	MAHASISWA	618000043	\N	\N
44	Tia Wulandari	tiawulan55@example.com	pass123	MAHASISWA	618000044	\N	\N
45	Ahmad Fauzan	ahmadfzn93@example.com	pass123	MAHASISWA	618000045	\N	\N
46	Rosa Meilani	rosamlni54@example.com	pass123	MAHASISWA	618000046	\N	\N
47	Doni Saputra	donispt21@example.com	pass123	MAHASISWA	618000047	\N	\N
48	Sylvia Anggraini	sylviaang81@example.com	pass123	MAHASISWA	618000048	\N	\N
49	Hilmi Rahmat	hilmirmt76@example.com	pass123	MAHASISWA	618000049	\N	\N
50	Fanny Zahra	fannyzhr27@example.com	pass123	MAHASISWA	618000050	\N	\N
81	Dr. Andi Setiawan	andi.setiawan42@example.com	pass123	DOSEN	\N	902334512	\N
82	Dr. Rina Hartati	rinahartati12@example.com	pass123	DOSEN	\N	985322144	\N
83	Prof. Budi Santoso	budisantoso31@example.com	pass123	DOSEN	\N	901237821	\N
84	Dr. Siti Rohmah	sitirohmah90@example.com	pass123	DOSEN	\N	934551287	\N
85	Dr. Rudi Kurniawan	rudikrn06@example.com	pass123	DOSEN	\N	910238774	\N
86	Dr. Dina Amelia	dina.amelia40@example.com	pass123	DOSEN	\N	987662341	\N
87	Dr. Taufan Prakoso	taufanprks22@example.com	pass123	DOSEN	\N	988732110	\N
88	Dr. Yulia Handayani	yuliahandy77@example.com	pass123	DOSEN	\N	976513882	\N
89	Prof. Dedi Firmansyah	dedifirmn65@example.com	pass123	DOSEN	\N	912345876	\N
90	Dr. Rachma Wulandari	rachmawld34@example.com	pass123	DOSEN	\N	998123440	\N
91	Dr. Hendro Saputra	hendrospt14@example.com	pass123	DOSEN	\N	933276441	\N
92	Dr. Mayang Pratiwi	mayangprtw48@example.com	pass123	DOSEN	\N	932178662	\N
93	Dr. Rafi Aliansyah	rafialian33@example.com	pass123	DOSEN	\N	955712631	\N
94	Dr. Natasya Widya	natasya.wdy99@example.com	pass123	DOSEN	\N	911334567	\N
95	Dr. Hendra Yusuf	hendrayusuf82@example.com	pass123	DOSEN	\N	988455123	\N
96	Prof. Lilis Permata	lilispermata41@example.com	pass123	DOSEN	\N	934885122	\N
97	Dr. Reza Firmansyah	rezafirm20@example.com	pass123	DOSEN	\N	922114578	\N
98	Dr. Santi Anggraeni	santiangg77@example.com	pass123	DOSEN	\N	956443211	\N
99	Dr. Zulfikar Adnan	zulfikaradn03@example.com	pass123	DOSEN	\N	966201337	\N
100	Dr. Maya Safitri	mayasafitri90@example.com	pass123	DOSEN	\N	986775112	\N
\.


--
-- TOC entry 5102 (class 0 OID 0)
-- Dependencies: 233
-- Name: AnggotaKelompok_idAnggotaKelompok_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."AnggotaKelompok_idAnggotaKelompok_seq"', 120, true);


--
-- TOC entry 5103 (class 0 OID 0)
-- Dependencies: 230
-- Name: MatkulSemester _idMatkulSemester_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."MatkulSemester _idMatkulSemester_seq"', 15, true);


--
-- TOC entry 5104 (class 0 OID 0)
-- Dependencies: 224
-- Name: Penilaian_idPenilaian _seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Penilaian_idPenilaian _seq"', 270, true);


--
-- TOC entry 5105 (class 0 OID 0)
-- Dependencies: 226
-- Name: Semester _idSemester _seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Semester _idSemester _seq"', 8, true);


--
-- TOC entry 5106 (class 0 OID 0)
-- Dependencies: 232
-- Name: Tubes_idTubes_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Tubes_idTubes_seq"', 15, true);


--
-- TOC entry 5107 (class 0 OID 0)
-- Dependencies: 237
-- Name: kelompok_idkelompok_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.kelompok_idkelompok_seq', 28, true);


--
-- TOC entry 5108 (class 0 OID 0)
-- Dependencies: 235
-- Name: matkul_idmatkul_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.matkul_idmatkul_seq', 15, true);


--
-- TOC entry 5109 (class 0 OID 0)
-- Dependencies: 236
-- Name: tahap_tubes_idTahap_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."tahap_tubes_idTahap_seq"', 9, true);


--
-- TOC entry 5110 (class 0 OID 0)
-- Dependencies: 228
-- Name: userMatkul _idUserMatkul _seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."userMatkul _idUserMatkul _seq"', 150, true);


--
-- TOC entry 4919 (class 2606 OID 16616)
-- Name: AnggotaKelompok AnggotaKelompok_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."AnggotaKelompok"
    ADD CONSTRAINT "AnggotaKelompok_pkey" PRIMARY KEY (idanggota_kelompok);


--
-- TOC entry 4907 (class 2606 OID 16475)
-- Name: kelompok Kelompok_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kelompok
    ADD CONSTRAINT "Kelompok_pkey" PRIMARY KEY (idkelompok);


--
-- TOC entry 4903 (class 2606 OID 16459)
-- Name: matkul Matkul _pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.matkul
    ADD CONSTRAINT "Matkul _pkey" PRIMARY KEY (idmatkul);


--
-- TOC entry 4917 (class 2606 OID 16530)
-- Name: matkul_semester MatkulSemester _pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.matkul_semester
    ADD CONSTRAINT "MatkulSemester _pkey" PRIMARY KEY (idmatkul_semester);


--
-- TOC entry 4911 (class 2606 OID 16493)
-- Name: penilaian Penilaian_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.penilaian
    ADD CONSTRAINT "Penilaian_pkey" PRIMARY KEY (idpenilaian);


--
-- TOC entry 4913 (class 2606 OID 16503)
-- Name: semester Semester _pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.semester
    ADD CONSTRAINT "Semester _pkey" PRIMARY KEY (idsemester);


--
-- TOC entry 4909 (class 2606 OID 16486)
-- Name: tahap_tubes TahapTubes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tahap_tubes
    ADD CONSTRAINT "TahapTubes_pkey" PRIMARY KEY ("idTahap");


--
-- TOC entry 4905 (class 2606 OID 16467)
-- Name: tubes Tubes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tubes
    ADD CONSTRAINT "Tubes_pkey" PRIMARY KEY (idtubes);


--
-- TOC entry 4915 (class 2606 OID 16511)
-- Name: user_matkul userMatkul _pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_matkul
    ADD CONSTRAINT "userMatkul _pkey" PRIMARY KEY (iduser_matkul);


--
-- TOC entry 4901 (class 2606 OID 16451)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (iduser);


--
-- TOC entry 4920 (class 2606 OID 16646)
-- Name: tubes fk_idmatkul; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tubes
    ADD CONSTRAINT fk_idmatkul FOREIGN KEY (idmatkul) REFERENCES public.matkul(idmatkul);


--
-- TOC entry 4923 (class 2606 OID 16675)
-- Name: penilaian fk_idtahap; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.penilaian
    ADD CONSTRAINT fk_idtahap FOREIGN KEY ("idTahap") REFERENCES public.tahap_tubes("idTahap");


--
-- TOC entry 4921 (class 2606 OID 16680)
-- Name: kelompok fk_idtubes; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kelompok
    ADD CONSTRAINT fk_idtubes FOREIGN KEY (idtubes) REFERENCES public.tubes(idtubes);


--
-- TOC entry 4922 (class 2606 OID 16668)
-- Name: tahap_tubes fk_idtubes; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tahap_tubes
    ADD CONSTRAINT fk_idtubes FOREIGN KEY (idtubes) REFERENCES public.tubes(idtubes);


--
-- TOC entry 4924 (class 2606 OID 16663)
-- Name: penilaian fk_iduser; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.penilaian
    ADD CONSTRAINT fk_iduser FOREIGN KEY (iduser) REFERENCES public.users(iduser);


--
-- TOC entry 4929 (class 2606 OID 16617)
-- Name: AnggotaKelompok idKelompok; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."AnggotaKelompok"
    ADD CONSTRAINT "idKelompok" FOREIGN KEY (idkelompok) REFERENCES public.kelompok(idkelompok);


--
-- TOC entry 4927 (class 2606 OID 16531)
-- Name: matkul_semester idMatkul ; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.matkul_semester
    ADD CONSTRAINT "idMatkul " FOREIGN KEY (idmatkul) REFERENCES public.matkul(idmatkul);


--
-- TOC entry 4925 (class 2606 OID 16512)
-- Name: user_matkul idMatkul ; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_matkul
    ADD CONSTRAINT "idMatkul " FOREIGN KEY (idmatkul) REFERENCES public.matkul(idmatkul);


--
-- TOC entry 4928 (class 2606 OID 16536)
-- Name: matkul_semester idSemester ; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.matkul_semester
    ADD CONSTRAINT "idSemester " FOREIGN KEY (idsemester) REFERENCES public.semester(idsemester);


--
-- TOC entry 4926 (class 2606 OID 16517)
-- Name: user_matkul idUser ; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_matkul
    ADD CONSTRAINT "idUser " FOREIGN KEY (iduser) REFERENCES public.users(iduser);


--
-- TOC entry 4930 (class 2606 OID 16622)
-- Name: AnggotaKelompok userid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."AnggotaKelompok"
    ADD CONSTRAINT userid FOREIGN KEY (userid) REFERENCES public.users(iduser);


-- Completed on 2025-11-25 20:11:56

--
-- PostgreSQL database dump complete
--

\unrestrict QJ8nxVsWwqPUkToyTqEQGGEfPOVeASyrccLhKFL21HlffQaTSNobTRfvydXcyUa

