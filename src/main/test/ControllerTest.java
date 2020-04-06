import com.epam.controller.SongController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
@WebMvcTest(SongController.class)
public class ControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testBasic() throws Exception {
        testSync("/songs");
    }

    @Test
    public void testCallable() throws Exception {
        testAsync("/songs/callable");
    }

    @Test
    public void testDeferred() throws Exception {
        testAsync("/songs/deferred");
    }

    private void testSync(String route) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(route))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void testAsync(String route) throws Exception {
        MvcResult resultActions = mockMvc.perform(MockMvcRequestBuilders.get(route))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.asyncDispatch(resultActions))
                .andExpect(MockMvcResultMatchers.status().isOk());
//        .andExpect(jsonPath("$.responses[0].status").value(200))
//                .andExpect(jsonPath("$.responses[0].duration").isNumber())
//                .andExpect(jsonPath("$.responses[0].body.celsius").value(25.0))
//                .andExpect(jsonPath("$.responses[1].body.celsius").value(15.0))
//                .andExpect(jsonPath("$.responses[2].body.celsius").value(30.0))
//                .andExpect(jsonPath("$.responses[3].body.celsius").value(10.0))
//                .andExpect(jsonPath("$.responses[4].body.lat").value(52.087515))
//                .andExpect(jsonPath("$.responses[5].body.lat").value(51.5285578))
//                .andExpect(jsonPath("$.responses[6].body.lat").value(-33.873061))
//                .andExpect(jsonPath("$.responses[7].body.lat").value(52.372333))
//                .andExpect(jsonPath("$.duration", Matchers.lessThan(750))); //Should be around 500ms.
    }
}
