package com.team2.dbbuddy.controller;

import com.team2.dbbuddy.dto.ColumnMetaDTO;
import com.team2.dbbuddy.dto.TableMetaDTO;
import com.team2.dbbuddy.dto.UserDTO;
import com.team2.dbbuddy.service.MetaService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/meta")
@RequiredArgsConstructor
public class MetaController {

    private final MetaService metaService;
    private final com.team2.dbbuddy.service.SampleService sampleService; // Added for preview
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper(); // Added
                                                                                                                                // for
                                                                                                                                // preview
                                                                                                                                // json
                                                                                                                                // parsing

    // --- Table Management ---

    @GetMapping("/tables")
    public String tableList(HttpSession session, Model model) {
        UserDTO user = (UserDTO) session.getAttribute("loginUser");
        java.util.List<TableMetaDTO> tables = metaService.getTablesByUserId(user.getUserId());

        // Prepare Preview Data
        java.util.Map<Integer, java.util.List<java.util.Map<String, Object>>> previewData = new java.util.HashMap<>();
        java.util.Map<Integer, java.util.List<ColumnMetaDTO>> tableColumns = new java.util.HashMap<>();

        for (TableMetaDTO table : tables) {
            // Get Columns
            java.util.List<ColumnMetaDTO> cols = metaService.getColumnsByTableId(table.getTblId());
            tableColumns.put(table.getTblId(), cols);

            // Get Samples (Limit 3 for preview)
            java.util.List<com.team2.dbbuddy.dto.SampleDTO> samples = sampleService
                    .getSamplesByTableId(table.getTblId());
            if (samples.size() > 3)
                samples = samples.subList(0, 3);

            java.util.List<java.util.Map<String, Object>> rows = new java.util.ArrayList<>();
            for (com.team2.dbbuddy.dto.SampleDTO s : samples) {
                try {
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> map = objectMapper.readValue(s.getDataJson(), java.util.Map.class);
                    rows.add(map);
                } catch (Exception e) {
                }
            }
            previewData.put(table.getTblId(), rows);
        }

        model.addAttribute("tables", tables);
        model.addAttribute("tableColumns", tableColumns); // Columns for each table
        model.addAttribute("previewData", previewData); // Data rows for each table
        model.addAttribute("dataTypes", metaService.getAllDataTypes());
        return "meta/table_list";
    }

    @PostMapping("/tables/create")
    public String createTable(TableMetaDTO tableMetaDTO, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("loginUser");
        tableMetaDTO.setUserId(user.getUserId());
        metaService.createTable(tableMetaDTO);
        return "redirect:/meta/tables";
    }

    @GetMapping("/tables/{tblId}")
    public String tableDetail(@PathVariable Integer tblId, Model model) {
        model.addAttribute("table", metaService.getTableById(tblId));
        model.addAttribute("columns", metaService.getColumnsByTableId(tblId));
        model.addAttribute("dataTypes", metaService.getAllDataTypes());
        return "meta/table_detail";
    }

    // --- Column Management ---

    @PostMapping("/columns/create")
    public String createColumn(ColumnMetaDTO columnMetaDTO, @RequestParam(required = false) Boolean embedded,
            RedirectAttributes rttr) {
        metaService.addColumn(columnMetaDTO);
        rttr.addAttribute("tblId", columnMetaDTO.getTblId());
        if (embedded != null && embedded) {
            rttr.addAttribute("embedded", true);
        }
        return "redirect:/meta/tables/{tblId}";
    }

    @PostMapping("/columns/update")
    public String updateColumn(ColumnMetaDTO columnMetaDTO, @RequestParam(required = false) Boolean embedded,
            RedirectAttributes rttr) {
        metaService.modifyColumn(columnMetaDTO);
        rttr.addAttribute("tblId", columnMetaDTO.getTblId());
        if (embedded != null && embedded) {
            rttr.addAttribute("embedded", true);
        }
        return "redirect:/meta/tables/{tblId}";
    }

    @PostMapping("/columns/delete")
    public String deleteColumn(@RequestParam Integer colId, @RequestParam Integer tblId,
            @RequestParam(required = false) Boolean embedded, RedirectAttributes rttr) {
        metaService.removeColumn(colId);
        rttr.addAttribute("tblId", tblId);
        if (embedded != null && embedded) {
            rttr.addAttribute("embedded", true);
        }
        return "redirect:/meta/tables/{tblId}";
    }

    // --- API for AJAX ---
    // --- API for AJAX ---
    @PostMapping("/api/tables/create")
    @ResponseBody
    public org.springframework.http.ResponseEntity<?> createTableApi(
            @RequestBody com.team2.dbbuddy.dto.TableCreationRequestDTO request, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("loginUser");
        if (user == null) {
            return org.springframework.http.ResponseEntity.status(401)
                    .body("{\"success\":false, \"message\":\"Unauthorized\"}");
        }
        try {
            request.getTable().setUserId(user.getUserId());
            metaService.createTableWithColumns(request.getTable(), request.getColumns());
            return org.springframework.http.ResponseEntity.ok().body("{\"success\":true}");
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.badRequest()
                    .body("{\"success\":false, \"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/api/columns/create")
    @ResponseBody
    public org.springframework.http.ResponseEntity<?> createColumnApi(@RequestBody ColumnMetaDTO columnMetaDTO) {
        try {
            metaService.addColumn(columnMetaDTO);
            return org.springframework.http.ResponseEntity.ok().body("{\"success\":true}");
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.badRequest()
                    .body("{\"success\":false, \"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/api/tables/{tblId}/preview")
    @ResponseBody
    public org.springframework.http.ResponseEntity<?> getTablePreview(@PathVariable Integer tblId) {
        try {
            java.util.List<ColumnMetaDTO> cols = metaService.getColumnsByTableId(tblId);
            java.util.List<com.team2.dbbuddy.dto.SampleDTO> samples = sampleService.getSamplesByTableId(tblId);

            if (samples.size() > 3)
                samples = samples.subList(0, 3);

            java.util.List<java.util.Map<String, Object>> rows = new java.util.ArrayList<>();
            for (com.team2.dbbuddy.dto.SampleDTO s : samples) {
                try {
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> map = objectMapper.readValue(s.getDataJson(), java.util.Map.class);
                    rows.add(map);
                } catch (Exception e) {
                }
            }

            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("columns", cols);
            result.put("rows", rows);

            return org.springframework.http.ResponseEntity.ok(result);
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.badRequest().body(null);
        }
    }
}
