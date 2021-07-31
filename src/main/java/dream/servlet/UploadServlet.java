package dream.servlet;

import dream.model.Candidate;
import dream.store.PsqlStore;
import dream.store.Store;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> images = new ArrayList<>();
        String folder = "e:\\projects\\files\\images"
                + File.separator
                + "user_"
                + req.getParameter("id");
        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }
        for (File name : folderFile.listFiles()) {
            if (!name.isDirectory()) {
                images.add(name.getName());
            }
        }
        req.setAttribute("images", images);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/upload.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> items = upload.parseRequest(req);
            File folder = new File("e:\\projects\\files\\images"
                                            + File.separator
                                            + "user_"
                                            + req.getParameter("id"));
            if (!folder.exists()) {
                folder.mkdir();
            }

            Store store = PsqlStore.instOf();
            String id = req.getParameter("id");
            Candidate candidate = store.findCandidateById(Integer.parseInt(id));

            for (FileItem item : items) {
                if (!item.isFormField()) {
                    File file = new File(folder + File.separator + id + "_" +  item.getName());
                    store.addCandidatePhoto(candidate, file);
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        out.write(item.getInputStream().readAllBytes());
                    }
                }
            }


        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        doGet(req, resp);
    }
}