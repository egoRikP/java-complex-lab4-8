package org.egorik;

import org.egorik.manager.FileManager;
import org.egorik.model.Product;
import org.egorik.model.Salad;
import org.egorik.repository.MemoryRepository;
import org.egorik.repository.Repository;
import org.egorik.service.ProductService;
import org.egorik.service.SaladService;

public class AppContext {
    public final Repository<Product> productMemoryRepository = new MemoryRepository<>();
    public final Repository<Salad> saladMemoryRepository = new MemoryRepository<>();

    public final ProductService productService = new ProductService(productMemoryRepository);
    public final SaladService saladService = new SaladService(saladMemoryRepository);

    public final FileManager fileManager = new FileManager();
}