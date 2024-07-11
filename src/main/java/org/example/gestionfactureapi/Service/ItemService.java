package org.example.gestionfactureapi.Service;

import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Item;
import org.example.gestionfactureapi.Repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    public Item save(Item i){
        return itemRepository.saveAndFlush(i);
    }
    public List<Item> saveAll(List<Item> items){
        return itemRepository.saveAllAndFlush(items);
    }
}
