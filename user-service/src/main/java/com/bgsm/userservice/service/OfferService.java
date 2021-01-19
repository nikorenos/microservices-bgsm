package com.bgsm.userservice.service;

import com.bgsm.userservice.exception.NotFoundException;
import com.bgsm.userservice.model.Item;
import com.bgsm.userservice.model.Offer;
import com.bgsm.userservice.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository repository;

    public Offer findById(Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("item not found, id: " + id));
    }

    public Offer save(Offer offer) {
        return repository.save(offer);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<Offer> getAll() {
        return repository.findAll();
    }

    public List<Offer> findByCategory(Long categoryId) {
        return repository.findAll().stream()
                .filter(offer -> offer.getItem().getCategory().getId().equals(categoryId))
                .collect(Collectors.toList());
    }

    public List<Offer> findByUserId(Long userId) {
        return getAll().stream()
                .filter(offer -> offer.getItem().getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public Offer findByItem(Item item) {
        return repository.findByItem(item).orElseThrow(() -> new NotFoundException("Offer with item " +
                item.getName() + " not found"));
    }
}
