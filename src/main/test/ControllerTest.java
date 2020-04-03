import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
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
        testSync("/time/basic");
    }

    @Test
    public void testResponseEntity() throws Exception {
        testSync("/time/re");
    }

    @Test
    public void testCallable() throws Exception {
        testAsync("/time/callable");
    }

    @Test
    public void testDeferred() throws Exception {
        testAsync("/time/deferred");
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
    }
}
