package com.example.demo.Services;

import com.example.demo.Entities.Chapter;

public interface ChapterService {

    public void deleteChapter(Chapter chapter);

    public void saveChapter(Chapter chapter);

    public Chapter getChapter(long id);

    public void renameChapter(Chapter chapter, long id);
}
