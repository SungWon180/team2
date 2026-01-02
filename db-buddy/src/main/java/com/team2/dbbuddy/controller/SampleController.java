package com.team2.dbbuddy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.dbbuddy.dto.ColumnMetaDTO;
import com.team2.dbbuddy.dto.SampleDTO;
import com.team2.dbbuddy.dto.TableMetaDTO;
import com.team2.dbbuddy.service.MetaService;
import com.team2.dbbuddy.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpSession;
import com.team2.dbbuddy.dto.UserDTO;

@Controller
@RequestMapping("/samples")
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;
    private final MetaService metaService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public String list(@RequestParam(required = false) Integer tblId, Model model, HttpSession session) {
        if (tblId == null) {
            UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
            if (loginUser != null) {
                List<TableMetaDTO> tables = metaService.getTablesByUserId(loginUser.getUserId());
                if (!tables.isEmpty()) {
                    return "redirect:/samples?tblId=" + tables.get(0).getTblId();
                } else {
                    return "redirect:/meta/tables";
                }
            }
            return "redirect:/user/login";
        }

        TableMetaDTO table = metaService.getTableById(tblId);
        List<ColumnMetaDTO> columns = metaService.getColumnsByTableId(tblId);
        List<SampleDTO> samples = sampleService.getSamplesByTableId(tblId);

        // Convert JSON string to Map for display
        List<Map<String, Object>> sampleDataList = new ArrayList<>();
        for (SampleDTO sample : samples) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> data = objectMapper.readValue(sample.getDataJson(), Map.class);
                data.put("_sampleId", sample.getSampleId()); // Internal ID
                sampleDataList.add(data);
            } catch (JsonProcessingException e) {
                // Ignore parsing error or handle logging
            }
        }

        model.addAttribute("table", table);
        model.addAttribute("columns", columns);
        model.addAttribute("samples", sampleDataList);
        model.addAttribute("dataTypes", metaService.getAllDataTypes());
        return "sample/list";
    }

    @GetMapping("/form")
    public String form(@RequestParam Integer tblId, @RequestParam(required = false) Integer sampleId, Model model) {
        TableMetaDTO table = metaService.getTableById(tblId);
        List<ColumnMetaDTO> columns = metaService.getColumnsByTableId(tblId);

        model.addAttribute("table", table);
        model.addAttribute("columns", columns);

        if (sampleId != null) {
            SampleDTO sample = sampleService.getSampleById(sampleId);
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> data = objectMapper.readValue(sample.getDataJson(), Map.class);
                model.addAttribute("data", data);
                model.addAttribute("sampleId", sampleId);
            } catch (JsonProcessingException e) {
                // Handle error
            }
        }

        return "sample/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam Integer tblId, @RequestParam(required = false) Integer sampleId,
            @RequestParam(required = false) Boolean embedded,
            @RequestParam Map<String, String> allParams) {
        // Filter out system params and convert String to Object if necessary (Int
        // parsing)
        // For simplicity, we store everything as String or try basic parsing based on
        // logic if we had column info.
        // But since we receive Map<String, String> from request, and Service takes
        // Map<String, Object>, casting is fine.

        Map<String, Object> dataMap = new HashMap<>(allParams);
        dataMap.remove("tblId");
        dataMap.remove("sampleId");
        dataMap.remove("embedded"); // Remove embedded so it's not saved as data

        if (sampleId == null) {
            sampleService.createSample(tblId, dataMap);
        } else {
            sampleService.updateSample(sampleId, dataMap);
        }

        String redirectUrl = "redirect:/samples?tblId=" + tblId;
        if (embedded != null && embedded) {
            redirectUrl += "&embedded=true";
        }
        return redirectUrl;
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Integer sampleId, @RequestParam Integer tblId,
            @RequestParam(required = false) Boolean embedded) {
        sampleService.deleteSample(sampleId);
        String redirectUrl = "redirect:/samples?tblId=" + tblId;
        if (embedded != null && embedded) {
            redirectUrl += "&embedded=true";
        }
        return redirectUrl;
    }

    // --- API for AJAX ---
    @PostMapping("/api/save")
    @ResponseBody
    public org.springframework.http.ResponseEntity<?> saveApi(@RequestBody Map<String, Object> allParams) {
        try {
            Integer tblId = Integer.parseInt(allParams.get("tblId").toString());
            Integer sampleId = allParams.containsKey("sampleId") && allParams.get("sampleId") != null
                    ? Integer.parseInt(allParams.get("sampleId").toString())
                    : null;

            Map<String, Object> dataMap = new HashMap<>(allParams);
            dataMap.remove("tblId");
            dataMap.remove("sampleId");

            if (sampleId == null) {
                sampleService.createSample(tblId, dataMap);
            } else {
                sampleService.updateSample(sampleId, dataMap);
            }
            return org.springframework.http.ResponseEntity.ok().body("{\"success\":true}");
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.badRequest()
                    .body("{\"success\":false, \"message\":\"" + e.getMessage() + "\"}");
        }
    }
}
