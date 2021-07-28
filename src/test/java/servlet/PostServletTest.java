package servlet;



import dream.model.Post;
import dream.servlet.PostServlet;
import dream.store.MemStore;
import dream.store.PsqlStore;
import dream.store.Store;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PowerMockIgnore({"javax.xml.*", "javax.management.*", "dream.store.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class PostServletTest {

    @Test
    public void whenCreatePost() throws IOException {
        PsqlStore.instOf();
    }
}