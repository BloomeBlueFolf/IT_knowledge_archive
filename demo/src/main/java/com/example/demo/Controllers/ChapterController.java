package com.example.demo.Controllers;

import com.example.demo.Entities.Chapter;
import com.example.demo.Impls.ChapterServiceImpl;
import com.example.demo.Impls.FolderServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ChapterController {

    @Autowired
    private FolderServiceImpl folderService;

    @Autowired
    private ChapterServiceImpl chapterService;


    @GetMapping("/user/chapters")
    public String showChapters(Model model,
                               @RequestParam ("id") long id){

        List<Chapter> chapterList = folderService.getFolder(id).getChapters();

        String labelLastElement = "";
        if(!chapterList.isEmpty()) {
            labelLastElement = chapterList.get(chapterList.size() - 1).getLabel();
        }

        model.addAttribute(("folder"), folderService.getFolder(id));
        model.addAttribute(("lastElement"), labelLastElement);
        return "chapters";
    }

    @GetMapping("/user/chapter/delete/warning")
    public String deleteChapter(Model model,
                                @RequestParam ("id") long id,
                                @RequestParam ("folderId") long folderId){

        Chapter chapter = chapterService.getChapter(id);
        model.addAttribute(("chapter"), chapter);
        model.addAttribute(("folderId"), folderId);
        return "deleteWarningChapter";
    }

    @GetMapping("/user/chapter/delete")
    public String deleteChapter(@RequestParam ("id") long id,
                                @RequestParam ("folderId") long folderId,
                                RedirectAttributes redirectAttributes){

        chapterService.deleteChapter(chapterService.getChapter(id));
        redirectAttributes.addAttribute(("id"), folderId);
        return "redirect:/user/chapters?deletionSuccess";
    }

    @GetMapping("/user/chapter/rename")
    public String renameChapter(Model model,
                                @RequestParam ("id") long id){

        Chapter chapter = chapterService.getChapter(id);
        model.addAttribute(("id"), id);
        model.addAttribute(("chapter"), chapter);
        return "renameChapterForm";
    }

    @PostMapping("/user/chapter/rename")
    public String renameChapter(@Valid @ModelAttribute ("chapter") Chapter chapter,
                                BindingResult result,
                                @RequestParam ("id") long id,
                                RedirectAttributes redirectAttributes,
                                Model model){

        if(result.hasErrors()){

            model.addAttribute(("chapter"), chapter);
            model.addAttribute(("id"), id);
            return "renameChapterForm";
        }

        chapterService.renameChapter(chapter, id);
        redirectAttributes.addAttribute(("id"), chapterService.getChapter(id).getFolder().getId());
        return "redirect:/user/chapters?renamingSuccess";
    }
}
