package com.example.demo.Impls;

import com.example.demo.Entities.Chapter;
import com.example.demo.Entities.Folder;
import com.example.demo.Repositories.ChapterRepository;
import com.example.demo.Repositories.FolderRepository;
import com.example.demo.Security.User;
import com.example.demo.Services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private ChapterRepository chapterRepository;


    @Override
    public Folder getFolder(long id) {

        return folderRepository.findById(id);
    }

    @Override
    public List<Folder> getAllFolders(User user) {

        return folderRepository.findAllByUserOrderByLabelDesc(user);
    }

    @Override
    public void saveFolder(Folder folder) {

        folderRepository.save(folder);
    }

    @Override
    public void deleteFolder(Folder folder) {

        folderRepository.delete(folder);
    }

    @Override
    public void renameFolder(long id, Folder folder) {

        Folder renamedFolder = folderRepository.findById(id);
        renamedFolder.setLabel(folder.getLabel());
        saveFolder(renamedFolder);
    }

    @Override
    public void assignChapterToFolder(Chapter chapter, Folder folder){

        folder.getChapters().add(chapter);
        chapter.setFolder(folder);
        folderRepository.save(folder);
        chapterRepository.save(chapter);
    }
}
