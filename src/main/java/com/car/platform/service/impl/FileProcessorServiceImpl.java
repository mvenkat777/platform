
package com.car.platform.service.impl;

import com.car.platform.exception.InvalidRowException;
import com.car.platform.models.Listing;
import com.car.platform.models.Provider;
import com.car.platform.service.FileProcessorService;
import com.car.platform.service.ListingService;
import static com.car.platform.util.Util.getListing;
import static com.car.platform.util.Util.isHeaderRow;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class FileProcessorServiceImpl implements FileProcessorService {
    
    private final ListingService listingService;
    private final Validator validator;
    
    @Autowired
    public FileProcessorServiceImpl(ListingService listingService, Validator validator) {
        this.listingService = listingService;
        this.validator = validator;
    }

//    @Async
    @Override
    public void processFile(InputStream is, Provider provider) throws Exception {
        try(Reader reader = new BufferedReader(new InputStreamReader(is)); CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
            Iterator<CSVRecord> records = csvParser.iterator();
            List<Listing> listings = new ArrayList<>();
            while (records.hasNext()) {
                CSVRecord record = records.next();
                
                if (isHeaderRow(record))
                    continue;
                
                if (record.size() != 6)
                    throw new InvalidRowException(new StringBuilder("The row with code ").append(record.get(0)).append(" does not match the expected number of rows: 6").toString());
                
                // validate the records provided
                Listing listing = getListing(record, provider);
                Set<ConstraintViolation<Listing>> violations = validator.validate(listing);
                if (null != violations && !violations.isEmpty())
                    throw new ConstraintViolationException(violations);
                listings.add(listing);
            }
            
            // save all records
            listingService.saveListing(provider, listings);
        }
    }
    
}
