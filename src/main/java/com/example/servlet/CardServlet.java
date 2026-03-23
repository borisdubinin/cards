package com.example.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Card;
import com.example.exception.EntityNotFoundException;
import com.example.model.CardStatus;
import com.example.repository.CardRepository;
import com.example.service.CardService;
import com.example.service.CardServiceImpl;

@WebServlet("/cards/*")
public class CardServlet extends HttpServlet {

    private CardService cardService;

    @Override
    public void init() {
        this.cardService = new CardServiceImpl(new CardRepository());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String method = req.getMethod();

        if ("PATCH".equalsIgnoreCase(method)) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String[] pathSegments = splitPathInfo(req.getPathInfo());
            if (pathSegments.length == 0) {
                handleGetAll(resp);
            } else if (pathSegments.length == 1) {
                Long id = Long.parseLong(pathSegments[0]);
                handleGetById(id, resp);
            } else {
                throw new ResourceNotFoundException("URL not found");
            }
        } catch (Exception e) {
            handleErrorResponse(e, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String[] pathSegments = splitPathInfo(req.getPathInfo());
            if (pathSegments.length == 0) {
                handleCreate(req, resp);
            } else {
                throw new ResourceNotFoundException("URL not found");
            }
        } catch (Exception e) {
            handleErrorResponse(e, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String[] pathSegments = splitPathInfo(req.getPathInfo());
            if (pathSegments.length == 1) {
                Long id = Long.parseLong(pathSegments[0]);
                handleUpdate(id, req, resp);
            } else if (pathSegments.length == 0) {
                throw new BadRequestException("ID is required for update");
            } else {
                throw new ResourceNotFoundException("URL not found");
            }
        } catch (Exception e) {
            handleErrorResponse(e, resp);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String[] pathSegments = splitPathInfo(req.getPathInfo());
            if (pathSegments.length == 2) {
                if ("status".equals(pathSegments[1])) {
                    Long id = Long.parseLong(pathSegments[0]);
                    handleSetStatus(id, req, resp);
                } else {
                    throw new ResourceNotFoundException("URL not found");
                }
            } else if (pathSegments.length == 0) {
                throw new BadRequestException("ID is required for edit");
            } else {
                throw new ResourceNotFoundException("URL not found");
            }
        } catch (Exception e) {
            handleErrorResponse(e, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String[] pathSegments = splitPathInfo(req.getPathInfo());
            if (pathSegments.length == 1) {
                Long id = Long.parseLong(pathSegments[0]);
                handleDelete(id, resp);
            } else if (pathSegments.length == 0) {
                throw new BadRequestException("ID is required for delete");
            } else {
                throw new ResourceNotFoundException("URL not found");
            }
        } catch (Exception e) {
            handleErrorResponse(e, resp);
        }
    }

    private String[] splitPathInfo(String pathInfo) {
        if (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/")) {
            return new String[]{};
        }

        String normalized = pathInfo.replaceAll("^/|/$", "");
        if (normalized.isEmpty()) {
            return new String[]{};
        }
        return normalized.split("/");
    }

    private void handleGetAll(HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        JsonUtils.getMapper().writeValue(resp.getWriter(), cardService.getAll());
    }

    private void handleGetById(Long id, HttpServletResponse resp) throws IOException {
        Card card = cardService.getById(id);
        resp.setContentType("application/json;charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        JsonUtils.getMapper().writeValue(resp.getWriter(), card);
    }

    private void handleErrorResponse(Exception e, HttpServletResponse resp) throws IOException {
        int status;
        String message = e.getMessage();

        if (e instanceof BadRequestException || e instanceof NumberFormatException) {
            status = HttpServletResponse.SC_BAD_REQUEST;
        } else if (e instanceof EntityNotFoundException || e instanceof ResourceNotFoundException) {
            status = HttpServletResponse.SC_NOT_FOUND;
        } else {
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            message = "Internal server error: " + message;
            e.printStackTrace();
        }

        sendError(resp, status, message);
    }

    private void handleCreate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Card card = JsonUtils.getMapper().readValue(req.getReader(), Card.class);
        card = cardService.create(card);
        resp.setContentType("application/json;charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        JsonUtils.getMapper().writeValue(resp.getWriter(), card);
    }

    private void handleUpdate(Long id, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Card card = JsonUtils.getMapper().readValue(req.getReader(), Card.class);
        card = cardService.update(id, card);
        resp.setContentType("application/json;charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        JsonUtils.getMapper().writeValue(resp.getWriter(), card);
    }

    private void handleSetStatus(Long id, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CardStatus newStatus = JsonUtils.getMapper().readValue(req.getReader(), Card.class).getStatus();
        Card editedCard = cardService.changeStatus(id, newStatus);
        resp.setContentType("application/json;charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        JsonUtils.getMapper().writeValue(resp.getWriter(), editedCard);
    }

    private void handleDelete(Long id, HttpServletResponse resp) {
        cardService.delete(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private void sendError(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json;charset=UTF-8");
        JsonUtils.getMapper().writeValue(resp.getWriter(), new ErrorResponse(status, message));
    }

    private static class ErrorResponse {

        public int status;
        public String message;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }
    }
}
