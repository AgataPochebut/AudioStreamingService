//package com.epam;
//
//import com.epam.controller.GenreController;
//import com.epam.service.repository.GenreService;
//import org.dozer.Mapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.test.context.support.WithUserDetails;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.util.ArrayList;
//
//import static org.mockito.Mockito.when;
//
////@SpringBootTest
////@AutoConfigureMockMvc
//@WebMvcTest(value = GenreController.class,
//        excludeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
////@TestPropertySource("/application-test.yml")
//@WithUserDetails(value = "dru")
//public class ControllerUnitTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private GenreService songService;
//
//    @MockBean
//    private Mapper mapper;
//
//    @Test
//    public void testBasic() throws Exception {
//        when(songService.findAll()).thenReturn(new ArrayList<>());
//        testSync("/genres");
//    }
//
////    @Test
////    public void testFuture() throws Exception {
////        testAsync("/songs/future");
////    }
//
//    private void testSync(String route) throws Exception {
//
//        mockMvc.perform(MockMvcRequestBuilders.get(route))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    private void testAsync(String route) throws Exception {
//
//        MvcResult resultActions = mockMvc.perform(MockMvcRequestBuilders.get(route))
//                .andExpect(MockMvcResultMatchers.request().asyncStarted())
//                .andReturn();
//
//        mockMvc.perform(MockMvcRequestBuilders.asyncDispatch(resultActions))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//}
