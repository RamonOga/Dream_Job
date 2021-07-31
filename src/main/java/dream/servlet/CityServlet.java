package dream.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dream.model.City;
import dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class CityServlet extends HttpServlet {

    private static final Gson GSON = new GsonBuilder().create();

    private static final List<City> cites = PsqlStore.instOf().findAllCites();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> cList = cites.stream().map(City::getName).collect(Collectors.toList());
        resp.setContentType("application/json; charset=utf-8");
        OutputStream output = resp.getOutputStream();
        String json = GSON.toJson(cList);
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }
}
