
package com.car.platform.service;

import com.car.platform.models.Provider;
import java.io.InputStream;


public interface FileProcessorService {
    /**
     * Process the CSV content
     * @param is
     * @param provider 
     */
    void processFile(InputStream is, Provider provider) throws Exception;
}
