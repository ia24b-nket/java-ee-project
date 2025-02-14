package org.example.dailyplanner;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.*;
import java.io.IOException;
import java.time.LocalTime;

@WebServlet("/task")
public class TaskServlet extends HttpServlet {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("dailyplannerPU");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String startTimeStr = request.getParameter("startTime");  // Fix: startTime statt timeSlot
        String endTimeStr = request.getParameter("endTime");      // Fix: endTime hinzugefügt
        String username = (String) request.getSession().getAttribute("username");

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            User user;
            try {
                user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                        .setParameter("username", username)
                        .getSingleResult();
            } catch (NoResultException e) {
                response.sendRedirect("error.jsp");
                return;
            }

            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setStartTime(LocalTime.parse(startTimeStr));
            task.setEndTime(LocalTime.parse(endTimeStr));
            task.setUser(user);
            task.setCompleted(false);

            em.persist(task);
            em.getTransaction().commit();

            response.sendRedirect("dashboard.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        } finally {
            em.close();
        }
    }

    @Override
    public void destroy() {
        emf.close();
    }
}
