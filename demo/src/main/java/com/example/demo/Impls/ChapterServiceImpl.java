package com.example.demo.Impls;

import com.example.demo.Entities.Chapter;
import com.example.demo.Repositories.ChapterRepository;
import com.example.demo.Services.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChapterServiceImpl implements ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;


    @Override
    public void deleteChapter(Chapter chapter) {
        chapterRepository.delete(chapter);
    }

    @Override
    public void saveChapter(Chapter chapter) {
        chapterRepository.save(chapter);
    }

    @Override
    public Chapter getChapter(long id) {
        return chapterRepository.findById(id);
    }

    @Override
    public void renameChapter(Chapter chapter, long id) {
        Chapter renamedChapter = chapterRepository.findById(id);
        renamedChapter.setLabel(chapter.getLabel());
        chapterRepository.save(renamedChapter);
    }
}
