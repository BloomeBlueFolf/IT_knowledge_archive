package com.example.demo.Controllers;

import com.example.demo.Entities.Chapter;
import com.example.demo.Entities.Folder;
import com.example.demo.Services.FolderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class FolderController {

    @Autowired
    private FolderService folderService;


    @GetMapping("/user/folders")
    public String showFolders(Model model){

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
    public String createFolder(@Valid @ModelAttribute ("folder") Folder folder,
                               BindingResult result){

        if(result.hasErrors()){
            return "createFolderForm";
        }

        folderService.saveFolder(folder);
        return "redirect:/user/folders?creationSuccess";
    }

    @GetMapping("/user/folder/delete/warning")
    public String deleteFolder(Model model,
                               @RequestParam ("id") long id){

        Folder deleteFolder = folderService.getFolder(id);
        model.addAttribute(("folder"), deleteFolder);
        return "deleteWarningFolder";
    }

    @GetMapping("/user/folder/delete")
    public String deleteFolder(@RequestParam ("id") long id){

        folderService.deleteFolder(folderService.getFolder(id));
        return "redirect:/user/folders?deletionSuccess";
    }

    @GetMapping("/user/folder/rename")
    public String renameFolder(Model model,
                               @RequestParam ("id") long id){

        Folder renamedFolder = folderService.getFolder(id);
        model.addAttribute(("id"), id);
        model.addAttribute("folder", renamedFolder);
        return "renameFolderForm";
    }

    @PostMapping("/user/folder/rename")
    public String renameFolder(@Valid @ModelAttribute ("folder") Folder folder,
                               BindingResult result,
                               @RequestParam ("id") long id,
                               Model model){

        if(result.hasErrors()){

            model.addAttribute(("id"), id);
            model.addAttribute("folder", folder);
            return "renameFolderForm";
        }

        folderService.renameFolder(id, folder);
        return "redirect:/user/folders?renamingSuccess";
    }

    @GetMapping("/user/folder/addChapter")
    public String addChapter(Model model, @RequestParam ("id") long id){

        Chapter chapter = new Chapter();
        model.addAttribute(("id"), id);
        model.addAttribute(("chapter"), chapter);
        return "addChapterForm";
    }

    @PostMapping("/user/folder/addChapter")
    public ModelAndView addChapter(@Valid @ModelAttribute ("chapter") Chapter chapter,
                                   BindingResult result,
                                   @RequestParam ("id") long id,
                                   RedirectAttributes redirectAttributes,
                                   Model model){

        if(result.hasErrors()){
            model.addAttribute(("id"), id);
            model.addAttribute(("chapter"), chapter);
            return new ModelAndView("addChapterForm");
        }

        folderService.assignChapterToFolder(chapter, folderService.getFolder(id));
        redirectAttributes.addAttribute("id", id);
        return new ModelAndView("redirect:/user/chapters?addingSuccess");
    }
}
