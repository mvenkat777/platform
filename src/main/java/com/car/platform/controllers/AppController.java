
package com.car.platform.controllers;

//import com.car.platform.config.ApplicationProperties;
import com.car.platform.dto.ListingRequest;
import com.car.platform.models.Dealer;
import com.car.platform.models.Listing;
import com.car.platform.models.Provider;
import com.car.platform.service.DealerService;
import com.car.platform.service.FileProcessorService;
import com.car.platform.service.ListingService;
import com.car.platform.service.ProviderService;
import static com.car.platform.util.Util.validFileExtension;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static java.util.stream.Collectors.toList;


@RestController
@Slf4j
@Validated
public class AppController {
    
    @Autowired
    private ProviderService providerService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private ListingService listingService;
    @Autowired
    private FileProcessorService fileProcessorService;
    
    /**
     * Upload CSV for processing
     * @param dealerId
     * @param file
     * @param providerName
     * @return
     * @throws Exception 
     */
    @PostMapping("/upload_csv/{dealerId}")
    public ResponseEntity uploadCsv(@PathVariable @Min(value = 1, message = "Dealer ID must not be less than one (1)") Long dealerId,
                                    @RequestParam(value = "file", required = false) @NotNull(message = "Please upload the vehicles listing file") MultipartFile file,
                                    @RequestParam(value = "provider", required = false) @NotEmpty(message = "Please enter the provider for this listing") String providerName) throws Exception {
        // do some validation checks
        if (file.isEmpty() || null == file.getOriginalFilename() || file.getOriginalFilename().trim().isEmpty()) 
            return ResponseEntity.badRequest().body("Please upload a CSV file");
        
        if (!validFileExtension(file))
            return ResponseEntity.badRequest().body("The file must be in .csv format");
        
        // check if this provider and dealer exists
        Dealer dealer = dealerService.findDealerById(dealerId);
        if (null == dealer)
            return ResponseEntity.badRequest().body("This dealer does not exist");
        
        Provider provider = providerService.getByProviderNameAndDealer(dealer, providerName);
        if (null == provider)
            return ResponseEntity.badRequest().body(new StringBuilder("This provider does not exist for ").append(dealer.getName()).toString());
        
        fileProcessorService.processFile(file.getInputStream(), provider);
        return ResponseEntity.ok("Listings successfully uploaded");
    }
    
    /**
     * Saves dealers listings
     * @param request
     * @param result
     * @return 
     */
    @PostMapping("/vehicle_listings")
    public ResponseEntity uploadListing(@Valid @RequestBody ListingRequest request, BindingResult result) {
        
        // check if this provider and dealer exists
        Dealer dealer = dealerService.findDealerById(request.getDealer());
        
        if (null == dealer)
            return ResponseEntity.badRequest().body("This dealer does not exist");

        if (null == providerService.getByProviderNameAndDealer(dealer, request.getProvider()))
            return ResponseEntity.badRequest().body(new StringBuilder("This provider does not exist for ").append(dealer.getName()).toString());

        Provider provider = providerService.getByProviderNameAndDealer(dealer, request.getProvider());
        List<Listing> listings = request.getListings().stream().map((l) -> {
            Listing listing = new Listing();
            listing.setCode(l.getCode());
            listing.setColor(l.getColor());
            listing.setKw(l.getKW());
            listing.setMake(l.getMake());
            listing.setModel(l.getModel());
            listing.setPrice(l.getPrice());
            listing.setYear(l.getYear());
            listing.setProvider(provider);
            return listing;
        }).collect(toList());

        listingService.saveListing(provider, listings);

        return ResponseEntity.ok("Listings successfully saved");
    }
    
    
    /**
     * Show listing by queries
     * @param query {optional} -  required for searching specific parameters
     * @return 
     */
   @GetMapping("/search")
    public ResponseEntity showAllListing(@RequestParam(value = "query", required = false) String query) {
        if (null == query)
            return ResponseEntity.ok(listingService.getAllListings());
        else
            return ResponseEntity.ok(listingService.searchByQueryParameter(query));
    }
}
