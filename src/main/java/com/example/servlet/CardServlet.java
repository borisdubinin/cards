package com.example.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.converter.CardConverter;
import com.example.dto.CardRequestDto;
import com.example.dto.CardResponseDto;
import com.example.dto.ErrorResponseDto;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Card;
import com.example.exception.EntityNotFoundException;
import com.example.model.CardStatus;
import com.example.repository.CardRepository;
import com.example.service.CardService;
import com.example.service.CardServiceImpl;
import com.example.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;

@WebServlet("/cards/*")
public class CardServlet extends HttpServlet {

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private CardService cardService;
    private CardConverter converter;

    @Override
    public void init() {
        this.cardService = new CardServiceImpl(new CardRepository());
        this.converter = new CardConverter();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String method = req.getMethod();

            if ("PATCH".equalsIgnoreCase(method)) {
                doPatch(req, resp);
            } else {
                super.service(req, resp);
            }
        } catch (Exception e) {
            getServletContext().log("Request processing error", e);
            handleErrorResponse(e, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String[] pathSegments = splitPathInfo(req.getPathInfo());
            if (pathSegments.length == 0) {
                handleGetAll(resp);
            } else if (pathSegments.length == 1) {
                Long id = Long.parseLong(pathSegments[0]);
                handleGetById(id, resp);
            } else {
                throw new ResourceNotFoundException("URL not found");
            }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String[] pathSegments = splitPathInfo(req.getPathInfo());
            if (pathSegments.length == 0) {
                handleCreate(req, resp);
            } else {
                throw new ResourceNotFoundException("URL not found");
            }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
            String[] pathSegments = splitPathInfo(req.getPathInfo());
            if (pathSegments.length == 1) {
                Long id = Long.parseLong(pathSegments[0]);
                handleDelete(id, resp);
            } else if (pathSegments.length == 0) {
                throw new BadRequestException("ID is required for delete");
            } else {
                throw new ResourceNotFoundException("URL not found");
            }
    }

    private String[] splitPathInfo(String pathInfo) {
        if (StringUtils.isBlank(pathInfo) || pathInfo.equals("/")) {
            return new String[0];
        }
        String normalized = pathInfo.replaceAll("^/|/$", "");
        return normalized.isEmpty() ? new String[0] : normalized.split("/");
    }

    private void handleGetAll(HttpServletResponse resp) throws IOException {
        resp.setContentType(CONTENT_TYPE);
        resp.setStatus(HttpServletResponse.SC_OK);
        List<Card> allCards = cardService.getAll();
        List<CardResponseDto> cardResponseDto = converter.toDto(allCards);
        JsonUtils.writeValue(resp.getWriter(), cardResponseDto);
    }

    private void handleGetById(Long id, HttpServletResponse resp) throws IOException {
        CardResponseDto cardResponseDto = converter.toDto(cardService.getById(id));
        resp.setContentType(CONTENT_TYPE);
        resp.setStatus(HttpServletResponse.SC_OK);
        JsonUtils.writeValue(resp.getWriter(), cardResponseDto);
    }

    private void handleErrorResponse(Exception e, HttpServletResponse resp) throws IOException {
        String message = e.getMessage();
        int status = mapExceptionToStatus(e);
        sendError(resp, status, message);
    }

    private static int mapExceptionToStatus(Exception e) {
        return switch (e) {
            case BadRequestException _, NumberFormatException _ -> HttpServletResponse.SC_BAD_REQUEST;
            case EntityNotFoundException _, ResourceNotFoundException _ -> HttpServletResponse.SC_NOT_FOUND;
            default -> HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        };
    }

    private void handleCreate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CardRequestDto cardRequestDto = JsonUtils.readValue(req.getReader(), CardRequestDto.class);
        Card newCard = converter.toModel(cardRequestDto);
        Card savedCard = cardService.create(newCard);
        CardResponseDto cardResponseDto = converter.toDto(savedCard);
        resp.setContentType(CONTENT_TYPE);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        JsonUtils.writeValue(resp.getWriter(), cardResponseDto);
    }

    private void handleSetStatus(Long id, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CardRequestDto cardRequestDto = JsonUtils.readValue(req.getReader(), CardRequestDto.class);
        CardStatus newStatus = converter.toModel(cardRequestDto).getStatus();
        Card editedCard = cardService.changeStatus(id, newStatus);
        CardResponseDto cardResponseDto = converter.toDto(editedCard);
        resp.setContentType(CONTENT_TYPE);
        resp.setStatus(HttpServletResponse.SC_OK);
        JsonUtils.writeValue(resp.getWriter(), cardResponseDto);
    }

    private void handleDelete(Long id, HttpServletResponse resp) {
        cardService.delete(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private void sendError(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        resp.setContentType(CONTENT_TYPE);
        JsonUtils.writeValue(resp.getWriter(), new ErrorResponseDto(status, message));
    }
}
