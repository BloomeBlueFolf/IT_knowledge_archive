package com.example.demo.Controllers;

import com.example.demo.Impls.ChapterServiceImpl;
import com.example.demo.Impls.FolderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChapterController {

    @Autowired
    private FolderServiceImpl folderService;

    @Autowired
    private ChapterServiceImpl chapterService;

    @GetMapping("/user/chapters")
    public String showChapters(Model model, @RequestParam ("id") long id){
        model.addAttribute(("folder"), folderService.getFolder(id));
        return "chapters";
    }
}
