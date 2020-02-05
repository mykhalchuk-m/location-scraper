package com.mmm.util.location.locationscraper.loader;

import com.mmm.util.location.locationscraper.loader.dto.LocationDto;
import com.mmm.util.location.locationscraper.loader.dto.PostIndexLocationDto;
import com.mmm.util.location.locationscraper.loader.dto.VPZAddressDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CSVLoader {
    private static final String SEMICOLON_SEPARATOR = ";";
    private static final String COMA_SEPARATOR = ",";
    private static final String TAB_SEPARATOR = "\t";

    public List<LocationDto> loadLocationFromInputStream(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        return br.lines().skip(1).map(mapToLocation).collect(Collectors.toList());
    }

    public List<PostIndexLocationDto> localPostIndexesFromStream(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        return br.lines().map(mapToPostIndex).collect(Collectors.toList());
    }

    public List<VPZAddressDto> loadVPZAddressesFromStream(InputStream inputStream) {
        List<VPZAddressDto> vpzAddressDtos = new ArrayList<>();
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheet("VPZ");
            Iterator<Row> rowIterator = sheet.rowIterator();
            rowIterator.next(); // skip first row
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                vpzAddressDtos.add(VPZAddressDto.builder()
                        .postalCode(row.getCell(0).getStringCellValue())
                        .address(row.getCell(5).getStringCellValue())
                        .build());
            }
        } catch (IOException | InvalidFormatException e) {
            log.error(e.getMessage(), e);
        }
        return vpzAddressDtos;
    }

    private Function<String, LocationDto> mapToLocation = (line) -> {
        String[] values = line.split(SEMICOLON_SEPARATOR);
        return LocationDto.builder()
                .cyrillicRegion(values[0])
                .cyrillicDistrict(values[1])
                .cyrillicLocality(values[2])
                .postIndex(values[3])
                .cyrillicStreet(values[4])
                .buildingNumber(Arrays.asList(values[5].split(COMA_SEPARATOR)))
                .region(values[8])
                .district(values[9])
                .locality(values[10])
                .street(values[12])
                .build();
    };

    private Function<String, PostIndexLocationDto> mapToPostIndex = (line) -> {
        String[] values = line.split(TAB_SEPARATOR);
        return PostIndexLocationDto.builder()
                .countryCode(values[0])
                .postalCode(values[1])
                .localityCyrillicName(values[2])
                .localityName(values[3])
                .districtName(values[5])
                .latitude(Double.valueOf(values[9]))
                .longitude(Double.valueOf(values[10]))
                .coordinateAccuracy(values.length > 11 ? Integer.valueOf(values[11]) : 0)
                .build();
    };

}
