package com.example.demo.Services;


import com.example.demo.Entities.Folder;

import java.util.List;
import java.util.Optional;

public interface FolderService {

    public Folder getFolder(long id);

    public List<Folder> getAllFolders();

    public void createFolder(String label);

    public void saveFolder(Folder folder);

    public void deleteFolder(Folder folder);

    public void renameFolder(long id, Folder folder);
}
