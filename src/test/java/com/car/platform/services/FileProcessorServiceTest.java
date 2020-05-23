
package com.car.platform.services;

import com.car.platform.exception.InvalidRowException;
import com.car.platform.models.Dealer;
import com.car.platform.models.Provider;
import com.car.platform.service.FileProcessorService;
import com.car.platform.service.ListingService;
import com.car.platform.service.impl.FileProcessorServiceImpl;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import javax.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@Slf4j
public class FileProcessorServiceTest {
    
    private ListingService listingService;
    private Validator validator;
    private FileProcessorService fileProcessorService;
    private String validCsvFileContent;
    private String invalidCsvFileContent;
    
    @Before
    public void setup() {
        listingService = mock(ListingService.class);
        validator = mock(Validator.class);
        fileProcessorService = new FileProcessorServiceImpl(listingService, validator);
    }
    
    
    /**
     * Test case for valid file upload
     * @throws Exception 
     */
    @Test(expected = Test.None.class)
    public void processFile_whenValidFile_thenReturnNothing() throws Exception {
        // get file details
        try {
            validCsvFileContent = IOUtils.toString(this.getClass().getResourceAsStream("/testcsv.csv"), "UTF-8");
        } catch (IOException ex) {
            log.error("Unable to read csv file ", ex);
        }
        // mock dependencies
        when(validator.validate(any())).thenReturn(new HashSet<>());
        doNothing().when(listingService).saveListing(any(), any());
        
        // make the service call
        InputStream is = IOUtils.toInputStream(validCsvFileContent, "UTF-8");
        fileProcessorService.processFile(is, stubProvider());
    }
    
    /**
     * Test case for invalid file upload
     * @throws Exception 
     */
    @Test(expected = InvalidRowException.class)
    public void processFile_whenInValidFile_thenReturnNothing() throws Exception {
        // get file details
        try {
            invalidCsvFileContent = IOUtils.toString(this.getClass().getResourceAsStream("/testcsv_other.csv"), "UTF-8");
        } catch (IOException ex) {
            log.error("Unable to read csv file ", ex);
        }
        // mock dependencies
        when(validator.validate(any())).thenReturn(new HashSet<>());
        doNothing().when(listingService).saveListing(any(), any());
        
        // make the service call
        InputStream is = IOUtils.toInputStream(invalidCsvFileContent, "UTF-8");
        fileProcessorService.processFile(is, stubProvider());
    }

    public static Provider stubProvider() {
        return new Provider(1L, new Dealer(1L, "Benz", new Date(), new Date()), "Benz Provider", new Date(), new Date());
    }
}
