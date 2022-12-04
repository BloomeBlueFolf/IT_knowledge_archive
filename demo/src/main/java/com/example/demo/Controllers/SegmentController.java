package com.example.demo.Controllers;

import com.example.demo.Entities.Segment;
import com.example.demo.Impls.ChapterServiceImpl;
import com.example.demo.Impls.SegmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SegmentController {

    @Autowired
    private ChapterServiceImpl chapterService;

    @Autowired
    private SegmentServiceImpl segmentService;

    @GetMapping("/user/segments")
    public String showSegments(Model model,
                               @RequestParam ("id") long id){

        model.addAttribute(("chapter"), chapterService.getChapter(id));
        return "segments";
    }

    @GetMapping("/user/segment/addContent")
    public String addSegment(Model model,
                             @RequestParam ("id") long id){

        Segment segment = new Segment();
        model.addAttribute(("segment"), segment);
        model.addAttribute(("id"), id);
        return "addSegmentForm";
    }

    @PostMapping("/user/segment/addContent")
    public String addSegment(@ModelAttribute ("segment") Segment segment,
                             @RequestParam ("id") long id,
                             RedirectAttributes redirectAttributes){

        redirectAttributes.addAttribute(("id"), id);
        segmentService.assignSegmentToChapter(segment, chapterService.getChapter(id));
        return "redirect:/user/segments?addingSuccess";
    }
}
