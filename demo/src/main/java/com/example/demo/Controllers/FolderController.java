package com.example.demo.Controllers;

import com.example.demo.Entities.Folder;
import com.example.demo.Services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
