package com.example.demo.Controllers;

import com.example.demo.Entities.Chapter;
import com.example.demo.Entities.Folder;
import com.example.demo.Services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class FolderController {

    @Autowired
    private FolderService folderService;

    @GetMapping("/public/folder")
    public String showCategories(Model model){
        List<Folder> folderList = folderService.getAllFolders();
        model.addAttribute(("folders"), folderList);
        return "folders";
    }

    @GetMapping("/user/folder/create")
    public String createFolder(Model model){
        Folder createFolder = new Folder();
        model.addAttribute(("folder"), createFolder);
        return "createFolderForm";
    }

    @PostMapping("/user/folder/create")
    public String createFolder(@ModelAttribute ("createFolder") Folder folder){
        folderService.saveFolder(folder);
        return "redirect:/public/folder?creationSuccess";
    }

    @GetMapping("/user/folder/delete/warning")
    public String deleteFolder(Model model, @RequestParam ("id") long id){
        Folder deleteFolder = folderService.getFolder(id);
        model.addAttribute(("folder"), deleteFolder);
        return "deleteWarningFolder";
    }

    @GetMapping("/user/folder/delete")
    public String deleteFolder(@RequestParam ("id") long id){
        folderService.deleteFolder(folderService.getFolder(id));
        return "redirect:/public/folder?deletionSuccess";
    }

    @GetMapping("/user/folder/rename")
    public String renameFolder(Model model, @RequestParam ("id") long id){
        Folder renamedFolder = folderService.getFolder(id);
        model.addAttribute(("id"), id);
        model.addAttribute("folder", renamedFolder);
        return "renameFolderForm";
    }

    @PostMapping("/user/folder/rename")
    public String renameFolder(@ModelAttribute ("renameFolder") Folder folder, @RequestParam ("id") long id){
        folderService.renameFolder(id, folder);
        return "redirect:/public/folder?renamingSuccess";
    }

    @GetMapping("/user/folder/addChapter")
    public String addChapter(Model model, @RequestParam ("id") long id){
        Chapter chapter = new Chapter();
        model.addAttribute(("id"), id);
        model.addAttribute(("chapter"), chapter);
        return "addChapterForm";
    }

    @PostMapping("/user/folder/addChapter")
    public String addChapter(@ModelAttribute ("chapter") Chapter chapter, @RequestParam ("id") long id){
        folderService.assignChapterToFolder(chapter, folderService.getFolder(id));
        return "redirect:/public/folder?addingSuccess";
    }
}
