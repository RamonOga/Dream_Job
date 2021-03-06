<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="dream.store.PsqlStore" %>
<%@ page import="dream.model.Post" %>
<%@ page import="dream.model.Candidate" %>
<%@ page import="dream.model.User" %>
<%@ page import="dream.model.CandidateVisitors" %>
<%@ page import="dream.store.Store" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Работа мечты</title>
</head>
<script src="https://code.jquery.com/jquery-3.4.1.min.js" ></script>

<script>
    $(document).ready(function () {
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/Dream_Job/city',
            dataType: 'json',
            success: function(data) {
            let cities = '';
            for (let i = 0; i < data.length; i++ ) {
                cities += "<option value="+data[i]['id'] + ">" + data[i]['name'] + "</option>";
            }
            $('#cityList').html(cities);
        }
        });
    });
</script>
<body>


<%
    String id = request.getParameter("id");
    Candidate candidate = new Candidate(0,"");
    CandidateVisitors visitors = new CandidateVisitors(0,candidate,0);
    Store store = PsqlStore.instOf();
    if (id != null) {
        candidate = PsqlStore.instOf().findCandidateById(Integer.parseInt(id));
        store.incrementVisitors(candidate);
        visitors = store.findCandidateVisitorsByCandidateId(candidate);
    }
    HttpSession hs = request.getSession();
    User user = (User) hs.getAttribute("user");
%>
<div class="container pt-3">
    <div class="row">
        <ul class="nav">
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/posts.do">Вакансии</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/candidates.do">Кандидаты</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/post/edit.jsp">Добавить вакансию</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/candidate/edit.jsp">Добавить кандидата</a>
            </li>
               <% if (user == null) { %>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/login.jsp">Войти</a>
            </li>
                <% } else { %>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/login.jsp">  <%=user.getName()%> | Выйти</a>
            </li>
                <% } %>

        </ul>
    </div>
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <% if (id == null) { %>
                Новый кандидат.
                <% } else { %>
                Редактирование вакансии. <br> Количество просмотров кандидата = <%=visitors.getCount()%>
                <% } %>
            </div>
            <div class="card-body">
                <form action="<%=request.getContextPath()%>/candidates.do?id=<%=candidate.getId()%>" method="post">
                    <div class="form-group">
                        <label>Имя</label>
                        <input type="text" class="form-control" name="name" value ="<%=candidate.getName()%>">
                    </div>

                    <div class="form-group"  >
                        <label for="cityList">Город</label>
                        <select class="form-control" id="cityList" name="cityList">
                            <option> </option>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary">Сохранить</button>
                </form>

            </div>
        </div>

    </div>

</div>
</body>
</html>
