package com.example.demo.Impls;

import com.example.demo.Entities.Folder;
import com.example.demo.Repositories.FolderRepository;
import com.example.demo.Services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {

    @Autowired
    private FolderRepository folderRepository;

    @Override
    public Folder getFolder(long id) {
        return folderRepository.findById(id);
    }

    @Override
    public List<Folder> getAllFolders() {
        return folderRepository.findAll();
    }

    @Override
    public void createFolder(String label) {
        folderRepository.save(new Folder(label));
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
}
