package com.example.demo.Impls;

import com.example.demo.Entities.Chapter;
import com.example.demo.Entities.Segment;
import com.example.demo.ImageUtils;
import com.example.demo.Repositories.ChapterRepository;
import com.example.demo.Repositories.SegmentRepository;
import com.example.demo.Services.SegmentService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class SegmentServiceImpl implements SegmentService {

    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private ChapterRepository chapterRepository;


    @Override
    public List<Segment> getAllSegments() {

        return segmentRepository.findAll();
    }

    @Override
    public Segment getSegment(long id) {

        return segmentRepository.findById(id);
    }

    @Override
    public void saveSegment(Segment segment) {

        segmentRepository.save(segment);
    }

    @Override
    public void deleteSegment(Segment segment) {

        segmentRepository.delete(segment);
    }

    @Override
    public void assignSegmentToChapter(Segment segment, Chapter chapter, MultipartFile file) {

        if (file.getContentType().contains("image")) {
            try {
                segment.setFileType(file.getContentType());
                segment.setImage(ImageUtils.compressImage(file.getBytes()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            chapter.getSegments().add(segment);
            segment.setChapter(chapter);
            segmentRepository.saveAndFlush(segment);
            if(segment.getDbindex() == 0) {
                segment.setDbindex(segment.getId());
            }
            segmentRepository.save(segment);
            chapterRepository.save(chapter);
    }

    @Override
    public void editSegment(Segment segment, long editedSegmentId) {

        Segment editedSegment = segmentRepository.findById(editedSegmentId);
        editedSegment.setText(segment.getText());
        segmentRepository.save(editedSegment);
    }

    @Override
    public void swapWithPreviousSegment(Segment currentSegment){

        ArrayList<Segment> segmentsArrayList = new ArrayList<Segment>(segmentRepository.findByOrderByDbindex());
        int index = segmentsArrayList.indexOf(currentSegment);
        if(index > 0){
            Segment previousSegment = segmentsArrayList.get(index-1);
            long tempID = previousSegment.getDbindex();
            previousSegment.setDbindex(currentSegment.getDbindex());
            currentSegment.setDbindex(tempID);
            segmentRepository.save(currentSegment);
            segmentRepository.save(previousSegment);
        }
    }

    @Override
    public void swapWithFollowingSegment(Segment currentSegment){

        ArrayList<Segment> segmentsArrayList = new ArrayList<Segment>(segmentRepository.findByOrderByDbindex());
        int index = segmentsArrayList.indexOf(currentSegment);
        if(index < segmentsArrayList.size()-1){
            Segment followingSegment = segmentsArrayList.get(index+1);
            long tempID = followingSegment.getDbindex();
            followingSegment.setDbindex(currentSegment.getDbindex());
            currentSegment.setDbindex(tempID);
            segmentRepository.save(currentSegment);
            segmentRepository.save(followingSegment);
        }
    }

    @Override
    public List<Segment> findSegmentsOrderedByDbIndex(long id){

        List<Segment> allSegments = segmentRepository.findByOrderByDbindex();
        List<Segment> segmentsOfChapter = new LinkedList<>();
        for(Segment segment : allSegments){
            if(segment.getChapter().getId() == id){
            segmentsOfChapter.add(segment);
            }
        }
        return segmentsOfChapter;
    }

    public void createPdf(long chapterId) {

        Path PdfDirectory = Paths.get(System.getProperty("user.home") + File.separator + "OneDrive" + File.separator + "Desktop" + File.separator + "PDF-Files");
        try {
            Files.createDirectory(PdfDirectory);
        } catch (IOException e){
            e.printStackTrace();
        }

        Document document = new Document();
        Chapter chapter = chapterRepository.findById(chapterId);
        List<Segment> segments = chapter.getSegments();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(PdfDirectory + File.separator + String.format("%s.%s.pdf", chapter.getFolder().getLabel(), chapter.getLabel())));
        } catch(FileNotFoundException | DocumentException e){
            e.printStackTrace();
        }

        Font fontTitle = FontFactory.getFont(FontFactory.COURIER_BOLDOBLIQUE, 20, BaseColor.BLACK);
        Font fontContent = FontFactory.getFont(FontFactory.COURIER, 12, BaseColor.BLACK);

        document.open();

        try {
            document.add(new Paragraph(String.format("%s - %s", chapter.getFolder().getLabel(), chapter.getLabel()), fontTitle));
            document.add(Chunk.NEWLINE);
        } catch(DocumentException e) {
            e.printStackTrace();
        }

        for(Segment segment : segments){

                try {
                    document.add(new Paragraph(segment.getText(), fontContent));

                    if(!segment.getFileType().equals("")) {
                        Image image = Image.getInstance(ImageUtils.decompressImage(segment.getImage()));
                        image.scalePercent(10f);
                        document.add(image);
                    }
                    document.add(Chunk.NEWLINE);

                } catch(DocumentException | IOException e){
                    e.printStackTrace();
                }
        }
        document.close();
    }

}
