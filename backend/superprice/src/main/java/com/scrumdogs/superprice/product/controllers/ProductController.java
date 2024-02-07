package com.scrumdogs.superprice.product.controllers;

import com.scrumdogs.superprice.product.Product;
import com.scrumdogs.superprice.product.ProductInventory;
import com.scrumdogs.superprice.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@CrossOrigin
@RequiredArgsConstructor // This also performs @Autowiring if only one constructor
@RestController
public class ProductController {
    final private ProductService productService;

    private final List<Emitter> emitterList = new CopyOnWriteArrayList<>();

    @GetMapping("v1/products")
    public Collection<Product> all() {
        return productService.getAllProducts();
    }

    @GetMapping(value = "/v1/products", params = "id")
    @ResponseBody
    public Product id(@RequestParam("id")  Long id) {
        return productService.getProductById(id).orElseThrow(
                () -> new ProductNotFoundException(id));
    }

    @GetMapping(value = "v1/products/inventory")
    @ResponseBody
    public Collection<ProductInventory> idInventory(@RequestParam("id")  Long id, @RequestParam(name = "loc", required = false)  Long postcode) {
        if (postcode != null) {
            return productService.getProductInventoryByIdAndPostcode(id, postcode).orElseThrow(
                    () -> new ProductNotFoundException("Could not find product with ID = " + id + " and postcode = " + postcode));
        } else {
            return productService.getProductInventoryById(id).orElseThrow(
                    () -> new ProductNotFoundException(id));
        }

    }
    @RequestMapping(value = "v1/products/inventory", params = "name", method = GET)
    @ResponseBody
    public Collection<ProductInventory> nameInventory(@RequestParam("name")  String name, @RequestParam(name = "loc", required = false)  Long postcode) {
        if (postcode != null) {
            return productService.getProductInventoryByNameAndPostcode(name, postcode).orElseThrow(
                    () -> new ProductNotFoundException("Could not find product with Name = " + name + " and postcode = " + postcode));
        } else {
            return productService.getProductInventoryByName(name).orElseThrow(
                    () -> new ProductNotFoundException("Could not find product with Name = " + name));
        }
    }

    @RequestMapping(value = "/v1/products", params = "name", method = GET)
    @ResponseBody
    public Collection<Product> name(@RequestParam("name")  String name) {
        return productService.getProductsByName(name).orElseThrow(
                () -> new ProductNotFoundException("Could not find product with Name = " + name));
    }

    @RequestMapping(value = "/v1/products", params = "category", method = GET)
    @ResponseBody
    public Collection<Product> category(@RequestParam("category")  Long category) {
        return productService.getProductsByCategory(category).orElseThrow(
                () -> new ProductNotFoundException("Could not find product with Category = " + category));
    }

    @GetMapping("/v1/products/popular")
    @ResponseBody
    public Collection<ProductInventory> popular(@RequestParam(name = "loc", required = false)  Long postcode) {
        return productService.getPopularProductsInventory(postcode).orElseThrow(
                () -> new ProductNotFoundException("No popular product inventory found"));
    }


    @GetMapping(value = "/v1/products/notifications", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamNotifications(@RequestParam(name = "timeout", required = false)  Long timeout, @RequestParam(name = "time", required = false)  LocalTime time) {
        SseEmitter emitter = new SseEmitter(timeout == null ? -1L : timeout);
        Emitter e = new Emitter(emitter, time);
        addEmitter(e);
        return emitter;
    }

    private void addEmitter(Emitter e) {
        emitterList.add(e);
        e.emitter().onCompletion(() -> emitterList.remove(e));
        e.emitter().onTimeout(() -> emitterList.remove(e));
    }

    @Scheduled(fixedRate = 30000, initialDelay = 30000)
    public void sendEvents() {
        for (Emitter emitter: emitterList) {
            try {
                LocalTime time = emitter.time() == null ? LocalTime.now() : emitter.time() ;
                emitter.emitter().send(SseEmitter.event()
                        .name("new inventory update")
                        .data(productService.getPriceChanges(time.minusSeconds(30), time))
                );
            } catch (Exception ex) {
                emitter.emitter().completeWithError(ex);
            }
        }
    }

}

record Emitter(SseEmitter emitter, LocalTime time) {}