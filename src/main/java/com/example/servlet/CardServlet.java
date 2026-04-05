package com.example.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.converter.CardConverter;
import com.example.dto.CardRequestDto;
import com.example.dto.CardResponseDto;
import com.example.dto.CardChangeStatusRequestDto;
import com.example.dto.ErrorResponseDto;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Card;
import com.example.exception.EntityNotFoundException;
import com.example.model.CardStatus;
import com.example.repository.DataBaseCardRepository;
import com.example.service.CardService;
import com.example.service.CardServiceImpl;
import com.example.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;

@WebServlet("/cards/*")
public class CardServlet extends HttpServlet {

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private CardService cardService;
    private CardConverter cardConverter;
    private Logger logger;

    @Override
    public void init() {
        this.cardService = new CardServiceImpl(new DataBaseCardRepository());
        this.cardConverter = new CardConverter();
        this.logger = Logger.getLogger(CardServlet.class.getName());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String method = req.getMethod();

            if ("PATCH".equalsIgnoreCase(method)) {
                doPatch(req, resp);
            } else {
                super.service(req, resp);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Request processing error: %s".formatted(e.getMessage()));
            handleErrorResponse(e, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
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
                handleChangeStatus(id, req, resp);
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

    private void handleErrorResponse(Exception e, HttpServletResponse resp) {
        String message = e.getMessage();
        int status = mapExceptionToStatus(e);
        sendErrorResponse(resp, status, message);
    }

    private int mapExceptionToStatus(Exception e) {
        return switch (e) {
            case BadRequestException _, IllegalArgumentException _ -> HttpServletResponse.SC_BAD_REQUEST;
            case EntityNotFoundException _, ResourceNotFoundException _ -> HttpServletResponse.SC_NOT_FOUND;
            default -> HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        };
    }

    private void sendErrorResponse(HttpServletResponse resp, int status, String message) {
        writeJsonResponse(resp, status, new ErrorResponseDto(status, message));
    }

    private void writeJsonResponse(HttpServletResponse resp, int status, Object dto) {
        try {
            resp.setContentType(CONTENT_TYPE);
            JsonUtils.writeValue(resp.getWriter(), dto);
            resp.setStatus(status);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error during json serialization: %s", e.getMessage());
        }
    }

    private String[] splitPathInfo(String pathInfo) {
        if (StringUtils.isBlank(pathInfo) || pathInfo.equals("/")) {
            return new String[0];
        }
        String normalized = pathInfo.replaceAll("^/|/$", "");
        return normalized.isEmpty() ? new String[0] : normalized.split("/");
    }

    private void handleGetAll(HttpServletResponse resp) {
        List<Card> allCards = cardService.getAll();
        List<CardResponseDto> cardResponseDto = cardConverter.toDtos(allCards);
        writeJsonResponse(resp, HttpServletResponse.SC_OK, cardResponseDto);
    }

    private void handleGetById(Long id, HttpServletResponse resp) {
        Card card = cardService.get(id);
        CardResponseDto cardResponseDto = cardConverter.toDto(card);
        writeJsonResponse(resp, HttpServletResponse.SC_OK, cardResponseDto);
    }

    private void handleCreate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CardRequestDto cardRequestDto = JsonUtils.readValue(req.getReader(), CardRequestDto.class);
        validateCreateRequest(cardRequestDto);
        Card newCard = cardConverter.toModel(cardRequestDto);
        Card savedCard = cardService.create(newCard);
        CardResponseDto cardResponseDto = cardConverter.toDto(savedCard);
        writeJsonResponse(resp, HttpServletResponse.SC_CREATED, cardResponseDto);
    }

    private void validateCreateRequest(CardRequestDto dto) {
        if (Objects.isNull(dto)) {
            throw new BadRequestException("Data is required for create");
        } else if (Objects.isNull(dto.getAccountId())) {
            throw new BadRequestException("Account ID is required for create");
        } else if (StringUtils.isBlank(dto.getHolderName())) {
            throw new BadRequestException(("Holder name is required for create"));
        }
    }

    private void handleChangeStatus(Long id, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CardChangeStatusRequestDto cardChangeStatusRequestDto
                = JsonUtils.readValue(req.getReader(), CardChangeStatusRequestDto.class);
        validateChangeStatusRequest(cardChangeStatusRequestDto);
        CardStatus newStatus = cardChangeStatusRequestDto.status();
        Card editedCard = cardService.changeStatus(id, newStatus);
        CardResponseDto cardResponseDto = cardConverter.toDto(editedCard);
        writeJsonResponse(resp, HttpServletResponse.SC_OK, cardResponseDto);
    }

    private void validateChangeStatusRequest(CardChangeStatusRequestDto dto) {
        if (Objects.isNull(dto)) {
            throw new BadRequestException("Data is required for change status");
        } else if (Objects.isNull(dto.status())) {
            throw new BadRequestException("Status is required for change status");
        }
    }

    private void handleDelete(Long id, HttpServletResponse resp) {
        cardService.delete(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
