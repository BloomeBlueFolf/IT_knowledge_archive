package com.example.demo.Services;


import com.example.demo.Entities.Chapter;
import com.example.demo.Entities.Folder;
import com.example.demo.Security.User;

import java.util.List;
import java.util.Optional;

public interface FolderService {

    public Folder getFolder(long id);

    public void saveFolder(Folder folder);

    public void deleteFolder(Folder folder);

    public void renameFolder(long id, Folder folder);

    public void assignChapterToFolder(Chapter chapter, Folder folder);

    public List <Folder> getAllFolders(User user);
}
