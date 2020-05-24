package com.car.platform.controllers;

import com.car.platform.config.ApplicationProperties;
import com.car.platform.dto.ListingSearch;
import com.car.platform.exception.InvalidRowException;
import com.car.platform.models.Dealer;
import com.car.platform.models.Listing;
import com.car.platform.models.Provider;
import com.car.platform.service.DealerService;
import com.car.platform.service.FileProcessorService;
import com.car.platform.service.ListingService;
import com.car.platform.service.ProviderService;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringRunner.class)
@WebMvcTest(AppController.class)
@Slf4j
public class AppControllerTest {
    
    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext wac; 
    @MockBean
    private ProviderService providerService;
    @MockBean
    private DealerService dealerService;
    @MockBean
    private ApplicationProperties appProperties;
    @MockBean
    private ListingService listingService;
    
    @MockBean
    private FileProcessorService fileProcessorService;
    private String fileContent;
    
    @Before
    public void setup() { 
        try {
            mvc = MockMvcBuilders.webAppContextSetup(wac).build();
            fileContent = IOUtils.toString(this.getClass().getResourceAsStream("/testcsv.csv"), "UTF-8");
        } catch (IOException ex) {
            log.error("Unable to read csv file ", ex);
        }
    }


    /**
     * Get All Listings
     * @throws Exception
     */
    @Test
    public void getAllListing_whenSearchQuery_thenReturnOk() throws Exception {

        List<Listing> listings = new ArrayList<>();
        Listing listing1 = new Listing();
        listing1.setCode("a");
        listing1.setColor("red");
        listing1.setKw(123);
        listing1.setMake("mercedes");
        listing1.setModel("e300");
        listing1.setPrice(new BigDecimal(1000));
        listing1.setYear(2019);
        listings.add(listing1);

        when(listingService.getAllListings()).thenReturn(listings);

        mvc.perform(get("/search")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].make", Matchers.is("mercedes")));
    }

    /**
     * Get Listings by make
     * @throws Exception
     */
    @Test
    public void getListing_QueryByMake_thenReturnOk() throws Exception {

        List<ListingSearch> listings = new ArrayList<>();
        ListingSearch listing = new ListingSearch("vw", "volks", "passat", 28, 2019, "red", new BigDecimal(2000));
        listings.add(listing);

        when(listingService.searchByQueryParameter("volks")).thenReturn(listings);

        mvc.perform(get("/search?query=volks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].make", Matchers.is("volks")));
    }

    /**
     * Save Listing Test case for invalid request payload given
     * @throws Exception
     */
    @Test
    public void saveListing_withInvalidPayload_thenReturnBadGateway() throws Exception {

        String requestStr = "{ \"dealer\": 1, \"provider\": \"BMW Provider\", \"listings\": [] }";

        mvc.perform(post("/vehicle_listings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestStr))
                .andExpect(status().isBadGateway())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(jsonPath("$.message", Matchers.is("Invalid Parameter(s) Provided")))
                .andExpect(jsonPath("$.details[0]", Matchers.is("[], Invalid listings provided")));
    }

    /**
     * Save listings via with valid JSON data
     * @throws Exception
     */
    @Test
    public void saveListing_withValidPayload_thenReturnOk() throws Exception {

        String res = "Listings successfully saved";

        String requestStr = "{ \"dealer\": 1, \"provider\": \"BMW Provider\", \"listings\": [ { \"code\": \"a\", \"make\": \"renault\", \"model\": \"megane\", \"kW\": 132, \"year\": 2014, \"color\": \"red\", \"price\": 13990 } ] }";

        when(dealerService.findDealerById(any())).thenReturn(new Dealer());
        when(providerService.getByProviderNameAndDealer(any(), any())).thenReturn(new Provider());

        MvcResult result = mvc.perform(post("/vehicle_listings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestStr))
                .andExpect(status().isOk())
                .andExpect(content().string(res)).andReturn();

        verify(dealerService, times(1)).findDealerById(any());
        assertEquals(result.getResponse().getContentAsString(), res);
    }



    /**
     * Test case for valid CSV file
     * @throws Exception 
     */
    @Test
    public void uploadCsv_whenValidCsv_thenReturnOk() throws Exception {

        String res = "Listings successfully uploaded";

        when(dealerService.findDealerById(any())).thenReturn(new Dealer());
        when(providerService.getByProviderNameAndDealer(any(), any())).thenReturn(new Provider());
        doNothing().when(fileProcessorService).processFile(any(), any());
        
         MockMultipartFile mockMultipartFile = new MockMultipartFile("file","testcsv.csv",
                                                            "application/octet-stream", fileContent.getBytes());
        
         MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.fileUpload("/upload_csv/1")
                                                                        .file(mockMultipartFile)
                                                                        .param("provider", "Chevrolet Provider");
         
         MvcResult result = mvc.perform(requestBuilder)
//                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(content().string(res)).andReturn();
                ;
         // verify that the services are called
         verify(dealerService, times(1)).findDealerById(any());
         verify(providerService, times(1)).getByProviderNameAndDealer(any(), any());
         verify(fileProcessorService, times(1)).processFile(any(), any());
         assertEquals(result.getResponse().getContentAsString(), res);
    }
    
    /**
     * When no file/provider detail provided
     * @throws Exception 
     */
    @Test
    public void uploadCsv_whenNoFileAndNoProvider_thenReturnBadGateway() throws Exception {
        
        MockMultipartFile mockMultipartFile = new MockMultipartFile("filezzzz","testcsv.csv",
                                                            "application/octet-stream", fileContent.getBytes());
        
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.fileUpload("/upload_csv/1")
                                                                        .file(mockMultipartFile)
                                                                        .param("provider", "Chevrolet Provider");
        mvc.perform(requestBuilder)
//            .andDo(print())
            .andExpect(status().isBadGateway())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.timestamp", Matchers.notNullValue()))
            .andExpect(jsonPath("$.message", Matchers.notNullValue()))
            .andExpect(jsonPath("$.details", Matchers.notNullValue()));
        
        MockMultipartFile mockProviderMultipartFile = new MockMultipartFile("file","testcsv.csv",
                                                            "application/octet-stream", fileContent.getBytes());
        
        MockHttpServletRequestBuilder providerRequestBuilder = MockMvcRequestBuilders.fileUpload("/upload_csv/1")
                                                                        .file(mockProviderMultipartFile);
        mvc.perform(providerRequestBuilder)
//            .andDo(print())
            .andExpect(status().isBadGateway())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.timestamp", Matchers.notNullValue()))
            .andExpect(jsonPath("$.message", Matchers.notNullValue()))
            .andExpect(jsonPath("$.details", Matchers.notNullValue()));
    }
    
    /**
     * Test case for invalid dealer ID
     * @throws Exception 
     */
    @Test
    public void uploadCsv_whenInvalidDealerIdProvided_thenReturnBadGateway() throws Exception {
        
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file","testcsv.csv",
                                                            "application/octet-stream", fileContent.getBytes());
        
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.fileUpload("/upload_csv/-100")
                                                                        .file(mockMultipartFile)
                                                                        .param("provider", "Chevrolet Provider");
        mvc.perform(requestBuilder)
            .andExpect(status().isBadGateway())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.timestamp", Matchers.notNullValue()))
            .andExpect(jsonPath("$.message", Matchers.is("Invalid Parameter(s) Provided")))
            .andExpect(jsonPath("$.details[0]", Matchers.is("-100, Dealer ID must not be less than one (1)")));
    }
}
