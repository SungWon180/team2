package com.team2.dbbuddy.service;

import com.team2.dbbuddy.dto.SampleDTO;
import com.team2.dbbuddy.dto.ColumnMetaDTO; // Added
import com.team2.dbbuddy.mapper.MetaMapper; // Added
import com.team2.dbbuddy.mapper.SampleMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleMapper sampleMapper;
    private final MetaMapper metaMapper; // Added for column info
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional(readOnly = true)
    public List<SampleDTO> getSamplesByTableId(Integer tblId) {
        return sampleMapper.findAll(tblId);
    }

    @Transactional(readOnly = true)
    public SampleDTO getSampleById(Integer id) {
        return sampleMapper.findById(id);
    }

    @Transactional
    public void createSample(Integer tblId, Map<String, Object> dataMap) {
        // 1. Meta Info
        List<ColumnMetaDTO> columns = metaMapper.findColumnsByTableId(tblId);

        // 2. Handle Auto Increment & Type Conversion
        for (ColumnMetaDTO col : columns) {
            String colName = col.getColNm();

            // Auto Increment Logic
            if ("Y".equals(col.getIsAutoIncrement())) {
                // Ignore user input for AI column, generate new ID
                int nextId = getNextId(tblId, colName); // Helper method needed
                dataMap.put(colName, nextId);
            }
            // If PK and not AI, ensure it's present (Validation)
            else if ("Y".equals(col.getIsPk())) {
                if (!dataMap.containsKey(colName) || dataMap.get(colName) == null
                        || dataMap.get(colName).toString().isEmpty()) {
                    throw new IllegalArgumentException("Primary Key " + colName + " cannot be empty.");
                }
            }
        }

        // 3. Check PK Uniqueness
        checkPkUniqueness(tblId, dataMap, columns);

        try {
            SampleDTO sampleDTO = new SampleDTO();
            sampleDTO.setTblId(tblId);
            sampleDTO.setDataJson(objectMapper.writeValueAsString(dataMap));
            sampleDTO.setActiveFl("Y");
            sampleMapper.save(sampleDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON processing error", e);
        }
    }

    private void checkPkUniqueness(Integer tblId, Map<String, Object> newData, List<ColumnMetaDTO> columns) {
        List<SampleDTO> existingSamples = sampleMapper.findAll(tblId); // Fixed method name

        for (SampleDTO sample : existingSamples) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> existingData = objectMapper.readValue(sample.getDataJson(), Map.class);

                boolean match = true;
                boolean hasPk = false;
                for (ColumnMetaDTO col : columns) {
                    if ("Y".equals(col.getIsPk())) {
                        hasPk = true;
                        String pkCol = col.getColNm();
                        String newVal = String.valueOf(newData.get(pkCol));
                        String oldVal = String.valueOf(existingData.get(pkCol));
                        if (!newVal.equals(oldVal)) {
                            match = false;
                            break;
                        }
                    }
                }

                if (hasPk && match) {
                    throw new IllegalArgumentException("Duplicate Primary Key(s) found.");
                }

            } catch (JsonProcessingException e) {
                // Ignore parsing errors of bad data
            }
        }
    }

    private int getNextId(Integer tblId, String colName) {
        List<SampleDTO> samples = sampleMapper.findAll(tblId); // Fixed method name
        int max = 0;
        for (SampleDTO sample : samples) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> data = objectMapper.readValue(sample.getDataJson(), Map.class);
                if (data.containsKey(colName)) {
                    int val = Integer.parseInt(data.get(colName).toString());
                    if (val > max)
                        max = val;
                }
            } catch (Exception e) {
                // Ignore
            }
        }
        return max + 1;
    }

    @Transactional
    public void updateSample(Integer sampleId, Map<String, Object> dataMap) {
        try {
            // Should also check PK immutability or allow update if unique?
            // For now, simple update, but ideally PK shouldn't change or be validated.

            SampleDTO sampleDTO = new SampleDTO();
            sampleDTO.setSampleId(sampleId);
            sampleDTO.setDataJson(objectMapper.writeValueAsString(dataMap));
            sampleMapper.update(sampleDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON processing error", e);
        }
    }

    @Transactional
    public void deleteSample(Integer id) {
        sampleMapper.delete(id);
    }
}
