package com.bgsm.userservice.gui.forms;

import com.bgsm.userservice.dto.ItemDto;
import com.bgsm.userservice.dto.OfferDto;
import com.bgsm.userservice.dto.OrderDto;
import com.bgsm.userservice.mapper.ItemMapper;
import com.bgsm.userservice.mapper.OfferMapper;
import com.bgsm.userservice.mapper.OrderMapper;
import com.bgsm.userservice.service.AppUserService;
import com.bgsm.userservice.service.ItemService;
import com.bgsm.userservice.service.OfferService;
import com.bgsm.userservice.service.OrderService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class OfferViewForm extends FormLayout {

    private static final String CELL_WIDTH = "30em";

    @Autowired
    public OfferViewForm(OfferMapper offerMapper, OfferService offerService,
                         ItemMapper itemMapper, ItemService itemService,
                         String offerId,
                         OrderService orderService, OrderMapper orderMapper,
                         AppUserService userService) {
        OfferDto offerDto = offerMapper.mapToOfferDto(offerService.findById(Long.valueOf(offerId)));
        ItemDto itemDto = itemMapper.mapToItemDto(itemService.findById(offerDto.getItemId()));

        VerticalLayout layout = new VerticalLayout();

        TextField name = new TextField();
        name.setValue(itemDto.getName());
        name.setLabel("Name");
        name.setReadOnly(true);
        name.setMinWidth(CELL_WIDTH);

        TextField description = new TextField();
        description.setValue(itemDto.getDescription());
        description.setLabel("Description");
        description.setReadOnly(true);
        description.setMinWidth(CELL_WIDTH);

        TextField minPlayers = new TextField();
        minPlayers.setValue(String.valueOf((int)itemDto.getMinPlayers()));
        minPlayers.setLabel("Min players");
        minPlayers.setReadOnly(true);
        minPlayers.setMinWidth(CELL_WIDTH);

        TextField maxPlayers = new TextField();
        maxPlayers.setValue(String.valueOf((int)itemDto.getMaxPlayers()));
        maxPlayers.setLabel("Max players");
        maxPlayers.setReadOnly(true);
        maxPlayers.setMinWidth(CELL_WIDTH);

        TextField category = new TextField();
        category.setValue(itemDto.getCategoryName());
        category.setLabel("Category");
        category.setReadOnly(true);
        category.setMinWidth(CELL_WIDTH);

        TextField username = new TextField();
        username.setValue(itemDto.getUserName());
        username.setLabel("User");
        username.setReadOnly(true);
        username.setMinWidth(CELL_WIDTH);

        DatePicker dateFrom = new DatePicker();
        dateFrom.setValue(offerDto.getDateFrom());
        dateFrom.setLabel("Date from");
        dateFrom.setReadOnly(true);
        dateFrom.setMinWidth(CELL_WIDTH);

        DatePicker dateTo = new DatePicker();
        dateTo.setValue(offerDto.getDateTo());
        dateTo.setLabel("Date to");
        dateTo.setReadOnly(true);
        dateTo.setMinWidth(CELL_WIDTH);

        TextField location = new TextField();
        location.setValue(offerDto.getLocation());
        location.setLabel("Location");
        location.setReadOnly(true);
        location.setMinWidth(CELL_WIDTH);

        TextField price = new TextField();
        price.setValue(String.valueOf(offerDto.getPrice()));
        price.setLabel("Price");
        price.setReadOnly(true);
        price.setMinWidth(CELL_WIDTH);

        Label infoLabel = new Label();

        Button createOrder = new Button("Order");
        createOrder.addClickListener(e -> {
            OrderDto returnedOrder = orderMapper
                    .mapToOrderDto(orderService
                            .createOrderFromOffer(Long.valueOf(offerId), getCurrentUserId(userService)));
            infoLabel.setText("Order id:  " + returnedOrder.getOfferId() + " created.");
        });

        layout.add(name, description, minPlayers, maxPlayers, category, username,
                dateFrom, dateTo, location, price, createOrder, infoLabel);
        add(layout);
    }

    private Long getCurrentUserId(AppUserService userService) {
        String username = getCurrentUsername();
        return userService.findByName(username).getId();
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
