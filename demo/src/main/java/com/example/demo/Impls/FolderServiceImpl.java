package com.example.demo.Impls;

import com.example.demo.Entities.Folder;
import com.example.demo.Repositories.FolderRepository;
import com.example.demo.Services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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


}
