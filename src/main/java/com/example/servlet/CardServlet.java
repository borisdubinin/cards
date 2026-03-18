package com.example.servlet;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.models.Card;
import com.example.exceptions.CardNotFoundException;
import com.example.services.CardService;
import com.example.services.CardServiceImpl;

@WebServlet("/cards/*")
public class CardServlet extends HttpServlet {

    private final CardService cardService = new CardServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                handleGetAll(resp);
            } else {
                handleGetById(pathInfo, resp);
            }
        } catch (CardNotFoundException e) {
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                handleCreate(req, resp);
            } else if (pathInfo.endsWith("/block")) {
                handleBlock(pathInfo, resp);
            } else if (pathInfo.endsWith("/unblock")) {
                handleUnblock(pathInfo, resp);
            } else {
                sendError(resp, HttpServletResponse.SC_NOT_FOUND, "URL not found");
            }
        } catch (IllegalArgumentException e) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (CardNotFoundException e) {
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "ID is required");
                return;
            }
            handleUpdate(pathInfo, req, resp);
        } catch (IllegalArgumentException e) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (CardNotFoundException e) {
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "ID is required");
                return;
            }

            handleDelete(pathInfo, resp);
        } catch (CardNotFoundException e) {
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void handleGetAll(HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        JsonUtils.getMapper().writeValue(resp.getWriter(), cardService.getAllCards());
    }

    private void handleGetById(String pathInfo, HttpServletResponse resp) throws IOException {
        Long id = extractId(pathInfo);
        Card card = cardService.getCardById(id);

        resp.setContentType("application/json;charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        JsonUtils.getMapper().writeValue(resp.getWriter(), card);
    }

    private void handleCreate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Card card = JsonUtils.getMapper().readValue(req.getReader(), Card.class);
        cardService.createCard(card);

        resp.setContentType("application/json;charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    private void handleUpdate(String pathInfo, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = extractId(pathInfo);
        Card card = JsonUtils.getMapper().readValue(req.getReader(), Card.class);
        cardService.updateCard(id, card);

        resp.setContentType("application/json;charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void handleDelete(String pathInfo, HttpServletResponse resp) {
        Long id = extractId(pathInfo);
        cardService.deleteCard(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private void handleBlock(String pathInfo, HttpServletResponse resp) {
        Long id = extractId(pathInfo.replace("/block", ""));
        cardService.blockCard(id);

        resp.setContentType("application/json;charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void handleUnblock(String pathInfo, HttpServletResponse resp) {
        Long id = extractId(pathInfo.replace("/unblock", ""));
        cardService.unblockCard(id);

        resp.setContentType("application/json;charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private Long extractId(String pathInfo) {
        try {
            return Long.parseLong(pathInfo.replace("/", ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ID format");
        }
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
