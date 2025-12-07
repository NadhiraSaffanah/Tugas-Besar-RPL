package com.example.tubesrpl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/preview/dosen")
public class DosenPreviewController {

    @GetMapping("/{page}")
    public String previewDosen(@PathVariable String page) {
        return "dosen/" + page; 
    }
}
